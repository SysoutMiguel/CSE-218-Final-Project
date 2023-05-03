package project;

import java.io.IOException;

import accounts.User;
import data.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login {
	private Parent root;
	private Stage stage;
	private Scene scene;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	private double xOffset;
	private double yOffset;
	Database data = Database.getInstance();

	public void login(ActionEvent e) throws IOException {
		String username = emailField.getText().toString();
		String password = passwordField.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			Alert emptyFields = new Alert(AlertType.WARNING);
			emptyFields.setTitle("Input fields empty");
			emptyFields.setContentText("Please fill all input fields");
			emptyFields.showAndWait();
		} else {
			if (username.equals("admin") && password.equals("admin")) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Administrator.fxml"));
				root = loader.load();
				stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setResizable(false);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
			}
			User foundUser = validateLogin(username);
			if (foundUser != null) {
				if (checkPass(foundUser, password)) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
					root = loader.load();
					TimelineController tc = loader.getController();
					tc.initUser(foundUser);
					tc.setDefaultPfP();
					//tc.initAndHideScrollPanes();
					scene = new Scene(root);
					scene.setFill(Color.TRANSPARENT);
					//scene.getStylesheets().add("resource/createStyle.css");
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
					stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					stage.setResizable(false);
					stage.setScene(scene);
					stage.centerOnScreen();
					stage.show();
				} else {
					Alert emptyFields = new Alert(AlertType.WARNING);
					emptyFields.setTitle("Account Password Wrong");
					emptyFields.setContentText("Account was found, check your password again!");
					emptyFields.showAndWait();
				}

			} else {
				Alert emptyFields = new Alert(AlertType.WARNING);
				emptyFields.setTitle("Account Not Found");
				emptyFields.setContentText("Account was not found");
				emptyFields.showAndWait();
			}
		}

	}

	public boolean checkPass(User foundUser, String password) {
		if (foundUser.getPassword().equals(password))
			return true;
		return false;

	}

	public User validateLogin(String username) {
		User foundAcct = data.allUsers.findAcct(username);
		if (foundAcct != null)
			return foundAcct;
		else
			return null;
	}

	public void createAccount(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();

	}

	public void exit(ActionEvent e) {
		ButtonType logOutBtn = new ButtonType("LogOut");
		ButtonType cancel = new ButtonType("Cancel");
		Alert logOut = new Alert(AlertType.NONE,"Log Out???",logOutBtn, cancel);
		logOut.showAndWait().ifPresent(exit ->{
			if ((exit == logOutBtn)) {
				System.exit(0);
			} else if (exit == cancel) {
				
			}
		});;
	}

}
