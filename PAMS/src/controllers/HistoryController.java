package controllers;

import java.time.LocalDate;

import application.AppointmentsPage;
import application.Patient;
import application.TabPaneAppointments;
import database.HistoryDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class HistoryController {

	@FXML
	private Text collection;
	
	@FXML
	private ChoiceBox<String> daySelect;
	
	@FXML
	private ChoiceBox<String> monthSelect;
	
	@FXML
	private CheckBox renderMonth;
	
	@FXML
	private Text totalPatient;
	
	@FXML
	private ChoiceBox<String> yearSelect;
	
	@FXML
	private Button submitBtn;
	
	@FXML
	private TableView<Patient> recordTable;
	
	@FXML
	private VBox recordDisplay;
	
	private ObservableList<Patient> records = FXCollections.<Patient>observableArrayList();;
	
	@SuppressWarnings("unchecked")
	public void config() {
		for (int i = 1; i <= 31; i++) {
			if (i < 10)
				daySelect.getItems().add("0" + Integer.toString(i));
			else
				daySelect.getItems().add(Integer.toString(i));
		}
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				monthSelect.getItems().add("0" + Integer.toString(i));
			else
				monthSelect.getItems().add(Integer.toString(i));
		}
		for (int i = 2020; i <= 2040; i++) {
			yearSelect.getItems().add(Integer.toString(i));
		}
		collection.setText(Integer.toString(0));
		totalPatient.setText(Integer.toString(0));
		renderMonth.selectedProperty().addListener((ov, o, e) -> {
			if (e.booleanValue()) {
				daySelect.setDisable(true);
				recordTable.setDisable(true);
			} else {
				daySelect.setDisable(false);
				recordTable.setDisable(false);
			}
		});
		daySelect.requestFocus();
		daySelect.setOnKeyPressed(e -> {
			e.getCode();
			if (e.getCode() == KeyCode.ENTER)
				monthSelect.requestFocus();
		});
		monthSelect.setOnKeyPressed(e -> {
			e.getCode();
			if (e.getCode() == KeyCode.ENTER)
				yearSelect.requestFocus();
		});
		monthSelect.getSelectionModel().select(LocalDate.now().getMonthValue() + 1);
		yearSelect.getSelectionModel().select(LocalDate.now().getYear() - 2020);
		
		recordTable.setItems(records);
		TableColumn<Patient, Number> indexColumn = new TableColumn<Patient, Number>("Sr. No.");
		indexColumn.setSortable(false);
		indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(records.indexOf(column.getValue()) + 1));
		
		TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		nameCol.setPrefWidth(300.0);
		
		TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
		ageCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));

		TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
		
		TableColumn<Patient, Integer> feesCol = new TableColumn<>("Fees");
		feesCol.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("fees"));
		
		TableColumn<Patient, String> receiptTakenCol = new TableColumn<>("Receipt/Cash");
		receiptTakenCol.setCellValueFactory(new PropertyValueFactory<Patient, String>("receiptTaken"));
		receiptTakenCol.setPrefWidth(200.0);
		
		recordTable.getColumns().addAll(indexColumn, nameCol, ageCol, genderCol, feesCol, receiptTakenCol);
		
		double screenHeight = Screen.getPrimary().getBounds().getHeight();
		double screenWidth = Screen.getPrimary().getBounds().getWidth();
		//System.out.println(screenHeight/1080 + " " + screenWidth/1920);
		recordDisplay.setPrefWidth(1599*screenWidth/1920);
		recordDisplay.setPrefHeight(930*screenHeight/1080);
	}
	
	@FXML
	private void searchOldRecords() {
		String dateStr = yearSelect.getSelectionModel().getSelectedItem() + "-" + monthSelect.getSelectionModel().getSelectedItem() + "-" +
				daySelect.getSelectionModel().getSelectedItem();
		if (renderMonth.isSelected()) {
			records.clear();
			String dateSplit[] = dateStr.split("-");
			dateSplit[2] = "01";
			dateStr = dateSplit[0] + "-" + dateSplit[1] + "-" + dateSplit[2];
			HistoryDAO.loadMonthyRecords(collection, totalPatient, dateStr);
		} else {
			records.clear();
			HistoryDAO.loadRecords(records, dateStr);
			totalPatient.setText(Integer.toString(records.size()));
			int income = 0;
			for (Patient patient : records) {
				if (patient.isFeesPaid())
		    		income += patient.getFees();
			}
			collection.setText(Integer.toString(income));
			recordTable.refresh();
		}
	}
}
