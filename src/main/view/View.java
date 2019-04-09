package main.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.model.Region;

public class View extends JFrame {
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem openMenuItem, exitMenuItem;
	private JFileChooser fileChooser;
	private JSlider brightnessSlider, thresholdSlider;
	private JPanel processedPanel, brightnessPanel, thresholdPanel, buttonPanel, kPanel;
	private JSpinner kSpinner;
	private JButton brightnessButton, allocConnRegButton, openingButton, closingButton, medianFilterButton;
	private ImagePanel originalImage, processedImage;
	private JSplitPane splitPane;
	
	public View(String title) {
		super(title);
		initialize();
	}
	
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(1200, 700));
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Файл");
		openMenuItem = new JMenuItem("Открыть");
		exitMenuItem = new JMenuItem("Выход");
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);		
		brightnessPanel = new JPanel();
		brightnessPanel.setBorder(BorderFactory.createTitledBorder("Яркость"));
		brightnessSlider = new JSlider(-255,255,0);
		brightnessButton = new JButton("Применить");
		brightnessButton.setEnabled(false);
		brightnessPanel.add(brightnessSlider);
		brightnessPanel.add(brightnessButton);		
		thresholdPanel = new JPanel();		
		thresholdPanel.setBorder(BorderFactory.createTitledBorder("Пороговая бинаризация"));
		thresholdSlider = new JSlider(0, 255, 128);
		thresholdSlider.setPaintTicks(true);
		thresholdSlider.setPaintLabels(true);
		thresholdSlider.setMajorTickSpacing(50);
		thresholdSlider.setMinorTickSpacing(5);
		thresholdPanel.add(thresholdSlider);
		kPanel = new JPanel();		
		kPanel.setBorder(BorderFactory.createTitledBorder("Кол-во кластеров"));
		SpinnerModel spinnerModel =  new SpinnerNumberModel(1, 1, 10, 1);
		kSpinner = new JSpinner(spinnerModel);
		((JSpinner.DefaultEditor)kSpinner.getEditor()).getTextField().setEditable(false);
		kPanel.add(kSpinner);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));		
		allocConnRegButton = new JButton("Выделить области");
		openingButton = new JButton("Морфолог. раскрытие");
		closingButton = new JButton("Морфолог. закрытие");
		medianFilterButton = new JButton("Медианный фильтр");
		buttonPanel.add(openingButton);
		buttonPanel.add(medianFilterButton);
		buttonPanel.add(closingButton);
		buttonPanel.add(allocConnRegButton);
		processedPanel = new JPanel();
		processedPanel.setLayout(new GridLayout(1, 4));
		processedPanel.setVisible(false);		
		processedPanel.add(brightnessPanel);
		processedPanel.add(thresholdPanel);
		processedPanel.add(kPanel);
		processedPanel.add(buttonPanel);		
		this.add(processedPanel, BorderLayout.NORTH);
		originalImage = new ImagePanel();
		processedImage = new ImagePanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originalImage, processedImage);
		splitPane.setResizeWeight(0.5);
		this.add(splitPane, BorderLayout.CENTER);
		fileChooser = new JFileChooser();
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		fileChooser.setFileFilter(imageFilter);
	}
	
	public void addOpenMenuItemActionListener(ActionListener listener) {
		openMenuItem.addActionListener(listener);
	}
	
	public void addExitMenuItemActionListener(ActionListener listener) {
		exitMenuItem.addActionListener(listener);
	}
	
	public void addBrightnessSliderChangeListener(ChangeListener listener) {
		brightnessSlider.addChangeListener(listener);
	}
	
	public void addBrightnessButtonActionListener(ActionListener listener) {
		brightnessButton.addActionListener(listener);
	}
	
	public void addThresholdSliderChangeListener(ChangeListener listener) {
		thresholdSlider.addChangeListener(listener);
	}
	
	public void addAllocConnRegButtonActionListener(ActionListener listener) {
		allocConnRegButton.addActionListener(listener);
	}
	
	public void addOpeningButtonActionListener(ActionListener listener) {
		openingButton.addActionListener(listener);
	}
	
	public void addClosingButtonActionListener(ActionListener listener) {
		closingButton.addActionListener(listener);
	}
	
	public void addMedianFilterButtonActionListener(ActionListener listener) {
		medianFilterButton.addActionListener(listener);
	}
	
	public void openImage() throws IOException {
		if(fileChooser.showDialog(null, "Открыть") == JFileChooser.APPROVE_OPTION) {
			processedPanel.setVisible(false);
			brightnessButton.setEnabled(false);
			thresholdSlider.setValue(128);
			brightnessSlider.setValue(0);
			kSpinner.setValue(1);
			File imageFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
			originalImage.setImage(ImageIO.read(imageFile));
			processedImage.setImage(ImageIO.read(imageFile));
			processedPanel.setVisible(true);
		}
	}
	
	public void exit() {
		this.setVisible(false);
		this.dispose();
		System.exit(0);
	}
	
	public BufferedImage getOriginalImage() {
		return originalImage.getImage();
	}
	
	public void setOriginalImage(BufferedImage image) {
		originalImage.setImage(image);
	}
	
	public BufferedImage getProcessedImage() {
		return processedImage.getImage();
	}
	
	public void setProcessedImage(BufferedImage image) {
		processedImage.setImage(image);
	}
	
	public int getThreshold() {
		brightnessSlider.setValue(0);
		brightnessButton.setEnabled(false);
		return thresholdSlider.getValue();
	}
	
	public int getBrightness() {
		thresholdSlider.setValue(128);
		brightnessButton.setEnabled(true);
		return brightnessSlider.getValue();
	}
	
	public void takeBrightness() {
		originalImage.setImage(processedImage.getImage());
		brightnessSlider.setValue(0);
		brightnessButton.setEnabled(false);
	}
	
	public int getKValue() {
		return (int)kSpinner.getValue();
	}
	
	public void showSequentialScanningResult(HashMap<Color, Region> regions, BufferedImage image) {
		SequentialScanningResultFrame result = new SequentialScanningResultFrame(this, image, regions);
		result.setVisible(true);
	}
	
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
	}
	
}
