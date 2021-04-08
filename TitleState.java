import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;

public class TitleState extends State{

	public TitleState(World w) {
		super(w);
	}

	@Override
	public void onSet() {
		
		BorderPane pane = (BorderPane) getWorld().getParent();
		
		TextFlow instructions = new TextFlow(); 
		
		Text goal = new Text("- Your main goal is to get to the island with the lost treasure\r");
		Text steps = new Text("- You will have to navigate through small, medium, and large"
				+ "\robstacles* in the raging waters between you and the island\r");
		Text keys1 = new Text("- The treasure is locked, and the 5 keys needed to open it have "
				+ "\rwashed away and are floating in the waters you are traversing \r");
		Text keys2 = new Text("- Find all the keys to get the treaure!\r");
		Text obstacles = new Text("*Obstacles: \rWooden Plank = Small Obstacle (Removes 1/3 health if hit)"
				+ "\rPile of Wooden Logs = Medium Obstacle (Removes 2/3 health if hit)"
				+ "\rBoulder = Large Obstacle (Instantly kills if hit)\r");
		
		goal.setFill(Color.BLACK);
		goal.setFont(Font.font("Arial", FontWeight.BOLD, 27));
		
		steps.setFill(Color.BLACK);
		steps.setFont(Font.font("Arial", FontWeight.BOLD, 27));
		
		keys1.setFill(Color.BLACK);
		keys1.setFont(Font.font("Arial", FontWeight.BOLD, 27));
		
		keys2.setFont(Font.font("Arial", FontWeight.BOLD, 37));
		
		obstacles.setFill(Color.MAROON);
		obstacles.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 27));
		
		instructions.getChildren().add(goal);
		instructions.getChildren().add(steps);
		instructions.getChildren().add(keys1);
		instructions.getChildren().add(keys2);
		instructions.getChildren().add(obstacles);

		instructions.setLineSpacing(15.0f); 
		instructions.setTextAlignment(TextAlignment.CENTER);
		
		
		VBox vbox = new VBox(instructions); 
		   
        vbox.setAlignment(Pos.CENTER); 
		
		getWorld().getChildren().add(vbox);
		
		((BorderPane)getWorld().getParent()).setBottom(vbox);
	
		Text name = new Text("LOST TREASURE");
		name.setWrappingWidth(725);
		name.setX(pane.getWidth() / 2 - (name.getWrappingWidth() / 2));
		name.setY(pane.getHeight() / 7);
		name.setFill(Color.STEELBLUE);
		name.setFont(new Font("Baskerville Bold", 85));

		
		
		
		Light.Distant light = new Light.Distant();
		light.setAzimuth(200.0);
		light.setElevation(30.0);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(8.0);
		name.setEffect(lighting);
		
		getWorld().getChildren().add(name);
		
		
		Button startButton = new Button("Play Game");
		startButton.setMinWidth(300);
		startButton.setMinHeight(75);
		startButton.setLayoutX(pane.getWidth() * 4 / 13 - (startButton.getMinWidth() / 2));
		startButton.setLayoutY(pane.getHeight() / 4 - (startButton.getMinHeight() / 2));
		startButton.setBorder(new Border(new BorderStroke(Color.NAVY,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		startButton.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, new CornerRadii(5), new Insets(3))));
		startButton.setFont(Font.font("Arial",  FontWeight.BOLD, 45));
		getWorld().getChildren().add(startButton);
		
		Button settings = new Button("Settings");
		settings.setMinWidth(300);
		settings.setMinHeight(75);
		settings.setLayoutX(pane.getWidth() * 9 / 13 - (settings.getMinWidth() / 2));
		settings.setLayoutY(pane.getHeight() / 4 - (settings.getMinHeight() / 2));
		settings.setBorder(new Border(new BorderStroke(Color.DIMGRAY,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
		settings.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(3))));
		settings.setFont(Font.font("Arial",  FontWeight.BOLD, 45));
		getWorld().getChildren().add(settings);
		
		settings.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				getWorld().setState(new SettingsState(getWorld()));
			}
			
		});
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				getWorld().setState(new GameState(getWorld()));
				
			}
			
		});
		
		BackgroundImage img = new BackgroundImage(new Image("title_background.png"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);
				((BorderPane)getWorld().getParent()).setBackground(new Background(img));
				getWorld().setBackground(null);
	}

	@Override
	public void onRemove() {
		getWorld().getChildren().removeAll(getWorld().getChildren());
		((BorderPane)getWorld().getParent()).setBottom(null);
		((BorderPane)getWorld().getParent()).setBackground(null);
		getWorld().setBackground(null);
	}

	@Override
	public void onAct() {
		
	}

}
