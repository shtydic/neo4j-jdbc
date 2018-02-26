package com.shtydic.neo4j.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.shtydic.neo4j.model.AttrDisplayModel;
import com.shtydic.neo4j.model.DsModel;
import com.shtydic.neo4j.model.MatchModel;
import com.shtydic.neo4j.model.Neo4jJobConfRelationship;
import com.shtydic.neo4j.model.NodeModel2;
import com.shtydic.neo4j.model.QueryModel;
import com.shtydic.neo4j.model.RelationInnerGroupModel;
import com.shtydic.neo4j.model.RelationTwoGroupModel;
import com.shtydic.neo4j.model.RelationshipModel2;

/**
 * 
 * <p>Title: Neo4jService</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author dengyichao
 * @date 2017年4月28日 上午9:45:23
 */
public interface RelationService {
	static enum Neo4jResult {  
    	gx_result, node_result, all_result 
    }
	
    /**
     * 创建节点
     * 构造 NodeModel 对象 property 由 Attrs类 toString 后生成 如下：
     * Attrs attrs = new Attrs();
     * attrs.addAttr("name", "张三");
     * attrs.addAttr("born", 1980);
     * NodeModel nodeModel = new NodeModel("p11", "Person", attrs.toString());
     *
     * @param nm
     */
    void createNode(NodeModel2 nm);

    /**
     * 批量插入
     * @param nms
     */
    void createNodeBatch(List<NodeModel2> nms);


    void createNodeBatchEx(List<NodeModel2> nms);

    String CreateCsvImport(String file,String labelName,String primaryKey,DsModel dsModel,String coverage);

    String CreateCsvImportRelation(String fileUrl,Neo4jJobConfRelationship neo4jJobConfRelationship,DsModel  neo4jDatabase);
    /**
     * 创建索引
     * @param labelName
     * @param field
     */
    void createIndex(String labelName,String field);

    /**
     * 删除节点
     *
     * 删除节点之前该方法会自动删除该节点所有的关系
     *
     * Student stu = new Student();
     * stu.setId('34260******0011')
     * stu.setName('小明')
     * removeNode(stu);
     * 生成语句：
     * match (stu:Student{id:'34260******0011',name:'小明'})-[r]-() delete (r),(stu)
     *
     * @param obj 删除节点的对象
     * @return
     */
    boolean removeNode(String name,Object obj);

    /**
     * 创建关系
     * new RelationshipModel("p1", "p2", "同事")
     *
     * @param rm
     */
    void createRelationship(RelationshipModel2 rm);

    /**
     * 删除关系
     *
     * @return
     */
    boolean removeRelationship();


    /**
     * QueryModel qm = new QueryModel();qm.setNoe(asd);
     * Mat  m
     * qm.AddMasd();
     *
     * @param qm
     * @return
     */


    void createComplexQuery(QueryModel qm);

    /**
     * 根据id获取某一个节点的所有关系节点
     *
     * @param id
     * @return
     */
    String findByIdNexusAll(String id);

    /**
     * 根据键值对获取某一个节点的所有关系节点
     *
     * @param attrName
     * @param value
     * @return
     */
    String findAttrNameNexusAll(String attrName, Object value);


    /**
     * 根据id获取一个节点，并查询它的第1,2,3,4层（或包含）节点节点
     * start  n1=node(1) MATCH (n1)-[r:认识*1..4]->(n2) RETURN r, n1, n2
     *
     * @param id
     * @param depth 深度值
     * @return
     */
    String findByIdDepthQuery(String id, Integer depth);

 
    /**
     * 根据传入的属性和深度取一个节点，并查询它的第1,2,3,4层（或包含）节点节点
     * @param id         身份id
     * @param gxList     关系
     * @param depth      深度
     * @return
     */
    String findDepthOwnQuery(String gxlbid,String id,Set<String> gxList ,Integer depth);


    /**
     * 获取某一个节点到另外一个节点的最小路径/所有最小路径
     *
     * @param props    分别放置开始node和结束的node  添加至List  查询的节点可根据属性索引来查找添加到map
     * @param maxLeng  最大的关系层级
     * @param mathPath 是什么类型的路径查询（如最小，所有最小，命名等等）
     * @return
     */
    String findMatchPathQuery(List<Map<String, Object>> props, Integer maxLeng, MatchModel.MathPath mathPath);

    /**
     * 单独的命名路径
     *
     * @param props 分别放置开始node和结束的node  添加至List  查询的节点可根据属性索引来查找添加到map
     * @return
     */
    String findMatchPathQuery(List<Map<String, Object>> props);

    /**
     * 所有根据关系的不同统计每个节点所对应的关系数,并从多到低进行排序
     *
     * @param relationship 关系值
     * @return
     */
    String findRelationshipCount(String relationship);

    /**
     * 根据某个特定的节点和关系查询相对应的节点  如  查询某个人所乘所有航班中一同
     * 同乘过三次以上的人，其中包括同一航班不同班次的问题
     * startObj的唯一标识这个属性必须要在relationship这个关系对象里面，相当于sql
     * 里面的主从键
     *
     * @param startObj       节点的起始点  必须传入对象  不能是String，Integer..
     * @param relationship   相关的关系条件对象
     * @param count          重叠次数
     * @param whereProp      给予关系条件设置她的条件值（作为where判断使用）
     * @return
     */
    String findInCompanion(Object startObj,Object relationship,int count,String... whereProp);
    /**
     * 根据某个特定的节点和关系查询相对应的节点  如  查询某个人所乘所有航班中一同
     * 同乘过三次以上的人，其中包括同一航班不同班次的问题
     * startObj的唯一标识这个属性必须要在relationship这个关系对象里面，相当于sql
     * 里面的主从键
     *
     * 因为考虑到业务的灵活性，在这里增加这个方法，让其中的关系属性和需要找寻的节点产生相对应的条件，
     * 也就是条件里面必须具备一些找寻节点的特征，具有更大的灵活性
     *
     * 其中whereMap：给予关系条件设置她的条件值（作为where判断使用 其中键是关系属性，值是对应的链接节点的属性，让关系和需要找寻的节点产生关联，相当于sql里面的外键关联一样）
     *
     * @param startObj       节点的起始点  必须传入对象  不能是String，Integer..
     * @param relationship   相关的关系条件对象
     * @param count          重叠次数
     * @param whereMap      给予关系条件设置她的条件值（作为where判断使用 其中键是关系属性，值是对应的链接节点的属性，让关系和需要找寻的节点产生关联，相当于sql里面的外键关联一样）
     * @return
     */
    String findInCompanionB(Object startObj,Object relationship,int count,Map<String,String> whereMap);

    /**
     * WHERE b.name = '钱多多'  return (a2)
     *
     * @param startObjList  一群节点的起始点    Object为对象  不能是String，Integer..
     * @param relationship  相关的关系条件对象
     * @param count         重叠次数
     * @param whereProp     给予关系条件设置她的条件值（作为where判断使用）
     * @return
     */
    Map<Object,String> findInCompanionAll(List<Object> startObjList,Object relationship,int count,String... whereProp);


    /**
     * 场景：找出具有“房多多”“钱多多”“农民”这些相同标签的某些人
     * 注意事项:必须从单个的节点出发
     *
     * match (a:Person{})-[r:Nexus{name:'标签'}]->(b:Lable{}) WHERE b.name = '农民'
     * with a AS a1 match (a1)-[r:Nexus{name:'标签'}]->(b:Lable{}) WHERE b.name = '房多多'
     * with a1 AS a2  match (a2)-[r:Nexus{name:'标签'}]->(b:Lable{})
     * return (a2)
     *
     * @param startNodeName         开始的节点名称   案例为  人
     * @param relationshipModel     开始节点和需要的节点之间的关系  案例代表为  标签
     * @param rsNode             startNode需要具备的节点条件    案例代表：标签为：钱多多，房多多的标签节点
     * @return
     */
    String findSameNode(Object startNodeName,Object relationshipModel,List<Object> rsNode);

    /**
     * 单个人多层关系查询
     * 场景：张三  怎么通过身边特有（relationshipStr）的关系  最快找到李四
     *
     * Match p = shortestPath((a:person{id:'141221199000083564'})
     * -[r:夫妻|同学|同事|亲人|邻居|朋友*0..1]-(b:person{id:'141221199000020597'}))
     * RETURN p
     *
     * @param startObj   开始节点对象
     * @param relationshipStr    关系集合   限制是或的关系，所以这边给予String集合
     * @param endObj     结束节点对象
     * @param min  最小层级
     * @param max  最大层级
     * @return
     */
    String findShortestPath(Object startObj,List<String> relationshipStr,Object endObj,Integer min,Integer max);
    
    /**
     * 对某个群体，按多层关系对其中的对象进行分组
     * 场景：如一群人里面0..3层关系之后筛选出的另一群人，并按性别分组
     *
     * WITH ['141221199000689765','141221199000027194'] AS x Match
     * p=(a:person)-[r:夫妻|同学|同事|亲人|邻居|朋友*0..3]-(b:person{nation:'维吾尔'})
     * WHERE a.id IN x return b.gender,collect(b)
     *
     * @param Mark              该节点的唯一标识属性
     * @param ids               该值为这个群体中每一个的唯一标识，强制设置为id属性
     * @param startNodeName     开始节点的节点名称
     * @param relationshipStr   关系集合   限制是或的关系，所以这边给予String集合
     * @param endNode           结束节点（主要用于限制条件）
     * @param min               最小层级
     * @param max               最大层级
     * @param groundProp        需要分组的属性名称
     * @return
     */
    String findShortestPathPropGroup(String Mark,List<String> ids,String startNodeName,List<String> relationshipStr,Object endNode,Integer min,Integer max,String groundProp) throws IOException;

    /**
     * 查询两组关系之间的某些节点有没有关系
     * @param relationTwoGroupModel
     * @return
     */
    public String queryTwoGroup(RelationTwoGroupModel relationTwoGroupModel);

    /**
     * 查询一组关系内部的某些节点的关系
     * @param relationInnerGroupModel
     * @return
     */
    public String queryInnerGroup(RelationInnerGroupModel relationInnerGroupModel);

    public String findPersonNode(Integer sign,String value);
    /**
     * 测试
     * @param name
     * @return
     */
    public String testSpringCloud(String name);
    
    public String findDepthIsMailboxQuery(String id,List<String> gxList ,Integer depth,String findType);

	public JSONObject getResultInfo(String kid, String kvals,String gxcs,String  gxlbid);
    

	/**
	 * 根据节点类型 查询特有的属性值
	 * @param nodeType
	 * @param attr
	 * @return
	 */
	List<String> findAttrByNodeType(String nodeType,String attr);
	/**
	 * 该接口针对das5中人和人，为他们创建关系
	 * @param zjhm1
	 * @param zjhm2
	 * @param gx
	 */
	void createDas5RRGXById(String zjhm1,String zjhm2,String gx,String gxmc);
	
	/**
	 * 查询多个人一层关系
	 * @param pList
	 * @return
	 */
	String findPersonsGXById(List<String> pList);
	public String info();
	public Map<String, String> getAllGXMCMap();
	/**
	 * 根据某一节点查询不同关系不同深度的所有节点
	 * @param startObj   确定的节点
	 * @param gxList     不同的关系，这里也可以给null,默认为所有关系
	 * @param depth      深度 ，这里也可以给null 默认查1层深度
	 * @param entObj     需要查询的节点，可以固定查询的类型，也可以查询所有有关系的节点,给Null值默认为所有
	 * @param neo4jResults     返回值的结果，如返回关系==gx_result,返回结果节点==node_result,返回所有数据==all_result 
	 * @return  返回查询到的结果
	 */
	public String selectDepthOwnQuery(Object startObj,List<String> gxList,Integer depth,Object entObj,RelationService.Neo4jResult neo4jResults );
	 /**
     * 根据对象创建节点
     * @param obj
     */
    void createNode(Object obj,AttrDisplayModel attrDisplayModel);
    /**
     * 创建索引
     * @param neo4jType
     * @param attrList
     */
    void createIndex(String neo4jType,List<String> attrList);

    /**
     * 删除节点
     * 请注意如果删除该节点在这里自动删除跟这个节点的所有关系
     * 
     * @param obj
     * @param attrDisplayModel
     */
    void removeNode(Object obj,AttrDisplayModel attrDisplayModel);
    /**
     * 创建关系
     * @param node_a
     * @param node_b
     * @param relationObj
     */
    void createRelation(Object node_a,Object node_b,Object relationObj);
}
