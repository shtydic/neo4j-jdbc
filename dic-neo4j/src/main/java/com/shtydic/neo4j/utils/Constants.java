package com.shtydic.neo4j.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <p>Title: Constants</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午10:28:58
 */
public class Constants {
	public static String neo4jdb_peoplename =null;
	public static String neo4jdb_wpname =null;
	public static String neo4jdb_placename =null;
	public static String neo4jdb_zzname =null;
	
    public static String neo4jUserName = null;
    public static String neo4jPassword = null;
    public static String neo4j_ip_port = null;
    public static String serverIp = null;
    public static String serverPort = null;

    //kafka
    public static String zkConnect = null;
    public static String kafkaServer = null;
    public static String topic = null;
    public static String topic_relation = null;
    public static Integer partition_num = null;
    public static String kafka_groupName = null;
    public static String kafka_userName = null;
    public static Integer deal_thread_num = null;
    
    //ORACLE
    public static String oracle_driver = null;
    public static String oracle_url = null;
    public static String oracle_username = null;
    public static String oracle_password = null;
    
    public static String tomcat_dir = null;


    static {
        InputStream inStream = Constants.class.getClassLoader().getResourceAsStream("sys.properties");
        Properties prop = new Properties();
        try {
        	if(inStream == null){
        		neo4jUserName = "";
                neo4jPassword = "";
                neo4j_ip_port = "";
        	}else{
            prop.load(inStream);
            oracle_driver = (String) prop.get("oracle_driver");
            oracle_url = (String) prop.get("oracle_url");
            oracle_username = (String) prop.get("oracle_username");
            oracle_password = (String) prop.get("oracle_password");
            neo4jUserName = (String) prop.get("neo4j_username");
            neo4jPassword = (String) prop.get("neo4j_password");
            neo4j_ip_port = (String) prop.get("neo4j_ip_port");
            neo4jdb_peoplename =(String) prop.get("neo4jdb_peoplename");
            neo4jdb_wpname =(String) prop.get("neo4jdb_wpname");
            neo4jdb_placename =(String) prop.get("neo4jdb_placename");
            neo4jdb_zzname =(String) prop.get("neo4jdb_zzname");
            serverIp = (String)prop.get("server_ip");
            serverPort = (String)prop.get("server_port");
            zkConnect = (String)prop.get("zkConnect");
            kafkaServer = (String)prop.get("kafkaServer");
            topic = (String)prop.get("topic");
            topic_relation = (String)prop.get("topic_relation");
           String partition_numStr = (String)prop.get("partition_num");
           if(StringUtils.isNotBlank(partition_numStr)){
        	   partition_num = Integer.valueOf(partition_numStr);
           }
            kafka_groupName = (String)prop.get("kafkaGroupName");
            kafka_userName = (String)prop.get("kafkaUserName");
            String deal_thread_numStr = (String)prop.get("deal_thread_num");
            if(StringUtils.isNotBlank(deal_thread_numStr)){
            	deal_thread_num = Integer.valueOf(deal_thread_numStr);
            }
            tomcat_dir =  (String)prop.get("tomcat_dir");
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //任务运行状态
    public static final Integer job_running = 0;//正在运行
    public static final Integer job_finished = 1;//已完成
    public static final Integer job_suspending = 2;//暂停


    //任务发布状态
    public static final Integer job_unpublished = 0;//未发布
    public static final Integer job_published = 1;//发布


    //运行方式
    public static final Integer runType_Once = 0;//一次性运行
    public static final Integer runType_Period = 1;//周期性性运行

    //审核的操作类型
    public static final Integer audit_type_null = -1;  //不是审核状态  没有操作类型
    public static final Integer audit_type_add = 1;
    public static final Integer audit_type_del = 2;
    public static final Integer audit_type_upd = 3;

    //审核状态
    public static final Integer audit_stauts_no = 0;  //未审核
    public static final Integer audit_stauts_ok = 1;  //已审核
    public static final Integer audit_stauts_not = 2; //没有审核

    //是否有审核
    public static final Integer is_audit_no = 0;   //没有
    public static final Integer is_audit_ok = 1;   //有

    //操作类型
    public static final Integer add = 1;
    public static final Integer del = 2;
    public static final Integer upd = 3;

    //是否为索引
    public static final String Index_no = "1";   //不是是
    public static final String Index_yes = "0";   //是

    //是否为索引
    public static final String primaryKey_no = "1";   //不是是
    public static final String primaryKey_yes = "0";   //是

    //是否主键相同覆盖
    public static final String primary_nocover = "1";//不覆盖
    public static final String primary_cover = "0";//覆盖

    //日期格式化方式
    public static final String timeFomat_dateTime = "年月日时分秒";//覆盖
    public static final String timeFomat_date = "年月日";//覆盖

    //读取状态
    public static final Integer read_finished = 0;//读取完成
    public static final Integer read_unfinished = 1;//读取未完成

    public static final Integer extract_task_node = 0;//抽取节点类型
    public static final Integer extract_task_relation = 1;//抽取关系类型
    
    //A01亲子 A02继养 A03兄弟姐妹 A04祖孙 A05曾祖孙 A06夫妻 A07姻亲 A08堂亲 A09其他亲属 A10同户人 A11借住 
  	//B01同行 B02火车同行 B03飞机同行 B04同住 B05当天同住
  	//C01同事 C02同校 
    //目前只有的关系对应 20170518
    public static Map<String,String> getALLGXMap(){
    	Map<String,String> gxMap=new HashMap<String,String>();
    	JDBCUtil jdbc=new JDBCUtil();
    	String sql="SELECT MC,GX FROM GX_GZ ";
    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			list=jdbc.getArrBySQL(sql);
			for(int i=0;i<list.size();i++){
				gxMap.put(list.get(i).get("MC"), list.get(i).get("GX"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return gxMap;    	
    }
    /**
     * key:A06 value:夫妻
     * @param gx
     * @return
     */
    public static Map<String,String> getAllGXMCMap(){
    	Map<String,String> allGXMap=Constants.getALLGXMap();
    	Map<String,String> allGXMCMap=new HashMap<String, String>();
    	Iterator i = allGXMap.entrySet().iterator();
    	while (i.hasNext()) {
    		Entry entry = (java.util.Map.Entry)i.next();
    		allGXMCMap.put((String)entry.getValue(),(String)entry.getKey());
    		}
		return allGXMCMap;
    	
    }
    
  	
  	


}
