package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import application.Model;
import application.Patient;
 
/**
 *
 * @author mysqltutorial.org
 */
public class MySQLJDBCUtil {
 
    /**
     * Get database connection
     *
     * @return a Connection object
     * @throws SQLException
     */
	public static Connection conn = null;
	
    public static Connection getConnection() throws SQLException {
        //Connection conn = null;
 
        try (FileInputStream f = new FileInputStream("db.properties")) {
 
            // load the properties file
            Properties pros = new Properties();
            pros.load(f);
 
            // assign db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
  //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }
    }
 
    //DB Execute Query Operation
    public static void dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            conn = getConnection();
            System.out.println("Select statement: " + queryStmt + "\n");
 
            //Create statement
            stmt = conn.createStatement();
 
            //Execute select (query) operation
            rs = stmt.executeQuery(queryStmt);
            //return resultSet;
            while( rs.next() ) {
		    	String name = rs.getString("name");
		    	int age = rs.getInt("age");
		    	String gender = rs.getString("gender");
		    	Model.newAppPat.add(new Patient(name, age, gender));
		    }
            
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            //crs = new CachedRowSetImpl();
            //crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        //return crs;
    }
 
    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            conn = getConnection();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}