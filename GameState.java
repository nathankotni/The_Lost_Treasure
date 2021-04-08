import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

//Author: Nathan Kotni

public class GameState extends State{
	
	
	private BorderPane pane;
	
	private int maxLanes;
	
	private int level;
	
	private int timerCount;
	
	private int levelLength;
	
	private AnimationTimer clock;
	
	private int keySpawnTime;
	
	private Ship ship;
	
	private double spawnDelay;
	
	private AnimationTimer spawner;
	
	private int spawnerCounter;
	
	private int[] lanes;
	
	private boolean keyPlaced;
	
	private boolean isColliding;
	
	private AnimationTimer levelFade;
	
	private boolean spawned;
	
	private boolean keyColl;
	
	private Heart heart1;
	
	private Heart heart2;
	
	private Heart heart3;
	
	private boolean addHearts;
	
	private AnimationTimer collisionTimer;
	
	private AnimationTimer slowDownTimer;
	
	private double speedMod;
	
	private Button pause;
	
	private boolean isPaused;
	
	
	public GameState(World w) {
		super(w);
		pane = ((BorderPane)getWorld().getParent());
		maxLanes = 3;
		level = 1;
		spawnDelay = 2;
		//Keep levelLength greater than 10 for keySpawning to function correctly. Normal level length should be 60 seconds
		levelLength = 60;
		lanes = new int[]{0, 0, 0, 0, 0};
		isColliding = false;
		spawned = false;
		keyColl = false;
		addHearts = true;
		speedMod = 1;
		isPaused = false;
	}

	@Override
	public void onSet() {
		((SeaWorld)getWorld()).setNumKeys(0);
		
		ship = new Ship();
		getWorld().getChildren().add(ship);
		
		heart1 = new Heart();
		heart1.setX(pane.getWidth() - heart1.getWidth() - 90);
		heart1.setY(heart1.getHeight() / 10.0);
		getWorld().getChildren().add(heart1);
		
		heart2 = new Heart();
		heart2.setX(pane.getWidth() - heart2.getWidth() * 2 - 97);
		heart2.setY(heart2.getHeight() / 10.0);
		getWorld().getChildren().add(heart2);

		heart3 = new Heart();
		heart3.setX(pane.getWidth() - heart3.getWidth() * 3 - 104);
		heart3.setY(heart3.getHeight() / 10.0);
		getWorld().getChildren().add(heart3);

		pause = new Button("| |");
		pause.setMinWidth(50);
		pause.setMinHeight(35);
		pause.setLayoutX(pane.getWidth() - pause.getMinWidth());
		pause.setLayoutY((pause.getMinHeight() * 2.2));
		pause.setBorder(new Border(new BorderStroke(Color.DIMGRAY,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		pause.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(3))));
		pause.setFont(Font.font("Arial",  FontWeight.BOLD, 16));
		getWorld().getChildren().add(pause);
		pause.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				getWorld().getChildren().remove(pause);
				getWorld().getChildren().add(pause);
				
				if(!isPaused) {
					isPaused = true;
					ship.setPaused(true);
					pause.setText("►");
					getWorld().stop();
					
				}else {
					isPaused = false;
					ship.setPaused(false);
					pause.setText("| |");
					getWorld().start();
				}
				
			}
			
		});
		
		
		keySpawnTime = ((int)(Math.random() * ((levelLength - ((int)spawnDelay)) / 13))) + 1;
		
        maxLanes = 3;
        ((SeaWorld)getWorld()).setMaxLanes(maxLanes);
        ship.setLane(1);
        ship.setX(pane.getWidth() / (maxLanes * 2) - (ship.getWidth() / 2));
    	ship.setY(pane.getHeight() * 35 / 60);
		BackgroundImage img = new BackgroundImage(new Image("ocean1.png"),
		BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		BackgroundSize.DEFAULT);
		((BorderPane)getWorld().getParent()).setBackground(new Background(img));
		getWorld().setBackground(new Background(img));
		
     	getWorld().start();

     	TextFlow textFlow = new TextFlow(); 
		
		Text lev = new Text(" Level " + level + "\r\r\r\r");
		
		
		lev.setFont(Font.font("Arial", FontWeight.BOLD, 85));
		
		textFlow.getChildren().add(lev);

		textFlow.setTextAlignment(TextAlignment.CENTER);
		
		getWorld().getChildren().add(textFlow);
		
		
     	
     	levelFade = new AnimationTimer() {
			private long prev = 0;
			double opacityText = 1;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}		
				if(!isPaused) {
					getWorld().getChildren().remove(textFlow);
					getWorld().getChildren().add(textFlow);
					if(now - prev >= 10000) {
						opacityText -= 0.003;
						lev.opacityProperty().set(opacityText);
						prev = now;
					}
			        if (opacityText <= 0) {
			            getWorld().getChildren().remove(textFlow);
			            stop();
					}
				}
			}
		};
		levelFade.start();
		
		keyPlaced = false;
		spawnerCounter = 0;
		spawned = false;
		spawner = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(!isPaused) {
					if(spawned && ((!getWorld().getObjects(Debris.class).isEmpty() && getWorld().getObjects(Debris.class).get(0).getY() - getWorld().getObjects(Debris.class).get(0).getHeight() / 2 <= pane.getHeight() * 2 / 3) || (getWorld().getObjects(Debris.class).isEmpty()))) {
						spawned = false;
						int speed = level;
						if(speed > 4) {
							speed = 4;
						}
						//getWorld().getObjects(Debris.class).get(0).setY(getWorld().getObjects(Debris.class).get(0).getY() + speed);
					}
					if(!spawned && (getWorld().getObjects(Debris.class).isEmpty() || !getWorld().getObjects(Debris.class).isEmpty() && getWorld().getObjects(Debris.class).get(0).getY() - getWorld().getObjects(Debris.class).get(0).getHeight() / 2 >= pane.getHeight() * 2 / 3)) {
						spawnerCounter++;
						spawned = true;
						boolean firstDebPlaced = false;
						if(!keyPlaced && spawnerCounter == keySpawnTime) {
							int rand = ((int)(Math.random() * maxLanes));
							lanes[rand] = 2;
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 0 && !firstDebPlaced || lanes[i] == 0 && Math.random() > 0.60) {
									lanes[i] = 1;
									firstDebPlaced = true;
								}
							}
							int speed = level;
							if(speed > 4) {
								speed = 4;
							}
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 1) {
									Debris d = new Debris(((double)speed) * speedMod, level);
									d.setX((double)(i) * (pane.getWidth() / maxLanes) - d.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									d.setY(-d.getHeight());
									getWorld().getChildren().add(d);
								}else if(lanes[i] == 2) {
									Key k = new Key(((double)speed) * speedMod);
									k.setX((double)(i) * (pane.getWidth() / maxLanes) - k.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									k.setY(-k.getHeight());
									getWorld().getChildren().add(k);
								}
								keyPlaced = true;
							}
						}else {
							int amount = ((int)(Math.random() * maxLanes - 1)) + 1;
							if((level == 4 || level == 5 || level == 3) && amount < maxLanes - 2) {
								amount = maxLanes - 2;
							}else if((level == 1 || level == 2) && amount < maxLanes - 3) {
								amount = maxLanes - 3;
							}
							for(int i = 0; i < amount; i++) {
								int rand = ((int)(Math.random() * maxLanes));
								while(lanes[rand] != 0) {
									rand = ((int)(Math.random() * maxLanes));
								}
								lanes[rand] = 1;
							}
							int speed = level;
							if(speed > 4) {
								speed = 4;
							}
							boolean powerupInSpawn = false;
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 1) {
									Debris d = new Debris(((double)speed) * speedMod, level);
									d.setX((double)(i) * (pane.getWidth() / maxLanes) - d.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									d.setY(-d.getHeight());
									getWorld().getChildren().add(d);
								}else if(lanes[i] == 0 && Math.random() >= 0.995 - (0.05 * level) && !powerupInSpawn) {
									Powerup p = new Powerup(((double)speed) * speedMod);
									p.setX((double)(i) * (pane.getWidth() / maxLanes) - p.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									p.setY(-p.getHeight());
									getWorld().getChildren().add(p);
									powerupInSpawn = true;
								}
							}
						}
						for(int i = 0; i < lanes.length; i++) {
							lanes[i] = 0;
						}
					}
				}
			}
		}; 
		
		
		timerCount = levelLength * 10;
		
		Label clockText = new Label("" + timerCount);
		
		
		clockText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		
		clockText.setLayoutX(pane.getWidth() - 75);
		
		
		getWorld().getChildren().add(clockText);
		
		clock = new AnimationTimer() {
			private long prev = 0;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}	
				if(!isPaused) {
					getWorld().getChildren().remove(clockText);
					getWorld().getChildren().add(clockText);
					if(now - prev >= 100000000) {
						timerCount --;
						prev = now;
					}
					if(timerCount / 10.0 != (timerCount + 1) / 10.0) {
						clockText.setText("" + (((timerCount - 1) / 10) + 1));
					}
					if(timerCount <= (levelLength - ((int)spawnDelay)) * 10) {
						spawner.start();
					}
			        if (timerCount <= 0) {
			            getWorld().getChildren().remove(clockText);
			            stop();
			            getWorld().remove(heart1);
			            getWorld().remove(heart2);
			            getWorld().remove(heart3);
			            addHearts = false;
			            ship.endingAnimation(level);
					}
			        if(timerCount <= 80) {
			        	spawner.stop();
			        }
				}
			}
		};
		clock.start();
     	
     	
		
        getWorld().requestFocus();
        
 
        
	}
		
	public void nextLevel() {
		level++;
		getWorld().stop();
		
		getWorld().getChildren().removeAll(getWorld().getChildren());
		ship = new Ship();
		getWorld().getChildren().add(ship);
		
		heart1 = new Heart();
		heart1.setX(pane.getWidth() - heart1.getWidth() - 90);
		heart1.setY(heart1.getHeight() / 10.0);
		getWorld().getChildren().add(heart1);
		
		heart2 = new Heart();
		heart2.setX(pane.getWidth() - heart2.getWidth() * 2 - 97);
		heart2.setY(heart2.getHeight() / 10.0);
		getWorld().getChildren().add(heart2);

		heart3 = new Heart();
		heart3.setX(pane.getWidth() - heart3.getWidth() * 3 - 104);
		heart3.setY(heart3.getHeight() / 10.0);
		getWorld().getChildren().add(heart3);
		
		pause = new Button("| |");
		pause.setMinWidth(50);
		pause.setMinHeight(35);
		pause.setLayoutX(pane.getWidth() - pause.getMinWidth());
		pause.setLayoutY((pause.getMinHeight() * 2.2));
		pause.setBorder(new Border(new BorderStroke(Color.DIMGRAY,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		pause.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(3))));
		pause.setFont(Font.font("Arial",  FontWeight.BOLD, 16));
		getWorld().getChildren().add(pause);
		pause.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(!isPaused) {
					isPaused = true;
					ship.setPaused(true);
					pause.setText("►");
					getWorld().stop();
				}else {
					isPaused = false;
					ship.setPaused(false);
					pause.setText("| |");
					getWorld().start();
				}
				
			}
			
		});
		
		addHearts = true;
		
		if(level == 2) {
			maxLanes = 4;
			((SeaWorld)getWorld()).setMaxLanes(maxLanes);
			ship.setX(pane.getWidth() / (maxLanes * 2) - (ship.getWidth() / 2));
			ship.setY(pane.getHeight() * 35 / 60);
			ship.setLane(1);
			BackgroundImage img = new BackgroundImage(new Image("ocean2.png"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);
			((BorderPane)getWorld().getParent()).setBackground(new Background(img));
			getWorld().setBackground(new Background(img));
		}else if(level == 3) {
			maxLanes = 4;
			((SeaWorld)getWorld()).setMaxLanes(maxLanes);
			ship.setX(pane.getWidth() / (maxLanes * 2) - (ship.getWidth() / 2));
			ship.setY(pane.getHeight() * 35 / 60);
			ship.setLane(1);
			BackgroundImage img = new BackgroundImage(new Image("ocean5.png"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);
			((BorderPane)getWorld().getParent()).setBackground(new Background(img));
			getWorld().setBackground(new Background(img));
		}else if(level == 4) {
			maxLanes = 5;
			((SeaWorld)getWorld()).setMaxLanes(maxLanes);
			ship.setX(pane.getWidth() / (maxLanes * 2) - (ship.getWidth() / 2));
			ship.setY(pane.getHeight() * 35 / 60);
			ship.setLane(1);
			BackgroundImage img = new BackgroundImage(new Image("ocean4.png"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);
			((BorderPane)getWorld().getParent()).setBackground(new Background(img));
			getWorld().setBackground(new Background(img));
		}else if(level == 5) {
			maxLanes = 5;
			((SeaWorld)getWorld()).setMaxLanes(maxLanes);
			ship.setX(pane.getWidth() / (maxLanes * 2) - (ship.getWidth() / 2));
			ship.setY(pane.getHeight() * 35 / 60);
			ship.setLane(1);
			BackgroundImage img = new BackgroundImage(new Image("ocean3.png"),
			BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT);
			((BorderPane)getWorld().getParent()).setBackground(new Background(img));
			getWorld().setBackground(new Background(img));
		}
		
		keySpawnTime = ((int)(Math.random() * ((levelLength - ((int)spawnDelay)) / 12))) + 1;
		
		TextFlow textFlow = new TextFlow(); 
		
		Text lev = new Text(" Level " + level + "\r\r\r\r");
		
		
		lev.setFont(Font.font("Arial", FontWeight.BOLD, 85));
		
		textFlow.getChildren().add(lev);

		textFlow.setTextAlignment(TextAlignment.CENTER);
		
				    
		
		getWorld().getChildren().add(textFlow);
		
		
     	levelFade = new AnimationTimer() {
			private long prev = 0;
			double opacityText = 1;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}
				if(!isPaused) {
					getWorld().getChildren().remove(textFlow);
					getWorld().getChildren().add(textFlow);
					if(now - prev >= 10000) {
						opacityText -= 0.003;
						lev.opacityProperty().set(opacityText);
						prev = now;
					}
			        if (opacityText <= 0) {
			            getWorld().getChildren().remove(textFlow);
			            ((BorderPane)getWorld().getParent()).setBottom(null);
			            ((BorderPane)getWorld().getParent()).setBackground(null);
			            stop();
					}
				}
			}
		};
		levelFade.start();
		
		keyPlaced = false;
		spawnerCounter = 0;
		spawned = false;
		spawner = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(!isPaused) {
					if(spawned && ((!getWorld().getObjects(Debris.class).isEmpty() && getWorld().getObjects(Debris.class).get(0).getY() - getWorld().getObjects(Debris.class).get(0).getHeight() / 2 <= pane.getHeight() * 2 / 3) || (getWorld().getObjects(Debris.class).isEmpty()))) {
						spawned = false;
						int speed = level;
						if(speed > 4) {
							speed = 4;
						}
						//getWorld().getObjects(Debris.class).get(0).setY(getWorld().getObjects(Debris.class).get(0).getY() + speed);
					}
					if(!spawned && (getWorld().getObjects(Debris.class).isEmpty() || !getWorld().getObjects(Debris.class).isEmpty() && getWorld().getObjects(Debris.class).get(0).getY() - getWorld().getObjects(Debris.class).get(0).getHeight() / 2 >= pane.getHeight() * 2 / 3)) {
						spawnerCounter++;
						spawned = true;
						boolean firstDebPlaced = false;
						if(!keyPlaced && spawnerCounter == keySpawnTime) {
							int rand = ((int)(Math.random() * maxLanes));
							lanes[rand] = 2;
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 0 && !firstDebPlaced || lanes[i] == 0 && Math.random() > 0.60) {
									lanes[i] = 1;
									firstDebPlaced = true;
								}
							}
							int speed = level;
							if(speed > 4) {
								speed = 4;
							}
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 1) {
									Debris d = new Debris(((double)speed) * speedMod, level);
									d.setX((double)(i) * (pane.getWidth() / maxLanes) - d.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									d.setY(-d.getHeight());
									getWorld().getChildren().add(d);
								}else if(lanes[i] == 2) {
									Key k = new Key(((double)speed) * speedMod);
									k.setX((double)(i) * (pane.getWidth() / maxLanes) - k.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									k.setY(-k.getHeight());
									getWorld().getChildren().add(k);
								}
								keyPlaced = true;
							}
						}else {
							int amount = ((int)(Math.random() * maxLanes - 1)) + 1;
							if((level == 4 || level == 5 || level == 3) && amount < maxLanes - 2) {
								amount = maxLanes - 2;
							}else if((level == 1 || level == 2) && amount < maxLanes - 3) {
								amount = maxLanes - 3;
							}
							for(int i = 0; i < amount; i++) {
								int rand = ((int)(Math.random() * maxLanes));
								while(lanes[rand] != 0) {
									rand = ((int)(Math.random() * maxLanes));
								}
								lanes[rand] = 1;
							}
							int speed = level;
							if(speed > 4) {
								speed = 4;
							}
							boolean powerupInSpawn = false;
							for(int i = 0; i < maxLanes; i++) {
								if(lanes[i] == 1) {
									Debris d = new Debris(((double)speed) * speedMod, level);
									d.setX((double)(i) * (pane.getWidth() / maxLanes) - d.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									d.setY(-d.getHeight());
									getWorld().getChildren().add(d);
								}else if(lanes[i] == 0 && Math.random() >= 0.995 - (0.05 * level) && !powerupInSpawn) {
									Powerup p = new Powerup(((double)speed) * speedMod);
									p.setX((double)(i) * (pane.getWidth() / maxLanes) - p.getWidth() / 2 + (pane.getWidth() / (maxLanes * 2)));
									p.setY(-p.getHeight());
									getWorld().getChildren().add(p);
									powerupInSpawn = true;
								}
							}
						}
						for(int i = 0; i < lanes.length; i++) {
							lanes[i] = 0;
						}
					}
				}
			}
		}; 
		
		timerCount = levelLength * 10;
		
		Label clockText = new Label("" + timerCount);
		
		
		clockText.setFont(Font.font("Arial", FontWeight.BOLD, 60));
		
		clockText.setLayoutX(pane.getWidth() - 75);
		
		
		getWorld().getChildren().add(clockText);
		
		clock = new AnimationTimer() {
			private long prev = 0;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}	
				if(!isPaused) {
					getWorld().getChildren().remove(clockText);
					getWorld().getChildren().add(clockText);
					if(now - prev >= 100000000) {
						timerCount --;
						prev = now;
					}
					if(timerCount / 10.0 != (timerCount + 1) / 10.0) {
						clockText.setText("" + (((timerCount - 1) / 10) + 1));
					}
					if(timerCount <= (levelLength - ((int)spawnDelay)) * 10) {
						spawner.start();
					}
			        if (timerCount <= 0) {
			            getWorld().getChildren().remove(clockText);
			            stop();
			            getWorld().remove(heart1);
			            getWorld().remove(heart2);
			            getWorld().remove(heart3);
			            addHearts = false;
			            ship.endingAnimation(level);
					}
			        if(timerCount <= 80) {
			        	spawner.stop();
			        }
				}
			}
		};
		clock.start();
		
		getWorld().start();
	}
	
	public int getMaxLanes() {
		return maxLanes;
	}
	
	
	@Override
	public void onRemove() {
		getWorld().getChildren().removeAll(getWorld().getChildren());
		((BorderPane)getWorld().getParent()).setBackground(null);
		getWorld().setBackground(null);
	}

	@Override
	public void onAct() {
		if(!isPaused) {
			getWorld().getChildren().remove(ship);
			getWorld().getChildren().add(ship);
			
			if(addHearts) {
				getWorld().getChildren().remove(heart1);
				getWorld().getChildren().remove(heart2);
				getWorld().getChildren().remove(heart3);
			
				getWorld().getChildren().add(heart1);
				getWorld().getChildren().add(heart2);
				getWorld().getChildren().add(heart3);
			}
			
			Key key = (getWorld().getObjects(Ship.class)).get(0).getOneIntersectingObject(Key.class);
			Debris d = (getWorld().getObjects(Ship.class)).get(0).getOneIntersectingObject(Debris.class);
			Powerup p = (getWorld().getObjects(Ship.class)).get(0).getOneIntersectingObject(Powerup.class);
			if(key != null && !keyColl) {
				keyColl = true;
	            ((SeaWorld) getWorld()).keyFound();
	            collisionAnimation(ship, key, false, false);
	        }else if(d != null && !isColliding) {
				isColliding = true;
	            int size = d.getSize();
	            if(size == 1) {
	            	ship.setHealth(ship.getHealth() - 1);
	            }else if(size == 2) {
	            	ship.setHealth(ship.getHealth() - 2);
	            }else if(size == 3) {
	            	ship.setHealth(ship.getHealth() - 3);
	            }
	            if(ship.getHealth() <= 2) {
	            	heart3.setImage(new Image("empty_heart.png"));
	            }
	            if(ship.getHealth() <= 1) {
	            	heart2.setImage(new Image("empty_heart.png"));
	            }
	            if(ship.getHealth() <= 0) {
	            	heart1.setImage(new Image("empty_heart.png"));
	            }
	            boolean death = false;
	            if(ship.getHealth() <= 0) {
	            	death = true;
	            }
	            collisionAnimation(ship, d, death, true);
	        }else if(p != null && !keyColl) {
	        	keyColl = true;
	            collisionAnimation(ship, p, false, false);
	            slowDown();
			}
		}
	}
	
	public void slowDown() {
		for(int i = 0; i < getWorld().getObjects(Key.class).size(); i++) {
			getWorld().getObjects(Key.class).get(i).slow(5);
		}
		for(int i = 0; i < getWorld().getObjects(Debris.class).size(); i++) {
			getWorld().getObjects(Debris.class).get(i).slow(5);
		}
		for(int i = 0; i < getWorld().getObjects(Powerup.class).size(); i++) {
			getWorld().getObjects(Powerup.class).get(i).slow(5);
		}
		speedMod *= 0.5;
     	slowDownTimer = new AnimationTimer() {
			private long prev = 0;
			private int countMod = 0;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}
				if(!isPaused) {
					if(now - prev >= 100000000) {
						countMod++;
						prev = now;
					}
					if(countMod == 100) {
						for(int i = 0; i < getWorld().getObjects(Key.class).size(); i++) {
							getWorld().getObjects(Key.class).get(i).slow(20);
						}
						for(int i = 0; i < getWorld().getObjects(Debris.class).size(); i++) {
							getWorld().getObjects(Debris.class).get(i).slow(20);
						}
						for(int i = 0; i < getWorld().getObjects(Powerup.class).size(); i++) {
							getWorld().getObjects(Powerup.class).get(i).slow(20);
						}
						stop();
						speedMod *= 2;
					}
				}
			}
		};
		slowDownTimer.start();
	}
	
	public void collisionAnimation(Ship s, Actor a, boolean isDeath, boolean isDamaging) {
     	collisionTimer = new AnimationTimer() {
			private long prev = 0;
			double opacity = 1;
			int opacityCount = 1;
			@Override
			public void handle(long now) {
				if(prev == 0 || isPaused) {
					prev = now;
				}
				if(!isPaused) {
					if(isDeath) {
						ship.setDying(true);
						clock.stop();
					}
					if(prev == 0) {
						prev = now;
					}
					if(now - prev >= 10000) {
						opacityCount++;
						opacity -= 0.009;
						if(!isDamaging && opacityCount >= 28) {
							((Slowable)a).slow(5);
							opacityCount = 0;
						}
						if(isDeath && opacityCount >= 33) {
							((Slowable)a).slow(0.5);
							opacityCount = 0;
						}
						if(isDeath) {
							s.opacityProperty().set(opacity);
						}else if(isDamaging){
							a.opacityProperty().set(opacity);
							if(opacityCount >= 20 && s.getOpacity() == 1) {
								opacityCount = 0;
								((Slowable)a).slow(6);
								s.opacityProperty().set(0);
							}else if(opacityCount >= 12 && s.getOpacity() == 0) {
								opacityCount = 0;
								((Slowable)a).slow(6);
								s.opacityProperty().set(1);
							}
						}else {
							a.opacityProperty().set(opacity);
						}
						prev = now;
					}
					if(opacity <= 0.1) {
			        	if(isDeath) {
			        		spawner.stop();
			        		clock.stop();
			        		levelFade.stop();
			        		getWorld().setState(new GameOverState(getWorld()));
			        		
			        	}else if(isDamaging){
			        		isColliding = false;
			        	}else if(!isDamaging) {
			        		keyColl = false;
			        	}
			        	s.opacityProperty().set(1);
		        		getWorld().getChildren().remove(a);
			            stop();
					}
				}
			}
		};
		collisionTimer.start();
	}
	
	public double getPaneWidth() {
		return pane.getWidth();
	}
	
	public double getPaneHeight() {
		return pane.getHeight();
	}
	
	public boolean getPaused() {
		return isPaused;
	}
	
	
}
