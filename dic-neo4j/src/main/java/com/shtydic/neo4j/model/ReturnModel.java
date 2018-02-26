package com.shtydic.neo4j.model;

import java.util.Map;

/**
 * Created by dengyichao on 2017/2/24.
 */
public class ReturnModel {
    StringBuilder sb;
    private Map<Object,String> returnNode;
    public ReturnModel(StringBuilder sb,Map returnNode) {
        this.sb = sb;
        this.returnNode = returnNode;
    }

    public void addResult(Object... obj){
        if(null != obj ||obj.length > 0) {
            sb.append(" RETURN ");
            for (Object a : obj) {
                for (Map.Entry<Object, String> map : returnNode.entrySet()) {
                    if (a == map.getKey()) {
                        sb.append("(" + map.getValue() + "),");
                    }
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
