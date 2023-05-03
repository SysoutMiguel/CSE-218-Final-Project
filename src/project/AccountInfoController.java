package project;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.ResourceBundle;

import accounts.Post;
import accounts.User;
import data.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.ImageService;
import util.LocalStorage;

public class AccountInfoController implements Initializable {
	private static Parent root;
	private static Stage stage;
	private static Scene scene;
	private double xOffset = 0;
	private double yOffset = 0;
	private Database data = Database.getInstance();
	static User currentUser;
	FileChooser fc = new FileChooser();
	private ImageService is;

	@FXML
	private TextField tfPhone;
	@FXML
	private TextField tfAge;
	@FXML
	private ComboBox<String> cbStates = new ComboBox<>();
	@FXML
	private BorderPane extraDetailsPane;

	@FXML
	private Label ageLbl;

	@FXML
	private Label changePicLbl;

	@FXML
	private Label emailLbl;

	@FXML
	private Label fNameLbl;

	@FXML
	private Label lNameLbl;

	@FXML
	private Label noPostLbl;

	@FXML
	private Label stateLbl;

	@FXML
	private Circle pfp;

	@FXML
	private Circle profilePictureCir;

	@FXML
	private Button editInfoBtn;

	@FXML
	private ListView<Post> postLV;

	@FXML
	public void editInfo(ActionEvent event) {
		extraDetailsPane.setVisible(true);
	}

	public void confirm(ActionEvent event) throws IOException {
		String phone = tfPhone.getText();
		String age = tfAge.getText();
		String state = cbStates.getValue();

		if (state == null) {
			Alert wrongLength = new Alert(AlertType.WARNING);
			wrongLength.setTitle("Please choose a state");
			wrongLength.setContentText("Make sure you fill the rest");
			wrongLength.showAndWait();
			return;
		} else {
			currentUser.setState(state);
		}

		if (!phone.isEmpty()) {
			if (phone.length() != 10) {
				Alert wrongLength = new Alert(AlertType.WARNING);
				wrongLength.setTitle("Input fields wrong");
				wrongLength.setContentText("Please make sure phone number is 10 numbers");
				wrongLength.showAndWait();
				return;
			}

			boolean isNotNumPhone = false;
			for (int i = 0; i < phone.length(); i++) {
				if (!Character.isDigit(phone.charAt(i)))
					isNotNumPhone = true;
			}
			if (isNotNumPhone) {
				Alert notANumber = new Alert(AlertType.WARNING);
				notANumber.setTitle("Input fields wrong");
				notANumber.setContentText("Please make sure phone number is ONLY numbers");
				notANumber.showAndWait();
				return;
			}
			currentUser.setPhoneNumber(phone);
		}

		if (!age.isEmpty()) {
			if (Integer.valueOf(age) < 18) {
				Alert wrongLength = new Alert(AlertType.WARNING);
				wrongLength.setTitle("Age Restriction");
				wrongLength.setContentText("You must be over 18 to register");
				wrongLength.showAndWait();
				return;
			}
			currentUser.setAge(age);

		}
		updateLabels();
		extraDetailsPane.setVisible(false);
	}

	public void updateLabels() {
		fNameLbl.setText(currentUser.getFname());
		lNameLbl.setText(currentUser.getlName());
		ageLbl.setText(currentUser.getAge());
		stateLbl.setText(currentUser.getState());
		emailLbl.setText(currentUser.getEmail());

	}

	public void goToTimeline(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		TimelineController ec = loader.getController();
		ec.initUser(currentUser);
		ec.setDefaultPfP();
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
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
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();

	}

	@FXML
	public void exit(MouseEvent me) throws IOException {
		ButtonType logOutBtn = new ButtonType("LogOut");
		ButtonType cancel = new ButtonType("Cancel");
		Alert logOut = new Alert(AlertType.NONE, "Log Out???", logOutBtn, cancel);
		logOut.showAndWait().ifPresent(e -> {
			if ((e == logOutBtn)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
				try {
					root = loader.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				stage = (Stage) ((Node) me.getSource()).getScene().getWindow();
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
				stage.setResizable(true);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
			} else if (e == cancel) {

			}
		});
		;
	}

	@FXML
	public void explore(MouseEvent me) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		scene = new Scene(root);
		// stage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add("resource/createStyle.css");
		TimelineController e = loader.getController();
		e.initUser(currentUser);
		e.setDefaultPfP();
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
		stage = (Stage) ((Node) me.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	@FXML
	public void createPost(MouseEvent me) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add("resource/createStyle.css");
		TimelineController e = loader.getController();
		e.initUser(currentUser);
		e.createPost(me);
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
		stage = (Stage) ((Node) me.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	public void initUser(User user) {
		AccountInfoController.currentUser = user;
	}

	public void exit(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFXML.fxml"));
		root = loader.load();
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbStates.getItems().addAll("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN",
				"IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM",
				"NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV",
				"WI", "WY");
	}

	public void setDefaultPfp() {
		LocalStorage ls = new LocalStorage();
		is = new ImageService(ls, currentUser);
		String defaultPic = System.getProperty("user.dir");
		defaultPic = defaultPic + "\\src\\resource\\Default_User_PFP.png";
		if (currentUser.getPfp() == null) {
			Image img = new Image(defaultPic);
			profilePictureCir.setFill(new ImagePattern(img));
			pfp.setFill(new ImagePattern(img));
		} else {
			Image im = new Image(currentUser.getPfp());
			profilePictureCir.setFill(new ImagePattern(im));
			pfp.setFill(new ImagePattern(im));
		}
	}

	public void populateList() {
		postLV.setCellFactory(e -> new Cell() {
		});
		LinkedList<Post> posts = data.getAllPosts().getUserPosts(currentUser);
		postLV.getItems().setAll(posts);
	}

	static class Cell extends ListCell<Post> {
		HBox buttonBox = new HBox(5);
		HBox userInfoBox = new HBox(5);
		BorderPane bPane = new BorderPane();
		Button removePost = new Button("REMOVE");
		Button commentBtn = new Button("Comment");
		Pane pane = new Pane();
		Label postCaption = new Label();
		Label postUserName = new Label();
		Label postDate = new Label();
		ImageView profilePfp = new ImageView();
		ImageView likeImg = new ImageView();
		ImageView commentImg = new ImageView();
		Label likeLbl = new Label();

		public Cell() {
			super();
			postCaption.setWrapText(true);
			postDate.setPadding(new Insets(10, 10, 10, 10));
			profilePfp.setFitHeight(40d);
			profilePfp.setFitWidth(40d);
			likeImg.setFitHeight(40d);
			likeImg.setFitWidth(70d);
			likeLbl.setFont(new Font("Ariel", 30));
			likeLbl.setPrefHeight(20);
			likeLbl.setPrefWidth(50);
			commentImg.setFitHeight(40d);
			commentImg.setFitWidth(70d);
			likeImg.setImage(new Image("resource/like.png"));
			commentImg.setImage(new Image("resource/preComment.png"));
			profilePfp.setImage(new Image("resource/Default_User_PFP.png"));
			commentBtn.setGraphic(commentImg);
			userInfoBox.getChildren().addAll(profilePfp, postUserName, postDate);
			buttonBox.setSpacing(100d);
			buttonBox.getChildren().addAll(likeImg, commentImg, likeLbl);
			bPane.setCenter(postCaption);
			bPane.setBottom(buttonBox);
			bPane.setLeft(userInfoBox);
			bPane.setStyle("-fx-background-color: BLACK");
			postCaption.setTextFill(Color.WHITE);
			postUserName.setTextFill(Color.YELLOW);
			postDate.setTextFill(Color.WHITE);
			likeLbl.setTextFill(Color.LIMEGREEN);
		}

		public void updateItem(Post post, boolean empty) {
			super.updateItem(post, empty);
			setText(null);
			setGraphic(null);
			if (post != null && !empty) {
				postCaption.setText(post.getCaption());
				postUserName.setText(post.getUserOfPost().getEmail());
				postDate.setText(post.getDate());
				profilePfp.setImage(post.getUserOfPost().getImage());
				likeLbl.setText(post.getPostLikes() + "");
				setPrefHeight(200);
				setPrefWidth(350);
				setGraphic(bPane);
			}

			likeImg.setOnMouseClicked((e) -> {
				if (post.isLiked(AccountInfoController.currentUser)) {
					post.removeLike(AccountInfoController.currentUser);
					updateLikeLabel(post);
				} else {
					post.addPostLike(AccountInfoController.currentUser);
					updateLikeLabel(post);
				}

			});

		}

		private void updateLikeLabel(Post post) {
			this.likeLbl.setText(post.getPostLikes() + "");
		}

	}

	@FXML
	public void uploadImageLabel(MouseEvent me) throws IOException {
		// set title
		LocalStorage ls = new LocalStorage();
		is = new ImageService(ls, currentUser);
		fc.setTitle("Upload Picture");
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"));
		File selectedFile = fc.showOpenDialog(null);// shows new file open dialog
		if (selectedFile != null) {
			try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(selectedFile.toPath()))) {
				is.uploadCustomImg(inputStream);
			} catch (IOException e) {
				System.out.println("Error uploading Image");
			}
		}
	}

	@FXML
	public void changePfP(MouseEvent me) throws IOException {
		LocalStorage ls = new LocalStorage();
		is = new ImageService(ls, currentUser);
		fc.setTitle("Change Profile Picture");
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"));
		String userDir = System.getProperty("user.dir");
		userDir = userDir + "\\src\\resource";
		fc.setInitialDirectory(new File(userDir));
		File selectedFile = fc.showOpenDialog(null);// shows new file open dialog
		if (selectedFile != null) {
			try {
				currentUser.setPfp(selectedFile.toString());
				updatePfPCircles();
			} catch (Exception e) {
				System.out.println("Error changing profile Picture");
			}
		}
	}

	private void updatePfPCircles() {
		profilePictureCir.setFill(new ImagePattern(currentUser.getImage()));
		pfp.setFill(new ImagePattern(currentUser.getImage()));
	}

}
