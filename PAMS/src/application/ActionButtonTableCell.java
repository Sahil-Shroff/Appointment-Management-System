package application;

import java.util.Optional;
import java.util.function.Function;

import database.appointmentDAO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private Button actionButton;

    public ActionButtonTableCell(String label, Function< S, S> function) {
        this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> {
        	if	(label.equalsIgnoreCase("Prescribe")) {
        		function.apply(getCurrentItem());
        		return;
        	}
        	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.stage);
			alert.initStyle(StageStyle.TRANSPARENT);
        	alert.setTitle("Confirm");
        	String s = "Are you sure you want to " + label + "?";
        	alert.setContentText(s);
        	 
        	Optional<ButtonType> result = alert.showAndWait();
        	 
        	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
        		function.apply(getCurrentItem());
        	}
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function< S, S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }
    
    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {                
            setGraphic(actionButton);
        }
    }
}
