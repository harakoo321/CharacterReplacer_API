package com.tumagurocup_cswin.demo.service;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

@Service
public class CharacterReplacer {
	public Mat ConvertByteToMat(byte[] bytes)
	{
		return Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED);
	}
	
	public List<Rect> CreateRectList(List<Integer> x, List<Integer> y, List<Integer> width, List<Integer> height){
		List<Rect> rectList = new ArrayList<Rect>();
		for(int i = 0; i < x.size(); i++) {
			rectList.add(new Rect(x.get(i), y.get(i), width.get(i), height.get(i)));
		}
		return rectList;
	}
	
	public byte[] ReplaceCharacter(List<String> textList, List<Rect> rectList, Mat mat)
    {
        for (int i = 0; i < rectList.size(); i++)
		{
			DrawingRectangle(mat, rectList.get(i));
            DrawingText(mat, textList.get(i), rectList.get(i));
		}
        
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        
        return byteArray;
    }

    //画像の文字列の位置を緑の長方形で塗りつぶすメソッド
    private void DrawingRectangle(Mat mat, Rect lineRect)
    {
        Imgproc.rectangle(mat, new Point(lineRect.x, lineRect.y), new Point((lineRect.x + lineRect.width), (lineRect.y + lineRect.height)), new Scalar(0, 255, 0), -1);
    }

    //画像の文字列の位置に翻訳した文字を重ねるメソッド
    private void DrawingText(Mat mat, String text, Rect lineRect)
    {
    	BufferedImage img = new BufferedImage(lineRect.width, lineRect.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = img.getGraphics();
		//文字色
		g.setColor(Color.BLACK);
		g.drawString(text, 0, 0);
		g.dispose();
		
		Mat smat = mat.submat(lineRect);
		smat = ConvertBufferedImageToMat(img);
    }
    
    private Mat ConvertBufferedImageToMat(BufferedImage image) {
        // バイト列生成
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try {
          // 画像データをバイト列に書き出す
          ImageIO.write(image, "png", bout);
          // バイト列を閉じる
          bout.close();
        } catch (IOException e) {
          System.out.println("🖼 画像データのOpenCVへの取り込みに失敗しました：" + e);
          // 問題があれば終了
          return null;
        }

        // バイト列からOpenCVのMatを生成
        MatOfByte mb = new MatOfByte(bout.toByteArray());

        // Mat形式取得
        return Imgcodecs.imdecode(mb, Imgcodecs.IMREAD_COLOR);
      }
}
