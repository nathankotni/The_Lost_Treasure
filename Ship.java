import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Ship extends Actor {
	
	private int lane;
	
	private int health;
	
	private boolean isAnimating;
	
	private boolean isEnding;
	
	private AnimationTimer timer;
	
	private int laneChange;
	
	private int timerCount;
	
	private long prevTime = 0;
	
	private int trailRadius;
	
	private ArrayList<Circle> trails;
	
	private boolean isDying;
	
	private boolean isPaused;
	
	
	public Ship() {
		lane = 1;
		setImage(new Image("pirate_ship_00000.png"));
		isAnimating = false;
		isEnding = false;
		isDying = false;
		health = 3;
		trailRadius = 4;
		trails = new ArrayList<Circle>();
	}

	
	public void setHealth(int h) {
		health = h;
	}
	
	public int getHealth() {
		return health;
	}

	public void moveLane(boolean isRight) {
		laneChange = 0;
		if(isRight && lane != ((SeaWorld)getWorld()).getMaxLanes()) {
			laneChange = 1;
		}else if(!isRight && lane != 1) {
			laneChange = -1;
		}else {
			laneChange = 0;
		}
		
        double targetX = getX() + laneChange * ((GameState)getWorld().getState()).getPaneWidth() / ((SeaWorld)getWorld()).getMaxLanes();
        double changeX = targetX - getX(); 
        isAnimating = true;
        timerCount = 0;
	    timer = new AnimationTimer() {
			private long prev = 0;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}
				if(!isPaused) {
					if(now - prev >= 1000000 && timerCount < 17) {
						setX(getX() + changeX / 16.95);
						prev = now;
						timerCount++;
					}else if(now - prev >= 1000000 && timerCount >= 17){
						timerCount++;
						prev = now;
					}
					if(laneChange == 0) {
						stop();
						isAnimating = false;
					}
					if(changeX > 0 && timerCount >= 19) {
						if(getX() >= targetX) {
							setX(targetX);
							isAnimating = false;
							stop();
						}
					}else if (changeX < 0 && timerCount >= 24){
						if(getX() <= targetX) {
							setX(targetX);
							isAnimating = false;
							stop();
						}
					}
				}
			}
		};
		timer.start();
    
		lane += laneChange;
	}
	

	public int getLane() {
		return lane;
	}
	
	public void setLane(int l) {
		lane = l;
	}
	

	@Override
	public void act(long now) {
		if(!isPaused) {
				getWorld().setOnKeyPressed(new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						try {
							if(!isDying && !isPaused) {
								if(this != null && !isAnimating && !isEnding && ((SeaWorld)getWorld()).isKeyboardControls()) {
									if(event.getCode() == KeyCode.LEFT) {
										moveLane(false);
									}else if(event.getCode() == KeyCode.RIGHT) {
										moveLane(true);
									}
								}
							}
						}catch(Exception e) {
							
						}
						}});
		
				getWorld().setOnMousePressed(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						try {
							if(!isDying && !isPaused) {
								if(this != null && !isAnimating && !isEnding && !((SeaWorld)getWorld()).isKeyboardControls()) {
									if(getX() + getWidth() / 2 - event.getX() >= (((GameState)getWorld().getState()).getPaneWidth() / ((SeaWorld)getWorld()).getMaxLanes()) / 2) {
										moveLane(false);
									}else if(event.getX() - getX() - getWidth() / 2 >= (((GameState)getWorld().getState()).getPaneWidth() / ((SeaWorld)getWorld()).getMaxLanes()) / 2) {
										moveLane(true);
									}
								}
							}
						}catch(Exception e) {
						
						}
						}});
			if(((long)(now - prevTime)) >= 1600000 && !isDying) {
				Circle c = new Circle(this.getX() + this.getWidth() / 2 + 5, this.getY() + this.getHeight() + trailRadius + 1, trailRadius, Color.CORNFLOWERBLUE);
				getWorld().getChildren().add(c);
				for(int i = trails.size() - 1; i >= 0; i--) {
					trails.get(i).setCenterY(((Circle)trails.get(i)).getCenterY() + ((Circle)trails.get(i)).getRadius() * 2 + 2);
					trails.get(i).setCenterX(((Circle)trails.get(i)).getCenterX() + (((int)(Math.random() * 21) - 10)));
					if(((Circle)trails.get(i)).getCenterY() - ((Circle)trails.get(i)).getRadius() >= ((GameState)getWorld().getState()).getPaneHeight()) {
						getWorld().getChildren().remove(trails.get(i));
						trails.remove(i);
					}
				}
				prevTime = now;
				trails.add(c);
			}
		}
		
	}
	
	public void endingAnimation(int level) {
		isEnding = true;
		if(!isDying && !isPaused) {
			AnimationTimer end = new AnimationTimer() {
				private long prev = 0;
				@Override
				public void handle(long now) {
					if(prev == 0 || isPaused) {
						prev = now;
					}
					if(!isPaused) {
						if(now - prev >= 10000000) {
							setY(getY() - 3);
							prev = now;
						}
				        if (getY() + getHeight() <= 0) {
				            stop();
				            if(level == 5) {
				            	getWorld().setState(new GameWonState(getWorld()));
				            	
				            }else {
				            	((GameState)getWorld().getState()).nextLevel();
				            }
				            isEnding = false;
						}
					}
				}
			};
			end.start();	
		}
	}

	public void setAnimating(boolean isAnim) {
		isAnimating = isAnim;
	}
	
	public void setDying(boolean isDy) {
		isDying = isDy;
	}
	
	public void setPaused(boolean p) {
		isPaused = p;
	}
	
	
}
