package application;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import cotrollers.QueryDatabase;
import cotrollers.loadCNF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import application.ListBasedonFirstChoiceBox;
/**
 * This MainController is the controller for the GUI, contains 
 * some important method on action like initialize and search.
 * 
 * @author OuyangYicheng
 *
 */
public class MainController implements Initializable {
	QueryDatabase qb = new QueryDatabase(new loadCNF());
	@FXML
	private TableView<EarthquakeData> table;
	@FXML
	private TableColumn<EarthquakeData, LocalDate> Date;
	@FXML
	private TableColumn<EarthquakeData, LocalTime> Time;
	@FXML
	private TableColumn<EarthquakeData, String> Latitude;
	@FXML
	private TableColumn<EarthquakeData, String> Longitude;
	@FXML
	private TableColumn<EarthquakeData, Integer> Depth;
	@FXML
	private TableColumn<EarthquakeData, Double> Magnitude;
	@FXML
	private TableColumn<EarthquakeData, String> Region;

	public ObservableList<EarthquakeData> list = FXCollections.observableArrayList();
	public static ObservableList<String> plateslist = FXCollections
			.observableArrayList("Scotia","Antarctic","African","Arabian"
					,"Cocos","Eurasian","Indian","Australian","Juan de Fuca"
					,"Caribbean","North-American","Nazca","Pacific","Filipino"
					,"South-American");
	
	@FXML
	private DatePicker dp;
	@FXML
	private DatePicker dp2;
	@FXML
	private ToggleButton tbd;
	@FXML
	private ToggleButton tbm;
	@FXML
	private ToggleButton tbr;
	@FXML
	private VBox vbox;
	@FXML
	private Text text;
	@FXML
	private Text text1;
	@FXML
	private TextArea textinput1;
	@FXML
	private TextArea textinput2;
	@FXML
	private Canvas canva;
	@FXML
	private ChoiceBox<String> cb1;
	@FXML
	private ChoiceBox<String> cb2;

	/**
	 * The methods to search for the data based on the condition users choose.<br>
	 * There are 7 method in all: search by date<br>
	 * search by magnitude<br>
	 * search by region<br>
	 * search by date and magnitude<br>
	 * search by date and region<br>
	 * search by magnitude and region<br>
	 * search by date, magnitude and region<br>
	 * 
	 * @param event
	 *            - Click the button will run the method.
	 */

	@FXML
	public void Search(ActionEvent event) {
		try {
			GraphicsContext gc = canva.getGraphicsContext2D();
			boolean dateB = tbd.isSelected();
			boolean magB = tbm.isSelected();
			boolean regionB = tbr.isSelected();
			String date1, date2, area1, area2;
			Double low, up;
			if (dateB && magB && regionB) {
				if (dp.getValue().isAfter(dp2.getValue())) {
					new AlertBox().Alert("Date Input Error", "The first date should not be after the second date");
				}
				date1 = dp.getValue().toString();
				date2 = dp2.getValue().toString();
				low = Double.parseDouble(textinput1.getText());
				up = Double.parseDouble(textinput2.getText());
				area1 = cb1.getSelectionModel().getSelectedItem();
				area2 = cb2.getSelectionModel().getSelectedItem();
				qb.queryDAM(date1, date2, low, up, area1, area2);
			} else if (dateB && magB) {
				if (dp.getValue().isAfter(dp2.getValue())) {
					new AlertBox().Alert("Date Input Error", "The first date should not be after the second date!");
				}
				date1 = dp.getValue().toString();
				date2 = dp2.getValue().toString();
				low = Double.parseDouble(textinput1.getText());
				up = Double.parseDouble(textinput2.getText());
				qb.queryDM(date1, date2, low, up);
			} else if (dateB && regionB) {
				if (dp.getValue().isAfter(dp2.getValue())) {
					new AlertBox().Alert("Date Input Error", "The first date should not be after the second date!");
				}
				date1 = dp.getValue().toString();
				date2 = dp2.getValue().toString();
				area1 = cb1.getSelectionModel().getSelectedItem();
				area2 = cb2.getSelectionModel().getSelectedItem();
				qb.queryDA(date1, date2, area1, area2);
			} else if (magB && regionB) {
				low = Double.parseDouble(textinput1.getText());
				up = Double.parseDouble(textinput2.getText());
				area1 = cb1.getSelectionModel().getSelectedItem();
				area2 = cb2.getSelectionModel().getSelectedItem();
				qb.queryMA(low, up, area1, area2);
			} else if (dateB) {
				if (dp.getValue().isAfter(dp2.getValue())) {
					new AlertBox().Alert("Date Input Error", "The first date should not be after the second date!");
				}
				date1 = dp.getValue().toString();
				date2 = dp2.getValue().toString();
				qb.queryDate(date1, date2);
			} else if (magB) {
				low = Double.parseDouble(textinput1.getText());
				up = Double.parseDouble(textinput2.getText());
				qb.queryMag(low, up);
			} else if (regionB) {
				area1 = cb1.getSelectionModel().getSelectedItem();
				area2 = cb2.getSelectionModel().getSelectedItem();
				qb.queryArea(area1, area2);
			}
			if (dateB || magB || regionB) {
				gc.clearRect(0, 0, 640, 356);
				list.clear();
				ResultSet res = qb.getRes();
				resProcess(res);
			} else {
				new AlertBox().Alert("Condition Select Error", "You must select a condition before searching!");
			}
		} catch (NumberFormatException e) {
			new AlertBox().Alert("NumberFormatException", "Number Input Format error!");
		} catch (NullPointerException e) {
			new AlertBox().Alert("NullPointerException", "You did not choose the datetime!");
		}

	}


/**When the ToggleButton "Set Date Scale" is selected, "Search by Date" mode is on.
 * 
 * @param event - Click the button will run the method.
 */
	
	@FXML
	public void DateSelect(ActionEvent event) {
		if (tbd.isSelected()) {
			dp.setDisable(false);
			dp2.setDisable(false);
			text1.setStyle("-fx-fill: black");
		} else {
			dp.setDisable(true);
			dp2.setDisable(true);
			text1.setStyle("-fx-fill: gray");
		}
	}

/**When the ToggleButton "Set Magnitude Scale" is selected, "Search by Magnitude" mode is on.
 * 
 * @param event - Click the button will run the method.
 */
	
	@FXML
	public void MagSelect(ActionEvent event) {
		if (tbm.isSelected()) {
			vbox.setDisable(false);
			text.setStyle("-fx-fill: black");
		} else {
			vbox.setDisable(true);
			text.setStyle("-fx-fill: gray");
		}
	}

/**When the ToggleButton "Select Region" is selected, "Search by region" mode is on.
 * 
 * @param event - Click the button will run the method.
 */
	
	@FXML
	public void RegionSelect(ActionEvent event) {
		if (tbr.isSelected()) {
			cb1.setDisable(false);
			cb2.setDisable(false);
			cb1.setValue("Scotia");
			cb1.setItems(plateslist);
			cb2.setValue("Scotia");
			cb2.setItems(plateslist);
		} else {
			cb1.setDisable(true);
			cb2.setDisable(true);
		}
	}
/**
 * Rewrite the initialize method in "Initializable".
 * Here initialize the table columns and the table view.
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Date.setCellValueFactory(new PropertyValueFactory<EarthquakeData, LocalDate>("Date"));
		Time.setCellValueFactory(new PropertyValueFactory<EarthquakeData, LocalTime>("Time"));
		Latitude.setCellValueFactory(new PropertyValueFactory<EarthquakeData, String>("Latitude"));
		Longitude.setCellValueFactory(new PropertyValueFactory<EarthquakeData, String>("Longitude"));
		Depth.setCellValueFactory(new PropertyValueFactory<EarthquakeData, Integer>("Depth"));
		Magnitude.setCellValueFactory(new PropertyValueFactory<EarthquakeData, Double>("Magnitude"));
		Region.setCellValueFactory(new PropertyValueFactory<EarthquakeData, String>("Region"));
		table.setItems(list);
		
		cb1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    switch (cb1.getSelectionModel().getSelectedItem()) {
			case "Scotia":
				cb2.setItems(ListBasedonFirstChoiceBox.SClist);	
				break;
			case "Antarctic":
				cb2.setItems(ListBasedonFirstChoiceBox.ATlist);
				break;
			case "African":
				cb2.setItems(ListBasedonFirstChoiceBox.AFlist);
				break;
			case "Arabian":
				cb2.setItems(ListBasedonFirstChoiceBox.ARlist);
				break;
			case "Cocos":
				cb2.setItems(ListBasedonFirstChoiceBox.COlist);
				break;
			case "Eurasian":
				cb2.setItems(ListBasedonFirstChoiceBox.EAlist);
				break;
			case "Indian":
				cb2.setItems(ListBasedonFirstChoiceBox.INlist);
				break;
			case "Australian":
				cb2.setItems(ListBasedonFirstChoiceBox.AUlist);
				break;
			case "Juan de Fuca":
				cb2.setItems(ListBasedonFirstChoiceBox.JFlist);
				break;
			case "Caribbean":
				cb2.setItems(ListBasedonFirstChoiceBox.CAlist);
				break;
			case "North-American":
				cb2.setItems(ListBasedonFirstChoiceBox.NAlist);
				break;
			case "Nazca":
				cb2.setItems(ListBasedonFirstChoiceBox.NZlist);
				break;
			case "Pacific":
				cb2.setItems(ListBasedonFirstChoiceBox.PAlist);
				break;
			case "Filipino":
				cb2.setItems(ListBasedonFirstChoiceBox.FIlist);
				break;
			default:
				cb2.setItems(ListBasedonFirstChoiceBox.SAlist);
				break;
			}
		});
	}
/**
 * The detailed method to search for the data in the database. 
 * And draw the location of earthquakes on the map.
 * Larger is the magnitude, the circle will be larger and coarser. 
 * @param res - the result set return by the searching method.
 */
	public void resProcess(ResultSet res) {
		try {
			GraphicsContext gc = canva.getGraphicsContext2D();
			gc.setStroke(Color.RED);
			while (res.next()) {
				Date date = res.getDate(2);
				Time time = res.getTime(2);
				String lat = res.getString(3);
				String lon = res.getString(4);
				Integer dep = res.getInt(5);
				Float mag = res.getFloat(6);
				String re = res.getString(7);
				gc.setLineWidth(mag / 1.5);
				gc.strokeOval(lonAdjust(lon,-22.0), latAdjust(lat,-8.0), mag * 3, mag * 3);
				list.add(new EarthquakeData(date, time, lat, lon, dep, mag, re));
			}
		} catch (SQLException e) {
			System.err.println("none result!");
		}

	}
	
/**
 * This method is aimed to adjust the position showed in the map. 
 * @param lon - the longitude the earthquake happened.
 * @param adjustment - the distance that the point should move rightward. 
 * @return the adjusted longitude, that is the x position of the canvas.
 */
	
	private Double lonAdjust(String lon, Double adjustment) {
		double res;
		res = (Float.parseFloat(lon) + 180.0) / 9 * 16 + adjustment;
		if (res < 0) {
			return res + 640;
		} else {
			return res;
		}
	}

/**
 * This method is aimed to adjust the position showed in the map. 
 * @param lat - the latitude the earthquake happened.
 * @param adjustment - the distance that the point should move downward. 
 * @return the adjusted latitude, that is the y position of the canvas.
 */	
	
	private Double latAdjust(String lat, Double adjustment) {
		double res;
		res = (90.0 - Float.parseFloat(lat)) / 45 * 89 + adjustment;
		if (res > 356) {
			return res - 356;
		} else {
			return res;
		}
	}
}
