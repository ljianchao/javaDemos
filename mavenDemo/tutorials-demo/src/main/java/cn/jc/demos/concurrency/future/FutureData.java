package cn.jc.demos.concurrency.future;

/**
 * Created by Administrator on 2016/6/5.
 */
public class FutureData implements Data {
    protected RealData realData = null;
    protected  boolean isReady = false;

    public synchronized void setRealData(RealData realData){
        if (isReady)
            return;
        
        this.realData = realData;
        isReady = true;
        notifyAll();
    }
    
    public synchronized String getResult() {
        while (!isReady){
            try {
                wait();
            }catch (InterruptedException e){

            }
        }
        return realData.result;
    }
}
