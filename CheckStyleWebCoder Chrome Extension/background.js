var WEBSOCKET_URL = "ws://classroom1.cs.unc.edu:7070/ws/";
//var WEBSOCKET_URL = "ws://dewan1.cs.unc.edu:7070/ws/";
//var WEBSOCKET_URL = "ws://localhost:7070/ws";
//var WEBSOCKET_URL = "ws://152.2.130.185:7070/ws/";
//var instructor = "admin@192.168.214.128";
//var instructor = "admin@152.2.130.185";
var instructor = "instructor0@classroom1.cs.unc.edu";


var connection = null;
var connectionJID = null;

connection = new Strophe.Connection(WEBSOCKET_URL, {protocol: "ws"});

//connection.rawInput = rawInput;
//connection.rawOutput = rawOutput;

chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
    var messageBody = {
        "from": connectionJID,
        "editorContents": request.editorContents,
        "tags": ["EditorContents"]
    }
	//var messageBodyStr = "0.true." + JSON.stringify(messageBody);

    var messageBodyStr = JSON.stringify(messageBody);
    var msg = new Strophe.Builder('message', {to:instructor, from: connectionJID}).c("body").t(messageBodyStr);
    connection.send(msg);
});

function getPopupView() {
    var popupUrl = chrome.extension.getURL("popup.html");
    var views = chrome.extension.getViews();
    for(var i = 0; i < views.length; i++) {
        if(views[i].location.href == popupUrl) {
            return views[i];
        }
    }
}

function rawInput(data)
{
    log('RECV: ' + data);
}

function rawOutput(data)
{
    log('SENT: ' + data);
}

function onConnect(status)
{
    var popup = getPopupView();
    if (status == Strophe.Status.CONNECTING) {
       console.log('Connecting.');
    } else if (status == Strophe.Status.CONNFAIL) {
       console.log('Failed to connect.');
       popup.$('#connect').get(0).value = 'Connect';
    } else if (status == Strophe.Status.DISCONNECTING) {
       console.log('Disconnecting.');
    } else if (status == Strophe.Status.DISCONNECTED) {
       console.log('Disconnected.');
       popup.$('#connect').get(0).value = 'Connect';
    } else if (status == Strophe.Status.CONNECTED) {
       console.log('Connected.');
       connection.addHandler(handleMessage, null, 'message', null,
                                null, null, {matchBare: true});
       connection.send($pres());
       
    }
}

function handleMessage(msg) {
    var msgStr = msg.children[0].innerHTML;
 //   var period1 = msgStr.indexOf('.');
 //   var period2 = msgStr.indexOf('.', period1 + 1);
 //   var msgObj = JSON.parse(msgStr.substr(period2 + 1));
	var msgObj = JSON.parse(msgStr);

    console.log("Status: " + msgObj['result']);
    sendToContent(msgObj['result']);
    return true;
}

function sendToContent(msg){ 
    chrome.tabs.query({active: true, currentWindow: true}, function(tabs){
        chrome.tabs.sendMessage(tabs[0].id, {result: msg}, function(response) {});  
    });
}