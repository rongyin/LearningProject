- http请求的几种类型，http证书生成
get，post
它其实还是 HTTP 协议，只是在外面加了一层，SSL 是一种加密安全协议
在证书的数字签名中使用了哈希算法和非对称加密算法，在加密通信的过程中使用了对称加密算法
 Certificate Authority ，翻译成中文就是证书授权中心，它是专门负责管理和签发证书的第三方机构，一旦由于证书安全问题导致经济损失，可以获得一笔巨额的赔偿
 如果用户想得到一份属于自己的证书，他应先向 CA 提出申请。在 CA 判明申请者的身份后，便为他分配一个公钥，并且 CA 将该公钥与申请者的身份信息绑在一起，并为之签字后，便形成证书发给申请者

- mysql和oracle的集群区别

- oracle的分表,index

- 限流算法

- 防爬虫

- 分布式插入时候多服务如何生成key

- spring mvc迁移到spring boot
导入spring-boot-starter-web包括restful和mvc+tomcat
Spring boot mvc默认视图是thymeleaf
Profile配置
全局变量从properties文件读入
数据源与Mybatis配置
日志文件配置
WebConfig配置(包括原有的web.xml和spring-mvc.xml)
去掉多余的bean注入
https://blog.csdn.net/zxl2016/article/details/80708810

- spring boot starter 

- weakhashmap
它的Entry和普通HashMap的Entry最大的不同是它继承了WeakReference，然后把Key做成了弱引用（注意只有Key没有Value），然后传入了一个ReferenceQueue，这就让它能在某个key失去所有强引用的时候感知到。


- js请求超时设定
第一步：将网络请求事件赋值给变量ajaxTimeOut 
第二步：通过设置timeout属性值，来定义超时时间
第三步：通过complete中的status是否等于timeout来判断是否超时，并设置超时提示处理
 
- angularjs 预加载
route的loadChildren有个属性 url#Module
RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })

我们还可以在路由中定义附加的参数来指定哪些模块进行预加载，我们使用路由定义中的 data 来提供这个附加的数据。
  { path: 'shop', loadChildren: './shop/shop.module#ShopModule', data: {preload: true} },
然后，我们定义新的加载策略。
export class PreloadSelectedModules implements PreloadingStrategy {
  preload(route: Route, load: Function): Observable<any> {
    return route.data && route.data.preload ? load() : Observable.of(null);
  }
}
最后，在 app.module.ts 中使用这个策略。
RouterModule.forRoot(routes, { preloadingStrategy: PreloadSelectedModules })
https://www.jb51.net/article/125604.htm

- 重复登陆
1. 表
2. session