package com.cn;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ReferenceTest {
    /**
     * 在一个对象被垃圾回收器扫描到将要进行回收时，其相应的引用包装类，即reference对象会被放入其注册的引用队列queue中。可以从queue中获取到相应的对象信息，同时进行额外的处理。比如反向操作，数据清理，资源释放等。
     */
    static ReferenceQueue<NormalObject> referenceReferenceQueue = new ReferenceQueue<NormalObject>();

    public static void checkQueue(){
        Reference<NormalObject> weakReference = null;

        while ( (weakReference = (Reference<NormalObject>)referenceReferenceQueue.poll()) !=null){
            if(weakReference!=null){
                System.out.println("object is "+weakReference.get());
                System.out.println(((NormalObjectReference) weakReference).name);
            }
        }

    }

    public static void main(String[] args) {

        ArrayList<WeakReference<NormalObject>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(new NormalObjectReference(new NormalObject("normalObject "+i),referenceReferenceQueue));
            //list.get(i).enqueue();
            //referenceReferenceQueue.poll();
        }
        list.forEach( o ->{
            System.out.println(o.isEnqueued());
        });
        System.out.println("first time");

        checkQueue();
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("second time");
        checkQueue();

    }
}

class NormalObject{
    String name;
    NormalObject(String name){
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Normal Object finalize "+name);
    }
}

class NormalObjectReference extends WeakReference<NormalObject>{
    String name;


    public NormalObjectReference(NormalObject object, ReferenceQueue queue){
        super(object,queue);
        this.name = object.name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("NormalObject Reference finalize "+ name);
    }
}