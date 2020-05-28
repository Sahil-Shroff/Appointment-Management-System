package application;

import java.sql.SQLException;

import database.appointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AppointmentsPage {
	
	static int flag = 1;
	static Patient temp;
	
	static ObservableList<Patient> newAppPat = Model.getAllNewAppointments();
	public static ComboBox<String> genderChoice;
	public static TextField nameEntry;
	static Button add;
	static TableView<Patient> newAppointments;
	static TableView<Patient> newConsulteds;
	
	static Text nameErrorMessage;
	
	public static VBox createNewAppointment() {
		
		Text addPatient = new Text("Add Patient");
		addPatient.setStyle("-fx-font-size: 25;" +
							"-fx-padding: 0 0 0 20;"
				);
		
		Label name = new Label("Name");
		nameEntry = new TextField();
		nameEntry.setPrefWidth(300.0);
		
		Label gender = new Label("Gender");
		String genders[] = {"Male", "Female"};
		genderChoice = new ComboBox<>(FXCollections.observableArrayList(genders));
		genderChoice.getSelectionModel().select("Male");
		genderChoice.setStyle("-fx-background-color: #2997ff;");
		
		nameEntry.focusedProperty().addListener((e) -> {
			if (flag == 1)
				nameEntry.setText("deception");
		});		//for distraction
		nameEntry.textProperty().addListener((ov, os, ns) -> {
			if (flag == 1) {
				flag = 0;
				temp = new Patient();
				nameEntry.textProperty().bindBidirectional(temp.nameProperty());
				newAppPat.add(temp);
			}
		});
		nameEntry.setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER))
				genderChoice.requestFocus();
		});
		
		add = new Button("Add");
		add.setStyle("-fx-background-color: #2997ff;" +
					 "-fx-padding: 4 30 4 30;"				//for size
				);
		add.setOnAction((e) -> {
			gender.requestFocus();
			nameEntry.textProperty().unbindBidirectional(temp.nameProperty());
			if (flag == 0 && newAppPat.size() != 0)
				newAppPat.remove(newAppPat.size() - 1);
			
			try {
				int id = appointmentDAO.insertPatient();
				newAppPat.add(new Patient(id, nameEntry.getText(), genderChoice.getSelectionModel().getSelectedItem()));
				nameEntry.clear();
				flag = 1;
				nameEntry.requestFocus();
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		});
		
		genderChoice.setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER))
				add.fire();
		});
		
		HBox nameBox = new HBox(name, nameEntry);
		nameBox.setSpacing(12.0);
		HBox genderBox = new HBox(gender, genderChoice);
		genderBox.setSpacing(5.0);
		HBox btnWrapper = new HBox(add);					//for padding of btn
		btnWrapper.setStyle("-fx-padding: 0 0 0 80;");
		
		VBox patientDetails = new VBox(addPatient, nameBox, genderBox, btnWrapper);
		patientDetails.setStyle("-fx-padding: 8 4 4 4;" + 
								"-fx-background-color: #ffffff;"
				);
		patientDetails.setSpacing(10.0);
		
		return patientDetails;
	}
	
	public static VBox createErrorDisplay() {
		Text displayHead = new Text("Message logs");
		displayHead.setStyle("-fx-color: red;" +
				"-fx-padding: 0 0 0 20;" +
				"-fx-font-size: 20;"
				);
		
		nameErrorMessage = new Text("Please enter a valid name");
		nameErrorMessage.setStyle("-fx-color: red;" +
				"-fx-font-size: 10;"
				);
		
		VBox errorDisplay = new VBox(displayHead, nameErrorMessage);
		errorDisplay.setSpacing(5.0);
		errorDisplay.setPrefWidth(200.0);
		errorDisplay.setStyle("-fx-background-color: #ffffff;" +
				"-fx-padding: 6 4 4 4;");
		
		return errorDisplay;
	}
	
	public static VBox createStatusDisplay() {
		Text statusHead = new Text("Status");
		statusHead.setStyle("-fx-color: red;" +
							"-fx-padding: 0 0 0 20;" +
							"-fx-font-size: 25;"
				);
		
		Text limit = new Text("Limit: " + "0");
		limit.setStyle("-fx-padding: 0 0 0 10;");
		Text income = new Text("Today's Income: " + "0");
		income.setStyle("-fx-padding: 0 0 0 10;");
		
		VBox statusDisplay = new VBox(statusHead, limit, income);
		statusDisplay.setSpacing(5.0);
		statusDisplay.setPrefWidth(200.0);
		statusDisplay.setStyle("-fx-background-color: #ffffff;" +
								"-fx-padding: 6 4 4 4;");
		
		return statusDisplay;
	}
	
	public static TabPane createTabs() {		
		return TabPaneAppointments.createTabs();
	}
	
	public static HBox createUpperDashBoard() {
		HBox upperDashBoard = new HBox(createNewAppointment(),
					createStatusDisplay(), createErrorDisplay());
		upperDashBoard.setSpacing(20.0);
		return upperDashBoard;
	}
	
}
