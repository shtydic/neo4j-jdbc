package com.shtydic.neo4j.model;

import java.util.LinkedList;

/**
 * Created by wanghong on 2016/11/24 16:44.
 */

public class MatchModel {
    private String varName;    //确定的人
    private DirType direction;   //逻辑方向
    private String aliasOne;   //第二个对象
    private String nexus;   //关系
    private Integer depth; //深度
    private MathPath mathPath; //路径类型


    private LinkedList<String> varNames = new LinkedList<String>();    //确定的人
    private LinkedList<MatchModel.DirType> directions = new LinkedList<MatchModel.DirType>();   //逻辑方向
    private LinkedList<String> aliasOnes = new LinkedList<String>();   //第二个对象
    private LinkedList<String> nexuss = new LinkedList<String>();   //关系
    private LinkedList<Integer> depths = new LinkedList<Integer>(); //深度

    public enum DirType {    //关系方向
        left, right,eq;
    }

    public enum ShortesPath {
        YES,NO;
    }
    //所有路径的方法
    public enum MathPath{
        minimum,
        minimunAll,
        nomenclature;
    };

    public String getVarName() {
        return varName;
    }

    public DirType getDirection() {
        return direction;
    }

    public String getNexus() {
        return nexus;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public void setDirection(DirType direction) {
        this.direction = direction;
    }

    public void setNexus(String nexus) {
        this.nexus = nexus;
    }

    public void setDepth(Integer depth) {
        if(depth<=0){
            depth = 1;
        }

        this.depth = depth;
    }

    public String getAliasOne() {
        return aliasOne;
    }

    public void setAliasOne(String aliasOne) {
        this.aliasOne = aliasOne;
    }

    public void setVarNames(LinkedList<String> varNames) {
        this.varNames = varNames;
    }

    public void setDirections(LinkedList<DirType> directions) {
        this.directions = directions;
    }

    public void setAliasOnes(LinkedList<String> aliasOnes) {
        this.aliasOnes = aliasOnes;
    }

    public void setNexuss(LinkedList<String> nexuss) {
        this.nexuss = nexuss;
    }

    public void setDepths(LinkedList<Integer> depths) {
        this.depths = depths;
    }

    public LinkedList<String> getVarNames() {

        return varNames;
    }

    public LinkedList<DirType> getDirections() {
        return directions;
    }

    public LinkedList<String> getAliasOnes() {
        return aliasOnes;
    }

    public LinkedList<String> getNexuss() {
        return nexuss;
    }

    public LinkedList<Integer> getDepths() {
        return depths;
    }

    public MathPath getMathPath() {
        return mathPath;
    }

    public void setMathPath(MathPath mathPath) {
        this.mathPath = mathPath;
    }

}
