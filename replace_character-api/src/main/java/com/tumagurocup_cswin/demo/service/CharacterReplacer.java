package com.tumagurocup_cswin.demo.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.opencv.core.CvType;
import org.springframework.stereotype.Service;

@Service
public class CharacterReplacer {
	public Mat ConvertByteToMat(int width, int height, byte[] bytes)
	{
		return opencv_imgcodecs.imdecode(new Mat(new BytePointer(bytes)), opencv_imgcodecs.IMREAD_ANYCOLOR);
	}
	
	public List<Rect> CreateRectList(List<Integer> x, List<Integer> y, List<Integer> width, List<Integer> height){
		List<Rect> rectList = new ArrayList<Rect>();
		for(int i = 0; i < x.size(); i++) {
			rectList.add(new Rect(x.get(i), y.get(i), width.get(i), height.get(i)));
		}
		return rectList;
	}
	
	public byte[] ReplaceCharacter(int size, List<String> textList, List<Rect> rectList, Mat mat)
    {
        for (int i = 0; i < rectList.size(); i++)
		{
            DrawingText(mat, textList.get(i), rectList.get(i));
		}
        
        byte[] byteArray = new byte[size];
        opencv_imgcodecs.imencode(".png", mat, byteArray);
        
        return byteArray;
    }
	
    //画像の文字列の位置に翻訳した文字を重ねるメソッド
    private void DrawingText(Mat mat, String text, Rect lineRect)
    {
    	BufferedImage img = new BufferedImage(lineRect.width(), lineRect.height(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		//文字色
		g.setColor(Color.green);
		g.fillRect(0, 0, lineRect.width(), lineRect.height());
		g.setColor(Color.black);
		g.setFont(new Font("Serif", Font.PLAIN, 18));
		g.drawString(text, 0, lineRect.height() - 3);
		g.dispose();
		
		Mat overlayMat = ConvertBufferedImageToMat(img);
		overlayMat.copyTo(mat.apply(lineRect));
    }
    
    private Mat ConvertBufferedImageToMat(BufferedImage image) {
    	// 各画素のRGB色情報を抽出
        // intは4byteなので、各byteがそれぞれRGBの各要素に対応している
        int[] rgbArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        // int配列をbyte配列に変換する
        // その際、Mat型で読み込めるようにRGBの並びをBGRに変換する
        byte[] bgrArray = new byte[rgbArray.length * 3];
        for (int i = 0; i < rgbArray.length; i++) {
            bgrArray[i * 3 + 0] = (byte) ((rgbArray[i] >> 0) & 0xFF); // B
            bgrArray[i * 3 + 1] = (byte) ((rgbArray[i] >> 8) & 0xFF); // G
            bgrArray[i * 3 + 2] = (byte) ((rgbArray[i] >> 16) & 0xFF); // R
        }

        // Mat形式取得
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3, new BytePointer(bgrArray));
        return mat;
      }
}
