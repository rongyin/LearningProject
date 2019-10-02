# MQ优缺点
- 解耦,异步,减少高峰时期对服务器压力
- 系统可用性降低,万一 MQ 挂了，MQ 一挂，整套系统崩溃
- 一致性问题

# 
- 持久化消息比较:ActiveMq 和RabbitMq 都支持。持久化消息主要是指我们机器在不可抗力因素等情况下挂掉了，消息不会丢失的机制。
- 综合技术实现: RabbitMq / Kafka 最好
- 高并发:毋庸置疑，RabbitMQ 最高，原因是它的实现语言是天生具备高并发高可用的erlang 语言。
- 另外，Kafka 的定位主要在日志等方面， 因为Kafka 设计的初衷就是处理日志的，可以看做是一个日志（消息）系统一个重要组件，针对性很强，所以 如果业务方面还是建议选择 RabbitMq 。
还有就是，Kafka 的性能（吞吐量、TPS ）比RabbitMq 要高出来很多。

# ejb
- 消息驱动Bean(MDB)是设计用来专门处理基于消息请求的组件，它和无状态Session Bean一样也使用了实例池技术，容器可以使用一定数量的bean实例并发处理成百上千个JMS消息。
正因为MDB具有处理大量并发消息的能力，所以非常适合应用在一些消息网关产品。如果一个业务执行的时间很长，而执行结果无需实时向用户反馈时，也很适合使用MDB。
如订单成功后给用户发送一封电子邮件或发送一条短信等。
- 消息有下面几种类型，他们都是派生自Message接口。
* StreamMessage：一种主体中包含Java基本值流的消息。其填充和读取均按顺序进行。
* MapMessage：一种主体中包含一组名-值对的消息。(没有定义条目顺序)
* TextMessage：一种主体中包含Java字符串的消息(例如：XML消息)
* ObjectMessage：一种主体中包含序列化Java对象的消息。
* BytesMessage：一种主体中包含连续字节流的消息。

# 消息的传递模型：
JMS支持两种消息传递模型：点对点(point-to-point，简称PTP)和发布/订阅(publish/subscribe，简称pub/sub)。
二者有以下区别：
1. PTP 消息传递模型规定了一条消息只能传递给一个接收方。采用javax.jms.Queue表示。
2. Pub/sub 消息传递模型允许一条消息传递给多个接收方。采用javax.jms.Topic表示。
注意：每种模型都通过扩展公用基类来实现。例如，javax.jms.Queue 和javax.jms.Topic都扩展自javax.jms.Destination 类。

# active mq
1. PTP默认是持久化的先进先出的队列，可以设置超时时间（一般没人设置），默认是kahadb,可以改成jdbc
2. Pub/sub默认是非持久化

# 消息中间件如何解决消息丢失问题
消息发送超时，处于不确定状态，导致重试发送消息，有可能之前的消息已经发送成功，会出现消息重复的情况。解决的思路是，每个消息生成一个消息id，如果发送的消息Broker已经存在了，则丢弃。这种解决办法需要维护一个已经接收的消息的message id list。

# 消息生产Producer、Broker(消息服务端)、消息消费者Consumer。
1. Producer(消息生产者)：发送消息到Broker。

2. Broker(服务端)：Broker这个概念主要来自于Apache的ActiveMQ，特指消息队列的服务端。
主要功能就是：把消息从发送端传送到接收端，这里会涉及到消息的存储、消息通讯机制等。

3. Consumer(消息消费者)：从消息队列接收消息，consumer回复消费确认。

# JMS和AMQP比较

JMS: 只允许基于JAVA实现的消息平台的之间进行通信

AMQP: AMQP允许多种技术同时进行协议通信


# 消息的持久化

# 消息队列的高可用性
RabbitMQ的镜像集群模式的高可用性方案，ActiveMQ也有基于LevelDB+ZooKeeper的高可用性方案，以及Kafka的Replication机制等。


# P2P and Topic
生产者发送一条消息到队列queue，只有一个消费者能收到,发布者发送到topic的消息，只有订阅了topic的订阅者才会收到消息。
多个发布者将消息发送到Topic,系统将这些消息传递给多个订阅者。


# 消息的顺序性保证
基于Queue消息模型，利用FIFO先进先出的特性，可以保证消息的顺序性。

# 消息的ACK机制
为了保证消息不丢失，消息队列提供了消息Acknowledge机制，即ACK机制，当Consumer确认消息已经被消费处理，发送一个ACK给消息队列，此时消息队列便可以删除这个消
息了。如果Consumer宕机/关闭，没有发送ACK，消息队列将认为这个消息没有被处理，会将这个消息重新发送给其他的Consumer重新消费处理。

# 消息生产者、消息者、队列
- 消息生产者Producer：发送消息到消息队列。
- 消息消费者Consumer：从消息队列接收消息。
- Broker：概念来自与Apache ActiveMQ，指MQ的服务端，帮你把消息从发送端传送到接收端。
- 消息队列Queue：一个先进先出的消息存储区域。消息按照顺序发送接收，一旦消息被消费处理，该消息将从队列中删除。


