package com.shtydic.neo4j.model;

/**
 * Created by wanghong on 2016/11/22 12:58.
 */
public class WhereModel2 {
    private String name;
    private String value;
    private String opt;

    public WhereModel2(String name, String value, String opt) {
        this.name = name;
        this.value = value;
        this.opt = opt;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
