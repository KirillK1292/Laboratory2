package main.model;

import java.awt.Point;

public class Pixel {
	
	private Point point;
	private boolean isBorder;
	
	public Pixel(Point point, boolean isBorder) {
		this.point = point;
		this.isBorder = isBorder;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	public boolean isBorder() {
		return isBorder;
	}
	
	public void setBorder(boolean isBorder) {
		this.isBorder = isBorder;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Pixel pixel = (Pixel)obj;
		if(isBorder != pixel.isBorder)
			return false;
		if(point == null)
			return pixel.point == null;
		return point.equals(pixel.point);
	}
	
	@Override
	public int hashCode() {
		return 31 * ((point == null) ? 0 : point.hashCode()) + ((isBorder) ? 1 : 0);
	}
	
	@Override
	public String toString() {
		return "x= " + point.x + " y= " + point.y;
	}
	
}
