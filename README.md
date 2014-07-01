tilisty
=======

Requirements: Java VM v7.0 or above. The application will prompt an update if you have Java 6 or below. 

TItanium LIve STYle - edit your titanium styles live on screen to save the time recompiling to see visible changes.

Usage Instructions:  
- Pull this repository.
- Drop js/Tilisty.js in to your titanium application and implement code as in the examples below.
- On Mac you can launch the java Application with the Jar Launcher app
- running ```java -jar tilisty.jar``` from command line
- Ensure you have java version 1.7 (VM 7.0+) installed, type ```java -version``` in terminal
- You can download JRE 7.0 from here: [Java SE 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

Using Tilisty in your Titanium App: 

Tilisty is a singleton module, meaning there is only one instance of it used in your application. When you register a view with Tilisty it will only be added to the same instance as a view you registered from anywhere else in your application. 

BE SURE TO GIVE ALL REGISTERED VIEWS AN ID (BEST TO USE THE VARIABLE NAME USED IN YOUR APPLICATION).

To get started do this in your bootstrap (commonly app.js)
```
var Tilisty = require('Tilisty');

Tilisty.start({
  host : 'localhost' // change to your IP address, 'localhost' will only work on the iOS Simulator.
});

```

Then from anywhere else in your application when you have a view you wish to style:
 (example, replace 'tiView' with your view and replace id with your variable name)
```
var tiView = Ti.UI.createView({
  id : 'tiView', //this id shows up in tilisty 
  width : 120, // All following properties will be editable in the Tilisty UI
  height : 120, 
  left : 10,
  top : 10,
  backgroundColor : 'white'
});

var Tilisty = require('Tilisty');

Tilisty.registerView(tiView); 

```

There is an example app in this repository under the demo directory.
