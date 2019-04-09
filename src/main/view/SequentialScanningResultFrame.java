package main.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.*;

import main.model.Region;

class SequentialScanningResultFrame extends JDialog {
	
	public SequentialScanningResultFrame(JFrame owner, BufferedImage image, HashMap<Color, Region> regions) {
		super(owner, true);
		this.setTitle("Результат кластеризации");
		this.setSize(900, 400);
		this.setResizable(false);
		this.setLayout(null);
		
		RegionsTableModel tableModel = new RegionsTableModel(new String[]{"№", "Площадь", "Периметр", "Компактность", "Центр масс", "Удлиненность"});
		JTable regionsTable = new JTable(tableModel);
		regionsTable.getColumnModel().getColumn(0).setMaxWidth(20);		
		JScrollPane scrollPane = new JScrollPane(regionsTable);
		scrollPane.setBounds(3, 3, 530, 367);
		Graphics g = image.getGraphics();
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		int n = 1;
		for(Color color : regions.keySet()) {
			Region reg = regions.get(color);
			tableModel.addRow(n, reg.getArea(), reg.getPerimeter(), reg.getCompact(), reg.getCenterMass().toString(), reg.getElongation());
			g.drawString(Integer.toString(n), reg.getPixel(0).getPoint().x, reg.getPixel(0).getPoint().y);
			n++;
		}
		g.dispose();
		ImagePanel resultImage = new ImagePanel();
		resultImage.setImage(image);
		resultImage.setBounds(533, 3, 358, 366);
		this.add(scrollPane);
		this.add(resultImage);
	}
	
}
