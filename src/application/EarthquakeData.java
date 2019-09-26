package application;

import java.sql.Date;
import java.sql.Time;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is to set the data type in the table columns. It also contain a
 * constructor and get methods.
 * 
 * @author OuyangYicheng
 *
 */
public class EarthquakeData {
	private final Date Date;
	private final Time Time;
	private final SimpleStringProperty Latitude;
	private final SimpleStringProperty Longitude;
	private final SimpleIntegerProperty Depth;
	private final SimpleFloatProperty Magnitude;
	private final SimpleStringProperty Region;

	EarthquakeData(java.sql.Date date2, java.sql.Time time2, String lat, String lon, Integer Depth, Float mag,
			String Region) {
		this.Date = date2;
		this.Time = time2;
		this.Latitude = new SimpleStringProperty(lat);
		this.Longitude = new SimpleStringProperty(lon);
		this.Depth = new SimpleIntegerProperty(Depth);
		this.Magnitude = new SimpleFloatProperty(mag);
		this.Region = new SimpleStringProperty(Region);
	}

	public Date getDate() {
		return Date;
	}

	public Time getTime() {
		return Time;
	}

	public String getLatitude() {
		return Latitude.get();
	}

	public String getLongitude() {
		return Longitude.get();
	}

	public Integer getDepth() {
		return Depth.get();
	}

	public Float getMagnitude() {
		return Magnitude.get();
	}

	public String getRegion() {
		return Region.get();
	}

}
