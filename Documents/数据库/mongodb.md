# mongoDB的特性
1. 面向集合存储。数据被分组到若干集合，每个集合可以包含无限个文档，可以将集合想象成RDBMS的表，区别是集合不需要进行模式定义。
2. 模式自由。集合中没有行和列的概念，每个文档可以有不同的key，key的值不要求一致的数据类型。
3. 支持动态查询。mongoDB支持丰富的查询表达式，查询指令使用json形式表达式。
4. 完整的索引支持。mongoDB的查询优化器会分析查询表达式，并生成一个高效的查询计划。
5. 高效的数据存储，支持二进制数据及大型对象（图片、视频等）。
6. 支持复制和故障恢复。
7. 自动分片以支持云级别的伸缩性，支持水平的数据库集群，可动态添加额外的服务器。


# docker
- docker run -p 27017:27017 -v $PWD/db:/data/db -d mongo
- docker run -it mongo mongo --host 172.17.0.1

# 基本命令
1. show dbs
2. use
3. db.dropDatabase()
4. db.createCollection(name,option)
5. show collections
6. db.集合name.drop()
7. db.集合name.insert()
8. db.集合name.find({查询条件},{字段名称:1,...}) _id默认是0 这个是叫投影

- lt,lte,gt,gte,ne
- find({age:{lte:18}})
- and就是json里写多个，or使用$or[{$or[{age:18},{name:""}]}]
- regex :  name：/^abc/
- skip,limit
- $where 返回自定义方法
```
find({
   $where:function(){
   return this.age>30;
   }
})
```
- sort({字段1：1，字段2 ：-1}) 1是升序，-1是降序
- count() 里面也可以加条件
- distinct("字段"，{条件})
- .explain('excutionStats')返回查询时间 , explain(true)返回执行计划

9. db.集合name.update(query,update,{multi:true})
- db.集合name.update({name:"xiaowang"},{age:12},{multi:true})
- db.集合name.update({name:"xiaowang"},{$set{age:14 }},{multi:true})

10. db.集合name.update(query,justOne:true)

11. aggregate 
```
aggregate({
    $group:{_id:"$name"，count：{$sum:1},average:{avg:"age"}}
})
```


# 数据类型
1. String UTF-8
2. Integer,Double
3. Arrays
4. Object 嵌入式文档
5. Null
6. TimeStamp，从1970-1-1到现在的总秒数
7. Date 当前时间

# 建立索引(联合)
```
db.collections.ensureIndexes({属性：1}) ，1升序，-1降序
db.collections.dropIndexes({属性：1})
db.collections.getIndexes()
```
- MongoDB在A:{B,C}上建立索引，查询A:{B,C}和A:{C,B}都会使用索引吗？
不会，只会在A:{B,C}上使用索引。
- 无法在索引建立之后再去增加索引的过期时间
- _id索引无法删除

# MongoOperations and MongoTemplate
The preferred way to reference the operations on MongoTemplate instance is through its interface, MongoOperations.
A major difference between the two APIs is that MongoOperations can be passed domain objects instead of Document. 
Also, MongoOperations has fluent APIs for Query, Criteria, and Update operations instead of populating a Document 
to specify the parameters for those operations.

# How the _id Field is Handled in the Mapping Layer
A property or field annotated with @Id (org.springframework.data.annotation.Id) maps to the _id field.


# insert and save
- insert: Inserts an object. If there is an existing document with the same id, an error is generated.
- save: Saves the object, overwriting any object that might have the same id.

# upsert,findAndModify
- upsert will perform an insert if no document is found that matches the query
- findAndModify can update a document and return either the old or newly updated document in a single operation

# Optimistic Locking
The @Version annotation provides syntax similar to that of JPA in the context of MongoDB and makes sure updates are only applied to documents with a matching version. 
Therefore, the actual value of the version property is added to the update query in such a way that the update does not have any effect if another operation altered the document in the meantime. 
In that case, an OptimisticLockingFailureException is thrown

# query
- Query (Criteria criteria)
All find methods take a Query object as a parameter
接受的参数是org.springframework.data.mongodb.core.query.Criteria
- Criteria是标准查询的接口，可以引用静态的Criteria.where的把多个条件组合在一起，就可以轻松地将多个方法标准和查询连接起来，方便我们操作查询语句。

The criteria are specified by using a Criteria object that has a static factory method
```
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

…

List<Person> result = mongoTemplate.find(query(where("age").lt(50)
  .and("accounts.balance").gt(1000.00d)), Person.class);

```


# MongoDB支持存储过程吗？如果支持的话，怎么用？
MongoDB支持存储过程，它是javascript写的，保存在db.system.js表中。

# 如何理解MongoDB中的GridFS机制，MongoDB为何使用GridFS来存储文件？
GridFS是一种将大型文件存储在MongoDB中的文件规范。使用GridFS可以将大文件分隔成多个小文档存放，这样我们能够有效的保存大文档，而且解决了BSON对象有限制的问题。

# 分析器在MongoDB中的作用是什么
- 0表示没有打开
1表示打开了，并且如果查询的执行时间超过了第二个参数毫秒（ms）为单位的最大查询执行时间，就会被记录下来，否则忽略。
2表示打开了，并且记录一切查询语句
db.setProfilingLevel(2)
- 此时发现在test数据库下多了一个system.profile集合：不过此时这个集合还是空的。
- 然后再查询集合：db.system.profile.find().pretty()，我们可以看到刚才查询的执行计划。
* "nReturned" : 1返回行数1
*  "millis" : 0 执行时间
* "stage" : "COLLSCAN" 未使用索引
* "docsExamined" : 8 一共扫描了8个文档



# journal
- Mongodb在1.8版本之后开始支持journal，就是我们常说的redo log，用于故障恢复和持久化。 
-  journal除了故障恢复的作用之外，还可以提高写入的性能，批量提交（batch-commit），journal一般默认100ms刷新一次，在这个过程中，所有的写入都可以一次提交，是单事务的，全部成功或者全部失败。


# bson
- MongoDB使用了BSON这种结构来存储数据和网络数据交换。把这种格式转化成一文档这个概念(Document)，因为BSON是schema-free的，所以在MongoDB中所对应的文档也有这个特征，这里的一个Document也可以理解成关系数据库中的一条记录(Record)，只是这里的Document的变化更丰富一些，如Document可以嵌套。
- bson size不能超过16MB的限制

# 复制集（Replica Set）
https://www.cnblogs.com/zyfd/p/9810858.html

# 分片
https://docs.mongodb.com/manual/core/sharded-cluster-components/
http://www.lanceyan.com/tech/arch/mongodb_shard1.html
