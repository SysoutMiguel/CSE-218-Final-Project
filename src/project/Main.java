package project;

import data.Database;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Main extends Application {
	private double xOffset = 0;
	private double yOffset = 0;
	public static void main(String[] args) throws ClassCastException {
		Database.load();
		//Database.getInstance().allUsers.display();
		Database.loadDictionary();
		launch(args);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				Database.save();
				System.out.println("Exiting");
			}
		});
	}

	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Social Media App");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		scene.getStylesheets().add("resource/createStyle.css");
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

}