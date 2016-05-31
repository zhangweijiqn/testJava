package zwj.test.redis;

/**
 * Created by zhangwj on 16-1-8.
 * 另外一种缓存技术 memcacheed+mysql
 */
public class testRedis {
    public static void main(String[] args) {
        RedisLogService redisLogService = new RedisLogService();
        redisLogService.set("test1","111");
        System.out.println(redisLogService.get("test1"));
        System.out.println(redisLogService.get("test1"));
        redisLogService.setSQLResultSet("sql","sqlResult");
        System.out.println(redisLogService.getSQLResultSetList("sql"));
        System.out.println(redisLogService.getSQLResultSetList("sql"));
    }

}
