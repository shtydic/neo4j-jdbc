package com.shtydic.neo4j.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shtydic.neo4j.model.AttrDisplayModel;
import com.shtydic.neo4j.model.DsModel;
import com.shtydic.neo4j.model.MatchModel;
import com.shtydic.neo4j.model.Neo4jJobConfRelationship;
import com.shtydic.neo4j.model.Neo4jModel;
import com.shtydic.neo4j.model.NodeModel;
import com.shtydic.neo4j.model.NodeModel2;
import com.shtydic.neo4j.model.QueryModel;
import com.shtydic.neo4j.model.RelationInnerGroupModel;
import com.shtydic.neo4j.model.RelationTwoGroupModel;
import com.shtydic.neo4j.model.RelationshipModel;
import com.shtydic.neo4j.model.RelationshipModel2;
import com.shtydic.neo4j.model.ReturnModel2;
import com.shtydic.neo4j.model.StartModel;
import com.shtydic.neo4j.service.RelationService;
import com.shtydic.neo4j.utils.Constants;
import com.shtydic.neo4j.utils.DriverManager;
import com.shtydic.neo4j.utils.JSONDataUtil;
import com.shtydic.neo4j.utils.Master;
import com.shtydic.neo4j.utils.MatchBuilder;
import com.shtydic.neo4j.utils.Neo4jBuilder;
import com.shtydic.neo4j.utils.Neo4jConnection;
import com.shtydic.neo4j.utils.Tools;


//import kafka.utils.Json;

/**
 * 
 * <p>Title: Neo4jServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午10:26:10
 */

public class RelationServiceImpl implements RelationService {
    private Neo4jBuilder nb = new Neo4jBuilder();
    ObjectMapper mapper = new ObjectMapper();
    
    private String url;
    private String username;
    private String password;
      
    
    
    public RelationServiceImpl() {
		super();
	}

	public RelationServiceImpl(String url, String username, String password) {
		super();
		this.register(url, username, password);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		String url_ip = "http://" + url + "/db/data/transaction/commit";
		this.url = url_ip;
	}

	public String getUsername() {
		return username;
	}

	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void register(String ip,String username,String password){
		this.setUrl(ip);
		this.username = username;
		this.password = password;
	}
	
	@Override
    public void createNode(NodeModel2 nm) {
		DriverManager.sendPost_too(url, nb.getInsertNode(nm), username, password);
    }

    @Override
    public void createNodeBatch(List<NodeModel2> nms) {
        StringBuffer sb = new StringBuffer();
        for(NodeModel2 nm: nms){
            sb.append(nb.getInsertNode(nm));
        }
        DriverManager.sendPost_too(url, sb.toString(), username, password);
    }
    
    @Override
    public void createNodeBatchEx(List<NodeModel2> nms) {
        StringBuffer sb = new StringBuffer();
        for(int i =0;i<nms.size();i++){
            if (i==0){
                sb.append("CREATE");
                sb.append(nb.getInsertNodeEx(nms.get(i)));
            } else {
                sb.append("," + nb.getInsertNodeEx(nms.get(i)));
            }
        }
        DriverManager.sendPost_too(url, sb.toString(), username, password);
    }

    public String CreateCsvImport(String file, String labelName, String primaryKey, DsModel dsModel, String coverage) {
        StringBuffer sb = new StringBuffer();
        sb.append("USING PERIODIC COMMIT 20000 LOAD CSV WITH HEADERS FROM ");
        sb.append("\\\"" + file + "\\\"");
        sb.append(" AS row ");
        if (Constants.primary_cover.equals(coverage)) {
            sb.append("MERGE (p:" + labelName + "{" + primaryKey + ":row." + primaryKey + "})");
            sb.append("ON CREATE SET p = row ON MATCH  SET p += row");
        } else {
            sb.append("CREATE (p:" + labelName + ") set p = row");
        }
        String result = DriverManager.sendPost_too(url, sb.toString(), username, password);
        return result;
    }

    public String CreateCsvImportRelation(String file, Neo4jJobConfRelationship neo4jJobConfRelationship, DsModel neo4jDatabase) {
        StringBuffer sb = new StringBuffer();
        String node1 = neo4jJobConfRelationship.getStart_node();
        String node2 = neo4jJobConfRelationship.getEnd_node();
        String node1Index = neo4jJobConfRelationship.getStart_node_index();
        String node2Index = neo4jJobConfRelationship.getEnd_node_index();
        String node1Type = neo4jJobConfRelationship.getStart_node_type();
        String node2Type = neo4jJobConfRelationship.getEnd_node_type();
        String relation = neo4jJobConfRelationship.getRelationship_type();
        Map<String, String> paramMap = neo4jJobConfRelationship.getRelationPropMap();
        sb.append("USING PERIODIC COMMIT 20000 LOAD CSV WITH HEADERS FROM ");
        sb.append("\\\"" + file + "\\\"");
        sb.append(" AS row ");
        sb.append(" MATCH ");
        sb.append("(a:" + node1Type + "{" + node1Index + ":row." + node1 + "}),");
        sb.append("(b:" + node2Type + "{" + node2Index + ":row." + node2 + "})");
        sb.append(" create (a)-[:" + relation + "{");
        boolean isFirst = true;
        for (String key : paramMap.keySet()) {
            if (isFirst) {
                if (paramMap.get(key).equals("INT")) {
                    sb.append(key + ":toInt(row." + key + ")");
                } else {
                    sb.append(key + ":row." + key);
                }
            } else {
                if (paramMap.get(key).equals("INT")) {
                    sb.append("," + key + ":toInt(row." + key + ")");
                } else {
                    sb.append("," + key + ":row." + key);
                }
            }
            isFirst = false;
        }
        sb.append("}]->(b)");
        String result = DriverManager.sendPostWithAddress(sb.toString(), neo4jDatabase);
        return result;
    }

    @Override
    public void createIndex(String labelName, String field) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE INDEX ON :" + labelName + "(" + field + ")");
        DriverManager.sendPost_too(url, sb.toString(), username, password);
    }

    @Override
    public boolean removeNode(String name, Object obj) {
        StringBuffer sb = new StringBuffer("MATCH ");
        String Nodel = MatchBuilder.createNode(name, obj);
        sb.append("-[r]-() ");
        sb.append("delete ");
        sb.append("(r)");
        //先删除关系
        DriverManager.sendPost_too(url, sb.toString(), username, password);
//        DriverManager.sendPost(DriverManager.url, sb.toString());
        StringBuffer sb2 = new StringBuffer("MATCH ");
        String Nodel2 = MatchBuilder.createNode(name, obj);
        sb.append("delete ");
        sb.append("(n)");
        //在删除节点
//        DriverManager.sendPost(DriverManager.url, sb2.toString());
        DriverManager.sendPost_too(url, sb2.toString(), username, password);
        return false;
    }

    @Override
    public void createRelationship(RelationshipModel2 rm) {
    	DriverManager.sendPost_too(url, nb.getInsertRelationship(rm), username, password);
//        DriverManager.sendPost(DriverManager.url, nb.getInsertRelationship(rm));
    }

    @Override
    public boolean removeRelationship() {

        return false;
    }

    @Override
    public void createComplexQuery(QueryModel qm) {
    	 DriverManager.sendPost_too(url, nb.getQuery(qm), username, password);
//        DriverManager.sendPost(DriverManager.url, nb.getQuery(qm));
    }

    @Override
    public String findByIdNexusAll(String id) {
        String startName = "a";
        QueryModel qm = new QueryModel();
        StartModel sm = new StartModel(startName, "Person");
        ReturnModel2 rm = new ReturnModel2();
        sm.add("id", "=", "'" + id + "'");
        qm.getStartModels().add(sm);
        qm.setSelectType(Neo4jModel.SelectType.node);
        String matchStr = "MATCH p = (" + startName + ")--(b)";
        qm.setMatchStr(matchStr);
        rm.setName("p");
        qm.getReturnModels().add(rm);
        String queryStr = nb.getQuery(qm);
        String jsonStr = DriverManager.sendPost_too(url, queryStr.trim(), username, password);
//        String jsonStr = DriverManager.sendPost(DriverManager.url, queryStr.trim());
        return jsonStr;
    }

    @Override
    public String findAttrNameNexusAll(String attrName, Object value) {
        String startName = "a";
        QueryModel qm = new QueryModel();
        StartModel sm = new StartModel(startName, "Person");
        ReturnModel2 rm = new ReturnModel2();
        if (value instanceof String) {
            sm.add(attrName, "=", "'" + value + "'");
        } else {
            sm.add(attrName, "=", value);
        }

        qm.getStartModels().add(sm);
        qm.setSelectType(Neo4jModel.SelectType.node);
        String matchStr = "MATCH p= (" + startName + ")--(b)";
        qm.setMatchStr(matchStr);
        rm.setName("p");
        qm.getReturnModels().add(rm);
        String neo4jStr = nb.getQuery(qm);
        String jsonStr = DriverManager.sendPost_too(url, neo4jStr.trim(), username, password);
//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
        return jsonStr;
    }

    @Override
    public String findByIdDepthQuery(String id, Integer depth) {
        String startName = "a";
        QueryModel qm = new QueryModel();
        StartModel sm = new StartModel(startName, "Person");
        ReturnModel2 rm = new ReturnModel2();
        sm.add("id", "=", "'" + id + "'");
        qm.getStartModels().add(sm);
        qm.setSelectType(Neo4jModel.SelectType.node);
        String matchStr = qm.createMatchBuilder()
                .nexusStart(startName, "r*1.." + depth, "b", MatchModel.DirType.right).createComplexQueryStr("p");
        qm.setMatchStr(matchStr);
        rm.setName("p");
        qm.getReturnModels().add(rm);
        String neo4jStr = nb.getQuery(qm);
        String jsonStr = DriverManager.sendPost_too(url, neo4jStr.trim(), username, password);
//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
        return jsonStr;
    }
    
    @Override
    public String findDepthOwnQuery(String gxlbid,String id,Set<String> gxList ,Integer depth) {
//    	Match p = (a:Das5_Person{id:'342622195310307128'})-[r*0..1]->(b:Das5_Person{})  RETURN p
    	StringBuilder sb = new StringBuilder("MATCH ");
    	sb.append("(a:"+Constants.neo4jdb_peoplename+"{ZJHM:'"+id+"'})-");
    	sb.append("[r");
    	if(gxList !=null){
    		sb.append(":");
    		for(String gx : gxList){
    			sb.append(gx+"|");
        	}
    		sb = sb.deleteCharAt(sb.length()-1);
    	}
    	if("-1".equals(gxlbid)){
    		sb.append("*0.."+depth+"]-(b)  RETURN r,b");
    	}else if("02".equals(gxlbid)){
    		sb.append("*0.."+depth+"]-(b:"+Constants.neo4jdb_wpname+")  RETURN r,b");
    	}else if("04".equals(gxlbid)){
    		sb.append("*0.."+depth+"]-(b:"+Constants.neo4jdb_wpname+")  RETURN r,b");
    	}else{
    		sb.append("*0.."+depth+"]-(b:"+Constants.neo4jdb_peoplename+"{})  RETURN r,b");
    	}
    	
//        String startName = "a";
//        QueryModel qm = new QueryModel();
//        StartModel sm = new StartModel(startName, "Person");
//        ReturnModel2 rm = new ReturnModel2();
//        if (attrName instanceof String) {
//            sm.add(attrName, "=", "'" + value + "'");
//        } else {
//            sm.add(attrName, "=", value);
//        }
//        qm.getStartModels().add(sm);
//        qm.setSelectType(Neo4jModel.SelectType.node);
//        String matchStr = qm.createMatchBuilder()
//                .nexusStart(startName, "r*1.." + depth, "b", MatchModel.DirType.right).createComplexQueryStr("p");
//        qm.setMatchStr(matchStr);
//        rm.setName("p");
//        qm.getReturnModels().add(rm);
//        String neo4jStr = nb.getQuery(qm);
//        System.out.println("测试语句："+neo4jStr);	
//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
////        return null;
//        return jsonStr;
//    	System.out.println("+++++++++++++++++++++++++++++++"+sb.toString());
    	String jsonStr = DriverManager.sendPost_too(url, sb.toString(), username, password);
//    	String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        return jsonStr;
    }
    
    /**
     * 根据手机WPID查询人
     * @param id
     * @param depth
     * @return
     */
    public String findDeptPhoneQuery(String id,Integer depth) {
    	StringBuilder sb = new StringBuilder("MATCH ");
    	sb.append("(a:"+Constants.neo4jdb_wpname+"{WPID:'"+id+"'})-");
    	sb.append("[r");
    	sb.append("*0.."+depth+"]-(b)  RETURN r,b");
    	
    	System.out.println("WPCQL==="+sb.toString());
    	//String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
    	String jsonStr = DriverManager.sendPost_too(url, sb.toString(), username, password);
        return jsonStr;
    }


    @Override
    public String findDepthIsMailboxQuery(String id,List<String> gxList ,Integer depth,String findType) {
//    	Match p = (a:Das5_Person{id:'342622195310307128'})-[r*0..1]->(b:Das5_Person{})  RETURN p
    	StringBuilder sb = new StringBuilder("MATCH p = ");
    	sb.append("(a:People{ZJHM:'"+id+"'})-");
    	sb.append("[r");
    	if(gxList !=null){
    		sb.append(":");
    		for(String gx : gxList){
    			sb.append(gx+"|");
        	}
    		sb = sb.deleteCharAt(sb.length()-1);
    	}
    	sb.append("*0.."+depth+"]-(b:"+findType+"{})  RETURN p");
    	String jsonStr = DriverManager.sendPost_too(url, sb.toString(), username, password);
//    	String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        return jsonStr;
    }
    @Override
    public String findMatchPathQuery(List<Map<String, Object>> props, Integer maxLeng, MatchModel.MathPath mathPath) {
        if (maxLeng == null) {
            maxLeng = 10;
        }
        String startName = "a";
        String endName = "b";
        QueryModel qm = new QueryModel();
        StartModel smStart = new StartModel(startName, "Person");
        StartModel smEnd = new StartModel(endName, "Person");
        ReturnModel2 rm = new ReturnModel2();
        Map<String, Object> mapStart = props.get(0);
        Map<String, Object> mapEnd = props.get(1);
        for (String key : mapStart.keySet()) {
            smStart.add(key, "=", "'" + mapStart.get(key) + "'");
        }
        for (String keyEnd : mapEnd.keySet()) {
            smEnd.add(keyEnd, "=", "'" + mapEnd.get(keyEnd) + "'");
        }
        qm.getStartModels().add(smStart);
        qm.getStartModels().add(smEnd);
        qm.setSelectType(Neo4jModel.SelectType.node);
        String matchStr = qm.createMatchBuilder().macthPath(startName, endName, maxLeng, mathPath);
        qm.setMatchStr(matchStr);
        rm.setName("p");
        qm.getReturnModels().add(rm);
        String neo4jStr = nb.getQuery(qm);

//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
        String jsonStr = DriverManager.sendPost_too(url, neo4jStr.trim(), username, password);
        return jsonStr;
    }

    @Override
    public String findMatchPathQuery(List<Map<String, Object>> props) {
        String startName = "a";
        String endName = "b";
        QueryModel qm = new QueryModel();
        StartModel smStart = new StartModel(startName, "Person");
        StartModel smEnd = new StartModel(endName, "Person");
        ReturnModel2 rm = new ReturnModel2();
        Map<String, Object> mapStart = props.get(0);
        Map<String, Object> mapEnd = props.get(1);
        for (String key : mapStart.keySet()) {
            smStart.add(key, "=", "'" + mapStart.get(key) + "'");
        }
        for (String keyEnd : mapEnd.keySet()) {
            smEnd.add(keyEnd, "=", "'" + mapEnd.get(keyEnd) + "'");
        }
        qm.getStartModels().add(smStart);
        qm.getStartModels().add(smEnd);
        qm.setSelectType(Neo4jModel.SelectType.node);
        String matchStr = qm.createMatchBuilder().macthPath(startName, endName);
        qm.setMatchStr(matchStr);
        rm.setName("p");
        qm.getReturnModels().add(rm);
        String neo4jStr = nb.getQuery(qm);
//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
        String jsonStr = DriverManager.sendPost_too(url, neo4jStr.trim(), username, password);
        return jsonStr;
    }

    @Override
    public String findRelationshipCount(String relationship) {
        QueryModel qm = new QueryModel();
        StartModel sm = new StartModel("a", "");
        ReturnModel2 rm = new ReturnModel2();
        ReturnModel2 rm2 = new ReturnModel2();
        qm.setSelectType(Neo4jModel.SelectType.node);
        rm.setName("a");
        rm2.setName("count(*)");
        qm.getStartModels().add(sm);
        String matchStr = qm.createMatchBuilder().nexusStart("a", ":" + relationship, "", MatchModel.DirType.eq).createComplexQueryStr();
        qm.setMatchStr(matchStr);
        qm.getReturnModels().add(rm);
        qm.getReturnModels().add(rm2);
        String neo4jStr = nb.getQuery(qm) + " order by count(*) desc";
//        String jsonStr = DriverManager.sendPost(DriverManager.url, neo4jStr.trim());
        String jsonStr = DriverManager.sendPost_too(url, neo4jStr.trim(), username, password);
       
        return jsonStr;
    }

    @Override
    public String findInCompanion(Object startObj, Object relationship,int count,String... relationshipProp) {
        // match (a:Person{id:'1000',name:'李四'})-[r:Nexus*0..4]-(b)
        // with b as b1,r as r1
        // MATCH (p:Person)-[x:Nexus]->(b1)
        // where x.date=r1.date and x.flight_id = r1.flight_id
        // return (p),count(b1)>3,count(b1)
        StringBuilder sb = new StringBuilder();
        Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
        Master master = conn.createMaster();
        NodeModel obj = master.createNodeModel().createWhereNodeObj("obj", startObj);
        String indexName = obj.getIndexName();
        String rStr = master.createRelationshipModel().createWhereRelationshipStr("r",relationship);
        String xStr = master.createRelationshipModel().createWhereRelationshipStr("x",relationship);
        sb.append("MATCH ");
        sb.append(obj.nodeStr());
        sb.append("-");
        sb.append(rStr);
        sb.append("-");
        sb.append("(b) ");
        sb.append("WITH b AS b1,r AS r1 ");
        sb.append("MATCH ");
        sb.append("(");
        sb.append("p:");
        sb.append(indexName);
        sb.append(")");
        sb.append("-");
        sb.append(xStr);
        sb.append("->(b1) ");
        sb.append("WHERE ");
        for (int i = 0; i < relationshipProp.length; i++) {
            if (i == relationshipProp.length - 1) {
                sb.append("x." + relationshipProp[i] + "=r1." + relationshipProp[i]);
            } else {
                sb.append("x." + relationshipProp[i] + "=r1." + relationshipProp[i] + " AND ");
            }
        }
        //return (p),count(b1)>3,count(b1)
        sb.append(" return (p),count(b1)>" + count + ",count(b1)");
//        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
//        return jsonStr;
//        System.out.println("发送的cypher语句："+sb.toString());
//        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        String jsonStr = DriverManager.sendPost_too(url, sb.toString(), username, password);
        return jsonStr;
    }

    @Override
    public String findInCompanionB(Object startObj, Object relationship, int count, Map<String, String> whereProp) {
        StringBuilder sb = new StringBuilder();
        Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
        Master master = conn.createMaster();
        NodeModel obj = master.createNodeModel().createWhereNodeObj("obj", startObj);
        String indexName = obj.getIndexName();
        String rStr = master.createRelationshipModel().createWhereRelationshipStr("r",relationship);
        String xStr = master.createRelationshipModel().createWhereRelationshipStr("x",relationship);
        sb.append("MATCH ");
        sb.append(obj.nodeStr());
        sb.append("-");
        sb.append(rStr);
        sb.append("-");
        sb.append("(b) ");
        sb.append("WITH b AS b1,r AS r1 ");
        sb.append("MATCH ");
        sb.append("(");
        sb.append("p:");
        sb.append(indexName);
        sb.append(")");
        sb.append("-");
        sb.append(xStr);
        sb.append("->(b1) ");
        sb.append("WHERE ");
        for(Map.Entry<String, String> s : whereProp.entrySet()){
                sb.append("x." + s.getKey() + "=b1." + s.getValue() + " AND ");
        }
       sb.delete(sb.lastIndexOf("AND"),sb.length()-1);
        //return (p),count(b1)>3,count(b1)
        sb.append(" return (p),count(b1)>"+ count + ",count(b1)");
//        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
//        return jsonStr;
        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        return jsonStr;
    }

    @Override
    public Map<Object, String> findInCompanionAll(List<Object> startObjList, Object relationship, int count, String... whereProp) {
        Map<Object, String> map = new HashMap<>();
        for (Object obj : startObjList) {
            String inCompanion = this.findInCompanion(obj, relationship, count, whereProp);
            map.put(obj, inCompanion);
        }
        return map;
    }

    /**
     * match (a:Person{})-[r:Nexus{name:'标签'}]->(b:Lable{}) WHERE b.name = '农民'
     * with a AS a1 match (a1)-[r:Nexus{name:'标签'}]->(b:Lable{}) WHERE b.name = '房多多'
     * with a1 AS a2  match (a2)-[r:Nexus{name:'标签'}]->(b:Lable{})
     * WHERE b.name = '钱多多'  return (a2)
     */
    @Override
    public String findSameNode(Object startNodeName, Object relationshipModel, List<Object> rsNode) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < rsNode.size(); i++) {
        	Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
            Master s = conn.createMaster();
            String nodeStr = s.createNodeModel().createWhereNodeObj("b", rsNode.get(i)).nodeStr();
            list.add(nodeStr);
        }
        StringBuilder sb = new StringBuilder("MATCH ");
        Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
        Master m = conn.createMaster();
        String str = m.createNodeModel().createWhereNodeObj("a", startNodeName).nodeStr();
        String relationshipObjStr = m.createRelationshipModel().createWhereRelationshipStr("r", relationshipModel);
        sb.append(str + "-");
        sb.append(relationshipObjStr + "->" + list.get(0));
        String nodeNameStart = "a";
        String nodeNameEnd = "a";
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                nodeNameEnd += "a";
//               with a AS a1 match (a1)-[r:Nexus{name:'标签'}]->(b:Lable{}) WHERE b.name = '房多多'
                sb.append(" WITH " + nodeNameStart + " AS " + nodeNameEnd);
                sb.append(" MATCH (" + nodeNameEnd + ")-" + relationshipObjStr);
                sb.append("->" + list.get(i));
                nodeNameStart = nodeNameEnd;
            }
        }
        sb.append(" RETURN (" + nodeNameEnd + ")");
        System.out.println(sb.toString());
//        System.out.println(DriverManager.url);
//        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        String jsonStr = DriverManager.sendPost_too(url, sb.toString(), username, password);
        return jsonStr;
    }


    @Override
    public String findShortestPath(Object startObj, List<String> relationshipStr, Object endObj, Integer min, Integer max) {
        StringBuilder sb = new StringBuilder("MATCH p = shortestPath(");
        Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
        Master master = conn.createMaster();
        String a = master.createNodeModel().createWhereNodeObj("a", startObj).nodeStr();
        sb.append(a);
        sb.append("-");
        sb.append("[r:");
        for (String str : relationshipStr) {
            sb.append(str + "|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("*");
        sb.append(min);
        sb.append("..");
        sb.append(max);
        sb.append("]");
        sb.append("-");
        String b = master.createNodeModel().createWhereNodeObj("b", endObj).nodeStr();
        sb.append(b);
        sb.append(") RETURN p");
//        String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
        String jsonStr= DriverManager.sendPost_too(url, sb.toString(), username, password);
//        return null;
        return jsonStr;
    }

    /**
     * WITH ['141221199000689765','141221199000027194'] AS x Match
     * p=(a:person)-[r:夫妻|同学|同事|亲人|邻居|朋友*0..3]-(b:person{nation:'维吾尔'})
     * WHERE a.id IN x return b.gender,collect(b)
     *
     * @param ids
     * @param startNodeName
     * @param relationshipStr
     * @param endNode
     * @param groundProp
     * @return
     */
    @Override
    public String findShortestPathPropGroup(final String mark,final List<String> ids, final String startNodeName, final List<String> relationshipStr, final Object endNode,final Integer min,final Integer max, final String groundProp){
//        BlockingQueue<String> queue = new LinkedBlockingQueue();
//        Map<String,ObjectNode>

//        for (String id : ids){
//                queue.add(id);
//        }
//        for(String id : queue){
//            System.out.println(id);
//        }
        int size = ids.size();
        int threadNumber = 0;
        final int listNumber = size < 10 ? size : size / 10;

        if(size<10){
            threadNumber = 1;
        }else{
            threadNumber = size % 10 == 0 ? 10 : 11;
        }
        final Map<String, List<JsonNode>> resultMap = new HashMap<>();

//        Map<String,List<JsonNode>> resultMap = new HashMap<>();
        //创建线程池
        ExecutorService executors = Executors.newCachedThreadPool();
        final CountDownLatch countDownLatch = new CountDownLatch(threadNumber);
        for(int i = 0 ; i < threadNumber ; i++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder sb = new StringBuilder("WITH ");
                    sb.append("[");
                    if(ids.size() <= 0){
                        countDownLatch.countDown();
                        return;
                    }
                    int a = 0;
                    for(String id : ids){
                        if( listNumber < a) break;
                        sb.append("'" + id + "',");
                        a++;
                    }
//                    for (int i = 0 ; i < listNumber ; i++){
//
//                        System.out.println("###################"+listNumber);
////                        sb.append("'" + queue. + "',");
//                        sb.append("'" + queue. + "',");
////                        if(queue.poll() == null) break;
//                    }

                    sb.deleteCharAt(sb.length()-1);
                    sb.append("] AS x ");
                    sb.append("MATCH ");
                    sb.append("p=(a:"+startNodeName+")-");
                    sb.append("[r:");
                    for(String str : relationshipStr){
                        sb.append(str + "|");
                    }
                    sb.deleteCharAt(sb.length()-1)   ;
                    sb.append("*"+min+".."+max);
                    sb.append("]-");
                    Neo4jConnection conn = new Neo4jConnection("192.168.128.59:7474", "neo4j", "tydic");
                    Master master = conn.createMaster();
                    sb.append(master.createNodeModel().createWhereNodeObj("b",endNode).nodeStr());
                    sb.append("WHERE a."+mark+" IN x ");
                    sb.append("RETURN b."+groundProp+",collect(b)");
//                    String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
                    String jsonStr= DriverManager.sendPost_too(url, sb.toString(), username, password);
//                    System.out.println("sb"+sb.toString());
//                    System.out.println("JsonStr"+jsonStr);
                    JsonNode result = null;
                    try {
                        result =  mapper.readTree(jsonStr);
                        JsonNode jsonData = result.get("results").get(0).get("data");
                        for(int i = 0 ; i < jsonData.size();i++){
                            JsonNode jsonRow = jsonData.get(i).get("row");
                            if(resultMap.get(jsonRow.get(0))==null){
                                List<JsonNode> listnode = new ArrayList<>();
                                resultMap.put(jsonRow.get(0).asText(),listnode);
                                for (JsonNode jn : jsonRow.get(1)){
                                    listnode.add(jn);
                                }
//                    System.out.println("ss" + listnode);
                                resultMap.put(jsonRow.get(0).asText(),listnode);
                            } else {
                                for (JsonNode jn : jsonRow.get(1)){
                                    resultMap.get(0).add(jn);
                                }
//                    ((ArrayNode)resultMap.get(jsonRow.get(0))).addAll((ArrayNode)jsonRow.get(1));
                            }
                        }

//            mapper.writeValue();
//            results.get("data");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
            executors.submit(t);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String resultStr = "";
        try {
            resultStr = mapper.writeValueAsString(resultMap);

//            System.out.println("*******************************");
//            System.out.println("*******************************");
//            System.out.println("===" + resultStr);
//            System.out.println("*******************************");
//            System.out.println("*******************************");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    //查询两组节点之间的关系
    @Override
    public String queryTwoGroup(RelationTwoGroupModel relationTwoGroupModel) {
        StringBuffer sb = new StringBuffer();
        List<String> list1 = relationTwoGroupModel.getIdList1();
        List<String> list2 = relationTwoGroupModel.getIdList2();
        String label1Name = relationTwoGroupModel.getLabel1Name();
        String label2Name = relationTwoGroupModel.getLabel2Name();
        String label1Id = relationTwoGroupModel.getLabel1Id();
        String label2Id = relationTwoGroupModel.getLabel2Id();
        int depth = relationTwoGroupModel.getDepth();
        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0) {
            return "";
        }
        long start = System.currentTimeMillis();
        List<String> relationList = relationTwoGroupModel.getRelationType();
        List<List<String>> splitList = Tools.splitList(list1, list1.size() / 10);
        ObjectNode jsonReturn = mapper.createObjectNode();
        org.codehaus.jackson.node.ArrayNode jsonReturnResults = mapper.createArrayNode();
        ObjectNode jsonReturnResult1 = mapper.createObjectNode();
        org.codehaus.jackson.node.ArrayNode jsonReturnData = mapper.createArrayNode();
        CountDownLatch latch = new CountDownLatch(splitList.size());
        QueryGroup queryGroup = new QueryGroup(list1,list2,label1Name,label2Name,label1Id,label2Id,relationList,depth,latch,jsonReturnData);
        for (List<String> sublist : splitList) {
            Thread thread = new Thread(queryGroup);
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        if (jsonReturnData != null) {
            jsonReturnResult1.put("data", jsonReturnData);
        }
        jsonReturnResults.add(jsonReturnResult1);
        jsonReturn.put("results", jsonReturnResults);
        jsonReturn.put("responseTime",(end-start));
        return jsonReturn.toString();
    }

    @Override
    public String queryInnerGroup(RelationInnerGroupModel relationInnerGroupModel) {
        List<String> list1 = relationInnerGroupModel.getIdList();
        String labelName = relationInnerGroupModel.getLabelName();
        String labelId = relationInnerGroupModel.getLabelId();
        int depth = relationInnerGroupModel.getDepth();
        List<String> relationList = relationInnerGroupModel.getRelationType();
        List<List<String>> splitList = Tools.splitList(list1, 100);
        ObjectNode jsonReturn = mapper.createObjectNode();
        org.codehaus.jackson.node.ArrayNode jsonReturnResults = mapper.createArrayNode();
        ObjectNode jsonReturnResult1 = mapper.createObjectNode();
        org.codehaus.jackson.node.ArrayNode jsonReturnData = mapper.createArrayNode();
        CountDownLatch latch = new CountDownLatch(splitList.size());
        for (List<String> sublist : splitList) {
        	QueryGroup queryGroup = new QueryGroup(sublist,list1,labelName,labelName,labelId,labelId,relationList,depth,latch,jsonReturnData);
            Thread thread = new Thread(queryGroup);
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jsonReturnResult1.put("data", jsonReturnData);
        jsonReturnResults.add(jsonReturnResult1);
        jsonReturn.put("results", jsonReturnResults);
        
        return jsonReturn.toString();
    }


    public class QueryGroup implements Runnable{
        private List<String> list1;
        private List<String> list2;
        private String label1Name;
        private String label2Name;
        private String id1;
        private String id2;
        private List<String> relationList;
        private int depth;
        private CountDownLatch latch;
        private ArrayNode jsonReturnData;

        public QueryGroup(List<String> list1, List<String> list2, String label1Name, String label2Name, String id1, String id2, List<String> relationList, int depth, CountDownLatch latch, ArrayNode jsonReturnData) {
            this.list1 = list1;
            this.list2 = list2;
            this.label1Name = label1Name;
            this.label2Name = label2Name;
            this.id1 = id1;
            this.id2 = id2;
            this.relationList = relationList;
            this.depth = depth;
            this.latch = latch;
            this.jsonReturnData = jsonReturnData;
        }

        public void run() {
            StringBuffer sql = new StringBuffer(" WITH [");
            for (int i = 0; i < list1.size(); i++) {
                String id1 = list1.get(i);
                if (i == 0) {
                    sql.append("'" + id1 + "'");
                } else {
                    sql.append(",'" + id1 + "'");
                }
            }
            sql.append("] AS x ");
            sql.append(",[ ");
            for (int j = 0; j < list2.size(); j++) {
                String id2 = list2.get(j);
                if (j == 0) {
                    sql.append("'" + id2 + "'");
                } else {
                    sql.append(",'" + id2 + "'");
                }
            }
            sql.append("] AS y ");
            sql.append(" MATCH (a:" + label1Name + ") WHERE a." + id1 + " IN x");
            sql.append(" MATCH (b:" + label2Name + ") WHERE b." + id2 + " IN y");
            sql.append(" MATCH p=(a)-[");
            if (relationList != null && relationList.size() > 0) {
                sql.append("r:" + StringUtils.join(relationList, "|"));
            }else{
            	sql.append("r");
            }
            sql.append("*1.." + depth + "]-(b) WHERE a<>b RETURN r");
            System.out.println("sql" + sql);
            Long start = System.currentTimeMillis();
//            String result = DriverManager.sendPost(DriverManager.url, sql.toString());
            String result= DriverManager.sendPost_too(url, sql.toString(), username, password);
            Long end = System.currentTimeMillis();
            System.out.println("currentTimeMillis***********"+(end - start));
            try {
                JsonNode josn = mapper.readTree(result);
                JsonNode resultsJson = josn.get("results");
                if (resultsJson.size()!=0){
                ArrayNode dataJson = (ArrayNode) resultsJson.get(0).get("data");
 //               System.out.println("result" + dataJson);
                if (dataJson != null) {
                    jsonReturnData.addAll(dataJson);
                }
              }
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


	@Override
	public String testSpringCloud(String name) {
		return "进来了...进来了..存入参数为："+name;
	}

	@Override
	public JSONObject getResultInfo(String kid,String kvals,String gxcs,String  gxlbid) {
		long cha1=System.currentTimeMillis();
		JSONObject result = new JSONObject();
		String MC = JSONDataUtil.getJsonKeyWordById(kid);//根据kid确实是人找物/人还是物找人
		JSONObject nodes = new JSONObject();
		String code="1";//默认给1，kid为空就给0
		if(kid !=null && kid.length()>0){//kid为空返回查询失败
			if("118".equals(kid)){//根据kid确实是人找物/人还是物找人
				long cha22=System.currentTimeMillis();
				nodes = queryZJHMInfo(kvals,kid,gxcs,gxlbid);
				long cha222=System.currentTimeMillis();
				System.out.println("cha222-cha22时间："+(cha222-cha22));
			}else{//物找人
				nodes =  queryWPInfo(kvals,kid,gxcs,gxlbid);
			}
		}else {
			code="0";
		}
		result.put("code", code);
		result.put("result", nodes);
		result.put("took", 141);
		result.put("Jobtasksession", "d67ef511-1b31-401d-99d4-de0b888129b9");
		long cha2=System.currentTimeMillis();
		System.out.println("全部解析完时间："+(cha2-cha1));
		return result;
	}
	
	//关系圈根据电话找关系
	public JSONObject queryWPInfo(String kvals,String kid,String gxcs,String gxlbid){
		JSONDataUtil jsonData = new JSONDataUtil();
//		String neo4jName=JSONDataUtil.getNeo4jNameByKID(kid);
		String relation = findDeptPhoneQuery(kvals,Integer.parseInt(gxcs));
//		System.out.println("neo手机号relation："+relation);
		ArrayList neo4jData = findDataJson(relation);
		
		JSONObject resultCh = new JSONObject();
		JSONObject node = new JSONObject();
		JSONArray endsArr = new JSONArray();
		JSONArray linsArr = new JSONArray();
		int size = neo4jData.size();
		int i = 0;
		String dxid = jsonData.getJsonKeyWordByvalue("证件号码").get("DXID").toString();
		String gjzid = jsonData.getJsonKeyWordByvalue("证件号码").get("ID").toString();
		int linkSize = 0;
		List<Map<String,String>> list = jsonData.getAllList();
		for (; i < size; i++) {
			JSONObject endsObj = new JSONObject();
			Map<String, Object> rowObj = (Map)neo4jData.get(i);
			ArrayList rowArr = (ArrayList)rowObj.get("row");
			if(rowArr.get(0)!=null && ((ArrayList) rowArr.get(0)).size()>0){
				Map<String, Object> nodeObj = (Map)rowArr.get(1);
				String ZJHM = (String)nodeObj.get("ZJHM");
				
				endsObj.put("nodeId",ZJHM+"_"+gjzid);
				endsObj.put("key",ZJHM);
				endsObj.put("dxid",dxid);
				endsObj.put("gjzid", gjzid);
				endsObj.put("lybid","2a4f5e1b-cf09-49c5-91a3-a21a310dd9af");//
				endsObj.put("lybmc", "人与人关系主题库");//
				endsArr.add(endsObj);
				
				ArrayList link = (ArrayList)rowArr.get(0);
				linkSize = link.size();
				if(linkSize > 0){
					int j = 0;
					for (; j < linkSize; j++) {
						JSONObject linksObj = new JSONObject();
						Map<String, Object> linkObj = (Map)link.get(j);
						String GXLX = (String)linkObj.get("GXLX");
						String GXID = (String)linkObj.get("GXID");

						String[] arr = GXID.split("_");
						String gxid = getGxid(GXLX,list);
						
						linksObj.put("edgeName", gxid);
						linksObj.put("fromNodeId", arr[1]+"_"+gjzid);
						linksObj.put("toNodeId", arr[3]+"_"+kid);
						linksObj.put("gxsjl", "");
						linksObj.put("dgxbh", "");
						linksObj.put("dgxmc", "");
						linksObj.put("gxid", gxid);
						linksObj.put("gxgzid", "1027");//
						linksObj.put("cs", 1);//
						linksObj.put("edgeId", arr[1]+"_"+arr[3]);
						linksObj.put("edgeLabel", GXLX);
						linksObj.put("desc", GXLX+",1次 ,来自人与人关系主题库");//
						
						linsArr.add(linksObj);
					}
				}
				
			}
		}
		node.put("ends", endsArr);
		node.put("links", linsArr);
		resultCh.put(kvals+"_"+kid, node);
//		System.out.println("WPresultCh:"+resultCh);
		return resultCh;
	}
	
	//关系圈根据证件号码找关系
	public JSONObject queryZJHMInfo(String kvals,String kid,String gxcs,String gxlbid){
		JSONDataUtil jsonData = new JSONDataUtil();
		Set<String> gxList = new HashSet<String>();
		if("10".equals(gxlbid)){
			gxlbid = "";
		}
		gxList = JSONDataUtil.getJsonListGxInfoByValue(gxlbid);
		long neo4jSeach = System.currentTimeMillis();
//		String neo4jName=JSONDataUtil.getNeo4jNameByKID(kid);
		String relation = findDepthOwnQuery(gxlbid,kvals, gxList, Integer.parseInt(gxcs));
//		System.out.println("neo证件relation："+relation);
		long neo4jSeach2 = System.currentTimeMillis();
		System.out.println("neo4j查询时间："+(neo4jSeach2-neo4jSeach));
		long chaK1=System.currentTimeMillis();
		ArrayList neo4jData = findDataJson(relation);
		
		JSONObject resultCh = new JSONObject();
		JSONObject node = new JSONObject();
		JSONArray endsArr = new JSONArray();
		JSONArray linsArr = new JSONArray();
		int size = neo4jData.size();
		int i = 0;
		String dxid = "";
		String gjzid = "";
		String yxdxid = jsonData.getValueByKeyWordList("邮箱","DXID");
		String yxgjzid = jsonData.getValueByKeyWordList("邮箱","ID");
		String sjhmdxid = jsonData.getValueByKeyWordList("电话","DXID");
		String sjhmgjzid = jsonData.getValueByKeyWordList("电话","ID");
		String cphdxid = jsonData.getValueByKeyWordList("车牌号","DXID");
		String cphgjzid = jsonData.getValueByKeyWordList("车牌号","ID");
		String zjhmdxid = jsonData.getValueByKeyWordList("证件号码","DXID");;
		String zjhmgjzid = jsonData.getValueByKeyWordList("证件号码","ID");
		int linkSize = 0;
		List<Map<String,String>> list = jsonData.getAllList();
		for (; i < size; i++) {
			JSONObject endsObj = new JSONObject();
			Map<String, Object> rowObj = (Map)neo4jData.get(i);
			String labels = getLabels(rowObj);
			ArrayList rowArr = (ArrayList)rowObj.get("row");
			if(rowArr.get(0)!=null && ((ArrayList) rowArr.get(0)).size()>0){
				Map<String, Object> nodeObj = (Map)rowArr.get(1);
				String ZJHM = "";
				if(nodeObj.get("ZJHM")==null){
					String WPLX = (String)nodeObj.get("WPLX");
					if("邮箱".equals(WPLX)){//与neo4j中WPLX一致
						ZJHM = (String)nodeObj.get("WPID");
						dxid = yxdxid;
						gjzid = yxgjzid;
					}else if("手机号码".equals(WPLX)){//与neo4j中WPLX一致
						ZJHM = (String)nodeObj.get("WPID");
						dxid = sjhmdxid;
						gjzid = sjhmgjzid;
					}else if("车牌号码".equals(WPLX)){//与neo4j中WPLX一致
						ZJHM = (String)nodeObj.get("WPID");
						dxid = cphdxid;
						gjzid = cphgjzid;
					}
				}else{
					ZJHM = (String)nodeObj.get("ZJHM");
					dxid = zjhmdxid;
		    		gjzid = zjhmgjzid;
				}
				endsObj.put("nodeId",ZJHM+"_"+gjzid);
				endsObj.put("key",ZJHM);
				endsObj.put("dxid",dxid);
				endsObj.put("gjzid", gjzid);
				endsObj.put("lybid","2a4f5e1b-cf09-49c5-91a3-a21a310dd9af");//
				endsObj.put("lybmc", "人与人关系主题库");//
				endsArr.add(endsObj);
				
				ArrayList link = (ArrayList)rowArr.get(0);
				linkSize = link.size();
				if(linkSize > 0){
					int j = 0;
					for (; j < linkSize; j++) {
						JSONObject linksObj = new JSONObject();
						Map<String, Object> linkObj = (Map)link.get(j);
						String GXLX = (String)linkObj.get("GXLX");
						String GXID = (String)linkObj.get("GXID");

						String[] arr = GXID.split("_");
						String gxid = getGxid(GXLX,list);
						
						linksObj.put("edgeName", gxid);
						linksObj.put("fromNodeId", arr[1]+"_"+kid);
						linksObj.put("toNodeId", arr[3]+"_"+gjzid);
						linksObj.put("gxsjl", "");
						linksObj.put("dgxbh", "");
						linksObj.put("dgxmc", "");
						linksObj.put("gxid", gxid);
						linksObj.put("gxgzid", "1027");//
						linksObj.put("cs", 1);//
						linksObj.put("edgeId", arr[1]+"_"+arr[3]);
						linksObj.put("edgeLabel", GXLX);
						linksObj.put("desc", GXLX+",1次 ,来自人与人关系主题库");//
						
						linsArr.add(linksObj);
					}
				}
				
//				if("02".equals(gxlbid)){
//					ZJHM = (String)nodeObj.get("WPID");
//		    	}else if("04".equals(gxlbid)){
//		    		ZJHM = (String)nodeObj.get("WPID");
//		    	}else{
//		    		ZJHM = (String)nodeObj.get("ZJHM");
//		    	}
				
				
				
			}
		}
		node.put("ends", endsArr);
		node.put("links", linsArr);
		resultCh.put(kvals+"_"+kid, node);
		long chaJ1=System.currentTimeMillis();
//		System.out.println("ZJHMresultCh:"+resultCh);
		System.out.println("ZJHMneo4j解析时间:"+(chaJ1-chaK1));
		return resultCh;
	}
	
	private String getLabels(Map<String, Object> rowObj) {
		Map<String, Object> graph = (Map<String, Object>) rowObj.get("graph");
		ArrayList nodes = (ArrayList)graph.get("nodes");
		Map<String, Object> labelsObj = new HashMap<String, Object>();
		String labels = "";
		if(nodes.size()>1){
			labelsObj = (Map<String, Object>)nodes.get(1);
			ArrayList labelsArr = (ArrayList)labelsObj.get("labels");
			labels = (String)labelsArr.get(0);
		}
		
		return labels;
	}

	public Map<String,String> findMapKeyWord(String MC,String gxlbid){
		JSONDataUtil jsonData = new JSONDataUtil();
		Map<String,String> map = new HashMap<String, String>();
		String dxid = "";
		String gjzid = "";
		if(MC.equals("证件号码")){
			if("02".equals(gxlbid)){
				dxid = jsonData.getJsonKeyWordByvalue("邮箱").get("DXID").toString();
				gjzid = jsonData.getJsonKeyWordByvalue("邮箱").get("ID").toString();
				
	    	}else if("04".equals(gxlbid)){
	    		dxid = jsonData.getJsonKeyWordByvalue("电话").get("DXID").toString();
	    		gjzid = jsonData.getJsonKeyWordByvalue("电话").get("ID").toString();
	    	}else{
	    		dxid = jsonData.getJsonKeyWordByvalue("证件号码").get("DXID").toString();
	    		gjzid = jsonData.getJsonKeyWordByvalue("证件号码").get("ID").toString();
	    	}
			map.put("dxid", dxid);
			map.put("gjzid", gjzid);
		}else if(MC.equals("电话")){
			dxid = jsonData.getJsonKeyWordByvalue("证件号码").get("DXID").toString();
    		gjzid = jsonData.getJsonKeyWordByvalue("证件号码").get("ID").toString();
    		map.put("dxid", dxid);
			map.put("gjzid", gjzid);
		}
		
		return map;
	}

	private String getGxid(String gxlx, List<Map<String, String>> list) {
		String returnStr = "";
		for(int i=0;i<list.size();i++){
			Map<String,String> map=list.get(i);
			if(gxlx.equals(map.get("GXMC"))){
				returnStr=map.get("GXID");
			}
		}
		return returnStr;
	}


	public ArrayList findDataJson(String relation){
		ArrayList data = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> user = null;
		try {
			user= mapper.readValue(relation, Map.class);
			ArrayList resultsArr = (ArrayList)user.get("results");
			if(resultsArr.size()>0){
				Map<String, Object> resultsObj = (Map)resultsArr.get(0);
				data = (ArrayList)resultsObj.get("data");
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	public Set<String> findIds(JSONArray data){
		int size = data.size();
		int i = 0;
		Set<String> idArr = new HashSet<String>();
		for (; i < size; i++) {
			JSONObject rowObj = data.getJSONObject(i);
			JSONArray rowArr = rowObj.getJSONArray("meta");
			String id = rowArr.getJSONObject(0).getString("id");
			idArr.add(id);
		}
		
		return idArr;
	}

	@Override
	public String findPersonNode(Integer sign, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findAttrByNodeType(String nodeType, String attr) {
		List<String> resultList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("MATCH ");
		sb.append(" ");
		sb.append("(n:");
		sb.append(nodeType+"{}) ");
		sb.append("return ");
		sb.append("n."+attr);
//		String jsonStr = DriverManager.sendPost(DriverManager.url, sb.toString());
		String jsonStr= DriverManager.sendPost_too(url, sb.toString(), username, password);
		com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject();
		JSONArray dataArry = jsonObj.parseObject(jsonStr).getJSONArray("results").getJSONObject(0).getJSONArray("data");
		for(int i = 0;i < dataArry.size();i++){
			String col = dataArry.getJSONObject(i).getJSONArray("row").getString(0);
			resultList.add(col);
		}
		return resultList;
	}

	@Override
	public void createDas5RRGXById(String zjhm1, String zjhm2, String gx,String gxmc) {
		//MATCH (a:People{ZJHM:"610909090931132412"}),(b:People{ZJHM:"610909090990547370"}) 
		//create (a)-[n:A07{ZJLXDM1:"111",GXLX:"同事",GXID:"111_610909090931132412_111_610909090990547370_A07"}]->(b) 
		//return n
		
		StringBuilder sb = new StringBuilder("MATCH ");
		sb.append("(a:People{ZJHM:'"+zjhm1+"'}),(b:People{ZJHM:'"+zjhm2+"'}) ");
		sb.append("create (a)-");
		sb.append("[n:");
		sb.append(gx+"{");
		sb.append("ZJLXDM1:'111',");
		sb.append("GXLX:'"+gxmc+"',");
		sb.append("GXID:'111_"+zjhm1+"_111_"+zjhm2+"_"+gx+"'");
		sb.append("}");
		sb.append("]->(b) ");
		sb.append("return n");
//		System.out.println(sb.toString());
		String jsonStr = DriverManager.sendInsertPost(this.url, sb.toString(), this.username, this.password);
//		String jsonStr = DriverManager.sendInsertPost(DriverManager.url, sb.toString());
//		System.out.println(jsonStr);
//		com.alibaba.fastjson.JSONObject jsonObj = new com.alibaba.fastjson.JSONObject();
	}

	@Override
	public String findPersonsGXById(List<String> pList) {
		// WITH ['342622195310307128','342622199105227091','740793197207011279','266242195802036384'] AS x 
		// MATCH (a:People)-[r*0..1]-(b) 
		// WHERE a.ZJHM in x  return (b)
		StringBuilder sb = new StringBuilder("WITH ");
		sb.append("[");
		for(String id : pList){
			sb.append("'"+id+"',");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		sb.append(" AS ");
		sb.append("x ");
		sb.append("MATCH (a:People)-[r*0..1]-(b) WHERE a.ZJHM in x  return (b)");
		
//		String jsonStr = DriverManager.sendInsertPost(DriverManager.url, sb.toString());
		String jsonStr = DriverManager.sendInsertPost(this.url, sb.toString(), this.username, this.password);
		return jsonStr;
	}
	
	public String info(){
		return "11111111122222224444422234";
	}

	@Override
	public Map<String, String> getAllGXMCMap() {
		return Constants.getAllGXMCMap();
	}

	public JSONArray parseDataArr(String neo4jJSONStr) {
		JSONObject neo4jJSON = JSONObject.parseObject(neo4jJSONStr);
		JSONArray resultArr = neo4jJSON.getJSONArray("results");
		JSONObject dataJson =  (JSONObject) resultArr.get(0);
		JSONArray  dataArr = dataJson.getJSONArray("data");
		
		
		return dataArr;
	}
	public String selectDepthOwnQuery(Object startObj,List<String> gxList,Integer depth,Object entObj,RelationService.Neo4jResult neo4jResults ){
		//MATCH p=((n:People{ZJHM:'610909090931132412'})-[r:C01|B05*0..1]-(b)) RETURN (b)
		if(depth == null || depth == 0){
			depth = 1;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("MATCH ");
		sb.append("p = (");
		String nodestr = new NodeModel().createWhereNodeObj("a",startObj).nodeStr(AttrDisplayModel.uppercase_all);
		sb.append(nodestr);
		if(gxList == null || gxList.size() == 0){
			sb.append("-[r*0..");
			sb.append(depth);
			sb.append("]");
		}else{
			sb.append("-[r:");
			for(int i = 0 ; i < gxList.size() ; i++){
				sb.append(gxList.get(i) + "|");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("*0..");
			sb.append(depth);
			sb.append("]");
		}
		sb.append("-");
		if(entObj == null){
			sb.append("(b)");
		}else{
			String entStr = new NodeModel().createWhereNodeObj("b",entObj).nodeStr();
			sb.append(entStr);
		}
		sb.append(") RETURN ");
		if(neo4jResults == RelationService.Neo4jResult.gx_result){
			sb.append("(r)");	
		}else if(neo4jResults == RelationService.Neo4jResult.node_result){
			sb.append("(b)");	
		}else{
			sb.append("(p)");	
		}
//		System.out.println("neo4j======"+sb.toString());
//		String url_ip = "http://192.168.128.59:7474/db/data/transaction/commit";
		String json_result = DriverManager.sendPost_too(this.url, sb.toString(), this.username, this.password);
		return json_result;
	}

	@Override
	public void createNode(Object obj,AttrDisplayModel attrDisplayModel) {
		StringBuilder sb = new StringBuilder("CREATE ");
		NodeModel nodeModel = new NodeModel();
		sb.append(nodeModel.createWhereNodeObj("a", obj).nodeStr(attrDisplayModel));
		 DriverManager.sendPost_too(this.url, sb.toString(), this.username, this.password);
	}

	@Override
	public void createIndex(String neo4jType, List<String> attrList) {
		//CREATE INDEX ON :AddressNode( preAddressNodeGUIDs)
		for(String attr : attrList){
			StringBuilder sb = new StringBuilder("CREATE INDEX ON ");
			sb.append(":");
			sb.append(neo4jType);
			sb.append("(");
			sb.append(attr);
			sb.append(")");
			DriverManager.sendPost_too(this.url, sb.toString(), this.username, this.password);
		}
	}

	@Override
	public void removeNode(Object obj,AttrDisplayModel attrDisplayModel) {
		NodeModel nodeModel = new NodeModel();
		String nodeStr = nodeModel.createWhereNodeObj("a", obj).nodeStr(attrDisplayModel);
		StringBuilder r_sb = new StringBuilder("match ");
		r_sb.append(nodeStr);
		r_sb.append("-[r]-() delete (r)");
		 DriverManager.sendPost_too(this.url, r_sb.toString(), this.username, this.password);
		
		StringBuilder n_sb = new StringBuilder("match ");
		n_sb.append(nodeStr);
		n_sb.append(" delete (a)");
		 DriverManager.sendPost_too(this.url, n_sb.toString(), this.username, this.password);
	}

	@Override
	public void createRelation(Object node_a, Object node_b, Object relationObj) {
		NodeModel nodeModel = new NodeModel();
		String node_a_str = nodeModel.createWhereNodeObj("a", node_a).nodeStr();
		String node_b_str = nodeModel.createWhereNodeObj("b", node_b).nodeStr();
		RelationshipModel relationshipModel = new RelationshipModel();
		String relationStr = relationshipModel.createWhereRelationshipObj("r", relationObj).RelationshipStr();
		/**
		 * MATCH (a:tressa{id:"99980"}),(b:tressa{id:"99981"}) 
		 * create (a)-[n:gift{name:"礼物"}]->(b) return n 
		 */
		StringBuilder sb = new StringBuilder("MATCH ");
		sb.append(node_a_str);
		sb.append(",");
		sb.append(node_b_str);
		sb.append(" create ");
		sb.append("(a)-");
		sb.append(relationStr);
		sb.append("->(b) ");
		sb.append("return (r)");
		System.out.println(sb.toString());
		String result = DriverManager.sendPost_too(this.url, sb.toString(), this.username, this.password);
		System.out.println(result);
	}

}
