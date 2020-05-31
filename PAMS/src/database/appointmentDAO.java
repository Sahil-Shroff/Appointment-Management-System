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
            PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(updateStmt);//MySQLJDBCUtil.dbExecuteUpdate(updateStmt);
            psmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
	
	public static int insertPatient(int priority) throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
        
        String sql = "INSERT INTO new_appointment (`order`, `name`, `gender`, `priority`) VALUES (?,?,?,?);";
        int pos = 0;
        int countAppoint = AppointmentsPage.newAppPat.size();
        try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            // set parameters for statement
        	int order = countAppoint + 1;
        	pstmt.setInt(1, order);
            pstmt.setString(2, AppointmentsPage.nameEntry.getText());
            pstmt.setString(3, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
            pstmt.setBoolean(4, false);
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
			PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			psmt.executeUpdate();
			p.setFeesPaidProperty(pay);
			//System.out.println("Working");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadconsulteds() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT idconsulteds, name, age, gender, fees, fees_paid, receipt FROM consulteds";
		    //PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	int fees = rs.getInt("fees");
		    	int feesPaid = rs.getInt("fees_paid");
		    	int receipt = rs.getInt("receipt");
		    	Model.consulteds.add(new Patient(id, name, age, gender, fees, feesPaid, receipt));
		    }
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
	
	public static void loadNewAppoint() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT `idnew_table`, `order`, `name`, `age`, `gender`, `priority` FROM new_appointment";
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	int order = rs.getInt("order");
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	int priority = rs.getInt("priority");
		    	Model.newAppPat.add(new Patient(id, order, name, age, gender, priority));
		    }
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
	
	public static void updateName(int id, String newName) {
		String sql;
		sql = "UPDATE new_appointment SET name = \" " + newName + "\" WHERE idnew_table = " + id;
		try {
			PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}