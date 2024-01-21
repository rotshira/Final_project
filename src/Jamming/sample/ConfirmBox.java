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
public class ConfirmBox {

    static boolean answer;

    public static boolean display(String title,  String msg)
    {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label  = new Label(msg);
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Button yesButton = new Button("yes");
        Button noButton = new Button("no");
       yesButton.setOnAction(e->{
            answer= true;
            window.close();
        });
        noButton.setOnAction(e->{
            answer= false;
            window.close();
        });

        layout.getChildren().addAll(label,yesButton, noButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

}
