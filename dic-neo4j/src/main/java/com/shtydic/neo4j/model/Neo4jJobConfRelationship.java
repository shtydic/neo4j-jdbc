package com.shtydic.neo4j.model;




import java.util.Map;

/**
 * Created by dengyichao on 2017/2/9.
 */
public class Neo4jJobConfRelationship extends Neo4jJobConfMainExt{
    private String relation_id;
    private String start_node;          //节点一
    private String start_node_type;     //节点一类型
    private String start_node_index;//节点一对应图数据库的index
    private String end_node_index;//节点二对应图数据库的index
    private String end_node;            //节点二
    private String end_node_type;       //节点二类型
    private String relationship_type;   //关系类型
    private String relationship_prop;  //关系属性
    private Map<String,String> relationPropMap;//属性对应的字段,数据类型,键值对
    private String field_format;//格式化

    public Neo4jJobConfRelationship() {
    }

    public Neo4jJobConfRelationship(String relation_id, String start_node, String start_node_type, String end_node, String end_node_type, String relationship_type, String relationship_prop) {
        this.relation_id = relation_id;
        this.start_node = start_node;
        this.start_node_type = start_node_type;
        this.end_node = end_node;
        this.end_node_type = end_node_type;
        this.relationship_type = relationship_type;
        this.relationship_prop = relationship_prop;
    }

    public String getField_format() {
        return field_format;
    }

    public void setField_format(String field_format) {
        this.field_format = field_format;
    }

    public String getStart_node_index() {
        return start_node_index;
    }

    public void setStart_node_index(String start_node_index) {
        this.start_node_index = start_node_index;
    }

    public String getEnd_node_index() {
        return end_node_index;
    }

    public void setEnd_node_index(String end_node_index) {
        this.end_node_index = end_node_index;
    }

    public Map<String, String> getRelationPropMap() {
        return relationPropMap;
    }

    public void setRelationPropMap(Map<String, String> relationPropMap) {
        this.relationPropMap = relationPropMap;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getStart_node() {
        return start_node;
    }

    public void setStart_node(String start_node) {
        this.start_node = start_node;
    }

    public String getStart_node_type() {
        return start_node_type;
    }

    public void setStart_node_type(String start_node_type) {
        this.start_node_type = start_node_type;
    }

    public String getEnd_node() {
        return end_node;
    }

    public void setEnd_node(String end_node) {
        this.end_node = end_node;
    }

    public String getEnd_node_type() {
        return end_node_type;
    }

    public void setEnd_node_type(String end_node_type) {
        this.end_node_type = end_node_type;
    }

    public String getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(String relationship_type) {
        this.relationship_type = relationship_type;
    }

    public String getRelationship_prop() {
        return relationship_prop;
    }

    public void setRelationship_prop(String relationship_prop) {
        this.relationship_prop = relationship_prop;
    }
}
