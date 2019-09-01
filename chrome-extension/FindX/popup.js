document.addEventListener('DOMContentLoaded', function() {
    //alert('popup.js');
    getCookie();

    var btn_submit = document.getElementById("btn");//登录
    btn_submit.addEventListener("click",submit);
    var btn_del = document.getElementById("delete_cookie");
    btn_del.addEventListener("click",delete_cookie);
    var refresh_btn = document.getElementById("refresh");
    refresh_btn.addEventListener("click",refresh);
    //soap();
    //getHistory();
});

//刷新，可以再次执行POST请求
function refresh() {
    location.reload();
}

//删除cookie
function  delete_cookie() {
    chrome.cookies.remove({
        url:"http://localhost/",
        name:"userName"
    },function () {
        location.reload();
    })
}
//验证登录
function  validate(name,psd) {
    //验证登录
    var request = new XMLHttpRequest();
    var url = domain+"/FindX/Login?name="+name+"&psd="+psd;
    request.open("GET",url);
    var result="";
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){
            result = request.responseText;
            if(result=="0"){
                alert("登陆失败！");
            }
            else{
                store_cookie(name,result);
                alert("登录成功！");
            }
        }
    }
    request.send(null);
}
//登陆
function  submit() {
    var name = document.getElementById("input");
    var psd    = document.getElementById("psd");
    psd = psd.value;
    name = name.value;
    validate(name,psd);
}

//保存cookies
function store_cookie(name,userId) {
    var expirationDate = new Date(2017,11,31).getTime();
    alert("欢迎登陆！"+name);
    chrome.cookies.set({
        'url':'http://localhost/',
        'name':'userId',
        'value':userId,
        'secure':false,
        'httpOnly':false,
        "expirationDate":expirationDate
    });
    chrome.cookies.set({
        'url':'http://localhost/',
        'name':'userName',
        'value':encodeURI(name),
        'secure':false,
        'httpOnly':false,
        "expirationDate":expirationDate
    },function () {
        //刷新
        location.reload();
    });
}
function getHistory(){
  var microsecondsPerHour   = 1000 * 60 * 60;
  var microsecondsPerDay    = 1000 * 60 * 60 * 24;
  var microsecondsPerWeek   = microsecondsPerDay * 7;
  var microsecondsPerMonth  = microsecondsPerDay * 30;

  var now         = (new Date).getTime();
  var oneDayAgo   = (new Date).getTime() - microsecondsPerDay;
  var oneWeekAgo  = (new Date).getTime() - microsecondsPerWeek;
  var oneMonthAgo = (new Date).getTime() - microsecondsPerMonth;
  var sixMonthAgo = (new Date).getTime() - microsecondsPerMonth*6;
  var oneYearAgo  = (new Date).getTime() - microsecondsPerDay*365;
  var startTime   = sixMonthAgo;
  //想从3月1号开始
  startTime = new Date(2017,2,1).getTime();
  
  chrome.history.search({
      'text': '',              // Return every history item....
      'startTime': startTime,  // that was accessed less than one week ago.
      'maxResults':1000
    },
    function(historyItems) {
      //alert(historyItems.length);
      // For each history item, get details on all visits.
      //for (var i = 0; i < historyItems.length; ++i) {
        //var url = historyItems[i].url;
        //alert(url);        
      //}
      buildPopupDom('historyList',historyItems,startTime);      
   	  getRequest(historyItems);
    });
}
// Given an array of URLs, build a DOM list of those URLs in the
// browser action popup.
function buildPopupDom(divName, data,startTime) {  
  var popupDiv = document.getElementById(divName);

  var h2 = document.createElement('h2');
  var count = data.length;
  startTime = (new Date(startTime)).toLocaleString();
  h2.innerHTML = '从'+startTime+'开始'+'共有'+count+'条记录';
  popupDiv.appendChild(h2);

  var ul = document.createElement('ul');
  popupDiv.appendChild(ul);

  for (var i = 0; i < count; ++i) {
    var a = document.createElement('a');
    a.href = data[i].url;
    a.appendChild(document.createTextNode(a.href));
    a.addEventListener('click', onAnchorClick);

    var a_title = document.createElement('h4');
    a_title.innerHTML = (i+1)+'. '+data[i].title;

    var lastVisitTime = data[i].lastVisitTime; 
    lastVisitTime = (new Date(lastVisitTime)).toLocaleString();
    var time = document.createElement('div');
    time.innerHTML = '最后访问时间:'+lastVisitTime+'\t'+'访问次数:'+data[i].visitCount;

    var li = document.createElement('li');
    li.appendChild(a_title);
    li.appendChild(a);
    li.appendChild(time);

    ul.appendChild(li);

  }
}

// Event listner for clicks on links in a browser action popup.
// Open the link in a new tab of the current window.
function onAnchorClick(event) {
  chrome.tabs.create({
    selected: true,
    url: event.srcElement.href
  });
  return false;
}

//POST历史记录
function getRequest(data){
	//JSON编码
  var json_data = JSON.stringify(data);

  var request = new XMLHttpRequest();
  //发送userName,history,userId
    //var userName;
    //var userId;
    chrome.cookies.getAll({
        url: 'http://localhost/'
    },function (cookies) {
        console.log(cookies);
        var userName="";
        var userId="";
        for (var i in cookies){
            console.log("ccc:"+cookies[i]);
            // console.log(cookie.name);
            // console.log(cookie.value);
            if(cookies[i].name=="userName"){
                userName = decodeURI(cookies[i].value);
            }
            else if(cookies[i].name=="userId"){
                userId = cookies[i].value;
            }
        }
        var url = domain+"/FindX/Login?userName="+userName+"&userId="+userId;
        url=encodeURI(url);
        console.log(url);
        request.open("POST",url);
        //alert(url);
        //显示responseText
        //var contt = document.getElementById('responseText');

        request.onreadystatechange = function(){
            if(request.readyState == 4 && request.status == 200){
                //获得响应的类型
                var type = request.getResponseHeader("content-type");
                console.log("GetRequest:content-type:"+type);
                console.log("GetRequest:responseText1:"+request.responseText);
                //contt.innerText = request.responseText;
            }
            else{
                //alert(request.status);
                console.log("GetRequest:responseText2:"+request.responseText);
            }
        }
        request.setRequestHeader("Content-Type","application/json;charset=UTF-8");
        //json_data =encodeURI(json_data);
        request.send(json_data);
    })
  //var userId=getUserId();
  //console.log("userName:"+userName);
   // console.log("userId:"+userId);

}


//GetCookie
function getCookie() {
    chrome.cookies.get({
        url: 'http://localhost/',
        name: 'userName'
    },function (cookie) {
        if(cookie){
            //显示右侧用户信息
            document.getElementById('user').setAttribute('visibility',"visible");
            console.log("getCookie:"+cookie.value);
            document.getElementById('option').addEventListener('click',onAnchorClick);
            var input_div=document.getElementById('input_div');
            input_div.setAttribute('hidden',"true");
            var userName = decodeURI(cookie.value);
            var hello = getHello();
            document.getElementById('user_name').innerText = hello+userName;
            getHistory();
        }
        else
        {
            //隐藏右侧用户信息
            document.getElementById('user').setAttribute('hidden',true);
            alert('请登录！');
        }
    })
}
//GetCookie--userName&userId
function getUserName() {
    var userName='';
    chrome.cookies.get({
        url: 'http://localhost/',
        name: 'userName'
    }, function (cookie) {
        userName=decodeURI(cookie.value);
    });
    console.log("getUserName:"+userName);
    return userName;
}
function getUserId() {
    var userId='';
    chrome.cookies.get({
        url: 'http://localhost/',
        name: 'userId'
    },function (cookie) {
        userId=cookie.value;
    });
    console.log("getUserId:"+userId);
    return userId;
}
//GetHello
function getHello() {
    var time = new Date();
    var hour = time.getHours();
    if(hour<12)
        return "上午好！";
    else if(hour<18)
        return "下午好！";
    else
        return "晚上好！";
}