import com.demo.wxpay.pojo.Order;
import com.demo.wxpay.utils.WxPayUtils;

/**
 * @Package: PACKAGE_NAME
 * @author: jt
 * @date: 2021-08-31   18:46
 * @Description:
 */
public class demo {
    public static void main(String[] args) {
        // System.out.println("书名\t作者\t价格\t销量\t\t\n三国\t罗贯中\t120\t\t1000");
        System.out.println("韩顺平教育\r北京");
        // System.out.println("===========");
        // System.out.println("传统的是\n方法");
        // System.out.println("===========");
        // System.out.println("传统的是\r\n方法");
        // System.out.println("===========");
        // System.out.println("传统的是\n\r方法");

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setOutTradeNo(WxPayUtils.createorderidByuuid());
            System.out.println(order);
            System.out.println("=========================");
        }
    }
}
