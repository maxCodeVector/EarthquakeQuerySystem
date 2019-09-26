package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class define a AlertBox used for giving error message to users.
 * 
 * @author OuyangYicheng
 *
 */
public class AlertBox {
	public Stage window = new Stage();
	public Label label = new Label();

	/**
	 * This method generate a AlertBox used for giving error message to users.
	 * 
	 * @param title
	 *            - The title of the AlertBox.
	 * @param message
	 *            - The message of the AlertBox.
	 */
	public void Alert(String title, String message) {
		try {
			window.setTitle(title);
			window.initModality(Modality.APPLICATION_MODAL);
			window.setMinWidth(300);
			window.setMinHeight(200);
			window.setAlwaysOnTop(true);

			Button button = new Button("OK");
			button.setPrefWidth(82);
			button.setPrefHeight(35);
			button.setOnAction(e -> window.close());

			label.setText(message);
			label.setPrefHeight(94);
			label.setPrefWidth(270);
			label.setWrapText(true);
			label.setFont(Font.font("System", 18));
			label.setLineSpacing(7);

			VBox layout = new VBox();
			layout.setPrefSize(300, 200);
			layout.setSpacing(20);
			layout.getChildren().addAll(label, button);
			layout.setAlignment(Pos.CENTER);

			Scene scene = new Scene(layout);
			window.setScene(scene);
			window.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

