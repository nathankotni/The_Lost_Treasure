import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class SettingsState extends State {

	public SettingsState(World w) {
		super(w);
		
	}

	@Override
	public void onSet() {
		BorderPane pane = (BorderPane) getWorld().getParent();
		
		getWorld().setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), new Insets(0))));
		((BorderPane)getWorld().getParent()).setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), new Insets(0))));
		
		Text name = new Text("SETTINGS");
		name.setWrappingWidth(490);
		name.setX(pane.getWidth() / 2 - (name.getWrappingWidth() / 2));
		name.setY(pane.getHeight() * 2 / 9);
		name.setFill(Color.GRAY);
		name.setFont(new Font("IMPACT", 130));
		

		Light.Distant light = new Light.Distant();
		light.setAzimuth(200.0);
		light.setElevation(30.0);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(8.0);
		name.setEffect(lighting);
		
		getWorld().getChildren().add(name);
		
		Button keyboard = new Button("Keyboard Controls");
		keyboard.setMinWidth(430);
		keyboard.setMinHeight(75);
		keyboard.setLayoutX(pane.getWidth() / 2 - (keyboard.getMinWidth() / 2));
		keyboard.setLayoutY(pane.getHeight() * 5 / 15 - (keyboard.getMinHeight() / 2));
		if(((SeaWorld)getWorld()).isKeyboardControls()) {
			keyboard.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
			keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
		}else {
			keyboard.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
			keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
		}
		keyboard.setFont(Font.font("Arial",  FontWeight.BOLD, 40));
		getWorld().getChildren().add(keyboard);
		
		Button mouse = new Button("Mouse Controls");
		mouse.setMinWidth(400);
		mouse.setMinHeight(75);
		mouse.setLayoutX(pane.getWidth() / 2 - (mouse.getMinWidth() / 2));
		mouse.setLayoutY(pane.getHeight() * 8 / 15 - (mouse.getMinHeight() / 2));
		if(((SeaWorld)getWorld()).isKeyboardControls()) {
			mouse.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
			mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
		}else {
			mouse.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
			mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
		}
		mouse.setFont(Font.font("Arial",  FontWeight.BOLD, 40));
		getWorld().getChildren().add(mouse);
		
		Button cancel = new Button("X");
		cancel.setMinWidth(40);
		cancel.setMinHeight(35);
		cancel.setLayoutX((cancel.getMinWidth() / 2));
		cancel.setLayoutY((cancel.getMinHeight() / 2));
		cancel.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(4))));
		cancel.setBackground(new Background(new BackgroundFill(Color.INDIANRED, new CornerRadii(5), new Insets(3))));
		cancel.setFont(Font.font("Arial",  FontWeight.BOLD, 15));
		getWorld().getChildren().add(cancel);
		
		TextFlow textFlow = new TextFlow(); 
		
		Text controls = new Text("Keyboard Controls: - Left Arrow moves the boat to the lane to the left\r                                       - Right Arrow moves the boat to the lane to the right"
				+ "\rMouse Controls: - Clicking on lanes to the left moves the boat to the lane to the left \r                                 - Clicking on lanes to the right moves the boat to the lane to the right\r\r\r");
		
		
		
		controls.setFill(Color.MAROON);
		controls.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 20));
		
		textFlow.getChildren().add(controls);

		textFlow.setLineSpacing(15.0f); 
		textFlow.setTextAlignment(TextAlignment.CENTER);
		
		VBox vbox = new VBox(textFlow); 
		   
        vbox.setAlignment(Pos.CENTER); 
		
		getWorld().getChildren().add(vbox);
		
		((BorderPane)getWorld().getParent()).setBottom(vbox);
		
		keyboard.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(!((SeaWorld)getWorld()).isKeyboardControls()) {
					keyboard.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
					keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
					
					mouse.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
					mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
					
					((SeaWorld)getWorld()).setKeyboardControls(true);
				}else {
					mouse.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
					mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
					
					keyboard.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
					keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
					
					((SeaWorld)getWorld()).setKeyboardControls(false);
				}
			}
			
		});
		
		mouse.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(!((SeaWorld)getWorld()).isKeyboardControls()) {
					keyboard.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
					keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
					
					mouse.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
					mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
					
					((SeaWorld)getWorld()).setKeyboardControls(true);
				}else {
					mouse.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,  BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(7))));
					mouse.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), new Insets(3))));
					
					keyboard.setBorder(new Border(new BorderStroke(Color.MAROON,  BorderStrokeStyle.DASHED, new CornerRadii(8), new BorderWidths(7))));
					keyboard.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL, new CornerRadii(5), new Insets(3))));
					
					((SeaWorld)getWorld()).setKeyboardControls(false);
				}
				
			}
			
		});
		
		cancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				getWorld().setState(new TitleState(getWorld()));
				
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
