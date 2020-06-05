package controllers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import application.AppointmentsPage;
import application.Main;
import application.TabPaneAppointments;
import database.appointmentDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

public class DisplayController {
	
	@FXML
	private Button accountBtn;
	
	@FXML
	private Button appointmentsBtn;
	
	@FXML
	private VBox contentBox;
	
	@FXML
	private Text currentDate;
	
	@FXML
	private Text dateHead;
	
	@FXML
	private Button recordsBtn;
	
	@FXML
	private HBox mainBox;
	
	@FXML
	private ImageView menuBtn;
	
	@FXML
	private Button settingsBtn;
	
	@FXML
	private VBox sideMenu;
	
	@FXML
	private Text titleHead;
	
	private DoubleProperty fontSize = new SimpleDoubleProperty(34);
	private DoubleProperty refImgSizeX = new SimpleDoubleProperty(1);
	private DoubleProperty refImgSizeY = new SimpleDoubleProperty(1);
	
	public void config(Scene scene) {
		fontSize.bind(scene.widthProperty().add(scene.heightProperty()).divide(160));
		refImgSizeX.bind(scene.widthProperty().divide(1920));
		refImgSizeY.bind(scene.heightProperty().divide(1080));
		
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String strDate= formatter.format(date);
		currentDate.textProperty().set(strDate);
		//System.out.println(strDate);
		
		this.menuBtn.styleProperty().bind(Bindings.concat("-fx-scale-x: ", refImgSizeX.asString(), ";",
													 "-fx-scale-y: ", refImgSizeY.asString(), ";"
				));
		
		accountBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		appointmentsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		appointmentsBtn.setOnAction(e -> displayAppointments());
		
		recordsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		settingsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		titleHead.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.multiply(2).asString(), ";"));
		
		dateHead.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.multiply(2).asString(), ";"));
		currentDate.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.multiply(2).asString(), ";"));
		
		displayAppointments();
	}
	
	@FXML
	public void displayAppointments() {
		contentBox.getChildren().clear();
		Button btn = new Button("complete day");
		btn.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.stage);
			alert.initStyle(StageStyle.TRANSPARENT);
        	alert.setTitle("Confirm");
        	String s = "Are you sure you want to wind up?";
        	alert.setContentText(s);
        	 
        	Optional<ButtonType> result = alert.showAndWait();
        	 
        	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
        		((TabPane) ((VBox) contentBox.getChildren().get(0)).getChildren().get(1)).getSelectionModel().select(1);
        		TabPaneAppointments.newConsulteds.getColumns().get(9).setVisible(true);
        		
        		//appointmentDAO.completeSession();    	    
        	}
		});
		
		VBox appointments = new VBox(AppointmentsPage.createUpperDashBoard(),
									 AppointmentsPage.createTabs(), btn
									);
		appointments.setSpacing(10.0);
		appointments.setStyle("-fx-padding: 0 0 0 10; -fx-font-family: sansSherif;");
		contentBox.getChildren().add(appointments);
	}
	
	@FXML
	public void hideSideMenu() {
		if(this.mainBox.getChildren().size() == 2)
			this.mainBox.getChildren().remove(0);
		else
			this.mainBox.getChildren().add(0, sideMenu);
	}

}
