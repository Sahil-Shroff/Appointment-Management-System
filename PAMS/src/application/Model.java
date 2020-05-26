package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.MySQLJDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	static ObservableList<Patient> newAppPat;
	private static Connection connection;
	private static Statement stmt;
	private static ResultSet rs;
	
	public static ObservableList<Patient> getAllNewAppointments() {
		newAppPat = FXCollections.<Patient>observableArrayList();
		try {
		    // create a connection to the database
		    connection = MySQLJDBCUtil.getConnection();
		    stmt = connection.createStatement();
		    
		    String sql = "SELECT name, age, gender FROM new_appointment";
		    rs = stmt.executeQuery(sql);
		    while( rs.next() ) {
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	newAppPat.add(new Patient(name, age, gender));
		    }
		    
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		} finally {
		    try{
		           if(connection != null)
		             connection.close();
		           if(stmt != null)
		        	   stmt.close();
		           if(rs != null)
		        	   rs.close();
		    }catch(SQLException ex){
		           System.out.println(ex.getMessage());
		    }
		}
		return newAppPat;
	}

	public static ObservableList<Patient> getAllNewConsulteds() {
		ObservableList<Patient> newConsulteds = FXCollections.<Patient>observableArrayList();
		return newConsulteds;
	}
}