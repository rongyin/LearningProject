import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class MThreadTest {
    @Test
    public void test(){
        while(true){
            try {
                Thread.sleep(2000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Test
    public void FutureTest1() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(() -> {
            try {
                Thread.sleep(1000);

                System.out.println("execute done.");
            } catch (Exception e) {
                e.printStackTrace();
                ;
            }
        });

        Future<String> future = service.submit(() -> {
            Thread.sleep(2000);
            return "submit.";
        });
        future.cancel(true);

        //Optional.ofNullable(future.get()).ifPresent(System.out::println);
        /*
        1.8之前做法，没有完成前只能做其他事情，或者等着
         */
        //while(!future.isDone()){
        //    Thread.sleep(10);
        //}
        //String value = future.get(10, TimeUnit.MINUTES);

        //Optional.ofNullable(value).ifPresent(System.out::println);
    }

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Exchanger<String> exchanger = new Exchanger<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void TreadTest1() {
        executorService.execute(() -> {
            String a = "this is a";
            try {
                String b = exchanger.exchange(a);
                System.out.println("in a tread:" + b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            String b = "this is b";
            try {
                String a = exchanger.exchange(b);
                System.out.println("in b tread:" + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        executorService.shutdown();
    }


    @Test
    public void CompeletableTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Integer> production = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<String> collect = production.stream().map(i -> CompletableFuture.supplyAsync(() -> {
            return getData(i);
        }, executorService))
                .map(f -> f.thenApply(d -> d.toString()))
                .map(CompletableFuture::join).collect(Collectors.toList());
        System.out.println(collect);
    }


    static Double getData(Integer i) {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(i);
        return new Random().nextDouble();
    }

    @Test
    public void CompleteTest2() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 1).whenComplete((i, e) -> {
            System.out.println("result is " + i);
            System.out.println("exception is " + e);
        });
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("start");
            return 1;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println("then");
            return 2;
        }), (v1, v2) -> v1 + v2).thenAccept(System.out::println);
        System.out.println("main");
        //Thread.sleep(1000L);
        System.out.println("done");
    }

    @Test
    public void waitNotifyTest() {

        final Object lock = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                int count = 0;
                for (int i = 0; i < 100; i++) {
                    count++;

                    if (i == 2) {
                        //lock.notify();//The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object.
                        lock.notifyAll();
                        System.out.println("sent notify");
                    }
                }
                System.out.println(count);
            }
            ;
        });

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("release lock.");
                    lock.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait finished.");

            }
            ;
        });
        Thread t3 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("release lock3.");
                    lock.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait finished3.");
            }
            ;
        });
        //t2.setPriority(1);
        //t3.setPriority(10);
        //t3.setDaemon(true);
        t2.start();
        t3.start();
        t1.start();//不能确定t3和t1谁先执行
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //t1.interrupt();
        //Thread.interrupted();
    }

    @Test
    public void interuptedTest() {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("Thread 1 start." + Thread.interrupted());
                while (!Thread.interrupted()) {

                }
                //Thread.sleep(2000); InterruptedException
                System.out.println("Thread 1 done.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        Thread t2 = new Thread(() -> {
            try {
                System.out.println("Thread 2 start.");
                //t1.join();
                t1.interrupt();
                Thread.sleep(1000);
                System.out.println("Thread 2 done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void countDownTest() {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            int count = 1;
            for (int i = 0; i < 10; i++) {
                count++;
                if (i == 5) {
                    System.out.println("count down 1");
                    countDownLatch.countDown();

                }
                System.out.println("t1 count is " + count);

            }
        });
        Thread t3 = new Thread(() -> {
            int count = 1;
            for (int i = 0; i < 10; i++) {
                count++;
                if (i == 5) {
                    System.out.println("countDown 2");
                    countDownLatch.countDown();

                }
                System.out.println("t3 count is " + count);

            }
        });
        Thread t2 = new Thread(() -> {
            try {
                System.out.println("release lock.");
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("wait finished.");

        });
        Thread t4 = new Thread(() -> {
            try {
                System.out.println("release lock 4.");
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("wait finished 4.");

        });
        Thread t5 = new Thread(() -> {
            try {
                System.out.println("release lock 5.");
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("wait finished 5.");

        });
        t2.start();
        t5.start();
        t4.start();
        t1.start();
        t3.start();
    }
    @Test
    public void FairTest(){
        ReentrantLock lock  = new ReentrantLock(false);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                    try {
                        System.out.println(Thread.currentThread().getName() );
                        lock.lock();

                        //Thread.sleep(500);
                    } finally {
                        lock.unlock();
                        //System.out.println(Thread.currentThread().getName() + " unlock " );

                    }
            }).start();
        }

        try {
            Thread.sleep(11000);
        }catch (Exception e){

        }

    }
    @Test
    public void ThreadTest() {
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadClass.isRunning = false;


    }

    @Test
    public void LockSupportTest() {

        Thread t1 = new Thread(() -> {
            System.out.println("start park.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LockSupport.park(this);
              System.out.println("end.");
        });

        t1.start();
        System.out.println("un park.");
        LockSupport.unpark(t1);
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }

    @Test
    public void CyclicBarrierTest(){
        List<String> list = new ArrayList<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4,()->{
            //list.forEach(System.out::println);
            //list.add("my test");
            System.out.println("barrier");
        });
        //cyclicBarrier.reset();
        for (int i = 0; i < 8; i++) {
            new Thread(()->{
                list.add(Thread.currentThread().getName());
                try {
                    System.out.println(Thread.currentThread().getName());
                    if (Thread.currentThread().getName().endsWith("2")){
                        cyclicBarrier.reset();
                    }
                    cyclicBarrier.await();
                    //list.forEach(System.out::println);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

    @Test
    public void SemaphoreTest(){
        Semaphore semaphore = new Semaphore(2);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {

                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName());
                    //if(semaphore.availablePermits()==0){
                    //    semaphore.release();
                    //}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        }


    }


    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        System.out.println("1:"+Thread.interrupted());
        System.out.println("2:"+Thread.interrupted());
        System.out.println("end");
        ThreadClass threadClass = new ThreadClass();
        threadClass.start();
        //threadClass.interrupt();
        //threadClass.interrupt(threadClass);
        System.out.println("isInterrupted:"+threadClass.isInterrupted());
        //测试isRunning
        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadClass.isRunning = false;*/


       /* for (int i = 0; i < 4; i++) {
            Thread t =new Thread(new RunnableClass(),"t"+i);
            t.start();
            *//*if (i==3){
                t.interrupt();
            }*//*
        }*/
    }



}

class RunnableClass implements Runnable{
    private static ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+" start ");
            lock.lockInterruptibly();
            lock.lock();
            Thread.sleep(2000);

            System.out.println(Thread.currentThread().getName()+" end ");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+" interrupted ");
        } finally {
            lock.unlock();
            lock.unlock();
        }
    }
}

class ThreadClass extends Thread {
    public volatile boolean isRunning = true;//这个不是volatile的话，是会把副本传入vm stack

    @Override
    public void run() {

        while (isRunning) {
            /*if(Thread.interrupted()){
                System.out.println("interrupted:" +Thread.interrupted());
               break;
            }*/
            if(this.isInterrupted()){
                System.out.println("thread isInterrupted");
                break;
            }
        }


    }

}