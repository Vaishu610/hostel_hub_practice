package com.hostelhub.view;

import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.FirestoreStudentDao;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
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
                                new Insets(22, 40, 12, 40));

                root.setTop(logo);

                Label title = new Label("Create Student Account");

                title.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 30px;
                                -fx-font-weight: bold;
                                """);

                Label subtitle = new Label(
                                "Enter your details to register with Hostel Hub");

                subtitle.setStyle("""
                                -fx-text-fill: #BDB1D0;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                """);

                VBox header = new VBox(
                                6,
                                title,
                                subtitle);

                header.setAlignment(Pos.CENTER);

                GridPane form = new GridPane();

                form.setHgap(22);
                form.setVgap(7);
                form.setMaxWidth(760);

                TextField name = field("Full Name");
                TextField parentName = field("Parent Name");

                DatePicker dob = new DatePicker();
                styleDate(dob);

                ComboBox<String> gender = combo(
                                "Gender",
                                "Male",
                                "Female",
                                "Other");

                ComboBox<String> course = combo(
                                "Course",
                                "BCA",
                                "B.Sc Computer Science",
                                "B.Tech",
                                "B.E",
                                "MCA",
                                "M.Sc",
                                "MBA",
                                "Other");

                ComboBox<String> year = combo(
                                "Year",
                                "First Year",
                                "Second Year",
                                "Third Year",
                                "Fourth Year");

                TextField phone = field("Phone Number");
                TextField email = field("Email Address");

                PasswordField password = password("Password");

                TextField college = field("College Name");
                TextField city = field("City");

                ComboBox<String> room = combo(
                                "Room Preference",
                                "Single Room",
                                "Double Sharing",
                                "Triple Sharing",
                                "Any Available");

                TextField roomNumber = field("Room Number");

                ComboBox<String> messPlan = combo(
                                "Mess Plan",
                                "With Mess",
                                "Without Mess");

                TextArea address = new TextArea();

                address.setPromptText(
                                "Permanent Address");

                address.setPrefWidth(360);
                address.setPrefHeight(80);
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
                                -fx-padding: 10px 15px;
                                """);

                addField(
                                form,
                                name,
                                "Full Name",
                                0,
                                0);

                addField(
                                form,
                                parentName,
                                "Parent Name",
                                1,
                                0);

                addField(
                                form,
                                dob,
                                "Date of Birth",
                                0,
                                1);

                addField(
                                form,
                                gender,
                                "Gender",
                                1,
                                1);

                addField(
                                form,
                                course,
                                "Course",
                                0,
                                2);

                addField(
                                form,
                                year,
                                "Year",
                                1,
                                2);

                addField(
                                form,
                                phone,
                                "Phone Number",
                                0,
                                3);

                addField(
                                form,
                                email,
                                "Email Address",
                                1,
                                3);

                addField(
                                form,
                                college,
                                "College Name",
                                0,
                                4);

                addField(
                                form,
                                password,
                                "Password",
                                1,
                                4);

                addField(
                                form,
                                city,
                                "City",
                                0,
                                5);

                addField(
                                form,
                                room,
                                "Room Preference",
                                1,
                                5);

                addField(
                                form,
                                roomNumber,
                                "Room Number",
                                0,
                                6);

                addField(
                                form,
                                messPlan,
                                "Mess Plan",
                                1,
                                6);

                form.add(
                                label("Permanent Address"),
                                0,
                                14);

                form.add(
                                address,
                                0,
                                15,
                                2,
                                1);

                Label feeInfo = new Label();

                feeInfo.setWrapText(true);
                feeInfo.setMaxWidth(700);

                feeInfo.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                -fx-font-weight: bold;
                                """);

                messPlan.setOnAction(e -> {

                        if ("With Mess".equals(
                                        messPlan.getValue())) {

                                feeInfo.setText(
                                                "Hostel Fee: ₹75,000  |  Mess Included");

                        } else if ("Without Mess".equals(
                                        messPlan.getValue())) {

                                feeInfo.setText(
                                                "Hostel Fee: ₹41,000  |  Mess Not Included");

                        } else {

                                feeInfo.setText("");
                        }
                });

                Label message = new Label();

                message.setWrapText(true);
                message.setMaxWidth(700);

                message.setStyle("""
                                -fx-text-fill: #FF8FA3;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 13px;
                                """);

                Button create = new Button("CREATE ACCOUNT");

                create.setPrefWidth(300);
                create.setPrefHeight(50);

                styleButton(create);

                Hyperlink login = new Hyperlink(
                                "Already have an account?  Login");

                login.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-border-color: transparent;
                                """);

                login.setOnAction(e -> {

                        FadeTransition fade = new FadeTransition(
                                        Duration.millis(350),
                                        root);

                        fade.setFromValue(1);
                        fade.setToValue(0);

                        fade.setOnFinished(event -> StudentLogin.show());

                        fade.play();
                });

                create.setOnAction(e -> {

                        try {

                                message.setStyle("""
                                                -fx-text-fill: #FF8FA3;
                                                -fx-font-family: 'Segoe UI';
                                                -fx-font-size: 13px;
                                                """);

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
                                                || password.getText().isEmpty()
                                                || city.getText().trim().isEmpty()
                                                || room.getValue() == null
                                                || roomNumber.getText().trim().isEmpty()
                                                || messPlan.getValue() == null
                                                || address.getText().trim().isEmpty()) {

                                        message.setText(
                                                        "Please fill all fields.");

                                        shake(message);
                                        return;
                                }

                                if (password.getText().length() < 6) {

                                        message.setText(
                                                        "Password must be at least 6 characters.");

                                        shake(message);
                                        return;
                                }

                                if (!phone.getText().trim().matches(
                                                "\\d{10}")) {

                                        message.setText(
                                                        "Enter a valid 10-digit phone number.");

                                        shake(message);
                                        return;
                                }

                                if (!email.getText().trim().matches(
                                                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                                        message.setText(
                                                        "Enter a valid email address.");

                                        shake(message);
                                        return;
                                }

                                create.setDisable(true);
                                create.setText(
                                                "CREATING ACCOUNT...");

                                FirebaseAuthController.AuthResult result = FirebaseAuthController
                                                .signUpAndGetResult(
                                                                email.getText().trim(),
                                                                password.getText());

                                if (result.isSuccess()) {

                                        String uid = result.getUid();

                                        System.out.println(
                                                        "AUTH SUCCESS");

                                        System.out.println(
                                                        "UID: " + uid);

                                        FirestoreStudentDao dao = new FirestoreStudentDao();

                                        boolean saved = dao.saveStudent(
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
                                                        roomNumber.getText().trim(),
                                                        messPlan.getValue());

                                        if (saved) {

                                                message.setStyle("""
                                                                -fx-text-fill: #7CFF9B;
                                                                -fx-font-family: 'Segoe UI';
                                                                -fx-font-size: 13px;
                                                                -fx-font-weight: bold;
                                                                """);

                                                message.setText(
                                                                "Account created successfully!");

                                                create.setText(
                                                                "ACCOUNT CREATED");

                                                create.setDisable(true);

                                                ScaleTransition scale = new ScaleTransition(
                                                                Duration.millis(250),
                                                                create);

                                                scale.setFromX(1);
                                                scale.setFromY(1);
                                                scale.setToX(1.05);
                                                scale.setToY(1.05);

                                                scale.setAutoReverse(true);
                                                scale.setCycleCount(2);
                                                scale.play();

                                        } else {

                                                create.setDisable(false);

                                                create.setText(
                                                                "CREATE ACCOUNT");

                                                message.setText(
                                                                "Account created, but Firestore save failed.");

                                                shake(message);
                                        }

                                } else {

                                        create.setDisable(false);

                                        create.setText(
                                                        "CREATE ACCOUNT");

                                        message.setText(
                                                        "Signup failed: "
                                                                        + result.getErrorMessage());

                                        shake(message);
                                }

                        } catch (Exception ex) {

                                ex.printStackTrace();

                                create.setDisable(false);

                                create.setText(
                                                "CREATE ACCOUNT");

                                message.setText(
                                                "Error: " + ex.getMessage());

                                shake(message);
                        }
                });

                VBox content = new VBox(
                                15,
                                header,
                                form,
                                feeInfo,
                                message,
                                create,
                                login);

                content.setAlignment(
                                Pos.TOP_CENTER);

                content.setPadding(
                                new Insets(
                                                15,
                                                30,
                                                35,
                                                30));

                ScrollPane scroll = new ScrollPane(content);

                scroll.setFitToWidth(true);

                scroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scroll.setStyle("""
                                -fx-background-color: transparent;
                                -fx-background: transparent;
                                -fx-border-color: transparent;
                                """);

                root.setCenter(scroll);

                addBackgroundGlow(root);

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                Welcome.stage.setScene(scene);

                startAnimation(
                                root,
                                logo,
                                header,
                                form,
                                message,
                                create,
                                login);
        }

        private static void startAnimation(
                        BorderPane root,
                        Label logo,
                        VBox header,
                        GridPane form,
                        Label message,
                        Button create,
                        Hyperlink login) {

                root.setOpacity(0);

                logo.setOpacity(0);
                header.setOpacity(0);
                form.setOpacity(0);
                message.setOpacity(0);
                create.setOpacity(0);
                login.setOpacity(0);

                logo.setScaleX(0.85);
                logo.setScaleY(0.85);

                header.setTranslateY(25);
                form.setTranslateY(35);
                message.setTranslateY(25);
                create.setTranslateY(25);
                login.setTranslateY(20);

                FadeTransition rootFade = new FadeTransition(
                                Duration.millis(500),
                                root);

                rootFade.setFromValue(0);
                rootFade.setToValue(1);

                FadeTransition logoFade = new FadeTransition(
                                Duration.millis(500),
                                logo);

                logoFade.setFromValue(0);
                logoFade.setToValue(1);

                ScaleTransition logoScale = new ScaleTransition(
                                Duration.millis(500),
                                logo);

                logoScale.setFromX(0.85);
                logoScale.setFromY(0.85);
                logoScale.setToX(1);
                logoScale.setToY(1);

                ParallelTransition logoAnimation = new ParallelTransition(
                                logoFade,
                                logoScale);

                FadeTransition headerFade = new FadeTransition(
                                Duration.millis(550),
                                header);

                headerFade.setFromValue(0);
                headerFade.setToValue(1);

                TranslateTransition headerMove = new TranslateTransition(
                                Duration.millis(550),
                                header);

                headerMove.setFromY(25);
                headerMove.setToY(0);

                ParallelTransition headerAnimation = new ParallelTransition(
                                headerFade,
                                headerMove);

                FadeTransition formFade = new FadeTransition(
                                Duration.millis(650),
                                form);

                formFade.setFromValue(0);
                formFade.setToValue(1);

                TranslateTransition formMove = new TranslateTransition(
                                Duration.millis(650),
                                form);

                formMove.setFromY(35);
                formMove.setToY(0);

                ParallelTransition formAnimation = new ParallelTransition(
                                formFade,
                                formMove);

                FadeTransition messageFade = new FadeTransition(
                                Duration.millis(450),
                                message);

                messageFade.setFromValue(0);
                messageFade.setToValue(1);

                TranslateTransition messageMove = new TranslateTransition(
                                Duration.millis(450),
                                message);

                messageMove.setFromY(25);
                messageMove.setToY(0);

                ParallelTransition messageAnimation = new ParallelTransition(
                                messageFade,
                                messageMove);

                FadeTransition buttonFade = new FadeTransition(
                                Duration.millis(500),
                                create);

                buttonFade.setFromValue(0);
                buttonFade.setToValue(1);

                TranslateTransition buttonMove = new TranslateTransition(
                                Duration.millis(500),
                                create);

                buttonMove.setFromY(25);
                buttonMove.setToY(0);

                ParallelTransition buttonAnimation = new ParallelTransition(
                                buttonFade,
                                buttonMove);

                FadeTransition loginFade = new FadeTransition(
                                Duration.millis(450),
                                login);

                loginFade.setFromValue(0);
                loginFade.setToValue(1);

                TranslateTransition loginMove = new TranslateTransition(
                                Duration.millis(450),
                                login);

                loginMove.setFromY(20);
                loginMove.setToY(0);

                ParallelTransition loginAnimation = new ParallelTransition(
                                loginFade,
                                loginMove);

                rootFade.play();

                logoAnimation.setDelay(
                                Duration.millis(150));

                headerAnimation.setDelay(
                                Duration.millis(350));

                formAnimation.setDelay(
                                Duration.millis(500));

                messageAnimation.setDelay(
                                Duration.millis(750));

                buttonAnimation.setDelay(
                                Duration.millis(850));

                loginAnimation.setDelay(
                                Duration.millis(950));

                logoAnimation.play();
                headerAnimation.play();
                formAnimation.play();
                messageAnimation.play();
                buttonAnimation.play();
                loginAnimation.play();

                create.setOnMouseEntered(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(150),
                                        create);

                        scale.setToX(1.04);
                        scale.setToY(1.04);
                        scale.play();

                        create.setEffect(
                                        createGlow(0.8));
                });

                create.setOnMouseExited(e -> {

                        ScaleTransition scale = new ScaleTransition(
                                        Duration.millis(150),
                                        create);

                        scale.setToX(1);
                        scale.setToY(1);
                        scale.play();

                        create.setEffect(
                                        createGlow(0.5));
                });

                login.setOnMouseEntered(e -> login.setStyle("""
                                -fx-text-fill: white;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-border-color: transparent;
                                -fx-cursor: hand;
                                """));

                login.setOnMouseExited(e -> login.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-border-color: transparent;
                                """));
        }

        private static void addBackgroundGlow(
                        BorderPane root) {

                Circle glow1 = new Circle(180);

                glow1.setFill(
                                Color.rgb(
                                                119,
                                                52,
                                                220,
                                                0.08));

                Circle glow2 = new Circle(130);

                glow2.setFill(
                                Color.rgb(
                                                168,
                                                87,
                                                255,
                                                0.07));

                glow1.setMouseTransparent(true);
                glow2.setMouseTransparent(true);

                root.getChildren().addAll(
                                glow1,
                                glow2);

                glow1.setTranslateX(450);
                glow1.setTranslateY(300);

                glow2.setTranslateX(-400);
                glow2.setTranslateY(-200);

                ScaleTransition pulse1 = new ScaleTransition(
                                Duration.seconds(4),
                                glow1);

                pulse1.setFromX(0.9);
                pulse1.setFromY(0.9);
                pulse1.setToX(1.15);
                pulse1.setToY(1.15);
                pulse1.setAutoReverse(true);
                pulse1.setCycleCount(
                                ScaleTransition.INDEFINITE);

                ScaleTransition pulse2 = new ScaleTransition(
                                Duration.seconds(5),
                                glow2);

                pulse2.setFromX(1);
                pulse2.setFromY(1);
                pulse2.setToX(1.2);
                pulse2.setToY(1.2);
                pulse2.setAutoReverse(true);
                pulse2.setCycleCount(
                                ScaleTransition.INDEFINITE);

                pulse1.play();
                pulse2.play();
        }

        private static void addField(
                        GridPane grid,
                        javafx.scene.control.Control control,
                        String text,
                        int column,
                        int row) {

                grid.add(
                                label(text),
                                column,
                                row * 2);

                grid.add(
                                control,
                                column,
                                row * 2 + 1);
        }

        private static Label label(
                        String text) {

                Label label = new Label(text);

                label.setStyle("""
                                -fx-text-fill: #BFA3FF;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 12px;
                                -fx-font-weight: bold;
                                """);

                return label;
        }

        private static TextField field(
                        String prompt) {

                TextField field = new TextField();

                field.setPromptText(prompt);

                styleField(field);

                return field;
        }

        private static PasswordField password(
                        String prompt) {

                PasswordField field = new PasswordField();

                field.setPromptText(prompt);

                styleField(field);

                return field;
        }

        private static void styleField(
                        TextField field) {

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
                                -fx-border-width: 1px;
                                -fx-border-radius: 12px;
                                -fx-padding: 0 15px;
                                """);

                field.focusedProperty().addListener(
                                (obs, oldValue, focused) -> {

                                        if (focused) {

                                                field.setStyle("""
                                                                -fx-background-color: #1C1230;
                                                                -fx-text-fill: white;
                                                                -fx-prompt-text-fill: #80758F;
                                                                -fx-font-family: 'Segoe UI';
                                                                -fx-font-size: 14px;
                                                                -fx-background-radius: 12px;
                                                                -fx-border-color: #8E4DFF;
                                                                -fx-border-width: 1.5px;
                                                                -fx-border-radius: 12px;
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
                                                                -fx-border-width: 1px;
                                                                -fx-border-radius: 12px;
                                                                -fx-padding: 0 15px;
                                                                """);
                                        }
                                });
        }

        private static void styleDate(
                        DatePicker date) {

                date.setPrefWidth(360);
                date.setPrefHeight(44);

                date.setStyle("""
                                -fx-background-color: #181027;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-background-radius: 12px;
                                -fx-border-color: #352451;
                                -fx-border-width: 1px;
                                -fx-border-radius: 12px;
                                """);

                date.getEditor().setStyle("""
                                -fx-background-color: #181027;
                                -fx-text-fill: white;
                                -fx-prompt-text-fill: #80758F;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-background-radius: 12px;
                                -fx-border-color: transparent;
                                -fx-padding: 0 12px;
                                """);
        }

        private static ComboBox<String> combo(
                        String prompt,
                        String... items) {

                ComboBox<String> combo = new ComboBox<>();

                combo.setPromptText(prompt);

                combo.getItems().addAll(items);

                combo.setPrefWidth(360);
                combo.setPrefHeight(44);

                combo.setEditable(false);

                combo.setStyle("""
                                -fx-background-color: #181027;
                                -fx-font-family: 'Segoe UI';
                                -fx-font-size: 14px;
                                -fx-background-radius: 12px;
                                -fx-border-color: #352451;
                                -fx-border-width: 1px;
                                -fx-border-radius: 12px;
                                -fx-cursor: hand;
                                """);

                combo.setCellFactory(listView -> new javafx.scene.control.ListCell<String>() {

                        @Override
                        protected void updateItem(
                                        String item,
                                        boolean empty) {

                                super.updateItem(
                                                item,
                                                empty);

                                if (empty || item == null) {

                                        setText(null);

                                } else {

                                        setText(item);
                                        setTextFill(Color.WHITE);

                                        setFont(
                                                        Font.font(
                                                                        "Segoe UI",
                                                                        14));

                                        setStyle("""
                                                        -fx-background-color: #181027;
                                                        -fx-text-fill: white;
                                                        -fx-padding: 10px 15px;
                                                        """);
                                }
                        }
                });

                combo.setButtonCell(
                                new javafx.scene.control.ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty || item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(item);
                                                        setTextFill(Color.WHITE);

                                                        setFont(
                                                                        Font.font(
                                                                                        "Segoe UI",
                                                                                        14));

                                                        setStyle("""
                                                                        -fx-background-color: #181027;
                                                                        -fx-text-fill: white;
                                                                        -fx-padding: 0 15px;
                                                                        """);
                                                }
                                        }
                                });

                return combo;
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

                button.setEffect(
                                createGlow(0.5));
        }

        private static DropShadow createGlow(
                        double opacity) {

                DropShadow glow = new DropShadow();

                glow.setRadius(18);
                glow.setSpread(0.15);

                glow.setColor(
                                Color.rgb(
                                                145,
                                                85,
                                                255,
                                                opacity));

                return glow;
        }

        private static void shake(
                        javafx.scene.Node node) {

                TranslateTransition shake = new TranslateTransition(
                                Duration.millis(70),
                                node);

                shake.setFromX(0);
                shake.setByX(10);
                shake.setAutoReverse(true);
                shake.setCycleCount(4);
                shake.play();
        }
}
