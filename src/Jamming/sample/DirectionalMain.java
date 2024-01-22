package Jamming.sample;

import Geometry.Point2D;
import Jamming.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.util.Random;

import java.awt.*;

public class DirectionalMain extends Application {

    Button button2;
    Stage window;
    int phase;
    boolean flag;
    Scene scene1,scene2;
    @Override
    public void start(Stage primaryStage) throws Exception{
        //  Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        phase = 0;
        flag = false;
        window = primaryStage;
        Simulation JamSimulation = new Simulation(new Point2D(400,400), new Point2D(700, 700));
        //JammerParticle tst = new JammerParticle( 25, new Point2D(400,400));
        // JamSimulation.getJammerParticles().getJamList().get(0).setJamLoc(new Point2D(405,401));
        window.setOnCloseRequest(e->
                {
                    e.consume();
                    closewindows();

                }
        );

        BorderPane border = new BorderPane();
        HBox topMenu2 = new HBox();
        Label BotomMenu = new Label();
        Group root = new Group();
        Canvas canvas = new Canvas(800, 800);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //Image img = ImageIO.read(new File("Ariel_photo.png"));
        String imagePath = "Ariel_photo.png";
        Image image = new Image(imagePath);
// Draw the Image
        gc.setGlobalAlpha(0.5);
        gc.drawImage(image,0, 0, 800, 800);
        gc.setGlobalAlpha(1);


        Button Init = new Button("Change Jammer Velocity");
        Init.setOnAction(e->
        {
            BotomMenu.setText("Change Jammer Velocity...");
            for(int i=0; i<JamSimulation.RealJammerList.size(); i++)
                JamSimulation.RealJammerList.get(i).initialVelocity(new Random(phase+i));


        });

        Button runSimulation = new Button("stop Jammer 1");
        runSimulation.setOnAction(e->
                {
                    if (flag == false) {
                        runSimulation.setText("Activate Jammer 1");
                        JamSimulation.RealJammerList.get(0).setActive(false);
                    }
                    else if (flag == true) {
                        runSimulation.setText("stop Jammer 1");
                        JamSimulation.RealJammerList.get(0).setActive(true);
                    }

                    flag = !flag;

                }
        );
        Button runSimulation2 = new Button("stop Jammer 2");
        runSimulation2.setOnAction(e->
                {
                    if (flag == false) {
                        runSimulation2.setText("Activate Jammer 2");
                        JamSimulation.RealJammerList.get(1).setActive(false);
                    }
                    else if (flag == true) {
                        runSimulation2.setText("stop Jammer 2");
                        JamSimulation.RealJammerList.get(0).setActive(true);

                    }
                    flag = !flag;

                }
        );
        Button ClearButton = new Button("Clear Canvas");
        topMenu2.setAlignment(Pos.CENTER);
        // ClearButton.setAlignment(Pos.BASELINE_LEFT);
        ClearButton.setOnAction(e->
        {
            ClearScreen(gc);
        });

        //



        Button randButton = new Button("Next Phase");
        randButton.setOnAction(e->
        {

            System.out.println("");

            System.out.println("New Phase:");
            JamSimulation.getReceiverList().movebyCOGSOG();
            for(int j=0; j<JamSimulation.RealJammerList.size(); j++)
                JamSimulation.RealJammerList.get(j).moveByCOGSOG(0);
            // JamSimulation.getReceiverList().senseNoise(JamSimulation.RealJammerList);
            JamSimulation.getReceiverList().senseNoiseUnknownJammerPower(JamSimulation.RealJammerList);

            if(JamSimulation.getAdditionalParticles().size()>0)
            {
                JamSimulation.getAdditionalParticles().get(0).evalWeightsUnknownJammerPower(JamSimulation.getReceiverList());
                JamSimulation.getAdditionalParticles().get(0).Resample();
                JamSimulation.getAdditionalParticles().get(0).moveJammersbyCOGSOG();
            }

            JamSimulation.getJammerParticles().evalWeightsUnknownJammerPower(JamSimulation.getReceiverList());

            JamSimulation.ComputeBestJammer();

            JammerParticle bestJammer = JamSimulation.getBestJammerEstimation();
            ClearScreen(gc);
            //  JamSimulation.getJammerParticles().PrintwithDistMargins(JamSimulation.getRealJammer(), 0, 100);
            JamSimulation.getJammerParticles().Resample();
            JamSimulation.getJammerParticles().moveJammersbyCOGSOG();
            //if(JamSimulation.getJammerParticles().isConvergence(25.0, bestJammer ))
           // {
              //  JamSimulation.addParticlesSet(bestJammer);
              //  System.out.println("New Jammers!!");
           // }
            //  JamSimulation.getJammerParticles().moveJammers(10);// getJammerParticles().moveJammers(8);
            AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.CLEAR,0.5F);
            gc.setGlobalAlpha(0.5);
            gc.drawImage(image,0, 0, 800, 800);
            gc.setGlobalAlpha(1);
            drawShapes(gc, JamSimulation.getJammerParticles());

          //  if(JamSimulation.getAdditionalParticles().size()>0)
            //    drawShapes(gc, JamSimulation.getAdditionalParticles().get(0));

            for(int j=0; j<JamSimulation.RealJammerList.size(); j++)
                drawRealJammer(gc, JamSimulation.RealJammerList.get(j) , Color.RED, Color.BLACK, true);
            //drawRealJammer(gc, JamSimulation.getBestJammerEstimation(), Color.BLUE, Color.GREEN, true);

            drawClients(gc, JamSimulation.getReceiverList());
            phase++;


            //   JamSimulation.PrintJammerPos();



        });
        topMenu2.getChildren().addAll(ClearButton, randButton, Init, runSimulation,runSimulation2);
        border.setTop(topMenu2);
        border.setCenter(canvas);
        border.setBottom(BotomMenu);


        //root.getChildren().addAll(canvas, randButton, ClearButton);

        drawShapes(gc, JamSimulation.getJammerParticles());
        for(int j=0; j<JamSimulation.RealJammerList.size(); j++)
            drawRealJammer(gc, JamSimulation.RealJammerList.get(j) , Color.RED,Color.BLACK, true);
        drawClients(gc, JamSimulation.getReceiverList());

        window.setScene(new Scene(border));
        window.setTitle("Main WIndows");
        window.show();

    }

    private void drawClients(GraphicsContext gc, ClientList receiverList) {

        gc.setLineWidth(5);
        gc.setStroke(Color.MAROON);
        for(Client tmp : receiverList.getClients())
        {
            if(tmp.getLoc()!=null) {
                double x = tmp.getLoc().getX();
                double y = tmp.getLoc().getY();
                gc.strokeLine(x,y, x, y);
            }
        }

    }

    private void drawRealJammer(GraphicsContext gc, JammerParticle realJammer, Color color, Color border, boolean flag) {
        gc.setLineWidth(5);

        gc.setStroke(color);
        double x = realJammer.getJamLoc().getX();
        double y = realJammer.getJamLoc().getY();
        System.out.println();
        System.out.println("x is "+x+ "y is "+y+" flag is "+flag+ " radoius "+realJammer.getMaximalDistancetoSense()*2);
        gc.strokeLine(x,y, x, y);
        if(flag && realJammer.getJammingAnge()==Math.PI*2) {
            double JammerRadius = realJammer.getMaximalDistancetoSense()*2;
            double JammerSmallRadius = realJammer.getMinmalDistancetoLooseFix()*2;
            gc.setLineWidth(2);
            gc.setStroke(border);
            gc.strokeOval(x-JammerRadius/2, y-JammerRadius/2, JammerRadius, JammerRadius);
            gc.setLineWidth(1);
            gc.strokeOval(x-JammerSmallRadius/2, y-JammerSmallRadius/2, JammerSmallRadius, JammerSmallRadius);
        }
        if(flag && realJammer.getJammingAnge()!= Math.PI*2) {
            double JammerRadius = realJammer.getMaximalDistancetoSense()*2;
            double JammerSmallRadius = realJammer.getMinmalDistancetoLooseFix()*2;
            gc.setLineWidth(2);
            gc.setStroke(border);
            gc.strokeArc(x-JammerRadius/2, y-JammerRadius/2, JammerRadius, JammerRadius, 360-(realJammer.getCOGinDegrees()+realJammer.getJammingAnge()/2),realJammer.getJammingAnge(), ArcType.ROUND);

            //gc.strokeOval(x-JammerRadius/2, y-JammerRadius/2, JammerRadius, JammerRadius);
            gc.setLineWidth(1);
            if(color==Color.RED) {
                System.out.println();
                System.out.println("COG equal " + realJammer.getCOGinDegrees() + " degrees. SOg is "+realJammer.getSOG());
            }
            //gc.strokeOval(x-JammerSmallRadius/2, y-JammerSmallRadius/2, JammerSmallRadius, JammerSmallRadius);
        }

    }

    private void drawShapes(GraphicsContext gc, JamParticles jammerParticles) {

        gc.setLineWidth(4);
        Color[] colors = {Color.YELLOW,Color.GREENYELLOW,Color.GREEN,Color.DARKGREEN,Color.BLUE};

        // gc.setStroke(Color.GREEN);
        double x, y;
        int placeForColorVector;
        Random R1 = new Random();
        for (int i = 0; i < jammerParticles.getJamList().size(); i++)
        {
            placeForColorVector = jammerParticles.getJamList().get(i).getPixelSize();

            System.out.print(jammerParticles.getJamList().get(i).getJamPowe()+"-"+placeForColorVector+"   ");
            gc.setStroke(colors[placeForColorVector]);
            // gc.setLineWidth(jammerParticles.getJamList().get(i).getPixelSize());
            x = jammerParticles.getJamList().get(i).getJamLoc().getX();
            y = jammerParticles.getJamList().get(i).getJamLoc().getY();
            int dx = R1.nextInt(8) - 4;
            int dy = R1.nextInt(8) - 4;
            //   gc.strokeLine(x+dx, y+dy, x+dx, y+dy);
            gc.strokeLine(x, y, x, y);


        }
    }

    private void ClearScreen(GraphicsContext gc) {
        Canvas tmp =  gc.getCanvas();
        double height = tmp.getHeight();
        double width = tmp.getWidth();
        gc.clearRect(0, 0, width, height);


    }



    private void closewindows()
    {
        boolean ans = ConfirmBox.display("Title", "you want to exit?");
        if(ans)
            window.close();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
