package main.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Region {
	
	private ArrayList<Pixel> pixels;
	private Color color;
	private static ArrayList<ArrayList<Region>> equivalents = new ArrayList<ArrayList<Region>>();
	
	public Region(Color color) {
		pixels = new ArrayList<Pixel>();
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void addPixel(int x, int y, boolean isBorder) {
		Pixel pixel = new Pixel(new Point(x, y), isBorder);
		pixels.add(pixel);
	}
	
	public void addPixels(ArrayList<Pixel> pixels) {
		this.pixels.addAll(pixels);
	}
	
	public Pixel getPixel(int i) {
		return pixels.get(i);
	}
	
	public ArrayList<Pixel> getPixels() {
		return pixels;
	}
	
	public int getCountPixels() {
		return pixels.size();
	}
	
	static void addEquivalent(Region r1, Region r2) {
		ArrayList<Region> equivalent = new ArrayList<Region>(Arrays.asList(r1, r2));
		if(!equivalents.contains(equivalent))
			equivalents.add(equivalent);
	}
	
	static void resolveEquivalents(HashMap<Color, Region> regions) {
		int i, j;
		
		for(i = 0; i < equivalents.size(); i++) {
			Region reg1 = equivalents.get(i).get(0);
			Region reg2 = equivalents.get(i).get(1);
			reg2.addPixels(reg1.pixels);
			for(j = i + 1; j < equivalents.size(); j++) {
				ArrayList<Region> equivalent = equivalents.get(j);
				if(equivalent.get(0).equals(reg1))
					equivalent.set(0, reg2);
			}
			if(!regions.containsValue(reg2))
				regions.put(reg2.getColor(), reg2);
			for(j = i + 1; j < equivalents.size(); j++) {
				ArrayList<Region> equivalent = equivalents.get(j);
				if(equivalent.get(1).equals(reg1))
					equivalent.set(1, reg2);
			}
			if(!reg1.equals(reg2))
				regions.remove(reg1.getColor());
		}
		equivalents.clear();
	}
	
	public int getPerimeter() {
		int p = 0;
		
		for(Pixel pixel : pixels) {
			if(pixel.isBorder())
				p++;
		}
		return p;
	}
	
	public int getArea() {
		return pixels.size();
	}
	
	public double getCompact() {
		double p = getPerimeter(), a = getArea();
		
		return p * p / a;
	}
	
	public Pixel getCenterMass() {
		int sumX = 0, sumY = 0, a = getArea();
		
		for(Pixel pixel : pixels) {
			sumX += pixel.getPoint().x;
			sumY += pixel.getPoint().y;
		}
		return new Pixel(new Point(sumX / a, sumY / a), false);
	}
	
	public double getElongation() {
		double elongation, m20 = getDiscreteCentralMoment(2, 0), m02 = getDiscreteCentralMoment(0, 2), m11 = getDiscreteCentralMoment(1, 1);
		
		elongation = (m20 + m02 + Math.sqrt(Math.pow(m20 - m02, 2) + 4 * m11 * m11)) / (m20 + m02 - Math.sqrt(Math.pow(m20 - m02, 2) + 4 * m11 * m11));
		return elongation;
	}
	
	private double getDiscreteCentralMoment(int i, int j) {
		int mij = 0;
		Pixel pixelCm = getCenterMass();
		
		for(Pixel pixel : pixels)
			mij += Math.pow(pixel.getPoint().x - pixelCm.getPoint().x, i) * Math.pow(pixel.getPoint().y - pixelCm.getPoint().y, j);
		return mij;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Region reg = (Region)obj;
		if(color == null)
			return reg.color == null;
		return color.equals(reg.color);
	}
	
	@Override
	public int hashCode() {
		return 31 * ((color == null) ? 0 : color.hashCode()) + ((pixels == null) ? 0 : pixels.hashCode());
	}
	
}
