package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import application.AppointmentsPage;
import application.Main;
import application.Model;
import application.Patient;
import application.TabPaneAppointments;

public class appointmentDAO {
	
	public static void completeSession() {
		String sql1 = "INSERT INTO history(`order`, evening, name, age, gender, priority, fees, fees_paid, receipt) " + 
						"SELECT evening, `order`, new_appointment.name,  new_appointment.age, new_appointment.gender, " + 
						"priority, fees, fees_paid, receipt FROM app_state, new_appointment, consulteds WHERE sel = 1;";
		
		String sql3 = "TRUNCATE new_appointment;";
		
		String sql2 = "INSERT INTO new_appointment(`order`, name, age, gender, priority) " +
						"SELECT `order`, name, age, gender, priority FROM next_session;";
		
		String sql4 = "TRUNCATE consulteds;";
		String sql5 = "TRUNCATE next_session;";
		
		try {
			MySQLJDBCUtil.dbExecuteUpdate(sql1);
			MySQLJDBCUtil.dbExecuteUpdate(sql2);
			MySQLJDBCUtil.dbExecuteUpdate(sql3);
			MySQLJDBCUtil.dbExecuteUpdate(sql4);
			MySQLJDBCUtil.dbExecuteUpdate(sql5);
			Main.controller.displayAppointments();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setEvening(Optional<String> r) {
		String sql;
		if (r.get().equalsIgnoreCase("evening"))
			sql = "UPDATE app_state SET evening = 1;";
		else
			sql = "UPDATE app_state SET evening = 0;";
		try {
			MySQLJDBCUtil.dbExecuteUpdate(sql);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
	}
	
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
        
        String sql = "INSERT INTO new_appointment (`order`, `name`, `gender`, `age`, `priority`) VALUES (?,?,?,?,?);";
        int pos = 0;
        int countAppoint = AppointmentsPage.newAppPat.size() + TabPaneAppointments.newConsulteds.getItems().size();
        try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
            // set parameters for statement
        	int order = countAppoint + 1;
        	pstmt.setInt(1, order);
            pstmt.setString(2, AppointmentsPage.nameEntry.getText());
            pstmt.setString(3, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
            pstmt.setInt(4, AppointmentsPage.ageTemp);
            pstmt.setBoolean(5, false);
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
			int income = 0;
			for (Patient patient : TabPaneAppointments.newConsulteds.getItems()) {
				if (patient.isFeesPaid())
		    		income += patient.getFees();
			}
			AppointmentsPage.income.set(income);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateReceiptStatus(boolean sel, int id, Patient p) {
		String sql = "UPDATE consulteds SET receipt = 0 WHERE idconsulteds = " + id;
		if (p.isFeesPaid())
		if (sel) {
			sql = "UPDATE consulteds SET receipt = 1 WHERE idconsulteds = " + id;
			p.setSel(true);
		}
		try {
			PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			psmt.executeUpdate();
			p.setReceiptProperty(sel);
			//System.out.println("Working");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateSelStatus(boolean sel, int id, Patient p) {
		String sql = "UPDATE consulteds SET sel = 0 WHERE idconsulteds = " + id;
		if (sel || p.isReceiptTaken())
			sql = "UPDATE consulteds SET sel = 1 WHERE idconsulteds = " + id;
		try {
			PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			psmt.executeUpdate();
			p.setSel(sel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadconsulteds() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT idconsulteds, name, age, gender, fees, fees_paid, receipt, sel FROM consulteds";
		    //PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(sql);
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    int income = 0;
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	int fees = rs.getInt("fees");
		    	int feesPaid = rs.getInt("fees_paid");
		    	int receipt = rs.getInt("receipt");
		    	boolean sel = rs.getBoolean("sel");
		    	Model.consulteds.add(new Patient(id, name, age, gender, fees, feesPaid, receipt, sel));
		    	if (feesPaid > 0)
		    		income += fees;
		    }
			AppointmentsPage.income.set(income);
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
	
	public static void loadFutureAppoint() throws ClassNotFoundException {
		try {	    
		    String sql = "SELECT `idnext_session`, `order`, `name`, `age`, `gender`, `priority` FROM next_session";
		    ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
		    while( rs.next() ) {
		    	int id = rs.getInt(1);
		    	int order = rs.getInt("order");
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	int priority = rs.getInt("priority");
		    	Model.futAppPat.add(new Patient(id, order, name, age, gender, priority));
		    }
		} catch(SQLException e) {
		   System.out.println(e.getMessage());
		}
	}
	
	public static void updateName(int id, String newName, boolean future) {
		String sql1, sql2;
		sql1 = "UPDATE next_session SET name = \" " + newName + "\" WHERE idnext_session = " + id;
		sql2 = "UPDATE new_appointment SET name = \" " + newName + "\" WHERE idnew_table = " + id;
		try {
			PreparedStatement psmt1 = MySQLJDBCUtil.conn.prepareStatement(sql1);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			PreparedStatement psmt2 = MySQLJDBCUtil.conn.prepareStatement(sql2);
			if (future)
				psmt1.executeUpdate();
			else
				psmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateGender(int id, String newGender, boolean future) {
		String sql1, sql2;
		sql1 = "UPDATE next_session SET gender = \" " + newGender + "\" WHERE idnext_session = " + id;
		sql2 = "UPDATE new_appointment SET gender = \" " + newGender + "\" WHERE idnew_table = " + id;
		try {
			PreparedStatement psmt1 = MySQLJDBCUtil.conn.prepareStatement(sql1);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			PreparedStatement psmt2 = MySQLJDBCUtil.conn.prepareStatement(sql2);
			if (future)
				psmt1.executeUpdate();
			else
				psmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateOrder(int id, int newOrder, boolean future) {
		String sql1, sql2;
		sql1 = "UPDATE next_session SET `order` = " + newOrder + " WHERE idnext_session = " + id;
		sql2 = "UPDATE new_appointment SET `order` = " + newOrder + " WHERE idnew_table = " + id;
		try {
			PreparedStatement psmt1 = MySQLJDBCUtil.conn.prepareStatement(sql1);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			PreparedStatement psmt2 = MySQLJDBCUtil.conn.prepareStatement(sql2);
			if (future)
				psmt1.executeUpdate();
			else
				psmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateAge(int id, int newAge, boolean future) {
		String sql1, sql2;
		sql1 = "UPDATE next_session SET `age` = " + newAge + " WHERE idnext_session= " + id;
		sql2 = "UPDATE new_appointment SET `age` = " + newAge + " WHERE idnew_table = " + id;
		try {
			PreparedStatement psmt1 = MySQLJDBCUtil.conn.prepareStatement(sql1);//MySQLJDBCUtil.dbExecuteUpdate(sql);
			PreparedStatement psmt2 = MySQLJDBCUtil.conn.prepareStatement(sql2);
			if (future)
				psmt1.executeUpdate();
			else
				psmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteFutureAppoint(int id) throws SQLException, ClassNotFoundException {
	    //Declare a DELETE statement
	    String updateStmt = "DELETE FROM next_session WHERE idnext_session = "+ id + ";";

	    //Execute UPDATE operation
	    try {
	        PreparedStatement psmt = MySQLJDBCUtil.conn.prepareStatement(updateStmt);//MySQLJDBCUtil.dbExecuteUpdate(updateStmt);
	        psmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.print("Error occurred while DELETE Operation: " + e);
	        throw e;
	    }
	}

	public static int insertFuturePatient(int priority) throws SQLException, ClassNotFoundException {
		ResultSet rs = null;
	    
	    String sql = "INSERT INTO next_session (`order`, `name`, `gender`, `age`, `priority`) VALUES (?,?,?,?,?);";
	    int pos = 0;
	    int countAppoint = AppointmentsPage.futAppPat.size();
	    try ( PreparedStatement pstmt = MySQLJDBCUtil.conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);) {
	        // set parameters for statement
	    	int order = countAppoint + 1;
	    	pstmt.setInt(1, order);
	        pstmt.setString(2, AppointmentsPage.nameEntry.getText());
	        pstmt.setString(3, AppointmentsPage.genderChoice.getSelectionModel().getSelectedItem());
	        pstmt.setInt(4, AppointmentsPage.ageTemp);
	        pstmt.setBoolean(5, false);
	        pstmt.executeUpdate();
	        rs = pstmt.getGeneratedKeys();
	        rs.next();
	        pos = rs.getInt(1);
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	    return pos;
	}
}