package com.hostelhub.view;

import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.FirestoreStudentDao;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class StudentSignUp {

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

        Label logo = new Label("⌂  HOSTEL HUB");

        logo.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 25px;
                -fx-font-weight: bold;
                """);

        BorderPane.setMargin(
                logo,
                new Insets(22, 40, 12, 40)
        );

        root.setTop(logo);

        Label title = new Label(
                "Create Student Account"
        );

        title.setStyle("""
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                """);

        Label subtitle = new Label(
                "Enter your details to register with Hostel Hub"
        );

        subtitle.setStyle("""
                -fx-text-fill: #BDB1D0;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 14px;
                """);

        VBox header = new VBox(
                6,
                title,
                subtitle
        );

        header.setAlignment(Pos.CENTER);

        GridPane form = new GridPane();

        form.setHgap(20);
        form.setVgap(8);

        form.setMaxWidth(760);

        TextField name =
                field("Full Name");

        TextField parentName =
                field("Parent Name");

        DatePicker dob =
                new DatePicker();

        styleDate(dob);

        ComboBox<String> gender =
                combo(
                        "Gender",
                        "Male",
                        "Female",
                        "Other"
                );

        ComboBox<String> course =
                combo(
                        "Course",
                        "BCA",
                        "B.Sc Computer Science",
                        "B.Tech",
                        "B.E",
                        "MCA",
                        "M.Sc",
                        "MBA",
                        "Other"
                );

        ComboBox<String> year =
                combo(
                        "Year",
                        "First Year",
                        "Second Year",
                        "Third Year",
                        "Fourth Year"
                );

        TextField phone =
                field("Phone Number");

        TextField email =
                field("Email Address");

        TextField college =
                field("College Name");

        TextField city =
                field("City");

        ComboBox<String> room =
                combo(
                        "Room Preference",
                        "Single Room",
                        "Double Sharing",
                        "Triple Sharing",
                        "Any Available"
                );
                TextField roomNumber = new TextField();
                roomNumber.setPromptText("Room Number");

        TextArea address =
                new TextArea();

        address.setPromptText(
                "Permanent Address"
        );

        address.setPrefWidth(360);
        address.setPrefHeight(75);

        address.setWrapText(true);

        address.setStyle("""
                -fx-control-inner-background: #181027;
                -fx-text-fill: white;
                -fx-prompt-text-fill: #80758F;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 14px;
                -fx-background-radius: 12px;
                -fx-border-color: #352451;
                -fx-border-radius: 12px;
                """);

        PasswordField password =
                password("Password");

        PasswordField confirmPassword =
                password("Confirm Password");

        addField(
                form,
                name,
                "Full Name",
                0,
                0
        );

        addField(
                form,
                parentName,
                "Parent Name",
                1,
                0
        );

        addField(
                form,
                dob,
                "Date of Birth",
                0,
                1
        );

        addField(
                form,
                gender,
                "Gender",
                1,
                1
        );

        addField(
                form,
                course,
                "Course",
                0,
                2
        );

        addField(
                form,
                year,
                "Year",
                1,
                2
        );

        addField(
                form,
                phone,
                "Phone Number",
                0,
                3
        );

        addField(
                form,
                email,
                "Email Address",
                1,
                3
        );

        addField(
                form,
                college,
                "College Name",
                0,
                4
        );

        addField(
                form,
                city,
                "City",
                1,
                4
        );

        addField(
                form,
                room,
                "Room Preference",
                0,
                5
        );
        addField(
        form,
        roomNumber,
        "Room Number",
        0,
        7
);

        form.add(
                label("Permanent Address"),
                1,
                10
        );

        form.add(
                address,
                1,
                11
        );

        addField(
                form,
                password,
                "Password",
                0,
                6
        );

        addField(
                form,
                confirmPassword,
                "Confirm Password",
                1,
                6
        );

        Label message =
                new Label();

        message.setStyle("""
                -fx-text-fill: #FF8FA3;
                -fx-font-size: 13px;
                """);

        Button create =
                new Button("CREATE ACCOUNT");

        create.setPrefWidth(300);
        create.setPrefHeight(50);

        styleButton(create);

        Hyperlink login =
                new Hyperlink(
                        "Already have an account?  Login"
                );

        login.setStyle("""
                -fx-text-fill: #BFA3FF;
                -fx-font-size: 14px;
                """);

        login.setOnAction(e ->
                StudentLogin.show()
        );
         create.setOnAction(e -> {

    try {

        message.setText("");

        if (name.getText().trim().isEmpty()
                || parentName.getText().trim().isEmpty()
                || dob.getValue() == null
                || gender.getValue() == null
                || course.getValue() == null
                || year.getValue() == null
                || phone.getText().trim().isEmpty()
                || email.getText().trim().isEmpty()
                || college.getText().trim().isEmpty()
                || city.getText().trim().isEmpty()
                || room.getValue() == null
                || address.getText().trim().isEmpty()
                || password.getText().isEmpty()
                || confirmPassword.getText().isEmpty()
                || roomNumber.getText().trim().isEmpty()) {

            message.setText("Please fill all fields.");
            return;
        }

        if (!password.getText().equals(
                confirmPassword.getText())) {

            message.setText("Passwords do not match.");
            return;
        }

        if (password.getText().length() < 6) {

            message.setText(
                    "Password must be at least 6 characters."
            );
            return;
        }

        if (!phone.getText().trim().matches("\\d{10}")) {

            message.setText(
                    "Enter a valid 10-digit phone number."
            );
            return;
        }

        FirebaseAuthController.AuthResult result =
                FirebaseAuthController.signUpAndGetResult(
                        email.getText().trim(),
                        password.getText()
                );

        if (result.isSuccess()) {

            String uid = result.getUid();

            System.out.println("AUTH SUCCESS");
            System.out.println("UID: " + uid);

            FirestoreStudentDao dao =
                    new FirestoreStudentDao();

            boolean saved =
                    dao.saveStudent(
                            uid,
                            name.getText().trim(),
                            parentName.getText().trim(),
                            dob.getValue().toString(),
                            gender.getValue(),
                            course.getValue(),
                            year.getValue(),
                            phone.getText().trim(),
                            email.getText().trim(),
                            college.getText().trim(),
                            city.getText().trim(),
                            room.getValue(),
                            address.getText().trim(),
                            roomNumber.getText().trim()
                    );

            if (saved) {

                message.setText(
                        "Account created successfully!"
                );

                message.setStyle(
                        "-fx-text-fill: #7CFF9B;"
                        + "-fx-font-size: 13px;"
                );

                create.setDisable(true);

            } else {

                message.setText(
                        "Account created, but Firestore save failed."
                );
            }

        } else {

            message.setText(
                    "Signup failed: "
                            + result.getErrorMessage()
            );
        }

    } catch (Exception ex) {

        ex.printStackTrace();

        message.setText(
                "Error: " + ex.getMessage()
        );
    }
});
       
        VBox content =
                new VBox(
                        15,
                        header,
                        form,
                        message,
                        create,
                        login
                );

        content.setAlignment(
                Pos.TOP_CENTER
        );

        content.setPadding(
                new Insets(
                        15,
                        30,
                        30,
                        30
                )
        );

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);

        scroll.setStyle("""
                -fx-background-color: transparent;
                -fx-background: transparent;
                """);

        root.setCenter(scroll);

        Scene scene =
                new Scene(
                        root,
                        1200,
                        750
                );

        Welcome.stage.setScene(scene);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        content
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        fade.play();
    }

    private static void addField(
            GridPane grid,
            javafx.scene.control.Control control,
            String text,
            int column,
            int row
    ) {

        grid.add(
                label(text),
                column,
                row * 2
        );

        grid.add(
                control,
                column,
                row * 2 + 1
        );
    }

    private static Label label(
            String text
    ) {

        Label label =
                new Label(text);

        label.setStyle("""
                -fx-text-fill: #BFA3FF;
                -fx-font-family: 'Segoe UI';
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """);

        return label;
    }

    private static TextField field(
            String prompt
    ) {

        TextField field =
                new TextField();

        field.setPromptText(prompt);

        styleField(field);

        return field;
    }

    private static PasswordField password(
            String prompt
    ) {

        PasswordField field =
                new PasswordField();

        field.setPromptText(prompt);

        styleField(field);

        return field;
    }

    private static void styleField(
            TextField field
    ) {

        field.setPrefWidth(360);
        field.setPrefHeight(44);

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

    private static void styleDate(
            DatePicker date
    ) {

        date.setPrefWidth(360);
        date.setPrefHeight(44);

        date.setStyle("""
                -fx-background-color: #181027;
                -fx-font-size: 14px;
                -fx-background-radius: 12px;
                -fx-border-color: #352451;
                -fx-border-radius: 12px;
                """);
    }

    private static ComboBox<String> combo(
            String prompt,
            String... items
    ) {

        ComboBox<String> combo =
                new ComboBox<>();

        combo.setPromptText(prompt);

        combo.getItems().addAll(items);

        combo.setPrefWidth(360);
        combo.setPrefHeight(44);

        combo.setStyle("""
                -fx-background-color: #181027;
                -fx-text-fill: white;
                -fx-font-family: 'Segoe UI';
                -fx-background-radius: 12px;
                -fx-border-color: #352451;
                -fx-border-radius: 12px;
                """);

        return combo;
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
                -fx-font-size: 14px;
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
}