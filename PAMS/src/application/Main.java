package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.sql.SQLException;
import controllers.DisplayController;
import database.MySQLJDBCUtil;

public class Main extends Application {
	
	public static Stage stage = null;
	public static DisplayController controller;
	
	@Override
	public void start(Stage stage) throws IOException {
		ApplicationState.setAppointmentDisplayOn(true);
		ApplicationState.markUpdated();
		CheckForUpdates.pollDatabase();
		Main.stage = stage;
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	    int height = screenHeight - screenHeight / 16;
	    try {
			MySQLJDBCUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		FXMLLoader fxml = new FXMLLoader(Main.class.getResource("../controllers/Display.fxml"));
		VBox app = fxml.load();
		app.setStyle("-fx-background-color: white;");
		
		Scene scene = new Scene(app, screenWidth, height);
		Main.controller = fxml.getController();
		Main.controller.config(scene);
		
		stage.setScene(scene);
		stage.setTitle("Doctor");
		stage.setFullScreen(true);
		stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.isShiftDown() && e.getCode() == KeyCode.ESCAPE) {
				if (stage.isFullScreen())
					stage.setFullScreen(false);
				else
					stage.setFullScreen(true);
			}
		});
		stage.show();
	}
	
	@Override
	public void stop() {
		try {
			CheckForUpdates.closeConnection();
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
