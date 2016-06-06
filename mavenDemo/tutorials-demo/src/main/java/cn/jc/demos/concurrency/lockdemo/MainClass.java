package cn.jc.demos.concurrency.lockdemo;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MainClass {
    public static void main(String[] args) {
        final BlockQueue queue = new BlockQueue();

        new Thread() {
            @Override
            public void run() {
                try {
                    Object o= queue.pop();
                    System.out.println(o.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        queue.put("new item");

    }
}
