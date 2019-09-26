package cotrollers;

import java.util.Properties;
import java.sql.*;


/**
 * a class for query information from database
 * 
 * <p>it provide seven method to query, by date ,magnitude, region and their combination</p>
 *
 * @author Huang Yu'an
 */
public class QueryDatabase implements Query{
	protected Connection con = null;
	private ResultSet res;
	private static String comDate = "select * from quakes where UTC_date between ? and ?";
	private static String comMag = "select * from quakes where magnitude between ? and ?";
	private static String comArea = "select * from quakes where  area_id in (select id from plate_areas where "
			+ "plate1 in (select code from plates where name = ?) and plate2 in ( select code from plates where name = ?) "
			+ "or plate1 in ( select code from plates where name = ?) and plate2 in (select code from plates where name = ?))";
	
	
	
	/**
     * return the result set after query
     * @return    result set res
     */
	public ResultSet getRes() {
		return res;
	}
	
	
	
	
	/**
     * a constructor for load information from a configuration file
     * 
     * <p>the configuration file contains the model(csv,db,net)
     *	of loading data and the path of file which stores data</p>
     *
     * @param    cnf  a object that will load the configuration file
     *
     */
	public QueryDatabase(loadCNF cnf) {
		String path = cnf.findResouce();
		String mode = cnf.findMode();
		switch (mode) {
		case "db":
		case "net":
			getConnection(path);
			break;
		case "csv":
			String databasefile = path + ".sqlite";
			getConnection(databasefile);
			new loadCSV(path, con);
			break;
		default:
		}
	}
	
	
	
	/**
     * a method to connect to database
     * @param path,path of resource
     * 
     */
	private void getConnection(String path) {
		Properties info = new Properties();
		String url = "jdbc:sqlite:" + path;
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			System.err.println("Cannot find the driver.");
			System.exit(1);
		}
		try {
			con = DriverManager.getConnection(url, info);
			con.setAutoCommit(false);
		} catch (Exception e) {
			System.err.println("connect faild!");
			System.exit(1);
		}
	}

	/**
     * query information by date
     * @param  date1 a data earlier 
     * @param date2 a data later
     */
	public void queryDate(String date1,String date2) {
		date1 += " 00:00:00.0";
		date2 += " 23:59:59.9";
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comDate);
			sql.setString(1, date1);
			sql.setString(2, date2);
			res = sql.executeQuery();	
		} catch (SQLException e) {
			System.err.println("query exception, can't find the info: "+e.getMessage());
		}
	}
	
	/**
     * query information by magnitude
     * @param    low  a number from lower magnitude
     * @param    up  a number from higher magnitude
     */
	public void queryMag(double low,double up) {
		PreparedStatement sql ;
		try {
			sql = con.prepareStatement(comMag);
			sql.setDouble(1, low);
			sql.setDouble(2, up);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());
		}
		
	}
	
	/**
     * query information by region
     * @param   area1 a String from one area 
     * @param   area2 a String from another area
     */
	public void queryArea(String area1, String area2) {
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comArea);
			sql.setString(1, area1);
			sql.setString(2, area2);
			sql.setString(3, area2);
			sql.setString(4, area1);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());
		}
	}
	
	/**
     * query information by date and magnitude
     *
     */
	public void queryDM(String date1,String date2,double low, double up) {
		date1 += " 00:00:00.0";
		date2 += " 23:59:59.9";
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comDate+ " intersect "+comMag);
			sql.setString(1, date1);
			sql.setString(2, date2);
			sql.setDouble(3, low);
			sql.setDouble(4, up);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());

		}
	}
	
	/**
     * query information by date and magnitude
     *
     */
	public void queryDA(String date1,String date2, String area1, String area2) {
		date1 += " 00:00:00.0";
		date2 += " 23:59:59.9";
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comDate+ " intersect "+comArea);
			sql.setString(1, date1);
			sql.setString(2, date2);
			sql.setString(3, area1);
			sql.setString(4, area2);
			sql.setString(5, area2);
			sql.setString(6, area1);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());

		}
	}
	
	
	/**
     * query information by magnitude and region
     *
     */
	public void queryMA(double low, double up,String area1,String area2) {
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comMag+ " intersect "+comArea);
			sql.setDouble(1, low);
			sql.setDouble(2, up);
			sql.setString(3, area1);
			sql.setString(4, area2);
			sql.setString(5, area2);
			sql.setString(6, area1);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());

		}
	}
	
	
	/**
     * query information by date, magnitude and region
     */
	public void queryDAM(
			String date1,String date2,double low, double up, String area1, String area2
			) {
		date1 += " 00:00:00.0";
		date2 += " 23:59:59.9";
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(comDate+ " intersect "+comMag+" intersect "+comArea);
			sql.setString(1, date1);
			sql.setString(2, date2);
			sql.setDouble(3, low);
			sql.setDouble(4, up);
			sql.setString(5, area1);
			sql.setString(6, area2);
			sql.setString(7, area2);
			sql.setString(8, area1);
			res = sql.executeQuery();
		} catch (SQLException e) {
			System.err.println("Query exception, can't find the info: "+e.getMessage());

		}
	}
	
	
	/**
	 * a method to close the resource
	 *
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.err.println("Close failed!");
		}
	}
	
	
}
