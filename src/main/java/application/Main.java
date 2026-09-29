package application;


import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
	
	int appWidth = 800;
	Group root;
	Group endScreen;
	Scene gameOver;
	boolean isOver = false;
	
	ArrayList<String> input = new ArrayList<String>();
	
	KeyFrame gameFrame;
	
	Player player;
	
	ArrayList<Laser> playerLasers = new ArrayList<Laser>();
	int laserCooldown = 30;
	int cooldownFrame = 0;
	
	
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	int enemySpeed = 1;
	int currentEnemyStep = 1;
	int enemySteps = appWidth / enemySpeed;
	int enemyPad = 50;
	int enemyBoxWidth = 0;
	
	
	ArrayList<Laser> enemyLasers = new ArrayList<Laser>();
	double EnemyPercentFireRate = 2;	
	
	
	
	
	@Override
	public void start(Stage stage) {
		try {
			
			// game over scene
			endScreen = new Group();
			gameOver = new Scene(endScreen, appWidth, appWidth, Color.GRAY);
			
			
			
			// main group and scene
			root = new Group();
			Scene scene = new Scene(root, appWidth, appWidth, Color.rgb(0, 0,	15));
			
			
			// create a player ship
			player = new Player(scene.getWidth()/2, scene.getHeight()-100);		
			root.getChildren().add(player);
			
			
			// create enemies			
			for (int w=0; w<12; w++) {
				for (int h=0; h<5; h++) {
					Enemy enemy = new Enemy((w*enemyPad), (h*enemyPad)+60, 1);
					enemies.add(enemy);
//					enemyBox.getChildren().add(enemy);
					root.getChildren().add(enemy);
				}
				enemyBoxWidth += enemyPad;
			}
			enemySteps -= enemyBoxWidth;
			
			// event handling
			scene.setOnKeyPressed(event -> handleKeyPress(event));
			scene.setOnKeyReleased(event -> handleKeyReleased(event));

			
			stage.setTitle("Space Invaders");
			stage.setScene(scene);
			stage.show();
			
			initializeGameLoop(stage);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	private void initializeGameLoop(Stage stage) {
        // Set up the game loop with 60 FPS (16.67 milliseconds per frame)
        Duration frameDuration = Duration.millis(16.67);
        gameFrame = new KeyFrame(frameDuration, event -> {
			try {
				updateGame(stage);
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		});
        Timeline gameLoop = new Timeline(gameFrame);
        gameLoop.setCycleCount(Animation.INDEFINITE);

        // Start the game loop
        gameLoop.play();
    }
	
	

    private void updateGame(Stage stage) throws FileNotFoundException {
        // counters
    	cooldownFrame-=1;
    	
    	// exit if finished
    	if (isOver) {
    		return;
    	}
    	
    	
    	// player inputs
    	if (input.contains("LEFT")) {
        	if (player.getX() > 0) {
        		player.moveLeft();
        	}
        }
        if (input.contains("RIGHT")) {
        	if (player.getX() < appWidth - player.getWidth()) {
            	player.moveRight();
        	}
        }
        // laser firing
        if (input.contains("SPACE")) {
        	// only allow firing once every 30 frames
        	if (cooldownFrame <= 0) {
        		Laser laser = new Laser(player.getX() + (player.getWidth()/2)-5, player.getY(), 10);
    			playerLasers.add(laser);
    			root.getChildren().add(laser);
    			cooldownFrame = laserCooldown;
        	}
        	
        }
        
        
        // Laser movement
        for (int i=0;i<playerLasers.size();i++) {
        	Laser laser = playerLasers.get(i);
        	laser.update();
        	if (laser.getY() < -20) {
        		playerLasers.remove(i);
        		root.getChildren().remove(laser);
        	}
        	
        	
        }
        for (int i=0;i<enemyLasers.size();i++) {
        	Laser laser = enemyLasers.get(i);
        	laser.update();
        	if (laser.getY() > appWidth + 20) {
        		enemyLasers.remove(i);
        		root.getChildren().remove(laser);
        	}
        	
        	
        }
        
        
     // Enemy movement
        for (int i=0; i<enemies.size(); i++){
        	Enemy enemy = enemies.get(i);
        	enemy.setX(enemy.getX() + enemySpeed);
        	
        	// Enemy collisions with lasers
        	for (int j=0; j<playerLasers.size();j++) {
        		Laser laser = playerLasers.get(j);
        		if (laser.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
        			playerLasers.remove(j);
        			root.getChildren().remove(laser);
        			enemies.remove(i);
        			root.getChildren().remove(enemy);
        		}
        	}
        }
        currentEnemyStep += 1;
        if (currentEnemyStep >= enemySteps) {
    		enemySpeed *= -1;
    		currentEnemyStep = 1;
    	}
        
        
        
        
        // Enemy firing
        if (Math.random()*100 < EnemyPercentFireRate) {
        	
        	int firingEnemyIndex = (int)(Math.random()*enemies.size());
        	Enemy firingEnemy = enemies.get(firingEnemyIndex);
        	
        	Laser newLaser = new Laser(firingEnemy.getX() + (firingEnemy.getWidth()/2)-5, firingEnemy.getY(), -10);
        	enemyLasers.add(newLaser);
        	root.getChildren().add(newLaser);
        }
        
        // Enemy laser collision with player
        for (int j=0; j<enemyLasers.size();j++) {
    		Laser laser = enemyLasers.get(j);
    		if (laser.getBoundsInParent().intersects(player.getBoundsInParent())) {
    			endGame(stage, "You Lost.");
    		}
    	}
        
        // game over, player wins
        if (enemies.isEmpty()) {
        	endGame(stage, "You Won!");
			
        }
        
        
    }

    
    
    
    
    // event handler functions
    private void handleKeyPress(KeyEvent event) {
    	String code = event.getCode().toString();
    	if (!input.contains(code)) {
    		input.add(code);
    	}
    }
    private void handleKeyReleased(KeyEvent event) {
		input.remove(event.getCode().toString());
	}
    
    private void endGame(Stage stage, String text) {
    	Text endText = new Text(text);
    	endText.setFont(new Font(50));
		endText.setTextAlignment(TextAlignment.CENTER);
		endText.setX((appWidth/2)-(endText.getBoundsInParent().getWidth()/2));
		endText.setY((appWidth/2)-(endText.getBoundsInParent().getHeight()/2));
		
		endScreen.getChildren().add(endText);
		stage.setScene(gameOver);
		isOver = true;
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
