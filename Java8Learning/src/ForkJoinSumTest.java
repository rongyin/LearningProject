import java.util.concurrent.RecursiveTask;

public class ForkJoinSumTest extends  RecursiveTask<Long> {
    long start ;
    long end ;

    final long THRESHOLD = 100;
    public ForkJoinSumTest(long start,long end){
        this.start = start;
        this.end = end;
    }
    @Override
    protected Long compute() {
        long length = end - start;
        if(length<THRESHOLD){
            long sum = 0;
            for (long i=start;i<=end;i++){
                sum +=  i;
            }
            return  sum;
        }else{
            long middle = (start + end)/2;
            ForkJoinSumTest left = new ForkJoinSumTest(start,middle);
            left.fork();
            ForkJoinSumTest right = new ForkJoinSumTest(middle+1,end);
            right.fork();

            return left.join()+right.join();
        }

    }
}
