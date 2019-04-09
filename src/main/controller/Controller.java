package main.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.model.Img;
import main.model.ImgException;
import main.model.Region;
import main.view.View;

public class Controller {
	
	private Img model;
	private View view;
	
	public Controller(Img model, View view) {
		this.model = model;
		this.view = view;
		this.view.addOpenMenuItemActionListener(new OpenMenuItemActionListener());
		this.view.addExitMenuItemActionListener(new ExitMenuItemActionListener());
		this.view.addBrightnessSliderChangeListener(new BrightnessSliderChangeListener());
		this.view.addBrightnessButtonActionListener(new BrightnessButtonActionListener());
		this.view.addThresholdSliderChangeListener(new ThresholdSliderChangeListener());
		this.view.addAllocConnRegButtonActionListener(new AllocConnRegButtonActionListener());
		this.view.addOpeningButtonActionListener(new OpeningButtonActionListener());
		this.view.addClosingButtonActionListener(new ClosingButtonActionListener());
		this.view.addMedianFilterButtonActionListener(new MedianFilterButtonActionListener());
	}
	
	private class OpenMenuItemActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				view.openImage();
			} catch (IOException ex) {
				view.showErrorMessage(ex.getMessage());
			}
		}
		
	}
	
	private class ExitMenuItemActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			view.exit();			
		}
		
	}
	
	private class BrightnessSliderChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			try {
				model.setImage(view.getOriginalImage());
				model.brightnessImage(view.getBrightness());
				view.setProcessedImage(model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}		
		}
		
	}
	
	private class BrightnessButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			view.takeBrightness();
		}
		
	}
	
	private class ThresholdSliderChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			try {
				model.setImage(view.getOriginalImage());
				model.thresholdBinarization(view.getThreshold());
				view.setProcessedImage(model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}		
		}
		
	}
	
	private class AllocConnRegButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				model.setImage(view.getProcessedImage());
				HashMap<Color, Region> regions = model.sequentialScanning();
				view.setProcessedImage(model.getImage());
				model.setImage(view.getProcessedImage());
				model.clusteredRegions(view.getKValue(), regions);
				view.showSequentialScanningResult(regions, model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}
		}
		
	}
	
	private class OpeningButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				model.setImage(view.getProcessedImage());
				model.opening();
				view.setProcessedImage(model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}
		}
		
	}
	
	private class ClosingButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				model.setImage(view.getProcessedImage());
				model.closing();
				view.setProcessedImage(model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}
		}
		
	}
	
	private class MedianFilterButtonActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				model.setImage(view.getProcessedImage());
				model.medianFilter();
				view.setProcessedImage(model.getImage());
			} catch (NullPointerException ex) {
				view.showErrorMessage(ex.getMessage());
			} catch (ImgException ex) {
				view.showErrorMessage(ex.getMessage());
			}
		}
		
	}
	
}
