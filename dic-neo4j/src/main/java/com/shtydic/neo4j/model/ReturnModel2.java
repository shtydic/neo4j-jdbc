package com.shtydic.neo4j.model;

/**
 * Created by wanghong on 2016/11/22 12:58.
 */
public class ReturnModel2 {

    private String varName;
    private String name;
    private String asName;

    public ReturnModel2(String varName) {
        this.varName = varName;
    }

    public ReturnModel2(String name, String asName) {
        this.name = name;
        this.asName = asName;
    }
    public ReturnModel2(){}

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsName() {
        return asName;
    }

    public void setAsName(String asName) {
        this.asName = asName;
    }

}
