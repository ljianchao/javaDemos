package cn.jc.Utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成的工具类
 * 
 * @author liujianchao，2015-8-7
 *
 */
public class QRCodeUtils {

	/**
	 * 生成二维码图片
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encoderQRCode(String content, int width, int height,
			String imgPath) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, width, height, hints);

			MatrixToImageWriter.writeToFile(bitMatrix, "jpg",
					new File(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出文件流
	 * @param contents
	 * @param width
	 * @param height
	 * @param output
	 */
	public static void encoderQRCode(String content, int width, int height, OutputStream output){
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        // 指定纠错等级  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  
        // 指定编码格式  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
        try {  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                    BarcodeFormat.QR_CODE, width, height, hints);  
  
            MatrixToImageWriter  
                    .writeToStream(bitMatrix, "jpg", output);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}
	
	public static void encoderQRCode(String content, int width, int height,int margin, OutputStream output){
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        // 指定纠错等级  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  
        // 指定编码格式  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        //指定边框
        hints.put(EncodeHintType.MARGIN, margin);
        try {  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                    BarcodeFormat.QR_CODE, width, height, hints);  
  
            MatrixToImageWriter  
                    .writeToStream(bitMatrix, "jpeg", output);  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
	}

//	/**
//     * 生成二维码Bitmap
//     *
//     * @param content   内容
//     * @param widthPix  图片宽度
//     * @param heightPix 图片高度
//     * @param logoBm    二维码中心的Logo图标（可以为null）
//     * @param filePath  用于存储二维码图片的文件路径
//     * @return 生成二维码及保存文件是否成功
//     */
//    public static boolean createQRImage(String content, int widthPix, int heightPix, BufferedImage logoBm, String filePath) {
//        try {
//            if (content == null || "".equals(content)) {
//                return false;
//            }
// 
//            //配置参数
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            //容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            //设置空白边距的宽度
////            hints.put(EncodeHintType.MARGIN, 2); //default is 4
// 
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
//					BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
// 
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
// 
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
// 
//            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
//            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
//        } catch (WriterException | IOException e) {
//            e.printStackTrace();
//        }
// 
//        return false;
//    }
 
    /**
     * 在二维码中间添加Logo图案
     */
//    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
//        if (src == null) {
//            return null;
//        }
// 
//        if (logo == null) {
//            return src;
//        }
// 
//        //获取图片的宽高
//        int srcWidth = src.getWidth();
//        int srcHeight = src.getHeight();
//        int logoWidth = logo.getWidth();
//        int logoHeight = logo.getHeight();
// 
//        if (srcWidth == 0 || srcHeight == 0) {
//            return null;
//        }
// 
//        if (logoWidth == 0 || logoHeight == 0) {
//            return src;
//        }
// 
//        //logo大小为二维码整体大小的1/5
//        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//        try {
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(src, 0, 0, null);
//            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
// 
//            canvas.save(Canvas.ALL_SAVE_FLAG);
//            canvas.restore();
//        } catch (Exception e) {
//            bitmap = null;
//            e.getStackTrace();
//        }
// 
//        return bitmap;
//    }
	
	/**
	 * 设置二维码的格式参数
	 * @return
	 */
	public Map<EncodeHintType, Object> getDecodeHintType() {
		// 用于设置QR二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MAX_SIZE, 350);
		hints.put(EncodeHintType.MIN_SIZE, 100);

		return hints;
	}
	
	/**
	 * 二维码的数据信息初始化
	 * @param bm
	 * @return
	 */
	public BufferedImage fileToBufferedImage(BitMatrix bm) {
		BufferedImage image = null;
		try {
			int w = bm.getWidth(), h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 生成二维码bufferedImage图片
	 * 
	 * @param content
	 *            编码内容
	 * @param barcodeFormat
	 *            编码类型
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param hints
	 *            设置参数
	 * @return
	 */
	public BufferedImage getQR_CODEBufferedImage(String content,
			BarcodeFormat barcodeFormat, int width, int height,
			Map<EncodeHintType, ?> hints) {
		MultiFormatWriter multiFormatWriter = null;
		BitMatrix bm = null;
		BufferedImage image = null;
		try {
			multiFormatWriter = new MultiFormatWriter();

			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			bm = multiFormatWriter.encode(content, barcodeFormat, width,
					height, hints);

			int w = bm.getWidth();
			int h = bm.getHeight();
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
				}
			}
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * 将二维码生成为输出流
	 * 
	 * @param content
	 * @param imageFormat
	 * @param os
	 */
	public void decodeQR_CODE2OutputStream(BitMatrix bm, String imageFormat,
			OutputStream os) {
		try {
			BufferedImage image = fileToBufferedImage(bm);
			ImageIO.write(image, imageFormat, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* 给二维码图片添加Logo
	* 
	* @param qrPic
	* @param logoPic
	*/
	public void addLogo_QRCode(File qrPic, File logoPic, LogoConfig logoConfig) {
		try {
			if (!qrPic.isFile() || !logoPic.isFile()) {
				System.out.print("file not find !");
				System.exit(0);
			}

			/**
			 * 读取二维码图片，并构建绘图对象
			 */
			BufferedImage image = ImageIO.read(qrPic);
			Graphics2D g = image.createGraphics();

			/**
			 * 读取Logo图片
			 */
			BufferedImage logo = ImageIO.read(logoPic);

			int widthLogo = logo.getWidth(), heightLogo = logo.getHeight();

			// 计算图片放置位置
			int x = (image.getWidth() - widthLogo) / 2;
			int y = (image.getHeight() - logo.getHeight()) / 2;

			// 开始绘制图片
			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
			g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
			g.setStroke(new BasicStroke(logoConfig.getBorder()));
			g.setColor(logoConfig.getBorderColor());
			g.drawRect(x, y, widthLogo, heightLogo);

			g.dispose();

			ImageIO.write(image, "jpeg", new File("D:/newPic.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
	
	/**
	* 二维码的解析
	* 
	* @param file
	*/
	public void parseQR_CODEImage(File file) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();

			// File file = new File(filePath);
			if (!file.exists()) {
				return;
			}

			BufferedImage image = ImageIO.read(file);

			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			Result result = formatReader.decode(binaryBitmap, hints);

			System.out.println("result = " + result.toString());
			System.out.println("resultFormat = " + result.getBarcodeFormat());
			System.out.println("resultText = " + result.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class LogoConfig {
	// logo默认边框颜色
	public static final Color DEFAULT_BORDERCOLOR = Color.WHITE;
	// logo默认边框宽度
	public static final int DEFAULT_BORDER = 2;
	// logo大小默认为照片的1/5
	public static final int DEFAULT_LOGOPART = 5;

	private final int border = DEFAULT_BORDER;
	private final Color borderColor;
	private final int logoPart;

	/**
	 * Creates a default config with on color {@link #BLACK} and off color
	 * {@link #WHITE}, generating normal black-on-white barcodes.
	 */
	public LogoConfig() {
		this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
	}

	public LogoConfig(Color borderColor, int logoPart) {
		this.borderColor = borderColor;
		this.logoPart = logoPart;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorder() {
		return border;
	}

	public int getLogoPart() {
		return logoPart;
	}
}
