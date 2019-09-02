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
## 其实用更简单的话来说，就是如果选用的注册中心是eureka，那么就推荐@EnableEurekaClient，如果是其他的注册中心，那么推荐使用@EnableDiscoveryClient。

## Eureka
1. 保护机制：某一时刻微服务不可用，eureka不会立刻清除，依旧会对该服务的信息就行保留
2. Irule:根据特定算法从服务列表中选取一个要访问的服务（RoundRobenRule，RandomRule。。。）
## Euraka和ZOOkeeper比较
1. Euraka保证AP，ZooKeeper保证CP（partition tolerant）
https://blog.csdn.net/qq_35902689/article/details/78113317

## Ribbon,Feign，Nginx
1. Ribbon是客户端负载均衡 
Ribbon工作时分为两步,第一步先选择Eureka Server，它优先选择在同一个Zone且负载较少的Server。

第二部再根据用户指定的策略，再从Server取到的服务注册列表中选择一个地址。其中Ribbon提供了多种策略，例如轮询round robin，随机Random，根据相应时间加权等。

2. @LoadBalanced
3. Feign是声明式的Web服务客户端
4. Feign 是在 Ribbon的基础上进行了一次改进，是一个使用起来更加方便的 HTTP 客户端。调用方式不同，Ribbon需要自己构建http请求，模拟http请求然后使用RestTemplate发送给其他服务，步骤相当繁琐。
5. zuul 是netflix开源的一个API Gateway 服务器, 本质上是一个web servlet应用。
https://www.liangzl.com/get-article-detail-20957.html

# 服务的调度
Rest，RPC， gRPC

# 服务器的熔断器
Hystix, Sentinal
https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D
- 如何熔断：断路器（打开，半打开，关闭，threadhold）
- 断路器功能：异常处理，日志记录，测试失败操作，手动复位，并发，重试失败请求
- 熔断和降级：目的一致，最终表现类似，粒度一般都是服务级别，触发条件不同
 * 熔断的目的是当A服务模块中的某块程序出现故障后为了不影响其他客户端的请求而做出的及时回应。
 * 降级的目的是为了解决整体项目的压力，而牺牲掉某一服务模块而采取的措施。
1. 目的很一致，都是从可用性可靠性着想，为防止系统的整体缓慢甚至崩溃，采用的技术手段；
2. 最终表现类似，对于两者来说，最终让用户体验到的是某些功能暂时不可达或不可用；
3. 粒度一般都是服务级别，当然，业界也有不少更细粒度的做法，比如做到数据持久层（允许查询，不允许增删改）；
1. 触发原因不太一样，服务熔断一般是某个服务（下游服务）故障引起，而服务降级一般是从整体负荷考虑；
2. 管理目标的层次不太一样，熔断其实是一个框架级的处理，每个微服务都需要（无层级之分），而降级一般需要对业务有层级之分（比如降级一般是从最外围服务开始）



# 服务接口调用
Feign

# 消息队列
Kafka，RabbitMQ，ActiveMQ

# 服务配置中心管理
- SpringCLoudConfig
1. server config
 * 集中管理配置文件
 * 不同环境，不同配置
 * 运行期间，动态调整
 * 当配置发生变化，服务不需要重启就能感知到配置的改变
 * 将配置以rest接口形式暴露
 * 以git整合
- 配置分类
 * 按集成阶段：编译时，打包时，运行时
 * 加载方式：启动加载，动态加载
2. config server: 集中式的配置服务器，管理应用程序各个环境下的配置，默认使用是git
3. config client：用于操作存储在server中的配置属性，各个微服务都会在启动时，请求server以获得所需配置属性，然后混存这些属性以提高性能
4. profile 命名规则


# 全链路追踪
Zipkin, Brave, Dapper

# 服务部署
Docker, OpenStack, Kubernates

# 数据流操作开发包
SpringCLoud stream
- SpringCLoud 是微服务全家桶
- springboot是关注于快速，方便的开放单体的微服务个体，cloud是关注全局的微服务治理框架，cloud离不开boot

# 事件消息总线
Spring CLoud Bus 

# Zuul
代理+路由+过滤


Schaeffler is actively shaping this digital transformation

# 运行机制
1. 请求统一通过API网关（Zuul）来访问内部服务.

2. 网关接收到请求后，从注册中心（Eureka）获取可用服务

3. 由Ribbon进行均衡负载后，分发到后端具体实例

4. 微服务之间通过Feign进行通信处理业务

5. Hystrix负责处理服务超时熔断

6. Turbine监控服务间的调用和熔断相关指标


1. 请求统一通过API网关（Zuul）来访问内部服务.

2. 网关接收到请求后，从注册中心（Eureka）获取可用服务

3. 由Ribbon进行均衡负载后，分发到后端具体实例

4. 微服务之间通过Feign进行通信处理业务

5. Hystrix负责处理服务超时熔断

6. Turbine监控服务间的调用和熔断相关指标

