/**
 * Created by Sarketch on 2017/6/5.
 */
document.addEventListener('DOMContentLoaded', function() {
    getCookie();
    var btn_submit = document.getElementById("btn");//登录
    btn_submit.addEventListener("click",submit);
    var userid="";
    chrome.cookies.get({
        url: 'http://localhost/',
        name: 'userId'
    },function (cookie) {
        userid = cookie.value;
        if(userid==""){
            alert("请先登录！")
        }
        getRecUsers(userid);
        // getRecWebs(userid);
    })

});

function getRecUsers(uid) {
    var request = new XMLHttpRequest();
    var url  = "http://localhost:8888/search?flag=1&uid="+uid;
    request.open("GET",url);
    // alert(url)
    request.onreadystatechange = function () {
        if(request.readyState == 4 && request.status == 200){
            var recUsers = request.responseText;
            recUsers = recUsers.replace(/'/g,'"');
            console.log(recUsers);

            buidRecUsersDOM(recUsers);
            getRecWebs(uid)
        }
    }
    request.send();
}

function getRecWebs(uid) {
    var request = new XMLHttpRequest();
    var url  = "http://localhost:8888/search?flag=2&uid="+uid;
    request.open("GET",url);
    // alert(url)
    request.onreadystatechange = function () {
        if(request.readyState == 4 && request.status == 200){
            // alert('ok')
            var recWebs = request.responseText;
            recWebs=recWebs.replace(/'/g,'"');
            console.log(recWebs);
            buildRecWebsDOM(recWebs);
        }
    }
    request.send();
}

function buidRecUsersDOM(recUsers) {
    var ru = document.getElementById("rec_users");
    recUsers = JSON.parse(recUsers);
    var ul = document.createElement('ul');
    ru.appendChild(ul);
    for(var user in recUsers){
        console.log(user);
        var user_name=recUsers[user].name;
        console.log(user_name);
        var user_a = document.createElement('a')
        user_a.href='#';
        user_a.innerText=recUsers[user].name;

        ul.appendChild(user_a);
    }
}

function buildRecWebsDOM(recWebs) {
    var rw = document.getElementById("rec_webs");
    recWebs = JSON.parse(recWebs);
    var ul = document.createElement('ul');
    rw.appendChild(ul);
    for(var web in recWebs){
        var a = document.createElement('a');
        // console.log(recWebs[web])
        var url=recWebs[web].url;
        var title = recWebs[web].title;
        console.log('url:'+url);
        a.href = url;
        a.appendChild(document.createTextNode(a.href));
        a.addEventListener('click', onAnchorClick);

        var a_title = document.createElement('h4');
        a_title.innerHTML = title;

        var li = document.createElement('li');
        li.appendChild(a_title);
        li.appendChild(a);

        ul.appendChild(li);
    }
}

function onAnchorClick(event) {
    chrome.tabs.create({
        selected: true,
        url: event.srcElement.href
    });
    return false;
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
            // getHistory();
        }
        else
        {
            //隐藏右侧用户信息
            document.getElementById('user').setAttribute('hidden',true);
            alert('请登录！');
        }
    })
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

//登陆
function  submit() {
    var name = document.getElementById("input");
    var psd    = document.getElementById("psd");
    psd = psd.value;
    name = name.value;
    validate(name,psd);
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