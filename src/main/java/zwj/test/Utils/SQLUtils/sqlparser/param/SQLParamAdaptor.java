package zwj.test.Utils.SQLUtils.sqlparser.param;

import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sql参数适配器
 * <p>
 * 主要用于处理参数取值的问题
 * @author bjliuhongbin
 */
public class SQLParamAdaptor {
	private Map<String, AbstractParam> paramFinder = new ConcurrentHashMap<String, AbstractParam>();
	
	/**
	 * 添加参数
	 * @param params 要添加的参数列表
	 */
	public void addParams(AbstractParam...params) {
		for (AbstractParam param : params) {
			addParam(param);
		}
	}
	
	/**
	 * 添加参数
	 * @param param 要添加的参数
	 */
	public void addParam(AbstractParam param) {
		if (param == null) {
			return ;
		}
		
		String name = param.getName();
		if (!StringUtils.isEmpty(name)) {
			name = name.toUpperCase();
		}
		this.paramFinder.put(name, param);
	}
	
	/**
	 * 通过参数名获取参数
	 * @param paramName 参数名
	 * @return
	 */
	public AbstractParam find(String paramName) {
		if (StringUtils.isEmpty(paramName)) {
			return null;
		}
		
		String key = paramName.toUpperCase();
		return this.paramFinder.get(key);
	}
	
}
