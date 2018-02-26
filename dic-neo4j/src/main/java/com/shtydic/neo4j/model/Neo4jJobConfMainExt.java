package com.shtydic.neo4j.model;

/**
 * Created by dengyichao on 2016/12/15.18:37
 */
public class Neo4jJobConfMainExt extends Neo4jJobConfMain {
    private String src_db_name;   //原数据库名称
    private String src_db_address; //原数据地址
    private Integer src_db_type;  //原数据类型
    private String target_db_name;  //目标数据库名称
    private String target_db_address; //目标数据库地址

    public String getSrc_db_name() {
        return src_db_name;
    }

    public void setSrc_db_name(String src_db_name) {
        this.src_db_name = src_db_name;
    }

    public String getSrc_db_address() {
        return src_db_address;
    }

    public void setSrc_db_address(String src_db_address) {
        this.src_db_address = src_db_address;
    }

    public Integer getSrc_db_type() {
        return src_db_type;
    }

    public void setSrc_db_type(Integer src_db_type) {
        this.src_db_type = src_db_type;
    }

    public String getTarget_db_name() {
        return target_db_name;
    }

    public void setTarget_db_name(String target_db_name) {
        this.target_db_name = target_db_name;
    }

    public String getTarget_db_address() {
        return target_db_address;
    }

    public void setTarget_db_address(String target_db_address) {
        this.target_db_address = target_db_address;
    }

    public String getSrc_db_typeStr(){
        int i = this.src_db_type;
        if(i == 0) {
            return "MySql";
        }
        if(i == 1){
            return "Oracle";
        }
        if(i == 2){
            return "Neo4j";
        }
        if(i == 3){
            return "Elasticsearch";
        }
        return null;
    }
    @Override
    public String toString() {
        return "Neo4jJobConfMainExt{" +
                "src_db_name='" + src_db_name + '\'' +
                ", src_db_address='" + src_db_address + '\'' +
                ", src_db_type=" + src_db_type +
                ", target_db_name='" + target_db_name + '\'' +
                ", target_db_address='" + target_db_address + '\'' +
                '}';
    }
}
