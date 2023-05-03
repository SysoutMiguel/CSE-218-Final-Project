package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import accounts.User;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.image.Image;
import project.AccountInfoController;

public class ImageService implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_IMG_RSC = "/resource/Default_User_PFP.png";
	private static String IMG_FILENAME;//"fileName.png";
	private int imageId = 0;
	private final Path imgPath;
	private final ReadOnlyObjectWrapper<Image> theImg = new ReadOnlyObjectWrapper<>();

	public ImageService(LocalStorage ls, User user) {
		imageId++;
		IMG_FILENAME = user.getEmail()+imageId+".png";
		imgPath = ls.getLocalStoragePath().resolve(IMG_FILENAME);
		if (!Files.exists(imgPath)) {
			copyImage();
		}
		refreshImage();
	}

	private void copyImage() {
		try {
			Files.copy(Objects.requireNonNull(AccountInfoController.class.getResourceAsStream(DEFAULT_IMG_RSC)), imgPath);
		} catch (IOException e) {
			System.out.println("Unable to init image");
			e.printStackTrace();
			Platform.exit();
		}
	}

	public void uploadCustomImg(InputStream inputStream) {
		try {
			Files.copy(inputStream, imgPath, StandardCopyOption.REPLACE_EXISTING);

			refreshImage();
		} catch (IOException e) {
			System.out.println("Unable to upload custom avatar");
			e.printStackTrace();
		}
	}

	public Image getImage() {
		return theImg.get();
	}
	
    public ReadOnlyObjectProperty<Image> avatarImageProperty() {
        return theImg.getReadOnlyProperty();
    }
    
    private void refreshImage() {
        theImg.set(
                new Image(
                        "file:" + imgPath.toAbsolutePath()
                )
        );
    }

}
