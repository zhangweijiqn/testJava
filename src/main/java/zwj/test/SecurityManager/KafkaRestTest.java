package zwj.test.SecurityManager;

/**
 * Created by zhangweijian on 2015/12/4.
 */

        import zwj.test.Transports.transport.*;
        import zwj.test.Transports.util.IOUtils;

        import java.io.*;
        import java.net.URI;

public class KafkaRestTest implements Runnable {

    public InputStream in;

    public static void main(String args[]) throws Exception {
        KafkaRestTest test = new KafkaRestTest();
        Transport trans = new DefaultTransport();
        Connection conn = null;
        try {
            Request req = new Request();
            req.setURI(new URI("http://localhost:8082/topics/test"));
            req.setMethod(Request.Method.POST);
            req.setHeader("Content-Type", "application/vnd.kafka.json.v1 json");
//            req.setBody(new FileInputStream(new File("/home/mtc/����/kafkaresttest")));
            req.setBody(test.in);
            conn = trans.connect(req);
            test.run();
            DefaultResponse resp = null;
            // send request body
            if (req.getBody() != null) {
                OutputStream out = conn.getOutputStream();
                IOUtils.copyLarge(req.getBody(), out);
                out.close();
            }

            resp = (DefaultResponse) conn.getResponse();

            if (Request.Method.HEAD != req.getMethod()) {
                InputStream in = conn.getInputStream();
            }

            resp.getBody();
//            test.in.read()
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    @Override
    public void run() {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            in = new FileInputStream(new File("/home/mtc/����/kafkaresttest"));
        } catch (FileNotFoundException e) {
            in = null;
            e.printStackTrace();
        }
        int readResult = 0;
        byte[] b = new byte[139];
        do {
            try {
                readResult = in.read(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (readResult != -1);
    }
}
