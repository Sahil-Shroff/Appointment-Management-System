package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;

import controllers.DisplayController;


public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	    int height = screenHeight - screenHeight / 16; 
		FXMLLoader fxml = new FXMLLoader(Main.class.getResource("../controllers/Display.fxml"));
		VBox app = fxml.load();
		app.setStyle("-fx-background-color: white;");
		
		Scene scene = new Scene(app, screenWidth, height);
		DisplayController controller = fxml.getController();
		controller.config(scene);
		
		stage.setScene(scene);
		stage.setTitle("Reception");
		stage.setFullScreen(true);
		stage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
