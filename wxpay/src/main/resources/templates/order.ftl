<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户下单</title>
    <script src="../static/vuejs-2.5.16.js"></script>
</head>
<body>
    <div id="app">
        <h3>商户下单</h3>
        <form action="#" method="post" onsubmit="return false">
            <label for="partener">商户id：</label>
            <input type="text" name="partener" id="partener" v-model="order.partener" placeholder="请输入商户id"><br>
            <label for="appid">appid：</label>
            <input type="text" name="appid" id="appid" v-model="appid" placeholder="请输入appid"><br>
            <label for="partenrekey">商户的key：</label>
            <input type="text" name="partenrekey" id="partenrekey" v-model="partenrekey" placeholder="请输入商户的key"><br>
            <label for="notifyurl">回调地址：</label>
            <input type="text" name="notifyurl" id="notifyurl" v-model="notifyurl" placeholder="请输入回调地址"><br>
            <label for="price">金额：</label>
            <input type="text" name="price" id="price" v-model="totalFee" placeholder="请输入金额：xxx元"><br>

            <input type="submit" value="支付" @click="createPayQRcode()">
        </form>

    </div>


    <script>
        let vue = new Vue({
            el: "#app",
            data: {
                order: {
                    appid: "",
                    partener: "",
                    partenrekey: "",
                    notifyurl: "",
                    totalFee: "",
                },
            },
            method: {
                createPayQRcode() {
                    // 参数
                    let params = {
                        appid: this.order.appid,
                        partener: this.order.partener,
                        partenrekey: this.order.partenrekey,
                        notifyurl: this.order.notifyurl,
                        totalFee: this.order.totalFee,
                    };
                    // 发送请求
                    axiox.post("http://t77s8b.natappfree.cc//wx/pay", params).then(response => {
                        // 成功
                        if (response.data.status == 200) {
                            console.log(response.data.message);
                            this.order = response.data.object;
                        }
                        // 失败
                        if (response.data.status == 500) {
                            console.log(response.data.message);
                        }
                    })
                }

            },
        });
    </script>
</body>
</html>