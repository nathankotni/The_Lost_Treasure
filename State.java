
public abstract class State {

	private World world;
	
	public State(World w) {
		
		world = w;
			
	}
	
	
	
	public World getWorld() { return world; }
	
	public abstract void onSet();
	
	public abstract void onRemove();
		
	public abstract void onAct();
}
