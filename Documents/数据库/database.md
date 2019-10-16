# procedure
Create or replace procedure XXX (参数1，参数2) as 声明变量
Begin
查询
Exception
异常处理
End;


```
Procedure name is
    cursor c_name is
        select * from table;
        
Begin
    for oneline in c_name loop
        **begin**
        update..
        commit;
        
        exception
            when others then
            rollback;
            commit;
         **end;**
       end loop;
END;
```
       
        
# 三大范式
1. 第一范式的目标是确保每列的原子性，如果每列都是不可再分的最小数据单元（也称为最小的原子单元），则满足第一范式（1NF）
2. 第二范式的要求在满足第一范式的基础上，每个表只描述一件事情（2NF）
3. 第三范式要求表中各列必须和主键依赖相关，不能间接依赖（3NF）
        
# Truncate和Delete的区别是
delete逐条删除，truncate整个表截断即删除表中所有记录。
最根本的区别是：delete是DML（数据操纵语言,可以回滚）truncate是DDL（数据定义语言，不可以回滚）
Delete需要事务删除大量数据的时候速度慢，Truncate不需要事务，删除大量数据快
Delete不会释放空间，truncate会
delete会产生碎片，truncate不会

# Rowid和Rownum的区别？
ROWID（记录编号）：是用来唯一标识表中的一条记录，并且间接给出了表行的物理位置，定位表行最快的方式，是唯一的，使用insert语句插入数据时，oracle会自动生成rowid并将其值与表数据一起存放到表行中。
ROWNUM（行号）：是在查询操作时由ORACLE为每一行记录自动生成的一个编号。rownum不是表中原本的数据，每一次查询ROWNUM都会重新生成。（查询的结果中Oracle给你增加的一个编号，根据结果来重新生成）。
ROWNUM永远按照默认的顺序生成。（不受order by的影响）
rowid 用于定位数据表中某条数据的位置，是唯一的、也不会改变。(面试背)
rownum 表示查询某条记录在整个结果集中的位置， 同一条记录查询条件不同对应的 rownum 是不同的而 rowid 是不会变的。(面试背)
扩展：
1、主键命名要么id要么表名_id;
2、主键的值要么是UUID（32位全球唯一码），要么自增型的数字；
3、java中UUID.randomUUID()生成uuid是36位的，需要去掉-，成为32位。
UUID.randomUUID().toString().replaceAll("-", “”)
4、oracle中select sys_guid() from dual;

# Oracle中分页如何实现？Mysql中分页如何实现？
Oracle 中使用 rownum 来进行分页, 这个是效率最好的分页方法。从1开始
Mysql使用limit的关键字可以实现分页。从0开始
select * from TABLENAME limit start , end;


# 如何使用Oracle的游标
1. Oracle 中的游标分为显示游标和隐式游标
2. 显示游标是用 cursor…is 命令定义的游标，它可以对查询语句(select)返回的多条记录进行处理；
3. 隐式游标是在执行插入 (insert)、删除(delete)、修改(update) 和返回单条记录的查询(select)语句时由 PL/SQL 自动定义的。
4. 显式游标的操作：定义游标、打开游标、操作游标、关闭游标；隐式游标的操作PL/SQL 隐式地打开 SQL 游标，并在它内部处理 SQL语句，然后关闭它。

# 触发器的定义，语法，作用
定义：
触发器是指被隐含执行的存储过程，通常用的针对于Insert、Delete、Update之类的DML触发器。在进行特定的增删改操作时触发触发器。
语句级触发器和行级触发器区别：
1. 在语法上：
行级触发器就多了一句话：for each row
2. 在表现上
行级触发器，在每一行的数据进行操作的时候都会触发。
语句级触发器，对表的一个完整操作才会触发一次。
简单的说：行级触发器，是对应行操作的；语句级触发器，是对应表操作的。

语法：
create [or replace] trigger 触发器名 触发时间 触发事件
on 表名
[for each row]
begin
pl/sql语句
End

作用：
1. 数据确认；
2. 实施复杂的安全性检查；
3. 做审计，跟踪表上所做的数据操作等；
4. 数据的备份和同步。
（备份：在删除时，触发触发器。创建一个新的表将要删除的数据先插入这个新表中，然后再删除数据）

触发器注意事项：
1. 触发器会引起锁，降低效率！使用时要慎重。如无必要，尽量不要使用触发器。
2. 行级触发器会引发行级锁（锁行数据）
3. 语句级触发器可能会引起表级锁（锁表）


# 表约束
NOT NULL 指定字段不能包含空值
UNIQUE 指定字段的值（或字段组合的值）表中所有的行必须唯一
PRIMARY KEY 表的每行的唯一标识，即主键
FOREIGN KEY 在字段和引用表的一个字段之间建立并且强制外键关系，即外键
CHECK 指定一个必须为真的条件

主键与唯一约束的区别:

1.主键只能有一个，而唯一约束可以有多个；
2.主键可以由一列或多列充当，但唯一约束只能一列一列创建；
3.主键不允许为空，而唯一约束在Oracle中可以多次为空，在SQL中唯一约束只能一次为空；
