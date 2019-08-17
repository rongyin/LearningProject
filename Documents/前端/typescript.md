# typescript
1. typescript是javascript的超集,遵循最新的es5和es6规范，并且还扩展了Javascript的语法譬如多态，很像java了。
2. 给大型项目提供了一个构建机制，因为typescript基于class，接口，模块，最佳的面向对象原则和实现

# typescript 基本类型
- boolean, number(所有数字都是浮点数)，string,array(var list:Array<number> , list:number[])
- any , void，enum（为了给一个数字集合更好的命名，从0开始）
- var , let(比var小)，const,    元组类型（tuple）arr:[number,string]=[1,'aaa'];
- never 是任何类型的子类型, 并且可以赋值给任何类型，多用在抛出异常时候货无法执行到终点的（例如死循环）
- **允许联合声明 var path:string[]|string **
- 允许类型别名
```
type Type1 = Array<string|number|boolean>
```
- 运行时候能用typeof 和instanceof对类型验证
- ===是比较值和类型
- 位运算没java快，因为要把浮点型转化成32位整形，再转化回来

# 函数
- 可选参数，默认参数
- 可以重载
- promise 对异步操作结果的一个承诺
1. 3 状态：pending（初始），fulfilled(成功)，rejected(失败)
2. promise.all可以并行处理，then的话就是穿行
- async和await可以达到异步函数的顺序问题,一个异步函数调用另一个
```
var p:Promise<number> = ;
async function fn():Promise<number>{
    var i = await p;
    return 1+i;
}
```

# call,apply,bind 改变函数中默认this操作的值
- 所有函数的prototype中都继承了这3
- call以单个分开参数形式
- apply是数组参数
- bind一旦绑定，无法覆盖

# prototype
- 类的继承使用到了