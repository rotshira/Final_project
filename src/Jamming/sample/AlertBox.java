package Jamming.sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Roi on 9/23/2016.
 */
public class AlertBox {

    public static void display(String title,  String msg)
    {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label  = new Label(msg);
        Button close = new Button("Close the window");
        close.setOnAction(e -> window.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(close,label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
