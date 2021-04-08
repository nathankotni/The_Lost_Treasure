import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Author: Nathan Kotni

public class Game extends Application{
	
	// launching screen
	public static void main(String[] args) {
		launch(args);		
	}
	
	// making the screen
	@Override
	public void start(Stage stage) throws Exception {
		
		// setting up the frame of the scene
		BorderPane pane = new BorderPane();
		
		// making the scene
		Scene scene = new Scene(pane, 900, 800);
		
		// making the world
		SeaWorld seaWorld = new SeaWorld();
		pane.setCenter(seaWorld);
		
		seaWorld.setState(new TitleState(seaWorld));
		
		// setting the stage
		stage.setScene(scene);
		stage.setTitle("Lost Treasure"); 
		
		// show the stage
		stage.show();
	}
}
