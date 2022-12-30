package ca.qc.bdeb.inf203.tp1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainJavaFX extends Application{
    public static void main(String[] args) {
        launch(args);
    }
    private int longueur=15;
    private int largeur=15;

    @Override
    public void start(Stage stage) throws Exception {
        /**
         * Ayez pitié dans la correction.....
         */
        int bombe=5;
        BorderPane root = new BorderPane();
        Demineur demineur=new Demineur(longueur,largeur,bombe);
        var gridPane=new GridPane();
        var titre=new Text("Demineur");
        titre.setFont(Font.font(40));
        var np=new Button("Nouvelle partie");
        var recommencer =new Button("Recommencer");
        var abandon =new Button("Abandonner");
        //les boutons
        VBox haut=new VBox();
        VBox b1=new VBox();
        b1.setAlignment(Pos.CENTER);
        b1.getChildren().add(titre);
        HBox b2=new HBox();
        VBox b21=new VBox();VBox b22=new VBox();VBox b23=new VBox();
        b21.setPrefWidth(350);b22.setPrefWidth(350);
        b21.getChildren().add(np);b22.getChildren().add(recommencer);b23.getChildren().add(abandon);
        b2.getChildren().addAll(b21,b22,b23);
        HBox b3=new HBox();
        var choix=new SplitMenuButton();
        choix.setText("Mines:");
        MenuItem c1 = new MenuItem("1");
        MenuItem c2 = new MenuItem("5");
        MenuItem c3 = new MenuItem("15");
        MenuItem c4 = new MenuItem("30");
        MenuItem c5 = new MenuItem("200");
        choix.getItems().addAll(c1,c2,c3,c4,c5);
        b3.getChildren().addAll(choix);
        haut.getChildren().addAll(b1,b2,b3);
        root.setTop(haut);
        var leftbox=new HBox();
        leftbox.setPrefWidth(150);
        var rightbox=new HBox();
        leftbox.setPrefWidth(150);
        root.setLeft(leftbox);
        root.setRight(rightbox);
        root.setCenter(gridPane);
        var indice=new Text("Encore "+demineur.getDrapeau()+" mines!");
        //le texte sur les bombes, en bas.
        demineur.jouerEnFX(gridPane,root,indice,abandon,recommencer,np,c1,c2,c3,c4,c5);
        root.setBottom(indice);
        Scene scene = new Scene(root);
        stage.setTitle("Demineur");
        stage.setScene(scene);
        stage.getIcons().add(new Image("bombe.png"));
        stage.show();
        //L'affichage du démineur, le titre, les boutons et etc.
    }
}
