
package com.hostelhub.view;

import com.google.cloud.firestore.FieldValue;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ParentSignup {

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

                VBox card = new VBox(12);

                card.setAlignment(Pos.CENTER);
                card.setPrefWidth(450);
                card.setMaxWidth(450);
                card.setPadding(new Insets(32, 40, 30, 40));

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

                Label title = new Label("Parent Sign Up");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 30px;
                                -fx-font-weight: bold;
                                """);

                Label subtitle = new Label(
                                "Create your parent hostel account");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                TextField fullName = new TextField();

                fullName.setPromptText("Full Name");

                styleField(fullName);

                TextField email = new TextField();

                email.setPromptText("Email Address");

                styleField(email);

                TextField phone = new TextField();

                phone.setPromptText("Phone Number");

                styleField(phone);

                TextField studentName = new TextField();

                studentName.setPromptText("Student Name");

                styleField(studentName);

                TextField studentId = new TextField();

                studentId.setPromptText("Student ID");

                styleField(studentId);

                PasswordField password = new PasswordField();

                password.setPromptText("Password");

                styleField(password);

                PasswordField confirmPassword = new PasswordField();

                confirmPassword.setPromptText("Confirm Password");

                styleField(confirmPassword);

                Label message = new Label();

                message.setStyle("""
                                -fx-text-fill: #FF8FA3;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                """);

                Button signup = new Button("CREATE ACCOUNT");

                signup.setPrefWidth(340);
                signup.setPrefHeight(50);

                styleButton(signup);

                Hyperlink login = new Hyperlink(
                                "Already have an account?  Login");

                login.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-cursor: hand;
                                """);

                Hyperlink back = new Hyperlink(
                                "← Back to Parent Login");

                back.setStyle("""
                                -fx-text-fill: #9183A5;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                -fx-cursor: hand;
                                """);

                signup.setOnMouseEntered(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(150),
                                        signup);

                        animation.setToX(1.06);
                        animation.setToY(1.06);
                        animation.play();
                });

                signup.setOnMouseExited(e -> {

                        ScaleTransition animation = new ScaleTransition(
                                        Duration.millis(150),
                                        signup);

                        animation.setToX(1);
                        animation.setToY(1);
                        animation.play();
                });

                login.setOnMouseEntered(e -> login.setTextFill(
                                Color.web("#D3BFFF")));

                login.setOnMouseExited(e -> login.setTextFill(
                                Color.web("#BFA3FF")));

                back.setOnMouseEntered(e -> back.setTextFill(
                                Color.web("#BFA3FF")));

                back.setOnMouseExited(e -> back.setTextFill(
                                Color.web("#9183A5")));

                signup.setOnAction(e -> {

                        String name = fullName.getText().trim();

                        String emailText = email.getText().trim();

                        String phoneText = phone.getText().trim();

                        String studentText = studentName.getText().trim();

                        String studentIdText = studentId.getText().trim();

                        String passwordText = password.getText();

                        String confirmText = confirmPassword.getText();

                        if (name.isEmpty()
                                        || emailText.isEmpty()
                                        || phoneText.isEmpty()
                                        || studentText.isEmpty()
                                        || studentIdText.isEmpty()
                                        || passwordText.isEmpty()
                                        || confirmText.isEmpty()) {

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

                        if (!phoneText.matches("\\d{10}")) {

                                message.setText(
                                                "Please enter a valid 10 digit phone number.");

                                shake(card);

                                return;
                        }

                        if (passwordText.length() < 6) {

                                message.setText(
                                                "Password must contain at least 6 characters.");

                                shake(card);

                                return;
                        }

                        if (!passwordText.equals(confirmText)) {

                                message.setText(
                                                "Passwords do not match.");

                                shake(card);

                                return;
                        }

                        signup.setDisable(true);

                        message.setStyle("""
                                        -fx-text-fill: #BFA3FF;
                                        -fx-font-size: 13px;
                                        """);

                        message.setText(
                                        "Creating parent account...");

                        Thread thread = new Thread(() -> {

                                try {

                                        FirebaseAuthController.AuthResult result = FirebaseAuthController
                                                        .signUpAndGetResult(
                                                                        emailText,
                                                                        passwordText);

                                        if (!result.isSuccess()) {

                                                Platform.runLater(() -> {

                                                        signup.setDisable(false);

                                                        message.setStyle("""
                                                                        -fx-text-fill: #FF8FA3;
                                                                        -fx-font-size: 13px;
                                                                        """);

                                                        message.setText(
                                                                        getFirebaseError(
                                                                                        result.getErrorMessage()));

                                                        shake(card);
                                                });

                                                return;
                                        }

                                        String uid = result.getUid();

                                        Map<String, Object> parentData = new HashMap<>();

                                        parentData.put(
                                                        "uid",
                                                        uid);

                                        parentData.put(
                                                        "role",
                                                        "PARENT");

                                        parentData.put(
                                                        "fullName",
                                                        name);

                                        parentData.put(
                                                        "email",
                                                        emailText);

                                        parentData.put(
                                                        "phone",
                                                        phoneText);

                                        parentData.put(
                                                        "studentName",
                                                        studentText);

                                        parentData.put(
                                                        "studentId",
                                                        studentIdText);

                                        parentData.put(
                                                        "createdAt",
                                                        FieldValue.serverTimestamp());

                                        FirebaseConfig
                                                        .getFireStore()
                                                        .collection("parents")
                                                        .document(uid)
                                                        .set(parentData)
                                                        .get();

                                        Platform.runLater(() -> {

                                                signup.setDisable(false);

                                                message.setStyle("""
                                                                -fx-text-fill: #BFA3FF;
                                                                -fx-font-size: 13px;
                                                                """);

                                                message.setText(
                                                                "Account created successfully!");

                                                System.out.println(
                                                                "PARENT SIGNUP SUCCESS");

                                                System.out.println(
                                                                "Parent UID: " + uid);

                                                new Thread(() -> {

                                                        try {

                                                                Thread.sleep(700);

                                                        } catch (InterruptedException ignored) {
                                                        }

                                                        Platform.runLater(
                                                                        ParentLogin::show);

                                                }).start();
                                        });

                                } catch (Exception ex) {

                                        ex.printStackTrace();

                                        Platform.runLater(() -> {

                                                signup.setDisable(false);

                                                message.setStyle("""
                                                                -fx-text-fill: #FF8FA3;
                                                                -fx-font-size: 13px;
                                                                """);

                                                message.setText(
                                                                "Signup error: "
                                                                                + ex.getMessage());

                                                shake(card);
                                        });
                                }

                        });

                        thread.setDaemon(true);
                        thread.start();
                });

                login.setOnAction(e -> ParentLogin.show());

                back.setOnAction(e -> {

                        FadeTransition fade = new FadeTransition(
                                        Duration.millis(350),
                                        root);

                        fade.setFromValue(1);
                        fade.setToValue(0);

                        fade.setOnFinished(event -> ParentLogin.show());

                        fade.play();
                });

                card.getChildren().addAll(
                                logo,
                                title,
                                subtitle,
                                fullName,
                                email,
                                phone,
                                studentName,
                                studentId,
                                password,
                                confirmPassword,
                                message,
                                signup,
                                login,
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
                                fullName,
                                email,
                                phone,
                                studentName,
                                studentId,
                                password,
                                confirmPassword,
                                message,
                                signup,
                                login,
                                back);
        }

        private static void startAnimation(
                        VBox card,
                        Label logo,
                        Label title,
                        Label subtitle,
                        TextField fullName,
                        TextField email,
                        TextField phone,
                        TextField studentName,
                        TextField studentId,
                        PasswordField password,
                        PasswordField confirmPassword,
                        Label message,
                        Button signup,
                        Hyperlink login,
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
                fullName.setOpacity(0);
                email.setOpacity(0);
                phone.setOpacity(0);
                studentName.setOpacity(0);
                studentId.setOpacity(0);
                password.setOpacity(0);
                confirmPassword.setOpacity(0);
                message.setOpacity(0);
                signup.setOpacity(0);
                login.setOpacity(0);
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
                                150,
                                0.80);

                animateNode(
                                title,
                                250,
                                0.85);

                fadeNode(subtitle, 350);
                fadeNode(fullName, 450);
                fadeNode(email, 550);
                fadeNode(phone, 650);
                fadeNode(studentName, 750);
                fadeNode(studentId, 850);
                fadeNode(password, 950);
                fadeNode(confirmPassword, 1050);
                fadeNode(message, 1100);
                fadeNode(signup, 1150);
                fadeNode(login, 1250);
                fadeNode(back, 1350);
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
                field.setPrefHeight(43);

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

        private static String getFirebaseError(
                        String error) {

                if (error == null) {
                        return "Signup failed.";
                }

                if (error.contains("EMAIL_EXISTS")) {
                        return "This email is already registered.";
                }

                if (error.contains("WEAK_PASSWORD")) {
                        return "Password is too weak.";
                }

                if (error.contains("INVALID_EMAIL")) {
                        return "Invalid email address.";
                }

                if (error.contains("NETWORK_REQUEST_FAILED")) {
                        return "Network error. Check your internet connection.";
                }

                return error;
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
