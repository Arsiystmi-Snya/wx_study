<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>微信扫码支付demo</title>
    <script src="../static/qrious.js"></script>
</head>
<body>
    <center>
        <img id="order">
    </center>

    <#--设置要生成二维码的链接-->
    <script>
        new QRious({
            element: document.getElementById("order"),
            level: "H",
            value: "${map.code_url}",
            size: 250,
            background: "white",
            foreground: "green"
        })
    </script>

    <#--<script>-->
    <#--    var int = self.setInterval("queryStatus()", 3000);-->

    <#--    function queryStatus() {-->
    <#--        $.get("/wx/queryOrder/${map.no}", function (data, status) {-->
    <#--            if (data == "支付中") {-->
    <#--                console.log("字符中");-->
    <#--            } else {-->
    <#--                clearInterval(int)-->
    <#--                window.location.href = "/wx/success"-->
    <#--            }-->
    <#--        })-->
    <#--    }-->
    <#--</script>-->

</body>
</html>