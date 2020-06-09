package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import application.Patient;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

public class HistoryDAO {

	public static void loadRecords(ObservableList<Patient> records, String dateStr) {
		String sql = "SELECT name, age, gender, fees, receipt FROM history WHERE date = '" + dateStr + "';";
		try {
			ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				int fees = rs.getInt("fees");
				int receipt = rs.getInt("receipt");
				records.add(new Patient(name, age, gender, fees, receipt));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadMonthyRecords(Text collection, Text totalPatient, String dateStr) {
		String sql = "SELECT SUM(fees), COUNT(*) FROM history WHERE MONTH(date) = MONTH('" + dateStr + "');";
		try {
			ResultSet rs = MySQLJDBCUtil.dbExecuteQuery(sql);
			rs.next();
			collection.setText(rs.getString(1));
			totalPatient.setText(rs.getString(2));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
}
