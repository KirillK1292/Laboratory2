package main.model;

import java.util.ArrayList;
import java.util.List;

class Cluster {
	
	private List<PointC> v;
	private PointC center;
	private PointC lastCenter;
	
	public Cluster() {
		v = new ArrayList<PointC>();
	}

	public PointC getCenter() {
		return center;
	}

	public void setCenter(PointC center) {
		this.center = center;
	}

	public PointC getLastCenter() {
		return lastCenter;
	}

	public void setLastCenter(PointC lastCenter) {
		this.lastCenter = lastCenter;
	}
	
	public void addPoint(PointC pointC) {
		v.add(pointC);
	}
	
	public void clearV() {
		v.clear();
	}
	
	public int getSizeV() {
		return v.size();
	}
	
	public PointC getPoint(int i) {
		return v.get(i);
	}
	
	public void resetCenter() {
		double sum, min = 0.0;
		PointC newCenter = center;
		
		for(PointC pointC : v)
			min += newCenter.distance(pointC);
		
		for(PointC pc1 : v) {
			sum = 0.0;
			for(PointC pc2 : v)
				sum += pc1.distance(pc2);
			if(min > sum) {
				min = sum;
				newCenter = pc1;
			}
		}
		lastCenter = center;
		center = newCenter;
	}
	
}
