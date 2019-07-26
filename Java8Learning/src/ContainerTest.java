

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ContainerTest {


    @Test
    public void arrayListTest() {
        AtomicInteger atomicInteger = new AtomicInteger();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        Spliterator<Integer> spliterator = list.spliterator();

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {

                spliterator.trySplit().forEachRemaining((o) -> {
                    System.out.println(Thread.currentThread().getName() + " start:" + o);
                    atomicInteger.addAndGet(o);
                    System.out.println(Thread.currentThread().getName() + ":" + atomicInteger);
                });
                System.out.println(Thread.currentThread().getName() + " end");
            }).start();
        }


        /*
        ListIterator<Integer> listIterator = list.listIterator();
        listIterator.next();
        listIterator.add(10);
        listIterator.next();
        listIterator.remove();
        listIterator.next();*/
    }

    @Test
    public void testWeekMap() {
        WeakReference<String> weakReference = new WeakReference<>("test");
        WeakHashMap<Integer, Integer> weakHashMap = new WeakHashMap<>();
        weakHashMap.put(1, 2);
        weakHashMap.put(2, 3);
        weakHashMap.put(3, 4);

        System.gc();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        weakHashMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        System.out.println("weak " + weakReference.get());
    }

    @Test
    public void testLinkedHashMap() {
        LRUTest<Integer, String> linkedHashMap = new LRUTest<>();
        linkedHashMap.put(1, "a");
        linkedHashMap.put(2, "b");
        linkedHashMap.put(3, "b");
        linkedHashMap.get(1);
        linkedHashMap.put(4, "d");
        linkedHashMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
    }

    @Test
    public void testMap() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(1 << 30);
        int n = 6 - 1;
        n |= n >>> 1;
        System.out.println(n);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;

        System.out.println(n);
        Integer i1 = new Integer("2");
        Integer i2 = new Integer("4");
        System.out.println(i1.hashCode());
        System.out.println(i2.hashCode());
        String key = "test";

        String key1 = "west";
        String key2 = "key";
        Integer key_h = key.hashCode();
        Integer key_16 = key_h >>> 16;
        Integer key1_h = key1.hashCode();
        Integer key2_h = key2.hashCode();

        System.out.println(Integer.toBinaryString(key2_h));
        System.out.println(Integer.toBinaryString(31));
        //11001111001011111
        //1111
        //11111
        /*System.out.println(Integer.toBinaryString(key_h));
        System.out.println(Integer.toBinaryString(key1_h));
        System.out.println(Integer.toBinaryString(key_16));
        System.out.println(Integer.toBinaryString(15));*/
        //010010
        //110110 ^
        //100100
        //001111
        //& -> 000100
        System.out.println(15 & hash(key));
        System.out.println(31 & hash(key));

        System.out.println(15 & hash(key2));
        System.out.println(31 & hash(key2));
    }

    @Test
    public void testQueue1() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(3, Comparator.reverseOrder());
        pq.offer(4);
        pq.add(3);
        Optional.of(pq).ifPresent(System.out::println);
    }

    private String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result += Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Test
    public void testSet() {
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("2");
        boolean isAdd = treeSet.add("2");
        //Assert.assertEquals(false, isAdd);
        treeSet.stream().forEach(System.out::println);
    }

    @Test
    public void testQueue2() {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        try {
            queue.put("ff1");
            queue.put("ff2");
            queue.add("gg");//will throw IllegalStateException: Queue full
            //queue.put("fg"); // will wait for the queue size
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void SynchronousQueueTest() {
        final SynchronousQueue q = new SynchronousQueue();
        /*new Thread(()->{
            try {
                System.out.println(q.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
        //q.add("e"); // will throw Queue full issue.
        try {

            q.put("d");//Adds the specified element to this queue, waiting if necessary for another thread to receive it.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * Inserts the specified element into this queue if it is possible to do so
         * immediately without violating capacity restrictions, returning
         * <tt>true</tt> upon success and throwing an <tt>IllegalStateException</tt>
         * if no space is currently available.
         * */
        /*new Thread(()->{
            q.add("c");
        }).start();*/


    }

    public final class ConcurrentCache<K, V> {

        private final int size;

        private final Map<K, V> eden;

        private final Map<K, V> longterm;

        public ConcurrentCache(int size) {
            this.size = size;
            this.eden = new ConcurrentHashMap<>(size);
            this.longterm = new WeakHashMap<>(size);
        }

        public V get(K k) {
            V v = this.eden.get(k);
            if (v == null) {
                v = this.longterm.get(k);
                if (v != null)
                    this.eden.put(k, v);
            }
            return v;
        }

        public void put(K k, V v) {
            if (this.eden.size() >= size) {
                this.longterm.putAll(this.eden);
                this.eden.clear();
            }
            this.eden.put(k, v);
        }
    }
}

class LRUTest<K, V> extends LinkedHashMap<K, V> {
    LRUTest() {
        super(3, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > 3;
    }

}