<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
    <h2>首页</h2>

    <div>
        <button id="pay" onclick="wxPay()">支付</button>
    </div>

    <script>
        function wxPay() {
            window.location.href = "/order.ftl";
        }
    </script>
</body>
</html>