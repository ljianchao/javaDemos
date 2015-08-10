package cn.jc.Utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成的工具类
 * 
 * @author liujianchao，2015-8-7
 *
 */
public class QRCodeUtils {
	
    private static final int BLACK = 0xFF000000;  
    private static final int WHITE = 0xFFFFFFFF;  	   
    
	/**
	 * 设置二维码的格式参数
	 * @param margin，指定边框
	 * @return
	 */
	private static Map<EncodeHintType, Object> getHintType(Integer margin) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);  
        // 指定编码格式  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        if (margin != null) {
        	// 指定边框
            hints.put(EncodeHintType.MARGIN, margin.intValue());
		}
        
        
		return hints;
	}
	
    /**
     * 用RGB重绘二维码图像
     * @param matrix
     * @return
     */
	private static BufferedImage toBufferedImage(BitMatrix matrix){  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
          
        for(int x=0;x<width;x++){  
            for(int y=0;y<height;y++){  
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);  
            }  
        }  
        return image;     
    } 
	
	/**
	 * 生成含有logo的BufferedImage
	 * @param contents
	 * @param width
	 * @param height
	 * @param margin
	 * @param logoPath
	 * @param newLogoWidth，如果newLogoWidth和newLogoHeight都大于0，logo将按新的尺寸绘制
	 * @param newLogoHeight
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	private static BufferedImage encoderQRCodeWithLogo(String contents, int width, int height,Integer margin, 
			String logoPath, int newLogoWidth, int newLogoHeight) throws WriterException, IOException {
		File logoFile = new File(logoPath);
		if (!logoFile.exists()) {
			return null;
		}
  
		//绘制二维码图像
		BufferedImage qRCodeImage = encoderQRCodeToBufferImage(contents, width, height, margin);

		Graphics2D g = qRCodeImage.createGraphics();
		
		//读取Logo图片		
		BufferedImage logoImage = ImageIO.read(logoFile);
		
		//计算图片绘制位置
		int logoWidth,logoHeight;
		
		if (newLogoWidth > 0 && newLogoHeight > 0) {
			logoWidth = newLogoWidth;
			logoHeight = newLogoHeight;
		}else{
			logoWidth = logoImage.getWidth();
			logoHeight = logoImage.getHeight();
		}
		
		int x = (qRCodeImage.getWidth() - logoWidth) / 2;
		int y = (qRCodeImage.getHeight() - logoHeight) / 2;		
		
		//绘制logo到二维码中
		g.drawImage(logoImage, x, y, logoWidth, logoHeight,null);
		
		g.dispose();
		logoImage.flush();
		
		return qRCodeImage;
		
	}	
	
	/**
	 * 生成二维码图片
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static void encoderQRCode(String contents, int width, int height,
			String imgPath) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = getHintType(null);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
				BarcodeFormat.QR_CODE, width, height, hints);

		MatrixToImageWriter.writeToFile(bitMatrix, "jpg",
				new File(imgPath));
	}
	
	/**
	 * 输出二维码文件流
	 * @param contents
	 * @param width
	 * @param height
	 * @param output
	 * @throws IOException 
	 * @throws WriterException 
	 */
	public static void encoderQRCode(String contents, int width, int height, OutputStream output) throws WriterException, IOException{
        encoderQRCode(contents, width, height,  null, output);
	}
	
	/**
	 * 输出二维码文件流
	 * @param contents
	 * @param width
	 * @param height
	 * @param margin，指定边框
	 * @param output
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static void encoderQRCode(String contents, int width, int height,Integer margin, OutputStream output) throws WriterException, IOException{
        Map<EncodeHintType, Object> hints = getHintType(margin);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,  
                BarcodeFormat.QR_CODE, width, height, hints);  

        MatrixToImageWriter  
                .writeToStream(bitMatrix, "jpg", output); 
	}
	
	/**
	 * 获取生成二维码的BufferedImage
	 * @param contents
	 * @param width
	 * @param height
	 * @return
	 * @throws WriterException 
	 */
	private static BufferedImage encoderQRCodeToBufferImage(String contents, int width, int height,Integer margin) throws WriterException {
		BufferedImage bufferedImage = null;
		Map<EncodeHintType, Object> hints = getHintType(margin);
		
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, 
				BarcodeFormat.QR_CODE, width, height, hints);
		//RGB类型重绘二维码		
		bufferedImage = toBufferedImage(bitMatrix);
		
		return bufferedImage;
	}
	
	
	/**
	 * 生成含有logo的二维码图片，并保存到本地
	 * @param contents
	 * @param width
	 * @param height
	 * @param margin
	 * @param logoPath
	 * @param newLogoWidth
	 * @param newLogoHeight
	 * @param formatName
	 * @param saveFilePath
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void encoderQRCodeWithLogoToFile(String contents, int width, int height,Integer margin, 
			String logoPath, int newLogoWidth, int newLogoHeight,String formatName, String saveFilePath) throws WriterException, IOException{
		BufferedImage qRCodeImage = encoderQRCodeWithLogo(contents, width, height, margin, logoPath, newLogoWidth, newLogoHeight);
		if (qRCodeImage == null) {
			throw new IOException("logo图片不存在");
		}
		
		if (!ImageIO.write(qRCodeImage, formatName, new File(saveFilePath))) {
			throw new IOException("将二维码图片保存到本地失败");
		}
	}

	/**
	 * 生成含有logo的二维码图片，并保存到输出流中
	 * @param contents
	 * @param width
	 * @param height
	 * @param margin
	 * @param logoPath
	 * @param newLogoWidth
	 * @param newLogoHeight
	 * @param formatName
	 * @param outputStream
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void encoderQRCodeWithLogoToOutputStream(String contents, int width, int height,Integer margin, 
			String logoPath, int newLogoWidth, int newLogoHeight,String formatName, OutputStream outputStream) throws WriterException, IOException {
		BufferedImage qRCodeImage = encoderQRCodeWithLogo(contents, width, height, margin, logoPath, newLogoWidth, newLogoHeight);
		if (qRCodeImage == null) {
			throw new IOException("logo图片不存在");
		}
		
		if (!ImageIO.write(qRCodeImage, "jpg", outputStream)) {
			throw new IOException("将二维码图片生成输出流失败");
		}
	}
}

