package cn.jc.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

/**
 * 测试获取当前路径方法
 * @author Administrator
 *
 */
public class testGetFilePath {
	private String FileName = "center.gif";
	
	/**
	 * 获取文件路径
	 * @throws IOException 
	 */
	@Test
	public void getPath() throws IOException{
		//1. 加“/”获取当前类的所在工程的类路径
		File file = new File(java.net.URLDecoder.decode(this.getClass().getResource("/").getPath(),"utf-8"));
		System.out.println(file);
		
		//2. 获取当前类的绝对路径
		File file2 = new File(java.net.URLDecoder.decode(this.getClass().getResource("").getPath(),"utf-8"));
		System.out.println(file2);
		
		//3. 获取当前类的所在工程路径
		File directory = new File("");
		String courseFile = directory.getCanonicalPath();
		System.out.println(courseFile);
		
		//4. 获取工程src目录下文件的路径
		URL picUrl = this.getClass().getResource(FileName);
		System.out.println(java.net.URLDecoder.decode(picUrl.getPath(), "utf-8"));
		
		//5. 获取当前工程路径
		System.out.println("user.dir: " + System.getProperty("user.dir"));
		
		//6. 获取当前工程的类路径
		System.out.println(System.getProperty("java.class.path"));
		
		System.out.println("separator: " + File.separator);
		System.out.println("separatorChar: " + File.separatorChar);
		System.out.println("pathSeparator: " + File.pathSeparator);
		System.out.println("pathSeparatorChar" + File.pathSeparatorChar);
	}
		
}
