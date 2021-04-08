
public class SeaWorld extends World{

	private int keysCollected;
	private int maxLanes;
	private boolean isKeyboardControls;
	
	public SeaWorld() {
		keysCollected = 0;
		maxLanes = 3;
		isKeyboardControls = true;
	}
	@Override
	public void act(long now) {
		getState().onAct();
	}
	
	public void setMaxLanes(int mL) {
		maxLanes = mL;
	}
	
	public int getMaxLanes() {
		return maxLanes;
	}
	
	public int getNumKeys() {
		return keysCollected;
	}
	
	public void setNumKeys(int k){
		keysCollected = 0;
	}
	
	public void keyFound() {
		keysCollected++;
	}
	
	public void setKeyboardControls(boolean k) {
		isKeyboardControls = k;
	}
	
	public boolean isKeyboardControls() {
		return isKeyboardControls;
	}

	
}
