package com.fp2p.deprecated;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageMarkLogoByIcon {
	  /**   
     * 把图片印刷到图片上   
     *    
     * @param pressImg --   
     *            水印文件   
     * @param targetImg --   
     *            目标文件   
     * @param x   
     * @param y   
     */   
    public final static void pressImage(String pressImg, String targetImg,    
            int x, int y) {    
        try {    
            File _file = new File(targetImg);    
            Image src = ImageIO.read(_file);    
            int wideth = src.getWidth(null);    
            int height = src.getHeight(null);    
            BufferedImage image = new BufferedImage(wideth, height,    
                    BufferedImage.TYPE_INT_RGB);    
            Graphics g = image.createGraphics();    
            g.drawImage(src, 0, 0, wideth, height, null);    
   
            // 水印文件    
            File _filebiao = new File(pressImg);    
            Image src_biao = ImageIO.read(_filebiao);    
            int wideth_biao = src_biao.getWidth(null);    
            int height_biao = src_biao.getHeight(null);    
            g.drawImage(src_biao, wideth - wideth_biao - x, height    
                    - height_biao - y, wideth_biao, height_biao, null);    
            // /    
            g.dispose();    
            FileOutputStream out = new FileOutputStream(targetImg);    
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);    
            encoder.encode(image);    
            out.close();    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
   
    /**   
     * 打印文字水印图片   
     *    
     * @param pressText   
     *            --文字   
     * @param targetImg --   
     *            目标图片   
     * @param fontName --   
     *            字体名   
     * @param fontStyle --   
     *            字体样式   
     * @param color --   
     *            字体颜色   
     * @param fontSize --   
     *            字体大小   
     * @param x --   
     *            偏移量   
     * @param y   
     */   
   
    public static void pressText(String pressText,String sourceImg, String targetImg,    
            String fontName, int fontStyle,String color, int fontSize, int x,    
            int y) {
		OutputStream os = null;
        try {
            Image src = ImageIO.read(new File(sourceImg));    
            int wideth = src.getWidth(null);    
            int height = src.getHeight(null);    
            BufferedImage image = new BufferedImage(wideth, height,    
                    BufferedImage.TYPE_INT_RGB);    
            Graphics2D g = image.createGraphics();
         // 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(src.getScaledInstance(src.getWidth(null),src.getHeight(null), Image.SCALE_SMOOTH), 0, 0,null);
            g.setColor(Color.decode(color));    
            g.setFont(new Font(fontName, fontStyle, fontSize));    
            float alpha = 0.5f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
//            g.drawString(pressText, wideth - fontSize - x, height - fontSize    
//                    / 2 - y);  
            g.drawString(pressText, 150, 200);
            g.dispose();    
            os = new FileOutputStream(targetImg);
			// 生成图片
			ImageIO.write(image, "JPG", os);
        } catch (Exception e) {    
            System.out.println(e);    
        }finally{
        	try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }    
    }    
   
    /**
	 * 给图片添加水印、可设置水印的旋转角度
	 * 
	 * @param logoText
	 * @param srcImgPath
	 * @param targerPath
	 * @param degree
	 */
	public static void markByText(String logoText, String srcImgPath,String targerPath, Integer degree) {
		// 主图片的路径
//		InputStream is = null;
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// 得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null),	srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,null);

//			if (null != degree) {
//				// 设置水印旋转
//				g.rotate(Math.toRadians(degree),(double) buffImg.getWidth() / 2,(double) buffImg.getHeight() / 2);
//			}

			// 设置颜色
			g.setColor(Color.BLUE);

			// 设置 Font
			g.setFont(new Font("宋体", Font.BOLD, 30));

			float alpha = 0.5f;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
					alpha));

			// 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
			g.drawString(logoText, 150, 200);
			//g.drawString(logoText, 100, 130);
			//g.drawString(logoText, 200, 200);
			//g.drawString(logoText, 290, 400);

			g.dispose();

			os = new FileOutputStream(targerPath);

			// 生成图片
			ImageIO.write(buffImg, "JPG", os);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
    public static void main(String[] args) {
//    	markByText("", "d:/aa.jpg", "d:/xiao.jpg", 10);
//        pressImage("d:/bug.png", "d:/img_mark_icon.jpg", 20, 20);
        pressText("富壹代互联网金融", "d:/aa.jpg","d:/xiao.jpg", "楷体", Font.BOLD, "#002EB8", 30, 120, 70);
    } 
}
