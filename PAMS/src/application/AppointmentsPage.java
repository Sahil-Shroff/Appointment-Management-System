package application;

import java.sql.SQLException;
import database.appointmentDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class AppointmentsPage {
	
	public static SimpleIntegerProperty limit = new SimpleIntegerProperty(10);
	public static SimpleIntegerProperty income = new SimpleIntegerProperty(0);
	public static ObservableList<Patient> newAppPat = Model.getAllNewAppointments();
	public static ComboBox<String> genderChoice;
	public static TextField nameEntry;
	public static TextField ageEntry;
	static Button add;
	static TableView<Patient> newAppointments;
	public static TableView<Patient> newConsulteds;
	static int flag = 1;
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
		
		Label age = new Label("Age");
		ageEntry = new TextField();
		ageEntry.setPrefWidth(40.0);
		
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
			if (flag == 0 && newAppPat.size() != 0)
				newAppPat.remove(newAppPat.size() - 1);
			
			try {
				int countAppoint = newAppPat.size();
				int id = appointmentDAO.insertPatient(0);
				int ageTemp;
				if (ageEntry.getText().equalsIgnoreCase(""))
					ageTemp = 0;
				else
					ageTemp = Integer.parseInt(ageEntry.getText());
				newAppPat.add(new Patient(id, 1 + countAppoint, ageTemp, nameEntry.getText(),
						genderChoice.getSelectionModel().getSelectedItem(), 0));
				nameEntry.clear();
				flag = 1;
				nameEntry.requestFocus();
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		});
		
		genderChoice.setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				ageEntry.requestFocus();
				ageEntry.clear();
			}
		});
		
		ageEntry.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				add.fire();
			}
		});
		
		HBox nameBox = new HBox(name, nameEntry);
		nameBox.setSpacing(12.0);
		
		HBox genderBox = new HBox(gender, genderChoice, age, ageEntry);
		genderBox.setSpacing(5.0);
		
		HBox ageBox = new HBox(age, ageEntry);
		ageBox.setSpacing(5.0);
		
		HBox AGBox = new HBox(genderBox, ageBox);
		AGBox.setSpacing(20.0);
		
		HBox btnWrapper = new HBox(add);					//for padding of btn
		btnWrapper.setStyle("-fx-padding: 0 0 0 80;");
		
		VBox patientDetails = new VBox(addPatient, nameBox, AGBox, btnWrapper);
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
		
		Text limit = new Text("Limit: " + AppointmentsPage.limit.get());
		limit.setStyle("-fx-padding: 0 0 0 10;");
		Text preincome = new Text("Today's Income: ");
		preincome.setStyle("-fx-padding: 0 0 0 10;");
		Text income = new Text();
		income.textProperty().bind(AppointmentsPage.income.asString());
		income.setStyle("-fx-padding: 0 0 0 10;");
		HBox incomeBox = new HBox(preincome, income);
		
		VBox statusDisplay = new VBox(statusHead, limit, incomeBox);
		statusDisplay.setSpacing(5.0);
		statusDisplay.setPrefWidth(200.0);
		statusDisplay.setStyle("-fx-background-color: #ffffff;" +
								"-fx-padding: 6 4 4 4;");
		//statusDisplay.setVisible(false);
		statusDisplay.setOnMouseMoved(e ->
			statusDisplay.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
		statusDisplay.setOnMouseExited(e -> 
			statusDisplay.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))));
		
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
