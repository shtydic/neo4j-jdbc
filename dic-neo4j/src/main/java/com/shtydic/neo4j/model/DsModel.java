package com.shtydic.neo4j.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dengyichao on 2016/12/9.14:53
 */
public class DsModel {
    private Integer id;                    //ds  id
    private String db_name;               //自定义数据库名称
    //数据类型  0=mysql  1=oracle  2=Neo4j 3=Elasticsearch
    private Integer db_type = 0;
    private String service_name;          //服务名称
    private String db_address;            //数据库地址+端口号
    private String db_username;           //账号
    private String db_password;           //密码
    private Date create_time;             //创建时间
    private Date modify_time;             //修改时间
    private String creater;               //创建人
    private String modifier;              //修改人


    public String getDatabaseUrl(){
        StringBuilder urlStr = new StringBuilder();
        //dbc:mysql://192.168.128.59:3306/tydic?useUnicode=true&characterEncoding=utf8
        if(db_type == 0){
            urlStr.append("jdbc:mysql://"+this.db_address+"/"+this.service_name);
        }
        if(db_type == 1){
            //jdbc:oracle:thin:@localhost:1521:allandb
            urlStr.append("jdbc:oracle:thin:@"+this.db_address+"/"+this.service_name);
        }
        if(db_type == 2){
            //"http://192.168.128.59:7474/db/data/";  neo4j数据库
            urlStr.append("http://"+this.db_address+"/db/data/");
        }
        if(db_type == 3){
            //Elasticsearch数据库
            urlStr.append("http://"+this.db_address+"/"+this.service_name);
        }
        return urlStr.toString();
    }
    public String getCreateDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.getCreate_time());
    }
    public String getModify_timeFormat(){
        if(this.modify_time == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(this.modify_time);
    }
    public String getDsTypeName(){
        if(db_type == 0) {
            return "MySql";
        }
        if(db_type == 1){
            return "Oracle";
        }
        if(db_type == 2){
            return "Neo4j";
        }
        if(db_type == 3){
            return "Elasticsearch";
        }
        return null;
    }
    public Integer getId() {
        return id;
    }

    public String getDb_name() {
        return db_name;
    }

    public Integer getDb_type() {
        return db_type;
    }

    public String getService_name() {
        return service_name;
    }

    public String getDb_address() {
        return db_address;
    }

    public String getDb_username() {
        return db_username;
    }

    public String getDb_password() {
        return db_password;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public String getCreater() {
        return creater;
    }

    public String getModifier() {
        return modifier;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public void setDb_type(Integer db_type) {
        this.db_type = db_type;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setDb_address(String db_address) {
        this.db_address = db_address;
    }

    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "DsModel{" +
                "id=" + id +
                ", db_name='" + db_name + '\'' +
                ", db_type=" + db_type +
                ", service_name='" + service_name + '\'' +
                ", db_address='" + db_address + '\'' +
                ", db_username='" + db_username + '\'' +
                ", db_password='" + db_password + '\'' +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                ", creater='" + creater + '\'' +
                ", modifier='" + modifier + '\'' +
                '}';
    }
}
