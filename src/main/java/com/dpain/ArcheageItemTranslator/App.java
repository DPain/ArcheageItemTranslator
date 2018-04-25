package com.dpain.ArcheageItemTranslator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application {
	public static Translator translator;

	public static void main(String[] args) {
		System.out.println("App Initialized!");

		translator = new Translator();
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
		loader.setController(new AppController(translator));

		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("아키 아이템 번역기");
		primaryStage.setOnCloseRequest(e -> close());
		try {
			primaryStage.getIcons().add(new Image(App.class.getResourceAsStream("icon.png")));
		} catch(Exception e) {
			System.out.println("No icon!");
		}
		primaryStage.show();
	}
	
	public void close() {
		if(translator.driver != null) {
			translator.driver.quit();
		}
		System.exit(0);
	}
}