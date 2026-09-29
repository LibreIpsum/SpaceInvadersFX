package application;

import java.io.FileNotFoundException;

public class Enemy extends Ship {
	
	protected static double enemyWidth = 42;

	public Enemy(double startX, double startY, double speed) throws FileNotFoundException {
		super(startX, startY, enemyWidth, speed, "img/enemy.png");
	}
	
	public void update() {
		moveLeft();
	}

}
