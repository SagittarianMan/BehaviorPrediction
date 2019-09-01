/**
 * Created by Sarketch on 2017/3/27.
 */
document.addEventListener('DOMContentLoaded', function() {

    var uid = "";
    //alert('Option-Page is opened!');
    //检测cookies[userName]
    //var userName = getCookie();
    getName();//显示旧昵称;判断是否登录

    //测试div的显示和隐藏
    var test_btn = document.getElementById("test_btn");//测试按钮****************************************************************
    test_btn.addEventListener("click", test);
    //新用户登录
    var login_btn = document.getElementById("login_submit");//注册
    login_btn.addEventListener("click",submit)

    var table = document.getElementById("modify");//table1修改昵称
    table.setAttribute("hidden", "true");
    var addinfo = document.getElementById("addinfo");//table2完善信息
    addinfo.setAttribute("hidden", "true");
    var mod_btn = document.getElementById("modify_btn");//修改昵称
    mod_btn.addEventListener("click", function () {
        //alert("click11");
        table.hidden = false;
    })
    var ok_btn = document.getElementById("ok_btn");
    ok_btn.addEventListener("click", function () {
        //确认修改昵称
        var old_name = document.getElementById("old_name").innerText;
        var new_name = document.getElementById("new_name").value;
        if (old_name == new_name) {
            alert("要修改，请输入不一样的名字！");
        }
        else {
            modify_name(old_name, new_name);
        }
    })
    var cc_btn = document.getElementById("cancel_btn");//取消修改昵称
    cc_btn.addEventListener("click", function () {
        table.hidden = true;
    })
    var add_btn = document.getElementById("addinfo_btn");//完善信息
    add_btn.addEventListener("click", fc_addinfo)
    var ok2_btn = document.getElementById("ok2_btn");//提交信息QW
    ok2_btn.addEventListener("click", submitQW);

    var cancel2_btn = document.getElementById("cancel2_btn");//取消完善信息
    cancel2_btn.addEventListener("click", function () {
        addinfo.hidden = true;
    })
})
//*********************    自定义方法
//测试方法
function test() {
    var login = document.getElementById("login");
    if(login.style.display=="none"){
        login.style.display="block";
    }
    else{
        login.style.display="none";
    }
}
function test2() {
    alert("test");
    return;
    alert("testee");
}
function test3() {
    var reg=/^[a-zA-Z][a-zA-Z\d_]{5,}$/;//验证微信号格式
    var str='aa22';
    alert(reg.test(str));
}

// function test4() {
//     var text={"qq":"997558422","wechat":"sss"};
//     var json = {
//         "123":"value",//这个也只能用json["123"]读取
//         "cc":"value",
//         "animal":{ //对象结构 json对象
//             "type1":"dog",
//             "type2":"cat"
//         },
//
//         "people" : [//数组结构 json对象
//             {"id":"1","name":"方世玉"},
//             {"id":"2","name":"张君宝"}
//         ]
//     }
//     json = JSON.stringify(text);
//     alert(json);
//     // JSON.parse(text,function (k,v) {
//     //     alert(k+":"+v);
//     // })
// }
//点击完善信息
function fc_addinfo() {
    var  addinfo = document.getElementById("addinfo");//table2完善信息
    addinfo.hidden=false;
    //**询问服务器该用户是否已提交社交信息
    //   **先获取cookie中的用户名或用户ID
    chrome.cookies.get({
        url:'http://localhost/',
        name:'userId'
    },function (cookie) {
        uid = cookie.value;
        //**根据uid向服务器请求qq&wechat
        requestQW(uid);
    })
}
//点击提交QW
function submitQW() {
    //验证输入
    var wechat = document.getElementById("wechat");
    var  qq = document.getElementById("qq");
    wechat=wechat.value;
    qq=qq.value;
    var Regx = /^[a-zA-Z][a-zA-Z\d_]{5,16}$/;
    if(wechat.toString()!=""  &&  !Regx.test(wechat.toString())){
        alert("请输入正确的微信号！（检查是否有空格或特殊字符）");
        return;
    }
    Regx = /^[\d]{8,10}$/;
    if(qq.toString()!=""   &&   !Regx.test(qq.toString())){
        alert("请输入正确的QQ号！（检查是否有空格或特殊字符）");
        return;
    }
    alert("qq:"+qq+"\nwechat:"+wechat);
    if(qq==""&&wechat==""){
        alert("请填写至少一个信息！");
        return;
    }
    console.log("wechat:"+wechat);
    console.log("qq:"+qq);
    console.log("uid:"+uid);
    //更新数据库表
    sendInfo(qq,wechat,uid);
    //POST请求
}
//根据uid向服务器请求qq&wechat
function requestQW(uid) {
    var url = domain+"/FindX/GetQW?user_id="+uid;
    var request = new XMLHttpRequest();
    request.open("GET",url);
    var result="";
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){
            result = request.responseText;
            // result = request.responseText;//{"qq":"997558422","wechat":"sss"}
            // result = JSON.parse(result);
           var qq="";
           var wechat="";
            result = JSON.parse(result,function (k,v) {
                if(k=="qq"){
                    qq = v;
                }
                if(k=="wechat"){
                    wechat = v;
                }
            });
            var qq_box = document.getElementById("qq");
            var we_box=document.getElementById("wechat");
            if(qq!=""){
                qq_box.value=qq;
            }
            if(wechat!=""){
                we_box.value = wechat;
            }
           // alert("qq:"+qq);
           // alert("wechat:"+wechat);
        }
    }
    request.send(null);
}
//验证用户名
function  validate(name) {
    //判断数据库中是否有同名
    var request = new XMLHttpRequest();
    var url = domain+"/FindX/Login?name="+name;
    request.open("GET",url);
    var result="";
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){
            //获得响应的类型
            var type = request.getResponseHeader("content-type");
            console.log("validate:content-type:"+type);
            result = request.responseText;
            return judge(result,name);
        }
    }
    request.send(null);
}
function  judge(result,name) {
    if(result == "0"){
        alert("换个名字！");
    }
    else {
        alert("新用户注册成功！");
        store_cookie(name,result);
        var login=document.getElementById("login");
        login.style.display="none";
    }
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
//检查cookie
function getName() {
    chrome.cookies.get({
        url:'http://localhost/',
        name:'userName'
    },function (cookie) {
        if(cookie){
            var login=document.getElementById("login");
            login.style.display="none";
            var old_name = document.getElementById("old_name");
            old_name.innerText = decodeURI(cookie.value);
            //alert(cookie.value);
        }
        else{
            alert("请先登录！");
            document.getElementById("modify_btn").setAttribute("disabled","true");
            document.getElementById("addinfo_btn").setAttribute("disabled","true");
            document.getElementById("login").style.display="block";
        }
    })
}
//完善用户社交信息
function sendInfo(qq,wechat,uid) {
    var request = new XMLHttpRequest();
    var url = domain+"/FindX/GetQW?qq="+qq+"&wechat="+wechat+"&user_id="+uid;
    request.open("POST",url);
    var result="";
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){
            result = request.responseText;
            if(result==1){
                alert("提交成功！");
                var addinfo_div = document.getElementById("addinfo");
                addinfo_div.hidden = true;
            }
            else {
                alert("发生错误！"+result);
            }
        }
    }
    request.send();
}
//修改昵称
function modify_name(old_name,new_name) {
    var request = new XMLHttpRequest();
    var url = domain+"/FindX/Modify?old_name="+old_name+"&new_name="+new_name;
    request.open("GET",url);
    request.onreadystatechange = function(){
        if(request.readyState == 4 && request.status == 200){

            //获得响应的类型
            var type = request.getResponseHeader("content-type");
            console.log("validate:content-type:"+type);
            var result = request.responseText;
            if(result=="1"){
                alert("修改成功！");
                var old_name = document.getElementById("old_name");
                old_name.innerText = new_name;
                var new_n = document.getElementById("new_name");
                new_n.value = "";
                var expirationDate = new Date(2017,11,31).getTime();
                chrome.cookies.set({
                    url:'http://localhost/',
                    name:'userName',
                    value:encodeURI(new_name),
                    expirationDate:expirationDate
                },function (cookie) {
                    console.log(cookie.name);
                    console.log(cookie.value);
                })
            }
            else{
                alert(result);
            }
        }
    }
    request.send();
}
//注册
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
    var url = domain + "/FindX/Register?name="+name+"&psd="+psd;
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