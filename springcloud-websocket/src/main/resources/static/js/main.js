/*function sendMessage() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else {
        console.log("您的浏览器支持WebSocket");
        console.log('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
        socket.send('{"toUserId":"'+$("#toUserId").val()+'","contentText":"'+$("#contentText").val()+'"}');
    }
}*/
$(document).ready(function() {
    var socket_zhangsan;
    var socket_lisi;
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else{
        console.log("您的浏览器支持WebSocket");
        if(socket_zhangsan!=null){
            socket_zhangsan.close();
            socket_zhangsan=null;
        }
        socket_zhangsan = new WebSocket("ws://127.0.0.1:8005/imserver/zhangsan");
        socket_lisi = new WebSocket("ws://127.0.0.1:8005/imserver/lisi");
        //打开事件
        socket_zhangsan.onopen = function() {
            console.log("zhangsan_websocket已打开");
        };
        socket_lisi.onopen = function() {
            console.log("lisi_websocket已打开");
        };
        //获得消息事件
        socket_zhangsan.onmessage = function(msg) {
            // zhangsan的 消息窗口显示lisi的消息
            var str = '<div><div class="message_person">lisi说:</div><div class="atalk"><span>' + msg.data +'</span></div></div>';
            $("#words1").append(str);
        };
        socket_lisi.onmessage = function(msg) {
            // lisi的 消息窗口显示zhangsan的消息
            var str = '<div><div class="message_person">zhangsan说:</div><div class="atalk"><span>' + msg.data +'</span></div></div>';
            $("#words").append(str);
        };
        //关闭事件
        socket_zhangsan.onclose = function() {
            console.log("zhangsan_websocket已关闭");
        };
        socket_lisi.onclose = function() {
            console.log("lisi_websocket已关闭");
        };
        //发生了错误事件
        socket_zhangsan.onerror = function() {
            console.log("zhangsan_websocket发生了错误");
        }
        socket_lisi.onerror = function() {
            console.log("lisi_websocket发生了错误");
        }
    }

    /**
     * lisi发送消息给zhangsan
     */
    $("#talksub").on('click', function () {
        //定义空字符串
        var str = "";
        var inputContent = $("#talkwords").val();
        if(inputContent == ""){
            alert("消息不能为空");
            return;
        }
        $("#talkwords").val("");
        str = '<div class="btalk"><span>' + inputContent +'</span></div>';
        $("#words").append(str);
        $.ajax({
            url : "http://127.0.0.1:8005/sendMsg",
            type : "POST",
            contentType: "application/json;charset=utf-8",
            data : JSON.stringify({'toUserId':'zhangsan','msg':inputContent}),
            dataType : "text",
            success : function(result) {
            },
            error:function(msg){
                console.log("发送zhangsan失败");
            }
        })
    });

    /**
     * zhangsan发送消息给lisi
     */
    $("#talksub1").on('click', function () {
        var str = "";
        var inputContent = $("#talkwords1").val();
        if(inputContent == ""){
            alert("消息不能为空");
            return;
        }
        $("#talkwords1").val("");
        str = '<div class="btalk"><span>' + inputContent +'</span></div>';
        $("#words1").append(str);
        $.ajax({
            url : "http://127.0.0.1:8005/sendMsg",
            type : "POST",
            contentType: "application/json;charset=utf-8",
            data : JSON.stringify({'toUserId':'lisi','msg':inputContent}),
            dataType : "text",
            success : function(result) {
            },
            error:function(msg){
                console.log("发送lisi失败");
            }
        })
    });

});