<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>微信扫码支付demo</title>
    <script src="/static/qrcode.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.js"></script>
</head>
<body>
    <center>
        <div id="container"></div><br>

        <#--<button id="type" onclick="querystatus()" value="提交">点击</button>-->
    </center>

    <#--设置要生成二维码的链接-->
    <script>
        new QRCode(document.getElementById("container"), "${map.code_url}");
        <#--new QRCode({-->
        <#--    element: document.getElementById("container"),-->
        <#--    level: "H",-->
        <#--    value: "${map.code_url}",-->
        <#--    size: 250,-->
        <#--    background: "white",-->
        <#--    foreground: "green"-->
        <#--})-->
    </script>


    <#--<script type="text/javascript">-->
    <#--    var int = self.setInterval("querystatus()", 3000);-->

    <#--    function querystatus() {-->
    <#--        $.get("http://localhost:8088/wx/queryOrder/${map.outTradeNo}", function (data) {-->
    <#--        &lt;#&ndash;$.get("/wx/test/${map.outTradeNo}", function (data, status) {&ndash;&gt;-->
    <#--            if (data === "支付中") {-->
    <#--                console.log("支付中");-->
    <#--            } else {-->
    <#--                // clearInterval(int)-->
    <#--                console.log("支付成功");-->
    <#--                window.location.href = "/wx/success";-->
    <#--            }-->
    <#--        })-->
    <#--    }-->
    <#--</script>-->


    <#--<script>-->
    <#--    // 1. 声明客户端连接 EventSource-->
    <#--    var eventSource = new EventSource("http://8088//wx/queryOrder/${map.out_trade_no}");-->

    <#--    // 2. 默认的监听器-->
    <#--    eventSource.addEventListener("message", function (evt) {-->
    <#--        var data = evt.data;-->
    <#--        // 3. 发送异步请求，查询订单数据-->
    <#--        $.get("/wx/queryOrder/${map.out_trade_no}", function (data, status) {-->
    <#--            if (data == ${map.out_trade_no}) {-->

    <#--            }-->
    <#--            if (data == "支付中") {-->
    <#--                console.log("支付中");-->
    <#--            } else {-->
    <#--                clearInterval(int)-->
    <#--                window.location.href = "/wx/success"-->
    <#--            }-->
    <#--        })-->
    <#--        var json = JSON.parse(data);-->
    <#--        console.log(json);-->
    <#--    })-->
    <#--</script>-->

</body>
</html>