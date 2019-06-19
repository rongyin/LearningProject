import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.*;

public class MThreadTest {
    @Test
    public void FutureTest1() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(() ->{
            try {
                Thread.sleep(1000);
                System.out.println("execute done.");
            }catch (Exception e){
                e.printStackTrace();;
            }
        });

        Future<String> future =service.submit( ()->{
            Thread.sleep(1000);
            return "submit.";
        });

        /*
        1.8之前做法，没有完成前只能做其他事情，或者等着
         */
        while(!future.isDone()){
            Thread.sleep(10);
        }
        String value = future.get(10, TimeUnit.MINUTES);

        Optional.ofNullable(value).ifPresent(System.out::println);
    }



}
