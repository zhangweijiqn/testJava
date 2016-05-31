package zwj.test.Enum;

import java.util.Map;

/**
 * 状态
 */
public enum Status {
    RESEARCH("调研"),
    DEVELOP("开发"),
    TEST("测试"),
    ONLINE("上线"),
    ONLINE_APPLY("申请上线"),
    DEPLOYED("已部署"),
    UNDEPLOYED("未部署");

    String mean;

    Status(String mean) {
        this.mean = mean;
    }

    public String getMean() {
        return mean;
    }

    static Map<String, Status> meanMap = null;
    public static Status get(String name){
        for (Status status :Status.values()) {
            if (status.getMean().equals(name)) {
                return status;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Status.get("未部署"));  //界面传递的中文状态解析为枚举类型，方便SQL存储
        Status status = Status.get("已部署");
        System.out.println(status.name());//持久化时存入status.name()
    }
}
