package application;
	
import application.MainController;
import cotrollers.ThreadForScrap;
import cotrollers.loadCNF;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
/**
 * This class has the main method to load the fxml document. It can run with
 * different modes depend on the setting in the cnf document.
 * @author OuyangYicheng
 *
 */
@SuppressWarnings("unused")
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("./Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			System.err.println("load fxml file failed! Can't open the stage");
		}
	}
	
	public static void main(String[] args) {
		loadCNF cnf = new loadCNF();
		cnf.launch();
		launch(args);
	}
}
