package cn.jc.demos.concurrency.future;

/**
 * Created by Administrator on 2016/6/5.
 */
public class MainClass {
    public static void main(String[] args) {
        Client client = new Client();
        Data data = client.request("name");
        System.out.println("请求完毕");

        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){

        }

        System.out.println("数据 = " + data.getResult());
    }
}
