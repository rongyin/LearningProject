jvm内存，css选择器，java函数表达式和js的函数表达式的原理和区别，mybatis中join的时候遇到相同名字的字段怎么处理，sql调优思路，分布式事务，对spring的理解
还有数据库index的作用和影响，b+树和红黑树的比较

数据库调优，查询慢日志，sql语句分析，index
怎么解决线上问题，怎么做性能调优

synchronize，CAS, Atomic类  AQS, 线程池，各种线程安全的集合
还有实现的原理，通过对象头的mark word

# serialVersionUID
serialVersionUID适用于Java的序列化机制。简单来说，Java的序列化机制是通过判断类的serialVersionUID来验证版本一致性的。在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常，即是InvalidCastException。
具体的序列化过程是这样的：序列化操作的时候系统会把当前类的serialVersionUID写入到序列化文件中，当反序列化时系统会去检测文件中的serialVersionUID，判断它是否与当前类的serialVersionUID一致，如果一致就说明序列化类的版本与当前类版本是一样的，可以反序列化成功，否则失败。
serialVersionUID有两种显示的生成方式：        
一是默认的1L，比如：private static final long serialVersionUID = 1L;        
二是根据类名、接口名、成员方法及属性等来生成一个64位的哈希字段，比如：        
private static final  long   serialVersionUID = xxxxL;
A -> B , 如果A新增字段，B就无法读出，如果B增加字段，那就默认值
```
FileOutPutStream -> ObjectOutPutStream -> writeObject
FileInputStream -> ObjectInputStream -> readObject

```


