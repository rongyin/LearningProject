# Microservice 
微服务架构是一种架构模式，它提倡将单一应用程序划分成一组小的服务，服务之间互相协调、互相配合，为用户提供最终价值。每个服务运行在其独立的进程中，服务与服务间采用轻量级的通信机制互相沟通（通常是基于HTTP的RESTful
API）。每个服务都围绕着具体业务进行构建，并且能够被独立地部署到生产环境、类生产环境等。另外，应尽量避免统一的、集中式的服务管理机制，对具体的一个服务而言，应根据业务上下文，选择合适的语言、工具对其进行构建。

Martin Fowler提出来这一概念可以说把SOA的理念继续升华，精进了一步。 微服务架构强调的第一个重点就是业务系统需要彻底的组件化和服务化
从部署方式上，这个是最大的不同，对比以往的Java
EE部署架构，通过展现层打包WARs，业务层划分到JARs最后部署为EAR一个大包，而微服务则把应用拆分成为一个一个的单个服务，应用Docker技术，不依赖任何服务器和数据模型，是一个
全栈应用，可以通过自动化方式独立部署，每个服务运行在自己的进程。
https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485613&idx=1&sn=29af9be51654779b54f73315b3ba2c64&chksm=cea24766f9d5ce7011233f3a0634b608e41cc6bc5a0a0afe836ad57efe08f7a4bb9c5495fc85&token=287192206&lang=zh_CN&scene=21#wechat_redirect

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

# 演变
1. 单体架构，简单（成本，开发周期），所有功能在一起
2. 开发人员多，项目大，就不适合单体架构，一个bug影响巨大
3. SOA就像横切，微服务就像纵切
4. package -> project reference 多project方式 但是不知道关系-> maven 3.0开始，parent module
5. 业务一开始都在一个project里，代码影响了业务的效率
6. 从架构的角度
从数据库端oracle数据库集中式架构的瓶颈问题，连接池数量限制(oracle数据库大约提供5000个连接)，数据库的CPU已经到达了极限90%。数据库端也需要考虑垂直拆分了。

# 服务配置与管理
Netflix的Archaius,阿里的Diamond

# 服务的注册和管理

## why
在传统的服务架构中，服务的规模处于运维人员的可控范围内。当部署服务的多个节点时，一般使用静态配置的方式实现服务信息的设定。
而在微服务应用中，服务实例的数量和网络地址都是动态变化的，这对系统运维提出了巨大的挑战。
而且服务集群的跨度很大、数量很多（数以百计甚至更多），为保障系统的正常运行，必然需要有一个中心化的组件完成对各个服务的整合，
即将分散于各处的服务进行汇总，汇总的信息可以是服务器的名称、地址、数量等，并且这些服务器组件还拥有被监听功能等(服务发现)。

1. 服务注册
服务实例将自身服务信息注册到注册中心。这部分服务信息包括服务所在主机IP和提供服务的Port，以及暴露服务自身状态以及访问协议等信息。
2. 服务发现
服务实例请求注册中心获取所依赖服务信息。服务实例通过注册中心，获取到注册到其中的服务实例的信息，通过这些信息去请求它们提供的服务。
除此之外，服务注册与发现需要关注监控服务实例运行状态等问题。
3. 监控
微服务应用中，服务处于动态变化的情况，需要一定机制处理无效的服务实例。一般来讲，服务实例与注册中心在注册后通过心跳的方式维系联系，一旦心跳缺少，对应的服务实例会被注册中心剔除。



Eureka, Consul, Zookeeper
## 其实用更简单的话来说，就是如果选用的注册中心是eureka，那么就推荐@EnableEurekaClient，如果是其他的注册中心，那么推荐使用@EnableDiscoveryClient。

## Eureka
1. 保护机制：某一时刻微服务不可用，eureka不会立刻清除，依旧会对该服务的信息就行保留
2. 当客户端注册Eureka时，它提供关于自身的元数据，例如主机和端口，健康指示符URL，主页等。
Eureka从属于服务的每个实例接收心跳消息。如果心跳失败超过可配置的时间表，则通常将该实例从注册表中删除。
3. 服务注册表：用来记录微服务信息
4. 服务的注册与发现，通过心跳检查，客户端缓存等机制，提高系统的灵活，可伸缩和可用性
5. 服务状态：UP，Unknown(一般是因为健康检查导致的，配置在bootstrap.yml中有时会有问题)，Down
6. Eureka Server提供了服务发现的能力，各个微服务启动时， ，server会存储这些信息
7. 微服务启动会周期性（默认30秒）向server发送心跳以续约自己的租期
8. server 在一定时间没有接受到某个微服务实例的心跳，将其注销（90秒默认），多个server实例之间通过复制方式来同步数据
9. client会缓存注册表的信息，微服务无需每次请求查询server，降低server压力，即使server所有节点都down了，也可以使用缓存信息找到服务提供者完成调用
10. 非jvm微服务可以通过Rest（Representational State Transfer）端点操作Eureka，从而实现注册与发现
11. 自我保护机制：server在短时间内丢失客户端时，这个节点就进入自我保护模式，一旦进入这个模式，server就会保护注册表中信息，不再删除服务注册表中数据，当网络故障恢复后，会退出自我保护模式
12. 配置
```
@EnableEurekaServer
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/

eureka.client.register-with-eureka=false,由于我们目前创建的应用是一个服务注册中心，而不是普通的应用，默认情况下，这个应用会向注册中心（也是它自己）注册它自己，设置为false表示禁止这种默认行为
eureka.client.fetch-registry=false,表示不去检索其他的服务，因为服务注册中心本身的职责就是维护服务实例，它也不需要去检索其他服务


```
13. 高可用服务注册中心(replicate)
- 在peer1的配置文件中，让它的service-url指向peer2，在peer2的配置文件中让它的service-url指向peer1
- 由于peer1和peer2互相指向对方，实际上我们构建了一个双节点的服务注册中心集群
- 客户端的service-url中添加了两个注册中心地址，我们会发现我的服务提供者在这两个服务注册中心都注册了

14. 为什么注册服务这么慢？
作为一个实例也包括定期心跳到注册表（通过客户端的serviceUrl），默认持续时间为30秒。在实例，服务器和客户端在其本地缓存中都具有相同的元数据（因此可能需要3个心跳）之前，客户端才能发现服务。
您可以使用eureka.instance.leaseRenewalIntervalInSeconds更改期限，这将加快客户端连接到其他服务的过程。在生产中，最好坚持使用默认值，因为服务器内部有一些计算可以对租赁更新期进行假设。


## Euraka和Zookeeper比较
1. Euraka保证AP，ZooKeeper保证CP（partition tolerant）
https://blog.csdn.net/qq_35902689/article/details/78113317

## Ribbon,Feign，Nginx
1. Ribbon是客户端负载均衡 
Ribbon工作时分为两步,第一步先选择Eureka Server，它优先选择在同一个Zone且负载较少的Server。
2. Irule:根据特定算法从服务列表中选取一个要访问的服务（RoundRobenRule，RandomRule。。。）
- 自定义：继承AbstractLoadBalancerRule
- RoundRobinRule
这个类的choose(ILoadBalancer lb, Object key)函数整体逻辑是这样的：开启一个计数器count，在while循环中遍历服务清单，
获取清单之前先通过incrementAndGetModulo方法获取一个下标，这个下标是一个不断自增长的数先加1然后和服务清单总数取模之后获取到的（所以这个下标从来不会越界），拿着下标再去服务清单列表中取服务
- RandomRule
在这个重载的choose方法中，每次利用random对象生成一个不大于服务实例总数的随机数，并将该数作为下标所以获取一个服务实例。
- 第二部再根据用户指定的策略，再从Server取到的服务注册列表中选择一个地址。其中Ribbon提供了多种策略，例如轮询round robin，随机Random，根据相应时间加权等。
- RetryRule
每次还是采用RoundRobinRule中的choose规则来选择一个服务实例，如果选到的实例正常就返回，如果选择的服务实例为null或者已经失效，则在失效时间deadline之前不断的进行重试（重试时获取服务的策略还是RoundRobinRule中定义的策略），如果超过了deadline还是没取到则会返回一个null。
- WeightedResponseTimeRule
WeightedResponseTimeRule是RoundRobinRule的一个子类，在WeightedResponseTimeRule中对RoundRobinRule的功能进行了扩展，WeightedResponseTimeRule中会根据每一个实例的运行情况来给计算出该实例的一个权重，然后在挑选实例的时候则根据权重进行挑选，这样能够实现更优的实例调用。WeightedResponseTimeRule中有一个名叫DynamicServerWeightTask的定时任务，默认情况下每隔30秒会计算一次各个服务实例的权重，权重的计算规则也很简单，如果一个服务的平均响应时间越短则权重越大，那么该服务实例被选中执行任务的概率也就越大。
```
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)

/**
 * 
 * Here, we override the IPing and IRule used by the default load balancer. The
 * default IPing is a NoOpPing (which doesn’t actually ping server instances,
 * instead always reporting that they’re stable), and the default IRule is a
 * ZoneAvoidanceRule (which avoids the Amazon EC2 zone that has the most
 * malfunctioning servers, and might thus be a bit difficult to try out in our
 * local environment).
 * 
 */
public class RibbonConfiguration {
	@Autowired
	private IClientConfig ribbonClientConfig;
 
	/**
	 * Our IPing is a PingUrl, which will ping a URL to check the status of each
	 * server.provider has, as you’ll recall, a method mapped to the / path;
	 * that means that Ribbon will get an HTTP 200 response when it pings a
	 * running provider server.
	 * 
	 * server list defined in application.yml :listOfServers: localhost:8000,
	 * localhost:8002,localhost:8003
	 * 
	 */
	@Bean
	public IPing ribbonPing(IClientConfig config) {
		// ping url will try to access http://microservice-provider/provider/ to
		// see if reponse code is 200 . check PingUrl.isAlive()
		// param /provider/ is the context-path of provider service
		return new PingUrl(false, "/provider/");
	}
 
	/**
	 * The IRule we set up, the AvailabilityFilteringRule, will use Ribbon’s
	 * built-in circuit breaker functionality to filter out any servers in an
	 * “open-circuit” state: if a ping fails to connect to a given server, or if
	 * it gets a read failure for the server, Ribbon will consider that server
	 * “dead” until it begins to respond normally.
	 * 
	 * AvailabilityFilteringRule | 过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值） | 使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个server的运行状态
	 * RandomRule  | 随机选择一个server
	 * BestAvailableRule | 选择一个最小的并发请求的server | 逐个考察Server，如果Server被tripped了，则忽略，在选择其中
	 * RoundRobinRule  |  roundRobin方式轮询选择  |  轮询index，选择index对应位置的server
	 * WeightedResponseTimeRule  |  根据响应时间分配一个weight，响应时间越长，weight越小，被选中的可能性越低。  |  一 个后台线程定期的从status里面读取评价响应时间，为每个server计算一个weight。Weight的计算也比较简单responsetime 减去每个server自己平均的responsetime是server的权重。当刚开始运行，没有形成statas时，使用roubine策略选择 server。
	 * RetryRule  |  对选定的负载均衡策略机上重试机制。 |  在一个配置时间段内当选择server不成功，则一直尝试使用subRule的方式选择一个可用的server
	 * ZoneAvoidanceRule  |  复合判断server所在区域的性能和server的可用性选择server  |  使 用ZoneAvoidancePredicate和AvailabilityPredicate来判断是否选择某个server，前一个判断判定一个 zone的运行性能是否可用，剔除不可用的zone（的所有server），AvailabilityPredicate用于过滤掉连接数过多的 Server。
	 * @param config
	 * @return
	 */
	@Bean
	public IRule ribbonRule(IClientConfig config) {
		// return new AvailabilityFilteringRule();
		 return new RandomRule();//
		// return new BestAvailableRule();
		// return new RoundRobinRule();//轮询
		// return new WeightedResponseTimeRule();
		// return new RetryRule();
		// return new ZoneAvoidanceRule();
	}
}
```

2. @LoadBalanced
- Annotation to mark a RestTemplate bean to be configured to use a LoadBalancerClient
- choose(String serviceId)根据传入的服务名serviceId从客户端负载均衡器中挑选一个对应服务的实例。
- 简而言之，就是RestTemplate发起一个请求，这个请求被LoadBalancerInterceptor给拦截了，拦截后将请求的地址中的服务逻辑名转为具体的服务地址，然后继续执行请求，就是这么一个过程

# Feign
- Ribbon和Hystrix的功能都有，只是我们使用Feign实现起来更简单，Feign使用了一种更加优雅的方式来调用服务提供者的接口，避免了我们写模板式的RestTemplate代码。
- Feign是声明式的Web服务客户端，可帮助我们更加便捷调用http api,支持压缩GZIP，支持每个客户端日志，上传文件
- Feign 是在 Ribbon的基础上进行了一次改进，是一个使用起来更加方便的 HTTP 客户端。调用方式不同，
Ribbon需要自己构建http请求，模拟http请求然后使用RestTemplate发送给其他服务，步骤相当繁琐，还需拼接url
- 创建步骤
```
1. 创建一个Feign接口，并且加上注解 , 自定义配置类可以加http认证之类
@FeignClient(name="service-name" ，url="指定url",configuration = "自定义配置类.class",callback="histrixcallback.class")
public interface UseFeignClient

@RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json")
Store update(@PathVariable("storeId") Long storeId, Store store);

@ResController
public class Con {
    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id){
        return userFeignClient.findById(id);
    }
}
2. @EnableFeignClients(defaultConfiguration = DefaultRibbonConfig.class)

# 开启日志 格式为logging.level.+Feign客户端路径
logging.level.org.sang.HelloService=debug

# 配置请求GZIP压缩
feign.compression.request.enabled=true
# 配置响应GZIP压缩
feign.compression.response.enabled=true
# 配置压缩支持的MIME TYPE
feign.compression.request.mime-types=text/xml,application/xml,application/json
# 配置压缩数据大小的下限
feign.compression.request.min-request-size=2048

```

# zuul 是netflix开源的一个API Gateway 服务器, 本质上是一个web servlet应用。
https://www.liangzl.com/get-article-detail-20957.html
- 网关的作用
1. 降低客户端复杂性
2. 跨域请求，减少客户端和各个微服务的交互次数
3. 无需每个服务单独认证，防火墙
- Zuul
* 身份认证与安全：识别每个资源的验证要求，并拒绝那些与要求不符的请求。
* 审查与监控：在边缘位置追踪有意义的数据和统计结果，从而带来精确的生产视图。
* 动态路由：动态地将请求路由到不同的后端集群。
* 压力测试：逐渐增加指向集群的流量，以了解性能。
* 负载分配：为每一种负载类型分配对应容量，并启用超出限定值的请求。
* 静态响应处理：在边缘位置直接建立部分相应，从而避免其转发到内部集群。
- 请求过滤
1. 首先我们定义一个过滤器继承自ZuulFilter
```
public class PermisFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String login = request.getParameter("login");
        if (login == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.addZuulResponseHeader("content-type","text/html;charset=utf-8");
            ctx.setResponseBody("非法访问");
        }
        return null;
    }
}
1.filterType方法的返回值为过滤器的类型，过滤器的类型决定了过滤器在哪个生命周期执行，pre表示在路由之前执行过滤器，其他可选值还有post、error、route和static，当然也可以自定义。
2.filterOrder方法表示过滤器的执行顺序，当过滤器很多时，这个方法会有意义。
3.shouldFilter方法用来判断过滤器是否执行，true表示执行，false表示不执行，在实际开发中，我们可以根据当前请求地址来决定要不要对该地址进行过滤，这里我直接返回true。
4.run方法则表示过滤的具体逻辑，假设请求地址中携带了login参数的话，则认为是合法请求，否则就是非法请求，如果是非法请求的话，首先设置ctx.setSendZuulResponse(false);表示不对该请求进行路由，然后设置响应码和响应值。这个run方法的返回值在当前版本(Dalston.SR3)中暂时没有任何意义，可以返回任意值。


```
2. 然后在入口类中配置相关的Bean即可
```
@Bean
PermisFilter permisFilter() {
    return new PermisFilter();
}

```
- 通配符	
1. ? 表示单个字符
2. * 匹配任意字符
3. ** 匹配任意字符并且各个/
- 高可用
zuul 也可以注册到eureka 里
- 聚合
1. 加依赖aggregation
2. 使用Observable 模式和angularjs的Observable很类似
- sensitiveHeaders ignoredHeaders
sensitiveHeaders会过滤客户端附带的headers
例如：
sensitiveHeaders: X-ABC
如果客户端在发请求是带了X-ABC，那么X-ABC不会传递给下游服务

ignoredHeaders会过滤服务之间通信附带的headers
例如：
ignoredHeaders: X-ABC
如果客户端在发请求是带了X-ABC，那么X-ABC依然会传递给下游服务。但是如果下游服务再转发就会被过滤



# 服务的调度
Rest，RPC， gRPC

# 服务器的熔断器
- 如何熔断：断路器（打开，半打开，关闭，fa）
正常情况关闭，当失败数量到一定的threadshold就会打开，打开一段时间会进入半开，此时允许一个请求访问，如果成功就关闭断路器
(Hystrix中的默认值为5秒内的20次故障）
- 断路器功能：异常处理，日志记录，测试失败操作，手动复位，并发，重试失败请求
- 熔断和降级：目的一致，最终表现类似，粒度一般都是服务级别，触发条件不同
 * 熔断的目的是当A服务模块中的某块程序出现故障后为了不影响其他客户端的请求而做出的及时回应。
 * 降级的目的是为了解决整体项目的压力，而牺牲掉某一服务模块而采取的措施。
1. 目的很一致，都是从可用性可靠性着想，为防止系统的整体缓慢甚至崩溃，采用的技术手段；
2. 最终表现类似，对于两者来说，最终让用户体验e到的是某些功能暂时不可达或不可用；
3. 粒度一般都是服务级别，当然，业界也有不少更细粒度的做法，比如做到数据持久层（允许查询，不允许增删改）；
1. 触发原因不太一样，服务熔断一般是某个服务（下游服务）故障引起，而服务降级一般是从整体负荷考虑；
2. 管理目标的层次不太一样，熔断其实是一个框架级的处理，每个微服务都需要（无层级之分），而降级一般需要对业务有层级之分（比如降级一般是从最外围服务开始）
- 通用方法
```
@EnableHystrix ,@EnableCircuitBreaker

@HystrixCommand(fallbackMethod="")
```
- 隔离策略
1. Thread ：hystrixCommand将在单独线程上执行（默认）
2. Semaphore：将在调用线程上执行，开销相对较小，并受到线程池数量限制
- Feign的支持
1. 打开hystrix:feign.hystrix.enabled=true
2. 在FeignClient里加上fallback
- 回退原因
1. 在fallback方法里加上Throwable的参数
2. 在自定义fallbackFactory,覆盖create方法，create方法参数是Throwable
- 缓存
1. 如果我们使用了自定义Hystrix请求命令的方式来使用Hystrix，c，如下：
```
public class BookCommand extends HystrixCommand<Book> {

    private RestTemplate restTemplate;
    private Long id;

    @Override
    protected Book getFallback() {
        Throwable executionException = getExecutionException();
        System.out.println(executionException.getMessage());
        return new Book("宋诗选注", 88, "钱钟书", "三联书店");
    }

    @Override
    protected Book run() throws Exception {
        return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class,id);
    }

    public BookCommand(Setter setter, RestTemplate restTemplate,Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }
}

```

```
@CacheRemove(commandKey = "test6")
@HystrixCommand
public Book test7(@CacheKey Integer id) {
    return null;
}

```
- 请求合并
Hystrix中的请求合并，就是利用一个合并处理器，将对同一个服务发起的连续请求合并成一个请求进行处理(这些连续请求的时间窗默认为10ms)，
在这个过程中涉及到的一个核心类就是HystrixCollapser
https://mp.weixin.qq.com/s/0QSKVLaDjBAscRaeccaXuA?

- 整合hystrix首次请求失败
hystrix默认超时时间是1秒，由于spring的懒加载机制，首次请求有可能大于1秒，就进入了fallback逻辑，解决方法是给ribbon和zuul加eager-load
或者延长hystrix超时时间

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
- 监测
1. Hystrix Dashboard
2. Turbine

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

# 运行机制
1. 请求统一通过API网关（Zuul）来访问内部服务.

2. 网关接收到请求后，从注册中心（Eureka）获取可用服务

3. 由Ribbon进行均衡负载后，分发到后端具体实例

4. 微服务之间通过Feign进行通信处理业务

5. Hystrix负责处理服务超时熔断

6. Turbine监控服务间的调用和熔断相关指标

# swagger，compodoc
强大的API文档工具

# spring cloud各组件调优
1. tomcat的最大连接数，线程数量
2. zuul,hystrix的thread和semaphore

# Consistency 和 Availability 的矛盾
一致性和可用性，为什么不可能同时成立？答案很简单，因为可能通信失败（即出现分区容错）。

如果保证 G2 的一致性，那么 G1 必须在写操作时，锁定 G2 的读操作和写操作。只有数据同步后，才能重新开放读写。锁定期间，G2 不能读写，没有可用性不。

如果保证 G2 的可用性，那么势必不能锁定 G2，所以一致性不成立。

综上所述，G2 无法同时做到一致性和可用性。系统设计时只能选择一个目标。如果追求一致性，那么无法保证所有节点的可用性；如果追求所有节点的可用性，那就没法做到一致性。

