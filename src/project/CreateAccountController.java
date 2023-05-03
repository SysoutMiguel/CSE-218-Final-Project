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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class CreateAccountController {
	private Parent root;
	private Stage stage;
	private Scene scene;

	@FXML
	private CheckBox showPass;
	@FXML
	private CheckBox showRePass;
	@FXML
	private TextField tfPass;
	@FXML
	private TextField tfRePass;
	@FXML
	private TextField tfFirst;
	@FXML
	private TextField tfLast;
	@FXML
	private TextField tfEmail;
	@FXML
	private PasswordField pwPass = null;
	@FXML
	private PasswordField pwRePass = null;
	private Database data = Database.getInstance();
	protected double xOffset;
	protected double yOffset;
	
	public void createAccount(ActionEvent ae) throws IOException {
		String firstName = tfFirst.getText();
		String lastName = tfLast.getText();
		String email = tfEmail.getText();
		String pass1 = pwPass.getText();
		String pass2 = pwRePass.getText();

		if (fieldsEmpty()) {
			Alert emptyFields = new Alert(AlertType.WARNING);
			emptyFields.setTitle("Missing Information!!");
			emptyFields.setContentText("Enter Missing Information");
			emptyFields.showAndWait();
		} else {
			User isEmailAvailable = data.allUsers.findAcct(email);
			if (isEmailAvailable != null) {
				Alert unavailable = new Alert(AlertType.WARNING);
				unavailable.setTitle("Invalid Email");
				unavailable.setContentText("The Email is already associated to an account!!");
				unavailable.showAndWait();
				return;
			}
			if (!(pass2.equals(pass1))) {
				Alert passwordNotConfirmed = new Alert(AlertType.WARNING);
				passwordNotConfirmed.setTitle("Invalid Password");
				passwordNotConfirmed.setContentText("Please match the same password!");
				passwordNotConfirmed.showAndWait();
				return;
			} else {
				if (validatePassword(pass1)) {
					User userToAdd= new User(email, pass1);
					userToAdd.setfName(firstName);
					userToAdd.setlName(lastName);
					data.allUsers.addAccount(userToAdd);
					Database.save();
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountInfo.fxml"));
					root = loader.load();
					AccountInfoController aic = loader.getController();
					aic.initUser(userToAdd);
					aic.updateLabels();
					stage = (Stage) ((Node) ae.getSource()).getScene().getWindow();
					scene = new Scene(root);
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
					stage.setResizable(false);
					stage.setScene(scene);
					stage.centerOnScreen();
					stage.show();
				}
			}
		}
	}

	@FXML
	void changeVisibilityOfPass(ActionEvent event) {
		if (showPass.isSelected()) {
			tfPass.setText(pwPass.getText());
			tfPass.setVisible(true);
			pwPass.setVisible(false);
			return;
		}
		pwPass.setText(tfPass.getText());
		pwPass.setVisible(true);
		tfPass.setVisible(false);
	}

	@FXML
	void changeVisibilityOfRePass(ActionEvent event) {
		if (showRePass.isSelected()) {
			tfRePass.setText(pwRePass.getText());
			tfRePass.setVisible(true);
			pwRePass.setVisible(false);
			return;
		}
		pwRePass.setText(tfRePass.getText());
		pwRePass.setVisible(true);
		tfRePass.setVisible(false);

	}

	public boolean validatePassword(String password) {
		boolean upperCase = false;
		boolean lowerCase = false;
		boolean digit = false;
		if (password.length() >= 7) {
			for (int i = 0; i < password.length(); i++) {
				if (digit && upperCase && lowerCase)
					break;
				if (!(upperCase)) {
					if (Character.isUpperCase(password.charAt(i)))
						upperCase = true;
				}
				if (!(lowerCase)) {
					if (Character.isLowerCase(password.charAt(i)))
						lowerCase = true;
				}
				if (!(digit)) {
					if (Character.isDigit(password.charAt(i)))
						digit = true;
				}
			}
			if ((!digit || !upperCase || !lowerCase)) {
				Alert passwordNotStrongEnough = new Alert(AlertType.WARNING);
				passwordNotStrongEnough.setTitle("Wrong password inputs");
				passwordNotStrongEnough.setContentText(
						"Please make sure your passwords contains: 1 uppercase letter, 1 lowercase letter, and 1 digit!");
				passwordNotStrongEnough.showAndWait();
				return false;
			} else
				return true;
		} else {
			Alert passwordNotLongEnough = new Alert(AlertType.WARNING);
			passwordNotLongEnough.setTitle("Wrong password inputs");
			passwordNotLongEnough.setContentText("Please make sure your passwords is at least 7 characters long!");
			passwordNotLongEnough.showAndWait();
		}

		return false;

	}

	private boolean fieldsEmpty() {
		if (tfFirst.getText().isEmpty() || tfLast.getText().isEmpty() || tfEmail.getText().isEmpty()
				|| pwPass.getText().isEmpty() || pwRePass.getText().isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

}
