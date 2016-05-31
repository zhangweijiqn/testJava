package zwj.hadoop.testHbase.RWlog;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Configuration {

	public static final Log LOG = LogFactory.getLog(Configuration.class);

	private Properties properties;

	public Configuration(String fileName, boolean xml) {
		try {
			ClassLoader cL = Thread.currentThread().getContextClassLoader();
			if (cL == null) {
				cL = Configuration.class.getClassLoader();
			}
			FileInputStream in = null;
			Properties pro = new Properties();
			if (xml) {
				in = new FileInputStream(new File(cL.getResource(fileName).getPath()));
				pro.loadFromXML(in);
			} else {
				in =  new FileInputStream(new File(fileName));
				pro.load(in);
			}
			this.properties = pro;
			in.close();
		} catch (Exception e) {
			LOG.warn(e);
		}
	}

	public Configuration(String fileName) {
		this(fileName, true);
	}

	public Configuration(Properties properties) {
		this.properties = new Properties();
	} // for test

	public Object get(Object key) {
		return this.properties.get(key);
	}

	public void set(String key, String value) {
		this.properties.setProperty(key, value);
	}

	public boolean containsKey(String key) {
		return this.properties.containsKey(key);
	}

	public List<String> getValues(Object key) {
		String value = (String) this.properties.get(key);
		String[] values = value.split(",");
		return Arrays.asList(values);
	}

	public String getDefault(String key, String defaultValue) {
		try {
			return this.properties.getProperty(key, defaultValue);
		} catch (Exception e) {
			return null;
		}
	}

	public int getInt(String name, int defaultValue) {
		String valueString = getDefault(name, "");
		if (StringUtils.isBlank(valueString))
			return defaultValue;
		try {
			return Integer.parseInt(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public long getLong(String name, long defaultValue) {
		String valueString = getDefault(name, "");
		if (StringUtils.isBlank(valueString))
			return defaultValue;
		try {
			return Long.parseLong(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public float getFloat(String name, float defaultValue) {
		String valueString = getDefault(name, "");
		if (StringUtils.isBlank(valueString))
			return defaultValue;
		try {
			return Float.parseFloat(valueString);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public void addResource(String fileName) {
		try {
			FileInputStream in = new FileInputStream(new File(fileName));
			Properties pro = this.properties;
			if (pro == null) {
				pro = new Properties();
				pro.loadFromXML(in);
				in.close();
			}
			this.properties = pro;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addResource(FileInputStream in) {
		try {
			Properties pro = this.properties;
			if (pro == null) {
				pro = new Properties();
				pro.loadFromXML(in);
				in.close();
			}
			this.properties = pro;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addResource(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			Properties pro = this.properties;
			if (pro == null) {
				pro = new Properties();
				pro.loadFromXML(in);
				in.close();
			}
			this.properties = pro;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
