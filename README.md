                    #neo4j研究报告#


### 简介 ###
	你可以把Neo看作是一个高性能的图引擎，该引擎具有成熟和健壮的数据库的所有特性。
	程序员工作在一个面向对象的、灵活的网络结构下而不是严格、静态的表中——但是他们可以享受到具备完全的事务特性、企业级的数据库的所有好处。 
	Neo是一个网络——面向网络的数据库——也就是说，它是一个嵌入式的、基于磁盘的、具备完全的事务特性的Java持久化引擎，但是它将结构化数据存储在网络上而不是表中。
	网络（从数学角度叫做图）是一个灵活的数据结构，可以应用更加敏捷和快速的开发模式。
## 什么是图数据库 ##
	图数据库用图来存储数据，是最接近高性能的一种用于存储数据的数据结构方式之一。
# 为什么我们选择neo4j #
	我们在做数据可视化的时候发现neo4j更加适合我们，我们在使用系型数据库时常常会遇到一系列非常复杂的设计问题。例如一部电影中的各个演员常常有主角配角之分，还要有导演，特效等人员的参与。通常情况下这些人员常常都被抽象为Person类型，对应着同一个数据库表。同时一位导演本身也可以是其它电影或者电视剧的演员，更可能是歌手，甚至是某些影视公司的投资者。而这些影视公司则常常是一系列电影，电视剧的资方。这种彼此关联的关系常常会非常复杂，而且在两个实体之间常常同时存在着多个不同的关系：在尝试使用关系型数据库对这些关系进行建模时，我们首先需要建立表示各种实体的一系列表：表示人的表，表示电影的表，表示电视剧的表，表示影视公司的表等等。这些表常常需要通过一系列关联表将它们关联起来：通过这些关联表来记录一个人到底参演过哪些电影，参演过哪些电视剧，唱过哪些歌，同时又是哪些公司的投资方。同时我们还需要创建一系列关联表来记录一部电影中哪些人是主角，哪些人是配角，哪个人是导演，哪些人是特效等。可以看到，我们需要大量的关联表来记录这一系列复杂的关系。在更多实体引入之后，我们将需要越来越多的关联表，从而使得基于关系型数据库的解决方案繁琐易错。
	这一切的症结主要在于关系型数据库是以为实体建模这一基础理念设计的。该设计理念并没有提供对这些实体间关系的直接支持。在需要描述这些实体之间的关系时，我们常常需要创建一个关联表以记录这些数据之间的关联关系，而且这些关联表常常不用来记录除外键之外的其它数据。也就是说，这些关联表也仅仅是通过关系型数据库所已有的功能来模拟实体之间的关系。这种模拟导致了两个非常糟糕的结果：数据库需要通过关联表间接地维护实体间的关系，导致数据库的执行效能低下；同时关联表的数量急剧上升。
	在需要描述大量关系时，传统的关系型数据库已经不堪重负。它所能承担的是较多实体但是实体间关系略显简单的情况。而对于这种实体间关系非常复杂，常常需要在关系之中记录数据，而且大部分对数据的操作都与关系有关的情况，原生支持了关系的图形数据库才是正确的选择。它不仅仅可以为我们带来运行性能的提升，更可以大大提高系统开发效率，减少维护成本。
    在一个图形数据库中，数据库的最主要组成主要有两种，结点集和连接结点的关系。结点集就是图中一系列结点的集合，比较接近于关系数据库中所最常使用的表。而关系则是图形数据库所特有的组成。
	在需要表示多对多关系时，我们常常需要创建一个关联表来记录不同实体的多对多关系，而且这些关联表常常不用来记录信息。如果两个实体之间拥有多种关系，那么我们就需要在它们之间创建多个关联表。而在一个图形数据库中，我们只需要标明两者之间存在着不同的关系，例如用DirectBy关系指向电影的导演，或用ActBy关系来指定参与电影拍摄的各个演员。同时在ActBy关系中，我们更可以通过关系中的属性来表示其是否是该电影的主演。而且从上面所展示的关系的名称上可以看出，关系是有向的。如果希望在两个结点集间建立双向关系，我们就需要为每个方向定义一个关系。
	也就是说，相对于关系数据库中的各种关联表，图形数据库中的关系可以通过关系能够包含属性这一功能来提供更为丰富的关系展现方式。因此相较于关系型数据库，图形数据库的用户在对事物进行抽象时将拥有一个额外的武器，那就是丰富的关系
	因此在为图形数据库定义数据展现时，我们应该以一种更为自然的方式来对这些需要展现的事物进行抽象：首先为这些事物定义其所对应的结点集，并定义该结点集所具有的各个属性。接下来辨识出它们之间的关系并创建这些关系的相应抽象。
	由此可以看出图数据库大大简化了传统数据库的复杂程度，并可以提高效率的

# neo4j亮点简谈 #
	1. 完整的ACID支持
	2. 高可用性
    3. 轻易扩展到上亿级别的节点和关系 
	4. 通过遍历工具高速检索数据
	5. 数据更直观：相应的SQL语句也更好写（Neo4j使用Cypher语言，与传统SQL有很大不同）。
	6. 更灵活：不管有什么新的数据需要存储，都是一律的节点和边，只需要考虑节点属性和边属性。而MySql中即意味着新的表，还要考虑和其他表的关系。
	7. 数据库操作的速度并不会随着数据库的增大有明显的降低。这得益于Neo4j特殊的数据存储结构和专门优化的图算法。
    8. 数据存储： Neo4j对于图的存储自然是经过特别优化的。不像传统数据库的一条记录一条数据的存储方式，Neo4j的存储方式是：节点的类别，属性，边的类别，属性等都是分开存储的，这将大大有助于提高图形数据库的性能。 
    9. 数据读写：在Neo4j中，存储节点时使用了"index-free adjacency"，即每个节点都有指向其邻居节点的指针，可以让我们在O(1)  O(1) 的时间内找到邻居节点。另外，按照官方的说法，在Neo4j中边是最重要的,是"first-class entities"，所以单独存储，这有利于在图遍历的时候提高速度，也可以很方便地以任何方向进行遍历。
    总结：
		适当的ACID操作是保证数据一致性的基础。Neo4j确保了在一个事务里面的多个操作同时发生，保证数据一致性。不管是采用嵌入模式还是多服务器集群部署，都支持这一特性。
		可靠的图型存储可以非常轻松的集成到任何一个应用中。随着我们开发的应用在运营中不断发展，性能问题肯定会逐步凸显出来，而Neo4j不管应用如何变化，他只会受到计算机硬件性能的影响，不受业务本身的约束。部署一个neo4j服务器便可以承载上亿级的节点和关系。当然，当单节点无法承载我们的数据需求时，我们可以进行分布式集群部署。将图数据库用于存储关系复杂的数据是他最大的优势。通过Neo4j提供的遍历工具，可以非常高效的进行数据检索，每秒可以达到上亿级的检索量。一个检索操作类似于RDBMS里面的连接（_join_）操作。


# 安装 #
	Windows版本：
	版本：3.1.0
	下载网站：https://neo4j.com/download/
 	

# 启动neo4j #
	windos版：  
		bin/neo4j.bat stop
		bin/neo4j.bat status
		bin/neo4j.bat start
	Linux版：   
		./neo4j start

 
# 最常用的一些简单语法 #
	## 创建节点 ##
	  
	创建节点详细讲解：
	//创建Sally这个Person类型的结点，该结点的name属性为Sally，age属性为32
	CREATE (sally:Person { name: 'Sally', age: 32 })
	CREATE (john:Person { name: 'John', age: 27 })
	 
	
	// 创建Graph Databases一书所对应的结点
	CREATE (gdb:Book { title: 'Graph Databases',authors: ['Ian Robinson', 'Jim Webber'] })
	
	// 在Sally和John之间建立朋友关系，这里的since值应该是timestamp。请自行回忆各位的日期是如何在关系数据库中记录的
	CREATE (sally)-[:FRIEND_OF { since: 1357718400 }]->(john)
	// 在Sally和Graph Databases一书之间建立已读关系
	CREATE (sally)-[:HAS_READ { rating: 4, on: 1360396800 }]->(gdb)
	// 在John和Graph Databases一书之间建立已读关系
	CREATE (john)-[:HAS_READ { rating: 5, on: 1359878400 }]->(gdb)
	
	整体关系图：
		
	 
	
	
	
	## 一次创建多节点 ##
	MATCH (a:PersonTo{name:'测试1',age:'25'}),(b:PersonTo{name:'测试4',age:'28'}) create (a)-[r:GxTest{gxInfo:'第一条测试数据',gxName:'同事'}]->(b) return (r)
	删除节点
	删除节点必须删除该节点的所有关系，否则会报错，提示该节点存在关系无法删除
	 
	解决方案：
	match (n:Person{name:'李四'})-[r]-() delete r
	 
	match (n:Person{name:'李四'}) delete (n)
	 
	
	删除案例，语句整合(该语句在删除没有关系的节点时候并不起任何作用，只能用在删除有关系的节点上)：
	match (n:Person{name:'John'})-[r]-() delete (r),(n)
  

#创建索引#
	SchemaIndex:模型索引

# 新建索引
	CREATE INDEX ON :AddressNode( preAddressNodeGUIDs)
	删除索引DROP INDEX ON :AddressNode(_id)
    查看索引 :schema  

 

# 创建关系 #
	MATCH (a:tressa{id:"99980"}),(b:tressa{id:"99981"}) create (a)-[n:gift{name:"礼物"}]->(b) return n 


# 修改属性的值 #
	match (n:People) where n.ZWXM="菜菜" set n.ZWXM="谢楠玉",n.name="谢楠玉",n.PYZWXM="xienanyu" return (n);


# Neo4j   组成类api 设计 #
## Master ##
	简洁：组成neo4j语句的核心内，所有元素均在此类中的生成
 
	方法名	返回值类型	参数	简介
	createNodeModel	NodeModel	无	创建节点元素
	createRelationshipModel	RelationshipModel	无	创建关系元素
	createWhereModel	WhereModel	无	创建条件元素
	createReturnModel	ReturnModel	无	创建返回值元素
	start	Master	NodeModel，
	RelationshipModel ， NodeModel ，Master.DirType	开始查询，该方法需要三个参数分别是开始节点元素、关系元素、与他产生关系的第二个节点元素以及关系方向四个参数，从而确定neo4j的查询方式
	addNote	Master	RelationshipModel, NodeModel	Start只是添加了两个节点的查询关系，如果还需要添加更多的查询信息，就可以在start之后在用addnote添加之后的关系元素和节点元素
	addResult	Master	Object...	返回什么结果，可返回多个结果集（该参数只能放入之前就已经用Match这个类创建的NodeModel和RelationModel对象，这样才可以返回正确的值）
	createComplexQueryStr	String	无	当所有的元素和条件准备好了，就可以用该方法生成neo4j语句

## NodeModel ##
	简洁：组成节点元素的类
 
	方法名	返回值类型	参数	简介
	createWhereNode	NodeModel	Object	把所有的属性变成map集合存储到nodeMap（这里也会给予别名，别名用的是UUID，确保具有唯一性）
	createWhereNodeObj	NodeModel	String alias,
	Object object	把一个对象变成字面量的节点，
	alias   给这个对象起别名
	object  需要解析的对象
	addContain	String	String prop	把该对象中某个属性作为模糊查询的条件
	Prop就是该对象的属性名
	addGt	String	String prop	同上，只是模糊查询变成了“>”大于查询
	addLt	String	String prop	同上， “<”小于查询
	addGtEq	String	String prop	同上， “>=”大于等于查询
	addLtEq	String	String prop	同上， “<=”小于等于查询
	nodeStr	String	无	该方法是用于单独创建NodeModel时组建Node节点时使用的
	nodeStr	String	AttrDisplayModel 	重构了一个方法，因为有时候我们业务使用的实体类属性展示形式不一样，用这个enum去控制属性
	forNeo4jStr	String	String prop
	String Symbol	帮助处理运算符的方法，私有方法，外部调用不了
	getNameMapKey	String	无	获取节点对应的标记名，在Match开始构建neo4j的时候起到很重要的作用
	getIndexName	String	无	获取节点对应的节点名称，在Match开始构建neo4j的时候起到很重要的作用

## RelationshipModel ##
	简洁：组成关系元素的类
 
	方法名	返回值类型	参数	简介
	createWhereRelationship	RelationshipModel	Object	把所有的属性变成map集合存储到relationshipModelMap（这里也会给予别名，别名用的是UUID，确保具有唯一性）
	createWhereRelationshipStr	String	String alias,
	Object object	该方法是为了给单独需要一个关系元素的需求准备的，返回一个Relationship元素的字符串
	createWhereRelationshipObj	RelationshipModel	String alias,
	Object object	把一个对象变成字面量的关系元素，
	alias   给这个对象起别名
	object  需要解析的对象
	addContain	String	String prop	把该对象中某个属性作为模糊查询的条件
	Prop就是该对象的属性名
	addGt	String	String prop	同上，只是模糊查询变成了“>”大于查询
	addLt	String	String prop	同上， “<”小于查询
	addGtEq	String	String prop	同上， “>=”大于等于查询
	addLtEq	String	String prop	同上， “<=”小于等于查询
	nodeStr	String	无	该方法是用于单独创建NodeModel时组建Node节点时使用的
	forNeo4jStr	String	String prop
	String Symbol	帮助处理运算符的方法，私有方法，外部调用不了
	getNameMapKey	String	无	获取节点对应的标记名，在Match开始构建neo4j的时候起到很重要的作用
	getIndexName	String	无	获取节点对应的节点名称，在Match开始构建neo4j的时候起到很重要的作用
	RelationshipStr	String	无	把关系节点的值变成相应的字符串
## WhereModel ##
	简介：组成where条件的类,该类只能在Match实例化出来的时候才可以使用，里面所有的方法参数依赖Match构造出来的条件
 
	方法名	返回值类型	参数	简介
	start	WhereModel	String whereStr	条件开始，放置第一个条件的参数
	or	WhereModel	String whereStr	或的条件，
	参数whereStr代表二个条件
	也就是在start之后.or具体语句就是: WHERE xx>1 or yy<2
	and	WhereModel	String whereStr	并且条件
	includeStart	WhereModel	无	大括号开始
	语句示例：
	WHERE xx>1 or yy<2 （
	includeEnd	WhereModel	无	大括号结束
	语句示例：
	WHERE xx>1 or yy<2 （）
	not	WhereModel	无	或的条件，
	WHERE Not xx=123 


## ReturnModel ##
	简介：该类是结果元素的类，该类只能在Match实例化出来的时候才可以使用，里面所有的方法参数依赖Match构造出来的条件
	方法名	返回值类型	参数	简介
	addResult	无	Object... obj	把最后的结果进行一些封装，因为这个类单独是没有任何作用的，所以在构建Match的时候做了一系列的操作才使得这里可以用obj的参数，在这里不好解释，下面把该类的代码复制出来简单看看

 
## AttrDisplayModel ##
	控制属性展示形式的类
 
	注意事项
		1.	给节点或者关系起别名的时候有些符号是不符合Neo4j的规范的，如“-”
		2.	给关系深度的同时，关系不能在具有条件查询
 
	综合示例
	组成的neo4j语句
		MATCH (p:Person{gender:'男',height:'1.96m'})-[guanxi:Person{gender:'男',name:'我是关系'}]-(p2:Person{gender:'男',name:'22222'})-[guanxi2222:Person{gender:'男',name:'我是关系22222',height:'1.96m'}]-(p:Person{gender:'男',height:'1.96m'})( WHERE p.name Contains '李' and p2.height > '1.96m') or guanxi.height >= '1.96m' RETURN (p)
	组成的测试代码
		Person p = new Person();
		p.setGender("男");
		p.setHeight("1.96m");
		p.setName("李");
		
		Person guanxi = new Person();
		guanxi.setGender("男");
		guanxi.setHeight("1.96m");
		guanxi.setName("我是关系");
		
		Person guanxi2 = new Person();
		guanxi2.setGender("男");
		guanxi2.setHeight("1.96m");
		guanxi2.setName("我是关系22222");
		
		Person p2 = new Person();
		p2.setGender("男");
		p2.setHeight("1.96m");
		p2.setName("22222");
		
		Master m = new Master();
		NodeModel nodel = m.createNodeModel().createWhereNodeObj("p", p);
		System.out.println("节点：" + nodel.nodeStr());
		
		NodeModel nodel2 = m.createNodeModel().createWhereNodeObj("p2", p2);
		RelationshipModel r = m.createRelationshipModel().createWhereRelationshipObj("guanxi", guanxi);
		RelationshipModel r2 = m.createRelationshipModel().createWhereRelationshipObj("guanxi2222", guanxi2);
		
		String where1 = nodel.addContain("name");
		String where2 = nodel2.addGt("height");
		String where3 = r.addGtEq("height");
		m.createWhereModel().includeStart().start(where1).and(where2).includeEnd().or(where3);
		
		String a = m.start(nodel, r, nodel2, DirType.eq).addNote(r2, nodel).addResult(nodel).createComplexQueryStr();
		System.out.println(a);

## 代码解析 ##
 
 
# Neo4j场景api设计 #
	简介
	该api都是根据不同的场景单独封装起来使用的接口，使用方式
 
	实例化RelationServicImple类，在根据以下api介绍使用
	必须在Neo4jConnection中实例话出来


	功能api简介
	方法名	返回值类型	参数	简介
	createNode	void	Object
	,AttrDisplayModel 	根据对象创建节点，第一个参数是你的实体类对象，第二个是初始化你数据库里面的属性展示形式，具体请看AttrDisplayModel参数简介
	createNodeBatch	void	List<Object>	批量创建节点，这里没有AttrDisplayModel参数。默认为java标准
	createIndex	void	String
	List<String> 	创建索引。第一参数是需要创建索引的类型，第二个是需要为该类型中的哪些属性需要创建索引
	removeNode	void	Object
	,AttrDisplayModel 	删除节点。第一个参数为需要删除的节点对象，第二个是属性展示形式
	createRelation	void	Object node_a,
	Object node_b,
	Object relationObj	创建关系。第一个参数：开始节点，第二个参数：结束节点，第三个参数：关系对象
	createRelationById	void	String node_type,
	String attr,
	String val_first,
	String val_second,
	Object relationObj	根据id创建关系。node_type：需要的节点类型，attr：该类型的哪个属性作为查询条件（一般为唯一标识），val_first：开始节点的唯一标识值，val_second：第二个节点的唯一标识值，relationObj：需要删除的关系对象
	createRelation	Void	String node_first,
	String attr_first,
	String val_first,
	String node_second,
	String attr_second,
	String val_second,
	Object relationObj	* 创建关系
	    * 根据不同类型的唯一属性创建关系
	    * @param node_first     第一个节点类型
	    * @param attr_first 	第一个属性类型
	    * @param val_first   	第一个值
	    * @param node_second	第二个节点类型
	    * @param attr_second	第二个属性类型
	    * @param val_second		第二个值
	    * @param relationObj	关系对象
	removeRelationship	Void	Object startNode,
	Object relationObj,
	Object endNode	删除关系
	@param startNode 	   开始节点
	@param relationObj 需要删除的关
	@param endNode	   结束节点 （如果该参数为null那么删除和开始节点到所有节点类型的对应关系）
	
	
	1. String findInCompanion(Object startObj,Object relationship,int count,String... whereProp);
	场景模拟
	* 根据某个特定的节点和关系查询相对应的节点  如  查询某个人所乘所有航班中一同乘过三次以上的人，其中包括同一航班不同班次的问题
	
	Cypher语句
		1.	match (a:Person{person_id:'141221199013721258'})-[r:Travel]-(b) with b as b1,r as r1 match (p:Person)-[x:Travel]->(b1) where x.flight_number = r1.flight_number and x.a = r1.a and x.b = r1.b and x.c = r1.c and x.d = r1.d and x.e = r1.e and x.f = r1.f  return (p),count(x)>3,count(x)
		
		2.	match (a:Person{person_id:'141221199013721258'})-[r:Travel]-(b) with b as b1,r as r1 match (p:Person)-[x:Travel]->(b1) where x.flight_number = r1.flight_number return (p),count(b1)>3,count(b1)
	
	分析：在这种情况下2的语句只绑定了航班的id但并没有考虑到每个航班各个时间点都有可能会起飞，这样用第二条语句就会出现数据冗余，返回结果不对，所以建议根据合适的情况绑定不同的数据，如案例中的人与航班及他们的搭乘关系，考虑到不同航班不同时间的时间所一起搭乘的人，在这里必须给予他两个必要的条件，第一个就是搭乘条件必须要有航班的id，第二个就是搭乘航班的时间，不同的场景下想用该API必须具备这种思维才能正确使用

    API使用 

 	对应cypher语句和相对应的参数解释 
		* @param startObj       节点的起始点  必须传入对象  不能是String，Integer..
		对应(a)节点，java中传入一个封装好的实体对象，如人这个对象，所有设置的值都将映射到该节点上，类作为该节点的节点类型，该对象所赋予的属性也将作为这个节点的属性
		* @param relationship   相关的关系条件对象
		对应(r)节点，java中传入一个封装好的实体对象，如搭乘信息这个对象，所有设置的值都将映射到该关系上，类作为该节点的节点类型，该对象所赋予的属性也将作为这个节点的属性
	
		* @param count          重叠次数
		对应count(x)>3  3就是过滤的次数，解释起来就是，当前跟a共同搭乘过3次以上航班的人
		* @param whereProp      给予关系条件设置她的条件值（作为where判断使用）
		对应  x.flight_number = r1.flight_number and x.a = r1.a and x.b = r1.b and x.c = r1.c and x.d = r1.d and x.e = r1.e and x.f = r1.f

	程序案例
	 
	
	发送的cypher语句：
	MATCH (obj:Person{person_id:'141221199013721258'})-[r:Travel]-(b) WITH b AS b1,r AS r1 MATCH (p:Person)-[x:Travel]->(b1) WHERE x.flight_number=r1.flight_number AND x.a=r1.a AND x.b=r1.b AND x.c=r1.c AND x.d=r1.d AND x.e=r1.e AND x.f=r1.f return (p),count(b1)>3,count(b1)
	
	返回的cypher查询结果
	{"results":[{"columns":["p","count(b1)>3","count(b1)"],"data":[{"row":[{"person_id":"141221199013721258"},true,5],"meta":[{"id":21320250,"type":"node","deleted":false},null,null]},{"row":[{"person_id":"141221199009295243"},false,2],"meta":[{"id":21320241,"type":"node","deleted":false},null,null]}]}],"errors":[]}
	
	5200W数据cypher查询结果共用时74毫秒
	性能测试
	分析：该查询先进行定位在进行查询，查询耗时差距不大，耗时一般在200-1000毫秒之间，5000W数据5个人同行情况下的大概在200-300毫秒之间
	
	2. Map<Object,String> findInCompanionAll(List<Object> startObjList,Object relationship,int count,String... whereProp);
	参考findInCompanion 的API
	场景及api分析
	场景同标签一的一样，唯一不同在于把单人所有同行乘客变成一群人所有对应的同行乘客，返回值为Map<Object,String>  Object和String对应的就是哪一个人（Object）对应的所有同行乘客(String)
	
	3. String findSameNode(Object startNodeName,Object relationshipModel,List<Object> rsNode);
	场景模拟
	找出具有“房多多”“钱多多”“农民”这些相同标签的某些人
	
	Cypher语句
	match (a:Person{})-[r:Nexus{name:'标签'}]->(b:Lable{name:'农民'})  with a AS a1 match (a1)-[r:Nexus{name:'标签'}]->(b:Lable{name:'房多多'}) with a1 AS a2  match (a2)-[r:Nexus{name:'标签'}]->(b:Lable{}) return (a2)

	API使用
	* @param startNodeName         开始的节点名称   案例为  人
	对应（a:Person{}）这里传入的是一个对象
	
	* @param relationshipModel     开始节点和需要的节点之间的关系案例代表为  标签
	对应（r:Nexus）传入的是一个关系类型的对象
	
	* @param rsNode             startNode需要具备的节点条件    案例代表：标签为：钱多多，房多多的标签节点
	 对应的是每次过滤的条件 （b:Label{name=’农民’}）


	程序案例
	
	
	性能测试
	
	4. String findShortestPath(Object startObj,List<String> relationshipStr,Object endObj,Integer min,Integer max);
	场景模拟
	如张三  怎么通过身边特有（relationshipStr）的关系  最快找到李四
	
	Cypher语句
	Match p=shortestPath((a:person{id:'141221199000000042'})-[r:夫妻|同学|同事|亲人|邻居|朋友*0..4]-(b:person{id:'141221199000000025'}))  RETURN p
	分析：首先这里的关系（r）是一个大的关系，并不是小的关系，如社会关系中包含着的同事、同学、闺蜜等等，这里给予或者的关系，也就是说其中一条符合就可以，业务需要的时候请根据实际情况使用（如关系必须是同学并且也是同事的情况下不能使用）
	API使用
	对应cypher语句和相对应的参数解释
	* @param startObj   开始节点对象
	对应（a）节点，入的必须为一个对象，否则无法解析
	* @param relationshipStr    关系集合   限制是或的关系，所以这边给予String集合
	对应上面的（r：同事|同学|朋友）底层处理关系之间是并且关系 这点很重要，因为传入的是一个大的关系，所以这里给予的是关系名称的字符串集合即可
	* @param endObj     结束节点对象
	对应（b）节点，传入的必须为一个对象，否则无法解析
	* @param min  最小层级
	层级：a到b之间最小的距离之间夹着一个c 那么他们之间的层级就是2,设置最小
	* @param max  最大层级
	层级：a到b之间最小的距离之间夹着一个c 那么他们之间的层级就是2,设置最大
	* @return
	返回（p）
	
	
	
	程序案例
	 
	发送的cypher语句：
	MATCH p = shortestPath((a:person{id:'141221199000000042'})-[r:夫妻|同学|同事|亲人|邻居|朋友*0..4]-(b:person{id:'141221199000000025'})) RETURN p
	
	返回的cypher查询结果
	{"results":[{"columns":["p"],"data":[{"row":[[{"education":"1985-08-10","hairlengh":"短
	","owncar":"2","gender":"男","nation":"汉
	","smoke":"3","isarmy":"0","birth":"0","ismarried":"0","fatedegree":"3","religion":"无
	","criminalrecord":"3","ownhouse":"4","bloodtype":"O","nationality":"germany","spectacled":"不戴眼镜","name":"郑丽成","nativeplace":"安徽省芜湖市恩县广场26号-12-9","id":"141221199000000042","height":"194"},{"nodefrom":"141221199000000042"},{"education":"1992-08-10","hairlengh":"长","owncar":"2","gender":"女","nation":"土家","smoke":"0","isarmy":"0","birth":"3","ismarried":"0","fatedegree":"4","religion":"无","criminalrecord":"4","ownhouse":"1","bloodtype":"A","nationality":"china","spectacled":"不戴眼镜","name":"倪女斯","nativeplace":"安徽省淮北市漳州街23号-11-4","id":"141221199002216636","height":"126"},{"relationType":"朋友"},{"education":"1971-11-14","hairlengh":"长","owncar":"1","gender":"男","nation":"汉","smoke":"0","isarmy":"0","birth":"3","ismarried":"1","fatedegree":"0","religion":"无","criminalrecord":"1","ownhouse":"4","bloodtype":"C","nationality":"germany","spectacled":"不戴眼镜","name":"常于爱","nativeplace":"安徽省池州市荷泽一路124号-6-1","id":"141221199001235537","height":"135"},{"relationType":"亲人"},{"education":"1975-03-30","hairlengh":"长","owncar":"3","gender":"女","nation":"汉","smoke":"1","isarmy":"0","birth":"2","ismarried":"1","fatedegree":"0","religion":"无","criminalrecord":"2","ownhouse":"2","bloodtype":"A","nationality":"japan","spectacled":"不戴眼镜","name":"郑出斯","nativeplace":"安徽省滁州市道口路91号-7-6","id":"141221199004958975","height":"196"},{"nodefrom":"141221199000000025"},{"education":"1992-04-15","hairlengh":"长","owncar":"4","gender":"女","nation":"汉","smoke":"2","isarmy":"0","birth":"2","ismarried":"1","fatedegree":"2","religion":"无","criminalrecord":"4","ownhouse":"2","bloodtype":"C","nationality":"germany","spectacled":"不戴眼镜","name":"苗以轩","nativeplace":"安徽省亳州市滕县路139号-11-10","id":"141221199000000025","height":"166"}]],"meta":[[{"id":11869825,"type":"node","deleted":false}]]}]}],"errors":[]}
	
	2000W数据cypher查询结果共用时219毫秒
	性能测试
	耗时（毫秒）	层级	查出节点	查出关系	开始id	结束id
	75	0~1	0	0	141221199000085878	141221199000068404
	122		2	1	141221199000000006	141221199010531047
	107		0	0	141221199000032714	141221199000071901
	88		2	1	141221199000000009	141221199011751233
	88	0~2	0	0	141221199000050135	141221199000049429
	114		3	2	141221199000009379	141221199000046324
	72		3	2	141221199000008766	141221199000036058
	83		3	2	141221199000083891	141221199000000252
	163	0~3	0	0	141221199000094094	141221199000010695
	138		4	3	141221199000000559	141221199000000217
	146		4	3	141221199000000215	141221199000000052
	153		0	0	141221199000023896	141221199000025713
	169	0~4	0	0	141221199000000002	141221199000000402
	96		0	0	141221199000000131	141221199000000458
	61		5	4	141221199000000398	141221199000000458
	167		5	4	141221199000000219	141221199000000148
	314	0~5	0	0	141221199000000950	141221199000000867
	106		0	0	141221199000000424	141221199000000142
	273		6	5	141221199000000364	141221199000000449
	428		0	0	141221199000000974	141221199000000147
	91	0~6	7	6	141221199000000382	141221199000000344
	421		0	0	141221199000011911	141221199000046645
	303		0	0	141221199000405011	141221199000711963
	427		0	0	141221199000028548	141221199000319345
	
	分析：该测试在不同id下进行测试，测试发现不同id之间第一次查询的时候，耗时较长，之后在去查询耗时削减很明显，整体大概是深度越深耗时越长，相同语句下第一次查询深度越深时间越长，第二次之后基本就可以控制在120毫秒以下（个人测试经验）
	
	5. String findShortestPathPropGroup(List<String> ids,String startNodeName,List<String> relationshipStr,Object endNodeName,Integer min,Integer max,String groundProp);
	场景模拟
	如一群人里面0..3层关系之后筛选出的另一群人，并按性别分组
	
	Cypher语句
	WITH ['141221199000689765','141221199000027194'] AS x Match p=(a:person)-[r:夫妻|同学|同事|亲人|邻居|朋友*0..3]-(b:person{nation:'维吾尔'}) WHERE a.id IN x return b.gender,collect(b),count(b.gender)
	
	分析：首先确定一群人的所有人情况，取出唯一标识（WITH ['141221199000689765','141221199000027194'] AS x），然后进行筛选，collect(b)的值是根据b.gender进行分组而产生的结果，在这里是根据性别进行分组
	API使用
	对应cypher语句和相对应的参数解释
	* @param Mark              该节点的唯一标识属性
	该节点的唯一标识属性，对应 a.id中的id属性，用于区分条件属性
	* @param ids      该值为这个群体中每一个的唯一标识，强制设置为id属性
	对应with里面的id主要用于确认唯一标识，从而找到相对应的节点，这里强制为id，传入的值为一组String类型的List集合
	* @param startNodeName     开始节点的节点名称
	字符串类型的节点名称，如a:person中的person字符串
	* @param relationshipStr   关系集合   限制是或的关系，所以这边给予String集合
	对应上面的（r：同事|同学|朋友）底层处理关系之间是并且关系 这点很重要，因为传入的是一个大的关系，所以这里给予的是关系名称的字符串集合即可
	* @param endNode       结束节点（主要用于限制条件）
	对应（b:person{nation:’维吾尔’}）这个节点，传入的是一个结束的节点，也就是一个对象，至于为什么不用String类型，是因为引用对象可以进一步进行过滤，提供查询的效率
	*@param min               最小层级
	层级：a到b之间最小的距离之间夹着一个c 那么他们之间的层级就是2,设置最小
	* @param max               最大层级
	层级：a到b之间最小的距离之间夹着一个c 那么他们之间的层级就是2,设置最大
	* @param groundProp        需要分组的属性名称
	对应b.gender，根据该属性进行分组，调用者应该注意，该传入的值是一个字符串，也是你endNode里面应该有的属性，该值填错，结果将于原本的值差距极大
	
	
	程序案例
	 
	发送的cypher语句
	WITH ['141221199000689765','141221199000027194'] AS x MATCH p=(a:person)-[r:夫妻|同事|亲人|同学|邻居|朋友*0..3]-(b:person{nation:'维吾尔'})WHERE a.id IN x RETURN b.gender,collect(b)
	
	返回的cypher查询结果
	{"results":[{"columns":["b.gender","collect(b)"],"data":[{"row":["男",[{"education":"1983-03-12","hairlengh":"长","owncar":"1","gender":"男","nation":"维吾尔","smoke":"3","isarmy":"1","birth":"1","ismarried":"0","fatedegree":"4","religion":"无","criminalrecord":"2","ownhouse":"3","bloodtype":"A","nationality":"japan","spectacled":"不戴眼镜","name":"安甚芳","nativeplace":"安徽省马鞍山市东海西路140号-16-4","id":"141221199015649285","height":"168"}。。。。。"errors":[]}
	
	5000W数据cypher查询结果共用时203毫秒
	
	性能测试
	总共2000W数据，提供不同数量的人群，根据性别分组，返回名人
	耗时（毫秒）	层级	查出节点（男）	查出节点(女)	人群数
	114	0~1	2	1	2
	99		2	1	20
	89		8	5	200
	199		8	5	2000
	1319		8	5	20000
	410	0~2	96	121	2
	118		96	121	20
	130		96	121	200
	239		8	5	2000
	1200		96	121	20000
	3245	0~3	---	---	2
	1726		---	---	20
	7654		---	---	200
	74340		---	---	2000
					20000
	227615	0~4			2
					20
	
	6. String queryTwoGroup(RelationTwoGroupModel relationTwoGroupModel);
	场景模拟
	查询两组关系之间的某些节点有没有关系
	
	Cypher语句
	WITH ['141221199000689765','141221199000027194'] AS x Match p=(a:person)-[r:夫妻|同学|同事|亲人|邻居|朋友*0..3]-(b:person{nation:'维吾尔'}) WHERE a.id IN x return b.gender,collect(b),count(b.gender)
	
	分析：首先确定一群人的所有人情况，取出唯一标识（WITH ['141221199000689765','141221199000027194'] AS x），然后进行筛选，collect(b)的值是根据b.gender进行分组而产生的结果，在这里是根据性别进行分组
	API使用
	对应cypher语句和相对应的参数解释
	* @param relationInnerGroupModel
	对应一个场景对象，根据里面的值进行查询
	
	
	程序案例
	
	性能测试
	
	7. String queryInnerGroup(RelationInnerGroupModel relationInnerGroupModel);
	场景模拟
	Cypher语句
	
	
	API使用
	
	程序案例
	性能测试
	







 
# 性能调优 #
## 1.首先尝试 ##
	首先需要做的事情就是确保JVM运行良好而没有浪费大量的时间来进行垃圾收集。监视使用Neo4j的一个应用的堆使用可能有点混乱，因为当内存充裕时Neo4j会增加缓存，而相反会减少缓存。目标就是有一个足够大的堆来确保一个重型加载不会导致调用GC收集（如果导致GC收集，那么性能将降低高达两个数量级）。
	使用标记 -server 和 -Xmx<good sized heap> (f.ex. -Xmx512M for 512Mb memory or -Xmx3G for 3Gb memory)来启动JVM。太大的堆尺寸也会伤害性能，因此你可以反复尝试下不同的堆尺寸。使用 parallel/concurrent 垃圾收集器 (我们发现使用 -XX:+UseConcMarkSweepGC 在许多情况下使用良好)
	最后，确保操作系统有一些内存来管理属性文件系统缓存, 这意味着如果你的系统有8G内存就不要使用全部的内存给堆使用（除非你关闭内存映射缓冲区）而要留一个适合大小的内存给系统使用

## 2.Neo4j 基础元素的生命周期 ##
	Neo4j根据你使用Neo4j的情况来管理它的基础元素（节点，关系和属性）。比如如果你从来都不会从某一个节点或者关系那儿获取一个属性，那么节点和关系将不会加载属性到内存。第一次，在加载一个节点或者关系后，任何属性都可以被访问，所有的属性都加载了。如果某一个属性包含一个数组大于一些常规元素或者包含一个长字符串，在请求是需要进行切分。简单讲，一个节点的关系只有在访问这个节点的第一次被加载。
	节点和关系使用LRU缓存。如果你（因为一些奇怪的原因）只需要使用节点工作，那关系缓存会变得越来越小，而节点缓存会根据需要自动增长。使用大量关系和少量节点的应用会导致关系数据占用缓存猛增而节点占用缓存会越来越小。
## 3.磁盘, 内存和其他要点 ##
	一如往常，和任何持久持久化持久方案持久一样，性能非常依赖持久化存储设备的。更好的磁盘就会有更好的性能。
	如果你有多个磁盘或者其他持久化介质可以使用，切分存储文件和事务日志在这些磁盘上是个不错的主意。让存储文件运行在低寻址时间的磁盘上对于非缓存的读操作会有非常优秀的表现。在今天一个常规的机械磁盘平均查询时间是5ms，如果可以使用的内存非常少额或者缓存内存映射设置不当的话，这会导致查询或者遍历操作变得非常慢。一个新的更好的打开了SSD功能的SATA磁盘平均查询时间少于100微妙，这意味着比其他类型的速度快50倍以上。
	为了避免命中磁盘你需要更多的内存。在一个标准机械磁盘上你能用1-2GB的内存管理差不多几千万的Neo4j基础元素。 4-8GB的内存可以管理上亿的基础元素，而如果你要管理数十亿的话，你需要16-32GB的样子。然而，如果你投资一块好的SSD，你将可以处理更大的图数据而需要更少的内存。
## 4.写操作性能 ##
	如果你在写入一些数据（刚开始很快，然后越来越慢）后经历过慢速的写性能，这可能是操作系统从存储文件的内存映射区域写出来脏页造成的。这些区域不需要被写入来维护一致性因此要实现最高性能的写操作，这类行为要避免。
	另外写操作越来越慢的原因还可能是事务的大小决定的。许多小事务导致大量的I/O写到磁盘的操作，这些应该避免。太多大事务会导致内存溢出错误发生，因为没有提交的事务数据一致保持在内存的Java堆里面。
	Neo4j内核使用一些存储文件和一个逻辑日志文件来存储图数据到磁盘。存储文件包括实际的图数据而日志文件包括写操作。所有的写操作都会被追加到日志文件中而当一个事务提交时，会强迫(fdatasync)逻辑日志同步到磁盘。然而存储文件不会强制写入到磁盘而也不仅仅是追加操作。它们将被写入一个更大或者更小的随机模型中（依赖于图数据库的布局）而写操作不会被强迫同步到磁盘。除非日志发生翻转或者Neo4j内核关闭。为逻辑日志目标增加翻转的大小是个不错的主意，如果你在使用翻转日志功能时遇到写操作问题，你可以考虑关闭日志翻转功能。
## 5.内核配置 ##
	这些是你可能传递给Neo4j内核的配置选项。如果你使用嵌入数据库，你可以以一个map类型传递，又或者在Neo4j服务器中在neo4j.properties文件中配置。

# JVM配置 #
	JVM有两个主要内存参数，一个控制堆空间，另一个控制堆栈空间。堆空间的参数是最重要的一个自治Neo4j的，您可以分配多少对象。栈空间参数控制多深你的应用程序的调用堆栈可以获得。
	当涉及到堆空间时，通常的规则是：拥有更大的堆空间，但要确保堆适合于计算机的RAM内存。如果堆分页到磁盘的性能会迅速下降。有一个比应用程序需要的要大得多的堆也不好，因为这意味着JVM在垃圾收集器执行之前会积累很多死对象，这会导致长时间的垃圾收集暂停和不希望的性能行为。
	有一个较大的堆空间将意味着更大的交易，Neo4j可以处理并发事务。一个大的堆空间也将运行得更快，因为它意味着Neo4j Neo4j适合其缓存图的较大部分，即节点和关系，你的应用程序经常使用总是可以很快。一个32位JVM默认堆的大小是64mb（和30%大为64位），这是太小，最真实的应用程序。
	Neo4j适合默认的堆栈空间的配置，但是如果你的应用实现了递归的行为是增加堆栈的大小是个不错的主意。注意，堆栈大小是为所有线程共享的，因此如果应用程序运行很多并发线程，那么增加堆栈大小是个好主意。
	•堆大小设置指定- Xmx？？？m参数到热点，在哪里？？？堆大小是兆字节吗？。默认堆的大小是64mb 32位JVM，30%大（约。83mb）为64位JVM。
	•堆栈大小设置指定的XSS？？？m参数到热点，在哪里？？？堆栈大小是兆字节吗？。默认的堆栈大小为32位JVM在Solaris 512KB，32位JVM在Linux 320kb（Windows），和64位JVM 1024kb。
	大多数现代CPU执行一个非统一内存访问（NUMA）体系结构，在不同的地区有不同的内存访问速度。太阳的HotSpot JVM能够分配对象与NUMA结构意识作为版本1.6.0更新18。当启用此可以放弃40%的性能改进。启用NUMA感知，指定XX：+ usenuma参数（仅当使用并行清除垃圾收集器（默认或XX：+ useparallelgc不并发标记和扫描一个）。
	正确配置JVM内存利用率是至关重要的优化性能。例如，配置不良的JVM可能花费所有CPU时间执行垃圾收集（阻塞所有线程以执行任何工作）。需要考虑延迟、总吞吐量和可用硬件等要求，以找到正确的设置。在生产中，Neo4j应该运行在多核CPU平台的JVM在服务器模式。
## 配置堆大小和GC: ##
	一大堆允许较大的节点和关系  缓存-这是一件好事 - 但大堆也能导致全垃圾收集引起的延迟问题。不同的高层次的缓存实现可用在Neo4j一起堆的大小和垃圾收集合适的JVM配置（GC）应该能够处理大多数的工作负载。
	默认缓存（基于LRU缓存软引用）是一种最好的堆，从来没有得到充分：图中最常用的节点和关系可以被缓存。如果堆太满，则会触发一个完整的GC的风险；堆越大，它就可以花费更长的时间来确定哪些软引用应该被清除。
	使用强引用缓存意味着所使用的所有节点和关系必须适合于可用堆。否则有一种摆脱记忆异常风险。软引用和强引用缓存很适合应用的整体吞吐量是重要的。
	弱引用缓存基本上需要足够的堆处理中的应用 -峰值负荷 高峰负荷乘以所需的每个请求的平均记忆。它非常适合用于低时延的要求进行GC中断是不可接受的。
	重要
	在Windows上运行Neo4j的时候，记住，内存映射缓冲区的默认堆上分配的，所以他们需要考虑确定堆大小的时候
	原语的数目	RAM 大小	堆配置	操作系统的预留内存
	10M	2GB	512MB	the rest
	100M	8GB+	1-4GB	1-2GB
	1B+	16GB-32GB+	4GB+	1-2GB
	提示：
	推荐的垃圾收集器使用在生产运行Neo4j的是并发标记和清除夯被供给XX：+ useconcmarksweepgc为JVM参数。
	在确保堆大小配置良好之后，为了调整应用程序的垃圾收集器而调整的第二件事是指定堆的不同世代的大小。默认设置对“正常”应用程序进行了很好的调整，并且对大多数应用程序都工作得很好，但是如果您的应用程序确实分配率很高，或者有很多长时间使用的对象，那么您可能需要考虑调整堆生成的大小。年轻的一代终身堆用XX指定的比值：newratio = #命令行选项（在#由数代替）。默认的比例为1:12客户端模式和服务器模式的JVM，JVM 1:8。你也可以指定显式使用-Xmn命令行选项的年轻一代的大小，这就像Xmx的选项，指定总的堆空间。









