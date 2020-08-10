package application;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

public class EditCheckCell extends TableCell<Patient, Boolean> {
    private CheckBox checkBox;

    public EditCheckCell() {
        checkBox = new CheckBox("select");
        //checkBox.setDisable(true);
        checkBox.selectedProperty().addListener((ov, oldValue, newValue) -> {
        	if (isEditing())
                commitEdit(newValue == null ? false : newValue);
        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        checkBox.setDisable(true);
    }

    public void commitEdit(Boolean value) {
        super.commitEdit(value);

        checkBox.setDisable(true);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            checkBox.setSelected(item);
        }
    }
}
