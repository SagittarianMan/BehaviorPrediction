var domain="";
domain="http://localhost:8080";
//domain = "http://192.168.253.1:8080";
//domain = "http://172.30.129.21:8080";
// domain="http://edison-zsh.xicp.net";
domain="http://172.23.206.189:8080";

document.addEventListener('DOMContentLoaded', function() {
  alert('FindX is reloaded!');
  //检测cookies[userName]
  //var userName = getCookie();
});
//
// chrome.tabs.onCreated.addListener(function(tab){
//   //var url = chrome.tabs.getCurrent(function(tab){
//   //  return tab.url;
//   //});
//   console.log("标签页更新");
//   console.log("您正在访问："+tab.url);
// });

chrome.history.onVisited.addListener(function(historyItem) {
    //alert(111);
    console.log(111);
    console.log(JSON.stringify(historyItem));
    console.log(historyItem.title);
})

