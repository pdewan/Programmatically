$(document).ready(getAndSendText);

function getAndSendText() {
    var content = $(".ace_content");
    var text = content.children(".ace_text-layer");
    var lines = text.children();
    var editorText = "";
    for(var i = 0; i < lines.length; i++) {
        editorText += lines[i].innerText + "\n";
    }
    chrome.runtime.sendMessage({editorContents: editorText});
    setTimeout(getAndSendText, 1000);
}
