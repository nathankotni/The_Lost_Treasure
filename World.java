import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;





/**
 * A World is an extension of a Pane that holds Actors (which are custom ImageView objects). 
 * It already keeps track of its children Nodes. If you want a list of Actors in the World, call 
 * getChildren() - assuming you have only added Actors and not other types of Nodes. A World 
 * contains one private attribute: private AnimationTimer timer. Inside the handle() method, 
 * the World will act() and then go through the list of all Actors in the World and tell each 
 * of them to act()
 */
public abstract class World extends javafx.scene.layout.Pane{
	
	private State state;
	
	
	
	// the timer for the animation
	private AnimationTimer timer;
	
	
	private long milliseconds;
	
	/**
	 * Creates a world. The world will initially be a size determined 
	 * by its parent Node. When start() is called, the world will begin 
	 * to call act on itself and on each of its Actor children every frame.
	 */
	public World() {
		// class for the SceneListener at the bottom
		this.sceneProperty().addListener(new SceneListener());
			
		milliseconds = 0;
		
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				milliseconds = now / 1000000;
				act(now);
				for(int i = 0; i < getChildren().size(); i++) {
					if(getChildren().get(i) instanceof Actor) {
						((Actor) getChildren().get(i)).act(now);
					}	
				}
			}
		};
	}
	
	
	public long getCurrentTime() {
		return milliseconds;
	}
	
	
	/**
	 * Adds the given actor to the world.
	 * 
	 * @param actor the actor to add
	 */
	public void add(Actor actor) {
		getChildren().add(actor);
		
	}
	
	/**
	 * Returns a list of all the actors in the world of the given class.
	 * 
	 * @param <A> the type of actors
	 * @param cls the type of actors that will be in the list
	 * @return a list of all the actors in the world of the given class
	 */
	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls) {
		ArrayList<A> list = new ArrayList<A>(); 
		
		for(int i = 0; i < getChildren().size(); i++) {
			if(getChildren().get(i).getClass().equals(cls)) {
				list.add((A) getChildren().get(i));
			}
		}
		
		return list;
	}
	
	
	
	/**
	 * Removes the given actor from the world. 
	 * 
	 * @param actor the actor to remove
	 */
	public void remove(Actor actor) {
		getChildren().remove(actor);
		
	}
	
	/**
	 * Starts the timer that calls the act method on the world 
	 * and on each Actor in the world each frame.
	 */
	public void start() {
		timer.start();
		
	}
	
	/**
	 * Stops the timer that calls the act method.
	 */
	public void stop() {
		timer.stop();
		
	}
	
	public void setState(State s) {
		if(state != null) {
			state.onRemove();
		}
		state = s;
		state.onSet();
	}
	
	public State getState() {
		return state;
	}
	
	/** 
	 * This method is called every frame once start has been called.
	 * 
	 * @param now the current time in nanoseconds
	 */
	public abstract void act(long now);
}

/**
 * Calls all the events for all the nodes and the world when there is keyboard input
 */
class SceneListener implements ChangeListener<Scene>{

    @Override
    public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
        // if scene is not default
        if(newValue != null) {
            // if the world responds to key events - (getRoot() is to get parent node of scene)
            if(newValue.getRoot().getOnKeyPressed() != null) {
                newValue.getOnKeyPressed().handle(null);
            }
            // if the actors (individually) respond to key events - (getChildrenUnmodifiable() unmodifiable is an unmodifiable list of the parent's children)
            for(int i = 0; i < newValue.getRoot().getChildrenUnmodifiable().size(); i++) {
                if(newValue.getRoot().getChildrenUnmodifiable().get(i).getOnKeyPressed() != null) {
                    newValue.getRoot().getChildrenUnmodifiable().get(i).getOnKeyPressed().handle(null);
                }
            }
        }

    }
}

