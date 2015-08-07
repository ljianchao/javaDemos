package cn.jc.Utils;

import org.junit.Test;

public class testQRCode {

	@Test
	public void testGenerateQRCode(){
		String content = "http://m.damai.cn/proj.aspx?id=123456";
		int width = 265;
		int height = 265;
		String imgPath = "E:/picture/1.jpg";
		
		QRCodeUtils.encoderQRCode(content, width, height, imgPath);
	}
}
