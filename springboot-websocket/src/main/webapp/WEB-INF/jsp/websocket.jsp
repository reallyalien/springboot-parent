<%--
  Created by IntelliJ IDEA.
  User: hello world
  Date: 2020/6/22
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
<script>
    var socket;

    function openSocket() {
        if (typeof(WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            console.log("您的浏览器支持WebSocket");
            //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
            //等同于socket = new WebSocket("ws://localhost:8080/xxxx/im/25");
            //var socketUrl="${request.contextPath}/im/"+$("#userId").val();
            var socketUrl = "http://localhost:8080/imserver/" + $("#userId").val();
            socketUrl = socketUrl.replace("https", "ws").replace("http", "ws");
            console.log(socketUrl);
            if (socket != null) {
                socket.close();
                socket = null;
            }
            socket = new WebSocket(socketUrl);
            //打开事件
            socket.onopen = function () {
                console.log("websocket已打开");
                //socket.send("这是来自客户端的消息" + location.href + new Date());
            };
            //获得消息事件
            socket.onmessage = function (msg) {
                console.log(msg.data);
                //发现消息进入    开始处理前端触发逻辑
            };
            //关闭事件
            socket.onclose = function () {
                console.log("websocket已关闭");
            };
            //发生了错误事件
            socket.onerror = function () {
                console.log("websocket发生了错误");
            }
        }
    }

    function sendMessage() {
        if (typeof(WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        } else {
            console.log("您的浏览器支持WebSocket");
            console.log('{"toUserId":"' + $("#toUserId").val() + '","contentText":"' + $("#contentText").val() + '"}');
            socket.send('{"toUserId":"' + $("#toUserId").val() + '","contentText":"' + $("#contentText").val() + '"}');
        }
    }
</script>
<body>
    <p>【userId】：
    <div><input id="userId" name="userId" type="text" value="10"></div>
    <p>【toUserId】：
    <div><input id="toUserId" name="toUserId" type="text" value="20"></div>
    <p>【contentText】：
    <div><input id="contentText" name="contentText" type="text" value="hello websocket"></div>
    <p>【操作】：
    <div><button onclick="openSocket()">开启socket</button></div>
    <p>【操作】：
    <div><button onclick="sendMessage()">发送消息</button></div>
</body>
</html>
