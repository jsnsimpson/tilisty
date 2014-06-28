// this sets the background color of the master UIView (when there are no windows/tab groups on it)
Titanium.UI.setBackgroundColor('#000');

// create tab group
var tabGroup = Titanium.UI.createTabGroup();


//
// create base UI tab and root window
//
var win1 = Titanium.UI.createWindow({  
    title:'Tab 1',
    backgroundColor:'#fff',
    id: 'win1'
});
var tab1 = Titanium.UI.createTab({  
    icon:'KS_nav_views.png',
    title:'Tab 1',
    id:'tab2',
    window:win1
});

var label1 = Titanium.UI.createLabel({
	color:'#999',
	text:'I am Window 1',
	font:{fontSize:20,fontFamily:'Helvetica Neue'},
	textAlign:'center',
	width:'auto',
	id:'label1'
});

var img1 = Titanium.UI.createImageView({
	id:"img1",
	height : "100",
	width: "200",
	image :"none"
	
});
win1.add(img1);

win1.add(label1);

//
// create controls tab and root window
//
var win2 = Titanium.UI.createWindow({  
    title:'Tab 2',
    backgroundColor:'#fff',
    id : 'win2'
});
var tab2 = Titanium.UI.createTab({  
    icon:'KS_nav_ui.png',
    title:'Tab 2',
    window:win2,
    id: 'tab2'

});

var view1 = Titanium.UI.createView({
	height:'100%',
	width : '100%',
	backgroundColor:'black',
	id: 'view1'
});


var label2 = Titanium.UI.createLabel({
	color:'#999',
	text:'I am Window 6',
	font:{fontSize:40,fontFamily:'Arial'},
	textAlign:'center',
	width:'auto',
	left : 20,
	id : "label2"
});

view1.add(label2);


win2.add(view1);


var Tilisty = require("Tilisty");
Tilisty.start({
	host : 'localhost'
});
Tilisty.registerView(win2);
Tilisty.registerView(win1);



//  add tabs
//
tabGroup.addTab(tab1);  
tabGroup.addTab(tab2);  


// open tab group
tabGroup.open();
