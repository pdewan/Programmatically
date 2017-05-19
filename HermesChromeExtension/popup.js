$(document).ready(function () {
    $('#connect').bind('click', function () {
        var background = chrome.extension.getBackgroundPage();
        var button = $('#connect').get(0);
        if (button.value == 'Connect') {
            button.value = 'Disconnect';
            background.connectionJID = $('#jid').get(0).value;
            background.connection.connect(background.connectionJID,
                    $('#pass').get(0).value,
                    background.onConnect);
        } else {
            button.value = 'Connect';
            background.connection.disconnect();
        }
    });
});
