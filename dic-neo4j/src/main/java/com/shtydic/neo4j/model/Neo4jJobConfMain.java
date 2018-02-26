package com.shtydic.neo4j.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Neo4jJobConfMain {
    private Integer id;        //id

    private Integer targetdb_id;  //目标库id

    private Integer ds_id;     //数据库id

    private String name;     //任务名称

    private String table_name;      //表名

    private Integer run_period;    //运行方式

    private String period_expression;   //周期表达式

    private Integer status = 0;     //状态   0:未发布   1:发布

    private Timestamp create_time;   //创建时间

    private Timestamp modify_time;  //修改时间

    private String creater;   //创建人

    private String modifier;   //修改人

    private String timestamp; //时间戳

    private String timestamp_type;// 时间戳类型

    private String timestamp_format;//时间戳格式

    private Date timestamp_start;

    private Date timestamp_stop;

    private String timestamp_field;//时间戳字段

    private String primary_key;//主键

    private String coverage;//是否覆盖 0 是，1 不是

    private Integer task_type;  //任务类型  1:代表关系型任务

    private String index_name;//索引名

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public Integer getTask_type() {
        return task_type;
    }

    public void setTask_type(Integer task_type) {
        this.task_type = task_type;
    }

    public String getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getTimestamp_field() {
        return timestamp_field;
    }

    public void setTimestamp_field(String timestamp_field) {
        this.timestamp_field = timestamp_field;
    }

    public Date getTimestamp_start() {
        return timestamp_start;
    }

    public void setTimestamp_start(Date timestamp_start) {
        this.timestamp_start = timestamp_start;
    }

    public Date getTimestamp_stop() {
        return timestamp_stop;
    }

    public void setTimestamp_stop(Date timestamp_stop) {
        this.timestamp_stop = timestamp_stop;
    }

    public String getTimestamp_format() {
        return timestamp_format;
    }

    public void setTimestamp_format(String timestamp_format) {
        this.timestamp_format = timestamp_format;
    }

    public String getTimestamp_type() {
        return timestamp_type;
    }

    public void setTimestamp_type(String timestamp_type) {
        this.timestamp_type = timestamp_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTargetdb_id() {
        return targetdb_id;
    }

    public void setTargetdb_id(Integer targetdb_id) {
        this.targetdb_id = targetdb_id;
    }

    public Integer getDs_id() {
        return ds_id;
    }

    public void setDs_id(Integer ds_id) {
        this.ds_id = ds_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Integer getRun_period() {
        return run_period;
    }

    public void setRun_period(Integer run_period) {
        this.run_period = run_period;
    }

    public String getPeriod_expression() {
        return period_expression;
    }

    public void setPeriod_expression(String period_expression) {
        this.period_expression = period_expression;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Timestamp modify_time) {
        this.modify_time = modify_time;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
    public String getRun_period_str(){
       // String str = "";
        int i = this.getRun_period();
        if(i == 0){
            return "";
        }else{
            return this.period_expression;
        }
    }

    public String getTimestamp_start_str(){
        if(this.timestamp_start != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(this.timestamp_start);
        }
        return null;
    }
    public String getTimestamp_stop_str(){
        if(this.timestamp_stop != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(this.timestamp_stop);
        }
        return null;
    }
    public String getRun_fun_str(){
        if(this.run_period == 0) return "一次性执行";
        if(this.run_period == 1)  return "周期性执行";
        return null;
    }
    @Override
    public String toString() {
        return "Neo4jJobConfMain{" +
                "id=" + id +
                ", targetdb_id=" + targetdb_id +
                ", ds_id=" + ds_id +
                ", name='" + name + '\'' +
                ", table_name='" + table_name + '\'' +
                ", run_period='" + run_period + '\'' +
                ", period_expression='" + period_expression + '\'' +
                ", status=" + status +
                ", create_time=" + create_time +
                ", modify_time=" + modify_time +
                ", creater='" + creater + '\'' +
                ", modifier='" + modifier + '\'' +
                '}';
    }
}