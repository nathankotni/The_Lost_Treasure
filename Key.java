import javafx.scene.image.Image;

public class Key extends Actor implements Slowable{
	
	private double speed;
	
	
	public Key(double s) {
		speed = s;
		
		int rand = (int)(Math.random() * 7) + 1;
		if(rand == 1) {
			setImage(new Image("key1.png"));
		}else if(rand == 2) {
			setImage(new Image("key2.png"));
		}else if(rand == 3) {
			setImage(new Image("key3.png"));
		}else if(rand == 4) {
			setImage(new Image("key4.png"));
		}else if(rand == 5) {
			setImage(new Image("key5.png"));
		}else if(rand == 6) {
			setImage(new Image("key6.png"));
		}else if(rand == 7) {
			setImage(new Image("key7.png"));
		}
	}
	
	
	@Override
	public void act(long now) {
		if(getY() - getHeight() / 2 > getWorld().getHeight()) {
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
