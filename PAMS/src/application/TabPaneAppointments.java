package application;


import java.sql.SQLException;

import database.appointmentDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class TabPaneAppointments {
	
	static ComboBox<String> genderChoice = AppointmentsPage.genderChoice;
	static TextField nameEntry = AppointmentsPage.nameEntry;
	static TableView<Patient> newAppointments = AppointmentsPage.newAppointments;
	static TableView<Patient> newConsulteds = AppointmentsPage.newConsulteds;
	static ObservableList<Patient> newAppPat = AppointmentsPage.newAppPat;
	
	public static TabPane createTabs() {
		Tab pendingPatients = new Tab("Pending Patients");
		pendingPatients.setContent(getAllNewAppointments());
		pendingPatients.setClosable(false);
		
		Tab consultedPatients = new Tab("Consulted Patients");
		consultedPatients.setContent(getAllNewConsulteds());
		consultedPatients.setClosable(false);
		
		TabPane tabPaneAppointments = new TabPane();
		tabPaneAppointments.getTabs().addAll(pendingPatients, consultedPatients);
		tabPaneAppointments.setStyle("-fx-background-color: #ffffff;" +
						 "-fx-padding: 2;"
				);
		
		return tabPaneAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getAllNewAppointments() {
		newAppointments = new TableView<>(newAppPat);
		
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(newAppointments.getItems().indexOf(column.getValue())));
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		nameCol.setPrefWidth(300.0);

		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		
		TableColumn<Patient, Button> removeCol = new TableColumn<>();
		removeCol.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("Remove", (Patient p) -> {
			try {
				appointmentDAO.deleteAppoint(p.getId());
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    newAppointments.getItems().remove(p);
		    return p;
		})); 
		
		newAppointments.getColumns().addAll(indexColumn, nameCol, genderCol, removeCol);
		newAppointments.setPrefHeight(500.0);
		
		newAppointments.setOnMouseClicked((e) -> {
			if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && newAppointments.getItems().size() != 0) {
				newConsulteds.getItems().add(newAppointments.getSelectionModel().getSelectedItem());
				newAppointments.getItems().remove(newAppointments.getSelectionModel().getSelectedItem());
			}
		});
		
		setTableEditable();
		nameCol.setCellFactory(
	            EditCell.< Patient> forTableColumn());
	        // updates the salary field on the PersonTableData object to the
	        // committed value
	    nameCol.setOnEditCommit(event -> {
	            final String value = event.getNewValue() != null ?
	            event.getNewValue() : event.getOldValue();
	            if (event.getTablePosition().getRow() < newAppPat.size())
	            	((Patient) event.getTableView().getItems()
	                .get(event.getTablePosition().getRow())).setName(value);
	            newAppointments.refresh();
	        });
		
		return newAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getAllNewConsulteds() {
		newConsulteds = new TableView<>(Model.getAllNewAppointments());
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		
		TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
		
		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		
		newConsulteds.getColumns().addAll(nameCol, ageCol, genderCol);
		return newConsulteds;
	}
	
    private static void setTableEditable() {
        newAppointments.setEditable(true);
        // allows the individual cells to be selected
        newAppointments.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        newAppointments.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell();
            } else if (event.getCode() == KeyCode.RIGHT ||
                event.getCode() == KeyCode.TAB) {
                newAppointments.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // work around due to
                // TableView.getSelectionModel().selectPrevious() due to a bug
                // stopping it from working on
                // the first column in the last row of the table
                selectPrevious();
                event.consume();
            }
        });
    }
    @SuppressWarnings("unchecked")
    private static void editFocusedCell() {
        final TablePosition < Patient, ? > focusedCell = newAppointments
            .focusModelProperty().get().focusedCellProperty().get();
        newAppointments.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }
    @SuppressWarnings("unchecked")
    private static void selectPrevious() {
        if (newAppointments.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition < Patient, ? > pos = newAppointments.getFocusModel()
                .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                newAppointments.getSelectionModel().select(pos.getRow(),
                    getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < newAppointments.getItems().size()) {
                // wrap to end of previous row
                newAppointments.getSelectionModel().select(pos.getRow() - 1,
                    newAppointments.getVisibleLeafColumn(
                        newAppointments.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = newAppointments.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                newAppointments.getSelectionModel().select(newAppointments.getItems().size() - 1);
            } else if (focusIndex > 0) {
                newAppointments.getSelectionModel().select(focusIndex - 1);
            }
        }
    }
    private static TableColumn < Patient, ? > getTableColumn(
        final TableColumn < Patient, ? > column, int offset) {
        int columnIndex = newAppointments.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return newAppointments.getVisibleLeafColumn(newColumnIndex);
    }

}
