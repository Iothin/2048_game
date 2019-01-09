package pl.lmb.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
	
			Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/pl/lmb/view/MainPane.fxml"));
			Scene scene = new Scene(parent);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	        primaryStage.setMinWidth(620);
	        primaryStage.setMinHeight(570);
			primaryStage.setScene(scene);
			primaryStage.setTitle("2048 game");
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
