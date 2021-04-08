import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

public class GameWonState extends State{

	public GameWonState(World w) {
		super(w);
	}

	@Override
	public void onSet() {
		
		BorderPane pane = (BorderPane) getWorld().getParent();
		
		Button startButton = new Button("Replay Game");
		startButton.setMinWidth(320);
		startButton.setMinHeight(75);
		startButton.setLayoutX(pane.getWidth() * 4 / 13 - (startButton.getMinWidth() / 2));
		startButton.setLayoutY(pane.getHeight() / 2 - (startButton.getMinHeight() / 2));
		startButton.setBorder(new Border(new BorderStroke(Color.GOLD,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		startButton.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(5), new Insets(3))));
		startButton.setFont(Font.font("Arial",  FontWeight.BOLD, 40));
		
		Text name;
		if(((SeaWorld)getWorld()).getNumKeys() == 5) {
			getWorld().setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(0), new Insets(0))));
			name = new Text("CONGRATULATIONS!\r          YOU WON!");
			name.setWrappingWidth(550);
			name.setX(pane.getWidth() / 2 - (name.getWrappingWidth() / 2));
			name.setY(pane.getHeight() / 3);
			name.setFill(Color.GOLD);
			name.setFont(new Font("Cochin Bold", 50));
			startButton.setBorder(new Border(new BorderStroke(Color.GOLD,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
			startButton.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, new CornerRadii(5), new Insets(3))));
		}else {
			getWorld().setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(0), new Insets(0))));
			name = new Text("THE TREASURE\r   IS LOCKED!!!");
			name.setWrappingWidth(485);
			name.setX(pane.getWidth() / 2 - (name.getWrappingWidth() / 2));
			name.setY(pane.getHeight() * 3 / 10);
			name.setFill(Color.DARKGREEN);
			name.setFont(new Font("Cochin Bold", 60));
			startButton.setBorder(new Border(new BorderStroke(Color.OLIVEDRAB,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
			startButton.setBackground(new Background(new BackgroundFill(Color.PALETURQUOISE, new CornerRadii(5), new Insets(3))));
		}

		
		Button mainMenu = new Button("Main Menu");
		mainMenu.setMinWidth(300);
		mainMenu.setMinHeight(75);
		mainMenu.setLayoutX(pane.getWidth() * 9 / 13 - (mainMenu.getMinWidth() / 2));
		mainMenu.setLayoutY(pane.getHeight() / 2 - (mainMenu.getMinHeight() / 2));
		mainMenu.setBorder(new Border(new BorderStroke(Color.DIMGRAY,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		mainMenu.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(3))));
		mainMenu.setFont(Font.font("Arial",  FontWeight.BOLD, 40));
		getWorld().getChildren().add(mainMenu);
		
		mainMenu.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				getWorld().setState(new TitleState(getWorld()));
			}
			
		});
		
		Light.Distant light = new Light.Distant();
		light.setAzimuth(-150.0);
		light.setElevation(45.0);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(8.0);
		name.setEffect(lighting);
		
		getWorld().getChildren().add(name);
	
		
		
		getWorld().getChildren().add(startButton);
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				getWorld().setState(new GameState(getWorld()));
			}
			
		});
	}

	@Override
	public void onRemove() {
		getWorld().getChildren().removeAll(getWorld().getChildren());
		((BorderPane)getWorld().getParent()).setBackground(null);
		getWorld().setBackground(null);
	}

	@Override
	public void onAct() {
		
	}

}
