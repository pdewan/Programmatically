window.onload = function(){
    var codestyle = document.getElementById("code-style");
    var result = document.getElementById("result");
    var value = "";
    var resultValue = "";
    chrome.storage.sync.get('myLine', function(data){
        codestyle.innerHTML = data.myLine;
        value = codestyle.innerHTML;
        chrome.storage.sync.set({'myLine': value}, function(){
            console.log(value);
        });
    });

    chrome.storage.sync.get('result', function(data){
        result.innerHTML = data.result;
        resultValue = result.innerHTML;
    });
}