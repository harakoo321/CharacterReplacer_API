package com.tumagurocup_cswin.demo.service;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.opencv.core.CvType;
import org.springframework.stereotype.Service;

@Service
public class CharacterReplacer {
	public Mat ConvertByteToMat(byte[] bytes)
	{
		return opencv_imgcodecs.imdecode(new Mat(bytes), opencv_imgcodecs.IMREAD_COLOR);
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
		/*
        for (int i = 0; i < rectList.size(); i++)
		{
			DrawingRectangle(mat, rectList.get(i));
            DrawingText(mat, textList.get(i), rectList.get(i));
		}
		*/
        
        byte[] byteArray = new byte[size];
        opencv_imgcodecs.imencode(".png", mat, byteArray);
        
        return byteArray;
    }

    //画像の文字列の位置を緑の長方形で塗りつぶすメソッド
    private void DrawingRectangle(Mat mat, Rect lineRect)
    {
        opencv_imgproc.rectangle(mat, lineRect, new Scalar(0, 255, 0, 0), -1, opencv_imgproc.FILLED, 0);
    }

    //画像の文字列の位置に翻訳した文字を重ねるメソッド
    private void DrawingText(Mat mat, String text, Rect lineRect)
    {
    	BufferedImage img = new BufferedImage(lineRect.width(), lineRect.height(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		//文字色
		g.setColor(Color.BLACK);
		g.drawString(text, 0, 0);
		g.dispose();
		
		Mat overlayMat = ConvertBufferedImageToMat(img);
		overlayMat.copyTo(mat.apply(lineRect));
    }
    
    private Mat ConvertBufferedImageToMat(BufferedImage image) {
    	// 各画素のARGB色情報を抽出
        // intは4byteなので、各byteがそれぞれARGBの各要素に対応している
        int[] argbArray = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        // int配列をbyte配列に変換する
        // その際、Mat型で読み込めるようにARGBの並びをBGRAに変換する
        byte[] bgraArray = new byte[argbArray.length * 4];
        for (int i = 0; i < argbArray.length; i++) {
            bgraArray[i * 4 + 0] = (byte) ((argbArray[i] >> 0) & 0xFF); // B
            bgraArray[i * 4 + 1] = (byte) ((argbArray[i] >> 8) & 0xFF); // G
            bgraArray[i * 4 + 2] = (byte) ((argbArray[i] >> 16) & 0xFF); // R
            bgraArray[i * 4 + 3] = (byte) ((argbArray[i] >> 24) & 0xFF); // A
        }

        // Mat形式取得
        return new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC4, new BytePointer(bgraArray));
      }
}
