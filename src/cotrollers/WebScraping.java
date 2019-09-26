package cotrollers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * this class aim at scraping the target web and get useful information 
 * from it using regular expression.
 * @author OuyangYicheng
 *
 */
public class WebScraping {
	LinkedList<String> TimeLL = new LinkedList<String>();
	LinkedList<String> OthersLL = new LinkedList<String>();
	/**
	 * get the LinkedList containing the data of time.
	 * @return LinkedList containing the data of time.
	 */
	public LinkedList<String> getTimeLL() {
		return TimeLL;
	}
/**
 * get the LinkedList containing the data of infomation except time.
 * @return LinkedList containing the data of infomation except time.
 */
	public LinkedList<String> getOthersLL() {
		return OthersLL;
	}
	/**
	 * Get the latest data from the web and return its time as a Timestamp.
	 * @return The latest time of the data in the web.
	 */
	public Timestamp getLatest() {
		String time = TimeLL.getFirst().split("\t")[1];
		Timestamp result = getTime(time);
		return result;
	}
/**
 * Get the earliest data from the web and return its time as a Timestamp.
 * @return The earliest time of the data in the web.
 */
	public Timestamp getEarliest(){
			String time = TimeLL.getLast().split("\t")[1];
			return getTime(time);
		}
/**
 * Change the String with the format of "yyyy-MM-dd HH:mm:ss" into a Timestamp
 * with the same time information.
 * @param time - the target String.
 * @return The timestamp result.
 */
	private Timestamp getTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(time);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			System.err.println("The format of time should be yyyy-mm-dd HH:mm:ss");
		}
		return null;
	}
		

	public WebScraping(int pagenum){
		GetData(pagenum);
	}

	/**
	 * This method scrap the source code of the web site of the url, then return it
	 * as a string.
	 * 
	 * @param url
	 *            - the url of the web we want to scrap.
	 * @return The source code we get.
	 */
	private String SendGet(String url) {
		StringBuilder result = new StringBuilder(84000);
		BufferedReader in = null;

		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				System.err.println("close failed");
			}
		}
		return result.toString();
	}
/**
 * To search useful information from pattern text using regular expression,
 * and return a object of class Matcher.
 * @param targetStr - the regular expression used for searching.
 * @param patternStr - the pattern text we get data from.
 * @return the Matcher contains the result of matching.
 */
	private Matcher RegexString(String targetStr, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(targetStr);
		return matcher;
	}
/**
 * Scrap the data of time, id, latitude, longitude, depth, magnitude and region
 * from the certain page of the web site "https://www.emsc-csem.org" 
 * @param pagenum - the page number users want to search.
 */
	private void GetData(int pagenum) {
			String url = "https://www.emsc-csem.org/Earthquake/?view=" + pagenum;
			String result = SendGet(url);
			addTimeData(result);
			addOtherData(result);
	}
/**
 * Scrap the time data 
 * from the String result, then generate a String containing those 
 * data, and add it to the LinkedList  TimeLL.
 * @param result - The pattern text used to search for data. 
 */
	private void addTimeData(String result) {
		Matcher Time = RegexString(result, "\\{evid:\\d+[^a]+?time.+?\\}");
		while (Time.find()) {
			String time = Time.group(0);
			time = time.substring(6, 12) + "\t" + time.substring(20, 30) + " " + time.substring(48, 58);
			TimeLL.add(time);
		}
	}
/**
 * Scrap the data id, latitude, longitude, depth, magnitude and region
 * from the String result, then generate a String containing those 
 * data, and add it to the LinkedList  OthersLL.
 * @param result - The pattern text used to search for data. 
 */
	private void addOtherData(String result) {
		Matcher Others = RegexString(result, "\\{evid:\\d+[^#]+?ago.+?\\}");
		while (Others.find()) {
			String others0;
			String others = Others.group(0);

			Matcher id = RegexString(others, "(?<=evid:)\\d+");
			id.find();
			others0 = id.group(0);
			Matcher lat = RegexString(others, "(?<=lat:\")\\d+\\.\\d+|(?<=lat:\")-\\d+\\.\\d+");
			lat.find();
			others0 += "\t" + lat.group(0);
			Matcher lon = RegexString(others, "(?<=lon:\")\\d+\\.\\d+|(?<=lon:\")-\\d+\\.\\d+");
			lon.find();
			others0 += "\t" + lon.group(0);
			Matcher depth = RegexString(others, "(?<=depth:\")\\d+|(?<=depth:\")-\\d+");
			depth.find();
			others0 += "\t" + depth.group(0);
			Matcher mag = RegexString(others, "(?<=mag:\")\\d+\\.\\d+");
			mag.find();
			others0 += "\t" + mag.group(0);
			Matcher reg = RegexString(others, "(?<=reg:\\\")[^\"]+");
			reg.find();
			others0 += "\t" + reg.group(0);
			OthersLL.add(others0);
		}
	}
}  