import javafx.scene.image.Image;

public class Powerup extends Actor implements Slowable{
	private double speed;
	
	public Powerup(double s) {
		speed = s;

		setImage(new Image("powerup.png"));
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
