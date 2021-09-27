/*
Name: Clinton Schultz
Prof: Dr. Rasib Khan
Assign: Homework 4 - Shape Generator
Date: 10/27/19
*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ModernArt extends Application {

    int xMin = 0;
    int xMax = 599;
    int yMin = 0;
    int yMax = 299;
    int wMin = 10;
    int hMin = 10;
    double sliderMin = 10;
    double sliderMax = 100;

    @Override
    public void start( Stage primaryStage ) {

        Pane artworkPane = new Pane();

        // Drawing Bar
        Pane drawingBarPane = new HBox();
        drawingBarPane.setPadding(new Insets(0, 50, 5, 50));

        // Animation Bar
        Pane animationPane = new HBox();
        animationPane.setPadding(new Insets(0, 50, 5, 50));


        // Combo Box and initial settings
        ComboBox<String> cbo = new ComboBox<>();
        cbo.getItems().addAll("Circle", "Rectangle");
        cbo.setValue("Circle");

        // Text fields and initial settings
        TextField count = new TextField("50");
        count.setPrefWidth(55);
        TextField size = new TextField("100");
        size.setPrefWidth(55);

        // Buttons and initial settings
        Button draw = new Button("Draw");
        Button clear = new Button("Clear");
        Button playPause = new Button("Play");

        // Slider
        Slider slider = new Slider(sliderMin, sliderMax, sliderMin);
        double rate = slider.getValue();

        // Timeline for the Animation
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.rateProperty().setValue(rate);

        // Event handler
        EventHandler<ActionEvent> fullArt = e -> {
            createModernArt(artworkPane, cbo, count, size, animation, false);
        };

        // Handlers
        cbo.setOnAction(fullArt);
        count.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                createModernArt(artworkPane, cbo, count, size, animation, false);
            }
        });
        size.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                createModernArt(artworkPane, cbo, count, size, animation, false);
            }
        });
        draw.setOnAction(fullArt);
        clear.setOnAction(e -> {
            artworkPane.getChildren().clear();
            animation.getKeyFrames().clear();
        });
        slider.setOnDragDetected(e -> {
            animation.setRate(slider.getValue());
        });

        // Pause and resume animation
        playPause.setOnAction(e -> {
            if (animation.getStatus() == Animation.Status.STOPPED || animation.getStatus() == Animation.Status.PAUSED) {
                playPause.setText("Pause");
                createModernArt(artworkPane, cbo, count, size, animation, true);
                animation.play();
            } else {
                playPause.setText("Play");
                animation.pause();
            }
        });

        // Adding nodes to Panes
        drawingBarPane.getChildren().addAll(cbo, new Label("Object Count:"), count, new Label("Max Size:"), size, draw, clear);
        animationPane.getChildren().addAll(new Label("Animation"), playPause, new Label("Speed"), slider);

        // Control Bar
        VBox controlBarPane = new VBox();
        controlBarPane.getChildren().addAll(drawingBarPane, animationPane);

        // Border Pane
        BorderPane pane = new BorderPane();
        pane.setCenter(artworkPane);
        pane.setBottom(controlBarPane);

        Scene scene = new Scene(pane,600.00, 300.00);
        primaryStage.setTitle("Modern Art");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Circle createCircle(int radius){
        Circle circle = new Circle();
        double randRadius = wMin + (Math.random() * radius);
        circle.setRadius(randRadius);
        circle.setCenterX((xMin + ((xMax - xMin) * Math.random())));
        circle.setCenterY((yMin + ((yMax - yMin) * Math.random())));
        circle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        circle.setOpacity(Math.random());
        return circle;
    }

    public Rectangle createRectangle(int size){
        double width = (wMin + ((size - wMin) * Math.random()));
        double height = (hMin + ((size - hMin) * Math.random()));
        double randX = (xMin + ((xMax - xMin) * Math.random()) - width );
        double randY = (yMin + ((yMax - yMin) * Math.random()) - height );

        Rectangle r = new Rectangle(randX, randY, width, height);
        r.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        r.setOpacity(Math.random());
        return r;
    }

    public void createModernArt(Pane artworkPane, ComboBox<String> cbo, TextField count, TextField size, Timeline tl, boolean b){
        String shape = cbo.getValue();

        // Count of objects to add
        int num = Integer.parseInt(count.getText());

        // Dimensions of the object, either max radius or max length
        int dim = Integer.parseInt(size.getText());

        if (shape == "Circle") {
            artworkPane.getChildren().clear();
            for (int i = 1; i <= num; i++) {
                if (b){
                    KeyFrame item = new KeyFrame( Duration.seconds(i), e -> {
                        Circle circle = createCircle(dim);
                        artworkPane.getChildren().add(circle);
                    });
                    tl.getKeyFrames().add(item);
                    tl.play();
                } else {
                    Circle circle = createCircle(dim);
                    artworkPane.getChildren().add(circle);
                }
            }
        } else {
            artworkPane.getChildren().clear();
            for (int i = 0; i < num; i++) {
                if(b) {
                    KeyFrame item = new KeyFrame(Duration.seconds(i), e -> {
                        Rectangle rectangle = createRectangle(dim);
                        artworkPane.getChildren().add(rectangle);
                    });
                    tl.getKeyFrames().add(item);
                    tl.play();
                } 
                else {
                    Rectangle rectangle = createRectangle(dim);
                    artworkPane.getChildren().add(rectangle);
                }
            }
        }
    }
}
