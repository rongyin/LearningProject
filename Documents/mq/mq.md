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