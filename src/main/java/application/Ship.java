package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ship extends ImageView {

	private double speed;
	private double width;
	
	public double getSpeed() {
		return speed;
	}

	public double getWidth() {
		return width;
	}

	
	
	public Ship(double startX, double startY, double size, double speed, String imageURL) throws FileNotFoundException {
		super(new Image(new FileInputStream(imageURL)));
		this.speed = speed;
		this.width = size;
		setX(startX);
		setY(startY);
		setFitWidth(size);
		setPreserveRatio(true);
	}
	
	public void moveLeft() {
		setX(getX()-speed);
	}
	
	public void moveRight() {
		setX(getX()+speed);
	}
	
	
	
}
