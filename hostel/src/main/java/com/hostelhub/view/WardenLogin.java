package com.hostelhub.view;

import com.hostelhub.controller.FirebaseAuthController;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class WardenLogin {

        private static final Pattern EMAIL_PATTERN = Pattern.compile(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

        public static void show() {

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

                VBox card = new VBox(16);

                card.setAlignment(Pos.CENTER);

                card.setPrefWidth(430);

                card.setMaxWidth(430);

                card.setPadding(
                                new Insets(40));

                card.setStyle("""
                                -fx-background-color: #151022;
                                -fx-background-radius: 25px;
                                -fx-border-color: #352451;
                                -fx-border-radius: 25px;
                                """);

                DropShadow shadow = new DropShadow();

                shadow.setRadius(30);

                shadow.setColor(
                                Color.rgb(
                                                120,
                                                60,
                                                220,
                                                0.35));

                card.setEffect(shadow);

                Label logo = new Label(
                                "⌂  HOSTEL HUB");

                logo.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 25px;
                                -fx-font-weight: bold;
                                """);

                Label icon = new Label(
                                "👨‍💼");

                icon.setStyle("""
                                -fx-font-size: 45px;
                                """);

                Label title = new Label(
                                "Warden Login");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 30px;
                                -fx-font-weight: bold;
                                """);

                Label subtitle = new Label(
                                "Login to manage hostel operations");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                TextField email = new TextField();

                email.setPromptText(
                                "Email Address");

                styleField(email);

                PasswordField password = new PasswordField();

                password.setPromptText(
                                "Password");

                styleField(password);

                Hyperlink forgot = new Hyperlink(
                                "Forgot Password?");

                forgot.setStyle("""
                                -fx-text-fill: #A064FF;
                                -fx-font-size: 13px;
                                -fx-font-family: 'Segoe UI';
                                -fx-cursor: hand;
                                """);

                Label message = new Label();

                message.setWrapText(true);

                message.setMaxWidth(340);

                message.setStyle("""
                                -fx-text-fill: #FF8FA3;
                                -fx-font-size: 13px;
                                -fx-font-family: 'Segoe UI';
                                """);

                Button login = new Button(
                                "LOGIN");

                login.setPrefWidth(340);

                login.setPrefHeight(50);

                styleButton(login);

                Hyperlink back = new Hyperlink(
                                "← Back to Role Selection");

                back.setStyle("""
                                -fx-text-fill: #9183A5;
                                -fx-font-size: 13px;
                                -fx-font-family: 'Segoe UI';
                                -fx-cursor: hand;
                                """);

                login.setOnMouseEntered(e -> scale(login, 1.05));

                login.setOnMouseExited(e -> scale(login, 1));

                login.setOnAction(e -> {

                        String emailText = email.getText().trim();

                        String passwordText = password.getText();

                        message.setText("");

                        if (emailText.isEmpty()) {

                                message.setText(
                                                "Please enter your email address.");

                                email.requestFocus();

                                return;
                        }

                        if (!EMAIL_PATTERN.matcher(
                                        emailText).matches()) {

                                message.setText(
                                                "Please enter a valid email address.");

                                email.requestFocus();

                                return;
                        }

                        if (passwordText.isEmpty()) {

                                message.setText(
                                                "Please enter your password.");

                                password.requestFocus();

                                return;
                        }

                        if (passwordText.length() < 6) {

                                message.setText(
                                                "Password must contain at least 6 characters.");

                                password.requestFocus();

                                return;
                        }

                        login.setDisable(true);

                        login.setText(
                                        "LOGGING IN...");

                        Thread loginThread = new Thread(() -> {

                                try {

                                        FirebaseAuthController.AuthResult result = FirebaseAuthController.signIn(
                                                        emailText,
                                                        passwordText);

                                        javafx.application.Platform.runLater(() -> {

                                                login.setDisable(false);

                                                login.setText(
                                                                "LOGIN");

                                                if (result.isSuccess()) {

                                                        String uid = result.getUid();

                                                        System.out.println(
                                                                        "WARDEN LOGIN SUCCESS");

                                                        System.out.println(
                                                                        "Warden UID: "
                                                                                        + uid);

                                                        try {

                                                                WardenDashboard.show(uid);

                                                        } catch (Exception ex) {

                                                                ex.printStackTrace();

                                                                message.setText(
                                                                                "Warden dashboard could not be opened.");
                                                        }

                                                } else {

                                                        message.setText(
                                                                        getFirebaseErrorMessage(
                                                                                        result.getErrorMessage()));

                                                        password.clear();

                                                        password.requestFocus();
                                                }
                                        });

                                } catch (Exception ex) {

                                        ex.printStackTrace();

                                        javafx.application.Platform.runLater(() -> {

                                                login.setDisable(false);

                                                login.setText(
                                                                "LOGIN");

                                                message.setText(
                                                                "Login error: "
                                                                                + ex.getMessage());
                                        });
                                }

                        });

                        loginThread.setDaemon(true);

                        loginThread.start();
                });

                forgot.setOnAction(e -> {

                        String emailText = email.getText().trim();

                        if (emailText.isEmpty()) {

                                message.setText(
                                                "Enter your email address first.");

                                email.requestFocus();

                                return;
                        }

                        if (!EMAIL_PATTERN.matcher(
                                        emailText).matches()) {

                                message.setText(
                                                "Please enter a valid email address.");

                                email.requestFocus();

                                return;
                        }

                        message.setText(
                                        "Password reset will be available soon.");
                });

                back.setOnAction(e -> RoleSelection.show());

                card.getChildren().addAll(
                                logo,
                                icon,
                                title,
                                subtitle,
                                email,
                                password,
                                forgot,
                                message,
                                login,
                                back);

                StackPane center = new StackPane(card);

                center.setPadding(
                                new Insets(30));

                root.setCenter(center);

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                Welcome.stage.setScene(scene);

                Welcome.stage.setTitle(
                                "Hostel Hub - Warden Login");

                FadeTransition fade = new FadeTransition(
                                Duration.millis(500),
                                card);

                fade.setFromValue(0);

                fade.setToValue(1);

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
                                -fx-padding: 0 15px;
                                """);
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

                glow.setRadius(18);

                glow.setColor(
                                Color.rgb(
                                                145,
                                                85,
                                                255,
                                                0.5));

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

        private static String getFirebaseErrorMessage(
                        String error) {

                if (error == null) {

                        return "Invalid email or password.";
                }

                switch (error) {

                        case "INVALID_LOGIN_CREDENTIALS":
                                return "Invalid email or password.";

                        case "INVALID_PASSWORD":
                                return "Invalid password.";

                        case "EMAIL_NOT_FOUND":
                                return "No account found with this email.";

                        case "USER_DISABLED":
                                return "This account has been disabled.";

                        case "TOO_MANY_ATTEMPTS_TRY_LATER":
                                return "Too many login attempts. Please try again later.";

                        case "INVALID_EMAIL":
                                return "Invalid email address.";

                        case "NETWORK_REQUEST_FAILED":
                                return "Network error. Please check your internet connection.";

                        default:
                                return "Login failed. Please check your credentials.";
                }
        }
}