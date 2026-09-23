package com.hostelhub.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Welcome extends Application {

        public static Stage stage;

        private final List<Circle> particles = new ArrayList<>();

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
                                    #06040C,
                                    #10081C,
                                    #1C0D35,
                                    #0A0613
                                );
                                """);

                StackPane center = new StackPane();

                Circle glow1 = createGlow(
                                210,
                                -430,
                                -220,
                                0.13);

                Circle glow2 = createGlow(
                                260,
                                430,
                                220,
                                0.10);

                Circle glow3 = createGlow(
                                130,
                                330,
                                -260,
                                0.08);

                Circle glow4 = createGlow(
                                100,
                                -330,
                                250,
                                0.07);

                center.getChildren().addAll(
                                glow1,
                                glow2,
                                glow3,
                                glow4);

                createParticles(center);

                Label logo = new Label("⌂  HOSTEL HUB");

                logo.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 25px;
                                -fx-font-weight: bold;
                                -fx-letter-spacing: 1px;
                                """);

                DropShadow logoGlow = new DropShadow();

                logoGlow.setRadius(18);
                logoGlow.setSpread(0.18);
                logoGlow.setColor(
                                Color.rgb(150, 85, 255, 0.55));

                logo.setEffect(logoGlow);

                BorderPane.setAlignment(
                                logo,
                                Pos.CENTER_LEFT);

                BorderPane.setMargin(
                                logo,
                                new Insets(
                                                28,
                                                45,
                                                15,
                                                45));

                root.setTop(logo);

                VBox content = new VBox(14);

                content.setAlignment(
                                Pos.CENTER_LEFT);

                content.setMaxWidth(540);

                Label welcome = new Label("WELCOME TO");

                welcome.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 18px;
                                -fx-font-weight: bold;
                                -fx-letter-spacing: 2px;
                                """);

                Label title = new Label("HOSTEL HUB");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 58px;
                                -fx-font-weight: bold;
                                """);

                DropShadow titleGlow = new DropShadow();

                titleGlow.setRadius(28);
                titleGlow.setSpread(0.10);
                titleGlow.setColor(
                                Color.rgb(132, 70, 255, 0.38));

                title.setEffect(titleGlow);

                Label subtitle = new Label(
                                "Smart Hostel Management System");

                subtitle.setStyle("""
                                -fx-text-fill: #D8D0E8;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 19px;
                                """);

                Label tagline = new Label(
                                "Manage  •  Connect  •  Stay Smart");

                tagline.setStyle("""
                                -fx-text-fill: #9582B2;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                """);

                Button getStarted = new Button("GET STARTED   →");

                getStarted.setPrefWidth(225);
                getStarted.setPrefHeight(56);

                getStarted.setStyle("""
                                -fx-background-color: linear-gradient(
                                    to right,
                                    #7134E6,
                                    #A064FF
                                );
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 15px;
                                -fx-font-weight: bold;
                                -fx-background-radius: 30px;
                                -fx-cursor: hand;
                                """);

                DropShadow buttonGlow = new DropShadow();

                buttonGlow.setRadius(24);
                buttonGlow.setSpread(0.20);
                buttonGlow.setColor(
                                Color.rgb(145, 85, 255, 0.60));

                getStarted.setEffect(buttonGlow);

                getStarted.setOnMouseEntered(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(180),
                                        getStarted);

                        scale.setToX(1.07);
                        scale.setToY(1.07);
                        scale.play();

                        buttonGlow.setRadius(34);
                        buttonGlow.setSpread(0.30);
                });

                getStarted.setOnMouseExited(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(180),
                                        getStarted);

                        scale.setToX(1);
                        scale.setToY(1);
                        scale.play();

                        buttonGlow.setRadius(24);
                        buttonGlow.setSpread(0.20);
                });

                getStarted.setOnAction(e -> {

                        FadeTransition transition = new FadeTransition(
                                        Duration.millis(400),
                                        root);

                        transition.setFromValue(1);
                        transition.setToValue(0);

                        transition.setOnFinished(
                                        event -> RoleSelection.show());

                        transition.play();
                });

                content.getChildren().addAll(
                                welcome,
                                title,
                                subtitle,
                                tagline,
                                getStarted);

                StackPane leftSide = new StackPane(content);

                StackPane.setAlignment(
                                content,
                                Pos.CENTER_LEFT);

                StackPane.setMargin(
                                content,
                                new Insets(
                                                0,
                                                20,
                                                0,
                                                80));

                ImageView image1 = createImage(
                                "/images/hostel1.jpeg",
                                300,
                                420);

                ImageView image2 = createImage(
                                "/images/hostel2.jpeg",
                                245,
                                330);

                StackPane imageContainer = new StackPane();

                StackPane.setAlignment(
                                image1,
                                Pos.CENTER);

                StackPane.setAlignment(
                                image2,
                                Pos.BOTTOM_RIGHT);

                StackPane.setMargin(
                                image1,
                                new Insets(
                                                0,
                                                120,
                                                0,
                                                0));

                StackPane.setMargin(
                                image2,
                                new Insets(
                                                0,
                                                10,
                                                20,
                                                0));

                imageContainer.getChildren().addAll(
                                image1,
                                image2);

                StackPane rightSide = new StackPane(
                                imageContainer);

                HBox mainContent = new HBox();

                mainContent.setAlignment(
                                Pos.CENTER);

                HBox.setHgrow(
                                leftSide,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                rightSide,
                                Priority.ALWAYS);

                mainContent.getChildren().addAll(
                                leftSide,
                                rightSide);

                center.getChildren().add(
                                mainContent);

                root.setCenter(center);

                Label footer = new Label(
                                "© 2026 Hostel Hub  •  Smart • Simple • Secure");

                footer.setStyle("""
                                -fx-text-fill: #817494;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 12px;
                                """);

                BorderPane.setAlignment(
                                footer,
                                Pos.CENTER);

                BorderPane.setMargin(
                                footer,
                                new Insets(
                                                10,
                                                0,
                                                20,
                                                0));

                root.setBottom(footer);

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                stage.setScene(scene);

                prepareNodes(
                                logo,
                                welcome,
                                title,
                                subtitle,
                                tagline,
                                getStarted,
                                image1,
                                image2);

                stage.show();

                startAnimations(
                                logo,
                                welcome,
                                title,
                                subtitle,
                                tagline,
                                getStarted,
                                image1,
                                image2);
        }

        private void prepareNodes(
                        Label logo,
                        Label welcome,
                        Label title,
                        Label subtitle,
                        Label tagline,
                        Button button,
                        ImageView image1,
                        ImageView image2) {

                logo.setOpacity(0);
                logo.setScaleX(0.85);
                logo.setScaleY(0.85);

                welcome.setOpacity(0);
                title.setOpacity(0);
                subtitle.setOpacity(0);
                tagline.setOpacity(0);
                button.setOpacity(0);

                title.setScaleX(0.88);
                title.setScaleY(0.88);

                image1.setOpacity(0);
                image2.setOpacity(0);

                image1.setScaleX(0.85);
                image1.setScaleY(0.85);

                image2.setScaleX(0.85);
                image2.setScaleY(0.85);
        }

        private void startAnimations(
                        Label logo,
                        Label welcome,
                        Label title,
                        Label subtitle,
                        Label tagline,
                        Button button,
                        ImageView image1,
                        ImageView image2) {

                FadeTransition logoFade = fade(
                                logo,
                                0,
                                1,
                                700,
                                0);

                ScaleTransition logoScale = scale(
                                logo,
                                0.85,
                                1,
                                700,
                                0);

                new ParallelTransition(
                                logoFade,
                                logoScale).play();

                playFade(
                                welcome,
                                250);

                FadeTransition titleFade = fade(
                                title,
                                0,
                                1,
                                800,
                                400);

                ScaleTransition titleScale = scale(
                                title,
                                0.88,
                                1,
                                800,
                                400);

                new ParallelTransition(
                                titleFade,
                                titleScale).play();

                playFade(
                                subtitle,
                                650);

                playFade(
                                tagline,
                                800);

                playFade(
                                button,
                                950);

                animateImage(
                                image1,
                                550,
                                -8);

                animateImage(
                                image2,
                                750,
                                9);
        }

        private void animateImage(
                        ImageView image,
                        double delay,
                        double movement) {

                FadeTransition fade = fade(
                                image,
                                0,
                                1,
                                800,
                                delay);

                ScaleTransition scale = scale(
                                image,
                                0.85,
                                1,
                                800,
                                delay);

                TranslateTransition enter = new TranslateTransition(
                                Duration.millis(800),
                                image);

                enter.setFromY(25);
                enter.setToY(0);
                enter.setDelay(
                                Duration.millis(delay));

                new ParallelTransition(
                                fade,
                                scale,
                                enter).play();

                TranslateTransition floating = new TranslateTransition(
                                Duration.seconds(3.8),
                                image);

                floating.setFromY(0);
                floating.setToY(movement);
                floating.setAutoReverse(true);
                floating.setCycleCount(
                                TranslateTransition.INDEFINITE);

                floating.setDelay(
                                Duration.millis(
                                                delay + 850));

                floating.play();
        }

        private void createParticles(
                        StackPane pane) {

                Random random = new Random();

                for (int i = 0; i < 22; i++) {

                        Circle particle = new Circle(
                                        1.5 +
                                                        random.nextDouble() * 2.5);

                        particle.setFill(
                                        Color.rgb(
                                                        180,
                                                        130,
                                                        255,
                                                        0.20 +
                                                                        random.nextDouble() * 0.35));

                        particle.setTranslateX(
                                        -600 +
                                                        random.nextDouble() * 1200);

                        particle.setTranslateY(
                                        -350 +
                                                        random.nextDouble() * 700);

                        pane.getChildren().add(
                                        particle);

                        particles.add(
                                        particle);

                        animateParticle(
                                        particle,
                                        2.5 +
                                                        random.nextDouble() * 4);
                }
        }

        private void animateParticle(
                        Circle particle,
                        double seconds) {

                TranslateTransition movement = new TranslateTransition(
                                Duration.seconds(seconds),
                                particle);

                movement.setByY(-25);
                movement.setAutoReverse(true);
                movement.setCycleCount(
                                TranslateTransition.INDEFINITE);

                movement.play();

                FadeTransition fade = new FadeTransition(
                                Duration.seconds(seconds),
                                particle);

                fade.setFromValue(0.25);
                fade.setToValue(0.80);
                fade.setAutoReverse(true);
                fade.setCycleCount(
                                FadeTransition.INDEFINITE);

                fade.play();
        }

        private FadeTransition fade(
                        javafx.scene.Node node,
                        double from,
                        double to,
                        double duration,
                        double delay) {

                FadeTransition transition = new FadeTransition(
                                Duration.millis(duration),
                                node);

                transition.setFromValue(from);
                transition.setToValue(to);
                transition.setDelay(
                                Duration.millis(delay));

                return transition;
        }

        private ScaleTransition scale(
                        javafx.scene.Node node,
                        double from,
                        double to,
                        double duration,
                        double delay) {

                ScaleTransition transition = new ScaleTransition(
                                Duration.millis(duration),
                                node);

                transition.setFromX(from);
                transition.setFromY(from);
                transition.setToX(to);
                transition.setToY(to);
                transition.setDelay(
                                Duration.millis(delay));

                return transition;
        }

        private void playFade(
                        javafx.scene.Node node,
                        double delay) {

                fade(
                                node,
                                0,
                                1,
                                650,
                                delay).play();
        }

        private Circle createGlow(
                        double radius,
                        double x,
                        double y,
                        double opacity) {

                Circle glow = new Circle(radius);

                glow.setFill(
                                Color.rgb(
                                                125,
                                                55,
                                                235,
                                                opacity));

                glow.setTranslateX(x);
                glow.setTranslateY(y);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(90);
                shadow.setSpread(0.05);
                shadow.setColor(
                                Color.rgb(
                                                145,
                                                75,
                                                255,
                                                opacity));

                glow.setEffect(shadow);

                ScaleTransition pulse = new ScaleTransition(
                                Duration.seconds(5),
                                glow);

                pulse.setFromX(0.90);
                pulse.setFromY(0.90);
                pulse.setToX(1.10);
                pulse.setToY(1.10);
                pulse.setAutoReverse(true);
                pulse.setCycleCount(
                                ScaleTransition.INDEFINITE);

                pulse.play();

                return glow;
        }

        private ImageView createImage(
                        String path,
                        double width,
                        double height) {

                ImageView imageView = new ImageView();

                var resource = getClass().getResource(path);

                if (resource != null) {

                        Image image = new Image(
                                        resource.toExternalForm());

                        imageView.setImage(image);

                } else {

                        System.out.println(
                                        "Image not found: " + path);
                }

                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(false);

                Rectangle clip = new Rectangle(
                                width,
                                height);

                clip.setArcWidth(35);
                clip.setArcHeight(35);

                imageView.setClip(clip);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(28);
                shadow.setSpread(0.18);
                shadow.setColor(
                                Color.rgb(
                                                125,
                                                55,
                                                235,
                                                0.55));

                imageView.setEffect(shadow);

                return imageView;
        }

        public static void main(String[] args) {
                launch(args);
        }
}