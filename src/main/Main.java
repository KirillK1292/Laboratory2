package main;

import main.controller.Controller;
import main.model.Img;
import main.view.View;

public class Main {
	
	public static void main(String[] args) {
		Img model = new Img();
		View view = new View("Laboratory 2");
		Controller controller = new Controller(model, view);
		view.setVisible(true);
	}
	
}
