package cotrollers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
* this class is for load information from the csv file
* <p>has an Connection type object from QueryDatabase, 
* using it to transfer information from .csv file to database, the database 's name 
* has smme ralation to csv file. <br>
* for example, .csv name is "data.csv", then database's name is "data.csv.sqlite".
* </p>
* @see QueryDatabase
* @author Huang Yu'an
*/
public class loadCSV {
	private Connection con;
	private BufferedReader bre;

	/**
	* a constructor using connection con to connect the .csv and .sqlite 
	* <p>has an Connection type object from QueryDatabase.
	* </p>
	* 
	* @param path  is the adress of the resource file<br>
	* @param con  a Connection is frome the QueryDatabase.
	*/
	loadCSV(String path, Connection con) {
		this.con = con;
		try {
			bre = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			System.err.println("Can't read the csv file: " + e.getMessage());
		}
		insertData();
	}
	
	
	/**
	* judege a table is exist in the database
	* 
	* @param tableName,  a String referring to the table's name in the database.
	* @return if it is exist, return true. otherwise, false.
	* @exception SQLException if an exception take place
	*/
	private boolean isExistT(String tableName) throws SQLException {
		ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);
		return rs.next();
	}

	/**
	 * <b>for transfer data from csv file to sqlite file</b>
	 */
	private void insertData() {
		try {
			if (!isExistT("quakes")) {
				String command = "CREATE TABLE quakes (" + "id INT PRIMARY KEY, UTC_date DATETIME,"
						+ "latitude FLOAT CHECK (latitude BETWEEN -90 AND 90),"
						+ "longitude FLOAT CHECK (longitude BETWEEN -180 AND 180),"
						+ "depth INT, magnitude FLOAT, region VARCHAR (100), " + "CONSTRAINT quakes_uq UNIQUE (id) );";
				PreparedStatement sql = con.prepareStatement(command);
				sql.execute();
				con.commit();
			}
			String data = bre.readLine();
			String command = "replace into quakes(" + "UTC_date,latitude,longitude,depth,magnitude,region,id)"
					+ "values (?,?,?,?,?,?,?)";
			PreparedStatement sql = con.prepareStatement(command);
			while ((data = bre.readLine()) != null) {
				String[] loadData = data.replace("\"", "").split(",");
				sql.setInt(7, Integer.parseInt(loadData[0]));
				sql.setString(1, loadData[1].replace('/', '-'));
				sql.setDouble(2, Double.parseDouble(loadData[2]));
				sql.setDouble(3, Double.parseDouble(loadData[3]));
				sql.setInt(4, Integer.parseInt(loadData[4]));
				sql.setDouble(5, Double.parseDouble(loadData[5]));
				sql.setString(6, loadData[6]);
				sql.addBatch();
			}
			sql.executeBatch();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Data format error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Can't read the csv file: " + e.getMessage());
		}

	}

}
