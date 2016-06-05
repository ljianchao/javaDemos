package cn.jc.demos.concurrency.future;

import cn.jc.demos.concurrency.future.Data;

/**
 * Created by Administrator on 2016/6/5.
 */
public class RealData implements Data {

    protected final String result;

    public RealData(String para) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <10; i++){
            sb.append(para);
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){

            }
        }
        result = sb.toString();
    }

    public String getResult() {
        return result;
    }
}
