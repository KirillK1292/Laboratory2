package main.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Img {
	
	private BufferedImage image;
	
	public Img() {
		
	}
	
	public Img(BufferedImage image)
			throws NullPointerException, ImgException
	{
		if(image.getType() == BufferedImage.TYPE_CUSTOM)
			throw new ImgException("Неизвестный формат изображения.");
		this.image = getCopy(image);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image)
			throws NullPointerException, ImgException
	{
		if(image.getType() == BufferedImage.TYPE_CUSTOM)
			throw new ImgException("Неизвестный формат изображения.");
		this.image = getCopy(image);
	}
	
	public int[] getPixelData(int x, int y)
			throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		int[] a = null;
		a = image.getRaster().getPixel(x, y, a);
		return a;
	}
	
	public void setPixelData(int x, int y, int[] a)
			throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		image.getRaster().setPixel(x, y, a);
	}
	
	public Color getPixelColor(int x, int y)
			throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		Object colorData = image.getRaster().getDataElements(x, y, null);
		return new Color(image.getColorModel().getRGB(colorData));
	}
	
	public void setPixelColor(int x, int y, Color color)
			throws NullPointerException, ArrayIndexOutOfBoundsException
	{
		image.setRGB(x, y, color.getRGB());
	}
	
	public static BufferedImage getCopy(BufferedImage image)
			throws NullPointerException
	{
		return new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
	}
	
	public void thresholdBinarization(int threshold)
			throws NullPointerException, ImgException
	{
		int x, y, Y;
		Img binImage = new Img(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY));
		
		for(y = 0; y < image.getHeight(); y++) {
			for(x = 0; x < image.getWidth(); x++) {
				int[] a = getPixelData(x, y);
				if(a.length == 1)
					Y = a[0];
				else {
					if(a.length == 3)
						Y = Math.round(0.3f * a[0] + 0.59f * a[1] + 0.11f * a[2]);
					else {
						if(a.length == 4)
							Y = Math.round(0.3f * a[0] + 0.588f * a[1] + 0.11f * a[2] + 0.002f * a[3]);
						else
							throw new ImgException("Не удалось получить бинарное изображение.");
					}
				}
				Y = (Y > threshold) ? 1 : 0;
				binImage.setPixelData(x, y, new int[]{ Y });
			}
		}
		image = binImage.getImage();
	}
	
	public void brightnessImage(int brightness)
			throws NullPointerException
	{
		int x, y, k;
		
		for(y = 0; y < image.getHeight(); y++) {
			for(x = 0; x < image.getWidth(); x++) {
				int[] a = getPixelData(x, y);
				for(k = 0; k < a.length; k++) {
					if(a[k] + brightness > 255)
						a[k] = 255;
					else
						a[k] = (a[k] + brightness < 0) ? 0 : a[k] + brightness;
				}
				setPixelData(x, y, a);
			}
		}
	}
	
	public HashMap<Color, Region> sequentialScanning() 
			throws NullPointerException, ImgException
	{
		int x, y;
		Random r = new Random();
		Color A, B, C, label, backgroundColor = new Color(0, 0, 0);
		HashMap<Color, Region> regions = new HashMap<Color, Region>();
		
		if(image.getType() != BufferedImage.TYPE_BYTE_BINARY)
			throw new ImgException("Изображение должно быть бинарным.");
		
		BufferedImage image1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image1.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		image = image1;
		
		for(y = 0; y < image.getHeight(); y++) {
			for(x = 0; x < image.getWidth(); x++) {
				A = getPixelColor(x, y);
				B = (x - 1 < 0) ? backgroundColor : getPixelColor(x - 1, y);
				C = (y - 1 < 0) ? backgroundColor : getPixelColor(x, y - 1);
				if(!A.equals(backgroundColor)) {
					if(B.equals(backgroundColor) && C.equals(backgroundColor)) {
						do {
							label = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
						} while(regions.containsKey(label));
						Region reg = new Region(label);
						reg.addPixel(x, y, true);
						regions.put(label, reg);
						setPixelColor(x, y, label);
					}
					if(!B.equals(backgroundColor) && C.equals(backgroundColor)) {
						regions.get(B).addPixel(x, y, true);
						setPixelColor(x, y, B);
					}
					if(B.equals(backgroundColor) && !C.equals(backgroundColor)) {
						regions.get(C).addPixel(x, y, true);
						setPixelColor(x, y, C);
					}
					if(!B.equals(backgroundColor) && !C.equals(backgroundColor)) {
						if(B.equals(C)) {
							regions.get(C).addPixel(x, y, false);
							setPixelColor(x, y, C);
						} else {
							regions.get(B).addPixel(x, y, false);
							setPixelColor(x, y, B);
							Region.addEquivalent(regions.get(C), regions.get(B));
						}
					}
				}	
			}
		}
		Region.resolveEquivalents(regions);
		for (Color color : regions.keySet()) {
			Region reg = regions.get(color);
			ArrayList<Pixel> pixels = reg.getPixels();
			for(Pixel pixel : pixels)
				setPixelColor(pixel.getPoint().x, pixel.getPoint().y, reg.getColor());
		}
		return regions;
	}

	public void clusteredRegions(int k, HashMap<Color, Region> regions)
			throws NullPointerException
	{
		int i, j, size;

		K_medoids kMedoids = new K_medoids(k, regions);
		Cluster[] clusters = kMedoids.getClusters();
		for(i = 0; i < clusters.length; i++) {
			size = clusters[i].getSizeV();
			Color color = clusters[i].getPoint(0).getColor();
			for(j = 1; j < size; j++) {
				Region reg = regions.get(clusters[i].getPoint(j).getColor());
				reg.setColor(color);
				ArrayList<Pixel> pixels = reg.getPixels();
				for(Pixel pixel : pixels)
					setPixelColor(pixel.getPoint().x, pixel.getPoint().y, color);
			}
		}
	}
	
	private void erosion()
			throws NullPointerException, ImgException
	{
		int x, y;
		Img img1 = new Img(new BufferedImage(image.getWidth() - 2, image.getHeight() - 2, BufferedImage.TYPE_BYTE_BINARY));
		
		for(y = 1; y < image.getHeight() - 1; y++) {
			for(x = 1; x < image.getWidth() - 1; x++) {
				int[] a1 = getPixelData(x - 1, y - 1);
				int[] a2 = getPixelData(x, y - 1);
				int[] a3 = getPixelData(x + 1, y - 1);				
				int[] a4 = getPixelData(x - 1, y);
				int[] a5 = getPixelData(x, y);
				int[] a6 = getPixelData(x + 1, y);				
				int[] a7 = getPixelData(x - 1, y + 1);
				int[] a8 = getPixelData(x, y + 1);
				int[] a9 = getPixelData(x + 1, y + 1);
				if(a1[0] + a2[0] + a3[0] + a4[0] + a5[0] + a6[0] + a7[0] + a8[0] + a9[0] == 9)
					img1.setPixelData(x - 1, y - 1, a1);
			}
		}
		image = img1.getImage();
	}
	
	private void dilation()
			throws NullPointerException, ImgException
	{
		int x, y;
		Img img1 = new Img(new BufferedImage(image.getWidth() + 2, image.getHeight() + 2, BufferedImage.TYPE_BYTE_BINARY));
		
		for(y = 0; y < image.getHeight(); y++) {
			for(x = 0; x < image.getWidth(); x++) {
				int[] a = getPixelData(x, y);
				if(a[0] == 1) {
					img1.setPixelData(x, y, a);
					img1.setPixelData(x + 1, y, a);
					img1.setPixelData(x + 2, y, a);					
					img1.setPixelData(x, y + 1, a);
					img1.setPixelData(x + 1, y + 1, a);
					img1.setPixelData(x + 2, y + 1, a);					
					img1.setPixelData(x, y + 2, a);
					img1.setPixelData(x + 1, y + 2, a);
					img1.setPixelData(x + 2, y + 2, a);
				}
			}
		}
		image = img1.getImage();
	}
	
	public void closing()
			throws NullPointerException, ImgException
	{
		if(image.getType() != BufferedImage.TYPE_BYTE_BINARY)
			throw new ImgException("Изображение должно быть бинарным.");
		dilation();
		erosion();		
	}
	
	public void opening()
			throws NullPointerException, ImgException
	{
		if(image.getType() != BufferedImage.TYPE_BYTE_BINARY)
			throw new ImgException("Изображение должно быть бинарным.");
		erosion();
		dilation();
	}
	
	public void medianFilter()
			throws NullPointerException, ImgException
	{
		int x, y;
		
		if(image.getType() != BufferedImage.TYPE_BYTE_BINARY)
			throw new ImgException("Изображение должно быть бинарным.");
		
		BufferedImage image1 = new BufferedImage(image.getWidth() + 6, image.getHeight() + 6, image.getType());  
		Graphics g = image1.getGraphics();		
		g.drawImage(image, 0, 0, image1.getWidth(), image1.getHeight(), null);
		g.drawImage(image, 3, 3, image.getWidth(), image.getHeight(), null);
		g.dispose();
		Img img1 = new Img(image1);
		for(y = 3; y < image1.getHeight() - 3; y++) {
			for(x = 3; x < image1.getWidth() - 3; x++)
				setPixelData(x - 3, y - 3, img1.getFilteredPixel(x - 3, y - 3));
		}	
	}
			
	private int[] getFilteredPixel(int x, int y) {
		List<Integer> v = new ArrayList<Integer>(49);
		int i, j;
		
		for(i = 0; i < 7; i++, y++) {
			for(j = 0; j < 7; j++) {
				int[] a = getPixelData(x + j, y);
				v.add(a[0]);
			}
		}
		Collections.sort(v);
		return new int[]{ v.get(v.size() / 2) };
	}
	
}
