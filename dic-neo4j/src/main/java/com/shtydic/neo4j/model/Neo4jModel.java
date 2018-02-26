package com.shtydic.neo4j.model;

/**
 * Created by wanghong on 2016/11/22 11:10.
 */
public class Neo4jModel {
    public String varName;//变量名称  如Match (n:Person) WHERE n.name = '张三' return n;
    public static final String NODE = "node";
    public static final String RELATIONSHIP = "relationship";
    public static final String CREATE = "CREATE";

    public enum SelectType {
        node, relationship;
    }


    /*getter setter*/

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

}
