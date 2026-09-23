package com.hostelhub.view;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.dao.FirestoreParentDao;
import com.hostelhub.dao.FirestoreStudentDao;
import com.hostelhub.dao.FirestoreWardenDao;
import com.hostelhub.dao.MessMenuDao;
import com.hostelhub.dao.NoticeDao;
import com.hostelhub.dao.StudentRequestDao;
import com.hostelhub.model.Student;
import com.hostelhub.model.Warden;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WardenDashboard {

    private static BorderPane root;
    private static String wardenUid;
    private static Warden warden;
    private static String currentPage = "dashboard";
    private static boolean darkMode = false;

    private static final FirestoreWardenDao wardenDao = new FirestoreWardenDao();
    private static final FirestoreStudentDao studentDao = new FirestoreStudentDao();
    private static final FirestoreParentDao parentDao = new FirestoreParentDao();
    private static final StudentRequestDao requestDao = new StudentRequestDao();
    private static final NoticeDao noticeDao = new NoticeDao();
    private static final MessMenuDao messMenuDao = new MessMenuDao();

    private static final String PRIMARY = "#4F46E5";
    private static final String PRIMARY_DARK = "#3730A3";
    private static final String GREEN = "#16A34A";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#EA580C";
    private static final String BLUE = "#2563EB";

    private static String BG = "#F4F6FB";
    private static String CARD = "#FFFFFF";
    private static String TEXT = "#111827";
    private static String MUTED = "#6B7280";
    private static String BORDER = "#E5E7EB";
    private static String SIDEBAR = "#111827";
    private static String SIDEBAR_TEXT = "#D1D5DB";
    private static String SOFT_BG = "#F3F4F6";
    private static String INPUT_BG = "#F9FAFB";
    private static String ICON_BG = "#EEF2FF";
    private static String SIDEBAR_HOVER = "#1F2937";
    private static String PROMPT = "#9CA3AF";

    public static void show(String uid) {

        wardenUid = uid;
        warden = wardenDao.getWardenByUid(uid);

        applyTheme();

        root = new BorderPane();
        currentPage = "dashboard";

        rebuild(currentPage);

        Scene scene = new Scene(root, 1550, 800);

        Stage stage = Welcome.stage;

        stage.setTitle("HostelHub - Warden Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private static void applyTheme() {

        if (darkMode) {

            BG = "#0F172A";
            CARD = "#1E293B";
            TEXT = "#F8FAFC";
            MUTED = "#94A3B8";
            BORDER = "#334155";
            SIDEBAR = "#020617";
            SIDEBAR_TEXT = "#CBD5E1";
            SOFT_BG = "#1E293B";
            INPUT_BG = "#0F172A";
            ICON_BG = "#312E81";
            SIDEBAR_HOVER = "#1E293B";
            PROMPT = "#64748B";

        } else {

            BG = "#F4F6FB";
            CARD = "#FFFFFF";
            TEXT = "#111827";
            MUTED = "#6B7280";
            BORDER = "#E5E7EB";
            SIDEBAR = "#111827";
            SIDEBAR_TEXT = "#D1D5DB";
            SOFT_BG = "#F3F4F6";
            INPUT_BG = "#F9FAFB";
            ICON_BG = "#EEF2FF";
            SIDEBAR_HOVER = "#1F2937";
            PROMPT = "#9CA3AF";
        }
    }

    private static void rebuild(String page) {

        currentPage = page;

        applyTheme();

        root.setStyle("-fx-background-color:" + BG + ";");

        root.setTop(createTopBar());
        root.setLeft(createSidebar());

        switch (page) {

            case "students":
                root.setCenter(createStudentsPage());
                break;

            case "rooms":
                root.setCenter(createRoomsPage());
                break;

            case "attendance":
                root.setCenter(createAttendancePage());
                break;

            case "fees":
                root.setCenter(createFeesPage());
                break;

            case "complaints":
                root.setCenter(createComplaintsPage());
                break;

            case "leave":
                root.setCenter(createLeavePage());
                break;

            case "notices":
                root.setCenter(createNoticesPage());
                break;

            case "mess":
                root.setCenter(createMessPage());
                break;

            case "parents":
                root.setCenter(createParentsPage());
                break;

            case "parentVisits":
                root.setCenter(createParentVisitsPage());
                break;

            case "emergency":
                root.setCenter(createEmergencyPage());
                break;

            case "profile":
                root.setCenter(createProfilePage());
                break;

            default:
                root.setCenter(createPageScroll(createDashboardPage()));
                break;
        }
    }

    private static ScrollPane createPageScroll(VBox content) {

        ScrollPane scroll = new ScrollPane(content);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setStyle(
                "-fx-background-color:" + BG + ";" +
                "-fx-background:" + BG + ";" +
                "-fx-border-color:transparent;");

        return scroll;
    }

    private static HBox createTopBar() {

        HBox bar = new HBox(18);

        bar.setAlignment(Pos.CENTER_LEFT);

        bar.setPadding(
                new Insets(
                        16,
                        30,
                        16,
                        30));

        bar.setStyle(
                "-fx-background-color:" +
                        CARD +
                        ";" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-width:0 0 1 0;");

        Label logo = new Label(
                "🏫  HOSTELHUB");

        logo.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        18));

        logo.setStyle(
                "-fx-text-fill:" +
                        PRIMARY +
                        ";");

        Label divider = new Label("|");

        divider.setStyle(
                "-fx-text-fill:" +
                        BORDER +
                        ";");

        Label title = new Label(
                "Warden Dashboard");

        title.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        16));

        title.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        ToggleButton themeToggle = new ToggleButton(
                darkMode
                        ? "🌙 Dark"
                        : "☀ Light");

        themeToggle.setSelected(
                darkMode);

        themeToggle.setPrefWidth(100);
        themeToggle.setPrefHeight(38);

        themeToggle.setStyle(
                "-fx-background-color:" +
                        SOFT_BG +
                        ";" +
                        "-fx-text-fill:" +
                        TEXT +
                        ";" +
                        "-fx-background-radius:9;" +
                        "-fx-cursor:hand;");

        themeToggle.setOnAction(
                e -> {

                    darkMode =
                            themeToggle.isSelected();

                    rebuild(currentPage);
                });

        Label notification = new Label("🔔");

        notification.setFont(
                Font.font(
                        "Segoe UI",
                        19));

        notification.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        String name =
                warden == null
                        ? "Warden"
                        : safe(
                                warden.getFullName());

        Label wardenName = new Label(
                "👤  " + name);

        wardenName.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        13));

        wardenName.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        bar.getChildren().addAll(
                logo,
                divider,
                title,
                spacer,
                themeToggle,
                notification,
                wardenName);

        return bar;
    }

    private static VBox createSidebar() {

        VBox sidebar = new VBox(8);

        sidebar.setPrefWidth(245);

        sidebar.setPadding(
                new Insets(
                        24,
                        14,
                        18,
                        14));

        sidebar.setStyle(
                "-fx-background-color:" +
                        SIDEBAR +
                        ";");

        Label menuTitle = new Label(
                "WARDEN MENU");

        menuTitle.setPadding(
                new Insets(
                        0,
                        0,
                        12,
                        10));

        menuTitle.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11));

        menuTitle.setStyle(
                "-fx-text-fill:" +
                        SIDEBAR_TEXT +
                        ";");

        sidebar.getChildren().add(menuTitle);

        sidebar.getChildren().add(
                menuButton(
                        "🏠",
                        "Dashboard",
                        "dashboard"));

        sidebar.getChildren().add(
                menuButton(
                        "👨‍🎓",
                        "Students",
                        "students"));

        sidebar.getChildren().add(
                menuButton(
                        "👨‍👩‍👧",
                        "Parents",
                        "parents"));

        sidebar.getChildren().add(
                menuButton(
                        "📋",
                        "Parent Visits",
                        "parentVisits"));

        sidebar.getChildren().add(
                menuButton(
                        "🚪",
                        "Rooms",
                        "rooms"));

        sidebar.getChildren().add(
                menuButton(
                        "📍",
                        "Attendance",
                        "attendance"));

        sidebar.getChildren().add(
                menuButton(
                        "💰",
                        "Fees",
                        "fees"));

        sidebar.getChildren().add(
                menuButton(
                        "📝",
                        "Complaints",
                        "complaints"));

        sidebar.getChildren().add(
                menuButton(
                        "📋",
                        "Leave Requests",
                        "leave"));

        sidebar.getChildren().add(
                menuButton(
                        "📢",
                        "Notices",
                        "notices"));

        sidebar.getChildren().add(
                menuButton(
                        "🍽",
                        "Mess Menu",
                        "mess"));

        sidebar.getChildren().add(
                menuButton(
                        "🚨",
                        "Emergency",
                        "emergency"));

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS);

        sidebar.getChildren().add(spacer);

        sidebar.getChildren().add(
                menuButton(
                        "👤",
                        "Warden Profile",
                        "profile"));

        Button logout = menuButton(
                "↪",
                "Logout",
                "logout");

        sidebar.getChildren().add(logout);

        return sidebar;
    }

    private static Button menuButton(
            String icon,
            String text,
            String page) {

        Button button = new Button(
                icon + "   " + text);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setPrefHeight(44);

        button.setAlignment(
                Pos.CENTER_LEFT);

        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14));

        String background =
                currentPage.equals(page)
                        ? PRIMARY
                        : "transparent";

        button.setStyle(
                "-fx-background-color:" +
                        background +
                        ";" +
                        "-fx-text-fill:" +
                        SIDEBAR_TEXT +
                        ";" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:9;" +
                        "-fx-cursor:hand;");

        button.setOnMouseEntered(
                e -> {

                    if (!currentPage.equals(page)) {

                        button.setStyle(
                                "-fx-background-color:" +
                                        SIDEBAR_HOVER +
                                        ";" +
                                        "-fx-text-fill:white;" +
                                        "-fx-font-family:'Segoe UI';" +
                                        "-fx-font-size:13;" +
                                        "-fx-font-weight:bold;" +
                                        "-fx-background-radius:9;" +
                                        "-fx-cursor:hand;");
                    }
                });

        button.setOnMouseExited(
                e -> {

                    String bg =
                            currentPage.equals(page)
                                    ? PRIMARY
                                    : "transparent";

                    button.setStyle(
                            "-fx-background-color:" +
                                    bg +
                                    ";" +
                                    "-fx-text-fill:" +
                                    SIDEBAR_TEXT +
                                    ";" +
                                    "-fx-font-family:'Segoe UI';" +
                                    "-fx-font-size:13;" +
                                    "-fx-font-weight:bold;" +
                                    "-fx-background-radius:9;" +
                                    "-fx-cursor:hand;");
                });

        button.setOnAction(
                e -> {

                    if ("logout".equals(page)) {

                        Stage stage =
                                Welcome.stage;

                        stage.close();

                        return;
                    }

                    rebuild(page);
                });

        return button;
    }

    private static VBox createDashboardPage() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(28));

        VBox heading = pageHeading(
                "Good Morning, " +
                        (warden == null
                                ? "Warden"
                                : safe(
                                        warden.getFullName()))
                        +
                        " 👋",
                "Here's what's happening in your hostel today.");

        List<Student> students =
                studentDao.getAllStudents();

        List<Map<String, Object>> complaints =
                requestDao.getAllComplaints();

        List<Map<String, Object>> leaves =
                requestDao.getAllLeaveRequests();

        List<Map<String, Object>> emergencies =
                requestDao.getAllEmergencies();

        List<Map<String, Object>> parents =
                parentDao.getAllParents();

        List<Map<String, Object>> parentVisits =
                parentDao.getAllVisits();

        int pendingParentVisits = 0;

        for (Map<String, Object> visit :
                parentVisits) {

            if ("Waiting".equalsIgnoreCase(
                    value(
                            visit.get("status")))) {

                pendingParentVisits++;
            }
        }

        HBox cards = new HBox(16);

        cards.getChildren().addAll(
                statCard(
                        "TOTAL STUDENTS",
                        String.valueOf(
                                students.size()),
                        "👨‍🎓",
                        BLUE),
                statCard(
                        "TOTAL PARENTS",
                        String.valueOf(
                                parents.size()),
                        "👨‍👩‍👧",
                        PRIMARY),
                statCard(
                        "PARENT VISITS",
                        String.valueOf(
                                pendingParentVisits),
                        "📋",
                        GREEN),
                statCard(
                        "PENDING COMPLAINTS",
                        String.valueOf(
                                countPending(
                                        complaints)),
                        "📝",
                        ORANGE),
                statCard(
                        "EMERGENCY",
                        String.valueOf(
                                countPending(
                                        emergencies)),
                        "🚨",
                        RED));

        VBox recent = whitePanel();

        recent.getChildren().add(
                sectionTitle(
                        "👨‍🎓 Recent Students"));

        int limit =
                Math.min(
                        students.size(),
                        5);

        for (int i = 0;
             i < limit;
             i++) {

            Student student =
                    students.get(i);

            recent.getChildren().add(
                    requestRow(
                            "👤",
                            safe(
                                    student.getFullName()),
                            safe(
                                    student.getCourse()) +
                                    " • Room " +
                                    safe(
                                            student.getRoomNumber()),
                            BLUE));
        }

        if (students.isEmpty()) {

            recent.getChildren().add(
                    emptyLabel(
                            "No students found."));
        }

        VBox requests = whitePanel();

        requests.getChildren().add(
                sectionTitle(
                        "📌 Pending Requests"));

        int pending = 0;

        for (Map<String, Object> item :
                complaints) {

            if (isPending(
                    value(
                            item.get("status")))) {

                requests.getChildren().add(
                        requestRow(
                                "📝",
                                safe(
                                        value(
                                                item.get(
                                                        "studentName"))),
                                "Complaint • " +
                                        value(
                                                item.get(
                                                        "category")),
                                ORANGE));

                pending++;

                if (pending >= 3) {
                    break;
                }
            }
        }

        if (pending == 0) {

            requests.getChildren().add(
                    emptyLabel(
                            "No pending requests."));
        }

        HBox middle = new HBox(18);

        HBox.setHgrow(
                recent,
                Priority.ALWAYS);

        HBox.setHgrow(
                requests,
                Priority.ALWAYS);

        middle.getChildren().addAll(
                recent,
                requests);

        VBox notices = whitePanel();

        notices.getChildren().add(
                sectionTitle(
                        "📢 Latest Notices"));

        List<Map<String, Object>> noticeList =
                noticeDao.getNotices();

        int noticeLimit =
                Math.min(
                        noticeList.size(),
                        4);

        for (int i = 0;
             i < noticeLimit;
             i++) {

            Map<String, Object> notice =
                    noticeList.get(i);

            notices.getChildren().add(
                    announcementRow(
                            value(
                                    notice.get(
                                            "title")),
                            value(
                                    notice.get(
                                            "message"))));
        }

        if (noticeList.isEmpty()) {

            notices.getChildren().add(
                    emptyLabel(
                            "No notices available."));
        }

        content.getChildren().addAll(
                heading,
                cards,
                middle,
                notices);

        return content;
    }

    private static VBox statCard(
            String title,
            String value,
            String icon,
            String color) {

        VBox box = new VBox(10);

        box.setPadding(
                new Insets(20));

        box.setPrefHeight(125);

        box.setStyle(
                "-fx-background-color:" +
                        CARD +
                        ";" +
                        "-fx-background-radius:14;" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:14;");

        HBox top = new HBox();

        top.setAlignment(
                Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);

        iconLabel.setPadding(
                new Insets(9));

        iconLabel.setStyle(
                "-fx-background-color:" +
                        color +
                        "20;" +
                        "-fx-background-radius:10;" +
                        "-fx-font-size:18;");

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label count = new Label(value);

        count.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        25));

        count.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        top.getChildren().addAll(
                iconLabel,
                spacer,
                count);

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11));

        titleLabel.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        box.getChildren().addAll(
                top,
                titleLabel);

        HBox.setHgrow(
                box,
                Priority.ALWAYS);

        return box;
    }

    private static VBox createStudentsPage() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Student Management",
                        "View and manage all hostel students."));

        TableView<Student> table =
                new TableView<>();

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, String> name =
                new TableColumn<>(
                        "Student Name");

        name.setCellValueFactory(
                new PropertyValueFactory<>(
                        "fullName"));

        TableColumn<Student, String> email =
                new TableColumn<>("Email");

        email.setCellValueFactory(
                new PropertyValueFactory<>(
                        "email"));

        TableColumn<Student, String> phone =
                new TableColumn<>("Phone");

        phone.setCellValueFactory(
                new PropertyValueFactory<>(
                        "phone"));

        TableColumn<Student, String> course =
                new TableColumn<>("Course");

        course.setCellValueFactory(
                new PropertyValueFactory<>(
                        "course"));

        TableColumn<Student, String> year =
                new TableColumn<>("Year");

        year.setCellValueFactory(
                new PropertyValueFactory<>(
                        "year"));

        TableColumn<Student, String> room =
                new TableColumn<>("Room");

        room.setCellValueFactory(
                new PropertyValueFactory<>(
                        "roomNumber"));

        TableColumn<Student, String> college =
                new TableColumn<>("College");

        college.setCellValueFactory(
                new PropertyValueFactory<>(
                        "college"));

        TableColumn<Student, Void> actions =
                new TableColumn<>(
                        "Actions");

        actions.setCellFactory(
                column ->
                        new TableCell<>() {

                            private final Button edit =
                                    actionButton(
                                            "EDIT",
                                            PRIMARY);

                            private final Button roomButton =
                                    actionButton(
                                            "ROOM",
                                            BLUE);

                            private final Button delete =
                                    actionButton(
                                            "DELETE",
                                            RED);

                            private final HBox box =
                                    new HBox(
                                            6,
                                            edit,
                                            roomButton,
                                            delete);

                            {
                                edit.setOnAction(
                                        e -> {

                                            Student s =
                                                    getTableView()
                                                            .getItems()
                                                            .get(
                                                                    getIndex());

                                            showEditStudentDialog(s);
                                        });

                                roomButton.setOnAction(
                                        e -> {

                                            Student s =
                                                    getTableView()
                                                            .getItems()
                                                            .get(
                                                                    getIndex());

                                            showRoomDialog(s);
                                        });

                                delete.setOnAction(
                                        e -> {

                                            Student s =
                                                    getTableView()
                                                            .getItems()
                                                            .get(
                                                                    getIndex());

                                            deleteStudent(s);
                                        });
                            }

                            @Override
                            protected void updateItem(
                                    Void item,
                                    boolean empty) {

                                super.updateItem(
                                        item,
                                        empty);

                                setGraphic(
                                        empty
                                                ? null
                                                : box);
                            }
                        });

        table.getColumns().addAll(
                name,
                email,
                phone,
                course,
                year,
                room,
                college,
                actions);

        table.setItems(
                FXCollections.observableArrayList(
                        studentDao.getAllStudents()));

        VBox panel = whitePanel();

        panel.getChildren().add(table);

        VBox.setVgrow(
                panel,
                Priority.ALWAYS);

        content.getChildren().add(panel);

        return content;
    }

    private static VBox createParentsPage() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Parent Management",
                        "View, edit and manage registered parents."));

        VBox panel = whitePanel();

        List<Map<String, Object>> parents =
                parentDao.getAllParents();

        if (parents.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No parents registered."));

        } else {

            for (Map<String, Object> parent :
                    parents) {

                String uid =
                        value(
                                parent.get("uid"));

                if (uid.isEmpty()) {

                    uid =
                            value(
                                    parent.get(
                                            "documentId"));
                }

                String name =
                        safe(
                                value(
                                        parent.get(
                                                "fullName")));

                String email =
                        safe(
                                value(
                                        parent.get(
                                                "email")));

                String phone =
                        safe(
                                value(
                                        parent.get(
                                                "phone")));

                String studentName =
                        safe(
                                value(
                                        parent.get(
                                                "studentName")));

                String studentId =
                        safe(
                                value(
                                        parent.get(
                                                "studentId")));

                HBox row = new HBox(15);

                row.setAlignment(
                        Pos.CENTER_LEFT);

                row.setPadding(
                        new Insets(
                                14,
                                0,
                                14,
                                0));

                VBox info = new VBox(4);

                Label parentLabel =
                        new Label(
                                "👤  " +
                                        name);

                parentLabel.setFont(
                        Font.font(
                                "Segoe UI",
                                FontWeight.BOLD,
                                14));

                parentLabel.setStyle(
                        "-fx-text-fill:" +
                                TEXT +
                                ";");

                Label details =
                        new Label(
                                "Email: " +
                                        email +
                                        " • Phone: " +
                                        phone +
                                        " • Student: " +
                                        studentName +
                                        " • Student ID: " +
                                        studentId);

                details.setWrapText(true);

                details.setStyle(
                        "-fx-text-fill:" +
                                MUTED +
                                ";");

                info.getChildren().addAll(
                        parentLabel,
                        details);

                Region spacer =
                        new Region();

                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS);

                Button edit =
                        actionButton(
                                "EDIT",
                                BLUE);

                Button delete =
                        actionButton(
                                "DELETE",
                                RED);

                String finalUid = uid;

                edit.setOnAction(
                        e ->
                                showEditParentDialog(
                                        finalUid,
                                        value(
                                                parent.get(
                                                        "fullName")),
                                        value(
                                                parent.get(
                                                        "email")),
                                        value(
                                                parent.get(
                                                        "phone")),
                                        value(
                                                parent.get(
                                                        "studentName")),
                                        value(
                                                parent.get(
                                                        "studentId"))));

                delete.setOnAction(
                        e ->
                                deleteParent(
                                        finalUid,
                                        name));

                row.getChildren().addAll(
                        info,
                        spacer,
                        edit,
                        delete);

                panel.getChildren().add(row);
            }
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(scroll);

        return content;
    }

    private static VBox createParentVisitsPage() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Parent Visit Requests",
                        "Review, approve or reject parent visit requests."));

        VBox panel = whitePanel();

        List<Map<String, Object>> visits =
                parentDao.getAllVisits();

        if (visits.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No parent visit requests found."));

        } else {

            for (Map<String, Object> visit :
                    visits) {

                String visitId =
                        value(
                                visit.get(
                                        "documentId"));

                String parentName =
                        safe(
                                value(
                                        visit.get(
                                                "parentName")));

                String studentName =
                        safe(
                                value(
                                        visit.get(
                                                "studentName")));

                String studentId =
                        safe(
                                value(
                                        visit.get(
                                                "studentId")));

                String date =
                        safe(
                                value(
                                        visit.get(
                                                "visitDate")));

                String time =
                        safe(
                                value(
                                        visit.get(
                                                "visitTime")));

                String purpose =
                        safe(
                                value(
                                        visit.get(
                                                "purpose")));

                String status =
                        value(
                                visit.get(
                                        "status"));

                if (status.isEmpty()) {
                    status = "Waiting";
                }

                HBox row = new HBox(15);

                row.setAlignment(
                        Pos.TOP_LEFT);

                row.setPadding(
                        new Insets(
                                15,
                                0,
                                15,
                                0));

                VBox info = new VBox(5);

                Label parent =
                        new Label(
                                "👨‍👩‍👧  " +
                                        parentName);

                parent.setFont(
                        Font.font(
                                "Segoe UI",
                                FontWeight.BOLD,
                                15));

                parent.setStyle(
                        "-fx-text-fill:" +
                                TEXT +
                                ";");

                Label student =
                        new Label(
                                "Student: " +
                                        studentName +
                                        " • ID: " +
                                        studentId);

                student.setStyle(
                        "-fx-text-fill:" +
                                MUTED +
                                ";");

                Label dateTime =
                        new Label(
                                "Date: " +
                                        date +
                                        " • Time: " +
                                        time);

                dateTime.setStyle(
                        "-fx-text-fill:" +
                                MUTED +
                                ";");

                Label purposeLabel =
                        new Label(
                                "Purpose: " +
                                        purpose);

                purposeLabel.setWrapText(true);

                purposeLabel.setStyle(
                        "-fx-text-fill:" +
                                MUTED +
                                ";");

                info.getChildren().addAll(
                        parent,
                        student,
                        dateTime,
                        purposeLabel);

                Region spacer =
                        new Region();

                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS);

                VBox right = new VBox(8);

                right.setAlignment(
                        Pos.TOP_RIGHT);

                Label statusLabel =
                        new Label(status);

                String statusTextColor =
                        statusColor(status);

                statusLabel.setStyle(
                        "-fx-text-fill:" +
                                statusTextColor +
                                ";" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-color:" +
                                statusTextColor +
                                "20;" +
                                "-fx-padding:7 12;" +
                                "-fx-background-radius:12;");

                right.getChildren().add(
                        statusLabel);

                if ("Waiting".equalsIgnoreCase(
                        status)) {

                    HBox actions =
                            new HBox(7);

                    Button approve =
                            actionButton(
                                    "ACCEPT",
                                    GREEN);

                    Button reject =
                            actionButton(
                                    "REJECT",
                                    RED);

                    approve.setOnAction(
                            e ->
                                    updateParentVisit(
                                            visitId,
                                            visit,
                                            "Approved",
                                            ""));

                    reject.setOnAction(
                            e ->
                                    showRejectVisitDialog(
                                            visitId,
                                            visit));

                    actions.getChildren().addAll(
                            approve,
                            reject);

                    right.getChildren().add(
                            actions);
                }

                if ("Rejected".equalsIgnoreCase(
                        status)) {

                    String reason =
                            value(
                                    visit.get(
                                            "rejectionReason"));

                    Label reasonLabel =
                            new Label(
                                    "Reason: " +
                                            safe(reason));

                    reasonLabel.setWrapText(true);
                    reasonLabel.setMaxWidth(300);

                    reasonLabel.setStyle(
                            "-fx-text-fill:" +
                                    RED +
                                    ";");

                    right.getChildren().add(
                            reasonLabel);
                }

                row.getChildren().addAll(
                        info,
                        spacer,
                        right);

                panel.getChildren().add(row);
            }
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(scroll);

        return content;
    }

    private static void showEditParentDialog(
            String uid,
            String currentName,
            String currentEmail,
            String currentPhone,
            String currentStudentName,
            String currentStudentId) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Parent");

        dialog.setHeaderText(
                "Update Parent Details");

        ButtonType save =
                new ButtonType(
                        "SAVE",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        ButtonType.CANCEL);

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(15));

        TextField name =
                inputField(
                        "Full Name");

        name.setText(currentName);

        TextField email =
                inputField(
                        "Email");

        email.setText(currentEmail);

        TextField phone =
                inputField(
                        "Phone");

        phone.setText(currentPhone);

        TextField studentName =
                inputField(
                        "Student Name");

        studentName.setText(
                currentStudentName);

        TextField studentId =
                inputField(
                        "Student ID");

        studentId.setText(
                currentStudentId);

        box.getChildren().addAll(
                name,
                email,
                phone,
                studentName,
                studentId);

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(
                button -> {

                    if (button == save) {

                        if (name.getText()
                                .trim()
                                .isEmpty()
                                ||
                                email.getText()
                                        .trim()
                                        .isEmpty()
                                ||
                                phone.getText()
                                        .trim()
                                        .isEmpty()
                                ||
                                studentName.getText()
                                        .trim()
                                        .isEmpty()
                                ||
                                studentId.getText()
                                        .trim()
                                        .isEmpty()) {

                            showMessage(
                                    "Please fill all parent details.");

                            return null;
                        }

                        boolean updated =
                                parentDao.updateParent(
                                        uid,
                                        name.getText()
                                                .trim(),
                                        email.getText()
                                                .trim(),
                                        phone.getText()
                                                .trim(),
                                        studentName.getText()
                                                .trim(),
                                        studentId.getText()
                                                .trim());

                        if (updated) {

                            showMessage(
                                    "Parent updated successfully.");

                            rebuild("parents");

                        } else {

                            showMessage(
                                    "Unable to update parent.");
                        }
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static void deleteParent(
            String uid,
            String name) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION);

        alert.setTitle(
                "Delete Parent");

        alert.setHeaderText(
                "Delete " +
                        safe(name) +
                        "?");

        alert.setContentText(
                "This parent profile will be deleted from Firestore.");

        alert.showAndWait()
                .ifPresent(
                        result -> {

                            if (result ==
                                    ButtonType.OK) {

                                boolean deleted =
                                        parentDao.deleteParent(
                                                uid);

                                if (deleted) {

                                    showMessage(
                                            "Parent deleted successfully.");

                                    rebuild("parents");

                                } else {

                                    showMessage(
                                            "Unable to delete parent.");
                                }
                            }
                        });
    }

    private static void showRejectVisitDialog(
            String visitId,
            Map<String, Object> visit) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Reject Parent Visit");

        dialog.setHeaderText(
                "Enter rejection reason");

        ButtonType reject =
                new ButtonType(
                        "REJECT",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        reject,
                        ButtonType.CANCEL);

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(15));

        TextArea reason =
                new TextArea();

        reason.setPromptText(
                "Enter reason for rejecting this visit...");

        reason.setPrefRowCount(5);
        reason.setWrapText(true);

        reason.setStyle(
                "-fx-background-color:" +
                        INPUT_BG +
                        ";" +
                        "-fx-control-inner-background:" +
                        INPUT_BG +
                        ";" +
                        "-fx-text-fill:" +
                        TEXT +
                        ";" +
                        "-fx-prompt-text-fill:" +
                        PROMPT +
                        ";" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:9;" +
                        "-fx-background-radius:9;");

        box.getChildren().add(reason);

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(
                button -> {

                    if (button == reject) {

                        String rejectionReason =
                                reason.getText()
                                        .trim();

                        if (rejectionReason.isEmpty()) {

                            showMessage(
                                    "Please enter rejection reason.");

                            return null;
                        }

                        updateParentVisit(
                                visitId,
                                visit,
                                "Rejected",
                                rejectionReason);
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static void updateParentVisit(
            String visitId,
            Map<String, Object> visit,
            String status,
            String rejectionReason) {

        boolean updated =
                parentDao.updateVisitStatus(
                        visitId,
                        status,
                        rejectionReason);

        if (!updated) {

            showMessage(
                    "Unable to update parent visit.");

            return;
        }

        String parentUid =
                value(
                        visit.get(
                                "parentUid"));

        String studentUid =
                value(
                        visit.get(
                                "studentUid"));

        String studentName =
                value(
                        visit.get(
                                "studentName"));

        String date =
                value(
                        visit.get(
                                "visitDate"));

        String time =
                value(
                        visit.get(
                                "visitTime"));

        saveVisitStatusNotification(
                parentUid,
                "PARENT",
                studentName,
                date,
                time,
                status,
                rejectionReason);

        saveVisitStatusNotification(
                studentUid,
                "STUDENT",
                value(
                        visit.get(
                                "parentName")),
                date,
                time,
                status,
                rejectionReason);

        showMessage(
                "Parent visit " +
                        status.toLowerCase() +
                        " successfully.");

        rebuild("parentVisits");
    }

    private static void saveVisitStatusNotification(
            String recipientUid,
            String recipientRole,
            String otherPersonName,
            String date,
            String time,
            String status,
            String reason) {

        if (recipientUid == null ||
                recipientUid.trim().isEmpty()) {

            return;
        }

        try {

            Map<String, Object> notification =
                    new LinkedHashMap<>();

            notification.put(
                    "recipientUid",
                    recipientUid);

            notification.put(
                    "recipientRole",
                    recipientRole);

            notification.put(
                    "title",
                    "🔔 Parent Visit " +
                            status);

            String message;

            if ("Approved".equalsIgnoreCase(
                    status)) {

                message =
                        "Parent visit request for " +
                                date +
                                " at " +
                                time +
                                " has been approved.";

            } else {

                message =
                        "Parent visit request for " +
                                date +
                                " at " +
                                time +
                                " has been rejected. Reason: " +
                                reason;
            }

            notification.put(
                    "message",
                    message);

            notification.put(
                    "type",
                    "PARENT_VISIT_STATUS");

            notification.put(
                    "read",
                    false);

            notification.put(
                    "createdAt",
                    FieldValue.serverTimestamp());

            FirebaseConfig
                    .getFireStore()
                    .collection(
                            "notifications")
                    .add(notification)
                    .get();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static VBox createRoomsPage() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Room Management",
                        "Allocate and update rooms for students."));

        VBox panel = whitePanel();

        List<Student> students =
                studentDao.getAllStudents();

        if (students.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No students available."));

        } else {

            for (Student student :
                    students) {

                HBox row =
                        new HBox(15);

                row.setAlignment(
                        Pos.CENTER_LEFT);

                row.setPadding(
                        new Insets(
                                12,
                                0,
                                12,
                                0));

                Label studentLabel =
                        new Label(
                                "👤  " +
                                        safe(
                                                student.getFullName()));

                studentLabel.setPrefWidth(260);

                styleBodyLabel(
                        studentLabel);

                Label roomLabel =
                        new Label(
                                "Room: " +
                                        safe(
                                                student.getRoomNumber()));

                roomLabel.setPrefWidth(180);

                styleBodyLabel(
                        roomLabel);

                Region spacer =
                        new Region();

                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS);

                Button change =
                        actionButton(
                                "CHANGE ROOM",
                                BLUE);

                change.setOnAction(
                        e ->
                                showRoomDialog(
                                        student));

                row.getChildren().addAll(
                        studentLabel,
                        roomLabel,
                        spacer,
                        change);

                panel.getChildren().add(row);
            }
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(scroll);

        return content;
    }

    private static VBox createAttendancePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Attendance Management",
                        "Monitor student hostel attendance and location records."));

        List<Map<String, Object>> attendance =
                requestDao.getAllAttendance();

        int present =
                attendance.size();

        int absent =
                Math.max(
                        0,
                        studentDao
                                .getAllStudents()
                                .size()
                                - present);

        PieChart pie =
                new PieChart(
                        FXCollections.observableArrayList(
                                new PieChart.Data(
                                        "Present",
                                        present),
                                new PieChart.Data(
                                        "Absent",
                                        absent)));

        pie.setTitle(
                "Attendance Overview");

        pie.setLegendVisible(true);

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();

        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis);

        chart.setTitle(
                "Attendance Records");

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        series.setName(
                "Attendance");

        series.getData().add(
                new XYChart.Data<>(
                        "Present",
                        present));

        series.getData().add(
                new XYChart.Data<>(
                        "Absent",
                        absent));

        chart.getData().add(series);

        HBox charts =
                new HBox(
                        20,
                        pie,
                        chart);

        HBox.setHgrow(
                pie,
                Priority.ALWAYS);

        HBox.setHgrow(
                chart,
                Priority.ALWAYS);

        VBox.setVgrow(
                charts,
                Priority.ALWAYS);

        VBox panel =
                whitePanel();

        panel.getChildren().add(
                sectionTitle(
                        "📍 Attendance Records"));

        for (Map<String, Object> item :
                attendance) {

            panel.getChildren().add(
                    requestRow(
                            "📍",
                            safe(
                                    value(
                                            item.get(
                                                    "studentName"))),
                            "Date: " +
                                    value(
                                            item.get(
                                                    "date"))
                                    +
                                    " • Check-in: " +
                                    value(
                                            item.get(
                                                    "checkInTime")),
                            BLUE));
        }

        if (attendance.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No attendance records found."));
        }

        content.getChildren().addAll(
                charts,
                panel);

        return content;
    }

    private static VBox createFeesPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Fee Management",
                        "View student fees and record payments."));

        List<Student> students =
                studentDao.getAllStudents();

        VBox summary =
                whitePanel();

        double total = 0;
        double paid = 0;
        double remaining = 0;

        for (Student student :
                students) {

            Map<String, Object> fee =
                    getStudentFeeData(
                            student.getUid());

            double totalFee =
                    getFeeNumber(
                            fee.get(
                                    "totalFee"));

            double paidFee =
                    getFeeNumber(
                            fee.get(
                                    "paidFee"));

            double remainingFee =
                    getFeeNumber(
                            fee.get(
                                    "remainingFee"));

            if (totalFee <= 0) {

                totalFee =
                        student.getTotalFee();
            }

            if (paidFee < 0) {
                paidFee = 0;
            }

            remainingFee =
                    Math.max(
                            0.0,
                            totalFee -
                                    paidFee);

            total += totalFee;
            paid += paidFee;
            remaining += remainingFee;
        }

        HBox feeStats =
                new HBox(20);

        feeStats.getChildren().addAll(
                statCard(
                        "TOTAL FEE",
                        rupee(total),
                        "💰",
                        BLUE),
                statCard(
                        "PAID",
                        rupee(paid),
                        "✅",
                        GREEN),
                statCard(
                        "REMAINING",
                        rupee(remaining),
                        "⏳",
                        ORANGE));

        VBox list =
                whitePanel();

        list.getChildren().add(
                sectionTitle(
                        "Student Fee Records"));

        for (Student student :
                students) {

            Map<String, Object> fee =
                    getStudentFeeData(
                            student.getUid());

            double totalFee =
                    getFeeNumber(
                            fee.get(
                                    "totalFee"));

            double paidFee =
                    getFeeNumber(
                            fee.get(
                                    "paidFee"));

            double remainingFee =
                    getFeeNumber(
                            fee.get(
                                    "remainingFee"));

            if (totalFee <= 0) {

                totalFee =
                        student.getTotalFee();
            }

            paidFee =
                    Math.max(
                            0.0,
                            paidFee);

            remainingFee =
                    Math.max(
                            0.0,
                            totalFee -
                                    paidFee);

            String status;

            if (remainingFee <= 0) {

                status = "PAID";

            } else if (paidFee > 0) {

                status = "PARTIALLY PAID";

            } else {

                status = "PENDING";
            }

            HBox row =
                    new HBox(15);

            row.setAlignment(
                    Pos.CENTER_LEFT);

            row.setPadding(
                    new Insets(
                            12,
                            0,
                            12,
                            0));

            Label studentLabel =
                    new Label(
                            "👤  " +
                                    safe(
                                            student.getFullName()));

            studentLabel.setPrefWidth(220);

            styleBodyLabel(
                    studentLabel);

            Label totalLabel =
                    new Label(
                            "Total: " +
                                    rupee(totalFee));

            totalLabel.setPrefWidth(130);

            styleBodyLabel(
                    totalLabel);

            Label paidLabel =
                    new Label(
                            "Paid: " +
                                    rupee(paidFee));

            paidLabel.setPrefWidth(130);

            styleBodyLabel(
                    paidLabel);

            Label remainingLabel =
                    new Label(
                            "Remaining: " +
                                    rupee(remainingFee));

            remainingLabel.setPrefWidth(160);

            styleBodyLabel(
                    remainingLabel);

            Label statusLabel =
                    new Label(status);

            statusLabel.setStyle(
                    "-fx-text-fill:" +
                            statusColor(status) +
                            ";" +
                            "-fx-font-weight:bold;");

            Region spacer =
                    new Region();

            HBox.setHgrow(
                    spacer,
                    Priority.ALWAYS);

            Button payment =
                    actionButton(
                            "💳 RECORD PAYMENT",
                            GREEN);

            final double finalRemainingFee =
                    remainingFee;

            payment.setOnAction(
                    e ->
                            showPaymentDialog(
                                    student.getUid(),
                                    student.getFullName(),
                                    finalRemainingFee));

            row.getChildren().addAll(
                    studentLabel,
                    totalLabel,
                    paidLabel,
                    remainingLabel,
                    statusLabel,
                    spacer,
                    payment);

            list.getChildren().add(row);
        }

        if (students.isEmpty()) {

            list.getChildren().add(
                    emptyLabel(
                            "No student fee records found."));
        }

        ScrollPane scroll =
                createScroll(list);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().addAll(
                feeStats,
                scroll);

        return content;
    }

    private static VBox createComplaintsPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Complaint Management",
                        "Review and manage student complaints."));

        VBox panel =
                whitePanel();

        List<Map<String, Object>> complaints =
                requestDao.getAllComplaints();

        for (Map<String, Object> complaint :
                complaints) {

            String status =
                    value(
                            complaint.get(
                                    "status"));

            HBox row =
                    new HBox(15);

            row.setAlignment(
                    Pos.CENTER_LEFT);

            row.setPadding(
                    new Insets(
                            14,
                            0,
                            14,
                            0));

            VBox info =
                    new VBox(4);

            Label student =
                    new Label(
                            safe(
                                    value(
                                            complaint.get(
                                                    "studentName"))));

            student.setFont(
                    Font.font(
                            "Segoe UI",
                            FontWeight.BOLD,
                            14));

            student.setStyle(
                    "-fx-text-fill:" +
                            TEXT +
                            ";");

            Label category =
                    new Label(
                            value(
                                    complaint.get(
                                            "category"))
                                    +
                                    " • " +
                                    value(
                                            complaint.get(
                                                    "description")));

            category.setWrapText(true);

            category.setStyle(
                    "-fx-text-fill:" +
                            MUTED +
                            ";");

            info.getChildren().addAll(
                    student,
                    category);

            Region spacer =
                    new Region();

            HBox.setHgrow(
                    spacer,
                    Priority.ALWAYS);

            Label statusLabel =
                    new Label(
                            safe(status));

            statusLabel.setStyle(
                    "-fx-text-fill:" +
                            statusColor(status) +
                            ";" +
                            "-fx-font-weight:bold;");

            Button progress =
                    actionButton(
                            "IN PROGRESS",
                            ORANGE);

            Button resolve =
                    actionButton(
                            "RESOLVE",
                            GREEN);

            Button reject =
                    actionButton(
                            "REJECT",
                            RED);

            String studentId =
                    value(
                            complaint.get(
                                    "studentId"));

            String complaintId =
                    value(
                            complaint.get(
                                    "complaintId"));

            progress.setOnAction(
                    e -> {

                        if (requestDao.updateComplaintStatus(
                                studentId,
                                complaintId,
                                "In Progress")) {

                            rebuild(
                                    "complaints");
                        }
                    });

            resolve.setOnAction(
                    e -> {

                        if (requestDao.updateComplaintStatus(
                                studentId,
                                complaintId,
                                "Resolved")) {

                            rebuild(
                                    "complaints");
                        }
                    });

            reject.setOnAction(
                    e -> {

                        if (requestDao.updateComplaintStatus(
                                studentId,
                                complaintId,
                                "Rejected")) {

                            rebuild(
                                    "complaints");
                        }
                    });

            row.getChildren().addAll(
                    info,
                    spacer,
                    statusLabel,
                    progress,
                    resolve,
                    reject);

            panel.getChildren().add(row);
        }

        if (complaints.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No complaints found."));
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(
                scroll);

        return content;
    }

    private static VBox createLeavePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Leave Requests",
                        "Approve or reject student leave requests."));

        VBox panel =
                whitePanel();

        List<Map<String, Object>> leaves =
                requestDao.getAllLeaveRequests();

        for (Map<String, Object> leave :
                leaves) {

            String status =
                    value(
                            leave.get(
                                    "status"));

            HBox row =
                    new HBox(15);

            row.setAlignment(
                    Pos.CENTER_LEFT);

            row.setPadding(
                    new Insets(
                            14,
                            0,
                            14,
                            0));

            VBox info =
                    new VBox(4);

            Label student =
                    new Label(
                            safe(
                                    value(
                                            leave.get(
                                                    "studentName"))));

            student.setFont(
                    Font.font(
                            "Segoe UI",
                            FontWeight.BOLD,
                            14));

            student.setStyle(
                    "-fx-text-fill:" +
                            TEXT +
                            ";");

            Label details =
                    new Label(
                            value(
                                    leave.get(
                                            "leaveType"))
                                    +
                                    " • " +
                                    value(
                                            leave.get(
                                                    "fromDate"))
                                    +
                                    " to " +
                                    value(
                                            leave.get(
                                                    "toDate"))
                                    +
                                    " • " +
                                    value(
                                            leave.get(
                                                    "reason")));

            details.setWrapText(true);

            details.setStyle(
                    "-fx-text-fill:" +
                            MUTED +
                            ";");

            info.getChildren().addAll(
                    student,
                    details);

            Region spacer =
                    new Region();

            HBox.setHgrow(
                    spacer,
                    Priority.ALWAYS);

            Label statusLabel =
                    new Label(
                            safe(status));

            statusLabel.setStyle(
                    "-fx-text-fill:" +
                            statusColor(status) +
                            ";" +
                            "-fx-font-weight:bold;");

            Button approve =
                    actionButton(
                            "APPROVE",
                            GREEN);

            Button reject =
                    actionButton(
                            "REJECT",
                            RED);

            String studentId =
                    value(
                            leave.get(
                                    "studentId"));

            String leaveId =
                    value(
                            leave.get(
                                    "leaveId"));

            approve.setOnAction(
                    e -> {

                        if (requestDao.updateLeaveStatus(
                                studentId,
                                leaveId,
                                "Approved")) {

                            rebuild("leave");
                        }
                    });

            reject.setOnAction(
                    e -> {

                        if (requestDao.updateLeaveStatus(
                                studentId,
                                leaveId,
                                "Rejected")) {

                            rebuild("leave");
                        }
                    });

            row.getChildren().addAll(
                    info,
                    spacer,
                    statusLabel,
                    approve,
                    reject);

            panel.getChildren().add(row);
        }

        if (leaves.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No leave requests found."));
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(
                scroll);

        return content;
    }

    private static VBox createNoticesPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        HBox headingRow =
                new HBox();

        headingRow.setAlignment(
                Pos.CENTER_LEFT);

        VBox heading =
                pageHeading(
                        "Notice Management",
                        "Post, update and delete hostel notices.");

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Button post =
                actionButton(
                        "➕ POST NOTICE",
                        PRIMARY);

        post.setOnAction(
                e ->
                        showAddNoticeDialog());

        headingRow.getChildren().addAll(
                heading,
                spacer,
                post);

        VBox panel =
                whitePanel();

        List<Map<String, Object>> notices =
                noticeDao.getNotices();

        for (Map<String, Object> notice :
                notices) {

            HBox row =
                    new HBox(15);

            row.setAlignment(
                    Pos.CENTER_LEFT);

            row.setPadding(
                    new Insets(
                            14,
                            0,
                            14,
                            0));

            VBox info =
                    new VBox(4);

            Label title =
                    new Label(
                            value(
                                    notice.get(
                                            "title")));

            title.setFont(
                    Font.font(
                            "Segoe UI",
                            FontWeight.BOLD,
                            14));

            title.setStyle(
                    "-fx-text-fill:" +
                            TEXT +
                            ";");

            Label message =
                    new Label(
                            value(
                                    notice.get(
                                            "message")));

            message.setWrapText(true);

            message.setStyle(
                    "-fx-text-fill:" +
                            MUTED +
                            ";");

            info.getChildren().addAll(
                    title,
                    message);

            Region spacer2 =
                    new Region();

            HBox.setHgrow(
                    spacer2,
                    Priority.ALWAYS);

            String noticeId =
                    value(
                            notice.get(
                                    "noticeId"));

            Button edit =
                    actionButton(
                            "EDIT",
                            BLUE);

            Button delete =
                    actionButton(
                            "DELETE",
                            RED);

            edit.setOnAction(
                    e ->
                            showEditNoticeDialog(
                                    noticeId,
                                    value(
                                            notice.get(
                                                    "title")),
                                    value(
                                            notice.get(
                                                    "message"))));

            delete.setOnAction(
                    e -> {

                        Alert alert =
                                new Alert(
                                        Alert.AlertType.CONFIRMATION);

                        alert.setTitle(
                                "Delete Notice");

                        alert.setHeaderText(
                                "Delete this notice?");

                        alert.showAndWait()
                                .ifPresent(
                                        result -> {

                                            if (result ==
                                                    ButtonType.OK) {

                                                if (noticeDao.deleteNotice(
                                                        noticeId)) {

                                                    rebuild(
                                                            "notices");
                                                }
                                            }
                                        });
                    });

            row.getChildren().addAll(
                    info,
                    spacer2,
                    edit,
                    delete);

            panel.getChildren().add(row);
        }

        if (notices.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No notices available."));
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().addAll(
                headingRow,
                scroll);

        return content;
    }

    private static VBox createMessPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Mess Menu Management",
                        "Update today's breakfast, lunch, snacks and dinner."));

        VBox panel =
                whitePanel();

        Map<String, Object> menu =
                messMenuDao.getTodayMenu();

        if (menu == null) {
            menu = new LinkedHashMap<>();
        }

        TextField breakfast =
                inputField(
                        "Breakfast");

        TextField lunch =
                inputField(
                        "Lunch");

        TextField snacks =
                inputField(
                        "Snacks");

        TextField dinner =
                inputField(
                        "Dinner");

        breakfast.setText(
                value(
                        menu.get(
                                "breakfast")));

        lunch.setText(
                value(
                        menu.get(
                                "lunch")));

        snacks.setText(
                value(
                        menu.get(
                                "snacks")));

        dinner.setText(
                value(
                        menu.get(
                                "dinner")));

        panel.getChildren().addAll(
                sectionTitle(
                        "🍽 Today's Menu"),
                breakfast,
                lunch,
                snacks,
                dinner);

        Button save =
                actionButton(
                        "SAVE MENU",
                        GREEN);

        save.setOnAction(
                e -> {

                    String breakfastValue =
                            breakfast.getText()
                                    .trim();

                    String lunchValue =
                            lunch.getText()
                                    .trim();

                    String snacksValue =
                            snacks.getText()
                                    .trim();

                    String dinnerValue =
                            dinner.getText()
                                    .trim();

                    if (breakfastValue.isEmpty() &&
                            lunchValue.isEmpty() &&
                            snacksValue.isEmpty() &&
                            dinnerValue.isEmpty()) {

                        showMessage(
                                "Please enter at least one menu item.");

                        return;
                    }

                    boolean saved =
                            messMenuDao.saveTodayMenu(
                                    breakfastValue,
                                    lunchValue,
                                    snacksValue,
                                    dinnerValue,
                                    wardenUid);

                    if (saved) {

                        showMessage(
                                "Mess menu updated successfully.");

                        rebuild("mess");

                    } else {

                        showMessage(
                                "Unable to update mess menu.");
                    }
                });

        panel.getChildren().add(save);

        content.getChildren().add(panel);

        return content;
    }

    private static VBox createEmergencyPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Emergency Management",
                        "Monitor and manage urgent student requests."));

        VBox panel =
                whitePanel();

        List<Map<String, Object>> emergencies =
                requestDao.getAllEmergencies();

        for (Map<String, Object> emergency :
                emergencies) {

            String status =
                    value(
                            emergency.get(
                                    "status"));

            HBox row =
                    new HBox(15);

            row.setAlignment(
                    Pos.CENTER_LEFT);

            row.setPadding(
                    new Insets(
                            14,
                            0,
                            14,
                            0));

            VBox info =
                    new VBox(4);

            Label student =
                    new Label(
                            "🚨 " +
                                    safe(
                                            value(
                                                    emergency.get(
                                                            "studentName"))));

            student.setFont(
                    Font.font(
                            "Segoe UI",
                            FontWeight.BOLD,
                            14));

            student.setStyle(
                    "-fx-text-fill:" +
                            TEXT +
                            ";");

            Label details =
                    new Label(
                            value(
                                    emergency.get(
                                            "emergencyType"))
                                    +
                                    " • " +
                                    value(
                                            emergency.get(
                                                    "description")));

            details.setWrapText(true);

            details.setStyle(
                    "-fx-text-fill:" +
                            MUTED +
                            ";");

            info.getChildren().addAll(
                    student,
                    details);

            Region spacer =
                    new Region();

            HBox.setHgrow(
                    spacer,
                    Priority.ALWAYS);

            Label statusLabel =
                    new Label(
                            safe(status));

            statusLabel.setStyle(
                    "-fx-text-fill:" +
                            statusColor(status) +
                            ";" +
                            "-fx-font-weight:bold;");

            Button acknowledge =
                    actionButton(
                            "ACKNOWLEDGE",
                            ORANGE);

            Button resolve =
                    actionButton(
                            "RESOLVE",
                            GREEN);

            String studentId =
                    value(
                            emergency.get(
                                    "studentId"));

            String emergencyId =
                    value(
                            emergency.get(
                                    "emergencyId"));

            acknowledge.setOnAction(
                    e -> {

                        if (requestDao.updateEmergencyStatus(
                                studentId,
                                emergencyId,
                                "Acknowledged")) {

                            rebuild("emergency");
                        }
                    });

            resolve.setOnAction(
                    e -> {

                        if (requestDao.updateEmergencyStatus(
                                studentId,
                                emergencyId,
                                "Resolved")) {

                            rebuild("emergency");
                        }
                    });

            row.getChildren().addAll(
                    info,
                    spacer,
                    statusLabel,
                    acknowledge,
                    resolve);

            panel.getChildren().add(row);
        }

        if (emergencies.isEmpty()) {

            panel.getChildren().add(
                    emptyLabel(
                            "No emergency requests found."));
        }

        ScrollPane scroll =
                createScroll(panel);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        content.getChildren().add(
                scroll);

        return content;
    }

    private static VBox createProfilePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(28));

        content.getChildren().add(
                pageHeading(
                        "Warden Profile",
                        "Your HostelHub warden profile."));

        VBox panel =
                whitePanel();

        if (warden != null) {

            panel.getChildren().addAll(
                    profileRow(
                            "Full Name",
                            warden.getFullName()),
                    profileRow(
                            "Email",
                            warden.getEmail()),
                    profileRow(
                            "Phone",
                            warden.getPhone()),
                    profileRow(
                            "Role",
                            warden.getRole()),
                    profileRow(
                            "Warden UID",
                            warden.getUid()));

        } else {

            panel.getChildren().add(
                    emptyLabel(
                            "Warden profile not found."));
        }

        content.getChildren().add(panel);

        return content;
    }

    private static VBox pageHeading(
            String title,
            String subtitle) {

        VBox box =
                new VBox(5);

        Label titleLabel =
                new Label(title);

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        25));

        titleLabel.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        Label subtitleLabel =
                new Label(subtitle);

        subtitleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        13));

        subtitleLabel.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        box.getChildren().addAll(
                titleLabel,
                subtitleLabel);

        return box;
    }

    private static Label sectionTitle(
            String title) {

        Label label =
                new Label(title);

        label.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        16));

        label.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        return label;
    }

    private static VBox whitePanel() {

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20));

        box.setStyle(
                "-fx-background-color:" +
                        CARD +
                        ";" +
                        "-fx-background-radius:14;" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:14;");

        return box;
    }

    private static VBox emptyPanel(
            String text) {

        VBox box =
                whitePanel();

        box.setAlignment(
                Pos.CENTER);

        box.getChildren().add(
                emptyLabel(text));

        return box;
    }

    private static Label emptyLabel(
            String text) {

        Label label =
                new Label(text);

        label.setFont(
                Font.font(
                        "Segoe UI",
                        13));

        label.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        label.setPadding(
                new Insets(15));

        return label;
    }

    private static HBox requestRow(
            String icon,
            String title,
            String message,
            String color) {

        HBox row =
                new HBox(12);

        row.setAlignment(
                Pos.CENTER_LEFT);

        row.setPadding(
                new Insets(
                        10,
                        0,
                        10,
                        0));

        Label iconLabel =
                new Label(icon);

        iconLabel.setPadding(
                new Insets(8));

        iconLabel.setStyle(
                "-fx-background-color:" +
                        color +
                        "20;" +
                        "-fx-background-radius:9;");

        VBox text =
                new VBox(3);

        Label titleLabel =
                new Label(
                        safe(title));

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        13));

        titleLabel.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        Label messageLabel =
                new Label(
                        safe(message));

        messageLabel.setWrapText(true);

        messageLabel.setFont(
                Font.font(
                        "Segoe UI",
                        11));

        messageLabel.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        text.getChildren().addAll(
                titleLabel,
                messageLabel);

        row.getChildren().addAll(
                iconLabel,
                text);

        return row;
    }

    private static HBox announcementRow(
            String title,
            String message) {

        HBox row =
                new HBox(12);

        row.setAlignment(
                Pos.CENTER_LEFT);

        row.setPadding(
                new Insets(
                        10,
                        0,
                        10,
                        0));

        Label icon =
                new Label("📢");

        icon.setPadding(
                new Insets(8));

        icon.setStyle(
                "-fx-background-color:" +
                        ICON_BG +
                        ";" +
                        "-fx-background-radius:9;");

        VBox text =
                new VBox(3);

        Label titleLabel =
                new Label(
                        safe(title));

        titleLabel.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        13));

        titleLabel.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        Label messageLabel =
                new Label(
                        safe(message));

        messageLabel.setWrapText(true);

        messageLabel.setFont(
                Font.font(
                        "Segoe UI",
                        11));

        messageLabel.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        text.getChildren().addAll(
                titleLabel,
                messageLabel);

        row.getChildren().addAll(
                icon,
                text);

        return row;
    }

    private static HBox profileRow(
            String title,
            String value) {

        HBox row =
                new HBox(20);

        row.setPadding(
                new Insets(
                        12,
                        0,
                        12,
                        0));

        Label key =
                new Label(title);

        key.setPrefWidth(130);

        key.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        13));

        key.setStyle(
                "-fx-text-fill:" +
                        MUTED +
                        ";");

        Label valueLabel =
                new Label(
                        safe(value));

        valueLabel.setFont(
                Font.font(
                        "Segoe UI",
                        14));

        valueLabel.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");

        valueLabel.setWrapText(true);

        row.getChildren().addAll(
                key,
                valueLabel);

        return row;
    }

    private static Button actionButton(
            String text,
            String color) {

        Button button =
                new Button(text);

        button.setPadding(
                new Insets(
                        9,
                        15,
                        9,
                        15));

        button.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11));

        button.setStyle(
                "-fx-background-color:" +
                        color +
                        ";" +
                        "-fx-text-fill:white;" +
                        "-fx-background-radius:8;" +
                        "-fx-cursor:hand;");

        button.setOnMouseEntered(
                e ->
                        button.setOpacity(
                                0.82));

        button.setOnMouseExited(
                e ->
                        button.setOpacity(
                                1));

        return button;
    }

    private static TextField inputField(
            String prompt) {

        TextField field =
                new TextField();

        field.setPromptText(prompt);

        field.setPrefHeight(43);

        field.setStyle(
                "-fx-background-color:" +
                        INPUT_BG +
                        ";" +
                        "-fx-control-inner-background:" +
                        INPUT_BG +
                        ";" +
                        "-fx-text-fill:" +
                        TEXT +
                        ";" +
                        "-fx-prompt-text-fill:" +
                        PROMPT +
                        ";" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:9;" +
                        "-fx-background-radius:9;" +
                        "-fx-padding:0 12;");

        return field;
    }

    private static ScrollPane createScroll(
            VBox content) {

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setStyle(
                "-fx-background-color:transparent;" +
                        "-fx-background:transparent;" +
                        "-fx-border-color:transparent;");

        return scroll;
    }

    private static void styleBodyLabel(
            Label label) {

        label.setFont(
                Font.font(
                        "Segoe UI",
                        13));

        label.setStyle(
                "-fx-text-fill:" +
                        TEXT +
                        ";");
    }

    private static int countPending(
            List<Map<String, Object>> list) {

        if (list == null) {
            return 0;
        }

        int count = 0;

        for (Map<String, Object> item :
                list) {

            if (isPending(
                    value(
                            item.get(
                                    "status")))) {

                count++;
            }
        }

        return count;
    }

    private static boolean isPending(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return true;
        }

        String value =
                status.trim()
                        .toUpperCase();

        return value.equals("PENDING") ||
                value.equals("IN PROGRESS") ||
                value.equals("SUBMITTED") ||
                value.equals("ACKNOWLEDGED");
    }

    private static String statusColor(
            String status) {

        if (status == null) {
            return ORANGE;
        }

        String value =
                status.toUpperCase();

        if (value.contains("APPROVED") ||
                value.contains("RESOLVED") ||
                value.contains("COMPLETED") ||
                value.equals("PAID")) {

            return GREEN;
        }

        if (value.contains("REJECTED")) {

            return RED;
        }

        if (value.contains("ACKNOWLEDGED") ||
                value.contains("PARTIALLY")) {

            return ORANGE;
        }

        return PRIMARY;
    }

    private static String rupee(
            Object value) {

        if (value == null) {
            return "₹0";
        }

        try {

            double amount =
                    Double.parseDouble(
                            String.valueOf(value));

            return String.format(
                    "₹%,.0f",
                    amount);

        } catch (Exception e) {

            return "₹" +
                    String.valueOf(value);
        }
    }

    private static String value(
            Object object) {

        return object == null
                ? ""
                : String.valueOf(object);
    }

    private static String safe(
            String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return "Not Available";
        }

        return value;
    }

    private static void showMessage(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION);

        alert.setTitle(
                "HostelHub");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    private static void showEditStudentDialog(
            Student student) {

        if (student == null) {
            return;
        }

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Student");

        dialog.setHeaderText(
                "Update Student Details");

        ButtonType save =
                new ButtonType(
                        "SAVE",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        ButtonType.CANCEL);

        VBox box =
                new VBox(10);

        TextField name =
                inputField(
                        "Full Name");

        name.setText(
                value(
                        student.getFullName()));

        TextField parent =
                inputField(
                        "Parent Name");

        parent.setText(
                value(
                        student.getParentName()));

        TextField phone =
                inputField(
                        "Phone");

        phone.setText(
                value(
                        student.getPhone()));

        TextField email =
                inputField(
                        "Email");

        email.setText(
                value(
                        student.getEmail()));

        TextField college =
                inputField(
                        "College");

        college.setText(
                value(
                        student.getCollege()));

        TextField city =
                inputField(
                        "City");

        city.setText(
                value(
                        student.getCity()));

        TextField course =
                inputField(
                        "Course");

        course.setText(
                value(
                        student.getCourse()));

        TextField year =
                inputField(
                        "Year");

        year.setText(
                value(
                        student.getYear()));

        box.getChildren().addAll(
                name,
                parent,
                phone,
                email,
                college,
                city,
                course,
                year);

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(
                button -> {

                    if (button == save) {

                        boolean updated =
                                studentDao.updateStudent(
                                        student.getUid(),
                                        name.getText().trim(),
                                        parent.getText().trim(),
                                        student.getDateOfBirth(),
                                        student.getGender(),
                                        course.getText().trim(),
                                        year.getText().trim(),
                                        phone.getText().trim(),
                                        email.getText().trim(),
                                        college.getText().trim(),
                                        city.getText().trim(),
                                        student.getRoomPreference(),
                                        student.getAddress(),
                                        student.getRoomNumber());

                        if (updated) {

                            showMessage(
                                    "Student updated successfully.");

                            rebuild("students");

                        } else {

                            showMessage(
                                    "Unable to update student.");
                        }
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static void showRoomDialog(
            Student student) {

        if (student == null) {
            return;
        }

        javafx.scene.control.TextInputDialog dialog =
                new javafx.scene.control.TextInputDialog(
                        value(
                                student.getRoomNumber()));

        dialog.setTitle(
                "Room Allocation");

        dialog.setHeaderText(
                "Allocate Room to " +
                        safe(
                                student.getFullName()));

        dialog.setContentText(
                "Room Number:");

        dialog.showAndWait()
                .ifPresent(
                        room -> {

                            room =
                                    room.trim();

                            if (room.isEmpty()) {

                                showMessage(
                                        "Please enter room number.");

                                return;
                            }

                            boolean updated =
                                    studentDao.updateRoomNumber(
                                            student.getUid(),
                                            room);

                            if (updated) {

                                showMessage(
                                        "Room allocated successfully.");

                                rebuild("rooms");

                            } else {

                                showMessage(
                                        "Unable to allocate room.");
                            }
                        });
    }

    private static void deleteStudent(
            Student student) {

        if (student == null ||
                student.getUid() == null) {

            return;
        }

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION);

        alert.setTitle(
                "Delete Student");

        alert.setHeaderText(
                "Delete " +
                        safe(
                                student.getFullName())
                        +
                        "?");

        alert.setContentText(
                "This student record will be permanently deleted.");

        alert.showAndWait()
                .ifPresent(
                        result -> {

                            if (result ==
                                    ButtonType.OK) {

                                boolean deleted =
                                        studentDao.deleteStudent(
                                                student.getUid());

                                if (deleted) {

                                    showMessage(
                                            "Student deleted successfully.");

                                    rebuild("students");

                                } else {

                                    showMessage(
                                            "Unable to delete student.");
                                }
                            }
                        });
    }

    private static void showPaymentDialog(
            String uid,
            String studentName,
            double remaining) {

        if (remaining <= 0) {

            showMessage(
                    "This student's fee is already fully paid.");

            return;
        }

        javafx.scene.control.TextInputDialog dialog =
                new javafx.scene.control.TextInputDialog();

        dialog.setTitle(
                "Record Fee Payment");

        dialog.setHeaderText(
                "Payment for " +
                        safe(studentName));

        dialog.setContentText(
                "Remaining: " +
                        rupee(remaining) +
                        "\nEnter payment amount:");

        dialog.showAndWait()
                .ifPresent(
                        text -> {

                            try {

                                double amount =
                                        Double.parseDouble(
                                                text.trim());

                                if (amount <= 0) {

                                    showMessage(
                                            "Payment must be greater than ₹0.");

                                    return;
                                }

                                if (amount > remaining) {

                                    showMessage(
                                            "Payment cannot exceed remaining fee.");

                                    return;
                                }

                                boolean saved =
                                        studentDao.updateFeePayment(
                                                uid,
                                                amount);

                                if (saved) {

                                    showMessage(
                                            "Payment recorded successfully.");

                                    rebuild("fees");

                                } else {

                                    showMessage(
                                            "Unable to record payment.");
                                }

                            } catch (
                                    NumberFormatException ex) {

                                showMessage(
                                        "Please enter a valid amount.");
                            }
                        });
    }

    private static void showAddNoticeDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Add Notice");

        dialog.setHeaderText(
                "Create Hostel Notice");

        ButtonType save =
                new ButtonType(
                        "POST",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        ButtonType.CANCEL);

        VBox box =
                new VBox(10);

        TextField title =
                inputField(
                        "Notice title");

        TextArea message =
                new TextArea();

        message.setPromptText(
                "Notice message");

        message.setPrefRowCount(6);

        message.setWrapText(true);

        message.setStyle(
                "-fx-background-color:" +
                        INPUT_BG +
                        ";" +
                        "-fx-control-inner-background:" +
                        INPUT_BG +
                        ";" +
                        "-fx-text-fill:" +
                        TEXT +
                        ";" +
                        "-fx-prompt-text-fill:" +
                        PROMPT +
                        ";" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:9;" +
                        "-fx-background-radius:9;");

        box.getChildren().addAll(
                title,
                message);

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(
                button -> {

                    if (button == save) {

                        if (title.getText()
                                .trim()
                                .isEmpty()
                                ||
                                message.getText()
                                        .trim()
                                        .isEmpty()) {

                            showMessage(
                                    "Please enter title and message.");

                            return null;
                        }

                        boolean saved =
                                noticeDao.addNotice(
                                        title.getText()
                                                .trim(),
                                        message.getText()
                                                .trim(),
                                        wardenUid);

                        if (saved) {

                            showMessage(
                                    "Notice posted successfully.");

                            rebuild("notices");

                        } else {

                            showMessage(
                                    "Unable to post notice.");
                        }
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static void showEditNoticeDialog(
            String noticeId,
            String oldTitle,
            String oldMessage) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Notice");

        dialog.setHeaderText(
                "Update Hostel Notice");

        ButtonType update =
                new ButtonType(
                        "UPDATE",
                        javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        update,
                        ButtonType.CANCEL);

        VBox box =
                new VBox(10);

        TextField title =
                inputField(
                        "Notice title");

        title.setText(oldTitle);

        TextArea message =
                new TextArea(
                        oldMessage);

        message.setPrefRowCount(6);

        message.setWrapText(true);

        message.setStyle(
                "-fx-background-color:" +
                        INPUT_BG +
                        ";" +
                        "-fx-control-inner-background:" +
                        INPUT_BG +
                        ";" +
                        "-fx-text-fill:" +
                        TEXT +
                        ";" +
                        "-fx-prompt-text-fill:" +
                        PROMPT +
                        ";" +
                        "-fx-border-color:" +
                        BORDER +
                        ";" +
                        "-fx-border-radius:9;" +
                        "-fx-background-radius:9;");

        box.getChildren().addAll(
                title,
                message);

        dialog.getDialogPane()
                .setContent(box);

        dialog.setResultConverter(
                button -> {

                    if (button == update) {

                        if (title.getText()
                                .trim()
                                .isEmpty()
                                ||
                                message.getText()
                                        .trim()
                                        .isEmpty()) {

                            showMessage(
                                    "Please enter title and message.");

                            return null;
                        }

                        boolean updated =
                                noticeDao.updateNotice(
                                        noticeId,
                                        title.getText()
                                                .trim(),
                                        message.getText()
                                                .trim());

                        if (updated) {

                            showMessage(
                                    "Notice updated successfully.");

                            rebuild("notices");

                        } else {

                            showMessage(
                                    "Unable to update notice.");
                        }
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static Map<String, Object> getStudentFeeData(
            String uid) {

        Map<String, Object> fee =
                new LinkedHashMap<>();

        try {

            DocumentSnapshot student =
                    FirebaseConfig
                            .getFireStore()
                            .collection("students")
                            .document(uid)
                            .get()
                            .get();

            if (student.exists()) {

                Object total =
                        student.get(
                                "totalFee");

                Object paid =
                        student.get(
                                "paidFee");

                Object remaining =
                        student.get(
                                "remainingFee");

                Object status =
                        student.get(
                                "feeStatus");

                if (total != null) {

                    fee.put(
                            "totalFee",
                            total);
                }

                if (paid != null) {

                    fee.put(
                            "paidFee",
                            paid);
                }

                if (remaining != null) {

                    fee.put(
                            "remainingFee",
                            remaining);
                }

                if (status != null) {

                    fee.put(
                            "feeStatus",
                            status);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return fee;
    }

    private static double getFeeNumber(
            Object value) {

        if (value == null) {
            return 0.0;
        }

        try {

            return Double.parseDouble(
                    String.valueOf(value));

        } catch (Exception e) {

            return 0.0;
        }
    }
}