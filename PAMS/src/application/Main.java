package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

import controllers.DisplayController;
import database.MySQLJDBCUtil;


public class Main extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	    int height = screenHeight - screenHeight / 16;
	    try {
			MySQLJDBCUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	@Override
	public void stop() {
		try {
			MySQLJDBCUtil.dbDisconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
