
package com.hostelhub.view;

import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.controller.FirebaseAuthController;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class ParentLogin {

        public static void show() {

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

                addBackgroundGlow(center);
                addParticles(center);

                VBox card = new VBox(16);

                card.setAlignment(Pos.CENTER);
                card.setPrefWidth(430);
                card.setMaxWidth(430);
                card.setPadding(new Insets(40));

                card.setStyle("""
                                -fx-background-color: linear-gradient(
                                    to bottom right,
                                    #171125,
                                    #120D1E
                                );
                                -fx-background-radius: 28px;
                                -fx-border-color: #392456;
                                -fx-border-width: 1px;
                                -fx-border-radius: 28px;
                                """);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(30);
                shadow.setSpread(0.15);
                shadow.setColor(
                                Color.rgb(120, 60, 220, 0.35));

                card.setEffect(shadow);

                Label logo = new Label("⌂  HOSTEL HUB");

                logo.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 25px;
                                -fx-font-weight: bold;
                                """);

                Label title = new Label("Parent Login");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 30px;
                                -fx-font-weight: bold;
                                """);

                Label subtitle = new Label(
                                "Login to access your parent account");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                TextField email = new TextField();

                email.setPromptText("Email Address");

                styleField(email);

                PasswordField password = new PasswordField();

                password.setPromptText("Password");

                styleField(password);

                Hyperlink forgot = new Hyperlink(
                                "Forgot Password?");

                forgot.setStyle("""
                                -fx-text-fill: #A064FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                -fx-cursor: hand;
                                """);

                Label message = new Label();

                message.setStyle("""
                                -fx-text-fill: #FF8FA3;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                """);

                Button login = new Button("LOGIN");

                login.setPrefWidth(340);
                login.setPrefHeight(50);

                styleButton(login);

                Hyperlink signup = new Hyperlink(
                                "New User?  Sign Up");

                signup.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-cursor: hand;
                                """);

                Hyperlink back = new Hyperlink(
                                "← Back to Role Selection");

                back.setStyle("""
                                -fx-text-fill: #9183A5;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                -fx-cursor: hand;
                                """);

                login.setOnMouseEntered(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(150),
                                        login);

                        animation.setToX(1.06);
                        animation.setToY(1.06);
                        animation.play();
                });

                login.setOnMouseExited(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(150),
                                        login);

                        animation.setToX(1);
                        animation.setToY(1);
                        animation.play();
                });

                signup.setOnMouseEntered(e -> signup.setTextFill(
                                Color.web("#D3BFFF")));

                signup.setOnMouseExited(e -> signup.setTextFill(
                                Color.web("#BFA3FF")));

                back.setOnMouseEntered(e -> back.setTextFill(
                                Color.web("#BFA3FF")));

                back.setOnMouseExited(e -> back.setTextFill(
                                Color.web("#9183A5")));

                login.setOnAction(e -> {

                        String emailText = email.getText().trim();

                        String passwordText = password.getText();

                        if (emailText.isEmpty()
                                        || passwordText.isEmpty()) {

                                message.setText(
                                                "Please fill in all fields.");

                                shake(card);

                                return;
                        }

                        if (!emailText.matches(
                                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                                message.setText(
                                                "Please enter a valid email address.");

                                shake(card);

                                return;
                        }

                        login.setDisable(true);

                        try {

                                FirebaseAuthController.AuthResult result = FirebaseAuthController.signIn(
                                                emailText,
                                                passwordText);

                                if (result.isSuccess()) {

                                        String uid = result.getUid();

                                        System.out.println(
                                                        "PARENT LOGIN SUCCESS");

                                        System.out.println(
                                                        "Logged in UID: " + uid);

                                        message.setStyle("""
                                                        -fx-text-fill: #BFA3FF;
                                                        -fx-font-size: 13px;
                                                        """);

                                        message.setText(
                                                        "Loading parent profile...");

                                        Thread thread = new Thread(() -> {

                                                try {

                                                        boolean parentExists = FirebaseConfig
                                                                        .getFireStore()
                                                                        .collection("parents")
                                                                        .document(uid)
                                                                        .get()
                                                                        .get()
                                                                        .exists();

                                                        Platform.runLater(() -> {

                                                                login.setDisable(false);

                                                                if (parentExists) {

                                                                        message.setText(
                                                                                        "Login successful!");

                                                                        System.out.println(
                                                                                        "Parent profile found.");

                                                                        ParentDashboard.show(uid);

                                                                } else {

                                                                        message.setStyle("""
                                                                                        -fx-text-fill: #FF8FA3;
                                                                                        -fx-font-size: 13px;
                                                                                        """);

                                                                        message.setText(
                                                                                        "Parent profile not found.");

                                                                        shake(card);
                                                                }
                                                        });

                                                } catch (Exception ex) {

                                                        ex.printStackTrace();

                                                        Platform.runLater(() -> {

                                                                login.setDisable(false);

                                                                message.setStyle("""
                                                                                -fx-text-fill: #FF8FA3;
                                                                                -fx-font-size: 13px;
                                                                                """);

                                                                message.setText(
                                                                                "Unable to load parent profile.");

                                                                shake(card);
                                                        });
                                                }

                                        });

                                        thread.setDaemon(true);
                                        thread.start();

                                } else {

                                        login.setDisable(false);

                                        message.setStyle("""
                                                        -fx-text-fill: #FF8FA3;
                                                        -fx-font-size: 13px;
                                                        """);

                                        message.setText(
                                                        "Invalid email or password.");

                                        shake(card);
                                }

                        } catch (Exception ex) {

                                login.setDisable(false);

                                ex.printStackTrace();

                                message.setStyle("""
                                                -fx-text-fill: #FF8FA3;
                                                -fx-font-size: 13px;
                                                """);

                                message.setText(
                                                "Login error: "
                                                                + ex.getMessage());

                                shake(card);
                        }
                });

                signup.setOnAction(e -> ParentSignup.show());

                back.setOnAction(e -> {

                        FadeTransition fade = new FadeTransition(
                                        Duration.millis(350),
                                        root);

                        fade.setFromValue(1);
                        fade.setToValue(0);

                        fade.setOnFinished(event -> RoleSelection.show());

                        fade.play();
                });

                forgot.setOnAction(e -> {

                        message.setStyle("""
                                        -fx-text-fill: #BFA3FF;
                                        -fx-font-size: 13px;
                                        """);

                        message.setText(
                                        "Password reset will be available soon.");
                });

                card.getChildren().addAll(
                                logo,
                                title,
                                subtitle,
                                email,
                                password,
                                forgot,
                                message,
                                login,
                                signup,
                                back);

                center.getChildren().add(card);

                root.setCenter(center);

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                Welcome.stage.setScene(scene);
                Welcome.stage.show();

                startAnimation(
                                card,
                                logo,
                                title,
                                subtitle,
                                email,
                                password,
                                forgot,
                                message,
                                login,
                                signup,
                                back);
        }

        private static void startAnimation(
                        VBox card,
                        Label logo,
                        Label title,
                        Label subtitle,
                        TextField email,
                        PasswordField password,
                        Hyperlink forgot,
                        Label message,
                        Button login,
                        Hyperlink signup,
                        Hyperlink back) {

                card.setOpacity(0);
                card.setTranslateY(45);
                card.setScaleX(0.90);
                card.setScaleY(0.90);

                logo.setOpacity(0);
                logo.setScaleX(0.80);
                logo.setScaleY(0.80);

                title.setOpacity(0);
                title.setScaleX(0.85);
                title.setScaleY(0.85);

                subtitle.setOpacity(0);
                email.setOpacity(0);
                password.setOpacity(0);
                forgot.setOpacity(0);
                message.setOpacity(0);
                login.setOpacity(0);
                signup.setOpacity(0);
                back.setOpacity(0);

                FadeTransition cardFade = new FadeTransition(
                                Duration.millis(700),
                                card);

                cardFade.setFromValue(0);
                cardFade.setToValue(1);

                TranslateTransition cardMove = new TranslateTransition(
                                Duration.millis(700),
                                card);

                cardMove.setFromY(45);
                cardMove.setToY(0);

                ScaleTransition cardScale = new ScaleTransition(
                                Duration.millis(700),
                                card);

                cardScale.setFromX(0.90);
                cardScale.setFromY(0.90);
                cardScale.setToX(1);
                cardScale.setToY(1);

                new ParallelTransition(
                                cardFade,
                                cardMove,
                                cardScale).play();

                animateNode(
                                logo,
                                200,
                                0.80);

                animateNode(
                                title,
                                350,
                                0.85);

                fadeNode(subtitle, 500);
                fadeNode(email, 650);
                fadeNode(password, 750);
                fadeNode(forgot, 850);
                fadeNode(message, 900);
                fadeNode(login, 950);
                fadeNode(signup, 1050);
                fadeNode(back, 1150);
        }

        private static void animateNode(
                        javafx.scene.Node node,
                        double delay,
                        double scaleFrom) {

                FadeTransition fade = new FadeTransition(
                                Duration.millis(500),
                                node);

                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(
                                Duration.millis(delay));

                ScaleTransition scale = new ScaleTransition(
                                Duration.millis(500),
                                node);

                scale.setFromX(scaleFrom);
                scale.setFromY(scaleFrom);
                scale.setToX(1);
                scale.setToY(1);
                scale.setDelay(
                                Duration.millis(delay));

                new ParallelTransition(
                                fade,
                                scale).play();
        }

        private static void fadeNode(
                        javafx.scene.Node node,
                        double delay) {

                FadeTransition fade = new FadeTransition(
                                Duration.millis(450),
                                node);

                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(
                                Duration.millis(delay));

                fade.play();
        }

        private static void styleField(
                        TextField field) {

                field.setPrefWidth(340);
                field.setPrefHeight(48);

                field.setStyle("""
                                -fx-background-color: #181027;
                                -fx-text-fill: white;
                                -fx-prompt-text-fill: #80758F;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-background-radius: 12px;
                                -fx-border-color: #352451;
                                -fx-border-radius: 12px;
                                -fx-border-width: 1px;
                                -fx-padding: 0 15px;
                                """);

                field.focusedProperty().addListener(
                                (obs, oldValue, focused) -> {

                                        if (focused) {

                                                field.setStyle("""
                                                                -fx-background-color: #1B122D;
                                                                -fx-text-fill: white;
                                                                -fx-prompt-text-fill: #9589A5;
                                                                -fx-font-family: 'Segoe UI';
                                                                -fx-font-size: 14px;
                                                                -fx-background-radius: 12px;
                                                                -fx-border-color: #7437E8;
                                                                -fx-border-radius: 12px;
                                                                -fx-border-width: 1.5px;
                                                                -fx-padding: 0 15px;
                                                                """);

                                        } else {

                                                field.setStyle("""
                                                                -fx-background-color: #181027;
                                                                -fx-text-fill: white;
                                                                -fx-prompt-text-fill: #80758F;
                                                                -fx-font-family: 'Segoe UI';
                                                                -fx-font-size: 14px;
                                                                -fx-background-radius: 12px;
                                                                -fx-border-color: #352451;
                                                                -fx-border-radius: 12px;
                                                                -fx-border-width: 1px;
                                                                -fx-padding: 0 15px;
                                                                """);
                                        }
                                });
        }

        private static void styleButton(
                        Button button) {

                button.setStyle("""
                                -fx-background-color: linear-gradient(
                                    to right,
                                    #7437E8,
                                    #A064FF
                                );
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 15px;
                                -fx-font-weight: bold;
                                -fx-background-radius: 25px;
                                -fx-cursor: hand;
                                """);

                DropShadow glow = new DropShadow();

                glow.setRadius(20);
                glow.setSpread(0.18);
                glow.setColor(
                                Color.rgb(
                                                145,
                                                85,
                                                255,
                                                0.55));

                button.setEffect(glow);
        }

        private static void shake(
                        VBox card) {

                TranslateTransition shake = new TranslateTransition(
                                Duration.millis(60),
                                card);

                shake.setFromX(-8);
                shake.setToX(8);
                shake.setAutoReverse(true);
                shake.setCycleCount(5);

                shake.play();
        }

        private static void addBackgroundGlow(
                        StackPane pane) {

                Circle glow1 = new Circle(220);

                glow1.setFill(
                                Color.rgb(
                                                125,
                                                55,
                                                235,
                                                0.12));

                glow1.setTranslateX(-450);
                glow1.setTranslateY(-230);

                Circle glow2 = new Circle(250);

                glow2.setFill(
                                Color.rgb(
                                                125,
                                                55,
                                                235,
                                                0.09));

                glow2.setTranslateX(450);
                glow2.setTranslateY(220);

                ScaleTransition pulse1 = new ScaleTransition(
                                Duration.seconds(5),
                                glow1);

                pulse1.setFromX(0.9);
                pulse1.setFromY(0.9);
                pulse1.setToX(1.1);
                pulse1.setToY(1.1);
                pulse1.setAutoReverse(true);
                pulse1.setCycleCount(
                                ScaleTransition.INDEFINITE);

                pulse1.play();

                ScaleTransition pulse2 = new ScaleTransition(
                                Duration.seconds(6),
                                glow2);

                pulse2.setFromX(0.9);
                pulse2.setFromY(0.9);
                pulse2.setToX(1.1);
                pulse2.setToY(1.1);
                pulse2.setAutoReverse(true);
                pulse2.setCycleCount(
                                ScaleTransition.INDEFINITE);

                pulse2.play();

                pane.getChildren().addAll(
                                glow1,
                                glow2);
        }

        private static void addParticles(
                        StackPane pane) {

                Random random = new Random();

                for (int i = 0; i < 20; i++) {

                        Circle particle = new Circle(
                                        1.5 +
                                                        random.nextDouble() * 2);

                        particle.setFill(
                                        Color.rgb(
                                                        180,
                                                        130,
                                                        255,
                                                        0.25 +
                                                                        random.nextDouble() * 0.30));

                        particle.setTranslateX(
                                        -600 +
                                                        random.nextDouble() * 1200);

                        particle.setTranslateY(
                                        -350 +
                                                        random.nextDouble() * 700);

                        pane.getChildren().add(
                                        particle);

                        TranslateTransition move = new TranslateTransition(
                                        Duration.seconds(
                                                        3 +
                                                                        random.nextDouble() * 3),
                                        particle);

                        move.setByY(-25);
                        move.setAutoReverse(true);
                        move.setCycleCount(
                                        TranslateTransition.INDEFINITE);

                        move.play();

                        FadeTransition fade = new FadeTransition(
                                        Duration.seconds(
                                                        3 +
                                                                        random.nextDouble() * 3),
                                        particle);

                        fade.setFromValue(0.20);
                        fade.setToValue(0.75);
                        fade.setAutoReverse(true);
                        fade.setCycleCount(
                                        FadeTransition.INDEFINITE);

                        fade.play();
                }
        }
}
