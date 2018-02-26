package com.shtydic.neo4j.model;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shtydic.neo4j.utils.ObjectUtil;


/**
 * Created by dengyichao on 2017/2/22.
 */
public class NodeModel {
    //存储别名和索引名
    private Map<String,String> names = new HashMap<>();
    private Map<String,String> nodeMap = new HashMap<>();
    private List<String> whereList = new ArrayList<>();
    //类属性已经被抽离的集合，用于重复条件的添加
    private Map<String,String> existencePropMap = new HashMap<>();

    public NodeModel() {

    }

    /**
     * 把所有的属性变成map集合存储到nodeMap
     * @param object
     * @return
     */
    public NodeModel createWhereNode(Object object){
        String indexName = ObjectUtil.className(object);
        names.put(UUID.randomUUID().toString(),indexName);
        nodeMap = new ObjectUtil().objectMap(object);
        return this;
    }

    /**
     * 把一个对象变成字面量的节点
     * @param alias   给这个对象起别名
     * @param object  需要解析的对象
     * @return
     */
    public NodeModel createWhereNodeObj(String alias,Object object){
        String indexName = ObjectUtil.className(object);
        names.put(alias,indexName);
        nodeMap = new ObjectUtil().objectMap(object);
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

    /**
     * 获取节点对应的节点名称
     * @return
     */
    public String getIndexName(){
        String key = "";
        for (Map.Entry<String, String> mapName : names.entrySet()){
            key =  mapName.getValue();
            break;
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
        for(Map.Entry<String, String> map: nodeMap.entrySet()){
            if(prop.equals(map.getKey())){
            	String whereStr = null;
            	if(Symbol.equals("Contains")){
            		whereStr = this.getNameMapKey()+"."+prop + "=~'.*"+map.getValue()+".*'";
            	}else{
            		whereStr = this.getNameMapKey()+"."+prop + " "+Symbol+" '" + map.getValue() +"'";
            	}
                existencePropMap.put(map.getKey(),map.getValue());
                nodeMap.remove(map.getKey());
                return whereStr;
            }
        }
        for(Map.Entry<String, String> map: existencePropMap.entrySet()){
            if(prop.equals(map.getKey())){
                String whereStr = null;
                if(Symbol.equals("Contains")){
            		whereStr = this.getNameMapKey()+"."+prop + "=~'.*"+map.getValue()+".*'";
            	}else{
            		whereStr = this.getNameMapKey()+"."+prop + " "+Symbol+" '" + map.getValue() +"'";
            	}
                return whereStr;
            }
        }
        return "";
    }

    /**
     * 把节点的值变成相应的字符串
     * @return
     */
    public String nodeStr(AttrDisplayModel attrDisplayModel){
        StringBuilder sb = new StringBuilder("(");
        String name = "";
        String indexName = "";
	        for (Map.Entry<String, String> mapName : names.entrySet()){
	            name = mapName.getKey();
	            indexName = mapName.getValue();
	        }
        
        sb.append(name+":");
        sb.append(indexName);
        //如果没有赋予属性则返回一个空节点
        if(nodeMap == null || nodeMap.size() == 0) {
            sb.append(")");
            return sb.toString();
        }
        sb.append("{");

        for (String key : nodeMap.keySet()){
        	if(attrDisplayModel == null){
        		sb.append(key);
        	}else{
        		if(AttrDisplayModel.java_standard == attrDisplayModel){
        			sb.append(key);
        		}else if(AttrDisplayModel.lowercase_all == attrDisplayModel){
        			sb.append(key.toLowerCase());
        		}
        		else if(AttrDisplayModel.lowercase_first == attrDisplayModel){
        			 StringBuilder th = new StringBuilder(key);
	        		 th.setCharAt(0,Character.toLowerCase(th.charAt(0)));
	        		 sb.append(th.toString());
        		}
        		else if(AttrDisplayModel.uppercase_all == attrDisplayModel){
        			sb.append(key.toUpperCase());
        		}
        		else if(AttrDisplayModel.uppercase_first == attrDisplayModel){
        			StringBuilder th = new StringBuilder(key);
	        		 th.setCharAt(0,Character.toUpperCase(th.charAt(0)));
	        		 sb.append(th.toString());
        		}
        	}
            sb.append(":");
            if(nodeMap.get(key) instanceof String){
                sb.append("'");
                sb.append(nodeMap.get(key));
                sb.append("'");
                sb.append(",");
            }else{
                sb.append(nodeMap.get(key));
                sb.append(",");
            }
        }
        sb = sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        sb.append(")");
//        System.out.println(whereList.toString());
        return sb.toString();
    }

    public String nodeStr(){
        StringBuilder sb = new StringBuilder("(");
        String name = "";
        String indexName = "";
	        for (Map.Entry<String, String> mapName : names.entrySet()){
	            name = mapName.getKey();
	            indexName = mapName.getValue();
	        }
        
        sb.append(name+":");
        sb.append(indexName);
        //如果没有赋予属性则返回一个空节点
        if(nodeMap == null || nodeMap.size() == 0) {
            sb.append(")");
            return sb.toString();
        }
        sb.append("{");

        for (String key : nodeMap.keySet()){
        	sb.append(key);
            sb.append(":");
            if(nodeMap.get(key) instanceof String){
                sb.append("'");
                sb.append(nodeMap.get(key));
                sb.append("'");
                sb.append(",");
            }else{
                sb.append(nodeMap.get(key));
                sb.append(",");
            }
        }
        sb = sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        sb.append(")");
//        System.out.println(whereList.toString());
        return sb.toString();
    }
}
