package com.hostelhub.view;

import com.hostelhub.model.Student;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class StudentDashboard {

    public static void show(Student student) {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #090612;"
        );

        root.setLeft(
                createSidebar(root, student)
        );

        root.setCenter(
                createDashboardContent(student)
        );

        Scene scene =
                new Scene(root, 1250, 750);

        Welcome.stage.setScene(scene);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        root
                );

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private static VBox createSidebar(
            BorderPane root,
            Student student
    ) {

        VBox sidebar =
                new VBox(10);

        sidebar.setPrefWidth(240);

        sidebar.setPadding(
                new Insets(
                        28,
                        15,
                        25,
                        15
                )
        );

        sidebar.setStyle(
                "-fx-background-color: #110B1C;" +
                "-fx-border-color: #241735;" +
                "-fx-border-width: 0 1 0 0;"
        );

        Label logo =
                new Label("⌂  HOSTEL HUB");

        logo.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        22
                )
        );

        logo.setTextFill(Color.WHITE);

        sidebar.getChildren().add(logo);

        sidebar.getChildren().add(
                sectionLabel("MAIN")
        );

        Button dashboard =
                menuButton(
                        "⌂",
                        "Dashboard"
                );

        Button profile =
                menuButton(
                        "●",
                        "My Profile"
                );

        Button room =
                menuButton(
                        "▣",
                        "My Room"
                );

        Button fees =
                menuButton(
                        "₹",
                        "Fees & Payments"
                );

        sidebar.getChildren().addAll(
                dashboard,
                profile,
                room,
                fees
        );

        sidebar.getChildren().add(
                sectionLabel("SERVICES")
        );

        Button complaints =
                menuButton(
                        "!",
                        "Complaints"
                );

        Button leave =
                menuButton(
                        "□",
                        "Leave Application"
                );

        Button notices =
                menuButton(
                        "●",
                        "Notices"
                );

        Button mess =
                menuButton(
                        "◆",
                        "Mess"
                );

        Button attendance =
                menuButton(
                        "✓",
                        "Attendance"
                );

        sidebar.getChildren().addAll(
                complaints,
                leave,
                notices,
                mess,
                attendance
        );

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        sidebar.getChildren().add(
                spacer
        );

        Button logout =
                menuButton(
                        "↪",
                        "Logout"
                );

        sidebar.getChildren().add(
                logout
        );

        dashboard.setOnAction(e ->
                root.setCenter(
                        createDashboardContent(student)
                )
        );

        profile.setOnAction(e ->
                root.setCenter(
                        createProfilePage(student)
                )
        );

        room.setOnAction(e ->
                root.setCenter(
                        createRoomPage(student)
                )
        );

        fees.setOnAction(e ->
                root.setCenter(
                        createFeesPage()
                )
        );

        complaints.setOnAction(e ->
                root.setCenter(
                        createComplaintsPage()
                )
        );

        leave.setOnAction(e ->
                root.setCenter(
                        createLeavePage()
                )
        );

        notices.setOnAction(e ->
                root.setCenter(
                        createNoticesPage()
                )
        );

        mess.setOnAction(e ->
                root.setCenter(
                        createMessPage()
                )
        );

        attendance.setOnAction(e ->
                root.setCenter(
                        createAttendancePage()
                )
        );

        logout.setOnAction(e ->
                StudentLogin.show()
        );

        return sidebar;
    }

    private static VBox createDashboardContent(
            Student student
    ) {

        VBox content =
                new VBox(25);

        content.setPadding(
                new Insets(
                        30,
                        35,
                        30,
                        35
                )
        );

        Label welcome =
                new Label(
                        "Good Morning, "
                                + safe(
                                        student.getFullName()
                                )
                                + " 👋"
                );

        welcome.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        30
                )
        );

        welcome.setTextFill(
                Color.WHITE
        );

        Label subtitle =
                new Label(
                        "Here's what's happening with your hostel account."
                );

        subtitle.setTextFill(
                Color.web("#A99CB8")
        );

        content.getChildren().add(
                new VBox(
                        5,
                        welcome,
                        subtitle
                )
        );

        HBox cards =
                new HBox(18);

        cards.getChildren().addAll(

                statCard(
                        "COURSE",
                        safe(student.getCourse()),
                        safe(student.getYear()),
                        "◆"
                ),

                statCard(
                        "PHONE",
                        safe(student.getPhone()),
                        safe(student.getCity()),
                        "☎"
                ),

                statCard(
                        "ROOM",
                        "Not Assigned",
                        safe(student.getRoomPreference()),
                        "▣"
                ),

                statCard(
                        "GENDER",
                        safe(student.getGender()),
                        "Student",
                        "●"
                )
        );

        content.getChildren().add(
                cards
        );

        Label quickTitle =
                new Label("Quick Actions");

        quickTitle.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        20
                )
        );

        quickTitle.setTextFill(
                Color.WHITE
        );

        HBox quickActions =
                new HBox(15);

        quickActions.getChildren().addAll(

                quickButton(
                        "📝  Raise Complaint"
                ),

                quickButton(
                        "📅  Apply Leave"
                ),

                quickButton(
                        "💳  Pay Fees"
                ),

                quickButton(
                        "👤  View Profile"
                )
        );

        content.getChildren().addAll(
                quickTitle,
                quickActions
        );

        HBox lower =
                new HBox(20);

        lower.getChildren().addAll(

                panel(
                        "Student Information",

                        "Name: "
                                + safe(
                                        student.getFullName()
                                ),

                        "Email: "
                                + safe(
                                        student.getEmail()
                                ),

                        "College: "
                                + safe(
                                        student.getCollege()
                                ),

                        "City: "
                                + safe(
                                        student.getCity()
                                )
                ),

                panel(
                        "Personal Information",

                        "Parent: "
                                + safe(
                                        student.getParentName()
                                ),

                        "Date of Birth: "
                                + safe(
                                        student.getDateOfBirth()
                                ),

                        "Room Preference: "
                                + safe(
                                        student.getRoomPreference()
                                ),
                        "Room Number: " + safe(student.getRoomNumber()),
                        "Address: "
                                + safe(
                                        student.getAddress()
                                )
                )
        );

        content.getChildren().add(
                lower
        );

        return content;
    }

    private static VBox statCard(
            String title,
            String value,
            String sub,
            String icon
    ) {

        VBox card =
                new VBox(8);

        card.setPrefWidth(205);

        card.setPadding(
                new Insets(20)
        );

        card.setStyle(
                "-fx-background-color: #151022;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #2C1D3F;" +
                "-fx-border-radius: 18px;"
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setTextFill(
                Color.web("#A064FF")
        );

        iconLabel.setFont(
                Font.font(22)
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setTextFill(
                Color.web("#91849F")
        );

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11
                )
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setTextFill(
                Color.WHITE
        );

        valueLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        20
                )
        );

        valueLabel.setWrapText(true);

        Label subLabel =
                new Label(sub);

        subLabel.setTextFill(
                Color.web("#A99CB8")
        );

        subLabel.setWrapText(true);

        card.getChildren().addAll(
                iconLabel,
                titleLabel,
                valueLabel,
                subLabel
        );

        return card;
    }

    private static Button quickButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setPrefWidth(190);

        button.setPrefHeight(50);

        button.setStyle(
                "-fx-background-color: #181027;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 14px;" +
                "-fx-border-color: #352451;" +
                "-fx-border-radius: 14px;" +
                "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                scale(button, 1.04)
        );

        button.setOnMouseExited(e ->
                scale(button, 1)
        );

        return button;
    }

    private static VBox panel(
            String title,
            String... items
    ) {

        VBox box =
                new VBox(15);

        box.setPrefWidth(430);

        box.setPadding(
                new Insets(22)
        );

        box.setStyle(
                "-fx-background-color: #151022;" +
                "-fx-background-radius: 18px;" +
                "-fx-border-color: #2C1D3F;" +
                "-fx-border-radius: 18px;"
        );

        Label heading =
                new Label(title);

        heading.setTextFill(
                Color.WHITE
        );

        heading.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        18
                )
        );

        box.getChildren().add(
                heading
        );

        for (String item : items) {

            Label label =
                    new Label(
                            "•  " + item
                    );

            label.setWrapText(true);

            label.setTextFill(
                    Color.web("#B8AFC2")
            );

            label.setFont(
                    Font.font(
                            "Segoe UI",
                            13
                    )
            );

            box.getChildren().add(
                    label
            );
        }

        return box;
    }

    private static VBox createProfilePage(
            Student student
    ) {

        VBox content =
                pageContainer(
                        "My Profile",
                        "Your registered student information."
                );

        GridPane grid =
                new GridPane();

        grid.setHgap(20);

        grid.setVgap(15);

        TextField name =
                input(
                        "Full Name",
                        safe(student.getFullName())
                );

        TextField phone =
                input(
                        "Phone Number",
                        safe(student.getPhone())
                );

        TextField email =
                input(
                        "Email",
                        safe(student.getEmail())
                );

        TextField parent =
                input(
                        "Parent Name",
                        safe(student.getParentName())
                );

        TextField dob =
                input(
                        "Date of Birth",
                        safe(student.getDateOfBirth())
                );

        TextField gender =
                input(
                        "Gender",
                        safe(student.getGender())
                );

        TextField course =
                input(
                        "Course",
                        safe(student.getCourse())
                );

        TextField year =
                input(
                        "Year",
                        safe(student.getYear())
                );

        TextField college =
                input(
                        "College",
                        safe(student.getCollege())
                );

        TextField city =
                input(
                        "City",
                        safe(student.getCity())
                );

        TextField roomPreference =
                input(
                        "Room Preference",
                        safe(student.getRoomPreference())
                );

        TextField address =
                input(
                        "Address",
                        safe(student.getAddress())
                );

        addProfileField(
                grid,
                "Full Name",
                name,
                0,
                0
        );

        addProfileField(
                grid,
                "Phone",
                phone,
                1,
                0
        );

        addProfileField(
                grid,
                "Email",
                email,
                0,
                2
        );

        addProfileField(
                grid,
                "Parent Name",
                parent,
                1,
                2
        );

        addProfileField(
                grid,
                "Date of Birth",
                dob,
                0,
                4
        );

        addProfileField(
                grid,
                "Gender",
                gender,
                1,
                4
        );

        addProfileField(
                grid,
                "Course",
                course,
                0,
                6
        );

        addProfileField(
                grid,
                "Year",
                year,
                1,
                6
        );

        addProfileField(
                grid,
                "College",
                college,
                0,
                8
        );

        addProfileField(
                grid,
                "City",
                city,
                1,
                8
        );

        addProfileField(
                grid,
                "Room Preference",
                roomPreference,
                0,
                10
        );

        addProfileField(
                grid,
                "Address",
                address,
                1,
                10
        );

        Button save =
                primaryButton(
                        "SAVE CHANGES"
                );

        save.setOnAction(e ->
                showMessage(
                        "Profile updated successfully."
                )
        );

        content.getChildren().addAll(
                grid,
                save
        );

        return content;
    }

    private static void addProfileField(
            GridPane grid,
            String labelText,
            TextField field,
            int col,
            int row
    ) {

        grid.add(
                fieldLabel(labelText),
                col,
                row
        );

        grid.add(
                field,
                col,
                row + 1
        );
    }

    private static VBox createRoomPage(
            Student student
    ) {

        return pageContainer(
                "My Room",
                "Your current hostel room information.",
                panel(
                        "Room Details",
                        "Student: "
                                + safe(
                                        student.getFullName()
                                ),
                        "Room Preference: "
                                + safe(
                                        student.getRoomPreference()
                                ),
                        "Room Number: Not Assigned",
                        "Building: Not Assigned",
                        "Floor: Not Assigned",
                        "Bed Number: Not Assigned",
                        "Roommate: Not Assigned"
                )
        );
    }

    private static VBox createFeesPage() {

        VBox content =
                pageContainer(
                        "Fees & Payments",
                        "Manage your hostel fee payments."
                );

        HBox cards =
                new HBox(18);

        cards.getChildren().addAll(

                statCard(
                        "TOTAL FEES",
                        "₹25,000",
                        "Academic Year",
                        "₹"
                ),

                statCard(
                        "PAID",
                        "₹18,500",
                        "Completed",
                        "✓"
                ),

                statCard(
                        "REMAINING",
                        "₹6,500",
                        "Due Amount",
                        "!"
                )
        );

        Button pay =
                primaryButton(
                        "PAY REMAINING FEES"
                );

        pay.setOnAction(e ->
                showMessage(
                        "Payment gateway will open here."
                )
        );

        content.getChildren().addAll(
                cards,
                pay
        );

        return content;
    }

    private static VBox createComplaintsPage() {

        VBox content =
                pageContainer(
                        "Complaints",
                        "Raise and track your hostel complaints."
                );

        ComboBox<String> category =
                new ComboBox<>();

        category.getItems().addAll(
                "Room",
                "Mess",
                "Electricity",
                "Water",
                "Cleaning",
                "Wi-Fi",
                "Other"
        );

        category.setPromptText(
                "Select Complaint Category"
        );

        TextArea description =
                new TextArea();

        description.setPromptText(
                "Describe your complaint..."
        );

        description.setPrefHeight(120);

        Button submit =
                primaryButton(
                        "SUBMIT COMPLAINT"
                );

        submit.setOnAction(e ->
                showMessage(
                        "Complaint submitted successfully."
                )
        );

        content.getChildren().addAll(

                fieldLabel("Category"),

                category,

                fieldLabel("Description"),

                description,

                submit,

                panel(
                        "My Complaints",
                        "#102 — Room fan not working — In Progress",
                        "#101 — Cleaning issue — Resolved"
                )
        );

        return content;
    }

    private static VBox createLeavePage() {

        VBox content =
                pageContainer(
                        "Leave Application",
                        "Apply for hostel leave and track approval."
                );

        ComboBox<String> type =
                new ComboBox<>();

        type.getItems().addAll(
                "Home Visit",
                "Medical",
                "Personal",
                "Emergency",
                "Other"
        );

        type.setPromptText(
                "Leave Type"
        );

        DatePicker from =
                new DatePicker();

        DatePicker to =
                new DatePicker();

        TextArea reason =
                new TextArea();

        reason.setPromptText(
                "Reason for leave"
        );

        reason.setPrefHeight(100);

        Button apply =
                primaryButton(
                        "SUBMIT LEAVE APPLICATION"
                );

        apply.setOnAction(e ->
                showMessage(
                        "Leave application submitted."
                )
        );

        content.getChildren().addAll(

                fieldLabel("Leave Type"),

                type,

                fieldLabel("From Date"),

                from,

                fieldLabel("To Date"),

                to,

                fieldLabel("Reason"),

                reason,

                apply,

                panel(
                        "Application Status",
                        "Recent application — Pending"
                )
        );

        return content;
    }

    private static VBox createNoticesPage() {

        return pageContainer(
                "Hostel Notices",
                "Important announcements from hostel administration.",
                panel(
                        "Latest Notices",
                        "Hostel inspection this Sunday.",
                        "Mess menu updated.",
                        "Wi-Fi maintenance scheduled.",
                        "New visitor timing rules."
                )
        );
    }

    private static VBox createMessPage() {

        return pageContainer(
                "Mess",
                "Today's hostel mess information.",
                panel(
                        "Today's Menu",
                        "Breakfast — Poha, Tea & Banana",
                        "Lunch — Dal, Rice, Roti & Vegetable",
                        "Snacks — Tea & Biscuits",
                        "Dinner — Paneer, Roti & Rice"
                )
        );
    }

    private static VBox createAttendancePage() {

        return pageContainer(
                "Attendance",
                "Track your hostel attendance.",
                statCard(
                        "MONTHLY ATTENDANCE",
                        "92%",
                        "Excellent",
                        "✓"
                ),
                panel(
                        "Attendance Summary",
                        "Present — 23 Days",
                        "Absent — 2 Days",
                        "Total — 25 Days"
                )
        );
    }

    private static VBox pageContainer(
            String title,
            String subtitle,
            javafx.scene.Node... nodes
    ) {

        VBox box =
                new VBox(20);

        box.setPadding(
                new Insets(35)
        );

        Label heading =
                new Label(title);

        heading.setTextFill(
                Color.WHITE
        );

        heading.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        30
                )
        );

        Label sub =
                new Label(subtitle);

        sub.setTextFill(
                Color.web("#A99CB8")
        );

        box.getChildren().addAll(
                heading,
                sub
        );

        box.getChildren().addAll(
                nodes
        );

        return box;
    }

    private static TextField input(
            String prompt,
            String value
    ) {

        TextField field =
                new TextField(value);

        field.setPromptText(prompt);

        field.setPrefWidth(360);

        field.setPrefHeight(45);

        field.setStyle(
                "-fx-background-color: #181027;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #80758F;" +
                "-fx-background-radius: 12px;" +
                "-fx-border-color: #352451;" +
                "-fx-border-radius: 12px;" +
                "-fx-padding: 0 14px;"
        );

        return field;
    }

    private static Label fieldLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setTextFill(
                Color.web("#BFA3FF")
        );

        label.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        12
                )
        );

        return label;
    }

    private static Button primaryButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setPrefWidth(260);

        button.setPrefHeight(48);

        button.setStyle(
                "-fx-background-color: linear-gradient(" +
                "to right, #7437E8, #A064FF);" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25px;" +
                "-fx-cursor: hand;"
        );

        return button;
    }

    private static Button menuButton(
            String icon,
            String text
    ) {

        Button button =
                new Button(
                        icon + "   " + text
                );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(45);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(
                        0,
                        15,
                        0,
                        15
                )
        );

        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #BDB1D0;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 10px;" +
                "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #25163A;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #BDB1D0;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"
                )
        );

        return button;
    }

    private static Label sectionLabel(
            String text
    ) {

        Label label =
                new Label(text);

        label.setTextFill(
                Color.web("#675B74")
        );

        label.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        10
                )
        );

        label.setPadding(
                new Insets(
                        10,
                        10,
                        5,
                        10
                )
        );

        return label;
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

    private static void showMessage(
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Hostel Hub"
        );

        alert.setHeaderText(null);

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }

    private static String safe(
            String value
    ) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not Available";
        }

        return value;
    }
}