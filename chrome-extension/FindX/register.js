/**
 * Created by Sarketch on 2017/4/25.
 */
document.addEventListener('DOMContentLoaded', function() {
    var btn_submit = document.getElementById("submit");//登录
    btn_submit.addEventListener("click",submit);
});

function submit() {
    var name = document.getElementById("user_name").value;
    var psd = document.getElementById("psd").value;
    var rpsd = document.getElementById("rpsd").value;
    if(rpsd != psd){
        alert("两次密码不一致！");
        return;
    }

    if(name.toString().length>20){
        alert("名字太长了！");
        return;
    }
    else{
        register(name,psd);
    }
}

function register(name,psd) {
    var request = new XMLHttpRequest();
    var url = domain + "/Register?name="+name+"&psd="+psd;
    request.open("GET",url);
    request.onreadystatechange = function () {
        if(request.readyState==4 && request.status==200){
            var rs = request.responseText;
            if(rs=="-1"){
                alert("用户名已存在！"+rs);
            }
            else{
                alert("注册成功！"+rs);
                store_cookie(name,rs);
            }
        }
    }
    request.send();
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
        window.location="popup.html";
    });
}


