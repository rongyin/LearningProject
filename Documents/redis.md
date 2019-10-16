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
- Redis哨兵的高可用
1. 哨兵机制建立了多个哨兵节点(进程)，共同监控数据节点的运行状况。
2. 同时哨兵节点之间也互相通信，交换对主从节点的监控状况。
3. 每隔1秒每个哨兵会向整个集群：Master主服务器+Slave从服务器+其他Sentinel（哨兵）进程，发送一次ping命令做一次心跳检测。
- 主观下线和客观下线。
1. 主观下线：一个哨兵节点判定主节点down掉是主观下线。
2. 客观下线：只有半数哨兵节点都主观判定主节点down掉，此时多个哨兵节点交换主观判定结果，才会判定主节点客观下线。
3. 原理：基本上哪个哨兵节点最先判断出这个主节点客观下线，就会在各个哨兵节点中发起投票机制Raft算法（选举算法），最终被投为领导者的哨兵节点完成主从自动化切换的过程。



# redis 主从复制的核心原理
1. 当启动一个 slave node 的时候，它会发送一个 PSYNC 命令给 master node。

2. 如果这是 slave node 初次连接到 master node，那么会触发一次 full resynchronization 全量复制。
此时 master 会启动一个后台线程，开始生成一份 RDB 快照文件，同时还会将从客户端 client 新收到的所有写命令缓存在内存中。
RDB 文件生成完毕后， master 会将这个 RDB 发送给 slave，slave 会先写入本地磁盘，然后再从本地磁盘加载到内存中，接着 master 会将内存中缓存的写命令发送到 slave，slave 也会同步这些数据。
slave node 如果跟 master node 有网络故障，断开了连接，会自动重连，连接之后 master node 仅会复制给 slave 部分缺少的数据。

3. 主从复制的断点续传



# 留言协议（Gossip）


# 一致性hash算法


# 为什么Redis是单线程的
因为Redis是基于内存的操作，CPU不是Redis的瓶颈，Redis的瓶颈最有可能是机器内存的大小或者网络带宽。既然单线程容易实现，而且CPU不会成为瓶颈，那就顺理成章地采用单线程的方案了。

# Redis的高并发和快速原因
1. redis是基于内存的，内存的读写速度非常快；

2. redis是单线程的，省去了很多上下文切换线程的时间；

3. redis使用多路复用技术，可以处理并发的连接。非阻塞IO 内部实现采用epoll，采用了epoll+自己实现的简单的事件框架。epoll中的读、写、关闭、连接都转化成了事件，然后利用epoll的多路复用特性，绝不在io上浪费一点时间。

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
过期 key 处理
slave 不会过期 key，只会等待 master 过期 key。如果 master 过期了一个 key，或者通过 LRU 淘汰了一个 key，那么会模拟一条 del 命令发送给 slave。

# 如何解决Redis缓存雪崩、缓存穿透、缓存并发等5大难题
## 缓存雪崩
redis大量失效后，大量请求转向到mysql数据库
办法：缓存的高可用性，缓存降级，Redis备份和快速预热
## 缓存穿透
缓存穿透是指查询一个一不存在的数据
办法：如果查询数据库也为空，直接设置一个默认值存放到缓存
## 缓存并发
这里的并发指的是多个redis的client同时set
key引起的并发问题。其实redis自身就是单线程操作，多个client并发操作，按照先到先执行的原则，先到的先执行，其余的阻塞。当然，另外的解决方案是把redis.set操作放在队列中使其串行化，必须的一个一个执行。


# redis事务
 它先以 MULTI 开始一个事务， 然后将多个命令入队到事务中， 最后由 EXEC 命令触发事务
 
# redis 集群
1. redis是一个开源的key value存储系统，受到了广大互联网公司的青睐。redis3.0版本之前只支持单例模式，在3.0版本及以后才支持集群，我这里用的是redis3.0.0版本；
2. redis集群采用P2P模式，是完全去中心化的，不存在中心节点或者代理节点；
3. redis集群是没有统一的入口的，客户端（client）连接集群的时候连接集群中的任意节点（node）即可，集群内部的节点是相互通信的（PING-PONG机制），每个节点都是一个redis实例；
4. 为了实现集群的高可用，即判断节点是否健康（能否正常使用），redis-cluster有这么一个投票容错机制：如果集群中超过半数的节点投票认为某个节点挂了，那么这个节点就挂了（fail）。这是判断节点是否挂了的方法；
5. 那么如何判断集群是否挂了呢? -> 如果集群中任意一个节点挂了，而且该节点没有从节点（备份节点），那么这个集群就挂了。这是判断集群是否挂了的方法；
6. 那么为什么任意一个节点挂了（没有从节点）这个集群就挂了呢？ -> 因为集群内置了16384个slot（哈希槽），并且把所有的物理节点映射到了这16384[0-16383]个slot上，或者说把这些slot均等的分配给了各个节点。当需要在Redis集群存放一个数据（key-value）时，redis会先对这个key进行crc16算法，然后得到一个结果。再把这个结果对16384进行求余，这个余数会对应[0-16383]其中一个槽，进而决定key-value存储到哪个节点中。所以一旦某个节点挂了，该节点对应的slot就无法使用，那么就会导致集群无法正常工作。
综上所述，每个Redis集群理论上最多可以有16384个节点。 路由信息存放到第三方存储组件，如 zookeeper 或etcd，codis是目前用的最多的集群方案，codis一个比较大的优点是可以不停机动态新增或删除数据节点，旧节点的数据也可以自动恢复到新节点。
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

# redis和数据库双写一致性
MySQL处理实时性数据，例如金融数据、交易数据
Redis处理实时性要求不高的数据，例如网站最热贴排行榜，好友列表等
https://blog.csdn.net/zhuguang10/article/details/96136579

# redis高并发
- redis高并发：主从架构，一主多从，一般来说，很多项目其实就足够了，单主用来写入数据，单机几万QPS，多从用来查询数据，多个从实例可以提供每秒10万的QPS。
- redis高并发的同时，还需要容纳大量的数据：一主多从，每个实例都容纳了完整的数据，比如redis主就10G的内存量，其实你就最对只能容纳10g的数据量。如果你的缓存要容纳的数据量很大，达到了几十g，甚至几百g，或者是几t，那你就需要redis集群，而且用redis集群之后，可以提供可能每秒几十万的读写并发。
- redis高可用：如果你做主从架构部署，其实就是加上哨兵就可以了，就可以实现，任何一个实例宕机，自动会进行主备切换。

# quorum和majority
每次一个哨兵做主备切换，首先需要quorum数量的哨兵认为down，然后选举出一个哨兵来做主备切换，这个哨兵还要得到majority数量哨兵的授权，才能正式执行切换。

如果quorum < majority ,比如5个哨兵，majority就是3(超过半数)，quorum设置为2，那么就需要3个哨兵授权就可以执行切换。

如果 quorum >= majority，那么必须quorum数量的哨兵都授权才可以进行切换，比如5个哨兵，quorum是5，那么必须5个哨兵都同意授权，才可以进行切换。


# 内存分析
https://help.aliyun.com/knowledge_detail/50037.html?spm=5176.13394938.0.0.6e6d74e4dZjApe

# 缓存和数据库一致性解决方案
1. 采用延时双删策略
2. 异步更新缓存

# Redis高并发快总结
1. Redis是纯内存数据库，一般都是简单的存取操作，线程占用的时间很多，时间的花费主要集中在IO上，所以读取速度快。
2. 再说一下IO，Redis使用的是非阻塞IO，IO多路复用，使用了单线程来轮询描述符，将数据库的开、关、读、写都转换成了事件，减少了线程切换时上下文的切换和竞争。
3. Redis采用了单线程的模型，保证了每个操作的原子性，也减少了线程的上下文切换和竞争。
4. 另外，数据结构也帮了不少忙，Redis全程使用hash结构，读取速度快，还有一些特殊的数据结构，对数据存储进行了优化，如压缩表，对短数据进行压缩存储，再如，跳表，使用有序的数据结构加快读取的速度。
5. 还有一点，Redis采用自己实现的事件分离器，效率比较高，内部采用非阻塞的执行方式，吞吐能力比较大。

# 应用场景
- 页面缓存
- 技术/排行榜
- 共享session
- 消息队列
- 发布/订阅


# Jedis
如果应用非常平凡的创建和销毁Jedis对象,对应用的性能是很大影响的,因为构建Socket的通道是很耗时的(类似数据库连接)。我们应该使用连接池来减少Socket对象的创建和销毁过程。
1. 配置JedisPoolConfig，注入jedisPool
2. jedisPool.getResource
3. Pipeline set gerct都是二进制
4. google.protobuf
protobuf序列化后的大小是json格式的十分之一，xml格式的二十分之一。如果使用protobuf实现，首先要写一个proto文件

高可用连接 
我们知道,连接池可以大大提高应用访问Reids服务的性能,减去大量的Socket的创建和销毁过程。但是Redis为了保障高可用,服务一般都是Sentinel部署方式(可以查看我的文章详细了解)。当Redis服务中的主服务挂掉之后,会仲裁出另外一台Slaves服务充当Master。这个时候,我们的应用即使使用了Jedis连接池,Master服务挂了,我们的应用奖还是无法连接新的Master服务。为了解决这个问题,Jedis也提供了相应的Sentinel实现,能够在Redis Sentinel主从切换时候,通知我们的应用,把我们的应用连接到新的 Master服务。先看下怎么使用。
Jedis Sentinel的使用也是十分简单的,只是在JedisPool中添加了Sentinel和MasterName参数。Jedis Sentinel底层基于Redis订阅实现Redis主从服务的切换通知。当Reids发生主从切换时,Sentinel会发送通知主动通知Jedis进行连接的切换。JedisSentinelPool在每次从连接池中获取链接对象的时候,都要对连接对象进行检测,如果此链接和Sentinel的Master服务连接参数不一致,则会关闭此连接,重新获取新的Jedis连接对象。

