package cotrollers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

/**
 * a class for load information from Internet
 * 
 * <p>extends QueryDatabase,
 * it need WebScraping object to construct itself. This class provide some method
 * to load information to database and some other useful method</p>
 * @see WebScraping
 * @see QueryDatabase
 * @author Huang Yu'an
 */
public class loadWebSource extends QueryDatabase {
	private WebScraping wsp;
	private Timestamp maxDate;

	
	/**
     * reset the wsp (WebScraping object)
     * 
     * @param wsp   a webScraping object
     *
     */
	public synchronized void setWsp(WebScraping wsp) {
		this.wsp = wsp;
	}

	/**
     * get the wsp (WebScraping object) from this object
     * 
     *@return the wsp (WebScraping object)
     */
	public synchronized WebScraping getWsp() {
		return wsp;
	}
	
	
	
	/**
     * a constructor for load information from Internet
     * <p>need to load configuration file to know the path of resource and model of
     * reading. Also need the WebScraping object to scraping information</p>
     *
     * @param   webSp, a WebScraping object webSp to scraping information<br>
     * @param	cnf, a cnf file to load configuration information.
     */
	public loadWebSource(WebScraping webSp, loadCNF cnf) {
		super(cnf);
		wsp = webSp;
		maxDate = queryMaxDate();
	}

	
	/**
     * <b>to insert data to the database</b>
     * <p>this method is synchronized which will be used in a thread.</p>
     */
	public synchronized void insert() {
		Iterator<String> it = wsp.TimeLL.iterator();
		Iterator<String> me = wsp.OthersLL.iterator();
		try {
			String command = "replace into quakes(" + "id,UTC_date,latitude,longitude,depth,magnitude,region)"
					+ "values (?,?,?,?,?,?,?)";
			PreparedStatement sql = con.prepareStatement(command);
			while (it.hasNext() && me.hasNext()) {
				String[] localTime = it.next().split("\t");
				sql.setInt(1, Integer.parseInt(localTime[0]));
				sql.setString(2, localTime[1]);
				String[] localData = me.next().split("\t");
				if (!localTime[0].equals(localData[0])) {
					return;
				}
				sql.setDouble(3, Double.parseDouble(localData[1]));
				sql.setDouble(4, Double.parseDouble(localData[2]));
				sql.setInt(5, Integer.parseInt(localData[3]));
				sql.setDouble(6, Double.parseDouble(localData[4]));
				sql.setString(7, localData[5]);
				sql.addBatch();
			}
			sql.executeBatch();
			con.commit();
		} catch (SQLException e) {
			System.err.println("Insert operation failed:" + e.getMessage());
		}

	}

	/**
	 * <b>judge whether database should be insert data</b>
	 * <p>
	 * if the maxDate in database is after the latest date of net, then it should
	 * not be scraped. judge whether database latest time before net latest time
	 * </p>
	 * @return if it can be scrap, return true. Others, false.
	 * @author Hya
	 */
	public synchronized boolean isScrap() {
		Timestamp latestNet = wsp.getLatest();
		return maxDate.before(latestNet);
	}

	/**
	 * <b>get the latest date and time from database</b>
	 * 
	 * @return latest time from database
	 */
	private Timestamp queryMaxDate() {
		String command = "SELECT MAX(UTC_date) FROM quakes";
		PreparedStatement sql;
		try {
			sql = con.prepareStatement(command);
			return sql.executeQuery().getTimestamp(1);
		} catch (SQLException e) {
			System.err.println("Query Max Date Failed!");
		}
		return null;
	}


}
