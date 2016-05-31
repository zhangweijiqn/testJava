package zwj.test.Json;


import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;


/**
 * JSON转换工具
 * @author wangneng
 *
 */
public class JSONUtils {

	private static final JsonFactory jf = new JsonFactory();
	private static final ObjectMapper om = new ObjectMapper();
	
	private JSONUtils() {
	}
	
	/**
	 * 把一个对象转换成json字符串
	 * 
	 * @param obj
	 * @param prettyPrint，设置转换后的json样式<br/>
	 * 		true：可读性强的json串，如：<br/>
	 * 			{<br/>
	 * 				"key1" : "value1",<br/>
	 * 				"key2" : "value2"<br/>
	 * 			}<br/>
	 * 		fasle：精简的json串，如：<br/>
	 * 			{"key1":"value1","key2":"value2"}
	 * 
	 * @return 
	 */
	public static String toJSON(Object obj, boolean prettyPrint) throws Exception {
		if ( obj == null ) {
			return null;
		}

		StringWriter writer = new StringWriter();
		JsonGenerator jg = jf.createJsonGenerator(writer);		
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
			
		om.writeValue(jg, obj);
		return writer.toString();
	}
	
	/**
	 * 把一个对象转换成json字符串，默认采用精简模式
	 * 
	 * @param obj
	 * @return
	 * @see #toJSON(Object, boolean) 
	 */
	public static String toJSON(Object obj) throws Exception {
		return toJSON(obj, false);
	}
	
	/**
	 * 解析json字符串，该json字符串符合如下格式：<br/>
	 * [{key:value}]<br/>
	 * 比如：[{\"id\":\"123456\",\"name\":\"张三\"}, {\"id\":\"123457\",\"name\":\"李四\"}]<br/>
	 * 
	 * @param json
	 * @return ArrayList<Map<String, Object>>
	 * @throws Exception，如果解析错误
	 */
	public static ArrayList<Map<String, Object>> parseJSON(String json) throws Exception  {
		if ( json == null || json.length() == 0 ) {
			return null;
		}
		
		return om.readValue(json, new TypeReference<ArrayList<Map<Object, Object>>>() {});
	}
	
	/**
	 * 解析json字符串，该json字符串符合如下格式：<br/>
	 * {key:value}<br/>
	 * 比如：{\"id\":\"123456\",\"name\":\"张三\",\"borther\":[\"李四\",\"王五\"]}<br/>
	 * 
	 * @param json
	 * @return Map<String, Object>
	 * @throws Exception，如果解析错误
	 */
	public static Map<String, Object> parseJSON2Map(String json) {
		if ( json == null || json.length() == 0 ) {
			return null;
		}
		
		try {
			return om.readValue(json, new TypeReference<Map<String, Object>>() {});
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}
		
		
	}

	public static  <T> Object fromJson(String jsonAsString, Class<T> pojoClass) {
		try {
			return om.readValue(jsonAsString, pojoClass);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
