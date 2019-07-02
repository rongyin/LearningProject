# synchronized锁重入
关键字synchronized拥有锁重入的功能，也就是在使用synchronized时，当一个线程得到一个对象锁之后，再次请求此对象锁是可以再次得到该对象的锁的。


多个线程多个锁，每个线程都可以拿到自己指定的锁，获得锁之后，执行synchronized方法体的内容
static synchronized 的方法，不管你新建了多少Thread,就只有1个锁

atomic 保证一个方法原子性，不能保证多个方法原子性
volatile 保证同步，不保证原子性

notify， 是发出通知，准备唤醒wait，但是没有释放锁
wait 释放锁