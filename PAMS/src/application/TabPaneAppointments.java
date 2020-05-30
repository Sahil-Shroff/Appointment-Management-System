package application;


import java.sql.SQLException;
import java.util.Optional;

import database.appointmentDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.StageStyle;

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
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.stage);
			alert.initStyle(StageStyle.TRANSPARENT);
        	alert.setTitle("Confirm");
        	String s = "Are you sure you want to remove?";
        	alert.setContentText(s);
        	 
        	Optional<ButtonType> result = alert.showAndWait();
        	 
        	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
        		try {
    				if (p.getId() != 0)
    					appointmentDAO.deleteAppoint(p.getId());
    					newAppointments.getItems().remove(p);
    			} catch (ClassNotFoundException | SQLException e1) {
    				e1.printStackTrace();
    			}      	    
        	}
			return p;
		})); 
		
		newAppointments.getColumns().addAll(indexColumn, nameCol, genderCol, removeCol);
		newAppointments.setPrefHeight(500.0);
		
		setTableEditable();
		nameCol.setCellFactory(
	            EditCell.< Patient> forTableColumn());
	        // updates the salary field on the PersonTableData object to the
	        // committed value
	    nameCol.setOnEditCommit(event -> {
	        final String value = event.getNewValue() != null ?
	        event.getNewValue() : event.getOldValue();
	        Patient p = (Patient) event.getTableView().getItems().get(event.getTablePosition().getRow());
	        if (event.getTablePosition().getRow() < newAppPat.size())
	        	p.setName(value);
	        appointmentDAO.updateName(p.getId(), value);
	        newAppointments.refresh();
	    });
		
		return newAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getAllNewConsulteds() {
		newConsulteds = new TableView<>(Model.getAllNewConsulteds());
		
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(newConsulteds.getItems().indexOf(column.getValue())));
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		nameCol.setPrefWidth(300.0);
		
		TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
		
		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		
		TableColumn<Patient, Integer> feesCol = new TableColumn<>("Fees");
		feesCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("fees"));
		
		TableColumn<Patient, String> feesPaidCol = new TableColumn<>("Payment Status");
		feesPaidCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("feesPaid"));
		feesPaidCol.setPrefWidth(200.0);
		
		TableColumn<Patient, Button> payCol = new TableColumn<>();
		payCol.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("Pay/UnPay", (Patient p) -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.stage);
			alert.initStyle(StageStyle.TRANSPARENT);
        	alert.setTitle("Confirm");
        	String s = "Are you sure to click fees pay/unpay?";
        	alert.setContentText(s);
        	 
        	Optional<ButtonType> result = alert.showAndWait();
        	 
        	if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
        		newConsulteds.getColumns().get(0).setVisible(false);
    			newConsulteds.getColumns().get(0).setVisible(true);
    			appointmentDAO.payFees(!p.isFeesPaid(), p.getId(), p);        	    
        	}
			 return p;
		}));
		
		newConsulteds.getColumns().addAll(indexColumn, nameCol, ageCol, genderCol, feesCol, feesPaidCol, payCol);
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
