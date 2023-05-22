import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.concurrent.Semaphore;

public class DistributorePalline extends Application {

    private static final int NUM_PLAYERS = 5;
    private static final int NUM_BALLS = 15;

    private Semaphore dispenserLock = new Semaphore(1); 
    private Semaphore ballsAvailable = new Semaphore(NUM_BALLS); //semaforo palline disponibili

    private Label ballsAvailableLabel = new Label("Palline disponibili: " + NUM_BALLS);

    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #8bc34a;"); //sfondo verde chiaro
        
        VBox players = new VBox(); //crea le interfacce utente grafiche
        for (int i = 1; i <= NUM_PLAYERS; i++) {
            Player player = new Player(i, this);
            players.getChildren().add(player.getPlayerNode()); // aggiunge i player al vBox
        }
        
        root.setCenter(players);  

        root.setBottom(ballsAvailableLabel);

        Scene scene = new Scene(root, 1000, 750);

        primaryStage.setTitle("Dispenser di palline");
        primaryStage.setScene(scene);
        
          int x=220;
        //creazione dei buchi
        for(int i=0;i<NUM_PLAYERS;i++){
            Circle circle = new Circle(x, 150, 20, Color.BLACK);
            root.getChildren().add(circle);
            circle.setStroke(Color.WHITE);
            circle.setStrokeWidth(2);
            x+=140;
        }
        primaryStage.setResizable(false);
        primaryStage.show();

        
        // creazione delle palline bianche
        if(NUM_PLAYERS>0){
            Circle ball;
            ball = new Circle(220, 700, 15, Color.WHITE);
            root.getChildren().add(ball);
            
            new AnimationTimer() {
                private int yVelocity = -3; // velocità verticale iniziale della pallina
                private int y = (int) ball.getCenterY(); // posizione verticale iniziale della pallina
                public void handle(long now) {
                    y += yVelocity;
                    if (y < 0) {
                        // ripartire dal basso quando si arriva in cima
                        y = 700;
                    } else if (y < 170 ) {
                        // ripartire dal basso quando si raggiunge il cerchio nero
                        y = 700;
                    }
                    ball.setCenterY(y);
                }
            }.start();
            if(NUM_PLAYERS>1){
                Circle ball1;
                ball1 = new Circle(360, 700, 15, Color.WHITE);
                root.getChildren().add(ball1);
                
                new AnimationTimer() {
                private int yVelocity = -2; // velocità verticale iniziale della pallina
                private int y = (int) ball1.getCenterY(); // posizione verticale iniziale della pallina
                public void handle(long now) {
                    y += yVelocity;
                    if (y < 0) {
                        // ripartire dal basso quando si arriva in cima
                        y = 700;
                    } else if (y < 170 ) {
                        // ripartire dal basso quando si raggiunge il cerchio nero
                        y = 700;
                    }
                    ball1.setCenterY(y);
                }
            }.start();
                if(NUM_PLAYERS>2){
                    Circle ball2;
                    ball2 = new Circle(500, 700, 15, Color.WHITE);
                    root.getChildren().add(ball2);
                    
                    new AnimationTimer() {
                        private int yVelocity = -4; // velocità verticale iniziale della pallina
                        private int y = (int) ball2.getCenterY(); // posizione verticale iniziale della pallina
                        public void handle(long now) {
                            y += yVelocity;
                            if (y < 0) {
                                // ripartire dal basso quando si arriva in cima
                                y = 700;
                            } else if (y < 170 ) {
                                // ripartire dal basso quando si raggiunge il cerchio nero
                                y = 700;
                            }
                            ball2.setCenterY(y);
                        }
                    }.start();
                    if(NUM_PLAYERS>3){
                        Circle ball3;
                        ball3 = new Circle(640, 700, 15, Color.WHITE);
                        root.getChildren().add(ball3);
                        
                        new AnimationTimer() {
                            private int yVelocity = -5; // velocità verticale iniziale della pallina
                            private int y = (int) ball3.getCenterY(); // posizione verticale iniziale della pallina
                            public void handle(long now) {
                                y += yVelocity;
                                if (y < 0) {
                                    // ripartire dal basso quando si arriva in cima
                                    y = 700;
                                } else if (y < 170 ) {
                                    // ripartire dal basso quando si raggiunge il cerchio nero
                                    y = 700;
                                }
                                ball3.setCenterY(y);
                            }
                        }.start();

                        if(NUM_PLAYERS>4){
                            Circle ball4;
                            ball4 = new Circle(780, 700, 15, Color.WHITE);
                            root.getChildren().add(ball4);
                            
                            new AnimationTimer() {
                                private int yVelocity = -6; // velocità verticale iniziale della pallina
                                private int y = (int) ball4.getCenterY(); // posizione verticale iniziale della pallina
                                public void handle(long now) {
                                    y += yVelocity;
                                    if (y < 0) {
                                        // ripartire dal basso quando si arriva in cima
                                        y = 700;
                                    } else if (y < 170 ) {
                                        // ripartire dal basso quando si raggiunge il cerchio nero
                                        y = 700;
                                    }
                                    ball4.setCenterY(y);
                                }
                            }.start();
                        }
                    }
                }
            }
        }
            
            
            
            
            
            
        // animazione delle palline bianche
            
            
            
            
            
            
                        
            
        
    }

    public boolean dispenseBalls(int numBalls) {
        boolean result = false;
        try {
            dispenserLock.acquire(); //consente a più thread di accedere alle risorse condivise in modo sincronizzato
            if (ballsAvailable.availablePermits() >= numBalls) {  //ottiene il numero di permessi disponibili per acquisire un oggetto
                ballsAvailable.acquire(numBalls);
                result = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dispenserLock.release();
        }
        updateBallsAvailableLabel();
        return result;
    }

    public void returnBalls(int numBalls) {
        ballsAvailable.release(numBalls);
        updateBallsAvailableLabel();
    }

    private void updateBallsAvailableLabel() {
        Platform.runLater(() -> ballsAvailableLabel.setText("Palline disponibili: " + ballsAvailable.availablePermits()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
