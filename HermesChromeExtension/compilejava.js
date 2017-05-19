debugger;
$(document).ready(getAndSendText);

function getAndSendText() {
    var code = $(".CodeMirror-code");
    var lines = code.children().children("pre");
    var editorText = "";
    for(var i = 0; i < lines.length; i++) {
        editorText += lines[i].textContent + "\n";
    }
    if(editorText.length > 0) {
        chrome.runtime.sendMessage({editorContents: editorText});
    }
    setTimeout(getAndSendText, 1000);
}