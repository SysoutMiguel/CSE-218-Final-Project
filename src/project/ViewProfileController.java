package project;

import java.io.IOException;
import java.util.LinkedList;

import accounts.Post;
import accounts.User;
import data.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class ViewProfileController {
	private static Parent root;
	private static Stage stage;
	private static Scene scene;
	private double xOffset = 0;
	private double yOffset = 0;
	private Database data = Database.getInstance();
	static User mainUser;
	static User displayUser;
	static String defaultPic = (System.getProperty("user.dir") + "\\\\src\\\\resource\\\\Default_User_PFP.png");
	static Image img = new Image(defaultPic);
	FileChooser fc = new FileChooser();
	ImageService is;
	@FXML
	private Label ageLbl;

	@FXML
	private ScrollPane augmentedPostListView;

	@FXML
	private Label emailLbl;

	@FXML
	private Label fNameLbl;

	@FXML
	private Button followBtn;

	@FXML
	private Label lNameLbl;

	@FXML
	private Circle pfp;

	@FXML
	private ListView<Post> postLV;

	@FXML
	private Circle profilePictureCir;

	@FXML
	private Label stateLbl;

	@FXML
	private TextField tfAge;

	@FXML
	private TextField tfPhone;

	@FXML
	private Button unfollowBtn;

	public void followButtonHandler() {
		if (mainUser.follows(displayUser)) {
			followBtn.setVisible(false);
			unfollowBtn.setVisible(true);
		}
		followBtn.setOnMousePressed((e) -> {
			mainUser.subscribeTo(displayUser);
			displayUser.addSubscriber(mainUser);
			followBtn.setVisible(false);
			unfollowBtn.setVisible(true);
		});

		unfollowBtn.setOnMousePressed((e) -> {
			mainUser.unsubscribeTo(displayUser);
			displayUser.removeSubscriber(mainUser);
			followBtn.setVisible(true);
			unfollowBtn.setVisible(false);
		});
	}

	public void updateLabels() {
		fNameLbl.setText(displayUser.getFname());
		lNameLbl.setText(displayUser.getlName());
		ageLbl.setText(displayUser.getAge());
		stateLbl.setText(displayUser.getState());
		emailLbl.setText(displayUser.getEmail());

	}

	public void goToTimeline(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		TimelineController ec = loader.getController();
		ec.initUser(mainUser);
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

	public void populateList() {
		postLV.setCellFactory(e -> new Cell() {
		});
		LinkedList<Post> posts = data.getAllPosts().getUserPosts(displayUser);
		postLV.getItems().setAll(posts);
	}

	static class Cell extends ListCell<Post> {
		HBox buttonBox = new HBox(5);
		HBox userInfoBox = new HBox(5);
		BorderPane bPane = new BorderPane();
		Button removePost = new Button("REMOVE");
		Button commentBtn = new Button("Comment");
		Button likeBtn = new Button("Like");
		Pane pane = new Pane();
		Label postCaption = new Label();
		Label postUserName = new Label();
		Label postDate = new Label();
		ImageView profilePfp = new ImageView();
//		ImageView likeImg = new ImageView();
//		ImageView commentImg = new ImageView();
		Label likeLbl = new Label();

		public Cell() {
			super();
			postCaption.setWrapText(true);
			postDate.setPadding(new Insets(10, 10, 10, 10));
			profilePfp.setFitHeight(40d);
			profilePfp.setFitWidth(40d);
			likeBtn.setShape(new Circle(4.0));
			likeBtn.setStyle("-fx-background-color:  #8381f2");
			commentBtn.setShape(new Circle(4.0));
			commentBtn.setStyle("-fx-background-color:  #8381f2");
			likeBtn.setPrefWidth(100.0);
			commentBtn.setPrefWidth(100.0);
			bPane.setPadding(new Insets(5,5,5,5));
			likeLbl.setFont(new Font("Ariel", 30));
			likeLbl.setPrefHeight(20);
			likeLbl.setPrefWidth(50);
			profilePfp.setImage(img);
			userInfoBox.getChildren().addAll(profilePfp, postUserName, postDate);
			buttonBox.setSpacing(100d);
			buttonBox.getChildren().addAll(likeBtn, commentBtn, likeLbl); // (likeImg, commentImg, likeLbl);
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
				likeLbl.setText(post.getPostLikes() + "");
				setPrefHeight(200);
				setPrefWidth(350);
				setGraphic(bPane);
			}

			likeBtn.setOnMouseClicked((e) -> {
				if (post.isLiked(ViewProfileController.mainUser)) {
					post.removeLike(ViewProfileController.mainUser);
					updateLikeLabel(post);
				} else {
					post.addPostLike(ViewProfileController.mainUser);
					updateLikeLabel(post);
				}

			});

		}

		private void updateLikeLabel(Post post) {
			this.likeLbl.setText(post.getPostLikes() + "");
		}

	}

	public void initMainUser(User user) {
		ViewProfileController.mainUser = user;
	}

	public void initUserToView(User user) {
		ViewProfileController.displayUser = user;
	}

	public void setDefaultPfp() {
		LocalStorage ls = new LocalStorage();
		is = new ImageService(ls, mainUser);
		String defaultPic = System.getProperty("user.dir");
		defaultPic = defaultPic + "\\src\\resource\\Default_User_PFP.png";
		profilePictureCir.setFill(new ImagePattern(mainUser.getImage()));
		profilePictureCir.setFill(new ImagePattern(displayUser.getImage()));
		pfp.setFill(new ImagePattern(mainUser.getImage()));
	}

	@FXML
	public void explore(MouseEvent me) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		TimelineController e = loader.getController();
		e.initUser(mainUser);
		e.setDefaultPfP();
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
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
		Runtime.getRuntime().gc();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
		root = loader.load();
		scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		scene.getStylesheets().add("resource/createStyle.css");
		TimelineController e = loader.getController();
		e.initUser(mainUser);
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
}
