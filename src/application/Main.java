package application;

import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
//import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.transform.Translate;


/** *
* author Joma-kun
* reference K.Okano, karura
*/

public class Main extends Application {

    @FXML
    Button startButton;
    @FXML
    Button pauseButton;
    @FXML
    CheckBox syncCheck;

    @FXML
    ImageView ashige;
    @FXML
    ImageView kurige;

    /*@FXML
    Circle redCircle;

    @FXML
    Circle blueCircle;*/

    @FXML
    TextField textBox;

    @FXML
    Text text1;
    @FXML
    Text text2;

    @FXML
    Text textFinish1;
    @FXML
    Text textFinish2;

    @FXML
    ImageView win;
    @FXML
    ImageView ken;

    @FXML
    Text away1;
    @FXML
    Text away2;
    @FXML
    Text away3;

    //distance
    int d1 = 0;
    int d2 = 0;

    //initialization variables
    public static int temp1 = 0;
    public static int temp2 = 0;

    //cycle count
    int hp = 27;

    //branch variables
    int pkey = 0; //pause
    int skey = 0; //sync
    int flag = 0; //start
    int goal = 0; 
    int t = 0;    //finish

    public static int sync = 0;
    int dif = 0;

    public static void main(String[] args){
        launch(args);
    }


    @FXML //Push "Pause" -> Pause or Resume
    public void onClickPause(ActionEvent event){
        pkey += 1;
        if (pkey % 2 == 1){
            textBox.setText("レースを停止！");
            System.out.println("PAUSE!");
        } else {
            textBox.setText("レースを再開！");
        }
    }
    

    @FXML //Push "Start" -> Start or nothing
    public void onClickStart(ActionEvent event) {
        if (flag == 0){
            //initialize position
            kurige.getTransforms().add(new Translate (temp1*0.5, 0, 0));
            ashige.getTransforms().add(new Translate (temp2*0.5, 0, 0));
            win.setX(0);
            ken.setX(0);
            away1.setX(0);
            away2.setX(0);
            away3.setX(0);
            text1.setX(0);
            text2.setX(0);
            temp1 = 0;
            temp2 = 0;
            //initialize variables
            pkey = 0;
            goal = 0;
            sync = 0;
            t = 0;

            if (syncCheck.isSelected()){
                sync = 1;
            }

            flag = 1;
            textFinish1.setText("");
            textFinish2.setText(""); 
            System.out.println("START!");
            textBox.setText("レーススタート！");
            action();
        } else { //flag == 1
            System.out.println("Race has already begun");
            textBox.setText("レースは既に始まっています");
        }
    }

    
    //Actual Operation
    public void action(){

        ThreadS1 i = new ThreadS1();

        Timeline timeline = new Timeline(
            new KeyFrame(
                //Operating time(interval)
                Duration.millis(500),

                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event){
                        Loop:
                        while (pkey % 2 == 0){ //pause == false
                            //Create and Start Threads
                            Thread th1 = new Thread(i, "th.1");
                            Thread th2 = new Thread(i, "th.2");
                            th1.start();
                            th2.start();
                            i.run();

                            d1 = ThreadS1.d1;
                            d2 = ThreadS1.d2;

                            //Branch
                            if (d1 >= 50){
                                if (goal != 1){ //When this Thread has not been goal
                                    temp1 += d1*2;
                                    //System.out.println("temp1 = " + temp1);
                                    text1.setText(1000-temp1 + "m");
                                    kurige.getTransforms().add(new Translate (-d1, 0, 0));
                                    break Loop;
                                }
                            } else if (d2 >= 50){
                                if (goal != 2){
                                    temp2 += d2*2;
                                    //System.out.println("temp2 = " + temp2);
                                    text2.setText(1000-temp2 + "m");
                                    ashige.getTransforms().add(new Translate (-d2, 0, 0));
                                    break Loop;
                                }
                            }
                            if (temp1 == 1000 && t == 0){
                                goal = 1;
                                dif = temp1 - temp2;
                                t = 1;
                                textBox.setText("スレッド1がゴール！");
                            } else if (temp2 == 1000 && t == 0){
                                goal = 2;
                                dif = temp2 - temp1;
                                t = 1;
                                textBox.setText("スレッド2がゴール！");
                            }
                            if (temp1 >= 1000 && temp2 >= 1000){
                                flag = 0;
                                textBox.setText("レース終了！");
                                if (goal == 1){
                                    //Show Results and Remove some Displays
                                    textFinish1.setText("Thread1が\n"+dif/100+"馬身差で勝利!");
                                    win.setX(875);
                                    ken.setX(400);
                                    away1.setX(1000);
                                    away2.setX(1000);
                                    away3.setX(1000);
                                    text1.setX(1000);
                                    text2.setX(1000);
                                } else {
                                    textFinish2.setText("Thread2が\n"+dif/100+"馬身差で勝利!");
                                    win.setX(875);
                                    ken.setX(400);
                                    away1.setX(1000);
                                    away2.setX(1000);
                                    away3.setX(1000);
                                    text1.setX(1000);
                                    text2.setX(1000);
                                }
                                break Loop;
                            }
                        }
                    }
                }
            )
        );
        if (temp1 >= 1000 && temp2 >= 1000){ //When both are goal
            timeline.stop();
        }
        if (pkey % 2 == 0){ //When "Start" is pushed
            timeline.setCycleCount(hp);
            timeline.play();
            pkey = 0;
        }else if (pkey % 2 == 1){ //When "Pause" is pushed
            timeline.stop();
        }
    }

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getResource( "Main.fxml" );
        FXMLLoader fxmlLoader = new FXMLLoader( location );
        Pane root = (Pane) fxmlLoader.load();
        primaryStage.setTitle("Thread競馬");
        Scene scene = new Scene( root , 854 , 480); //480p
        primaryStage.setScene( scene );
        primaryStage.show();
    }
}


