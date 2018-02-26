package com.shtydic.neo4j.utils;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.shtydic.neo4j.model.MatchModel;
import com.shtydic.neo4j.model.Neo4jModel;
import com.shtydic.neo4j.model.QueryModel;
import com.shtydic.neo4j.model.ReturnModel2;
import com.shtydic.neo4j.model.StartModel;
import com.shtydic.neo4j.model.WhereModel2;

/**
 * 
 * <p>Title: QueryBuilder</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午10:31:31
 */
public class QueryBuilder {

    /**
     * 构造Match
     *
     * @param qm
     * @return String
     */
    public String builderStart(QueryModel qm) {
        StringBuilder stringBuilder = new StringBuilder(qm.START);
        stringBuilder.append(" ");
        for (StartModel sm : qm.getStartModels()) {
            stringBuilder.append(sm.getVarName()).append("=");
            if (Neo4jModel.SelectType.node == qm.getSelectType()) {
                stringBuilder.append(qm.NODE);
                stringBuilder.append(builderNodeStart(sm));
            } else if (Neo4jModel.SelectType.relationship == qm.getSelectType()) {
                stringBuilder.append(qm.RELATIONSHIP);
                stringBuilder.append(builderRelationshipStart(sm));
            }
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * 构造节点查询
     *
     * @param sm
     * @return String
     */
    private String builderNodeStart(StartModel sm) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isBlank(sm.getIndexName())) {
            stringBuilder.append("(*)");
        } else {
            stringBuilder.append(":")
                    .append(sm.getIndexName())
                    .append("(");
            for (int i = 0; i < sm.getNames().size(); i++) {
                String name = sm.getNames().get(i);
                stringBuilder.append(name).append(sm.getOpts().get(i)).append(sm.getValues().get(i)).append(",");
            }
            if(sm.getNames()!=null||sm.getNames().size()>0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    /**
     * 构造关系查询
     *
     * @param sm
     * @return String
     */
    private String builderRelationshipStart(StartModel sm) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.toString();
    }

    /**
     * 构造Match
     *
     * @param qm
     * @return String
     */
    public String builderMatch(QueryModel qm) {
//        stringBuilder.append(" (").append(qm.getVarName()).append(":").append(qm.getNodeName()).append(")");
        return qm.getMatchStr();
    }

    /**
     * 构造Where
     *
     * @param qm
     * @return String
     */
    public String builderWhere(QueryModel qm) {
        StringBuilder stringBuilder = new StringBuilder(qm.WHERE);
        stringBuilder.append(" ");
        for (WhereModel2 wm : qm.getWhereModels()) {
            stringBuilder.append(wm.getName()).append(":").append(wm.getValue()).append(" and ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * 构造Return
     *
     * @param qm
     * @return String
     */
    public String builderReturn(QueryModel qm) {
        StringBuilder stringBuilder = new StringBuilder(qm.RETURN);
        stringBuilder.append(" ");
        for (ReturnModel2 rm : qm.getReturnModels()) {
            if(rm.getAsName()==null){
                stringBuilder.append(rm.getName()).append(",");
            }else{
                stringBuilder.append(rm.getName()).append(" AS ").append(rm.getAsName()).append(",");
            }
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }



    /*
     为了不让程序员私自动用这里面的属性产生严重的操作失误或非法人员的注入破坏程序  把这一些属性提取到工具类里面，
     并不提供get和set方法，不给外部人员调动
     */
    private Integer matchNumber = 0;
    private Map<Integer,LinkedList<String>> aliasOnes = new HashMap<Integer,LinkedList<String>>();
    private Map<Integer,LinkedList<String>> nexusJs = new HashMap<Integer,LinkedList<String>>();
    private MatchModel match = new MatchModel();

    //添加复杂查询的条件
    public QueryBuilder addNote(String nexus, String note){
        LinkedList<String> aliasOnesList = aliasOnes.get(matchNumber-1);
        aliasOnesList.addLast(note);
        LinkedList<String> nexusJsList = nexusJs.get(matchNumber-1);
        nexusJsList.addLast(nexus);
        return this;
    }
    //开始查询
    public QueryBuilder nexusStart(String varName, String nexus, String aliasOne, MatchModel.DirType dirType){
        match.getVarNames().add(matchNumber,varName);
        match.getNexuss().add(matchNumber,nexus);
        match.getAliasOnes().add(matchNumber,aliasOne);
        match.getDirections().add(matchNumber,dirType);
        aliasOnes.put(matchNumber,new LinkedList<String>());
        nexusJs.put(matchNumber,new LinkedList<String>());
        matchNumber++;
        return this;
    }

    //创造字符串
    public String createComplexQueryStr(){
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        if(aliasOnes!=null && aliasOnes.size()>0)
            for(int i = 0; i < aliasOnes.size();i++){
                StringBuilder sbStr = new StringBuilder();
                sbStr.append("(" + this.match.getVarNames().get(i) + ")");
                sbStr.append("-");
                //sbStr.append("[:" + this.match.getNexuss().get(i) + "]_(" + this.match.getAliasOnes().get(i) + ")");
                sbStr.append("[:");
                if(MatchBuilder.isNexusSpecialStr(this.match.getNexuss().get(i))){
                    sbStr.append("'"+this.match.getNexuss().get(i)+"'");
                }else{
                    sbStr.append(this.match.getNexuss().get(i));
                }

                sbStr.append("]");
                sbStr.append("_");
                sbStr.append("(" + this.match.getAliasOnes().get(i) + ")");
                for (int j = 0;j<nexusJs.get(i).size();j++) {
                    sbStr.append("-");
                    sbStr.append("[:" + nexusJs.get(i).get(j) + "]");
                    sbStr.append("_");
                    sbStr.append("(" + aliasOnes.get(i).get(j) + ")");
                }
                sbStr.append(",");
                String str = sbStr.toString();
                if(match.getDirections().get(i) == MatchModel.DirType.left){
                    str = str.replaceAll("-","<-");
                    str = str.replaceAll("_","-");
                }
                if(match.getDirections().get(i) == MatchModel.DirType.right){
                    str = str.replaceAll("_","->");
                }
                if(match.getDirections().get(i) == MatchModel.DirType.eq){
                    str = str.replaceAll("_","-");
                }
                sb.append(str);
            }

        return sb.toString().substring(0,sb.length()-1);
    }
    /*
        所有路径查询方法
     */
    public String macthPath(String firstPath, String endPath,Integer maxLeng, MatchModel.MathPath mathPath){
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        sb.append("p=");

        if(mathPath == MatchModel.MathPath.nomenclature){    //命名路径
            sb.append(firstPath + "-->" + endPath);
            return sb.toString();
        }

        if(mathPath == MatchModel.MathPath.minimum){      //最短路径
            sb.append("shortestPath");
        }else if(mathPath == MatchModel.MathPath.minimunAll){     //所有最短路径
            sb.append("allShortestPaths");
        }
        sb.append("(");
        sb.append(firstPath+"-");
        sb.append("[*.."+maxLeng);
        sb.append("]");
        sb.append("->");
        sb.append(endPath);
        sb.append(")");
        return sb.toString();
    }
    //单独的命名查询
    public String macthPath(String firstPath, String endPath){
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        sb.append("p=");
        sb.append(firstPath + "-->" + endPath);
        return sb.toString();
    }

    //查询可变长度的关系
    public static String findMatchNexusAll(MatchModel matchModel){
        if(matchModel == null) return null;
        if(matchModel.getDirection() == null) return "此方法必须给予direction值";
        if(matchModel.getVarName() == null || matchModel.getVarName().length() == 0) return "此方法必须给予varName值";

        StringBuilder sb = new StringBuilder("MATCH ");
        if(1==1){
            sb.append("("+matchModel.getVarName()+")");
            sb.append("-");
            if(null != matchModel.getNexus()){
                sb.append("[r:");
                if(MatchBuilder.isNexusSpecialStr(matchModel.getNexus())){
                    sb.append("'"+matchModel.getNexus()+"'");
                }else{
                    sb.append(matchModel.getNexus());
                }

                if(matchModel.getDepth() != null){
                    sb.append("*1.."+matchModel.getDepth());
                }
                sb.append("]");
            }else{
                matchModel.setDirection(MatchModel.DirType.eq);
            }

            sb.append("_");
            sb.append("(b)");
        }
        String str = sb.toString();
        str = MatchBuilder.dirViewTool(matchModel,str);
        return str;
    }

    /**
     * 处理关系方向
     * @param matchModel
     * @param str
     * @return
     */
    public static String dirViewTool(MatchModel matchModel,String str){
        if(matchModel.getDirection() == MatchModel.DirType.left){
            str = str.replaceFirst("-","<-");
            str = str.replaceAll("_","<-");
            int index =str.lastIndexOf("<");
            str =  str.substring(0,index) + str.substring(index+1,str.length());
        }
        if(matchModel.getDirection() == MatchModel.DirType.right){
            str = str.replaceAll("_","->");
        }
        if(matchModel.getDirection() == MatchModel.DirType.eq){
            str =str.replaceAll("_","-");
        }
        return str;
    }

    //用来判断特殊字符的关系
    public static boolean isNexusSpecialStr(String str){
        if(str.trim().indexOf(" ")!=-1)   return true;
        return false;
    }
}
