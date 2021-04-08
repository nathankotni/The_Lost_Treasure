import javafx.scene.image.Image;

public class Debris extends Actor implements Slowable{

	private double speed;
	
	private int size;
	//1 for small, 2 for medium, 3 for big
	
	
	public Debris(double s, int level) {
		speed = s;
		
		int rand = (int)(Math.random() * (6 - level)) + 1;
		if(rand == 1) {
			setImage(new Image("large_debris.png"));
			size = 3;
		}else if(rand == 2 || rand == 3) {
			setImage(new Image("medium_debris.png"));
			size = 2;
		}else {
			setImage(new Image("small_debris.png"));
			size = 1;
		}
	}
	
	public int getSize() {
		return size;
	}
	
	@Override
	public void act(long now) {
		if(getY() - getHeight() / 2 > getWorld().getHeight()) {
			if(getWorld().getObjects(Debris.class).size() >= 2) {
				getWorld().getObjects(Debris.class).get(1).setY(getWorld().getObjects(Debris.class).get(1).getY() + speed * 2);
			}
			getWorld().getChildren().remove(this);
		}
		setY(getY() + speed);
	}
	
	public void setSpeed(double s) {
		speed = s;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	@Override
	public void slow(double num) {
		speed *= num/10.0;
		
	}

}
