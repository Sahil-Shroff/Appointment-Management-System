package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.AppointmentsPage;
import application.Model;
import application.Patient;

public class appointmentDAO {
	
	public static void deleteAppoint(int id) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM new_appointment WHERE idnew_table = "+ id + ";";
 
        //Execute UPDATE operation
        try {
            MySQLJDBCUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
	
	public static int insertPatient() throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
        
        String sql = "INSERT INTO new_appointment(name, gender) VALUES(?,?)";
        int pos = 0;
        try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            // set parameters for statement
            pstmt.setString(1, AppointmentsPage.nameEntry.getText());
            pstmt.setString(2, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            rs.next();
            pos = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return pos;
    }
	
	public static void payFees(boolean pay, int id, Patient p) {
		String sql;
		if (pay)
			sql = "UPDATE consulteds SET fees_paid = 1 WHERE idconsulteds = " + id;
		else
			sql = "UPDATE consulteds SET fees_paid = 0 WHERE idconsulteds = " + id;
		try {
			MySQLJDBCUtil.dbExecuteUpdate(sql);
			p.setFeesPaidProperty(pay);
			//System.out.println("Working");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadconsulteds() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT idconsulteds, name, age, gender, fees, fees_paid FROM consulteds";
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	int fees = rs.getInt("fees");
		    	int feesPaid = rs.getInt("fees_paid");
		    	Model.consulteds.add(new Patient(id, name, age, gender, fees, feesPaid));
		    }
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
	
	public static void loadNewAppoint() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT idnew_table, name, age, gender FROM new_appointment";
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	Model.newAppPat.add(new Patient(id, name, age, gender));
		    }
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
	
	public static void updateName(int id, String newName) {
		String sql;
		sql = "UPDATE new_appointment SET name = \" " + newName + "\" WHERE idnew_table = " + id;
		//sql = "UPDATE new_appointment SET name = \"Sahil\" WHERE idnew_table = 59;";
		try {
			MySQLJDBCUtil.dbExecuteUpdate(sql);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}