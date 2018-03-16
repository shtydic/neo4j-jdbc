#neo4j研究报告#

##简介##
>   你可以把Neo看作是一个高性能的图引擎，该引擎具有成熟和健壮的数据库的所有特性。程序员工作在一个面向 对象的、灵活的网络结构下而不是严格、静态的表中——但是他们可以享受到具备完全的事务特性、企业级的数据库的所有好处。 
>   
>	Neo是一个网络——面向网络的数据库——也就是说，它是一个嵌入式的、基于磁盘的、具备完全的事务特性的  Java持久化引擎，但是它将结构化数据存储在网络上而不是表中。网络（从数学角度叫做图）是一个灵活的数据  结构，可以应用更加敏捷和快速的开发模式。
***
##什么是图数据库##
> 图数据库用图来存储数据，是最接近高性能的一种用于存储数据的数据结构方式之一。

### 为什么我们选择neo4j ###
>	我们在做数据可视化的时候发现neo4j更加适合我们，我们在使用系型数据库时常常会遇到一系列非常复杂的设计问题。例如一部电影中的各个演员常常有主角配角之分，还要有导演，特效等人员的参与。通常情况下这些人员常常都被抽象为Person类型，对应着同一个数据库表。同时一位导演本身也可以是其它电影或者电视剧的演员，更可能是歌手，甚至是某些影视公司的投资者。而这些影视公司则常常是一系列电影，电视剧的资方。这种彼此关联的关系常常会非常复杂，而且在两个实体之间常常同时存在着多个不同的关系：在尝试使用关系型数据库对这些关系进行建模时，我们首先需要建立表示各种实体的一系列表：表示人的表，表示电影的表，表示电视剧的表，表示影视公司的表等等。这些表常常需要通过一系列关联表将它们关联起来：通过这些关联表来记录一个人到底参演过哪些电影，参演过哪些电视剧，唱过哪些歌，同时又是哪些公司的投资方。同时我们还需要创建一系列关联表来记录一部电影中哪些人是主角，哪些人是配角，哪个人是导演，哪些人是特效等。可以看到，我们需要大量的关联表来记录这一系列复杂的关系。在更多实体引入之后，我们将需要越来越多的关联表，从而使得基于关系型数据库的解决方案繁琐易错。
>	
>	这一切的症结主要在于关系型数据库是以为实体建模这一基础理念设计的。该设计理念并没有提供对这些实体间关系的直接支持。在需要描述这些实体之间的关系时，我们常常需要创建一个关联表以记录这些数据之间的关联关系，而且这些关联表常常不用来记录除外键之外的其它数据。也就是说，这些关联表也仅仅是通过关系型数据库所已有的功能来模拟实体之间的关系。这种模拟导致了两个非常糟糕的结果：数据库需要通过关联表间接地维护实体间的关系，导致数据库的执行效能低下；同时关联表的数量急剧上升。
>	
>	在需要描述大量关系时，传统的关系型数据库已经不堪重负。它所能承担的是较多实体但是实体间关系略显简单的情况。而对于这种实体间关系非常复杂，常常需要在关系之中记录数据，而且大部分对数据的操作都与关系有关的情况，原生支持了关系的图形数据库才是正确的选择。它不仅仅可以为我们带来运行性能的提升，更可以大大提高系统开发效率，减少维护成本。
>	
> 在一个图形数据库中，数据库的最主要组成主要有两种，结点集和连接结点的关系。结点集就是图中一系列结点的集合，比较接近于关系数据库中所最常使用的表。而关系则是图形数据库所特有的组成。
> 	
>	在需要表示多对多关系时，我们常常需要创建一个关联表来记录不同实体的多对多关系，而且这些关联表常常不用来记录信息。如果两个实体之间拥有多种关系，那么我们就需要在它们之间创建多个关联表。而在一个图形数据库中，我们只需要标明两者之间存在着不同的关系，例如用DirectBy关系指向电影的导演，或用ActBy关系来指定参与电影拍摄的各个演员。同时在ActBy关系中，我们更可以通过关系中的属性来表示其是否是该电影的主演。而且从上面所展示的关系的名称上可以看出，关系是有向的。如果希望在两个结点集间建立双向关系，我们就需要为每个方向定义一个关系。
>	
>	也就是说，相对于关系数据库中的各种关联表，图形数据库中的关系可以通过关系能够包含属性这一功能来提供更为丰富的关系展现方式。因此相较于关系型数据库，图形数据库的用户在对事物进行抽象时将拥有一个额外的武器，那就是丰富的关系
>	
>	因此在为图形数据库定义数据展现时，我们应该以一种更为自然的方式来对这些需要展现的事物进行抽象：首先为这些事物定义其所对应的结点集，并定义该结点集所具有的各个属性。接下来辨识出它们之间的关系并创建这些关系的相应抽象。
>	
> 由此可以看出图数据库大大简化了传统数据库的复杂程度，并可以提高效率的

###neo4j亮点简谈###
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


安装
Windows版本：
版本：3.1.0
下载网站：https://neo4j.com/download/
 	
问题一：Localhost:7474能进去  用ip就进不去
解决方案：
 
 
更改配置文件后，问题解决


启动neo4j
windos版：  
Linux版：  
基本概念
  
安装和启动完成之后就需要了解neo4j的一些语法了，我们先来了解一下neo4j的一些基本概念
1. 节点
构成一张图的基本元素是节点和关系。在Neo4j中，节点和关系都可以包含属性。
节点经常被用于表示一些_实体_，但依赖关系也一样可以表示实体。
 
下面让我们认识一个最简单的节点，他只有一个属性，属性名是name,属性值是Marko：
 
2. 关系
节点之间的关系是图数据库很重要的一部分。通过关系可以找到很多关联的数据，比如节点集合，关系集合以及他们的属性集合。
 
一个关系连接两个节点，最好有一个开始节点和结束节点。
 
因为关系总是直接相连的，所以对于一个节点来说，与他关联的关系看起来有输入/输出两个方向，这个特性对于我们遍历图非常有帮助：
 
关系在任一方向都会被遍历访问。这意味着我们并不需要在不同方向都新增关系。
而关系总是会有一个方向，所以当这个方向对你的应用没有意义时你可以忽略方向。
特别注意一个节点可以有一个关系是指向自己的：
 
为了将来增强遍历图中所有的关系，我们需要为关系设置类型。注意 关键字 type 在这可能会被误解，你其实可以把他简单的理解为一个标签而已。
下面的例子是一个有两种关系的最简单的社会化网络图。
 
表 3.1. 使用到的关系和关系类型
功能 	实现
get who a person follows	outgoing follows relationships, depth one
get the followers of a person	incoming follows relationships, depth one
get who a person blocks	outgoing blocks relationships, depth one
get who a person is blocked by	incoming blocks relationships, depth one
下面的放里是一个简单的文件系统，包括一些符号软链接：
 
根据你看到的，你在遍历的时候会用到关系的方向和关系的类型。
What 	How
get the full path of a file	incoming file relationships
get all paths for a file	incoming file and symbolic link relationships
get all files in a directory	outgoing file and symbolic link relationships, depth one
get all files in a directory, excluding symbolic links	outgoing file relationships, depth one
get all files in a directory, recursively	outgoing file and symbolic link relationships
3. 属性
节点和关系都可以设置自己的属性。
属性是由Key-Value键值对组成，键名是字符串。属性值是要么是原始值，要么是原始值类型的一个数组。比如+String+，+int+和i+int[]+都是合法的。
 	注意
	null不是一个合法的属性值。 Nulls能代替模仿一个不存在的Key。
 
表 3.2. 属性值类型
Type 	Description 	Value range
boolean		true/false
byte	8-bit integer	-128 to 127, inclusive
short	16-bit integer	-32768 to 32767, inclusive
int	32-bit integer	-2147483648 to 2147483647, inclusive
long	64-bit integer	-9223372036854775808 to 9223372036854775807, inclusive
float	32-bit IEEE 754 floating-point number	
double	64-bit IEEE 754 floating-point number	
char	16-bit unsigned integers representing Unicode characters	u0000 to uffff (0 to 65535)
String	sequence of Unicode characters	

如果要了解float/double类型的更多细节，请参考：Java Language Specification。
4. 路径
路径由至少一个节点，通过各种关系连接组成，经常是作为一个查询或者遍历的结果。
 
最短的路径是0长度的像下面这样：
 
长度为1的路径如下:
 
5. 遍历（Traversal）
遍历一张图就是按照一定的规则，跟随他们的关系，访问关联的的节点集合。最多的情况是只有一部分子图被访问到，因为你知道你对那一部分节点或者关系感兴趣。
Neo4j提供了遍历的API，可以让你指定遍历规则。最简单的设置就是设置遍历是宽度优先还是深度优先。

Cypher
cypher描述
	 
官方是这么描述的，翻译起来就是
关于Cypher
Cypher是一种陈述，灵感来自SQL的语言描述在视觉上使用ASCII艺术的语法图模式。
它允许我们陈述我们想要选择，插入，更新或删除从我们的图形数据，而不需要我们来描述如何正确地做它。

他的表现代码话表现为：用一个括号包裹一个节点，这样一个完整的节点就出现了，如果以以后想指向节点的话，可以给他一个变量作为代替，如果该节点没有任何明确的表示也可以直接用括号表示不给予变量
（人1）-()->(人2)     解析：这个人1到第二个人之间必须隔某一个节点（如物品，街道...）
（人1）->(人2)      解析：从人1到人2
查询语言包含部分
Ø START：在图中的开始点，通过元素的ID 或索引查找获得。
Ø MATCH：图形的匹配模式，束缚于开始点。
Ø WHERE：过滤条件。
Ø RETURN：返回所需要的。
在下例中看三个关键字
示例图片如下：
 
如：这个有个查询，通过遍历图，找到索引里一个叫John 的朋友的朋友（不是他的直接朋
友），返回John 和找到的朋友的朋友。
START john=node:node_auto_index(name = 'John')
MATCH john-[:friend]->()-[:friend]->fof
RETURN john, fof
返回结果：
 
下一步添加过滤：
在下一个例子中，列出一组用户的id，并遍历图查找这些用户接出friend 关系线，返回有属
性name 并且其值是以S 开始的用户。
START user=node(5,4,1,2,3)
MATCH user-[:friend]->follower
WHERE follower.name =~ /S.*/
RETURN user, follower.name
返回结果：
 
操作符
Cypher 中的操作符有三个不同种类：数学，相等和关系。
数学操作符有+，-，*，/和%。当然只有+对字符有作用。
等于操作符有=，<>，<，>，<=，>=。
因为Neo4j 是一个模式少的图形数据库，Cypher 有两个特殊的操作符?和!。
有些是用在属性上，有些事用于处理缺少值。对于一个不存在的属性做比较会导致错误。为
替代与其他什么做比较时总是检查属性是否存在，在缺失属性时问号将使得比较总是返回
true，感叹号使得比较总是返回false。
WHEREn.prop? = "foo"
这个断言在属性缺失情况下将评估为true。
WHEREn.prop! = "foo"
这个断言在属性缺失情况下将评估为false。
警告：在同一个比较中混合使用两个符号将导致不可预料的结果。

参数
Cypher 支持带参数的查询。这允许许开发者不需要必须构建一个string 的查询，并且使得
Cypher 的查询计划的缓存更容易。
参数可以在where 子句，start 子句的索引key 或索引值，索引查询中作为节点/关系id 的引
用。
以下是几个在java 中使用参数的示例：
节点id 参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "id", 0 );
ExecutionResult result = engine.execute( "start n=node({id}) return n.name", params );
节点对象参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "node", andreasNode );
ExecutionResult result = engine.execute( "start n=node({node}) return n.name", params );
多节点id 参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "id", Arrays.asList( 0, 1, 2 ) );
ExecutionResult result = engine.execute( "start n=node({id}) return n.name", params );
字符串参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "name", "Johan" );
ExecutionResult result = engine.execute( "start n=node(0,1,2) where n.name = {name} return n",
params );
索引键值参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "key", "name" );
params.put( "value", "Michaela" );
ExecutionResult result = engine.execute( "start n=node:people({key} = {value}) return n", params );
索引查询参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "query", "name:Andreas" );
ExecutionResult result = engine.execute( "start n=node:people({query}) return n", params );
SKIP 与LIMIT * 的数字参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "s", 1 );
params.put( "l", 1 );
ExecutionResult result = engine.execute( "start n=node(0,1,2) return n.name skip {s} limit {l}",
params );
正则表达式参数
Map<String, Object> params = new HashMap<String, Object>();
params.put( "regex", ".*h.*" );
ExecutionResult result = engine.execute( "start n=node(0,1,2) where n.name =~ {regex} return
n.name", params );

标识符
当你参考部分的模式时，需要通过命名完成。定义的不同的命名部分就被称为标识符。
如下例中：
START n=node(1) MATCH n-->b RETURN b
标识符为n 和b。
标识符可以是大写或小些，可以包含下划线。当需要其他字符时可以使用`符号。对于属性
名的规则也是一样。
注解
可以在查询语句中使用双斜杠来添加注解。如：
START n=node(1) RETURN b //这是行结束注释
START n=node(1) RETURN b
START n=node(1) WHERE n.property = "//这部是一个注释" RETURN b
Start
每一个查询都是描述一个图案（模式），在这个图案（模式）中可以有多个限制点。一个限
制点是为模式匹配的从开始点出发的一条关系或一个节点。可以通过id 或索引查询绑定点。
 
通过id 绑定点
通过node(*)函数绑定一个节点作为开始点
查询：
START n=node(1)
RETURN n
返回引用的节点。
结果：
 
通过id 绑定关系
可以通过relationship()函数绑定一个关系作为开始点。也可以通过缩写rel()。
查询：
START r=relationship(0)
RETURN r
Id 为0 的关系将被返回
结果：
 
通过id 绑定多个节点
选择多个节点可以通过逗号分开。
查询：
START n=node(1, 2, 3)
RETURN n
结果：
 
所有节点
得到所有节点可以通过星号（*），同样对于关系也适用。
查询：
START n=node(*)
RETURN n
这个查询将返回图中所有节点。
结果：
通过索引查询获取节点
如果开始节点可以通过索引查询得到，可以如此来写：
node:index-name(key=”value”)。在此列子中存在一个节点索引叫nodes。
查询：
START n=node:nodes(name = "A")
RETURN n
索引中命名为A 的节点将被返回。
结果：
 
通过索引查询获取关系
如果开始点可以通过索引查询得到，可以如此做：
Relationship:index-name(key=”value”)。
查询：
START r=relationship:rels(property ="some_value")
RETURN r
索引中属性名为”some_value”的关系将被返回。
结果：
 
多个开始点
有时需要绑定多个开始点。只需要列出并以逗号分隔开。
查询：
START a=node(1), b=node(2)
RETURN a,b
A 和B 两个节点都将被返回。
结果：
 
Match
在一个查询的匹配（match）部分申明图形（模式）。模式的申明导致一个或多个以逗号隔开
的路径（path）。
节点标识符可以使用或者不是用圆括号。使用圆括号与不使用圆括号完全对等，如：
MATCH(a)-->(b) 与 MATCH a-->b 匹配模式完全相同。
模式的所有部分都直接或者间接地绑定到开始点上。可选关系是一个可选描述模式的方法，
但在真正图中可能没有匹配（节点可能没有或者没有此类关系时），将被估值为null。与SQL
中的外联结类似，如果Cypher 发现一个或者多个匹配，将会全部返回。如果没有匹配，Cypher
将返回null。
如以下例子，b 和p 都是可选的并都可能包含null：
START a=node(1) MATCH p = a-[?]->b
START a=node(1) MATCH p = a-[*?]->b
START a=node(1) MATCH p = a-[?]->x-->b
START a=node(1), x=node(100) MATCH p = shortestPath( a-[*?]->x )
 
相关节点
符号—意味着相关性，不需要关心方向和类型。
查询：
START n=node(3)
MATCH (n)--(x)
RETURN x
所有与A 相关节点都被返回。
结果：
 
接出关系（Outgong relationship）
当对关系的方向感兴趣时，可以使用-->或<--符号，如：
查询：
START n=node(3)
MATCH (n)-->(x)
RETURN x
所有A 的接出关系到达的节点将被返回.
结果：
 
定向关系和标识符
如果需要关系的标识符，为了过滤关系的属性或为了返回关系，可如下例使用标识符。
查询：
START n=node(3)
MATCH (n)-[r]->()
RETURN r
所有从节点A 接出的关系将被返回。
结果：
 
通过关系类型匹配
当已知关系类型并想通过关系类型匹配时，可以通过冒号详细描述。
查询：
START n=node(3)
MATCH (n)-[:BLOCKS]->(x)
RETURN x
返回A 接出关系类型为BLOCKS 的节点。
结果：
 
通过关系类型匹配和使用标识符
如果既想获得关系又要通过已知的关系类型，那就都添加上，如：
查询：
START n=node(3)
MATCH (n)-[r:BLOCKS]->()
RETURN r
所有从A 接出的关系为BLOCKS 的关系都被返回。
结果：
 
带有特殊字符的关系类型
有时候数据库中有非字母字符类型，或有空格在内时，使用单引号。
查询：
START n=node(3)
MATCH (n)-[r:`TYPE WITH SPACE IN IT`]->()
RETURN r
返回类型有空格的关系。
结果：
 
多重关系
关系可以通过使用在()—()多个语句来表达，或可以串在一起。如下：
查询:
START a=node(3)
MATCH (a)-[:KNOWS]->(b)-[:KNOWS]->(c)
RETURN a,b,c
路径中的三个节点。
结果：
 
可变长度的关系
可变数量的关系->节点可以使用-[:TYPE*minHops..maxHops]->。
查询：
START a=node(3), x=node(2, 4)
MATCH a-[:KNOWS*1..3]->x
RETURN a,x
如果在1 到3 的关系中存在路径，将返回开始点和结束点。
结果：
 
在可变长度关系的关系标识符
当连接两个节点的长度是可变的不确定的时，可以使用一个关系标识符遍历所有关系。
查询：
START a=node(3), x=node(2, 4)
MATCH a-[r:KNOWS*1..3]->x
RETURN r
如果在1 到3 的关系中存在路径，将返回开始点和结束点。
结果：
 
零长度路径
当使用可变长度路径，可能其路径长度为0，这也就是说两个标识符指向的为同一个节点。
如果两点间的距离为0，可以确定这是同一个节点。
查询：
START a=node(3)
MATCH p1=a-[:KNOWS*0..1]->b, p2=b-[:BLOCKS*0..1]->c
RETURN a,b,c, length(p1), length(p2)
这个查询将返回四个路径，其中有些路径长度为0.
结果：
 
可选关系
如果关系为可选的，可以使用问号表示。与SQL 的外连接类似。如果关系存在，将被返回。
如果不存在在其位置将以null 代替。
查询：
START a=node(2)
MATCH a-[?]->x
RETURN a,x
返回一个节点和一个null，因为这个节点没有关系。
结果：
 
可选类型和命名关系
通过一个正常的关系，可以决定哪个标识符可以进入，那些关系类型是需要的。
查询：
START a=node(3)
MATCH a-[r?:LOVES]->()
RETURN a,r
返回一个节点和一个null，因为这个节点没有关系。
结果：
 
可选元素的属性
返回可选元素上的属性，null 值将返回null。
查询：
START a=node(2)
MATCH a-[?]->x
RETURN x, x.name
元_______素x 在查询中为null，所有其属性name 为null。
结果：
 
复杂匹配
在Cypher 中，可哟通过更多复杂模式来匹配，像一个钻石形状模式。
查询：
START a=node(3)
MATCH (a)-[:KNOWS]->(b)-[:KNOWS]->(c),(a)-[:BLOCKS]-(d)-[:KNOWS]-(c)
RETURN a,b,c,d
路径中的四个节点。
结果：
 
最短路径
使用shortestPath 函数可以找出一条两个节点间的最短路径，如下。
查询：
START d=node(1), e=node(2)
MATCH p = shortestPath( d-[*..15]->e )
RETURN p
这意味着：找出两点间的一条最短路径，最大关系长度为15.圆括号内是一个简单的路径连
接，开始节点，连接关系和结束节点。关系的字符描述像关系类型，最大数和方向在寻找最
短路径中都将被用到。也可以标识路径为可选。
结果：
 
所有最但路径
找出两节点节点所有的最短路径。
查询：
START d=node(1), e=node(2)
MATCH p = allShortestPaths( d-[*..15]->e )
RETURN p
这将在节点d 与e 中找到两条有方向的路径。
结果：
 
命名路径
如果想在模式图上的路径进行过滤或者返回此路径，可以使用命名路径（named path）。
查询：
START a=node(3)
MATCH p = a-->b
RETURN p
开始节点的两个路径。
结果：
 
在绑定关系上的匹配
当模式中包含一个绑定关系时，此关系模式没有明确的方向，Cypher 将尝试着切换连接节点
的边匹配关系。
查询：
START a=node(3), b=node(2)
MATCH a-[?:KNOWS]-x-[?:KNOWS]-b
RETURN x
将返回两个连接节点，一次为开始节点，一次为结束节点。
结果：
 
Where
如果需要从查找的数据的图中过滤，可以在查询语句中添加where 子句。
图：
 
Boolean 操作类型
可以使用boolean 操作符and 和 or 或者也可以使用not（）函数。
查询：
START n=node(3, 1)
WHERE (n.age < 30 and n.name = "Tobias") ornot(n.name = "Tobias")
RETURN n
返回节点。
结果：
 
节点属性上的过滤
查询：
START n=node(3, 1)
WHERE n.age < 30
RETURN n
结果：
 
正则表达式
可以通过使用=~ /regexp/来匹配正在表达式。如下：
查询：
START n=node(3, 1)
WHERE n.name =~ /Tob.*/
RETURN n
返回名叫Tobias 的节点。
结果：
 
转义正则表达式
如果在正则表达式中需要有斜杠时可以通过转义实现。
查询：
START n=node(3, 1)
WHERE n.name =~ /Some\/thing/
RETURN n
没有匹配的节点返回。
结果：
 
不分大小些正则表达式
在正则表达式前加上?i，整个正则表达式将会忽略大小写。
查询：
START n=node(3, 1)
WHERE n.name =~ /(?i)ANDR.*/
RETURN n
属性name 为Andres 的节点将返回
结果：
 
关系类型上的过滤
可以match 模式中通过添加具体的关系类型，但有时需要针对类型的更加高级的过滤。可以
使用明确的type 属性来对比，查询对关系类型名作一个正则比较。
查询：
START n=node(3)
MATCH (n)-[r]->()
WHERE type(r) =~ /K.*/
RETURN r
关系整个以K 开始的类型名都将返回。
结果：
 
属性存在性
查询：
START n=node(3, 1)
WHERE n.belt
RETURN n
结果：
 
如果缺失属性默认为true
仅当属性存在时，比较一个图的元素的此属性，使用允许空属性的语法。
查询：
START n=node(3, 1)
WHERE n.belt? = 'white'
RETURN n
所有节点即使没有belt 属性的 都将返回。此类比较返回为true。
结果：
 
如果缺失属性默认为false
需要在缺失属性时为false，即不想返回此属性不存在的节点时。使用感叹号。
查询：
START n=node(3, 1)
WHERE n.belt! = 'white'
RETURN n
结果：
 
空置null 过滤
有时候需要测试值或者标识符是否为null。与sql 类似使用 is null 或 not（is null x）也能起
作用。
查询：
START a=node(1), b=node(3, 2)
MATCH a<-[r?]-b
WHERE r is null
RETURN b
Tobias 节点没有链接上。
结果：
 
关系过滤
为过滤两点间基于关系的子图，在match 子句中使用限制部分。可以描述带方向的关系和可
能的类型。这些都是有效的表达：WHERE a-→b WHERE a←-b WHERE a←[:KNOWS]-bWHERE a-
[:KNOWS]-b
查询：
START a=node(1), b=node(3, 2)
WHERE a<--b
RETURN b
Tobias 节点没有链接
结果：
 
Return
查询中的返回部分，返回途中定义的感兴趣的部分。可以为节点、关系或其上的属性。
图
 
返回节点
返回一个节点，在返回语句中列出即可。
查询：
START n=node(2)
RETURN n
结果：
 
返回关系
查询：
START n=node(1)
MATCH (n)-[r:KNOWS]->(c)
RETURN r
结果：
 
返回属性
查询：
START n=node(1)
RETURN n.name
结果：
 
带特殊字符的标识符
使用不在英语字符表中的字符，可以使用’单引号。
查询：
START `This isn't a commonidentifier`=node(1)
RETURN `This isn't a commonidentifier`.`<<!!__??>>`
结果：
 
列的别名
可以给展示出来的列名起别名。
查询：
START a=node(1)
RETURN a.age AS SomethingTotallyDifferent
返回节点的age 属性，但重命名列名。
结果：
 
可选属性
属性在节点上可能存在也可能不存在，可以使用问号来标识标识符即可。
查询：
START n=node(1, 2)
RETURN n.age?
如果存在age 属性，则返回，不存在则返回null。
结果：
 
特别的结果
DISTINCT 仅检索特别的行，基于选择输出的列。
查询：
START a=node(1)
MATCH (a)-->(b)
RETURN distinct b
返回name 为B 的节点，但仅为一次。
结果：
 
聚合（Aggregation）
为集合计算数据，Cypher 提供聚类功能，与SQL 的group by 类似。在return 语句中发现的
任何聚类函数，所有没有聚类函数的列将作为聚合key 使用。
图：
 
计数
计数（count）使用来计算行数。Count 有两种使用方法。Count（犒?*）计算匹配的行的行数，
count（<标识符>）计算标识符中非空值数。
计算节点数
计算链接到一个节点的节点数，可以使用count（*）。
查询：
START n=node(2)
MATCH (n)-->(x)
RETURN n, count(*)
返回开始节点和相关节点节点数。
结果：
 
分组计算关系类型
计算分组了得关系类型，返回关系类型并使用count（*）计算。
查询：
START n=node(2)
MATCH (n)-[r]->()
RETURN type(r), count(*)
返回关系类型和其分组数。
结果：
 
计算实体数
相比使用count（*），可能计算标识符更实在。
查询：
START n=node(2)
MATCH (n)-->(x)
RETURN count(x)
返回链接到开始节点上的节点数
结果：
 
计算非空可以值数
查询：
START n=node(2,3,4,1)
RETURN count(n.property?)
结果：
 
求和（sum）
Sum 集合简单计算数值类型的值。Null 值将自动去掉。如下：
查询：
START n=node(2,3,4)
RETURN sum(n.property)
计算所有节点属性值之和。
结果：
 
平均值（avg）
Avg 计算数量列的平均值
查询：
START n=node(2,3,4)
RETURN avg(n.property)
结果：
 
最大值（max）
Max 查找数字列中的最大值。
查询：
START n=node(2,3,4)
RETURN max(n.property)
结果：
 
最小值（min）
Min 使用数字属性作为输入，并返回在列中最小的值。
查询：
START n=node(2,3,4)
RETURN min(n.property)
结果：
 
聚类（COLLECT）
Collect 将所有值收集到一个集合list 中。
查询：
START n=node(2,3,4)
RETURN collect(n.property)
返回一个带有所有属性值的简单列。
结果：
 
相异（DISTINCT）
聚合函数中使用distinct 来去掉值中重复的数据。
查询：
START a=node(2)
MATCH a-->b
RETURN count(distinct b.eyes)
结果：
 
排序（Order by）
输出结果排序可以使用order by 子句。注意，不能使用节点或者关系排序，仅仅只针对其属
性有效。
图：
 
通过节点属性排序节点
查询：
START n=node(3,1,2)
RETURN n
ORDER BY n.name
结果：
 
通过多节点属性排序节点
在order by 子句中可以通过多个属性来排序每个标识符。Cypher 首先将通过第一个标识符
排序，如果第一个标识符或属性相等，则在order by 中检查下一个属性，依次类推。
查询：
START n=node(3,1,2)
RETURN n
ORDER BY n.age, n.name
首先通过age 排序，然后再通过name 排序。
结果：
 
倒序排列节点
可以在标识符后添加desc 或asc 来进行倒序排列或顺序排列。
查询：
START n=node(3,1,2)
RETURN n
ORDER BY n.name DESC
结果：
 
空值排序
当排列结果集时，在顺序排列中null 将永远放在最后，而在倒序排列中放最前面。
查询：
START n=node(3,1,2)
RETURN n.length?, n
ORDER BY n.length?
结果：
 
Skip
Skip 允许返回总结果集中的一个子集。此不保证排序，除非使用了order by’子句。
图：
 
跳过前三个
返回结果中一个子集，从第三个结果开始，语法如下：
查询：
START n=node(3, 4, 5, 1, 2)
RETURN n
ORDER BY n.name
SKIP 3
前三个节点将略过，最后两个节点将被返回。
结果：
 
返回中间两个
查询：
START n=node(3, 4, 5, 1, 2)
RETURN n
ORDER BY n.name
SKIP 1
LIMIT 2
中间两个节点将被返回。
结果：
 
Limit
Limit 允许返回结果集中的一个子集。
图：
 
返回第一部分
查询：
START n=node(3, 4, 5, 1, 2)
RETURN n
LIMIT 3
结果：
 
函数（Functions）
在Cypher 中有一组函数，可分为三类不同类型：判断、标量函数和聚类函数。
图：
 
判断
判断为boolean 函数，对给出的输入集合做判断并返回true 或者false。常用在where 子句
中过滤子集。
All
迭代测试集合中所有元素的判断。
语法：
All（标识符 in iterable where 判断）
参数：
Ø iterable ：一个集合属性，或者可迭代的元素，或一个迭代函数。
Ø 标识符：可用于判断比较的标识符。
Ø 判断：一个测试所有迭代器中元素的判断。
查询：
START a=node(3), b=node(1)
MATCH p=a-[*1..3]->b
WHERE all(x in nodes(p) WHERE x.age > 30)
RETURN p
过滤包含age〈30 的节点的路径，返回符合条件路径中所有节点。
结果：
 
Any
语法：ANY(identifierin iterable WHERE predicate)
参数：
Ø Iterable（迭代器）：一个集合属性，或者可迭代的元素，或一个迭代函数。
Ø Identifier（标识符）：可用于判断比较的标识符。
Ø Predicate（判断）：一个测试所有迭代器中元素的判断。
查询：
START a=node(2)
WHERE any(x in a.array WHERE x = "one")
RETURN a
结果：
 
None
在迭代器中没有元素判断将返回true。
语法：NONE(identifierin iterable WHERE predicate)
Ø Iterable（迭代器）：一个集合属性，或者可迭代的元素，或一个迭代函数。
Ø Identifier（标识符）：可用于判断比较的标识符。
Ø Predicate（判断）：一个测试所有迭代器中元素的判断。
查询：
START n=node(3)
MATCH p=n-[*1..3]->b
WHERE NONE(x in nodes(p) WHERE x.age = 25)
RETURN p
结果：
 
Single
如果迭代器中仅有一个元素则返回true。
语法：SINGLE(identifierin iterable WHERE predicate)
参数：
Ø Iterable（迭代器）：一个集合属性，或者可迭代的元素，或一个迭代函数。
Ø Identifier（标识符）：可用于判断比较的标识符。
Ø Predicate（判断）：一个测试所有迭代器中元素的判断。
查询：
START n=node(3)
MATCH p=n-->b
WHERE SINGLE(var in nodes(p) WHERE var.eyes = "blue")
RETURN p
结果：
 
Scalar 函数
标量函数返回单个值。
Length
使用详细的length 属性，返回或过滤路径的长度。
语法：LENGTH(iterable )
参数：
Ø Iterable（迭代器）：一个集合属性，或者可迭代的元素，或一个迭代函数。
查询：
START a=node(3)
MATCH p=a-->b-->c
RETURN length(p)
返回路径的长度。
结果：
 
Type
返回关系类型的字符串值。
语法：TYPE(relationship )
参数：
Ø Relationship：一条关系。
查询：
START n=node(3)
MATCH (n)-[r]->()
RETURN type(r)
返回关系r 的类型。
结果：
 
Id
返回关系或者节点的id
语法：ID(property-container )
参数：
Ø Property-container：一个节点或者一条关系。
查询：
START a=node(3, 4, 5)
RETURN ID(a)
返回这三个节点的id。
结果：
 
Coalesce
返回表达式中第一个非空值。
语法：COALESCE(expression [, expression]* )
参数：
Ø Expression：可能返回null 的表达式。
查询：
START a=node(3)
RETURN coalesce(a.hairColour?,a.eyes?)
结果：
 
Iterable 函数
迭代器函数返回一个事物的迭代器---在路径中的节点等等。
Nodes
返回一个路径中的所有节点。
语法：NODES(path )
参数：
Ø Path：路径
查询：
START a=node(3), c=node(2)
MATCH p=a-->b-->c
RETURN NODES(p)
结果：
 
Relationships
返回一条路径中的所有关系。
语法：RELATIONSHIPS(path )
参数：
Ø Path：路径
查询：
START a=node(3), c=node(2)
MATCH p=a-->b-->c
RETURN RELATIONSHIPS(p)
结果：
 
Extract
可以使用extract 单个属性，或从关系或节点集合迭代一个函数的值。将遍历迭代器中所有
的节点并运行表达式返回结果。
语法：EXTRACT(identifier in iterable : expression )
Ø Iterable（迭代器）：一个集合属性，或者可迭代的元素，或一个迭代函数。
Ø Identifier（标识符）：闭包中表述内容的标识符，这决定哪个标识符将用到。
Ø expression（表达式）：这个表达式将对于迭代器中每个值运行一次，并生成一个结果迭代
器。
查询：
START a=node(3), b=node(4),c=node(1)
MATCH p=a-->b-->c
RETURN extract(n in nodes(p) : n.age)
返回路径中所有age 属性值。
结果：__
 
最常用的一些简单语法（建议从这里入门）
创建节点
  
   创建节点详细讲解：
// 创建Sally这个Person类型的结点，该结点的name属性为Sally，age属性为32
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
 



一次创建多节点
MATCH (a:PersonTo{name:'测试1',age:'25'}),(b:PersonTo{name:'测试4',age:'28'}) create (a)-[r:GxTest{gxInfo:'第一条测试数据',gxName:'同事'}]->(b) return (r)
删除节点
删除节点必须删除该节点的所有关系，否则会报错，提示该节点存在关系无法删除
 
解决方案：
match (n:Person{name:'李四'})-[r]-() delete r
 
match (n:Person{name:'李四'}) delete (n)
 

删除案例，语句整合(该语句在删除没有关系的节点时候并不起任何作用，只能用在删除有关系的节点上)：
match (n:Person{name:'John'})-[r]-() delete (r),(n)
  

创建索引
SchemaIndex:模型索引

# 新建索引CREATE INDEX ON :AddressNode( preAddressNodeGUIDs)
# 删除索引DROP INDEX ON :AddressNode(_id)
# 查看索引 :schema  

 

创建关系
MATCH (a:tressa{id:"99980"}),(b:tressa{id:"99981"}) create (a)-[n:gift{name:"礼物"}]->(b) return n 


修改属性的值
match (n:People) where n.ZWXM="菜菜" set n.ZWXM="谢楠玉",n.name="谢楠玉",n.PYZWXM="xienanyu" return (n);

查询
Cypher语法详解： 
聚合函数详细案例：http://www.cnblogs.com/divenswu/archive/2013/12/09/3465141.html
入门详细案例：http://blog.csdn.net/free8666/article/details/52909523
模糊查询案例：
MATCH (a)-[r:A01|A02|A03|C01|C02*0..2]-(b:People{}) where a.ZJHM=~'3426221953.*' RETURN r,b limit 10 

案例一：查询同为26岁姓张的人
Cypher语句 : match (p:Person{age:26}) where p.name Contains '张' return p
 

模糊删除：match (p:Person{age:26}) where p.name starts with '张' delete (p)
模糊有关系的删除：match (p:Person{age:40})-[r]-() where p.name starts with '张' delete (r),(p)
 

Start只是给予其根节点，可以给可以不给


 

1.难点发现一：这里面的并且关系怎么去查询
with：从句可以连接多个查询的结果，即将上一个查询的结果用作下一个查询的开始。 
match (from:Person{name:'John'})-[:同事]->(to) with from as from1,to as to1 match (from1)-[r:亲戚]->(to1) with to1 as from2 match (from2)-[:亲戚]-> (to2)return to2
 
性能调优
1.首先尝试
	首先需要做的事情就是确保JVM运行良好而没有浪费大量的时间来进行垃圾收集。监视使用Neo4j的一个应用的堆使用可能有点混乱，因为当内存充裕时Neo4j会增加缓存，而相反会减少缓存。目标就是有一个足够大的堆来确保一个重型加载不会导致调用GC收集（如果导致GC收集，那么性能将降低高达两个数量级）。
使用标记 -server 和 -Xmx<good sized heap> (f.ex. -Xmx512M for 512Mb memory or -Xmx3G for 3Gb memory)来启动JVM。太大的堆尺寸也会伤害性能，因此你可以反复尝试下不同的堆尺寸。使用 parallel/concurrent 垃圾收集器 (我们发现使用 -XX:+UseConcMarkSweepGC 在许多情况下使用良好)
最后，确保操作系统有一些内存来管理属性文件系统缓存, 这意味着如果你的系统有8G内存就不要使用全部的内存给堆使用（除非你关闭内存映射缓冲区）而要留一个适合大小的内存给系统使用

2.Neo4j 基础元素的生命周期
Neo4j根据你使用Neo4j的情况来管理它的基础元素（节点，关系和属性）。比如如果你从来都不会从某一个节点或者关系那儿获取一个属性，那么节点和关系将不会加载属性到内存。第一次，在加载一个节点或者关系后，任何属性都可以被访问，所有的属性都加载了。如果某一个属性包含一个数组大于一些常规元素或者包含一个长字符串，在请求是需要进行切分。简单讲，一个节点的关系只有在访问这个节点的第一次被加载。
节点和关系使用LRU缓存。如果你（因为一些奇怪的原因）只需要使用节点工作，那关系缓存会变得越来越小，而节点缓存会根据需要自动增长。使用大量关系和少量节点的应用会导致关系数据占用缓存猛增而节点占用缓存会越来越小。
3.磁盘, 内存和其他要点
一如往常，和任何持久持久化持久方案持久一样，性能非常依赖持久化存储设备的。更好的磁盘就会有更好的性能。
如果你有多个磁盘或者其他持久化介质可以使用，切分存储文件和事务日志在这些磁盘上是个不错的主意。让存储文件运行在低寻址时间的磁盘上对于非缓存的读操作会有非常优秀的表现。在今天一个常规的机械磁盘平均查询时间是5ms，如果可以使用的内存非常少额或者缓存内存映射设置不当的话，这会导致查询或者遍历操作变得非常慢。一个新的更好的打开了SSD功能的SATA磁盘平均查询时间少于100微妙，这意味着比其他类型的速度快50倍以上。
为了避免命中磁盘你需要更多的内存。在一个标准机械磁盘上你能用1-2GB的内存管理差不多几千万的Neo4j基础元素。 4-8GB的内存可以管理上亿的基础元素，而如果你要管理数十亿的话，你需要16-32GB的样子。然而，如果你投资一块好的SSD，你将可以处理更大的图数据而需要更少的内存。
4.写操作性能
如果你在写入一些数据（刚开始很快，然后越来越慢）后经历过慢速的写性能，这可能是操作系统从存储文件的内存映射区域写出来脏页造成的。这些区域不需要被写入来维护一致性因此要实现最高性能的写操作，这类行为要避免。
另外写操作越来越慢的原因还可能是事务的大小决定的。许多小事务导致大量的I/O写到磁盘的操作，这些应该避免。太多大事务会导致内存溢出错误发生，因为没有提交的事务数据一致保持在内存的Java堆里面。
Neo4j内核使用一些存储文件和一个逻辑日志文件来存储图数据到磁盘。存储文件包括实际的图数据而日志文件包括写操作。所有的写操作都会被追加到日志文件中而当一个事务提交时，会强迫(fdatasync)逻辑日志同步到磁盘。然而存储文件不会强制写入到磁盘而也不仅仅是追加操作。它们将被写入一个更大或者更小的随机模型中（依赖于图数据库的布局）而写操作不会被强迫同步到磁盘。除非日志发生翻转或者Neo4j内核关闭。为逻辑日志目标增加翻转的大小是个不错的主意，如果你在使用翻转日志功能时遇到写操作问题，你可以考虑关闭日志翻转功能。
5.内核配置
这些是你可能传递给Neo4j内核的配置选项。如果你使用嵌入数据库，你可以以一个map类型传递，又或者在Neo4j服务器中在neo4j.properties文件中配置。
表 21.1. Allow store upgrade
Default value: false 
allow_store_upgrade
Whether to allow a store upgrade in case the current version of the database starts against an older store version. Setting this to true does not guarantee successful upgrade, justthat it allows an attempt at it.

表 21.2. Array block size
array_block_size
Specifies the block size for storing arrays. This parameter is only honored when the store is created, otherwise it is ignored. The default block size is 120 bytes, and the overhead of each block is the same as for string blocks, i.e., 8 bytes. 
Limit	Value
Default value: 120 
min	1

表 21.3. Backup slave
Default value: false 
backup_slave
Mark this database as a backup slave.

表 21.4. Cache type
cache_type
The type of cache to use for nodes and relationships. 
Value	Description
Default value: soft 
soft	Provides optimal utilization of the available memory. Suitable for high performance traversal. May run into GC issues under high load if the frequently accessed parts of the graph does not fit in the cache.
weak	Use weak reference cache.
strong	Use strong references.
none	Don’t use caching.

表 21.5. Cypher parser version
cypher_parser_version 
Enable this to specify a parser other than the default one. 
Value	Description
1.5	Cypher v1.5 syntax.
1.6	Cypher v1.6 syntax.
1.7	Cypher v1.7 syntax.

表 21.6. Dump configuration
Default value: false 
dump_configuration
Print out the effective Neo4j configuration after startup.

表 21.7. Forced kernel id
Default value: 
forced_kernel_id
An identifier that uniquely identifies this graph database instance within this JVM. Defaults to an auto-generated number depending on how many instance are started in this JVM.

表 21.8. Gc monitor threshold
Default value: 200ms 
gc_monitor_threshold
The amount of time in ms the monitor thread has to be blocked before logging a message it was blocked.

表 21.9. Gc monitor wait time
Default value: 100ms 
gc_monitor_wait_time
Amount of time in ms the GC monitor thread will wait before taking another measurement.

表 21.10. Gcr cache min log interval
Default value: 60s 
gcr_cache_min_log_interval
The minimal time that must pass in between logging statistics from the cache (when using the 'gcr' cache).

表 21.11. Grab file lock
Default value: true 
grab_file_lock
Whether to grab locks on files or not.

表 21.12. Intercept committing transactions
Default value: false 
intercept_committing_transactions
Determines whether any TransactionInterceptors loaded will intercept prepared transactions before they reach the logical log.

表 21.13. Intercept deserialized transactions
Default value: false 
intercept_deserialized_transactions
Determines whether any TransactionInterceptors loaded will intercept externally received transactions (e.g. in HA) before they reach the logical log and are applied to the store.

表 21.14. Keep logical logs
Default value: true 
keep_logical_logs
Make Neo4j keep the logical transaction logs for being able to backup the database.Can be used for specifying the threshold to prune logical logs after. For example "10 days" will prune logical logs that only contains transactions older than 10 days from the current time, or "100k txs" will keep the 100k latest transactions and prune any older transactions.

表 21.15. Logging.threshold for rotation
logging.threshold_for_rotation
Threshold in bytes for when database logs (text logs, for debugging, that is) are rotated. 
Limit	Value
Default value: 104857600 
min	1

表 21.16. Logical log
Default value: nioneo_logical.log 
logical_log
The base name for the logical log files, either an absolute path or relative to the store_dir setting. This should generally not be changed.

表 21.17. Lucene searcher cache size
lucene_searcher_cache_size
Integer value that sets the maximum number of open lucene index searchers. 
Limit	Value
Default value: 2147483647 
min	1

表 21.18. Neo store
Default value: neostore 
neo_store
The base name for the Neo4j Store files, either an absolute path or relative to the store_dir setting. This should generally not be changed.

表 21.19. Neostore.nodestore.db.mapped memory
Default value: 20M 
neostore.nodestore.db.mapped_memory
The size to allocate for memory mapping the node store.

表 21.20. Neostore.propertystore.db.arrays.mapped memory
Default value: 130M 
neostore.propertystore.db.arrays.mapped_memory
The size to allocate for memory mapping the array property store.

表 21.21. Neostore.propertystore.db.index.keys.mapped memory
Default value: 1M 
neostore.propertystore.db.index.keys.mapped_memory
The size to allocate for memory mapping the store for property key strings.

表 21.22. Neostore.propertystore.db.index.mapped memory
Default value: 1M 
neostore.propertystore.db.index.mapped_memory
The size to allocate for memory mapping the store for property key indexes.

表 21.23. Neostore.propertystore.db.mapped memory
Default value: 90M 
neostore.propertystore.db.mapped_memory
The size to allocate for memory mapping the property value store.

表 21.24. Neostore.propertystore.db.strings.mapped memory
Default value: 130M 
neostore.propertystore.db.strings.mapped_memory
The size to allocate for memory mapping the string property store.

表 21.25. Neostore.relationshipstore.db.mapped memory
Default value: 100M 
neostore.relationshipstore.db.mapped_memory
The size to allocate for memory mapping the relationship store.

表 21.26. Node auto indexing
Default value: false 
node_auto_indexing
Controls the auto indexing feature for nodes. Setting to false shuts it down unconditionally, while true enables it for every property, subject to restrictions in the configuration.

表 21.27. Node cache array fraction
node_cache_array_fraction
The fraction of the heap (1%-10%) to use for the base array in the node cache (when using the 'gcr' cache). 
Limit	Value
Default value: 1.0 
min	1.0
max	10.0

表 21.28. Node cache size
node_cache_size 
The amount of memory to use for the node cache (when using the 'gcr' cache).

表 21.29. Node keys indexable
node_keys_indexable 
A list of property names (comma separated) that will be indexed by default. This applies to Nodes only.

表 21.30. Read only database
Default value: false 
read_only
Only allow read operations from this Neo4j instance.

表 21.31. Rebuild idgenerators fast
Default value: true 
rebuild_idgenerators_fast
Use a quick approach for rebuilding the ID generators. This give quicker recovery time, but will limit the ability to reuse the space of deleted entities.

表 21.32. Relationship auto indexing
Default value: false 
relationship_auto_indexing
Controls the auto indexing feature for relationships. Setting to false shuts it down unconditionally, while true enables it for every property, subject to restrictions in the configuration.

表 21.33. Relationship cache array fraction
relationship_cache_array_fraction
The fraction of the heap (1%-10%) to use for the base array in the relationship cache (when using the 'gcr' cache). 
Limit	Value
Default value: 1.0 
min	1.0
max	10.0

表 21.34. Relationship cache size
relationship_cache_size 
The amount of memory to use for the relationship cache (when using the 'gcr' cache).

表 21.35. Relationship keys indexable
relationship_keys_indexable 
A list of property names (comma separated) that will be indexed by default. This applies to Relationships only.

表 21.36. Remote logging enabled
Default value: false 
remote_logging_enabled
Whether to enable logging to a remote server or not.

表 21.37. Remote logging host
Default value: 127.0.0.1 
remote_logging_host
Host for remote logging using LogBack SocketAppender.

表 21.38. Remote logging port
remote_logging_port
Port for remote logging using LogBack SocketAppender. 
Limit	Value
Default value: 4560 
min	1
max	65535

表 21.39. Store dir
store_dir 
The directory where the database files are located.

表 21.40. String block size
string_block_size
Specifies the block size for storing strings. This parameter is only honored when the store is created, otherwise it is ignored. Note that each character in a string occupies two bytes, meaning that a block size of 120 (the default size) will hold a 60 character long string before overflowing into a second block. Also note that each block carries an overhead of 8 bytes. This means that if the block size is 120, the size of the stored records will be 128 bytes. 
Limit	Value
Default value: 120 
min	1

表 21.41. Tx manager impl
tx_manager_impl 
The name of the Transaction Manager service to use as defined in the TM service provider constructor, defaults to native.

表 21.42. Use memory mapped buffers
use_memory_mapped_buffers 
Tell Neo4j to use memory mapped buffers for accessing the native storage layer.

JVM配置
JVM有两个主要内存参数，一个控制堆空间，另一个控制堆栈空间。堆空间的参数是最重要的一个自治Neo4j的，您可以分配多少对象。栈空间参数控制多深你的应用程序的调用堆栈可以获得。
当涉及到堆空间时，通常的规则是：拥有更大的堆空间，但要确保堆适合于计算机的RAM内存。如果堆分页到磁盘的性能会迅速下降。有一个比应用程序需要的要大得多的堆也不好，因为这意味着JVM在垃圾收集器执行之前会积累很多死对象，这会导致长时间的垃圾收集暂停和不希望的性能行为。
有一个较大的堆空间将意味着更大的交易，Neo4j可以处理并发事务。一个大的堆空间也将运行得更快，因为它意味着Neo4j Neo4j适合其缓存图的较大部分，即节点和关系，你的应用程序经常使用总是可以很快。一个32位JVM默认堆的大小是64mb（和30%大为64位），这是太小，最真实的应用程序。
Neo4j适合默认的堆栈空间的配置，但是如果你的应用实现了递归的行为是增加堆栈的大小是个不错的主意。注意，堆栈大小是为所有线程共享的，因此如果应用程序运行很多并发线程，那么增加堆栈大小是个好主意。
•堆大小设置指定- Xmx？？？m参数到热点，在哪里？？？堆大小是兆字节吗？。默认堆的大小是64mb 32位JVM，30%大（约。83mb）为64位JVM。
•堆栈大小设置指定的XSS？？？m参数到热点，在哪里？？？堆栈大小是兆字节吗？。默认的堆栈大小为32位JVM在Solaris 512KB，32位JVM在Linux 320kb（Windows），和64位JVM 1024kb。
大多数现代CPU执行一个非统一内存访问（NUMA）体系结构，在不同的地区有不同的内存访问速度。太阳的HotSpot JVM能够分配对象与NUMA结构意识作为版本1.6.0更新18。当启用此可以放弃40%的性能改进。启用NUMA感知，指定XX：+ usenuma参数（仅当使用并行清除垃圾收集器（默认或XX：+ useparallelgc不并发标记和扫描一个）。
正确配置JVM内存利用率是至关重要的优化性能。例如，配置不良的JVM可能花费所有CPU时间执行垃圾收集（阻塞所有线程以执行任何工作）。需要考虑延迟、总吞吐量和可用硬件等要求，以找到正确的设置。在生产中，Neo4j应该运行在多核CPU平台的JVM在服务器模式。
配置堆大小和GC:
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


安全访问Neo4j服务器
1. 加强端口和远程客户端连接请求的安全
默认情况下，Neo4j服务端会捆绑一个Web服务器，服务在 7474 端口，通过地址： http://localhost:7474/ 访问，不过只能从本地访问。
在配置文件 conf/neo4j-server.properties 中：
1
2
3
4
5
6
7	# http port (for all data, administrative, and UI access) 
org.neo4j.server.webserver.port=7474
  
#let the webserver only listen on the specified IP. Default 
#is localhost (only accept local connections). Uncomment to allow 
#any connection. 
#org.neo4j.server.webserver.address=0.0.0.0
如果你需要能从外部主机访问，请在 conf/neo4j-server.properties 中设置 org.neo4j.server.webserver.address=0.0.0.0 来启用。
2. 任意的代码执行
默认情况下，Neo4j服务端是可以执行任意代码的。比如 Gremlin Plugin 和 Traversals REST endpoints。为了让这些更安全一些，要么从服务端的类路径完全移除这些插件，要么通过代理或者授权的角色来访问哲学地址。当然， Java Security Manager 也可以用于让代码更加安全。
3. HTTPS支持
Neo4j服务端内建支持HTTPS进行SSL加密通讯。服务端第一次启动时，他会自动生成一个自签名SSL证书和一个私钥。因为这个证书是自签名的，在生产环境使用是不安全的，相反，你应该生成为生产服务器单独产生。
为了提供你自己的KEY和证书，取代生成的KEY和证书，或者通过改变 neo4j-server.properties 的配置来充值你的证书和KEY的位置：
1
2
3
4
5	# Certificate location (auto generated if the file does not exist) 
org.neo4j.server.webserver.https.cert.location=ssl/snakeoil.cert 
  
# Private key location (auto generated if the file does not exist) 
org.neo4j.server.webserver.https.key.location=ssl/snakeoil.key
注意这个KEY应该是不加密的。确保你给私钥设置正确的权限，以至于只有neo4j服务端才有权限读取/写入它。
你可以设置https连接端口和配置文件中的一样，以及关闭/打开 https支持：
1
2
3
4
5	# Turn https-support on/off 
org.neo4j.server.webserver.https.enabled=true
  
# https port (for all data, administrative, and UI access) 
org.neo4j.server.webserver.https.port=443


监视服务器
	许多监视器特性只在Neo4j服务器高级版和企业版才可以使用。
为了能获取Neo4j数据库的健康状况，可以采用不同级别的监控等级。这些功能一般都是通过 JMX 呈现出来。
1.调整远程JMX访问Neo4j的服务器
默认情况下，Neo4j高级版和企业版都不允许远程的JMX连接，因为在 conf/neo4j-wrapper.conf 配置文件中的相关配置是被注释掉了的。为了启用该功能， 你必须去掉配置 com.sun.management.jmxremote 前面的 # 。
当被注释掉时，默认值允许以某种角色远程JMX连接的，查看 conf/jmx.password , conf/jmx.access 和 conf/wrapper.conf 文件了解细节。
确保 conf/jmx.password 文件有正确的文件权限。文件的所有者必须是运行服务的用户，而且只能对该用户只读。在Unix系统中，权限码应该是： 0600 。
在Windows中，按照 http://docs.oracle.com/javase/6/docs/technotes/guides/management/security-windows.html 设置正确的权限。如果你以本地系统账户允许服务，文件的所有者和访问者必须是SYSTEM。
使用这个配置，使用 <IP-OF-SERVER>:3637 ，用户名： monitor ，密码： Neo4j ，你应该能连接到Neo4j服务端的JMX监控上面。
特别注意你可能必须升级 conf/jmx.password 和 conf/jmx.access 文件的权限或者拥有者，查看 conf/wrapper.conf 文件了解细节。
为了最大的安全性，请至少调整在文件 conf/jmx.password 中的密码设置。
要获取更多细节，请查看：http://download.oracle.com/javase/6/docs/technotes/guides/management/agent.html
2.如何使用JMX和JConsole连接到一个Neo4j实例
1	$NEO4j_HOME/bin/neo4j start
现在启动JConsole：
1	$JAVA_HOME/bin/jconsole
连接到你的Neo4j数据库实例的进程：
图 25.1. 连接JConsole到Neo4j的Java进程
 

现在，与通过JVM暴露的MBeans无关，你将在MBeans选项卡处看到一个 org.neo4j 部分。使用它，你可以访问所有Neo4j的监控信息。
为了打开JMX的远程监控访问，确保传递com.sun.management.jmxremote.port=portNum 或者其他配置作为JVM参数给你的允许的JAVA进程。







