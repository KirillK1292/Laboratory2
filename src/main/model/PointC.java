package main.model;

import java.awt.Color;

class PointC {
	
	private int x, y;
	private double z, t;
	private Color color;
	
	public PointC(int x, int y, double z, double t, Color color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.t = t;
		this.color = color;;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public double distance(PointC pointC) {
		return Math.sqrt(Math.pow(x - pointC.x, 2) + Math.pow(y - pointC.y, 2) + Math.pow(z - pointC.z, 2) + Math.pow(t - pointC.t, 2));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		PointC pointC = (PointC)obj;
		if(x != pointC.x)
			return false;
		if(y != pointC.y)
			return false;
		if(z != pointC.z)
			return false;
		if(t != pointC.t)
			return false;
		if(color == null)
			return pointC.color == null;		
		return color.equals(pointC.color);
	}
	
	@Override
	public int hashCode() {
		return (int)(31 * x + y + z + t + ((color == null) ? 0 : color.hashCode()));
	}
	
}
