package com.hostelhub.view;

import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.FirestoreStudentDao;
import com.hostelhub.model.Student;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
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

public class StudentLogin {

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
        card.setPadding(new Insets(40));

        card.setStyle("""
                -fx-background-color: #151022;
                -fx-background-radius: 25px;
                -fx-border-color: #352451;
                -fx-border-radius: 25px;
                """);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(30);
        shadow.setColor(Color.rgb(120, 60, 220, 0.35));

        card.setEffect(shadow);

        Label logo = new Label("⌂  HOSTEL HUB");

        logo.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 25px;
                -fx-font-weight: bold;
                """);

        Label title = new Label("Student Login");

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                """);

        Label subtitle = new Label(
                "Login to access your hostel account"
        );

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
                "Forgot Password?"
        );

        forgot.setStyle("""
                -fx-text-fill: #A064FF;
                -fx-font-size: 13px;
                """);

        Label message = new Label();

        message.setStyle("""
                -fx-text-fill: #FF8FA3;
                -fx-font-size: 13px;
                """);

        Button login = new Button("LOGIN");

        login.setPrefWidth(340);
        login.setPrefHeight(50);

        styleButton(login);

        Hyperlink signup = new Hyperlink(
                "New User?  Sign Up"
        );

        signup.setStyle("""
                -fx-text-fill: #BFA3FF;
                -fx-font-size: 14px;
                -fx-font-family: 'Segoe UI';
                -fx-cursor: hand;
                """);

        Hyperlink back = new Hyperlink(
                "← Back to Role Selection"
        );

        back.setStyle("""
                -fx-text-fill: #9183A5;
                -fx-font-size: 13px;
                """);

        login.setOnMouseEntered(e ->
                scale(login, 1.05)
        );

        login.setOnMouseExited(e ->
                scale(login, 1)
        );
        login.setOnAction(e -> {

            String emailText =
                    email.getText().trim();

            String passwordText =
                    password.getText();

            if (emailText.isEmpty()
                    || passwordText.isEmpty()) {

                message.setText(
                        "Please fill in all fields."
                );

                return;
            }

            try {

                FirebaseAuthController.AuthResult result =
                        FirebaseAuthController.signIn(
                                emailText,
                                passwordText
                        );

                if (result.isSuccess()) {

                    String uid =
                            result.getUid();

                    System.out.println(
                            "LOGIN SUCCESS"
                    );

                    System.out.println(
                            "Logged in UID: " + uid
                    );


                    
                 FirestoreStudentDao dao = new FirestoreStudentDao();
 
                     Student student = dao.getStudentByUid(uid);


                    if (student != null) {

                        System.out.println(
                                "Student Name: "
                                        + student.getFullName()
                        );

                        System.out.println(
                                "Student Email: "
                                        + student.getEmail()
                        );

                        StudentDashboard.show(student);

                    } else {

                        message.setText(
                                "Student profile not found."
                        );
                    }

                } else {

                    message.setText(
                            "Invalid email or password."
                    );
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                message.setText(
                        "Login error: "
                                + ex.getMessage()
                );
            }
        });


        signup.setOnAction(e ->
                StudentSignUp.show()
        );

        back.setOnAction(e ->
                RoleSelection.show()
        );

        forgot.setOnAction(e ->
                message.setText(
                        "Password reset will be available soon."
                )
        );

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
                back
        );

        StackPane center =
                new StackPane(card);

        center.setPadding(
                new Insets(30)
        );

        root.setCenter(center);

        Scene scene =
                new Scene(root, 1200, 700);

        Welcome.stage.setScene(scene);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        card
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();
    }


    private static void styleField(
            TextField field
    ) {

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
            Button button
    ) {

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

        DropShadow glow =
                new DropShadow();

        glow.setRadius(18);

        glow.setColor(
                Color.rgb(
                        145,
                        85,
                        255,
                        0.5
                )
        );

        button.setEffect(glow);
    }


    private static void scale(
            Button button,
            double value
    ) {

        ScaleTransition animation =
                new ScaleTransition(
                        Duration.millis(150),
                        button
                );

        animation.setToX(value);
        animation.setToY(value);

        animation.play();
    }
}