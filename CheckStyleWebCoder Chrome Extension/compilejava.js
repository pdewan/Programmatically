$(document).ready(getAndSendText);

function getAndSendText(){
    chrome.extension.getURL("c.ico");
    chrome.extension.getURL("codestyle.html");
    var code = $('.CodeMirror-code');

    var lines = code.children().children("pre");
    var editorText = "";
    var message = "";
    for(var i = 0; i < lines.length; i++) {
        editorText += lines[i].textContent + "\n";
    }
    if(editorText.length > 0) {
        chrome.runtime.sendMessage({editorContents: editorText});
    }

    chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
        var message = request.result;
        chrome.storage.sync.set({'result': message}, function(){
            console.log(message);
        });
    });

    chrome.storage.sync.set({'myLine': editorText}, function(){
        console.log(editorText);
    });

    $('.CodeMirror-code').click(function(){
        setTimeout(getAndSendText, 1000);
        $(this).after('<img src="chrome-extension://eobfchhnnibdopdeiomnkngimcckacfa/c.ico" alt="Code-Style Checker" height="20" width="20">');
        $(this).css({'display': 'block'});
        $('img').css({'position': 'absolute', 'bottom': '15px', 'right': '15px'});
        $('img').click(function(){
            window.open("chrome-extension://eobfchhnnibdopdeiomnkngimcckacfa/codestyle.html", 
                "Code Style Checker","height=600, width=950, menubar=no");
        });
    });

    $('.CodeMirror-code').keypress(function(){
        setTimeout(getAndSendText, 1000);
    });

}
