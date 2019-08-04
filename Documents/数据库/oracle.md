# 基础
1. DMl 都有锁
select insert update delete merge
2. DDL
create drop truncate alter
3. TCL (transaction control)
commit rollback savepoint

使用savepoint，你可以在长事务中任何点任意标记你的操作。然后你可以选择回滚在事务中当前点之前、声明的savepoint之后执行的操作。比如，你可以在一长段复杂的更新中使用savepoint，如果犯了个错，你不需要重新提交所有语句。 
SQL> UPDATE SCOTT.DEPT  SET loc ='b' WHERE loc='DALLAS';
1 row updated
SQL> SAVEPOINT b;
Savepoint created
SQL> ROLLBACK TO SAVEPOINT a;
Rollback complete
SQL> COMMIT;


4. DCL (data control)
grant revoke

# 数据类型
1. char 固定长度，max 2000
varchar2 可变长度 1-4000
2. date timestamp
* to_char函数支持date和timestamp，但是trunc却不支持TIMESTAMP数据类型
* 计算间隔不一样
select to_date('2012-7-28 03:12:00','yyyy-mm-dd hh24:mi:ss')-sysdate from dual
结果是：92.2472685185185天，然后你根据相应的时间换算你想要的间隔就行！这个结果可能对程序员有用，对于想直接看到结果的人，这个数字还不是很直观，所以，就引出了timestamp类型
timestamp是DATE类型的扩展，可以精确到小数秒（fractional_seconds_precision），可以是0 to9，缺省是６。两个timestamp相减的话，不能直接的得到天数书，而是得到，多少天，多少小时，多少秒等
select to_timestamp('2012-7-28 03:12:00','yyyy-mm-dd hh24:mi:ss')-systimestamp from dual
结果是：+000000092 05:51:24.032000000，稍加截取，就可以得到92天5小时，51分钟，24秒，这样用户看起来比较直观一些！但是这个数字对程序员来说不是很直观了，如果想要具体的时间长度的话，并且精度不要求到毫秒的话，可以将timestamp类型转成date类型，然后直接相减即可。
  
当你进行两个日期的相减运算的时候，得到的是天数。
```
SELECT TO_CHAR(date1, 'MMDDYYYY:HH24:MI:SS') date1,
       TO_CHAR(date2, 'MMDDYYYY:HH24:MI:SS') date2,
       trunc(86400 * (date2 - date1)) -
       60 * (trunc((86400 * (date2 - date1)) / 60)) seconds,
       trunc((86400 * (date2 - date1)) / 60) -
       60 * (trunc(((86400 * (date2 - date1)) / 60) / 60)) minutes,
       trunc(((86400 * (date2 - date1)) / 60) / 60) -
       24 * (trunc((((86400 * (date2 - date1)) / 60) / 60) / 24)) hours,
       trunc((((86400 * (date2 - date1)) / 60) / 60) / 24) days,
       trunc(((((86400 * (date2 - date1)) / 60) / 60) / 24) / 7) weeks
  FROM date_table

DATE1 DATE2 SECONDS MINUTES HOURS DAYS WEEKS 
----------------- ----------------- ---------- ---------- ---------- ---------- ---------- 
06202003:16:55:14 07082003:11:22:57 43 27 18 17 2 
06262003:11:16:36 07082003:11:22:57 21 6 0 12 1
```
这就意味着不再需要关心一天有多少秒在麻烦的计算中。因此，得到天数、月数、天数、时数、分钟数和秒数就成为用substr函数摘取出数字的事情了
```
SELECT time1,
       time2,
       substr((time2 - time1), instr((time2 - time1), ' ') + 7, 2) seconds,
       substr((time2 - time1), instr((time2 - time1), ' ') + 4, 2) minutes,
       substr((time2 - time1), instr((time2 - time1), ' ') + 1, 2) hours,
       trunc(to_number(substr((time2 - time1), 1, instr(time2 - time1, ' ')))) days,
       trunc(to_number(substr((time2 - time1), 1, instr(time2 - time1, ' '))) / 7) weeks
  FROM date_table
  
TIME1 TIME2 SECONDS MINUTES HOURS DAYS WEEKS 
------------------------- -------------------------- ------- ------- ----- ---- ----- 
06/20/2003:16:55:14:000000 07/08/2003:11:22:57:000000 43 27 18 17 2 
06/26/2003:11:16:36:000000 07/08/2003:11:22:57:000000 21 06 00 12 1
```

date和timestamp之间的相互转换可以通过
```
select to_timestamp(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') from dual
select to_date(to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') from dual

```
TIMESTAMP类型支持时区,支持小数秒

3. clob 单字节数据 字符大型对象
文章或者是较长的文字
在绝大多数情况下，使用2种方法使用CLOB
1 相对比较小的，可以用String进行直接操作，把CLOB看成字符串类型即可
2 如果比较大，可以用 getAsciiStream 或者 getUnicodeStream 以及对应的 setAsciiStream 和 setUnicodeStream 即可
4. blob 二进制数据
图片、文件、音乐等信息就用BLOB字段来存储
?? 查一下咋么存储

# 转化类型
to_date('','YYYY-MM-DD')
to_char(sysdate,'YYY-MM-DD')
to_timestamp('2019-10-21','YYYY-MM-DD HH24:MI::SS FF')
to_number(replace(to_char(sysdate,'yyyy-mm-dd'),'-'))
decode(条件,值1,返回值1,值2,返回值2,…值n,返回值n,缺省值)
decode(dep,10,'department 1')
```
1.翻译

decode(sex,1,"man",2,"woman","others") as sex
2.decode比较大小
说明：sign(value)函数会根据value的值为0，正数，负数，分别返回0，1,-1
       decode(sign(t.age - 20),
              1,
              '20以上',
              -1,
              '20以下',
              0,
              '正好20',
              '未知') as sex
搜索字符串:
decode(instr(t.name, '三'), 0, '姓名不含有三', '姓名含有三') as name,              

3.decode分段
       decode(sign(sal - 5000),
              1,
              '高薪',
              0,
              '高薪',
              -1,
              decode(sign(sal - 3000), 1, '中等', 0, '中等', -1, '低薪')) as salname

4 判断是否为空
decode(t.sex,NULL,'暂无数据',t.sex) as sex

```
case
```
case when dep=10 then "department 1"
case when dep=20 then "department 2"
else "department 3" end
```

# dual 表
Oracle系统中dual表是一个“神秘”的表，网上有很多网友都对该表进行了测试，该表只有一行一列，其实该表和系统中的其他表一样，一样可以执行插入、更新、删除操作，还可以执行drop操作。但是不要去执行drop表的操作，否则会使系统不能用，数据库起不了，会报Database startup crashes with ORA-1092错误。此时也不要慌乱，可以通过执行以下步骤来进行恢复。可以用sys用户登陆。

1. select * from dual;　   mysql会出错——1096：没有使用到表；而oracle 会返回 dummy = X
```
　　SQL> desc dual　Name Null? Type

　　----------------------------------------- -------- ----------------------------

　　DUMMY VARCHAR2(1)


　　SQL> select dummy from dual;

　　DUMMY

　　----------

　　X


   SQL> delete from dual 
   SQL> select * from dual;

　　DUMMY

　　----------

　　Y

```
2. select  express　[from dual];  mysql总是作为返回该表达式值的普通select语句执行，返回一行记录的结果集，from dual 对mysql来说根本就是摆设！而oracle里该句必须有from dual；否则报错！
3. select  express　from dual where 0=2;   mysq 和 oracle的行为一致：该句就如同你认为的正常表那样——会先计算where的条件，再行计算express；这里的where条件会决定expres是否会返回！

4. dual是一个虚拟表，用来构成select的语法规则，oracle保证dual里面永远只有一条记录。我们可以用它来做很多事情，如下：
    * 查看当前用户，可以在 SQL Plus中执行下面语句 select user from dual;
    * 用来调用系统函数
```
     select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual;--获得当前系统时间
     select to_char(sysdate,'Q') from dual;--获得当前季度 https://blog.csdn.net/delphi308/article/details/25654455
　　  select SYS_CONTEXT('USERENV','TERMINAL') from dual;--获得主机名
　　  select SYS_CONTEXT('USERENV','language') from dual;--获得当前 locale
　　  select dbms_random.random from dual;--获得一个随机数
```
    * 得到序列的下一个值或当前值，用下面语句
       select your_sequence.nextval from dual;--获得序列your_sequence的下一个值
　　    select your_sequence.currval from dual;--获得序列your_sequence的当前值
    * 可以用做计算器 select 7*9 from dual;
    
5. Oracle中，当需要建立一个自增字段时，需要用到sequence,在oracle中sequence就是序号，每次取的时候它会自动增加。sequence与表没有关系。
```
CREATE SEQUENCE seqTest

INCREMENT BY 1 -- 每次加几个
START WITH 1 -- 从1开始计数
NOMAXvalue -- 不设置最大值
NOCYCLE -- 一直累加，不循环
CACHE 10; --设置缓存cache个序列，如果系统down掉了或者其它情况将会导致序列不连续，也可以设置为---------NOCACHE

create sequence SEQ_ID

minvalue 1

maxvalue 99999999

start with 1

increment by 1

nocache

order;

```
定义好sequence后，你就可以用currVal，nextVal取得值。

    CurrVal：返回 sequence的当前值
    CURRVAL 总是返回当前SEQUENCE的值，但是在第一次NEXTVAL初始化之后才能使用CURRVAL，否则会出错。

    NextVal：增加sequence的值，然后返回 增加后sequence值
    

得到值语句如下：

SELECT Sequence名称.CurrVal FROM DUAL;

如在插入语句中

insert into 表名(id,name)values(seqtest.Nextval,'sequence 插入测试');


# 函数
trunc
add_months
month_between
last_day
replace
substr
concat
abs
round
max,min,avg,count,sum  group by having
merge into tableA a using (select f1,f2 from tableb) b on a.f1=b.f1 when matched then update... when not matched then insert... 
start with 递归 查出这个人的所有上级关系人物pid = id，id=pid向下查询
select * from table where xx=xx start with xx=1234 connect by prior pid = id



# 链接

1. 内链接 （内连接查询只能查询出匹配的记录）
2. 自然连接（natural join）
自然连接是在广义笛卡尔积R×S中选出同名属性上符合相等条件元组，再进行投影，去掉重复的同名属性，组成新的关系。即自然连接是在两张表中寻找那些数据类型和列名都相同的字段，然后自动地将他们连接起来，并返回所有符合条件按的结果
自然链接和内链接区别是 相同属性是否显示1个还是2个
3. 外链接（左，右，全），
返回到查询结果集合中的不仅包含符合连接条件的行，而且还包括左表(左外连接或左连接))、右表(右外连接或右连接)或两个边接表(全外连接)中的所有数据行。
4. 交叉连接（cross join）
除了cross join不可以加on外，其它join连接都必须加上on关键字，后都可加where条件。
交叉连接不带ON子句，它返回被连接的两个表所有数据行的笛卡尔积，返回到 结果集合中的数据行数等于第一个表中符合查询条件的数据行数乘以第二个表中符合查 询条件的数据行数。 
```
SELECT  S.*, T.* FROM STUDENT S CROSS JOIN TEACHER  T ;
等价于：
SELECT  S.*, T.* FROM STUDENT S , TEACHER  T ;

```
5. 自链接
连接的表是同一张表，使用自连接可以将自身表的一个镜像当作另一个表来对待，从而能够得到一些特殊的数据。
```
table route(num, company, pos, stop）
SELECT * FROM route R1, route R2 WHERE R1.stop=R2.stop;
可查询公用同一公交车站的所有路线

```
# 聚集
union 重复记录只有一次
union all 所有
intersect 交集
minus 集合相减，那个表在前是主数据

# 子查询
1. 关联性子查询：在字段上
2. 非关联性子查询 ：主查询和子查询是独立的，在where语句中

# in和exists
in是全表扫描
select * from emp A where exists (select * from emp B where A.xx=B.yy)

# rownum
select * from emp where rownum<5没有问题 >5就没有东西返回
>必须要用别名的形式查询 
select * from (select E.* ,rownum as RN from emp E) where RN>5

# select create ,insert into select
create table new_table as select * from table
insert into table (f1,f2) select f1,f2 from table2


# 事务的特性
ACID
Atomicity : 要么都执行，要么都不执行
Consisitency : 一个查询的结果必须和查询前一致
Isolation : 事务与事务之间隔离 ， 没有commit之前都不可见 ，可以通过V&transaction表里看到其他用户事务
Durability : 一旦提交成功，不可逆 ，oracle主要靠redu日志，先记录在日志上，再写到硬盘上

# 锁
- 数据库的锁也是共享锁（读锁） 允许其他共享锁排他锁不行，或者排他锁（写锁）
DML锁：数据锁，TX（行及锁），TM（表及锁）
DDL锁：数据字典锁
system lock

- TM锁分5种
Row share 行共享（RS）
Row exclusive 行排他（RX）同时允许其他用户更新其他行，其他用户可以加行共享锁或者行排他锁
Share 共享（S）不允许其他用户更新任何行，只允许其他用户加共享锁
Share Row exclusive 共享行排他（SRX）不允许更新，可以其他用户加行共享锁
Exclusive 排他 （X）禁止更新，禁止加锁
DML语句自动加锁
select ... for update nowait/wait 5 由oracle自动加锁
oracle自己解决了死锁问题

# 索引
虚拟索引：在不必消耗CPU，IO及大量空间去实际创建索引情况下，来判断一个索引是否能对sql优化
B+树索引：分为头块，分支块，叶子块
- 由于叶子节点处于相同深度，所以其性能是可预测的
- 头块和分支块都是在内存中的，所以实际磁盘访问通常只有1到2次
- B树支持范围查找和精确查找
- 数据变更时候，维护代价大
bitmap索引：
索引的目的是减少IO

1. 唯一索引：值不重复 create unique index
2. 一般索引： create index
3. 组合索引：多列组合 create index idx_xxx on table (f1,f2)
4. 反向索引：避免平衡树热块
5. 函数索引：查询时必须用到这个函数 create index ... on (lower(name))
6. 压缩索引
7. 升序降序索引

优化：分析索引表，看索引碎片
alter index idx_name rebuild online

# 解析执行计划
Explain plan for
缩紧越多路径先执行
同样缩紧步骤，先执行最上面那条

# 第三范式
实体表所有数据完全依赖于主键
不能有重复的列

列的顺序：访问表靠后的列比前面的需要消耗额外的CPU资源
设计数据仓库：星型模型

# 查看索引碎片
Oracle通过Segment Advisor ,如果你想自行解决，可以查看index_stats表，height>=4,Pct_used<50%,del_lf_row/lf_row>0.2 

# 数据库表设计
1. 业务要会切分
2. 逻辑分层 基础信息加业务
3. 数据库表结构设计与拆分 oracle分区 500万条个区，物化视图，中间表
4. 数据规则
5. 预留字段
6. 做一些合理的冗余

# 物化view 是虚表，但是会物理存储
create **materialized** view 
on commit 一旦基表有更新就刷新, on demand 频繁插入时候
build immediate ,deferred 需要时候再生成
refresh fast有一条就刷，complete全表刷新，force，never
next 下次刷新间隔
start with

# synonym 

# OLTP OLAP
