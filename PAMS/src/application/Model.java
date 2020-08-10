package application;

import database.appointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	public static ObservableList<Patient> newAppPat;
	public static ObservableList<Patient> consulteds;
	public static ObservableList<Patient> futAppPat;
	
	public static ObservableList<Patient> getAllNewAppointments() {
		newAppPat = FXCollections.<Patient>observableArrayList();
		appointmentDAO.loadNewAppoint();
		return newAppPat;
	}

	public static ObservableList<Patient> getAllNewConsulteds() {
		consulteds = FXCollections.<Patient>observableArrayList();
		appointmentDAO.loadconsulteds();
		//System.out.println("ok " + consulteds.size());
		return consulteds;
	}
	
	public static ObservableList<Patient> getFutureAppointments() {
		futAppPat = FXCollections.<Patient>observableArrayList();
		appointmentDAO.loadFutureAppoint();
		return futAppPat;
	}
}