/**
 * 
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 * 
 * USAGE:
 * 
 * var Tilisty = require('Tilisty');
 * 
 * Tilisty.start({
 * 	host : 'localhost' // change to your IP address, 'localhost' will only work on the iOS Simulator.
 * });
 * 
 * //I RECOMMEND USING THE VARIABLE NAME USED TO INSTANTIATE THE VIEW AS AN ID PROPERTY - THIS WILL MAKE 
 * //THE TILISTY APP AWARE OF YOUR VIEW's  VARIABLE NAME. - regardless you should use an ID to make it more readable.
 * 
 * Tilisty.registerView(<<ANY TIVIEW TYPE>>); 
 * 
 */

/**
 * Monitors a socket, its basically a timer 
 * @param {Object} sock
 */
function SocketMonitor(sock) {
	//socket to monitor.
	var socket = sock;
	var that = this;
	
	var _monitorInterval = null;
	
	var _observer = null;
	
	/**
	 * Quick call to both stop and start at once.
	 */
	that.reset = function(time) {
		that.stop();
		if(typeof(time) == 'number') {
			that.start(time);
		} else {
			that.start(11000);
		}
	};
	
	/**
	 * Stop the monitoring
	 */
	that.stop = function() {
		if(_monitorInterval != null) {
			clearInterval(_monitorInterval);
			_monitorInterval = null;
			var date = new Date();
			Ti.API.trace("Stopping Monitor Services " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
		}
	};
	
	/**
	 * Start the monitoring of the socket.
	 */	
	that.start = function(time) {
		Ti.API.trace("Starting Monitor Services");
		if(_monitorInterval != null) {
			clearInterval(_monitorInterval);
			_monitorInterval = null;
		}
		if(typeof(time) != 'number') {
			time = 11000;
		}
		
		_monitorInterval = setInterval(that.checkServices, time);
	};
	
	/**
	 * Check the services are ok.
	 */
	that.checkServices = function() {
		var date = new Date();
		Ti.API.info("No Data in 11 seconds - checking socket " + date.getHours() + ":" + date.getSeconds());
		if(_observer != null) {
			_observer();
		}
	};
	
	that.registerObserver = function(observer) {
		if(typeof(_observer) != "function") {
			_observer = observer;
		}
	};
}


/**
 * Handles the connection to the Style Server
 */
function TilistyServer(obj) {
	var that  = this;

	that.socket = null;
	
	that.monitor = new SocketMonitor(this);
	var _attempting = false;
	var _observer = obj.observer;
	var messageId = 0;
	
	that.fullMsg = "";
	
	/**
	 * Creates the socket connection
	 */
	function createSocket() {
		_attempting = true;
		//create the TCP object.
		that.socket = Ti.Network.Socket.createTCP({
			host : obj.host !== undefined ? obj.host : "localhost",
			port : 19266,
			connected : function(e) {
				Ti.API.info("Tilisty Connected!");
				_attempting = false;
				updateObserver({type: "connected"});
				Ti.Stream.pump(that.socket, that.onData, 500000, true);
			},
			error: function(e) {
				if(that.socket.getState() == Ti.Network.Socket.CONNECTED ) {
					that.socket.close();
				}
				_attempting = false;
				Ti.API.error("Tilisty connection failed");
			},
			closed : function(e) {
				_attempting = false;
				Ti.API.info("Tilisty Connection closed");
			}
		});
	}
	
	/**
	 * When data is received, it will break in to this function
	 */
	that.onData = function(e) {
		if ((e.type == "pump" || Ti.Platform.name== "android") && typeof(e.buffer) != "undefined" && e.errorState == 0) {
			var msg  = e.buffer.toString();
			that.fullMsg += msg;
			//sometimes the message comes in parts if it is too long to send at once. 
			//If this is the case we need to wait until the null byte or new line character comes through before updating or we will
			//probably have malformed JSON (incomplete), that.fullMsg records the message so far.
			if (msg.indexOf("\0") > -1 || msg.indexOf("\n") > -1) {
				if (_observer != null) {
					that.monitor.reset();
					parsePacket(that.fullMsg);	
				}
				//once null byte or new line is received, we reset the full message to nothing. - most of the time this will be straight away
				that.fullMsg = "";
			} else {
				Ti.API.warn("GOT PARTIAL PACKET: " + msg);
			}
		}
	};
	
	function parsePacket(pack) {
		_attempting = false;
		var packet = pack;		
		
		if (packet.indexOf("\n") > -1) {
			var packets = packet.split("\n");
		} else {
			var packets = [];
			packets.push(packet);
		}
		
		for (var p in packets) {
			if (packets[p] != "") {
				
				packet = packets[p].replace(/[\n\r]/g, '');

				if (typeof(packet) != "undefined" && packet.indexOf("{") > -1) {
					
					try {
						var obj = JSON.parse(packet);		
						updateObserver(obj);
					} catch(e) {
						Ti.API.error(e.message);
						Ti.API.error(packet);
						return false;
					}
					
				} else {
					if (packet == "PROBE") {
						Ti.API.trace("Got PROBE keep alive from cog");
						that.monitor.reset(11000);
					}
				}
			}
		}
		return true;
	}
	
	function updateObserver(pack) {
		try { 
			if(_observer != null) {
				_observer.update(pack);
			}
		} catch(e) {
			Ti.API.error("Packet:" + JSON.stringify(pack) + " was malformed");
		}
	}
	
	/**
	 * Connects to the socket server.
	 */
	that.connect = function() {
		if(that.socket == null || that.socket.getState() != Ti.Network.Socket.INITIALIZED) {
			if(that.socket != null && that.socket.getState() == Ti.Network.Socket.CONNECTED) {
				that.socket.close();
			}
			that.socket = null;
			delete that.socket;
			that.sessionId = '';
			createSocket();

		}
		that.socket.connect();
		//this interval monitors for keep alive, if nothing received in last 11 seconds than it must be dead.
		that.monitor.reset();
	};
	
	/**
	 * Reconnects to the server
	 */
	that.reconnect = function() {
		if(!_attempting) {
			if(that.socket != null) {
				try {
					if(that.socket.getState() == Ti.Network.Socket.CONNECTED) {
						that.socket.close();
					}
				} catch(e) {
					Ti.API.error(e.message);
				}
			}
			that.connect();
		}
	};
	
	/**
 	 * Send a message to the Cog socket server. 
 	 * @param String v - the message to be sent.
	 */
	that.writeMessage = function(v) {
		var buffer = Ti.createBuffer({value:v});
		Ti.Stream.write( that.socket, buffer, function(e) {
			Ti.API.info("sent data successfully");
		});
		if (v !== "\r\n") {
			that.writeMessage("\r\n");
		}
	};
	
	that.disconnect = function() {
		that.monitor.stop();
		if(that.socket.getState() == Ti.Network.Socket.CONNECTED) {
			that.socket.close();
		}
	};
	
	that.monitor.registerObserver(that.reconnect);
	that.monitor.stop();
}

/**
 * The main object to be used by applications as a module.
 * 
 */
function Tilisty() {
	var that = this;
	var msgId = 0;
	var tilistyServer = null;
	var views = [];
	var viewsHash = {};
	var messages = {};
	var msgQueue = [];
	
	/**
	 * Register the device with the Tilisty Server
	 */
	var registerDevice = function() {
		var deviceData = {
			type : "register_device",
			deviceId : Ti.Platform.id,
		};
		that.submitMessage(deviceData);
	};
	
	that.start = function(config) {
		config.observer = that;
		tilistyServer = new TilistyServer(config);
		tilistyServer.connect();
		registerDevice();
	};
	
	that.update = function(packet) {
		var origPacket = {};
		if(packet.hasOwnProperty("msgId")) {
			//this is a response not a change prop
			origPacket = messages[packet.msgId];
			if(typeof origPacket !== "undefined") {
				delete messages[packet.msgId];
			}
			
		} 
		
		var packetType = "";
		if(typeof origPacket !== "undefined" && origPacket.hasOwnProperty("type") && packet.type === "rsp") {
			packetType = origPacket.type;
		} else {
			//must be a change of view properties!!
			packetType = packet.type;
		}
		
		switch(packetType) {
			case "connected":
				_clearMsgQueue();
				Ti.App.fireEvent("tilisty:connected");
				break;
			case "register_device":
				Ti.App.fireEvent("tilisty:registered");
				break;
			case "register_view":
				Ti.App.fireEvent("tilisty:view_registered", {view: origPacket['view']});
				break;
			case "update_property":
				if(packet.hasOwnProperty("view") && packet.hasOwnProperty('properties')) {
					var theView = viewsHash[packet.view];
					for(var prop in packet.properties) {
						theView[prop] = packet.properties[prop];
					}
				}
				break;
		}
	};
	
	that.submitMessage = function(dataObj) {
		if(dataObj.hasOwnProperty("type")) {
			messages[msgId] = dataObj;
			dataObj.msgId = msgId;
			msgId += 1;
		} else {
			throw new Error("No type supplied with message to Tilisty Server");
		}
		if(tilistyServer.socket.getState() === Ti.Network.Socket.CONNECTED) {
			_clearMsgQueue();
			tilistyServer.writeMessage(JSON.stringify(dataObj));		
		} else {
			msgQueue.push(dataObj);
		}
	};
	
	var _clearMsgQueue = function() {
		var len = msgQueue.length;
		for(var i = 0; i < len; i++) {
			tilistyServer.writeMessage(JSON.stringify(msgQueue[i]));
		}
		msgQueue = [];
	};
	
	that.registerView = function(view) {
		var parentView = JSON.stringify(view);
		var theView = JSON.parse(parentView);
		if(typeof view['id'] !== 'undefined') {
			theView.id = view.id;
		} else {
			// theView.id = view.
			theView.id = Ti.Utils.sha1(JSON.stringify(view) + view.toString());
			theView.type = view.toString();
		}
		viewsHash[theView.id] = view;
		var getChildren = function(children) {
			var childrenParsed = [];
			for(var i = 0; i < children.length; i++) {
				var child = children[i];
				var theChild = JSON.parse(JSON.stringify(child));
				
				if(typeof child['id'] !== "undefined") {
					theChild.id = child.id;
				} else {
					theChild.id = Ti.Utils.sha1(JSON.stringify(child) + child.toString());
					theChild.type = child.toString();
				}
				
				if(child.children.length > 0) {
					theChild.children = getChildren(child.children);
				}
				
				viewsHash[theChild.id] = child;
				childrenParsed[childrenParsed.length] = theChild;
			}
			return childrenParsed;
		};
		
		theView.children = getChildren(view.children);
		views[views.length] = theView;	
		var viewMsg = {
			type : "register_view",
			view : theView
		};
		that.submitMessage(viewMsg);
	};
	
	
}

//we only want one instance of Tilisty per application. - this allows views to be added on the fly as they are initiated in code.
var tilisty = new Tilisty();

module.exports = tilisty; //export the only instance.