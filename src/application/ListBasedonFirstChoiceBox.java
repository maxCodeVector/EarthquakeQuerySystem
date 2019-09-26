package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListBasedonFirstChoiceBox {

	public static ObservableList<String> SClist = FXCollections.observableArrayList("Scotia", "Antarctic",
			"South-American");

	public static ObservableList<String> ATlist = FXCollections.observableArrayList("Scotia", "Antarctic", "African",
			"Australian", "Nazca", "Pacific", "South-American");

	public static ObservableList<String> AFlist = FXCollections.observableArrayList("Antarctic", "African", "Arabian",
			"Eurasian", "Indian", "Australian", "North-American", "South-American");

	public static ObservableList<String> ARlist = FXCollections.observableArrayList("African", "Arabian", "Eurasian",
			"Indian");

	public static ObservableList<String> COlist = FXCollections.observableArrayList("Cocos", "Caribbean",
			"North-American", "Nazca", "Pacific");

	public static ObservableList<String> EAlist = FXCollections.observableArrayList("African", "Arabian", "Indian",
			"Eurasian", "Australian", "North-American", "Pacific", "Filipino");

	public static ObservableList<String> INlist = FXCollections.observableArrayList("African", "Arabian", "Eurasian",
			"Indian", "Australian");

	public static ObservableList<String> AUlist = FXCollections.observableArrayList("Antarctic", "African", "Eurasian",
			"Indian", "Australian", "Pacific");

	public static ObservableList<String> JFlist = FXCollections.observableArrayList("Juan de Fuca", "North-American",
			"Pacific");

	public static ObservableList<String> CAlist = FXCollections.observableArrayList("Cocos", "Caribbean",
			"North-American", "South-American");

	public static ObservableList<String> NAlist = FXCollections.observableArrayList("African", "Cocos", "Eurasian",
			"Juan de Fuca", "Caribbean", "North-American", "Pacific", "South-American");

	public static ObservableList<String> NZlist = FXCollections.observableArrayList("Antarctic", "Cocos", "Nazca",
			"Pacific", "South-American");

	public static ObservableList<String> PAlist = FXCollections.observableArrayList("Antarctic", "Cocos", "Eurasian",
			"Australian", "Juan de Fuca", "North-American", "Nazca", "Pacific", "Filipino");

	public static ObservableList<String> FIlist = FXCollections.observableArrayList("Filipino", "Eurasian", "Pacific");

	public static ObservableList<String> SAlist = FXCollections.observableArrayList("Scotia", "Antarctic", "African",
			"Caribbean", "North-American", "Nazca", "South-American");

}
