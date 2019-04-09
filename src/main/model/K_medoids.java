package main.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class K_medoids {
	
	private List<PointC> scores;
	private Cluster[] clusters;
	
	public K_medoids(int k, HashMap<Color, Region> regions) {
		clusters = new Cluster[k];
		for(int i = 0; i < clusters.length; i++)
			clusters[i] = new Cluster();
		scores = new ArrayList<PointC>();
		for(Color color : regions.keySet()) {
			Region reg = regions.get(color);
			scores.add(new PointC(reg.getArea(), reg.getPerimeter(), reg.getCompact(), reg.getElongation(), color));
		}
	}
	
	private void initialCenter() {
		Random r = new Random();
		int l, i;
		double min, max, tmp, sum, rnd;
		
		max = 0.0;
		clusters[0].setCenter(scores.get(0));
		for(PointC pointC : scores)
			max += clusters[0].getCenter().distance(pointC);		
		for(PointC pc1 : scores) {
			sum = 0.0;
			for(PointC pc2 : scores)
				sum += pc1.distance(pc2);
			if(max < sum) {
				max = sum;
				clusters[0].setCenter(pc1);
			}
		}
		l = 1;
		while(l != clusters.length) {
			sum = 0.0;
			for(PointC pointC : scores) {
				min = clusters[0].getCenter().distance(pointC);
				for(i = 1; i < l; i++) {
					tmp = clusters[i].getCenter().distance(pointC);
					if(min > tmp)
						min = tmp;
				}
				sum += min*min;
			}
			rnd = r.nextDouble() * sum;
			sum = 0.0;
			for(PointC pointC : scores) {
				min = clusters[0].getCenter().distance(pointC);
				for(i = 1; i < l; i++) {
					tmp = clusters[i].getCenter().distance(pointC);
					if(min > tmp)
						min = tmp;
				}
				sum += min*min;
				if(sum > rnd) {
					clusters[l].setCenter(pointC);
					break;
				}
			}
			l++;
		}
	}
	
	private void resetCenter() {
		for(int i = 0; i < clusters.length; i++)
			clusters[i].resetCenter();
	}
	
	private void bindPoints() {
		int i;
		double min, tmp;
		
		for(PointC pointC : scores) {
			Cluster c = clusters[0];
			min = clusters[0].getCenter().distance(pointC);
			for(i = 1; i < clusters.length; i++) {
				tmp = clusters[i].getCenter().distance(pointC);
				if(min > tmp) {
					min = tmp;
					c = clusters[i];
				}
			}
			c.addPoint(pointC);
		}
	}
	
	private void clearClusters() {
		for(int i = 0; i < clusters.length; i++)
			clusters[i].clearV();
	}
	
	private boolean isReady() {
		int i, check = 0;
		
		for(i = 0; i < clusters.length; i++) {
			if(clusters[i].getCenter().equals(clusters[i].getLastCenter()))
				check++;
		}
		return check != clusters.length;
	}
	
	public Cluster[] getClusters() {
		initialCenter();
		do {
			clearClusters();
			bindPoints();
			resetCenter();
		} while(isReady());
		return clusters;
	}
}
