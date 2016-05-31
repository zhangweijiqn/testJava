/**
 *
 */
package zwj.hadoop.testHbase.RWlog;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.security.UserGroupInformation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bjzhongdegen
 *
 */
public class HBaseClient {
	protected static final Logger LOGGER = LoggerFactory.getLogger(HBaseClient.class);
	private static org.apache.hadoop.conf.Configuration conf = null;
	private static  HTablePool pool = null;
	private static final String CONNECTOR = "#";
	private static Configuration commonConf = null;

	/**
	 * 初始化配置
	 */
	static {
		Security.addProvider(new BouncyCastleProvider());
		try {
			Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}

		commonConf = new Configuration("common-config.xml");
		LOGGER.info(commonConf.get("work.dir") + "/conf/krb5.conf");
		System.setProperty("java.security.krb5.conf", commonConf.get("work.dir") + "/conf/krb5.conf");
		String keytab = commonConf.get("work.dir") + "/conf/javaApp.keytab";
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", commonConf.getDefault("hbase.zookeeper.quorum", "127.0.0.1"));
		conf.set("hadoop.security.authentication", "kerberos");
		conf.set("hbase.security.authentication", "kerberos");
		conf.set("hbase.master.kerberos.principal" , "hbase/_HOST@HADOOP.JD");
//		conf.set("hbase.master.keytab.file" , keytab);
		conf.set("hbase.regionserver.kerberos.principal" , "hbase/_HOST@HADOOP.JD");
//		conf.set("hbase.regionserver.keytab.file" , keytab);
		conf.set("hbase.rpc.engine", "org.apache.hadoop.hbase.ipc.SecureRpcEngine");

		UserGroupInformation.setConfiguration(conf);
		UserGroupInformation userGroup = null;
		try {
			LOGGER.info("hbase javaapp: "+keytab+"  "+ JSON.toJSONString(conf));
			userGroup = UserGroupInformation.loginUserFromKeytabAndReturnUGI("javaApp@HADOOP.JD", keytab);
		} catch (IOException e) {
			e.printStackTrace();
		}
		UserGroupInformation.setLoginUser(userGroup);
		pool = new HTablePool(conf, 30);
	}

	public static LogRecord statusLog(String tableName, String taskInstanceId) {
		LOGGER.info("Get statuslog taskInstanceId[" + taskInstanceId + "] from " + tableName);
		LogRecord result = new LogRecord();
		StringBuffer sb = new StringBuffer();
		HTableInterface table = null;
		table = pool.getTable(tableName);
		String rowkey = taskInstanceId + CONNECTOR + CommonCommand.STATUS_TASK;
		Get get = new Get(rowkey.getBytes());
		try {
			Result res = table.get(get);
			KeyValue[] kv = res.raw();

			for (int i = 0; i < kv.length; i++) {
				sb.append(new String(kv[i].getValue())).append("\n");
			}
		} catch (IOException e) {
			LOGGER.info("Get statuslog taskInstanceId[" + taskInstanceId + "] from " + tableName
			+ " failed with " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeHTable(table);
		}
		result.setContent(sb.toString());
		return result;
	}

	public static LogRecord scanLog(String tableName, String prifixKey, String startkey, String stopKey) {
		LOGGER.info("Get prifixKey["+prifixKey+"], startkey="+startkey+", stopKey="+stopKey);
		LogRecord result = new LogRecord();

		StringBuffer sb = new StringBuffer();
		HTableInterface table = null;
		try {
			table = pool.getTable(tableName);
			Scan s = new Scan();
			s.setCaching(100);
			List<Filter> list = new ArrayList<Filter>();


			Filter pageFilter = new PageFilter(100);
			list.add(pageFilter);
			if(!StringUtils.isBlank(prifixKey)) {
				Filter prifixFilter =new PrefixFilter(prifixKey.getBytes());
				list.add(prifixFilter);
			}
			Filter all = new FilterList(Operator.MUST_PASS_ALL, list);
			s.setFilter(all);
			if(!StringUtils.isBlank(startkey)) {
				s.setStopRow(startkey.getBytes());
			}
			if(!StringUtils.isBlank(stopKey)) {
				s.setStopRow(stopKey.getBytes());
			}
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				KeyValue[] kv = r.raw();
				for (int i = 0; i < kv.length; i++) {
					sb.append(new String(kv[i].getValue())).append("\n");
					if(i == kv.length - 1) {
						result.setNextTimestamp(getTimeStamp(new String(kv[i].getRow())));
					}
				}
			}
			rs.close();
		} catch (IOException e) {
			LOGGER.error("Get prifixKey["+prifixKey+"], startkey="+startkey+", stopKey="+stopKey, e);
		} finally {
			closeHTable(table);
		}
		result.setContent(sb.toString());
		return result;
	}

	/**
	 * @param
	 * @return
	 */
	private static Long getTimeStamp(String stopKey) {
		return Long.valueOf(stopKey.split(CONNECTOR)[1]);
	}

	public static void pushLog(String tableName, String taskInstanceId, String logContent) {
		pushLog(tableName, taskInstanceId, logContent, "f", "content");
	}

	public static void pushLog(String tableName, String taskInstanceId, String logContent, String family, String qualifier) {
		LOGGER.info("push " + taskInstanceId + " log to " + tableName);
		HTableInterface table = null;
		try {
			table = pool.getTable(tableName);
			Put put = new Put((taskInstanceId + CONNECTOR + System.currentTimeMillis()).getBytes());
			put.add(family.getBytes(), qualifier.getBytes(), logContent.getBytes());
			table.put(put);
		} catch (IOException ioe) {
			LOGGER.error("push " + taskInstanceId + " log to " + tableName + " failed.", ioe);
		} catch (Throwable e) {
			LOGGER.error("push " + taskInstanceId + " log to " + tableName + " failed.", e);
		} finally {
			closeHTable(table);
		}
	}

	private static void closeHTable(HTableInterface table) {
		if(table == null)
			return;

		try {
			table.close();
		} catch (IOException e) {
			LOGGER.warn("close hbase table FAILED", e);
		}
	}

	private static void testPut() {
		String tableName = "task_logs";
		HTableInterface table = pool.getTable(tableName);
		Put put = new Put("hello".getBytes());
		put.add("f".getBytes(), "hello".getBytes(), "world".getBytes());
		try {
			table.put(put);
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("====================================================");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0; i< 100;i ++) {
			HBaseClient.pushLog("task_logs", "xxxxxx", "lllllllllllllllllllllllllll");
		}

		LogRecord result = HBaseClient.scanLog("task_logs", null,null, null);
//		LogRecord result = HBaseClient.scanLog("task_logs", "9#", "9#1", null);
		System.out.println(result.getContent());
//		try {
//			HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
//			if (hBaseAdmin.tableExists("task_logs")) {
//				hBaseAdmin.disableTable("task_logs");
//				hBaseAdmin.deleteTable("task_logs");
//			}
//			HTableDescriptor tableDescriptor = new HTableDescriptor("task_logs");
//			tableDescriptor.addFamily(new HColumnDescriptor("f"));
//			hBaseAdmin.createTable(tableDescriptor);
//			hBaseAdmin.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		testPut();
	}
}
