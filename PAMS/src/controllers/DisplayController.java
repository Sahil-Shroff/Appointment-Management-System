package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import application.AppointmentsPage;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
		currentDate = new Text(strDate);
		System.out.println(strDate);
		
		this.menuBtn.styleProperty().bind(Bindings.concat("-fx-scale-x: ", refImgSizeX.asString(), ";",
													 "-fx-scale-y: ", refImgSizeY.asString(), ";"
				));
		
		accountBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		appointmentsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		recordsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		settingsBtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		
		titleHead.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.multiply(2).asString(), ";"));
		
		dateHead.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.multiply(2).asString(), ";"));
		
		displayAppointments();
	}
	
	@FXML
	public void displayAppointments() {
		contentBox.getChildren().clear();
		VBox appointments = new VBox(AppointmentsPage.createUpperDashBoard(),
									 AppointmentsPage.createTabs()
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
