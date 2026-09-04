package com.hostelhub.view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Welcome extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        stage.setTitle("Hostel Hub");
        stage.setMinWidth(1100);
        stage.setMinHeight(650);

        BorderPane root = new BorderPane();

        root.setStyle("""
                -fx-background-color: linear-gradient(
                    to bottom right,
                    #08050F,
                    #120A20,
                    #21103D,
                    #10091D
                );
                """);

        Label logo = new Label("⌂  HOSTEL HUB");

        logo.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 25px;
                -fx-font-weight: bold;
                """);

        BorderPane.setAlignment(logo, Pos.CENTER_LEFT);
        BorderPane.setMargin(
                logo,
                new Insets(28, 45, 15, 45)
        );

        root.setTop(logo);

        VBox content = new VBox(16);

        content.setAlignment(Pos.CENTER_LEFT);
        content.setMaxWidth(520);

        Label welcome = new Label("WELCOME TO");

        welcome.setStyle("""
                -fx-text-fill: #BFA3FF;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        Label title = new Label("HOSTEL HUB");

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 55px;
                -fx-font-weight: bold;
                """);

        Label subtitle = new Label(
                "Smart Hostel Management System"
        );

        subtitle.setStyle("""
                -fx-text-fill: #D5CDE5;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 19px;
                """);

        Button getStarted = new Button("GET STARTED  →");

        getStarted.setPrefWidth(220);
        getStarted.setPrefHeight(55);

        getStarted.setStyle("""
                -fx-background-color: linear-gradient(
                    to right,
                    #7437E8,
                    #A064FF
                );
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 15px;
                -fx-font-weight: bold;
                -fx-background-radius: 30px;
                -fx-cursor: hand;
                """);

        DropShadow glow = new DropShadow();

        glow.setRadius(22);
        glow.setSpread(0.18);
        glow.setColor(
                Color.rgb(145, 85, 255, 0.55)
        );

        getStarted.setEffect(glow);

        getStarted.setOnMouseEntered(e -> {

            ScaleTransition animation =
                    new ScaleTransition(
                            Duration.millis(160),
                            getStarted
                    );

            animation.setToX(1.06);
            animation.setToY(1.06);
            animation.play();
        });

        getStarted.setOnMouseExited(e -> {

            ScaleTransition animation =
                    new ScaleTransition(
                            Duration.millis(160),
                            getStarted
                    );

            animation.setToX(1);
            animation.setToY(1);
            animation.play();
        });

        getStarted.setOnAction(e -> {
            RoleSelection.show();
        });

        content.getChildren().addAll(
                welcome,
                title,
                subtitle,
                getStarted
        );

        StackPane leftSide = new StackPane(content);

        StackPane.setAlignment(
                content,
                Pos.CENTER_LEFT
        );

        StackPane.setMargin(
                content,
                new Insets(0, 20, 0, 80)
        );

        ImageView image1 = createImage(
                "/images/hostel1.jpeg",
                300,
                420
        );

        ImageView image2 = createImage(
                "/images/hostel2.jpeg",
                245,
                330
        );

        StackPane imageContainer = new StackPane();

        StackPane.setAlignment(
                image1,
                Pos.CENTER
        );

        StackPane.setAlignment(
                image2,
                Pos.BOTTOM_RIGHT
        );

        StackPane.setMargin(
                image1,
                new Insets(0, 120, 0, 0)
        );

        StackPane.setMargin(
                image2,
                new Insets(0, 10, 20, 0)
        );

        imageContainer.getChildren().addAll(
                image1,
                image2
        );

        StackPane rightSide =
                new StackPane(imageContainer);

        HBox mainContent = new HBox();

        mainContent.setAlignment(Pos.CENTER);

        HBox.setHgrow(
                leftSide,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                rightSide,
                Priority.ALWAYS
        );

        mainContent.getChildren().addAll(
                leftSide,
                rightSide
        );

        root.setCenter(mainContent);

        Label footer = new Label(
                "© 2026 Hostel Hub  •  Smart • Simple • Secure"
        );

        footer.setStyle("""
                -fx-text-fill: #817494;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 12px;
                """);

        BorderPane.setAlignment(
                footer,
                Pos.CENTER
        );

        BorderPane.setMargin(
                footer,
                new Insets(10, 0, 20, 0)
        );

        root.setBottom(footer);

        Scene scene =
                new Scene(root, 1200, 700);

        stage.setScene(scene);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(900),
                        mainContent
                );

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        stage.show();
    }

    private ImageView createImage(
            String path,
            double width,
            double height
    ) {

        ImageView imageView =
                new ImageView();

        var resource =
                getClass().getResource(path);

        if (resource != null) {

            Image image =
                    new Image(
                            resource.toExternalForm()
                    );

            imageView.setImage(image);

        } else {

            System.out.println(
                    "Image not found: " + path
            );
        }

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(false);

        Rectangle clip =
                new Rectangle(width, height);

        clip.setArcWidth(35);
        clip.setArcHeight(35);

        imageView.setClip(clip);

        DropShadow shadow =
                new DropShadow();

        shadow.setRadius(25);
        shadow.setSpread(0.15);
        shadow.setColor(
                Color.rgb(120, 55, 230, 0.45)
        );

        imageView.setEffect(shadow);

        return imageView;
    }
}