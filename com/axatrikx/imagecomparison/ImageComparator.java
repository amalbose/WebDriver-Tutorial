package com.axatrikx.imagecomparison;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageComparator {

	private static final Color DIFF_COLOR = Color.GREEN;
	private static final String DIFF_FILE = "_Diff";
	private static final String DOT = ".";

	private String diffFileName;

	/**
	 * Compares the given images and returns whether images are same or not.
	 * 
	 * @param baseImage
	 * @param actualImage
	 * @param threshold
	 * @return
	 * @throws Exception
	 */
	public boolean compareImages(File baseImage, File actualImage, int threshold) {
		BufferedImage baseImgBuffer = null, actualImgBuffer = null;
		boolean isDifferent = false;
		try {
			baseImgBuffer = ImageIO.read(baseImage);
			actualImgBuffer = ImageIO.read(actualImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// get mininum height and width
		int h = Math.min(baseImgBuffer.getHeight(), actualImgBuffer.getHeight());
		int w = Math.min(baseImgBuffer.getWidth(), actualImgBuffer.getWidth());

		BufferedImage diffImage = cloneBufferedImage(baseImgBuffer);
		Graphics2D gc = diffImage.createGraphics();
		gc.setColor(DIFF_COLOR);

		int redDiff, blueDiff, greenDiff;
		int[] baseImgArr, actualImgArr;
		Color baseColor, actualColor;
		for (int y = 0; y < h - 1; y++) {
			for (int x = 0; x < w - 1; x++) {
				baseImgArr = baseImgBuffer.getRGB(x, y, 1, 1, null, 0, 1);
				actualImgArr = actualImgBuffer.getRGB(x, y, 1, 1, null, 0, 1);
				baseColor = new Color(baseImgArr[0]);
				actualColor = new Color(actualImgArr[0]);
				redDiff = actualColor.getRed() - baseColor.getRed();
				blueDiff = actualColor.getBlue() - baseColor.getBlue();
				greenDiff = actualColor.getGreen() - baseColor.getGreen();
				if ((Math.abs(redDiff - blueDiff) > threshold) || (Math.abs(redDiff - greenDiff) > threshold)
						|| (Math.abs(greenDiff - blueDiff) > threshold)) {
					gc.drawLine(x, y, x + 1, y + 1);
					isDifferent = true;
				}
			}
		}
		// get diff file name
		String[] fileNameArr = stripExtension(actualImage.getAbsolutePath());
		diffFileName = fileNameArr[0] + DIFF_FILE + DOT + fileNameArr[1];
		saveFile(diffImage, diffFileName);
		return isDifferent;
	}

	/**
	 * Returns the difference file name.
	 * 
	 * @return
	 */
	public String getDiffFileName() {
		return diffFileName;
	}

	/**
	 * Clones the buffered image.
	 * 
	 * @param bi
	 * @return
	 */
	private BufferedImage cloneBufferedImage(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Save the buffered image
	 * 
	 * @param img
	 * @param filename
	 */
	private void saveFile(BufferedImage img, String filename) {
		BufferedImage bi = cloneBufferedImage(img);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filename);
		} catch (java.io.FileNotFoundException io) {
		}
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
		param.setQuality(0.8f, false);
		encoder.setJPEGEncodeParam(param);
		try {
			encoder.encode(bi);
			out.close();
		} catch (java.io.IOException io) {
		}
	}

	/**
	 * Split the filename to simple name and extension
	 * 
	 * @param fileName
	 * @return
	 */
	private static String[] stripExtension(String fileName) {
		if (fileName == null || fileName.lastIndexOf(".") == -1)
			return null;
		return fileName.split("\\.(?=[^\\.]+$)");
	}

	public static void main(String[] args) {
		new ImageComparator().compareImages(new File("test1.jpg"), new File("test2.jpg"), 10);
	}
}
