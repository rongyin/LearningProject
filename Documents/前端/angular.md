# angular 1
- 需要bower,tslint,gulp
- lint - tsc - bundle - karma - browser-sync

# Yarn的优点？
- 并行安装：无论 npm 还是 Yarn 在执行包的安装时，都会执行一系列任务。npm 是按照队列执行每个 package，也就是说必须要等到当前 package 安装完成之后，才能继续后面的安装。而 Yarn 是同步执行所有任务，提高了性能。
- 离线模式：如果之前已经安装过一个软件包，用Yarn再次安装时之间从缓存中获取，就不用像npm那样再从网络下载了。
- 更简洁的输出
- 安装版本统一：为了防止拉取到不同的版本，Yarn 有一个锁定文件 (lock file) 记录了被确切安装上的模块的版本号。
- Yarn和npm命令对比

| npm  | yarn
|---|
|  npm install	 |yarn
| npm install react --save	|yarn add react
|npm uninstall react --save	|yarn remove react
|npm install react --save-dev	|yarn add react --dev
|npm update --save	|yarn upgrade

# Angular
Angular is a platform and framework for building client applications in HTML and TypeScript. Angular is written in TypeScript. It implements core and optional functionality as a set of TypeScript libraries that you import into your apps.



# Angular CLI

# NgModule
NgModules provide a compilation context for their components. A root NgModule always has a root component that is created during bootstrap, but any NgModule can include any number of additional components, which can be loaded through the router or created through the template. The components that belong to an NgModule share a compilation context.
In JavaScript each file is a module and all objects defined in the file belong to that module.
NgModule metadata
An NgModule is defined by a class decorated with @NgModule(). The @NgModule() decorator is a function that takes a single metadata object, whose properties describe the module. The most important properties are as follows.

- declarations: The components, directives, and pipes that belong to this NgModule.

- exports: The subset of declarations that should be visible and usable in the component templates of other NgModules.

- imports: Other modules whose exported classes are needed by component templates declared in this NgModule.

- providers: Creators of services that this NgModule contributes to the global collection of services; they become accessible in all parts of the app. (You can also specify providers at the component level, which is often preferred.)

- bootstrap: The main application view, called the root component, which hosts all other app views. Only the root NgModule should set the bootstrap property.

- Angular libraries are NgModules, such as Material Design, Ionic,

## There are five general categories of feature modules which tend to fall into the following groups:

- Domain feature modules.
- Routed feature modules.
- Routing modules.
- Service feature modules.
- Widget feature modules.

# Component
A component and its template together define a view. 
An app's components typically define many views, arranged hierarchically. Angular provides the Router service to help you define navigation paths among views. The router provides sophisticated in-browser navigational capabilities.
- A component has a lifecycle managed by Angular.
OnChanges, OnInit, DoCheck (3x), AfterContentInit, AfterContentChecked (3x), AfterViewInit, AfterViewChecked (3x), and OnDestroy.
* The ngOnChanges() method is your first opportunity to access those properties. Angular calls ngOnChanges() before ngOnInit() and many times after that. It only calls ngOnInit() once.
* ngOnDestroy this.gridOptions.api.destroy(); this.mainQueryParamSubscription.unsubscribe();
- Component Interact
- @Input,@Output

# Service
For data or logic that isn't associated with a specific view, and that you want to share across components, you create a service class. A service class definition is immediately preceded by the @Injectable() decorator. The decorator provides the metadata that allows other providers to be injected as dependencies into your class.
Dependency injection (DI) lets you keep your component classes lean and efficient.

```
1. constructor(private service: HeroService) { }

2. constructor(private injector: Injector){}
let modalHelper: ModalHelperService = this.injector.get(ModalHelperService);
```

# Template
A template combines HTML with Angular markup that can modify HTML elements before they are displayed. Template directives provide program logic, and binding markup connects your application data and the DOM. There are two types of data binding:

Event binding lets your app respond to user input in the target environment by updating your application data.
Property binding lets you interpolate values that are computed from your application data into the H

## four forms of data binding markup
- {{value}}
- [property]="value"
- (event)="handler"
- [(ng-Model)]="property"

# Pipe
Angular pipes let you declare display-value transformations in your template HTML.
```
<input type="text" name="duration" class="form-control mr-3" style="width:70px;" id="duration"
              (ngModelChange)="updateModel($event,'duration')" [ngModel]="serviceHeader.duration | hideZero">
@Pipe({
  name: "hideZero"
})
export class HideZeroPipe implements PipeTransform {
  transform(value: any, _args?: any): any {
    if (!value || value === 0) {
      return "";
    }
    return value;
  }
}

```
# directive
## Structural directives
Structural directives alter layout by adding, removing, and replacing elements in the DOM
```
<li *ngFor="let hero of heroes"></li>
<app-hero-detail *ngIf="selectedHero"></app-hero-detail>

```
## Attribute directives
Attribute directives alter the appearance or behavior of an existing element

# Observables
Observables provide support for passing messages between publishers and subscribers in your application
Angular makes use of observables as an interface to handle a variety of common asynchronous operations. For example:
3 种用法
1. 异步取值
```
<div style="font-size: 1.5rem;" [innerHtml]="(asyncMessage | async) || message"></div>
      this.asyncMessage = this.httpService.postAtInterval("/quotingWeb/controller/systemController/getCallStatus", messageId).pipe(
        filter((messages: string[]) => messages && messages.length > 0),
        map((messages: string[]) => messages.join("<br>"))
      );
```
2. httpclient.post return
3. Subject,BehaviorSubject
- You can define custom events that send observable output data from a child to a parent component.
- The HTTP module uses observables to handle AJAX requests and responses.
- The Router and Forms modules use observables to listen for and respond to user-input events.
## Observables compared to promises
- Observables are declarative; computation does not start until subscription. Promises execute immediately on creation. This makes observables useful for defining recipes that can be run whenever you need the result.
- Observables provide many values. Promises provide one. This makes observables useful for getting multiple values over time.
- Observables differentiate between chaining and subscription. Promises only have .then() clauses.
- Observables subscribe() is responsible for handling errors. Promises push errors to the child promises.
```
obs.pipe(map((value) => value * 2));

```
- Subject,BehaviorSubject
* A Subject is a special type of Observable that allows values to be multicasted to many Observables. Subjects are like EventEmitters.
* A variant of Subject that requires an initial value and emits its current value whenever it is subscribed to.
https://blog.csdn.net/u010130282/article/details/54633117

# Q2O
AppModule ->bootstrap -> RoutingModule -> sub Module -> Component -> Service
