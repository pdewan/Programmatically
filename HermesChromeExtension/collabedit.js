$(document).ready(getAndSendText);

function getAndSendText() {
    var text = $("#frame_the_input").contents().find("#textarea");
    if(text.length > 0) {
        var content = text.contents()[0].parentElement.value;
        chrome.runtime.sendMessage({editorContents: content});
    }
    setTimeout(getAndSendText, 1000);
}