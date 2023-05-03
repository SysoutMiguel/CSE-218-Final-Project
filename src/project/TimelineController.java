package project;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

import accounts.Comment;
import accounts.Post;
import accounts.User;
import data.Database;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TimelineController implements Initializable {
	private static Parent root;
	private static Stage stage;
	private static Scene scene;
	private Database data = Database.getInstance();
	static User currentUser;
	static String defaultPic = (System.getProperty("user.dir") + "\\\\src\\\\resource\\\\Default_User_PFP.png");
	static Image img = new Image(defaultPic);
	// User currentUser;
	@FXML
	private Circle circle1;

	@FXML
	private Circle circle2;

	@FXML
	private Circle circle3;

	@FXML
	private Circle circle4;

	@FXML
	private Circle circle5;
	@FXML
	private Label typosCountLbl;
	@FXML
	private Label typosLbl;
	@FXML
	private Circle circle6;

	@FXML
	private Circle userCircle;
	@FXML
	private ScrollPane timelineScrollPane;
	@FXML
	private Label menu;
	@FXML
	private Label menuBack;
	@FXML
	private ListView<Post> postListView;
	@FXML
	private AnchorPane sidebar;
	@FXML
	private AnchorPane backgroundPane;
	@FXML
	private ScrollPane commentsScrollPane;
	@FXML
	private ListView<Comment> commentsListView;
	@FXML
	private Button newCommentBtn;
	@FXML
	private ScrollPane subscribedScrollPane;
	@FXML
	private ListView<Post> subscribedListView;
	private Post currentPost;

	private static double xOffset = 0;
	private static double yOffset = 0;

	public void initUser(User currentUser) {
		TimelineController.currentUser = currentUser;
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
	private void goToSubsribedTimeline(MouseEvent e) throws IOException {
		if (subscribedScrollPane.isVisible()) {
			this.newCommentBtn.setVisible(false);
		} else {
			this.newCommentBtn.setVisible(false);
			displayScrollPane(subscribedScrollPane);
			populateSubscribedTimeline();
		}
	}

	public void displayScrollPane(ScrollPane sp) {
		commentsScrollPane.setVisible(false);
		subscribedScrollPane.setVisible(false);
		timelineScrollPane.setVisible(false);
		if (sp == commentsScrollPane) {
			commentsScrollPane.setVisible(true);
		} else if (sp == timelineScrollPane) {
			timelineScrollPane.setVisible(true);
		} else if (sp == subscribedScrollPane) {
			subscribedScrollPane.setVisible(true);
		} else {

		}
	}

	public void displaySubscibedTimeline() {
		timelineScrollPane.setVisible(false);
		subscribedScrollPane.setVisible(true);
	}

	public void populateSubscribedTimeline() {
		LinkedList<Post> posts = data.getAllPosts().getUserSubscribedPosts(currentUser);
		subscribedListView.getItems().setAll(posts);
	}

	@FXML
	private void goToTimeline(MouseEvent e) throws IOException {
		refreshTimeline();
		this.newCommentBtn.setVisible(false);
		displayScrollPane(timelineScrollPane);
	}

	private void returnToTimeline(ActionEvent e) throws IOException {
		refreshTimeline();
		this.newCommentBtn.setVisible(false);
		displayScrollPane(timelineScrollPane);
	}

	@FXML
	public void editInfo(MouseEvent me) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountInfo.fxml"));
		root = loader.load();
		AccountInfoController aic = loader.getController();
		aic.initUser(currentUser);
		aic.populateList();
		aic.updateLabels();
		aic.setDefaultPfp();
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
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	public void setDefaultPfP() {
		ArrayList<User> friends = currentUser.getSubscribed();
		if (friends != null) {
			Iterator<User> it = friends.iterator();
			for (User user : friends) {
				if (!user.getPfp().equals("")) {
					if (circle1.getFill().toString().equals("0x1e90ffff")) {
						circle1.setFill(new ImagePattern(user.getImage()));
					} else if (circle2.getFill().toString().equals("0x1e90ffff")) {
						circle2.setFill(new ImagePattern(user.getImage()));
					} else if (circle3.getFill().toString().equals("0x1e90ffff")) {
						circle3.setFill(new ImagePattern(user.getImage()));
					} else if (circle4.getFill().toString().equals("0x1e90ffff")) {
						circle4.setFill(new ImagePattern(user.getImage()));
					} else if (circle5.getFill().toString().equals("0x1e90ffff")) {
						circle5.setFill(new ImagePattern(user.getImage()));
					} else if (circle6.getFill().toString().equals("0x1e90ffff")) {
						circle6.setFill(new ImagePattern(user.getImage()));
					}
				}
				user = it.next();
			}
		}
		userCircle.setFill(new ImagePattern(currentUser.getImage()));
	}

	@FXML
	public void createPost(MouseEvent me) throws IOException {
		timelineScrollPane.setVisible(false);
		// Hide all posts and display create post gui
		TextArea ta = new TextArea();
		// Set font of textarea
		ta.setFont(new Font("Serif", 14));
		ta.setWrapText(true);
		ScrollPane scrollPane = new ScrollPane(ta);
		scrollPane.setMaxSize(500, 500);
		Button postBtn = new Button("Post");
		Button cancelBtn = new Button("Cancel");
		Button spellCheckBtn = new Button("Spell Check");
		HBox btnBox = new HBox(10, postBtn, cancelBtn, spellCheckBtn);
		backgroundPane.getChildren().addAll(scrollPane, btnBox);
		AnchorPane.setRightAnchor(btnBox, 5d);
		AnchorPane.setBottomAnchor(btnBox, 50d);
		//AnchorPane.setLeftAnchor(scrollPane, 5d);
		spellCheckBtn.setOnAction((e)->{
			typosLbl.setVisible(true);
			typosCountLbl.setVisible(true);
			typosCountLbl.setText(
					String.valueOf(spellCheck(ta.getText()))); 
		});
		cancelBtn.setOnAction((e) -> {
			try {
				typosLbl.setVisible(false);
				typosCountLbl.setVisible(false);
				scrollPane.setVisible(false);
				ta.setVisible(false);
				btnBox.setVisible(false);
				returnToTimeline(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		// Once user presses POST
		postBtn.setOnAction((e) -> {
			// Now user creates post
			typosLbl.setVisible(false);
			typosCountLbl.setVisible(false);
			scrollPane.setVisible(false);
			ta.setVisible(false);
			btnBox.setVisible(false);
			Post post = new Post();
			DateFormat format = new SimpleDateFormat("MM dd YYYY hh:mm");
			String date = format.format(new Date());
			post.setUserOfPost(currentUser);
			post.setDate(date);
			post.setCaption(ta.getText());
			data.allPosts.addPost(post);
			Database.save();
			try {

				returnToTimeline(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}


	private int spellCheck(String text) {
		int typos = (int) util.DictionaryMethods.getTypos(text);
		return typos;
	}

	@FXML
	public void logOut(MouseEvent me) throws IOException {
		Database.save();
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Runtime.getRuntime().gc();
		commentsScrollPane.setVisible(false);
		subscribedScrollPane.setVisible(false);

		postListView.setCellFactory(e -> new Cell() {
		});

		subscribedListView.setCellFactory(e -> new Cell() {
		});
		commentsListView.setCellFactory(e -> new Cell2() {
		});
	}

	public void refreshTimeline() {
		postListView.getItems().setAll(data.getAllPosts().getAllPosts());
	}

	// Look into add alls --> setAlls
	// check empty list
	// javaVisual VM or JConsole or Jprofiler or JvisualVM
	//

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
			buttonBox.setSpacing(50d);
			buttonBox.getChildren().addAll(likeBtn, commentBtn, likeLbl);// (likeImg, commentImg, likeLbl);
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
			likeBtn.setOnMouseClicked((e) -> {
				if (post.isLiked(TimelineController.currentUser)) {
					post.removeLike(TimelineController.currentUser);
					updateLikeLabel(post);
				} else {
					post.addPostLike(TimelineController.currentUser);
					updateLikeLabel(post);
				}
			});
			commentBtn.setOnMouseClicked((me) -> {
				root = null;
				scene = null;
				stage = null;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("Timeline.fxml"));
				try {
					root = loader.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				TimelineController tc = loader.getController();
				tc.initUser(currentUser);
				tc.setDefaultPfP();
				tc.showComments();
				tc.populateCommentsOfPost(post); // here I set currentPost to the post viewed
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
				stage.setResizable(false);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
			});
			profilePfp.setOnMouseClicked((me) -> {
				root = null;
				scene = null;
				stage = null;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
				try {
					root = loader.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ViewProfileController vpc = loader.getController();
				vpc.initMainUser(currentUser);
				vpc.initUserToView(post.getUserOfPost());
				vpc.populateList();
				vpc.updateLabels();
				vpc.setDefaultPfp();
				vpc.followButtonHandler();
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
				stage.setResizable(false);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();

			});
		}

		private void updateLikeLabel(Post post) {
			this.likeLbl.setText(post.getPostLikes() + "");
		}

	}

	public void populateCommentsOfPost(Post post) {
		this.currentPost = post;
		commentsListView.getItems().setAll(data.getAllComments().getAllCommentsOfPost(post));
	}

	static class Cell2 extends ListCell<Comment> {
		HBox buttonBox = new HBox(5);
		HBox userInfoBox = new HBox(5);
		BorderPane bPane = new BorderPane();
		Pane pane = new Pane();
		Label commentCaption = new Label();
		Label commentUserName = new Label();
		Label commentDate = new Label();
		ImageView profilePfp = new ImageView();

		public Cell2() {
			super();
			commentCaption.setWrapText(true);
			commentDate.setPadding(new Insets(10, 10, 10, 10));
			profilePfp.setFitHeight(40d);
			profilePfp.setFitWidth(40d);
			profilePfp.setImage(img);
			userInfoBox.getChildren().addAll(profilePfp, commentUserName, commentDate);
			buttonBox.setSpacing(100d);
			bPane.setCenter(commentCaption);
			bPane.setBottom(buttonBox);
			bPane.setLeft(userInfoBox);
			bPane.setStyle("-fx-background-color: BLACK");
		}

		public void updateItem(Comment comment, boolean empty) {
			super.updateItem(comment, empty);
			setText(null);
			setGraphic(null);
			if (comment != null && !empty) {
				commentCaption.setText(comment.getCaptionOfComment());
				commentUserName.setText(comment.getUserOfComment().getEmail());
				commentDate.setText(comment.getDate());
				setPrefHeight(200);
				setPrefWidth(350);
				setGraphic(bPane);
			}
			profilePfp.setOnMouseClicked((me) -> {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewProfile.fxml"));
				try {
					root = loader.load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ViewProfileController vpc = loader.getController();
				vpc.initMainUser(currentUser);
				vpc.initUserToView(comment.getUserOfComment());
				vpc.populateList();
				vpc.updateLabels();
				vpc.setDefaultPfp();
				vpc.followButtonHandler();
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
				stage.setResizable(false);
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();

			});
		}
	}

	public void showComments() {
		displayScrollPane(commentsScrollPane);
		this.newCommentBtn.setVisible(true);
	}

	@FXML
	public void createNewComment(ActionEvent ae) {
//		this.timelineScrollPane.setVisible(false);
		this.commentsScrollPane.setVisible(false);
		// Hide all posts and display create post gui
		TextArea ta = new TextArea();
		// Set font of textarea
		ta.setFont(new Font("Serif", 14));
		ta.setWrapText(true);
		// Scrollpane to hold text area
		ScrollPane scrollPane = new ScrollPane(ta);
		scrollPane.setMaxSize(500, 500);
		// scrollPane.setPadding(new Insets(5,5,5,5));
		// Button to post or cancel
		Button postBtn = new Button("Post");
		Button cancelBtn = new Button("Cancel");
		HBox btnBox = new HBox(10, postBtn, cancelBtn);
		backgroundPane.getChildren().addAll(scrollPane, btnBox);
		AnchorPane.setRightAnchor(btnBox, 5d);
		AnchorPane.setBottomAnchor(btnBox, 50d);
		AnchorPane.setLeftAnchor(scrollPane, 5d);
		cancelBtn.setOnAction((ce) -> {
			try {
				scrollPane.setVisible(false);
				btnBox.setVisible(false);
				this.newCommentBtn.setVisible(false);
				returnToTimeline(ce);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		// Once user presses POST
		postBtn.setOnAction((pe) -> {
			// Now user creates post
			scrollPane.setVisible(false);
			btnBox.setVisible(false);
			this.newCommentBtn.setVisible(false);
			Comment comment = new Comment();
			DateFormat format = new SimpleDateFormat("MM dd YYYY hh:mm");
			String date = format.format(new Date());
			comment.setUserOfComment(currentUser);
			comment.setDate(date);
			comment.setCaptionOfComment(ta.getText());
			comment.setPostOfComment(currentPost);
			data.allComments.addComment(comment);
			Database.save();
			try {
				returnToTimeline(pe);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

}
