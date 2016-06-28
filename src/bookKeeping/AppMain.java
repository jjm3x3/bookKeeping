package bookKeeping;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppMain extends Application {

	public static void main(String[] args) {
		System.out.println("HI brenna");
		launch(args);

	}

	@Override
	public void start(Stage firstStage) throws Exception {
		Button btn = new Button();
		btn.setText("sayHI to brenna");
		btn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("Hi brenna");
				// TODO Auto-generated method stub
				
			}
			
		});
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(btn);
		
		
		Scene scene = new Scene(stackPane, 300, 300);
		firstStage.setScene(scene);
		firstStage.setTitle("the 'say hi' to brenna app");
		firstStage.show();
	}

}
