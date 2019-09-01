# 数据类型
- String 里面有一个length，一个free，一个char[] buf
命令：get，set
- hash（比如一个对象（前提是这个对象没嵌套其他的对象））
命令：hmset,hget
- List 先进后出(stack) （搞个简单的消息队列,可以基于 list 实现分页查询）
命令：lpush，lrange，rpop
- Set String的无序集合，通过hash表实现，不允许重复 （可以基于 set 玩儿交集、并集、差集的操作，比如交集吧，可以把两个人的粉丝列表整一个交集，看看俩人的共同好友是谁）
命令：sadd，smembers
```
# 求两set的交集
sinter yourSet mySet

# 求两set的并集
sunion yourSet mySet

# 求在yourSet中而不在mySet中的元素
sdiff yourSet mySet

```
- Sorted Set
写进去的时候给一个分数，自动根据分数排序
命令：zadd myzet 分数（1） abc ， zrangebyscore myzet 0 10
- HyperLoglog
- Geo

# keys 命令有可能阻塞 ，scan是基于游标的迭代器
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
4. RDB-AOF混合模式（Redis 4.0）
这种持久化能够通过 AOF 重写操作创建出一个同时包含 RDB 数据和 AOF 数据的 AOF 文件， 其中 RDB 数据位于 AOF 文件的开头， 它们储存了服务器开始执行重写操作时的数据库状态： 至于那些在重写操作执行之后执行的 Redis 命令， 则会继续以 AOF 格式追加到 AOF 文件的末尾， 也即是 RDB 数据之后。

# pipeline
可以批量执行命令，多次IO往返降低到一次

# redis同步机制
- master-slave架构
* 一主多从，主负责写，并且将数据复制到其它的 slave 节点，从节点负责读。所有的读请求全部走从节点。
* redis replication -> 主从架构 -> 读写分离 -> 水平扩容支撑读高并发
* slave node 在做复制的时候，也不会 block 对自己的查询操作，它会用旧的数据集来提供服务；但是复制完成的时候，需要删除旧数据集，加载新数据集，这个时候就会暂停对外服务了；

- 哨兵机制（ sentinel）
* 监控：检查主服务器是否运行正常
* 提醒：通过API向管理员或者其他应用程序发送通知
* 自动故障迁移：主从切换
* 哨兵 + redis 主从的部署架构，是不保证数据零丢失的，只能保证 redis 集群的高可用性。
- 异步复制导致的数据丢失
1. 因为 master->slave 的复制是异步的，所以可能有部分数据还没复制到 slave，master 就宕机了，此时这部分数据就丢失了。
2. 脑裂，也就是说，某个 master 所在机器突然脱离了正常的网络，跟其他 slave 机器不能连接，但是实际上 master 还运行着。此时哨兵可能就会认为 master 宕机了，然后开启选举，将其他 slave 切换成了 master。这个时候，集群里就会有两个 master ，也就是所谓的脑裂。
https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-sentinel.md


# redis 主从复制的核心原理
1. 当启动一个 slave node 的时候，它会发送一个 PSYNC 命令给 master node。

2. 如果这是 slave node 初次连接到 master node，那么会触发一次 full resynchronization 全量复制。此时 master 会启动一个后台线程，开始生成一份 RDB 快照文件，同时还会将从客户端 client 新收到的所有写命令缓存在内存中。RDB 文件生成完毕后， master 会将这个 RDB 发送给 slave，slave 会先写入本地磁盘，然后再从本地磁盘加载到内存中，接着 master 会将内存中缓存的写命令发送到 slave，slave 也会同步这些数据。slave node 如果跟 master node 有网络故障，断开了连接，会自动重连，连接之后 master node 仅会复制给 slave 部分缺少的数据。

3. 主从复制的断点续传

# 过期 key 处理
slave 不会过期 key，只会等待 master 过期 key。如果 master 过期了一个 key，或者通过 LRU 淘汰了一个 key，那么会模拟一条 del 命令发送给 slave。


# 留言协议（Gossip）


# 一致性hash算法


# 为什么Redis是单线程的
因为Redis是基于内存的操作，CPU不是Redis的瓶颈，Redis的瓶颈最有可能是机器内存的大小或者网络带宽。既然单线程容易实现，而且CPU不会成为瓶颈，那就顺理成章地采用单线程的方案了。

# Redis的高并发和快速原因
1.redis是基于内存的，内存的读写速度非常快；

2.redis是单线程的，省去了很多上下文切换线程的时间；

3.redis使用多路复用技术，可以处理并发的连接。非阻塞IO 内部实现采用epoll，采用了epoll+自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间。

4. c语言实现
# Redis相比memcached有哪些优势？
1. memcached所有的值均是简单的字符串，Redis作为其替代者，支持更为丰富的数据类型

2. Redis的速度比memcached快很多

3. Redis可以持久化其数据

4. Redis支持数据的备份，即master-slave模式的数据备份。

# Redis有哪几种数据淘汰策略？
在Redis中，允许用户设置最大使用内存大小server.maxmemory，当Redis 内存数据集大小上升到一定大小的时候，就会施行数据淘汰策略。

1.volatile-lru:从已设置过期的数据集中挑选最近最少使用的淘汰
```
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int CACHE_SIZE;

    /**
     * 传递进来最多能缓存多少数据
     *
     * @param cacheSize 缓存大小
     */
    public LRUCache(int cacheSize) {
        // true 表示让 linkedHashMap 按照访问顺序来进行排序，最近访问的放在头部，最老访问的放在尾部。
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        CACHE_SIZE = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当 map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据。
        return size() > CACHE_SIZE;
    }
}

```
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

# redis事务
 它先以 MULTI 开始一个事务， 然后将多个命令入队到事务中， 最后由 EXEC 命令触发事务
 
# redis 集群
1. redis是一个开源的key value存储系统，受到了广大互联网公司的青睐。redis3.0版本之前只支持单例模式，在3.0版本及以后才支持集群，我这里用的是redis3.0.0版本；
2. redis集群采用P2P模式，是完全去中心化的，不存在中心节点或者代理节点；
3. redis集群是没有统一的入口的，客户端（client）连接集群的时候连接集群中的任意节点（node）即可，集群内部的节点是相互通信的（PING-PONG机制），每个节点都是一个redis实例；
4. 为了实现集群的高可用，即判断节点是否健康（能否正常使用），redis-cluster有这么一个投票容错机制：如果集群中超过半数的节点投票认为某个节点挂了，那么这个节点就挂了（fail）。这是判断节点是否挂了的方法；
5. 那么如何判断集群是否挂了呢? -> 如果集群中任意一个节点挂了，而且该节点没有从节点（备份节点），那么这个集群就挂了。这是判断集群是否挂了的方法；
6. 那么为什么任意一个节点挂了（没有从节点）这个集群就挂了呢？ -> 因为集群内置了16384个slot（哈希槽），并且把所有的物理节点映射到了这16384[0-16383]个slot上，或者说把这些slot均等的分配给了各个节点。当需要在Redis集群存放一个数据（key-value）时，redis会先对这个key进行crc16算法，然后得到一个结果。再把这个结果对16384进行求余，这个余数会对应[0-16383]其中一个槽，进而决定key-value存储到哪个节点中。所以一旦某个节点挂了，该节点对应的slot就无法使用，那么就会导致集群无法正常工作。
综上所述，每个Redis集群理论上最多可以有16384个节点。
7. 登陆哪台机器set，未必就存在这台机器上
8. 主从比例可以自定6：3，6：12，6：6

# redis 和 memcached 有啥区别？
1. redis 支持复杂的数据结构
2. redis 原生支持集群模式
3. 性能对比
由于 redis 只使用单核，而 memcached 可以使用多核，所以平均每一个核上 redis 在存储小数据时比 memcached 性能更高。而在 100k 以上的数据中，memcached 性能要高于 redis。虽然 redis 最近也在存储大数据的性能上进行优化，但是比起 memcached，还是稍有逊色。

# redis 的线程模型
文件事件处理器的结构包含 4 个部分：
1. 多个 socket
2. IO 多路复用程序
3. 文件事件分派器
4. 事件处理器（连接应答处理器、命令请求处理器、命令回复处理器）
多个 socket 可能会并发产生不同的操作，每个操作对应不同的文件事件，但是 IO 多路复用程序会监听多个 socket，
会将产生事件的 socket 放入队列中排队，事件分派器每次从队列中取出一个 socket，根据 socket 的事件类型交给对应的事件处理器进行处理。

