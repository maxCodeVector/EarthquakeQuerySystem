package cotrollers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
* this class is for load configuration information from the .cnf file
* <p>load path of resouce file(.csv, .sqlite) and loading model(csv, db, net) 
* </p>
* @author Huang Yu'an
*/
public class loadCNF {
	private Properties prop;
	private ThreadForScrap tscp;
	
	/**
	 *constuctor to loading the object 
	 * <p>using Properties object to load information.<br>
	 * if configuration file writting mistake, it will throws exception
	 * </p>
	 * 
	 */
	public loadCNF() {
		prop = new Properties();
		find();
	}
	
	/**
	 * find information using I.o. from configuration file .
	 */
	private void find() {
		try {
			BufferedReader cnf = new BufferedReader(new FileReader("properties.cnf"));
			prop.load(cnf);
			cnf.close();
			boolean isCSVR = prop.getProperty("data_resouce").endsWith("csv");
			boolean isCSVM = prop.getProperty("model").equals("csv");
			if (isCSVR ^ isCSVM)
				throw new NullPointerException();
		} catch (IOException e) {
			System.err.println("No such file named: " + e.getMessage());
			System.exit(0);
		} catch (NullPointerException e) {
			System.err.println("Configuration file writting error!");
			System.exit(0);
		}
	}

	/**
	 *get information :path of resource file
	 *@return information :path of resource file
	 */
	public String findResouce() {
		return prop.getProperty("data_resouce");
	}
	
	/**
	 *get information :loading model
	 *@return loaing model
	 */
	public String findMode() {
		return prop.getProperty("model");
	}
	
	/**
	 * <b>run the thread for Scraping</b>
	 * <p>
	 *if the model is net, it will run the thread for web Scraping
	 *</p>
	 */
	public void launch() {
		if (findMode().equals("net")) {
			tscp = new ThreadForScrap(this);
			tscp.start();
		}	
	}
}
