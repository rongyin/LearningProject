# 数据类型
- String 里面有一个length，一个free，一个char[] buf
命令：get，set
- hash
命令：hmset,hget
- List 先进后出(stack)
命令：lpush，lrange
- Set String的无序集合，通过hash表实现，不允许重复
命令：sadd，smembers
- Sorted Set
命令：zadd myzet 分数（1） abc ， zrangebyscore myzet 0 10
- HyperLoglog
- Geo
- keys 命令有可能阻塞 ，scan是基于游标的迭代器
```
scan 0 match r* count 10
```

# redis实现分布式锁
1. setnx ket value 
espire key seconds
缺点：原子性不行
2. set key value [ex seconds]

# 如何用redis做异步队列
用List，rpush生产队列，lpop消费队列 
blpop可以阻塞控制
- pub/sub
* 订阅者可以订阅任意数量的频道
* 无状态的，无法保证可达

# 如何持久化
1. RDB:保存某个时间点的数据快照
- save
- bgsave：fork出一个子进程来创建RDB文件，不会阻塞服务器进程
- save 600 1在配置文件里
2. 缺点
- 内存数据全量同步，数据大时候由于IO会严重影响性能
- 可能丢失redis当机到最近快照的数据
3. AOF（append-only-file）
- 记录除了查询所有变更数据库状态的命令
- 以增量方式追加到AOF文件中 
4. RDB-AOF混合模式

# pipeline
可以批量执行命令，多次IO往返降低到一次

# redis同步机制
- 哨兵机制（sentinel）
* 监控：检查主服务器是否运行正常
* 提醒：通过API向管理员或者其他应用程序发送通知
* 自动故障迁移：主从切换

# 留言协议（Gossip）


# 一致性hash算法


# 为什么Redis是单线程的
因为Redis是基于内存的操作，CPU不是Redis的瓶颈，Redis的瓶颈最有可能是机器内存的大小或者网络带宽。既然单线程容易实现，而且CPU不会成为瓶颈，那就顺理成章地采用单线程的方案了。

# Redis的高并发和快速原因
1.redis是基于内存的，内存的读写速度非常快；

2.redis是单线程的，省去了很多上下文切换线程的时间；

3.redis使用多路复用技术，可以处理并发的连接。非阻塞IO 内部实现采用epoll，采用了epoll+自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间。

# Redis相比memcached有哪些优势？
1. memcached所有的值均是简单的字符串，Redis作为其替代者，支持更为丰富的数据类型

2. Redis的速度比memcached快很多

3. Redis可以持久化其数据

4. Redis支持数据的备份，即master-slave模式的数据备份。

# Redis有哪几种数据淘汰策略？
在Redis中，允许用户设置最大使用内存大小server.maxmemory，当Redis 内存数据集大小上升到一定大小的时候，就会施行数据淘汰策略。

1.volatile-lru:从已设置过期的数据集中挑选最近最少使用的淘汰

2.volatile-ttr:从已设置过期的数据集中挑选将要过期的数据淘汰

3.volatile-random:从已设置过期的数据集中任意挑选数据淘汰

4.allkeys-lru:从数据集中挑选最近最少使用的数据淘汰

5.allkeys-random:从数据集中任意挑选数据淘汰

6.noenviction:禁止淘汰数据

redis淘汰数据时还会同步到aof

# Redis过期策略
https://www.cnblogs.com/xuliangxing/p/7151812.html

# 如何解决Redis缓存雪崩、缓存穿透、缓存并发等5大难题

https://youzhixueyuan.com/redis-cache-avalanche-solution.html

