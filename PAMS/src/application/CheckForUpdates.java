package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class CheckForUpdates {
	
	private static Connection pollingConn = null;
	public static Button update = null;
	public static Thread t;
	
	public static void pollDatabase() {
		t = new Thread(() -> {
			try (FileInputStream f = new FileInputStream("db.properties")) {
	            Properties pros = new Properties();
	            pros.load(f);
	            String url = pros.getProperty("url");
	            String user = pros.getProperty("user");
	            String password = pros.getProperty("password");
	            try {
					pollingConn = DriverManager.getConnection(url, user, password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
			while (true) {
				if (ApplicationState.isAppointmentDisplayOn() && sqlQuery()) {
					Platform.runLater(() -> {
						if (update != null) update.fire();
					});
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.setName("Poll Database");
		t.setDaemon(true);
		t.start();
	}
	
	public static boolean sqlQuery() {
		String sql = "SELECT last_update FROM app_state";
		try {
			PreparedStatement psmt = pollingConn.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			rs.next();
			return rs.getTimestamp(1).compareTo(ApplicationState.getLastApplicationUpdate()) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void closeConnection() {
		try {
            if (pollingConn != null && !pollingConn.isClosed()) {
                pollingConn.close();
                t.stop();
            }
        } catch (Exception e){
           e.printStackTrace();
        }
	}
}
