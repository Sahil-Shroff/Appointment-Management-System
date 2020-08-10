package application;

import database.appointmentDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

public class TabPaneAppointments {
	
	static ComboBox<String> genderChoice = AppointmentsPage.genderChoice;
	static TextField nameEntry = AppointmentsPage.nameEntry;
	static TableView<Patient> newAppointments = AppointmentsPage.newAppointments;
	public static TableView<Patient> newConsulteds = AppointmentsPage.newConsulteds;
	static ObservableList<Patient> newAppPat = AppointmentsPage.newAppPat;
	static ObservableList<Patient> futAppPat = AppointmentsPage.futAppPat;
	
	public static TabPane createTabs() {
		Tab pendingPatients = new Tab("Pending Patients");
		pendingPatients.setContent(getAllNewAppointments());
		pendingPatients.setClosable(false);
		
		Tab consultedPatients = new Tab("Consulted Patients");
		consultedPatients.setContent(getAllNewConsulteds());
		consultedPatients.setClosable(false);
		
		Tab futurePatients = new Tab("Next Session");
		futurePatients.setContent(getFutureAppointments());
		futurePatients.setClosable(false);
		
		TabPane tabPaneAppointments = new TabPane();
		tabPaneAppointments.getTabs().addAll(pendingPatients, consultedPatients, futurePatients);
		tabPaneAppointments.setStyle("-fx-background-color: #ffffff;" +
						 "-fx-padding: 2;"
				);
		
		return tabPaneAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getAllNewAppointments() {
		newAppPat = Model.getAllNewAppointments();
		newAppointments = new TableView<>(newAppPat);
		
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(newAppointments.getItems().indexOf(column.getValue()) + 1));
		
		TableColumn<Patient, Number> orderColumn = new TableColumn<Patient, Number>("Patient No.");
		orderColumn.setCellValueFactory(new PropertyValueFactory<Patient, Number>("order"));
		orderColumn.setSortType(TableColumn.SortType.ASCENDING);
		orderColumn.setPrefWidth(100);
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		nameCol.setPrefWidth(300.0);
		
		TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));

		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		genderCol.setCellFactory(col -> new EditChoiceCell(col));
		
		TableColumn<Patient, Button> prescribeBtn = new TableColumn<>();
		prescribeBtn.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("Prescribe", (Patient p) -> {
			new Prescription(p);
			return p;
		})); 
		
		TableColumn<Patient, Button> removeCol = new TableColumn<>();
		removeCol.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("Remove", (Patient p) -> {
			if (p.getId() != 0) {
				appointmentDAO.deleteAppoint(p.getId());
				newAppointments.getItems().remove(p);
			}
			return p;
		})); 
		
		newAppointments.getColumns().addAll(indexColumn, orderColumn, nameCol, ageCol, genderCol, prescribeBtn, removeCol);
		newAppointments.getSortOrder().add(orderColumn);
		newAppointments.setPrefHeight(500.0);
		
		EditCell.setTableEditable(newAppointments);
		nameCol.setCellFactory(
	            EditCell.< Patient> forTableColumn());
	        // updates the salary field on the PersonTableData object to the
	        // committed value
	    nameCol.setOnEditCommit(event -> TabPaneAppointments.<String>commitEvent(
	    		event, newAppointments, (Patient p, String value) -> {
	    	p.setName(value);
	    	appointmentDAO.updateName(p.getId(), value, false);
	    }));
	    
	    orderColumn.setCellFactory(EditCell. < Patient, Number > forTableColumn(new NumberStringConverter()));
	    orderColumn.setOnEditCommit(event -> TabPaneAppointments.<Number>commitEvent(
	    		event, newAppointments, (Patient p, Number value) -> {
	    	int localValue = (int) value;
	    	p.setOrder(localValue);
	    	appointmentDAO.updateOrder(p.getId(), localValue, false);
	    }));
	    
	    ageCol.setCellFactory(EditCell. < Patient, Integer > forTableColumn(new IntegerStringConverter()));
	    ageCol.setOnEditCommit(event -> TabPaneAppointments.<Integer>commitEvent(
	    		event, newAppointments, (Patient p, Integer value) -> {
	    	p.setAge(value);
	    	appointmentDAO.updateAge(p.getId(), value, false);
	    }));
	    
	    genderCol.setOnEditCommit(event -> TabPaneAppointments.<String>commitEvent(
	    		event, newAppointments, (Patient p, String value) -> {
	    	p.setGender(value);
			appointmentDAO.updateGender(p.getId(), value, false);
	    }));		
		return newAppointments;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getAllNewConsulteds() {
		newConsulteds = new TableView<>(Model.getAllNewConsulteds());
		
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(newConsulteds.getItems().indexOf(column.getValue()) + 1));
		
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
			appointmentDAO.payFees(!p.isFeesPaid(), p.getId(), p);
			int income = 0;
			for (Patient patient : newConsulteds.getItems()) {
				if (patient.isFeesPaid())
					income += patient.getFees();
			}
			AppointmentsPage.income.set(income);
			return p;
		}));
		
		TableColumn<Patient, String> receiptTakenCol = new TableColumn<>("Receipt/Cash");
		receiptTakenCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("receiptTaken"));
		receiptTakenCol.setPrefWidth(200.0);
		
		TableColumn<Patient, Button> receiptBtn = new TableColumn<>();
		receiptBtn.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("R/C change", (Patient p) -> {
			newConsulteds.getColumns().get(0).setVisible(false);
			newConsulteds.getColumns().get(0).setVisible(true);
			appointmentDAO.updateReceiptStatus(!p.isReceiptTaken(), p.getId(), p);
			return p;
		}));
		
		TableColumn<Patient, CheckBox> select = new TableColumn<>();
		select.setCellValueFactory(column -> {
			CheckBox checkBox = new CheckBox();
			Patient p = column.getValue();
            checkBox.selectedProperty().setValue(p.getSel());
			checkBox.selectedProperty().addListener((ov, old, n) -> {
				p.setSel(n);
				appointmentDAO.updateSelStatus(p.getSel(), p.getId(), p);
			});
			return new SimpleObjectProperty<CheckBox>(checkBox);
		});
		select.setVisible(false);
		
		newConsulteds.getColumns().addAll(indexColumn, nameCol, ageCol, genderCol, feesCol, feesPaidCol, payCol, receiptTakenCol, receiptBtn, select);
		return newConsulteds;
	}
	
	@SuppressWarnings("unchecked")
	public static TableView<Patient> getFutureAppointments() {
		futAppPat = Model.getFutureAppointments();
		TableView<Patient> futureAppointments = new TableView<>(futAppPat);
		
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(futureAppointments.getItems().indexOf(column.getValue()) + 1));
		
		TableColumn<Patient, Number> orderColumn = new TableColumn<Patient, Number>("Patient No.");
		orderColumn.setCellValueFactory(new PropertyValueFactory<Patient, Number>("order"));
		orderColumn.setSortType(TableColumn.SortType.ASCENDING);
		orderColumn.setPrefWidth(100);
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		nameCol.setPrefWidth(300.0);
		
		TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));

		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		genderCol.setCellFactory(col -> new EditChoiceCell(col));
		
		TableColumn<Patient, Button> removeCol = new TableColumn<>();
		removeCol.setCellFactory(ActionButtonTableCell.<Patient>forTableColumn("Remove", (Patient p) -> {
			if (p.getId() != 0) {
				appointmentDAO.deleteFutureAppoint(p.getId());
				futureAppointments.getItems().remove(p);
			}      	    
			return p;
		})); 
		
		futureAppointments.getColumns().addAll(indexColumn, orderColumn, nameCol, ageCol, genderCol, removeCol);
		futureAppointments.getSortOrder().add(orderColumn);
		futureAppointments.setPrefHeight(500.0);
		futureAppointments.setEditable(true);
		nameCol.setCellFactory(
	            EditCell.< Patient> forTableColumn());
	        // updates the salary field on the PersonTableData object to the
	        // committed value
	    nameCol.setOnEditCommit(event -> TabPaneAppointments.<String>commitEvent(
	    		event, futureAppointments, (Patient p, String value) -> {
	    	p.setName(value);
	    	appointmentDAO.updateName(p.getId(), value, true);
	    }));
	    
	    orderColumn.setCellFactory(EditCell. < Patient, Number > forTableColumn(new NumberStringConverter()));
	    orderColumn.setOnEditCommit(event -> TabPaneAppointments.<Number>commitEvent(
	    		event, futureAppointments, (Patient p, Number value) -> {
	    	int localValue = (int) value;
	    	p.setOrder(localValue);
	    	appointmentDAO.updateOrder(p.getId(), localValue, true);
	    }));
	    
	    ageCol.setCellFactory(EditCell. < Patient, Integer > forTableColumn(new IntegerStringConverter()));
	    ageCol.setOnEditCommit(event -> TabPaneAppointments.<Integer>commitEvent(
	    		event, futureAppointments, (Patient p, Integer value) -> {
	    	p.setAge(value);
	    	appointmentDAO.updateAge(p.getId(), value, true);
	    }));
	    
	    genderCol.setOnEditCommit(event -> TabPaneAppointments.<String>commitEvent(
	    		event, futureAppointments, (Patient p, String value) -> {
	    	p.setGender(value);
			appointmentDAO.updateGender(p.getId(), value, true);
	    }));
		return futureAppointments;
	}
	
	private static <T> void commitEvent(
		CellEditEvent<Patient, T> event, TableView<Patient> tableView, CommitAction<T> function) {
		final T value = event.getNewValue() != null ?
		        event.getNewValue() : event.getOldValue();
		Patient p = (Patient) event.getTableView().getItems().get(event.getTablePosition().getRow());
		if (event.getTablePosition().getRow() < tableView.getItems().size()) {
			function.function(p, value);
		}
		tableView.refresh();
	}
	
	public interface CommitAction<T> {
		void function(Patient p, T value);
	}
}
