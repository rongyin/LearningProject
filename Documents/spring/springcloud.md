# Microservice 
- 单体应用->分布式系统
- 根据业务拆分服务，彻底去耦合
- 轻量级的通信机制Restful，Dubbo是RPC
- 可以使用不同语言和数据库
- 每一个服务都是一个进程,都可以独立部署

# 优缺点
- 微服务是业务逻辑代码，不会和html，css或其他界面组建混合
- 微服务能被小团队开发
- 通讯成本
- 运营成本大
- 分布式复杂性
- 系统集成测试
- 性能监控

# 服务配置与管理
Netflix的Archaius,阿里的Diamond

# 服务的注册和管理
Eureka, Consul, Zookeeper

# 服务的调度
Rest，RPC， gRPC

# 服务器的熔断器
Hystix, Envoy

# 负载均衡
Ribbon， Nginx

# 服务接口调用
Feign

# 消息队列
Kafka，RabbitMQ，ActiveMQ

# 服务配置中心管理
SpringCLoudConfig，Chef

# 全链路追踪
Zipkin, Brave, Dapper

# 服务部署
Docker, OpenStack, Kubernates

# 数据流操作开发包
SpringCLoud stream

# 事件消息总线
Spring CLoud Bus 