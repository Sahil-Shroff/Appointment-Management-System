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
        //Declare a DELETE statement
		ResultSet rs = null;
        
        String sql = "INSERT INTO new_appointment(name, gender) "
                   + "VALUES(?,?)";
        
        try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            
            // set parameters for statement
            pstmt.setString(1, AppointmentsPage.nameEntry.getText());
            pstmt.setString(2, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
 
            pstmt.executeUpdate();
            String sql2 = "SELECT MAX(idnew_table) From new_appointment;";
            rs = MySQLJDBCUtil.dbExecuteQuery(sql2);
            //return rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        rs.next();
        return rs.getInt(1);
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
}