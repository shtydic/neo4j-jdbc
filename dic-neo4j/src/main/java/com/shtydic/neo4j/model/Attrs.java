package com.shtydic.neo4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghong on 2016/11/25 10:16.
 */
public class Attrs {
    public List<Attr> attrs = new ArrayList<>();

    public void addAttr(String name ,Object value){
        Attr attr = new Attr(name,value);
        attrs.add(attr);
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");
        for (Attr attr : attrs) {
            Object value = attr.getValue();
            String name = attr.getName();
            if (value instanceof String) {
                stringBuilder.append(name + ":'" + value + "'");
            } else if (value instanceof Long || value instanceof Integer || value instanceof Double) {
                stringBuilder.append(name + ":" + value);
            }
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public class Attr {
        private String name;
        private Object value;

        public Attr(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {

            return null;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }
}
