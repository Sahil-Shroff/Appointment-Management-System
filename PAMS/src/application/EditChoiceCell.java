package application;

import java.util.Optional;

import database.appointmentDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

public class EditChoiceCell extends TableCell<Patient, String> {

    ChoiceBox<String> genderChoice = new ChoiceBox<>();

    public EditChoiceCell(TableColumn<Patient, String> col) {
        genderChoice.getItems().addAll("Male", "Female");
        genderChoice.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
        	//if (oldValue == null)
        	//	genderChoice.getSelectionModel().select(( (Patient) getTableView().getItems().get(getIndex())).getGender());
            String value = genderChoice.getItems().get(newValue.intValue());
            processEdit(value);
        });

    }

    private void processEdit(String value) {
        commitEdit(value);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void commitEdit(String value) {
        super.commitEdit(value);
        // ((Item) this.getTableRow().getItem()).setName(value);
        setGraphic(null);
    }

    @Override
    public void startEdit() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(Main.stage);
		alert.initStyle(StageStyle.TRANSPARENT);
    	alert.setTitle("Confirm");
    	String s = "Are you sure you want to make changes?";
    	alert.setContentText(s);
    	 
    	Optional<ButtonType> result = alert.showAndWait();
    	 
    	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
    		super.startEdit();
            String value = getItem();
            if (value != null) {
                setGraphic(genderChoice);
                setText(null);
            }
    	}
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);

        } else {
            setText(item);
        }
    }

}

