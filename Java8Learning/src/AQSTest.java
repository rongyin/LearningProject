import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AQSTest extends AbstractQueuedSynchronizer {

    @Override
    protected boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryRelease(int releases) {
        int c = getState() - releases;
        if (Thread.currentThread() != getExclusiveOwnerThread())
            throw new IllegalMonitorStateException();
        boolean free = false;
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }

    public static void main(String[] args) {
        AQSTest lock = new AQSTest();
        lock.tryAcquire(1);
        System.out.println("lock 1 ");
        lock.tryAcquire(1);
        System.out.println("lock 1 ");
        lock.tryRelease(1);
        System.out.println("release 1 ");
        System.out.println("release 2 ");
    }
}