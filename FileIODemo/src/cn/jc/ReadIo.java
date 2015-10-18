package cn.jc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

/**
 * 测试文件的读取
 * @author Administrator
 *
 */
public class ReadIo {
	private String FileName = "rgw_access.log";
	
	/**
	 * 读取行
	 * 消耗内存小，但是读取时间较长
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testReadFileByLine() throws UnsupportedEncodingException {		
		String path = java.net.URLDecoder.decode(this.getClass().getResource(FileName).getFile(), "utf-8");
		logMemory();
		//System.out.println(path);
		File file = new File(path);
		
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));							
			bufferedWriter = new BufferedWriter(new FileWriter("d:/readFlie.txt"));			
			
			String temp = null;			
			while ((temp = bufferedReader.readLine()) != null) {
				//System.out.println(temp);
				bufferedWriter.write(java.net.URLDecoder.decode(temp,"utf-8") + "\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (bufferedReader != null && bufferedWriter != null) {
				try {
					bufferedReader.close();
					bufferedWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		logMemory();
		
		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 171 Mb
		Max Memory: 2631 Mb
		Total Memory: 215 Mb
		Free Memory: 145 Mb*/
		
		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 171 Mb
		Max Memory: 2631 Mb
		Total Memory: 153 Mb
		Free Memory: 137 Mb*/
		//400M:timespan-10.875s
	}	
	
	/**
	 * 文件流读取
	 * 变量文件中的的所有行-允许对每一行进行处理，而不保持对它的引用。
	 * 总之没有把它放到内存中
	 * @throws IOException 
	 */
	@Test
	public void testScanner() throws IOException {
		String path = java.net.URLDecoder.decode(this.getClass().getResource(FileName).getFile(), "utf-8"); 
		logMemory();
		
		FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                // System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
		
		logMemory();		
		
		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 171 Mb
		Max Memory: 2631 Mb
		Total Memory: 502 Mb
		Free Memory: 395 Mb*/
		//400M:timespan-13.790s
	}
	
	/**
	 * commons io在内存中读取所有行
	 * 消耗内存会随文件的增长而增长，但是读取时间很快
	 * @throws IOException
	 */
	@Test
	public void testCommonsIoReadAll() throws IOException {
		String path = java.net.URLDecoder.decode(this.getClass().getResource(FileName).getFile(), "utf-8"); 
		logMemory();
		FileUtils.readLines(new File(path), Charsets.UTF_8);
		logMemory();
		
		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 171 Mb
		Max Memory: 2631 Mb
		Total Memory: 309 Mb
		Free Memory: 210 Mb*/

		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 171 Mb
		Max Memory: 2631 Mb
		Total Memory: 1097 Mb
		Free Memory: 210 Mb*/
		//400M:timespan-4.287s
	}
	
	/**
	 * Commons IO库一行一行读取
	 * @throws IOException
	 * 由于整个文件不是全部存放在内存中，这也就导致相当保守的内存消耗：（大约消耗了150MB内存）
	 */
	@Test
	public void testCommonsIdReadLine() throws IOException {		
		String path = java.net.URLDecoder.decode(this.getClass().getResource(FileName).getFile(), "utf-8"); 
		logMemory();
		LineIterator it = FileUtils.lineIterator(new File(path), "UTF-8");
		try {
			while (it.hasNext()) {				
				String line = it.nextLine();
			}
		} finally{
			LineIterator.closeQuietly(it);
		}
		logMemory();
		
		/*Max Memory: 2631 Mb
		Total Memory: 178 Mb
		Free Memory: 170 Mb
		Max Memory: 2631 Mb
		Total Memory: 305 Mb
		Free Memory: 141 Mb*/
		//400M:timespan-1.950s
	}
	
	private final void logMemory() {
		//返回 Java 虚拟机试图使用的最大内存量，以字节为单位
		System.out.println(String.format("Max Memory: %s Mb", Runtime.getRuntime().maxMemory() / 1048576));
		//返回 Java 虚拟机中的内存总量，以字节为单位
		System.out.println(String.format("Total Memory: %s Mb", Runtime.getRuntime().totalMemory() / 1048576));
		//返回 Java 虚拟机中的空闲内存量。调用 gc 方法可能导致 freeMemory 返回值的增加。以字节为单位。
		System.out.println(String.format("Free Memory: %s Mb", Runtime.getRuntime().freeMemory() / 1048576));
	}
}
