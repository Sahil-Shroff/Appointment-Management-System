package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Prescription {
	
	public Prescription(Patient p) {
		Stage prescriptionWindow = new Stage();
		prescriptionWindow.setWidth(7 * Main.stage.getWidth() / 12);
		prescriptionWindow.setHeight(7 * Main.stage.getHeight() / 12);
		prescriptionWindow.initOwner(Main.stage);
		prescriptionWindow.setTitle(p.getName());
		prescriptionWindow.setScene(setContent(p));
		prescriptionWindow.show();
	}
	
	private Scene setContent(Patient p) {
		Text name = new Text("Name: " + p.getName());
		Text age = new Text("Age: " + p.getAge());
		Text gender = new Text("Gender: " + p.getGender());
		HBox patientDetails = new HBox(name, age, gender);
		patientDetails.setSpacing(20);
		
		HBox medicationsHeader = new HBox(
				new Text("Medicine "), new Text("Duration "), new Text("Dose "), new Text("Remarks "));
		medicationsHeader.setSpacing(20);
		medicationsHeader.setPadding(new Insets(40, 0, 10, 5));
		//medicationsHeader.setAlignment(Pos.CENTER);
		HBox medications = new HBox();
		medications.setPadding(new Insets(40, 0, 0, 20));
		
		VBox vBox = new VBox(patientDetails, medicationsHeader, medications);
		vBox.setSpacing(40);
		vBox.setPadding(new Insets(50));
		return new Scene(vBox, 7 * Main.stage.getWidth() / 12, 7 * Main.stage.getHeight() / 12);
	}

}