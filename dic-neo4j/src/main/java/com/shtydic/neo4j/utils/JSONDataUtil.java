package com.shtydic.neo4j.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONDataUtil {
	/**
	 * 根据关键字对KeyWord表进行查询
	 * @author Admin
	 *
	 */
	public  Map getJsonKeyWordByvalue(String wherevalue) {
		Map datamap=new HashMap();
		JDBCUtil jsbc=new JDBCUtil();
		String sql="SELECT ID,DXID,MC FROM KEYWORD WHERE MC='"+wherevalue+"'";
		try {
			List<Map<String,String>> list=jsbc.getArrBySQL(sql);
			if(list.size()==1){
				datamap.putAll(list.get(0));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return datamap;
	}
	
	
	/**
	 * KEYWORD表封装到List<Map<String,String>>
	 * @return
	 */
	public  List<Map<String,String>> getKeyWordList() {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			JDBCUtil jsbc=new JDBCUtil();
			String sql = "SELECT ID,DXID,MC FROM KEYWORD ";
			list = jsbc.getArrBySQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 *根据中文名称和字段获取KeyWordList里面的值
	 * @return
	 */
	public  String getValueByKeyWordList(String mc,String column) {
		String value = "";
		List<Map<String,String>> list = getKeyWordList();
		 for (int i = 0; i < list.size(); i++) {
			if(mc.equals(list.get(i).get("MC"))){
				value = list.get(i).get(column);
			}
		}
		return value;
	}
	/**
	 * 根据关键字id对GX_NEO4J表进行查询返回名称
	 */
	public static String getNeo4jNameByKID(String kid){
		JDBCUtil jsbc=new JDBCUtil();
		String sql ="SELECT MC FROM GX_NEO4J WHERE KID = '" +kid+"'";
		String MC = "";
		try {
			List<Map<String,String>> list=jsbc.getArrBySQL(sql);
			if(list.size() > 0){
				MC = list.get(0).get("MC");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return MC;
		
	}
	
	/**
	 * 根据关键字id对KeyWord表进行查询返回名称
	 * @author Admin
	 *
	 */
	public static  String getJsonKeyWordById(String kid) {
		JDBCUtil jsbc=new JDBCUtil();
		String sql="SELECT ID,MC FROM KEYWORD WHERE id='"+kid+"'";
		String MC = "";
		try {
			List<Map<String,String>> list=jsbc.getArrBySQL(sql);
			if(list.size() > 0){
				MC = list.get(0).get("MC");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return MC;
	}
	/**
	 * 根据GXMC值去GXINFO表中查询返回List集合，在集合中找GXID
	 * @param wherevalue
	 * @return
	 */
	public String getJsonGxInfoByValue(String wherevalue){
		String returnStr="";
		JDBCUtil jdbc=new JDBCUtil();
		String sql="SELECT GXID,GXMC FROM GXINFO";
		try {
			List<Map<String,String>> list=jdbc.getArrBySQL(sql);
			for(int i=0;i<list.size();i++){
				Map<String,String> map=list.get(i);
				if(wherevalue.equals(map.get("GXMC"))){
					returnStr=map.get("GXID");
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("".equals(returnStr)){
			System.out.println("错误：表里没有这种关系");
		}
		return returnStr;
		
	}
	/**
	 * 
	 * @param gxlbid对应oracle库中GXINFO表GXLBID字段和GXLB表中的id
	 * @return returnList返回关系集合如{A06,A11,A01}
	 */
	public static Set<String> getJsonListGxInfoByValue(String gxlbid){
		Set<String> returnList=new HashSet<String>();
		String sql="SELECT GXMC FROM GXINFO WHERE GXLBID ="+gxlbid;
		if("".equals(gxlbid) || "-1".equals(gxlbid)){//gxlbid为空表示取所有关系
			sql="SELECT GXMC FROM GXINFO";
		}
		JDBCUtil jdbc=new JDBCUtil();
		
		List<Map<String, String>> listSql;
		List<String> listallbygxlbid=new ArrayList<String>();//存放右键要查的关系集合
		try {
			listSql = jdbc.getArrBySQL(sql);
			for(int i=0;i<listSql.size();i++){
				listallbygxlbid.add(listSql.get(i).get("GXMC"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Map<String,String> allgxMap=Constants.getALLGXMap();
		if(listallbygxlbid.size()>0){
			for(int j=0;j<listallbygxlbid.size();j++){
				if("".equals(allgxMap.get(listallbygxlbid.get(j)))||null==allgxMap.get(listallbygxlbid.get(j))){
					continue;
				}
				returnList.add(allgxMap.get(listallbygxlbid.get(j)));
			}
		}
		System.out.println("====="+returnList.toString());
		return returnList;
		
	}
	/**
	 * 去GXINFO表中找所有的关系
	 * @return
	 */
	public List<Map<String,String>> getAllList() {
		JDBCUtil jdbc=new JDBCUtil();
		String sql="SELECT GXID,GXMC FROM GXINFO";
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			list=jdbc.getArrBySQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
