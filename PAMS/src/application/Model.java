package application;

import database.appointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	public static ObservableList<Patient> newAppPat;
	public static ObservableList<Patient> consulteds;
	
	public static ObservableList<Patient> getAllNewAppointments() {
		newAppPat = FXCollections.<Patient>observableArrayList();
		try {
			appointmentDAO.loadNewAppoint();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newAppPat;
	}

	public static ObservableList<Patient> getAllNewConsulteds() {
		consulteds = FXCollections.<Patient>observableArrayList();
		try {
			appointmentDAO.loadconsulteds();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("ok " + consulteds.size());
		return consulteds;
	}
}