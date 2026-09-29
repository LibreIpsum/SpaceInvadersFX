package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Laser extends ImageView {

	private double speed;
	
	public Laser(double startX, double startY, double speed) throws FileNotFoundException {
		super(new Image(new FileInputStream("img/laser.png")));
		this.speed = speed;
		// rotate lasers to point the correct direction
		if (speed < 0) {
			setRotate(180);
		}
		setX(startX);
		setY(startY);
		setFitWidth(10);
		setPreserveRatio(true);
	}
	
	public void update() {
		setY(getY()-speed);
	}
}
