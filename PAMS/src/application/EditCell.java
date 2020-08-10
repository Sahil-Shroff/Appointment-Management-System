package application;


import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class EditCell < S, T > extends TextFieldTableCell < S, T > {
    private TextField textField;
    private boolean escapePressed = false;
    private TablePosition < S,
    ? > tablePos = null;
    
    public EditCell(final StringConverter < T > converter) {
        super(converter);
    }
    
    public static <S> Callback <TableColumn <S, String>, TableCell <S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }
    
    public static <S, T> Callback <TableColumn <S, T>, TableCell<S, T>> forTableColumn(
        final StringConverter < T > converter) {
        return list -> new EditCell < S, T > (converter);
    }
    
    @SuppressWarnings("unused")
	public static void setTableEditable(TableView<Patient> tableView) {
        tableView.setEditable(true);
        // allows the individual cells to be selected
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        
        tableView.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell(tableView);
            } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
                tableView.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // work around due to
                // TableView.getSelectionModel().selectPrevious() due to a bug
                // stopping it from working on
                // the first column in the last row of the table
                selectPrevious(tableView);
                event.consume();
            }
        });
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
    private static void editFocusedCell(TableView<Patient> tableView) {
        final TablePosition < Patient, ? > focusedCell = tableView
            .focusModelProperty().get().focusedCellProperty().get();
        tableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
    private static void selectPrevious(TableView<Patient> tableView) {
        if (tableView.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition < Patient, ? > pos = tableView.getFocusModel()
                .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                tableView.getSelectionModel().select(pos.getRow(),
                    getTableColumn(tableView, pos.getTableColumn(), -1));
            } else if (pos.getRow() < tableView.getItems().size()) {
                // wrap to end of previous row
                tableView.getSelectionModel().select(pos.getRow() - 1,
                    tableView.getVisibleLeafColumn(
                        tableView.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = tableView.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                tableView.getSelectionModel().select(tableView.getItems().size() - 1);
            } else if (focusIndex > 0) {
                tableView.getSelectionModel().select(focusIndex - 1);
            }
        }
    }
    private static TableColumn < Patient, ? > getTableColumn(
    	TableView<Patient> tableView, final TableColumn < Patient, ? > column, int offset) {
        int columnIndex = tableView.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return tableView.getVisibleLeafColumn(newColumnIndex);
    }
    
    @Override
    public void startEdit() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(Main.stage);
		alert.initStyle(StageStyle.TRANSPARENT);
    	alert.setTitle("Confirm");
    	String s = "Do you want to change name?";
    	alert.setContentText(s);
    	 
    	Optional<ButtonType> result = alert.showAndWait();
    	 
    	if ((result.isPresent()) && (result.get() == ButtonType.OK)) { 
    		if (!isEditable() || !getTableView().isEditable() ||
    	            !getTableColumn().isEditable()) {
    	            return;
    	        }
    	        super.startEdit();
    	        if (isEditing()) {
    	            if (textField == null) {
    	                textField = getTextField();
    	            }
    	            escapePressed = false;
    	            startEdit(textField);
    	            final TableView < S > table = getTableView();
    	            tablePos = table.getEditingCell();
    	        }
    	}
    }
    /** {@inheritDoc} */
    @Override
    public void commitEdit(T newValue) {
        if (!isEditing())
            return;
        final TableView < S > table = getTableView();
        if (table != null) {
            // Inform the TableView of the edit being ready to be committed.
            CellEditEvent editEvent = new CellEditEvent(table, tablePos,
                TableColumn.editCommitEvent(), newValue);
            Event.fireEvent(getTableColumn(), editEvent);
        }
        // we need to setEditing(false):
        super.cancelEdit(); // this fires an invalid EditCancelEvent.
        // update the item within this cell, so that it represents the new value
        updateItem(newValue, false);
        if (table != null) {
            // reset the editing cell on the TableView
            table.edit(-1, null);
        }
    }
    /** {@inheritDoc} */
    @Override
    public void cancelEdit() {
        if (escapePressed) {
            // this is a cancel event after escape key
            super.cancelEdit();
            setText(getItemText()); // restore the original text in the view
        } else {
            // this is not a cancel event after escape key
            // we interpret it as commit.
            String newText = textField.getText();
            // commit the new text to the model
            this.commitEdit(getConverter().fromString(newText));
        }
        setGraphic(null); // stop editing with TextField
    }
    /** {@inheritDoc} */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        updateItem();
    }
    private TextField getTextField() {
        final TextField textField = new TextField(getItemText());
        textField.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("hi");
            }
        });
        // Use onAction here rather than onKeyReleased (with check for Enter),
        textField.setOnAction(event -> {
            if (getConverter() == null) {
                throw new IllegalStateException("StringConverter is null.");
            }
            this.commitEdit(getConverter().fromString(textField.getText()));
            event.consume();
        });
        textField.focusedProperty().addListener((ob, oldValue, newValue) -> {
        	if (!newValue) {
                commitEdit(getConverter().fromString(textField.getText()));
            }
        });
        /*textField.focusedProperty().addListener(new ChangeListener <Boolean> () {
            @Override
            public ChangeListener <Boolean> changed(ObservableValue << ? extends Boolean > observable,
                Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    commitEdit(getConverter().fromString(textField.getText()));
                }
            }
        });*/
        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ESCAPE)
                escapePressed = true;
            else
                escapePressed = false;
        });
        textField.setOnKeyReleased(t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                throw new IllegalArgumentException(
                    "did not expect esc key releases here.");
            }
        });
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                textField.setText(getConverter().toString(getItem()));
                cancelEdit();
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
                getTableView().getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                getTableView().getSelectionModel().selectPrevious();
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                getTableView().getSelectionModel().selectAboveCell();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                getTableView().getSelectionModel().selectBelowCell();
                event.consume();
            }
        });
        return textField;
    }
    private String getItemText() {
        return getConverter() == null ?
            getItem() == null ? "" : getItem().toString() :
            getConverter().toString(getItem());
    }
    private void updateItem() {
        if (isEmpty()) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getItemText());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getItemText());
                setGraphic(null);
            }
        }
    }
    private void startEdit(final TextField textField) {
        if (textField != null) {
            textField.setText(getItemText());
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
        // requesting focus so that key input can immediately go into the
        // TextField
        textField.requestFocus();
    }
}
