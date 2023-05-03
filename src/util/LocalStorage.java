package util;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Platform;

public class LocalStorage implements Serializable {
	private static final long serialVersionUID = 1L;
    private Path localStoragePath;

    public LocalStorage() {
        try {
            String userDir = System.getProperty("user.dir");
            userDir = userDir+"\\src\\resource";
            localStoragePath = Paths.get(userDir);
            if (!Files.isDirectory(localStoragePath)) {
                Files.createDirectory(localStoragePath);
            }
        } catch (IOException ex) {
            System.out.println("Unable to initialize local storage");
            ex.printStackTrace();
            Platform.exit();
        }
    }

    public Path getLocalStoragePath() {
        return localStoragePath;
    }
}
