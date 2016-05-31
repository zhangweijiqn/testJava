package zwj.test.Utils.UUID;

/**
 * ˳���������
 *
 * @author �����
 */
public class OrderGenerator {
    private static long oldOrder = 0;
    private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private OrderGenerator() {
    }

    /**
     * 生成一个新的顺序号字符串
     *
     * @return 返回顺序号字符串
     */
    public static String newOrder() {
//        long order = System.currentTimeMillis();
//        if (order <= oldOrder)
//        	order = oldOrder + 1;
//        while (order <= oldOrder) {
//            order++;
//        }
//        oldOrder = order;
        long order = newOrderID();

        StringBuffer rt = new StringBuffer(10);
        while (order > 0) {
            rt.insert(0, chars.charAt((int) (order % 36)));
            order = order / 36;
        }
        return rt.toString();
    }

    /**
     * 生成一个新的顺序号编码值
     *
     * @return 返回顺序号值
     */
    public synchronized static long newOrderID() {
        long order = System.currentTimeMillis();
        if (order <= oldOrder)
            order = oldOrder + 1;
        oldOrder = order;

        return order;
    }
}