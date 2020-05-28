package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.AppointmentsPage;
import application.Model;
import application.Patient;

public class appointmentDAO {
	
	public static void deleteAppoint(String name) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM new_appointment WHERE name = "+ name;
 
        //Execute UPDATE operation
        try {
            MySQLJDBCUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
	
	public static void insertPatient() throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
		ResultSet rs = null;
        
        String sql = "INSERT INTO new_appointment(name, gender) "
                   + "VALUES(?,?)";
        
        try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            
            // set parameters for statement
            pstmt.setString(1, AppointmentsPage.nameEntry.getText());
            pstmt.setString(2, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
 
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
	
	public static void loadNewAppoint() throws ClassNotFoundException {
		//ResultSet rs = null;
		try {
		    		    
		    String sql = "SELECT name, age, gender FROM new_appointment";
		    MySQLJDBCUtil.dbExecuteQuery(sql);
		    /*while( rs.next() ) {
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	Model.newAppPat.add(new Patient(name, age, gender));
		    }*/
		    
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
}