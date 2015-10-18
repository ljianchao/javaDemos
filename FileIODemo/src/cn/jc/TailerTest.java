package cn.jc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

public class TailerTest {
	private static String FileName ;	

	static{
		try {
			FileName = java.net.URLDecoder.decode("D://1.txt", "utf-8");
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
	}
	
	 public static void main(String []args) throws Exception{  
        TailerTest tailerTest = new TailerTest();  
        tailerTest.testReadLine();  
        boolean flag = true;  
        File file = new File(FileName);  
  
        int i = 0;
        while(flag){  
            Thread.sleep(1000);  
            FileUtils.write(file,""+(i++)+ IOUtils.LINE_SEPARATOR,true);  
        }  
	  
	 }  
	
	private void testReadLine() {
		File file = new File(FileName);
		try {
			FileUtils.touch(file);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		Tailer tailer = new Tailer(file, new TailerListenerAdapter(){
			@Override  
            public void fileNotFound() {  //文件没有找到  
                System.out.println("文件没有找到");  
                super.fileNotFound();  
            }  
  
            @Override  
            public void fileRotated() {  //文件被外部的输入流改变  
                System.out.println("文件rotated");  
                super.fileRotated();  
            }  
  
            @Override  
            public void handle(String line) { //增加的文件的内容  
                System.out.println("文件line:"+line);  
                super.handle(line);  
            }  
  
            @Override  
            public void handle(Exception ex) {  
                ex.printStackTrace();  
                super.handle(ex);  
            }  
			
		}, 4000, true);
		
		new Thread(tailer).start();
	}
}
