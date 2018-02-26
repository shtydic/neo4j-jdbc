package com.shtydic.neo4j.model;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shtydic.neo4j.utils.ObjectUtil;

/**
 * Created by dengyichao on 2017/2/23.
 */
public class RelationshipModel {
    //存储别名和索引名
    private Map<String,String> names = new HashMap<>();
    private Map<String,String> relationshipModelMap = new HashMap<>();
    private List<String> whereList = new ArrayList<>();
    
    private String depth = "";
    public void setDepth(String depth) {
		this.depth = depth;
	}
    
	//类属性已经被抽离的集合，用于重复条件的添加
    private Map<String,String> existencePropMap = new HashMap<>();

    public RelationshipModel() {

    }

    /**
     * 把所有的属性变成map集合存储到relationshipModelMap
     * @param object
     * @return
     */
    public RelationshipModel createWhereRelationship(Object object){
        String indexName = ObjectUtil.className(object);
        names.put(UUID.randomUUID().toString(),indexName);
        relationshipModelMap = new ObjectUtil().objectMap(object);
        return this;
    }


    public String createWhereRelationshipStr(String alias,Object object){
        String indexName = ObjectUtil.className(object);
        names.put(alias,indexName);
        relationshipModelMap = new ObjectUtil().objectMap(object);
        return this.RelationshipStr();
    }
    
    public RelationshipModel createWhereRelationshipObj(String alias,Object object){
        String indexName = ObjectUtil.className(object);
        names.put(alias,indexName);
        relationshipModelMap = new ObjectUtil().objectMap(object);
        return this;
    }
    /**
     * 获取节点对应的标记名
     * @return
     */
    public String getNameMapKey(){
        String key = "";
        for (Map.Entry<String, String> mapName : names.entrySet()){
            key =  mapName.getKey();
        }
        return key;
    }


    public String addContain(String prop){
        return forNeo4jStr(prop,"Contains");
    }


    public String addGt(String prop){
        return forNeo4jStr(prop,">");
    }

    public String addLt(String prop){
        return forNeo4jStr(prop,"<");
    }

    public String addGtEq(String prop){
        return forNeo4jStr(prop,">=");
    }

    public String addLtEq(String prop){
        return forNeo4jStr(prop,"<=");
    }

    /**
     * 帮助处理运算符的方法
     * @param prop  属性名称
     * @param Symbol  对应的运算符符号
     * @return
     */
    public String forNeo4jStr(String prop,String Symbol){
        for(Map.Entry<String, String> map: relationshipModelMap.entrySet()){
            if(prop.equals(map.getKey())){
                String whereStr = this.getNameMapKey()+"."+prop + " "+Symbol+" '" + map.getValue() +"'";
                existencePropMap.put(map.getKey(),map.getValue());
                relationshipModelMap.remove(map.getKey());
                return whereStr;
            }
        }
        for(Map.Entry<String, String> map: existencePropMap.entrySet()){
            if(prop.equals(map.getKey())){
                String whereStr = this.getNameMapKey()+"."+prop + " "+Symbol+" '" + map.getValue() +"'";
                return whereStr;
            }
        }
        return "";
    }
    /**
     * 把节点的值变成相应的字符串
     * @return
     */
    public String RelationshipStr(){
        StringBuilder sb = new StringBuilder("[");
        String name = "";
        String indexName = "";
        for (Map.Entry<String, String> mapName : names.entrySet()){
            name = mapName.getKey();
            indexName = mapName.getValue();
        }
        sb.append(name+":");
        sb.append(indexName);
        //如果没有赋予属性则返回一个空节点
        if(relationshipModelMap == null || relationshipModelMap.size() == 0) {
        	if(this.depth != null && this.depth != "" ){
        		 String regEx = "^[0-9]{1}\\.{2}[0-9]*$";
        		 Pattern pattern = Pattern.compile(regEx);
        		 Matcher matcher = pattern.matcher(this.depth);
        		 boolean rs = matcher.matches();
        		 if(rs){
        			 sb.append("*");
        			 sb.append(this.depth);
        		 }else{
        			 new RuntimeException("关系深度格式不正确，正确格式为 \"0..4\"");
        		 }
        	}
            sb.append("]");
            return sb.toString();
        }
        sb.append("{");
        for (String key : relationshipModelMap.keySet()){
            sb.append(key);
            sb.append(":");
            if(relationshipModelMap.get(key) instanceof String){
                sb.append("'");
                sb.append(relationshipModelMap.get(key));
                sb.append("'");
                sb.append(",");
            }else{
                sb.append(relationshipModelMap.get(key));
                sb.append(",");
            }
        }
        sb = sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        sb.append("]");
        return sb.toString();
    }
}
