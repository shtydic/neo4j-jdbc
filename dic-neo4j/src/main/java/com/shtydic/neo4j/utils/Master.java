package com.shtydic.neo4j.utils;



import java.util.HashMap;
import java.util.Map;

import com.shtydic.neo4j.model.NodeModel;
import com.shtydic.neo4j.model.RelationshipModel;
import com.shtydic.neo4j.model.ReturnModel;
import com.shtydic.neo4j.model.WhereModel;

/**
 * 
 * <p>Title: Master</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午10:29:43
 */
public class Master {
	private String neo4j_ip_port;
	private String user;
	private String password;
	
    public Master(String neo4j_ip_port, String user, String password) {
		super();
		this.neo4j_ip_port = neo4j_ip_port;
		this.user = user;
		this.password = password;
	}

	//where
    private StringBuilder whereStr = new StringBuilder();

    //return
    private StringBuilder returnStr = new StringBuilder();

    //储存完整的字符串,match 点和线 所需要的全部数据
    private Map<Integer,StringBuilder> masterStrMap = new HashMap<>();
    private Map<Integer,DirType> dirMap = new HashMap<>();

    //用于存储使用过的所有节点或关系
    private Map<Object,String> returnNode = new HashMap<>();

    //关系方向
    public enum DirType {
        left, right,eq;
    }

    private Integer matchNumber = 0;

    public NodeModel createNodeModel(){
        return new NodeModel();
    }
    public RelationshipModel createRelationshipModel(){
        return new RelationshipModel();
    }
    public WhereModel createWhereModel(){ return new WhereModel(whereStr);}
    public ReturnModel createReturnModel(){ return new ReturnModel(returnStr,returnNode);}

    //开始查询
    public Master start(NodeModel nodeOne,RelationshipModel relationshipModel,NodeModel nodeSecond,Master.DirType dir){
        masterStrMap.put(matchNumber,new StringBuilder(nodeOne.nodeStr() + "-" + relationshipModel.RelationshipStr() + "_" + nodeSecond.nodeStr()));
        dirMap.put(matchNumber,dir);
        returnNode.put(nodeOne,nodeOne.getNameMapKey());
        returnNode.put(nodeSecond,nodeSecond.getNameMapKey());
        returnNode.put(relationshipModel,relationshipModel.getNameMapKey());
        matchNumber++;
        return this;
    }

    public Master addResult(Object... obj){
        this.createReturnModel().addResult(obj);
        return this;
    }

    //添加复杂查询的条件
    public Master addNote(RelationshipModel relationshipModel, NodeModel nodeOne) {
        StringBuilder sb = masterStrMap.get(matchNumber - 1);
        sb.append("-"+relationshipModel.RelationshipStr() + "_" + nodeOne.nodeStr());
        returnNode.put(nodeOne,nodeOne.getNameMapKey());
        returnNode.put(relationshipModel,relationshipModel.getNameMapKey());
        return this;
    }

    //创造字符串
    public String createComplexQueryStr() {
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        //拼装节点和关系及方向
        if(null != masterStrMap && masterStrMap.size()>0){
            for (int i = 0; i < masterStrMap.size(); i++) {
                String str = masterStrMap.get(i).toString();
                DirType dirType = dirMap.get(i);
                if(dirType == DirType.left){
                    str = str.replaceAll("-", "<-");
                    str = str.replaceAll("_", "-");
                }
                if(dirType == DirType.right){
                    str = str.replaceAll("_", "->");
                }
                if(dirType == DirType.eq){
                    str = str.replaceAll("_", "-");
                }
                sb.append(str);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(this.whereStr);
        sb.append(this.returnStr);
        return sb.toString();
    }
    
    public String request(){
    	String url = "http://" + this.neo4j_ip_port + "/db/data/transaction/commit";
    	String resultStr = this.createComplexQueryStr();
    	return DriverManager.sendPost_too(url, resultStr.toString(),this.user,this.password);
    }
}
