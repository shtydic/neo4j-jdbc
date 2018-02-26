package com.shtydic.neo4j.model;

/**
 * Created by wanghong on 2016/11/25 14:16
 */
public class NodeModel2 extends Neo4jModel {
    private String id;//节点id 如 RYID_1234:User
    private String name;//节点名称 如对象User
    private String property;//属性

    public NodeModel2(String id, String name, String property) {
        this.id = id;
        this.name = name;
        this.property = property;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
