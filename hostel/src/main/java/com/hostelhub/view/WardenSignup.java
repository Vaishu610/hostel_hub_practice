
package com.hostelhub.view;

import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.FirestoreWardenDao;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

public class WardenSignup {

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

                VBox card = new VBox(14);

                card.setAlignment(Pos.CENTER);
                card.setPrefWidth(450);
                card.setMaxWidth(450);
                card.setPadding(new Insets(35));

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

                Label title = new Label("Warden Sign Up");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 30px;
                                -fx-font-weight: bold;
                                """);

                Label subtitle = new Label(
                                "Create your warden account");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                TextField fullName = new TextField();

                fullName.setPromptText(
                                "Full Name");

                styleField(fullName);

                TextField email = new TextField();

                email.setPromptText(
                                "Email Address");

                styleField(email);

                TextField phone = new TextField();

                phone.setPromptText(
                                "Mobile Number");

                styleField(phone);

                PasswordField password = new PasswordField();

                password.setPromptText(
                                "Password");

                styleField(password);

                PasswordField confirmPassword = new PasswordField();

                confirmPassword.setPromptText(
                                "Confirm Password");

                styleField(confirmPassword);

                Label message = new Label();

                message.setWrapText(true);

                message.setStyle("""
                                -fx-text-fill: #FF8FA3;
                                -fx-font-size: 13px;
                                """);

                Button signup = new Button(
                                "CREATE WARDEN ACCOUNT");

                signup.setPrefWidth(350);
                signup.setPrefHeight(50);

                styleButton(signup);

                Hyperlink login = new Hyperlink(
                                "Already have an account?  Login");

                login.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-size: 14px;
                                -fx-font-family: 'Segoe UI';
                                -fx-cursor: hand;
                                """);

                Hyperlink back = new Hyperlink(
                                "← Back to Role Selection");

                back.setStyle("""
                                -fx-text-fill: #9183A5;
                                -fx-font-size: 13px;
                                -fx-font-family: 'Segoe UI';
                                -fx-cursor: hand;
                                """);

                signup.setOnMouseEntered(e -> scale(signup, 1.04));

                signup.setOnMouseExited(e -> scale(signup, 1));

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

                        String passwordText = password.getText();

                        String confirmText = confirmPassword.getText();

                        if (name.isEmpty()
                                        || emailText.isEmpty()
                                        || phoneText.isEmpty()
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

                        if (!phoneText.matches(
                                        "[0-9]{10}")) {

                                message.setText(
                                                "Mobile number must contain 10 digits.");

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

                        try {

                                FirebaseAuthController.AuthResult result = FirebaseAuthController
                                                .signUpAndGetResult(
                                                                emailText,
                                                                passwordText);

                                if (result.isSuccess()) {

                                        String uid = result.getUid();

                                        FirestoreWardenDao dao = new FirestoreWardenDao();

                                        boolean saved = dao.saveWarden(
                                                        uid,
                                                        name,
                                                        emailText,
                                                        phoneText);

                                        if (saved) {

                                                message.setStyle("""
                                                                -fx-text-fill: #7CFFB2;
                                                                -fx-font-size: 13px;
                                                                """);

                                                message.setText(
                                                                "Warden account created successfully!");

                                                fullName.clear();
                                                email.clear();
                                                phone.clear();
                                                password.clear();
                                                confirmPassword.clear();

                                        } else {

                                                message.setText(
                                                                "Authentication created, but profile could not be saved.");

                                                shake(card);
                                        }

                                } else {

                                        message.setText(
                                                        getFirebaseError(
                                                                        result.getErrorMessage()));

                                        shake(card);
                                }

                        } catch (Exception ex) {

                                ex.printStackTrace();

                                message.setText(
                                                "Signup error: "
                                                                + ex.getMessage());

                                shake(card);

                        } finally {

                                signup.setDisable(false);
                        }
                });

                login.setOnAction(e -> WardenLogin.show());

                back.setOnAction(e -> RoleSelection.show());

                card.getChildren().addAll(
                                logo,
                                title,
                                subtitle,
                                fullName,
                                email,
                                phone,
                                password,
                                confirmPassword,
                                message,
                                signup,
                                login,
                                back);

                center.getChildren().add(card);

                center.setPadding(
                                new Insets(25));

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
                                300,
                                0.85);

                fadeNode(
                                subtitle,
                                450);

                fadeNode(
                                fullName,
                                550);

                fadeNode(
                                email,
                                650);

                fadeNode(
                                phone,
                                750);

                fadeNode(
                                password,
                                850);

                fadeNode(
                                confirmPassword,
                                950);

                fadeNode(
                                message,
                                1050);

                fadeNode(
                                signup,
                                1150);

                fadeNode(
                                login,
                                1250);

                fadeNode(
                                back,
                                1350);
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

        private static String getFirebaseError(
                        String error) {

                if (error == null) {
                        return "Signup failed.";
                }

                if (error.contains("EMAIL_EXISTS")) {
                        return "This email is already registered.";
                }

                if (error.contains("INVALID_EMAIL")) {
                        return "Invalid email address.";
                }

                if (error.contains("WEAK_PASSWORD")) {
                        return "Password is too weak.";
                }

                return "Signup failed: " + error;
        }

        private static void styleField(
                        TextField field) {

                field.setPrefWidth(350);
                field.setPrefHeight(45);

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
                                -fx-font-size: 14px;
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

        private static void scale(
                        Button button,
                        double value) {

                ScaleTransition animation = new ScaleTransition(
                                Duration.millis(150),
                                button);

                animation.setToX(value);
                animation.setToY(value);

                animation.play();
        }

        private static void shake(
                        VBox card) {

                TranslateTransition animation = new TranslateTransition(
                                Duration.millis(60),
                                card);

                animation.setFromX(-8);
                animation.setToX(8);
                animation.setAutoReverse(true);
                animation.setCycleCount(5);

                animation.play();
        }
}
