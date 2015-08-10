package cn.jc.Utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.junit.Test;

import com.google.zxing.WriterException;

public class testQRCode {

	@Test
	public void testGenerateQRCode() throws UnsupportedEncodingException{
		String contents = "http://m.damai.cn/proj.aspx?id=84434";
		int width = 265;
		int height = 265;
		String logoName = "center.gif";
//		QRCodeUtils.encoderQRCode(contents, width, height, imgPath);
//		URL url2 = this.getClass().getResource("");
//		URL url = this.getClass().getResource("center.gif");
		File file = new File(java.net.URLDecoder.decode(this.getClass().getResource("").getPath(),"utf-8"));
		String logoPath = String.format("%s\\%s", file.toString(), logoName);
		try {
			QRCodeUtils.encoderQRCodeWithLogoToFile(contents, width, height, 0, logoPath, 65, 65, "jpg", "E:/picture/QRCode.jpg");
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
