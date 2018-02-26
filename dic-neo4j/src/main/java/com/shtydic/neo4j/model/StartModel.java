package com.shtydic.neo4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghong on 2016/11/22 12:58.
 */
public class StartModel {
    private String indexName;//索引名称
    private String varName;
    private List<Object> values = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> opts = new ArrayList<>();

    public StartModel(String varName, String indexName) {
        this.varName = varName;
        this.indexName = indexName;
    }
    public void add(String name, String opt, Object value) {
        names.add(name);
        opts.add(opt);
        values.add(value);
    }
    /*getter setter*/

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getOpts() {
        return opts;
    }

    public void setOpts(List<String> opts) {
        this.opts = opts;
    }
}
