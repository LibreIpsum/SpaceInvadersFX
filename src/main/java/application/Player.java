package application;

import java.io.FileNotFoundException;

public class Player extends Ship{
	
	private static double playerWidth = 35;
	private static int playerSpeed = 3;

	public Player(double startX, double startY) throws FileNotFoundException {
		super(startX, startY, playerWidth, playerSpeed, "./img/playership.png");
	}
}
