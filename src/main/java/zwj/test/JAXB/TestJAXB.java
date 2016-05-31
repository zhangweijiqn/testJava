package zwj.test.JAXB;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by zhangwj on 15-12-17.
 */
public class TestJAXB {

    /*JavaToXml
    * Unmarshaller 类管理将 XML 数据反序列化为新创建的 Java 内容树的过程，并可在解组时有选择地验证 XML 数据。
    * */
    public static void testJavaToXML(){
        try {

            JAXBContext jc = JAXBContext.newInstance(Student.class);
            Marshaller ms = jc.createMarshaller();
            Student st = new Student("zhang", "w", "h", 11);
            ms.marshal(st, System.out);

        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

     public static void testXMLToJava() throws JAXBException {
         String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><student><age>11</age><height>h</height><name>zhang</name><width>w</width></student>";
         JAXBContext jc = JAXBContext.newInstance(Student.class);
         Unmarshaller unmar = jc.createUnmarshaller();
         Student stu = (Student) unmar.unmarshal(new StringReader(xml));
         System.out.println("\n"+stu.getName());

     }

    public static void main(String[] args) throws JAXBException {
        testJavaToXML();
         testXMLToJava();
    }
}
