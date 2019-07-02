import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.*;

public class MThreadTest {
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

    private synchronized void after() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void CompeletableTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Integer> production = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<String> collect = production.stream().map(i -> CompletableFuture.supplyAsync(() -> {
            return getData(i);
        }, executorService))
                .map( f -> f.thenApply( d -> d.toString()))
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
        CompletableFuture.supplyAsync(()->1).thenApply( i->i+1).whenComplete( (i,e) ->{
            System.out.println("result is "+i);
            System.out.println("exception is "+e);
        });
        CompletableFuture.supplyAsync( ()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("start");
            return 1;
        }).thenCombine(CompletableFuture.supplyAsync(() ->{
            System.out.println("then");
            return 2;
        }),(v1,v2) ->v1+v2).thenAccept(System.out::println);
        System.out.println("main");
        //Thread.sleep(1000L);
        System.out.println("done");
    }

    @Test
    public void waitNotifyTest(){

        final Object lock = new Object();
        Thread t1 = new Thread(()->{
            synchronized (lock){
                int count = 1;
                for(int i=0;i<10;i++){
                    count++;
                    if(i==5) {
                        lock.notify();
                        System.out.println("sent notify");
                    }
                    System.out.println("count is "+count);
                }
            };
        });

        Thread t2 = new Thread(()->{
            synchronized (lock){
                try {
                    System.out.println("release lock.");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait finished.");
            };
        });
        t2.start();
        t1.start();
    }

    @Test
    public void countDownTest(){
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(()->{
            int count = 1;
            for(int i=0;i<10;i++){
                    count++;
                    if(i==5) {
                        System.out.println("count down 1");
                        countDownLatch.countDown();

                    }
                    System.out.println("t1 count is "+count);

            }
        });
        Thread t3 = new Thread(()->{
            int count = 1;
            for(int i=0;i<10;i++){
                count++;
                if(i==5) {
                    System.out.println("countDown 2");
                    countDownLatch.countDown();

                }
                System.out.println("t3 count is "+count);

            }
        });
        Thread t2 = new Thread(()->{
                try {
                    System.out.println("release lock.");
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("wait finished.");

        });
        t2.start();
        t1.start();
        t3.start();
    }
}
