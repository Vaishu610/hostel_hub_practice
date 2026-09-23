package com.hostelhub.view;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.MessMenuDao;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentDashboard {

    private static BorderPane root;
    private static String parentUid;
    private static DocumentSnapshot parent;
    private static DocumentSnapshot student;

    private static boolean darkTheme = false;

    private static String displayName;
    private static String currentPage = "dashboard";

    private static final List<ListenerRegistration> realtimeListeners =
            new ArrayList<>();

    private static final MessMenuDao messMenuDao =
            new MessMenuDao();

    private static final String PRIMARY = "#4F46E5";
    private static final String PRIMARY_DARK = "#3730A3";

    private static final String GREEN = "#16A34A";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#EA580C";
    private static final String BLUE = "#2563EB";

    private static String bg() {
        return darkTheme ? "#0F172A" : "#F4F6FB";
    }

    private static String panel() {
        return darkTheme ? "#1E293B" : "#FFFFFF";
    }

    private static String card() {
        return darkTheme ? "#1E293B" : "#FFFFFF";
    }

    private static String border() {
        return darkTheme ? "#334155" : "#E5E7EB";
    }

    private static String text() {
        return darkTheme ? "#F8FAFC" : "#111827";
    }

    private static String secondary() {
        return darkTheme ? "#CBD5E1" : "#374151";
    }

    private static String muted() {
        return darkTheme ? "#94A3B8" : "#6B7280";
    }

    private static String inputBg() {
        return darkTheme ? "#0F172A" : "#F9FAFB";
    }

    private static String softBg() {
        return darkTheme ? "#1E293B" : "#F3F4F6";
    }

    private static String iconBg() {
        return darkTheme ? "#312E81" : "#EEF2FF";
    }

    public static void show(String uid) {

        parentUid = uid;

        removeRealtimeListeners();

        try {

            parent = FirebaseConfig
                    .getFireStore()
                    .collection("parents")
                    .document(uid)
                    .get()
                    .get();

            if (!parent.exists()) {
                showMessage("Parent profile not found.");
                return;
            }

            displayName = safeObject(
                    parent.getData().get("fullName"),
                    "Parent");

            loadLinkedStudent();

            root = new BorderPane();

            rebuild("dashboard");

            Scene scene = new Scene(
                    root,
                    1550,
                    800);

            Stage stage = Welcome.stage;

            stage.setTitle(
                    "Hostel Hub - Parent Dashboard");

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            attachRealtimeListeners();

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Unable to load parent dashboard.");
        }
    }

    private static void loadLinkedStudent() {

        student = null;

        try {

            if (parent == null || !parent.exists()) {
                return;
            }

            String studentId = safeObject(
                    parent.getData().get("studentId"),
                    "");

            if (studentId.isEmpty()) {
                return;
            }

            DocumentSnapshot byUid = FirebaseConfig
                    .getFireStore()
                    .collection("students")
                    .document(studentId)
                    .get()
                    .get();

            if (byUid.exists()) {

                student = byUid;
                return;
            }

            QuerySnapshot snapshot = FirebaseConfig
                    .getFireStore()
                    .collection("students")
                    .whereEqualTo(
                            "uid",
                            studentId)
                    .get()
                    .get();

            if (!snapshot.isEmpty()) {

                student = snapshot
                        .getDocuments()
                        .get(0);
            }

        } catch (Exception e) {

            student = null;
        }
    }

    private static void attachRealtimeListeners() {

        removeRealtimeListeners();

        try {

            ListenerRegistration parentListener =
                    FirebaseConfig
                            .getFireStore()
                            .collection("parents")
                            .document(parentUid)
                            .addSnapshotListener(
                                    (snapshot, error) -> {

                                        if (error != null ||
                                                snapshot == null ||
                                                !snapshot.exists()) {
                                            return;
                                        }

                                        parent = snapshot;

                                        displayName = safeObject(
                                                snapshot.getData()
                                                        .get("fullName"),
                                                "Parent");

                                        loadLinkedStudent();

                                        refreshUI();
                                    });

            realtimeListeners.add(
                    parentListener);

            if (student != null) {

                String studentUid =
                        student.getId();

                ListenerRegistration studentListener =
                        FirebaseConfig
                                .getFireStore()
                                .collection("students")
                                .document(studentUid)
                                .addSnapshotListener(
                                        (snapshot, error) -> {

                                            if (error != null ||
                                                    snapshot == null) {
                                                return;
                                            }

                                            student = snapshot;

                                            refreshUI();
                                        });

                realtimeListeners.add(
                        studentListener);

                ListenerRegistration attendanceListener =
                        FirebaseConfig
                                .getFireStore()
                                .collection("students")
                                .document(studentUid)
                                .collection("attendance")
                                .addSnapshotListener(
                                        (snapshot, error) -> {

                                            if (error != null) {
                                                return;
                                            }

                                            refreshUI();
                                        });

                realtimeListeners.add(
                        attendanceListener);

                ListenerRegistration paymentListener =
                        FirebaseConfig
                                .getFireStore()
                                .collection("payments")
                                .whereEqualTo(
                                        "studentId",
                                        studentUid)
                                .addSnapshotListener(
                                        (snapshot, error) -> {

                                            if (error != null) {
                                                return;
                                            }

                                            refreshUI();
                                        });

                realtimeListeners.add(
                        paymentListener);
            }

            ListenerRegistration visitListener =
                    FirebaseConfig
                            .getFireStore()
                            .collection("visits")
                            .whereEqualTo(
                                    "parentUid",
                                    parentUid)
                            .addSnapshotListener(
                                    (snapshot, error) -> {

                                        if (error != null) {
                                            return;
                                        }

                                        refreshUI();
                                    });

            realtimeListeners.add(
                    visitListener);

            ListenerRegistration messListener =
                    FirebaseConfig
                            .getFireStore()
                            .collection("messMenu")
                            .addSnapshotListener(
                                    (snapshot, error) -> {

                                        if (error != null) {
                                            return;
                                        }

                                        refreshUI();
                                    });

            realtimeListeners.add(
                    messListener);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void removeRealtimeListeners() {

        for (ListenerRegistration registration :
                realtimeListeners) {

            try {
                registration.remove();
            } catch (Exception ignored) {
            }
        }

        realtimeListeners.clear();
    }

    private static void refreshUI() {

        if (root == null) {
            return;
        }

        Platform.runLater(
                () -> {

                    try {
                        rebuild(currentPage);
                    } catch (Exception ignored) {
                    }
                });
    }

    private static void rebuild(String page) {

        currentPage = page;

        root.setStyle(
                "-fx-background-color:" +
                        bg() +
                        ";");

        root.setTop(createTopBar());
        root.setLeft(createSidebar());

        switch (page) {

            case "profile":
                root.setCenter(
                        createProfilePage());
                break;

            case "student":
                root.setCenter(
                        createStudentPage());
                break;

            case "room":
                root.setCenter(
                        createRoomPage());
                break;

            case "payments":
                root.setCenter(
                        createPaymentsPage());
                break;

            case "attendance":
                root.setCenter(
                        createAttendancePage());
                break;

            case "mess":
                root.setCenter(
                        createMessPage());
                break;

            case "visit":
                root.setCenter(
                        createParentVisitPage());
                break;

            case "visits":
                root.setCenter(
                        createVisitHistoryPage());
                break;

            case "settings":
                root.setCenter(
                        createSettingsPage());
                break;

            case "about":
                root.setCenter(
                        createAboutUsPage());
                break;

            default:
                root.setCenter(
                        createDashboardPage());
                break;
        }
    }

    private static HBox createTopBar() {

        HBox bar = new HBox(20);

        bar.setAlignment(
                Pos.CENTER_LEFT);

        bar.setPadding(
                new Insets(
                        15,
                        30,
                        15,
                        30));

        bar.setStyle(
                "-fx-background-color:" +
                        panel() +
                        ";" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-width:0 0 1 0;");

        Label logo =
                new Label("HOSTEL HUB");

        logo.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        23));

        logo.setTextFill(
                Color.web(PRIMARY));

        Label small =
                new Label(
                        "  |  Parent Portal");

        small.setFont(
                Font.font(
                        "Segoe UI",
                        13));

        small.setTextFill(
                Color.web(muted()));

        HBox logoBox =
                new HBox(
                        logo,
                        small);

        logoBox.setAlignment(
                Pos.CENTER_LEFT);

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        Label profileIcon =
                new Label("👤");

        profileIcon.setFont(
                Font.font(25));

        VBox info =
                new VBox(2);

        info.setAlignment(
                Pos.CENTER_RIGHT);

        Label name =
                new Label(
                        safe(displayName));

        name.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        14));

        name.setTextFill(
                Color.web(text()));

        Label role =
                new Label("Parent");

        role.setFont(
                Font.font(
                        "Segoe UI",
                        11));

        role.setTextFill(
                Color.web(muted()));

        info.getChildren().addAll(
                name,
                role);

        bar.getChildren().addAll(
                logoBox,
                spacer,
                profileIcon,
                info);

        return bar;
    }

    private static VBox createSidebar() {

        VBox side =
                new VBox(6);

        side.setPrefWidth(225);

        side.setPadding(
                new Insets(
                        25,
                        14,
                        20,
                        14));

        side.setStyle(
                darkTheme
                        ? "-fx-background-color:#020617;" +
                                "-fx-border-color:#334155;" +
                                "-fx-border-width:0 1 0 0;"
                        : "-fx-background-color:#111827;" +
                                "-fx-border-color:#E5E7EB;" +
                                "-fx-border-width:0 1 0 0;");

        Label title =
                new Label(
                        "PARENT MENU");

        title.setPadding(
                new Insets(
                        5,
                        12,
                        10,
                        12));

        title.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11));

        title.setTextFill(
                Color.web("#9CA3AF"));

        Button dashboard =
                menuButton(
                        "🏠",
                        "Dashboard");

        Button studentBtn =
                menuButton(
                        "🎓",
                        "My Student");

        Button room =
                menuButton(
                        "🛏",
                        "Room");

        Button payments =
                menuButton(
                        "💳",
                        "Payments");

        Button attendance =
                menuButton(
                        "📊",
                        "Attendance");

        Button mess =
                menuButton(
                        "🍽",
                        "Mess Menu");

        Button visit =
                menuButton(
                        "👨‍👩‍👧",
                        "Parent Visit");

        Button visits =
                menuButton(
                        "📋",
                        "Visit History");

        Button profile =
                menuButton(
                        "👤",
                        "Profile");

        Button about =
                menuButton(
                        "ℹ",
                        "About Us");

        side.getChildren().addAll(
                title,
                dashboard,
                studentBtn,
                room,
                payments,
                attendance,
                mess,
                visit,
                visits,
                profile,
                about);

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS);

        Button settings =
                menuButton(
                        "⚙",
                        "Settings");

        Button logout =
                menuButton(
                        "🚪",
                        "Logout");

        side.getChildren().addAll(
                spacer,
                settings,
                logout);

        Button activeButton = null;

        switch (currentPage) {

            case "dashboard":
                activeButton = dashboard;
                break;

            case "student":
                activeButton = studentBtn;
                break;

            case "room":
                activeButton = room;
                break;

            case "payments":
                activeButton = payments;
                break;

            case "attendance":
                activeButton = attendance;
                break;

            case "mess":
                activeButton = mess;
                break;

            case "visit":
                activeButton = visit;
                break;

            case "visits":
                activeButton = visits;
                break;

            case "profile":
                activeButton = profile;
                break;

            case "about":
                activeButton = about;
                break;

            case "settings":
                activeButton = settings;
                break;

            default:
                break;
        }

        if (activeButton != null) {
            setActiveMenuStyle(
                    activeButton);
        }

        dashboard.setOnAction(
                e -> rebuild("dashboard"));

        studentBtn.setOnAction(
                e -> rebuild("student"));

        room.setOnAction(
                e -> rebuild("room"));

        payments.setOnAction(
                e -> rebuild("payments"));

        attendance.setOnAction(
                e -> rebuild("attendance"));

        mess.setOnAction(
                e -> rebuild("mess"));

        visit.setOnAction(
                e -> rebuild("visit"));

        visits.setOnAction(
                e -> rebuild("visits"));

        profile.setOnAction(
                e -> rebuild("profile"));

        about.setOnAction(
                e -> rebuild("about"));

        settings.setOnAction(
                e -> rebuild("settings"));

        logout.setOnAction(
                e -> {

                    removeRealtimeListeners();

                    ParentLogin.show();
                });

        return side;
    }

    private static ScrollPane createDashboardPage() {

        VBox content =
                new VBox(22);

        content.setPadding(
                new Insets(
                        30,
                        35,
                        40,
                        35));

        String studentName =
                getStudentValue(
                        "fullName",
                        "Student Not Linked");

        content.getChildren().add(
                pageHeading(
                        "Welcome, " +
                                displayName +
                                " 👋",
                        "Here's your child's hostel overview."));

        HBox cards =
                new HBox(18);

        VBox studentCard =
                dashboardCard(
                        "🎓",
                        "STUDENT",
                        studentName,
                        getStudentValue(
                                "uid",
                                "Not Available"),
                        BLUE);

        VBox roomCard =
                dashboardCard(
                        "🛏",
                        "ROOM",
                        getStudentValue(
                                "roomNumber",
                                "Not Assigned"),
                        getStudentValue(
                                "roomPreference",
                                "Not Available"),
                        BLUE);

        Map<String, Object> fees =
                getFeeData();

        double pending =
                getDouble(
                        fees,
                        "pendingFees");

        VBox feeCard =
                dashboardCard(
                        "💳",
                        "PENDING FEES",
                        formatCurrency(pending),
                        pending > 0
                                ? "Payment pending"
                                : "All fees paid",
                        GREEN);

        double attendance =
                calculateAttendancePercentage(
                        getAttendanceData());

        VBox attendanceCard =
                dashboardCard(
                        "📊",
                        "ATTENDANCE",
                        String.format(
                                "%.0f%%",
                                attendance),
                        "Student attendance",
                        GREEN);

        studentCard.setOnMouseClicked(
                e -> rebuild("student"));

        roomCard.setOnMouseClicked(
                e -> rebuild("room"));

        feeCard.setOnMouseClicked(
                e -> rebuild("payments"));

        attendanceCard.setOnMouseClicked(
                e -> rebuild("attendance"));

        cards.getChildren().addAll(
                studentCard,
                roomCard,
                feeCard,
                attendanceCard);

        content.getChildren().add(cards);

        HBox lower =
                new HBox(20);

        VBox visits =
                createRecentVisits();

        HBox.setHgrow(
                visits,
                Priority.ALWAYS);

        lower.getChildren().add(
                visits);

        content.getChildren().add(
                lower);

        content.getChildren().add(
                createTodayMessMenu());

        content.getChildren().add(
                sectionTitle(
                        "Quick Actions"));

        HBox quick =
                new HBox(15);

        Button studentBtn =
                quickButton(
                        "🎓",
                        "Student Details");

        Button feesBtn =
                quickButton(
                        "💳",
                        "Fees");

        Button attendanceBtn =
                quickButton(
                        "📊",
                        "Attendance");

        Button visitBtn =
                quickButton(
                        "👨‍👩‍👧",
                        "Visit Student");

        studentBtn.setOnAction(
                e -> rebuild("student"));

        feesBtn.setOnAction(
                e -> rebuild("payments"));

        attendanceBtn.setOnAction(
                e -> rebuild("attendance"));

        visitBtn.setOnAction(
                e -> rebuild("visit"));

        quick.getChildren().addAll(
                studentBtn,
                feesBtn,
                attendanceBtn,
                visitBtn);

        content.getChildren().add(
                quick);

        return createScroll(content);
    }

    private static VBox createRecentVisits() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "👨‍👩‍👧 Parent Visit Status"));

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("visits")
                            .get()
                            .get();

            List<QueryDocumentSnapshot> list =
                    new ArrayList<>();

            for (QueryDocumentSnapshot doc :
                    snapshot.getDocuments()) {

                String visitParentUid =
                        safeObject(
                                doc.getData()
                                        .get("parentUid"),
                                "");

                if (parentUid.equals(
                        visitParentUid)) {

                    list.add(doc);
                }
            }

            list.sort(
                    Comparator.comparing(
                            doc -> safeObject(
                                    doc.getData()
                                            .get("createdAt"),
                                    ""),
                            Comparator.reverseOrder()));

            if (list.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No parent visit request found."));

            } else {

                QueryDocumentSnapshot latest =
                        list.get(0);

                Map<String, Object> data =
                        latest.getData();

                String studentName =
                        safeObject(
                                data.get("studentName"),
                                "Student");

                String date =
                        safeObject(
                                data.get("visitDate"),
                                "-");

                String time =
                        safeObject(
                                data.get("visitTime"),
                                "-");

                String purpose =
                        safeObject(
                                data.get("purpose"),
                                "-");

                String status =
                        safeObject(
                                data.get("status"),
                                "Waiting");

                String rejectionReason =
                        safeObject(
                                data.get("rejectionReason"),
                                "");

                VBox visitCard =
                        new VBox(10);

                visitCard.setPadding(
                        new Insets(15));

                visitCard.setStyle(
                        "-fx-background-color:" +
                                softBg() +
                                ";" +
                                "-fx-background-radius:12;" +
                                "-fx-border-color:" +
                                border() +
                                ";" +
                                "-fx-border-radius:12;");

                visitCard.getChildren().addAll(

                        pageText(
                                "Student : " +
                                        studentName,
                                14,
                                true,
                                text()),

                        infoLabel(
                                "📅 Date : " +
                                        date),

                        infoLabel(
                                "⏰ Time : " +
                                        time),

                        infoLabel(
                                "📝 Purpose : " +
                                        purpose),

                        statusBadge(status));

                if ("Approved".equalsIgnoreCase(
                        status)) {

                    Label message =
                            statusMessage(
                                    "✅ Visit Approved by Warden",
                                    GREEN,
                                    darkTheme
                                            ? "#14532D"
                                            : "#DCFCE7");

                    visitCard.getChildren().add(
                            message);
                }

                if ("Rejected".equalsIgnoreCase(
                        status)) {

                    Label message =
                            statusMessage(
                                    "❌ Visit Rejected by Warden",
                                    RED,
                                    darkTheme
                                            ? "#7F1D1D"
                                            : "#FEE2E2");

                    visitCard.getChildren().add(
                            message);

                    if (!rejectionReason
                            .trim()
                            .isEmpty()) {

                        Label reason =
                                statusMessage(
                                        "❌ Reason : " +
                                                rejectionReason,
                                        darkTheme
                                                ? "#FDA4AF"
                                                : "#991B1B",
                                        darkTheme
                                                ? "#4C0519"
                                                : "#FFF1F2");

                        visitCard.getChildren().add(
                                reason);
                    }
                }

                if ("Waiting".equalsIgnoreCase(
                        status)) {

                    Label message =
                            statusMessage(
                                    "⏳ Waiting for Warden Approval",
                                    darkTheme
                                            ? "#A5B4FC"
                                            : PRIMARY,
                                    darkTheme
                                            ? "#312E81"
                                            : "#EEF2FF");

                    visitCard.getChildren().add(
                            message);
                }

                box.getChildren().add(
                        visitCard);
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load parent visit status."));
        }

        Button view =
                secondaryButton(
                        "VIEW VISIT HISTORY");

        view.setOnAction(
                e -> rebuild("visits"));

        box.getChildren().add(
                view);

        return box;
    }

    private static VBox createTodayMessMenu() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "🍽 Today's Mess Menu"));

        try {

            Map<String, Object> menu =
                    messMenuDao.getTodayMenu();

            if (menu == null ||
                    menu.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "Today's mess menu is not available."));

            } else {

                box.getChildren().addAll(

                        infoLabel(
                                "🌅 Breakfast : " +
                                        safeObject(
                                                menu.get("breakfast"),
                                                "Not Available")),

                        infoLabel(
                                "🍛 Lunch : " +
                                        safeObject(
                                                menu.get("lunch"),
                                                "Not Available")),

                        infoLabel(
                                "☕ Snacks : " +
                                        safeObject(
                                                menu.get("snacks"),
                                                "Not Available")),

                        infoLabel(
                                "🌙 Dinner : " +
                                        safeObject(
                                                menu.get("dinner"),
                                                "Not Available")));
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load today's mess menu."));
        }

        Button view =
                secondaryButton(
                        "VIEW MESS MENU");

        view.setOnAction(
                e -> rebuild("mess"));

        box.getChildren().add(
                view);

        return box;
    }

    private static ScrollPane createStudentPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        content.getChildren().add(
                pageHeading(
                        "🎓 Student Information",
                        "Complete details of the student linked to your parent account."));

        if (student == null) {

            content.getChildren().add(
                    whitePanelWithMessage(
                            "No student is linked to this parent account."));

            return createScroll(content);
        }

        content.getChildren().add(
                infoPanel(
                        "Personal Information",

                        "Full Name : " +
                                getStudentValue(
                                        "fullName",
                                        "Not Available"),

                        "Student ID : " +
                                getStudentValue(
                                        "uid",
                                        "Not Available"),

                        "Parent Name : " +
                                getStudentValue(
                                        "parentName",
                                        "Not Available"),

                        "Date of Birth : " +
                                getStudentValue(
                                        "dateOfBirth",
                                        "Not Available"),

                        "Gender : " +
                                getStudentValue(
                                        "gender",
                                        "Not Available"),

                        "Phone : " +
                                getStudentValue(
                                        "phone",
                                        "Not Available"),

                        "Email : " +
                                getStudentValue(
                                        "email",
                                        "Not Available")));

        content.getChildren().add(
                infoPanel(
                        "Education Information",

                        "Course : " +
                                getStudentValue(
                                        "course",
                                        "Not Available"),

                        "Year : " +
                                getStudentValue(
                                        "year",
                                        "Not Available"),

                        "College : " +
                                getStudentValue(
                                        "college",
                                        "Not Available"),

                        "City : " +
                                getStudentValue(
                                        "city",
                                        "Not Available")));

        content.getChildren().add(
                infoPanel(
                        "Hostel Information",

                        "Room Number : " +
                                getStudentValue(
                                        "roomNumber",
                                        "Not Assigned"),

                        "Room Preference : " +
                                getStudentValue(
                                        "roomPreference",
                                        "Not Available"),

                        "Mess Plan : " +
                                getStudentValue(
                                        "messPlan",
                                        "Not Available"),

                        "Address : " +
                                getStudentValue(
                                        "address",
                                        "Not Available")));

        content.getChildren().add(
                infoPanel(
                        "Fee Information",

                        "Total Fee : " +
                                formatCurrency(
                                        getDouble(
                                                student.getData(),
                                                "totalFee")),

                        "Paid Fee : " +
                                formatCurrency(
                                        getDouble(
                                                student.getData(),
                                                "paidFee")),

                        "Remaining Fee : " +
                                formatCurrency(
                                        getDouble(
                                                student.getData(),
                                                "remainingFee")),

                        "Fee Status : " +
                                getStudentValue(
                                        "feeStatus",
                                        "PENDING")));

        return createScroll(content);
    }

    private static ScrollPane createRoomPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        content.getChildren().add(
                pageHeading(
                        "🛏 Room Information",
                        "Current hostel accommodation details."));

        content.getChildren().add(
                infoPanel(
                        "Room Details",

                        "Student Name : " +
                                getStudentValue(
                                        "fullName",
                                        "Not Available"),

                        "Room Number : " +
                                getStudentValue(
                                        "roomNumber",
                                        "Not Assigned"),

                        "Room Preference : " +
                                getStudentValue(
                                        "roomPreference",
                                        "Not Available"),

                        "Mess Plan : " +
                                getStudentValue(
                                        "messPlan",
                                        "Not Available")));

        return createScroll(content);
    }

    private static ScrollPane createPaymentsPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        Map<String, Object> fees =
                getFeeData();

        double total =
                getDouble(
                        fees,
                        "totalFees");

        double paid =
                getDouble(
                        fees,
                        "paidFees");

        double pending =
                getDouble(
                        fees,
                        "pendingFees");

        String status =
                safeObject(
                        fees.get("feeStatus"),
                        "PENDING");

        content.getChildren().addAll(

                pageHeading(
                        "💳 Fee Status",
                        "View your child's hostel fee information."),

                infoPanel(
                        "Fee Summary",

                        "Total Fees : " +
                                formatCurrency(total),

                        "Paid Fees : " +
                                formatCurrency(paid),

                        "Remaining Fees : " +
                                formatCurrency(pending),

                        "Payment Status : " +
                                status),

                createPaymentHistory());

        return createScroll(content);
    }

    private static VBox createPaymentHistory() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "Payment History"));

        if (student == null) {

            box.getChildren().add(
                    infoLabel(
                            "Student information unavailable."));

            return box;
        }

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("payments")
                            .whereEqualTo(
                                    "studentId",
                                    student.getId())
                            .get()
                            .get();

            if (snapshot.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No payment records found."));

            } else {

                for (QueryDocumentSnapshot payment :
                        snapshot.getDocuments()) {

                    Map<String, Object> data =
                            payment.getData();

                    double amount =
                            getDouble(
                                    data,
                                    "amount");

                    box.getChildren().add(
                            activityRow(
                                    "💳",
                                    formatCurrency(amount) +
                                            " • " +
                                            safeObject(
                                                    data.get(
                                                            "paymentDate"),
                                                    "Date unavailable") +
                                            " • Receipt " +
                                            safeObject(
                                                    data.get(
                                                            "receiptNumber"),
                                                    payment.getId()),
                                    safeObject(
                                            data.get(
                                                    "status"),
                                            "Unknown")));
                }
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load payment history."));
        }

        return box;
    }

    private static ScrollPane createAttendancePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        List<Map<String, Object>> records =
                getAttendanceData();

        double percentage =
                calculateAttendancePercentage(
                        records);

        content.getChildren().addAll(

                pageHeading(
                        "📊 Attendance",
                        "Complete hostel attendance history with time and location."),

                infoPanel(
                        "Attendance Summary",

                        "Student : " +
                                getStudentValue(
                                        "fullName",
                                        "Not Available"),

                        "Attendance Percentage : " +
                                String.format(
                                        "%.0f%%",
                                        percentage),

                        "Total Records : " +
                                records.size()),

                createAttendanceHistory());

        return createScroll(content);
    }

    private static VBox createAttendanceHistory() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "Complete Attendance History"));

        List<Map<String, Object>> records =
                getAttendanceData();

        if (records.isEmpty()) {

            box.getChildren().add(
                    infoLabel(
                            "No attendance records found."));

            return box;
        }

        records.sort(
                Comparator.comparing(
                        item -> safeObject(
                                item.get("date"),
                                ""),
                        Comparator.reverseOrder()));

        for (Map<String, Object> record :
                records) {

            VBox attendanceCard =
                    whitePanel();

            String date =
                    safeObject(
                            record.get("date"),
                            "-");

            String checkIn =
                    safeObject(
                            record.get("checkInTime"),
                            "-");

            String checkOut =
                    safeObject(
                            record.get("checkOutTime"),
                            "-");

            String checkInLat =
                    safeObject(
                            record.get("checkInLatitude"),
                            "-");

            String checkInLong =
                    safeObject(
                            record.get("checkInLongitude"),
                            "-");

            String checkInDistance =
                    safeObject(
                            record.get("checkInDistance"),
                            "-");

            String checkOutLat =
                    safeObject(
                            record.get("checkOutLatitude"),
                            "-");

            String checkOutLong =
                    safeObject(
                            record.get("checkOutLongitude"),
                            "-");

            String checkOutDistance =
                    safeObject(
                            record.get("checkOutDistance"),
                            "-");

            String locationVerified =
                    safeObject(
                            record.get("locationVerified"),
                            "false");

            String status =
                    safeObject(
                            record.get("status"),
                            "Present");

            String createdAt =
                    safeObject(
                            record.get("createdAt"),
                            "-");

            String updatedAt =
                    safeObject(
                            record.get("updatedAt"),
                            "-");

            attendanceCard.getChildren().addAll(

                    pageText(
                            "📅 " + date,
                            18,
                            true,
                            text()),

                    infoLabel(
                            "🟢 Check-In Time : " +
                                    checkIn),

                    infoLabel(
                            "📍 Check-In Location : " +
                                    checkInLat +
                                    ", " +
                                    checkInLong),

                    infoLabel(
                            "📏 Check-In Distance : " +
                                    checkInDistance),

                    infoLabel(
                            "🔴 Check-Out Time : " +
                                    checkOut),

                    infoLabel(
                            "📍 Check-Out Location : " +
                                    checkOutLat +
                                    ", " +
                                    checkOutLong),

                    infoLabel(
                            "📏 Check-Out Distance : " +
                                    checkOutDistance),

                    infoLabel(
                            "📌 Location Verified : " +
                                    locationVerified),

                    infoLabel(
                            "🕐 Attendance Marked At : " +
                                    createdAt),

                    infoLabel(
                            "🔄 Last Updated At : " +
                                    updatedAt),

                    statusBadge(status));

            box.getChildren().add(
                    attendanceCard);
        }

        return box;
    }

    private static ScrollPane createMessPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        content.getChildren().add(
                pageHeading(
                        "🍽 Mess Menu",
                        "Today's meal schedule published by hostel administration."));

        try {

            Map<String, Object> menu =
                    messMenuDao.getTodayMenu();

            if (menu == null ||
                    menu.isEmpty()) {

                content.getChildren().add(
                        whitePanelWithMessage(
                                "Today's mess menu is not available."));

            } else {

                VBox menuBox =
                        whitePanel();

                menuBox.getChildren().addAll(

                        sectionTitle(
                                "Today's Menu"),

                        mealRow(
                                "🌅",
                                "Breakfast",
                                safeObject(
                                        menu.get("breakfast"),
                                        "Not Available")),

                        mealRow(
                                "🍛",
                                "Lunch",
                                safeObject(
                                        menu.get("lunch"),
                                        "Not Available")),

                        mealRow(
                                "☕",
                                "Snacks",
                                safeObject(
                                        menu.get("snacks"),
                                        "Not Available")),

                        mealRow(
                                "🌙",
                                "Dinner",
                                safeObject(
                                        menu.get("dinner"),
                                        "Not Available")));

                content.getChildren().add(
                        menuBox);
            }

        } catch (Exception e) {

            content.getChildren().add(
                    whitePanelWithMessage(
                            "Unable to load mess menu."));
        }

        return createScroll(content);
    }

    private static VBox mealRow(
            String icon,
            String meal,
            String value) {

        VBox box =
                new VBox(5);

        box.setPadding(
                new Insets(14));

        box.setStyle(
                "-fx-background-color:" +
                        softBg() +
                        ";" +
                        "-fx-background-radius:12;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:12;");

        box.getChildren().addAll(

                pageText(
                        icon +
                                "  " +
                                meal,
                        15,
                        true,
                        text()),

                infoLabel(value));

        return box;
    }

    private static ScrollPane createParentVisitPage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30));

        TextField parentName =
                new TextField(
                        safeObject(
                                parent.getData()
                                        .get("fullName"),
                                ""));

        parentName.setEditable(false);
        parentName.setStyle(
                inputStyle());

        TextField studentName =
                new TextField(
                        getStudentValue(
                                "fullName",
                                safeObject(
                                        parent.getData()
                                                .get("studentName"),
                                        "")));

        studentName.setEditable(false);
        studentName.setStyle(
                inputStyle());

        TextField studentId =
                new TextField(
                        safeObject(
                                parent.getData()
                                        .get("studentId"),
                                getStudentValue(
                                        "uid",
                                        "")));

        studentId.setEditable(false);
        studentId.setStyle(
                inputStyle());

        DatePicker date =
                new DatePicker();

        date.setValue(
                LocalDate.now());

        date.setStyle(
                inputStyle());

        ComboBox<String> time =
                new ComboBox<>();

        time.getItems().addAll(
                "09:00 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "05:00 PM",
                "06:00 PM");

        time.setPromptText(
                "Select visit time");

        time.setStyle(
                inputStyle());

        TextArea purpose =
                new TextArea();

        purpose.setPromptText(
                "Purpose of visit...");

        purpose.setPrefHeight(120);

        purpose.setStyle(
                inputStyle());

        Button submit =
                primaryButton(
                        "SUBMIT VISIT REQUEST");

        submit.setOnAction(
                e -> {

                    if (student == null) {

                        showMessage(
                                "No student is linked to this parent account.");

                        return;
                    }

                    if (date.getValue() == null ||
                            time.getValue() == null ||
                            purpose.getText()
                                    .trim()
                                    .isEmpty()) {

                        showMessage(
                                "Please fill all visit details.");

                        return;
                    }

                    saveParentVisit(
                            date.getValue().toString(),
                            time.getValue(),
                            purpose.getText().trim());
                });

        content.getChildren().addAll(

                pageHeading(
                        "👨‍👩‍👧 Parent Visit",
                        "Submit a request to visit your child in the hostel."),

                fieldLabel("Parent Name"),
                parentName,

                fieldLabel("Student Name"),
                studentName,

                fieldLabel("Student ID"),
                studentId,

                fieldLabel("Visit Date"),
                date,

                fieldLabel("Visit Time"),
                time,

                fieldLabel("Purpose of Visit"),
                purpose,

                submit);

        return createScroll(content);
    }

    private static void saveParentVisit(
            String visitDate,
            String visitTime,
            String purpose) {

        try {

            String studentUid =
                    student.getId();

            String parentName =
                    safeObject(
                            parent.getData()
                                    .get("fullName"),
                            "Parent");

            String studentName =
                    getStudentValue(
                            "fullName",
                            "Student");

            String studentId =
                    safeObject(
                            parent.getData()
                                    .get("studentId"),
                            studentUid);

            Map<String, Object> visit =
                    new HashMap<>();

            visit.put(
                    "parentUid",
                    parentUid);

            visit.put(
                    "parentName",
                    parentName);

            visit.put(
                    "studentUid",
                    studentUid);

            visit.put(
                    "studentName",
                    studentName);

            visit.put(
                    "studentId",
                    studentId);

            visit.put(
                    "visitDate",
                    visitDate);

            visit.put(
                    "visitTime",
                    visitTime);

            visit.put(
                    "purpose",
                    purpose);

            visit.put(
                    "status",
                    "Waiting");

            visit.put(
                    "rejectionReason",
                    "");

            visit.put(
                    "createdAt",
                    FieldValue.serverTimestamp());

            FirebaseConfig
                    .getFireStore()
                    .collection("visits")
                    .add(visit)
                    .get();

            saveParentNotification(
                    studentUid,
                    studentName,
                    parentName,
                    visitDate,
                    visitTime);

            saveWardenNotification(
                    studentUid,
                    studentName,
                    parentName);

            showMessage(
                    "Parent visit request submitted successfully.");

            rebuild("visits");

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Unable to submit parent visit request.");
        }
    }

    private static void saveParentNotification(
            String studentUid,
            String studentName,
            String parentName,
            String date,
            String time) {

        try {

            Map<String, Object> notification =
                    new HashMap<>();

            notification.put(
                    "recipientUid",
                    studentUid);

            notification.put(
                    "recipientRole",
                    "STUDENT");

            notification.put(
                    "title",
                    "🔔 Parent Visit");

            notification.put(
                    "message",
                    parentName +
                            " has requested to visit you on " +
                            date +
                            " at " +
                            time +
                            ".");

            notification.put(
                    "type",
                    "PARENT_VISIT");

            notification.put(
                    "read",
                    false);

            notification.put(
                    "createdAt",
                    FieldValue.serverTimestamp());

            FirebaseConfig
                    .getFireStore()
                    .collection("notifications")
                    .add(notification)
                    .get();

        } catch (Exception ignored) {
        }
    }

    private static void saveWardenNotification(
            String studentUid,
            String studentName,
            String parentName) {

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("wardens")
                            .get()
                            .get();

            for (QueryDocumentSnapshot warden :
                    snapshot.getDocuments()) {

                Map<String, Object> notification =
                        new HashMap<>();

                notification.put(
                        "recipientUid",
                        warden.getId());

                notification.put(
                        "recipientRole",
                        "WARDEN");

                notification.put(
                        "title",
                        "🔔 Parent Visit");

                notification.put(
                        "message",
                        parentName +
                                " has requested to visit " +
                                studentName +
                                ".");

                notification.put(
                        "type",
                        "PARENT_VISIT");

                notification.put(
                        "read",
                        false);

                notification.put(
                        "createdAt",
                        FieldValue.serverTimestamp());

                FirebaseConfig
                        .getFireStore()
                        .collection("notifications")
                        .add(notification)
                        .get();
            }

        } catch (Exception ignored) {
        }
    }

    private static ScrollPane createVisitHistoryPage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30));

        content.getChildren().add(
                pageHeading(
                        "📋 Visit History",
                        "View all your parent visit requests and their current status."));

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("visits")
                            .get()
                            .get();

            List<QueryDocumentSnapshot> list =
                    new ArrayList<>();

            for (QueryDocumentSnapshot doc :
                    snapshot.getDocuments()) {

                String visitParentUid =
                        safeObject(
                                doc.getData()
                                        .get("parentUid"),
                                "");

                if (parentUid.equals(
                        visitParentUid)) {

                    list.add(doc);
                }
            }

            list.sort(
                    Comparator.comparing(
                            doc -> safeObject(
                                    doc.getData()
                                            .get("createdAt"),
                                    ""),
                            Comparator.reverseOrder()));

            if (list.isEmpty()) {

                content.getChildren().add(
                        whitePanelWithMessage(
                                "No visit history found."));

            } else {

                for (QueryDocumentSnapshot doc :
                        list) {

                    Map<String, Object> data =
                            doc.getData();

                    String studentName =
                            safeObject(
                                    data.get("studentName"),
                                    "Student");

                    String studentId =
                            safeObject(
                                    data.get("studentId"),
                                    "-");

                    String date =
                            safeObject(
                                    data.get("visitDate"),
                                    "-");

                    String time =
                            safeObject(
                                    data.get("visitTime"),
                                    "-");

                    String purpose =
                            safeObject(
                                    data.get("purpose"),
                                    "-");

                    String status =
                            safeObject(
                                    data.get("status"),
                                    "Waiting");

                    String rejectionReason =
                            safeObject(
                                    data.get("rejectionReason"),
                                    "");

                    VBox visit =
                            whitePanel();

                    visit.getChildren().addAll(

                            pageText(
                                    "👨‍👩‍👧 Parent Visit",
                                    18,
                                    true,
                                    text()),

                            infoLabel(
                                    "Student : " +
                                            studentName),

                            infoLabel(
                                    "Student ID : " +
                                            studentId),

                            infoLabel(
                                    "📅 Date : " +
                                            date),

                            infoLabel(
                                    "⏰ Time : " +
                                            time),

                            infoLabel(
                                    "📝 Purpose : " +
                                            purpose),

                            statusBadge(status));

                    if ("Approved".equalsIgnoreCase(
                            status)) {

                        Label approved =
                                statusMessage(
                                        "✅ Your visit request has been approved by the warden.",
                                        GREEN,
                                        darkTheme
                                                ? "#14532D"
                                                : "#DCFCE7");

                        visit.getChildren().add(
                                approved);
                    }

                    if ("Rejected".equalsIgnoreCase(
                            status)) {

                        Label rejected =
                                statusMessage(
                                        "❌ Your visit request has been rejected by the warden.",
                                        RED,
                                        darkTheme
                                                ? "#7F1D1D"
                                                : "#FEE2E2");

                        visit.getChildren().add(
                                rejected);

                        if (!rejectionReason
                                .trim()
                                .isEmpty()) {

                            Label reason =
                                    statusMessage(
                                            "❌ Reason : " +
                                                    rejectionReason,
                                            darkTheme
                                                    ? "#FDA4AF"
                                                    : "#991B1B",
                                            darkTheme
                                                    ? "#4C0519"
                                                    : "#FFF1F2");

                            visit.getChildren().add(
                                    reason);
                        }
                    }

                    if ("Waiting".equalsIgnoreCase(
                            status)) {

                        Label waiting =
                                statusMessage(
                                        "⏳ Waiting for warden approval.",
                                        darkTheme
                                                ? "#A5B4FC"
                                                : PRIMARY,
                                        darkTheme
                                                ? "#312E81"
                                                : "#EEF2FF");

                        visit.getChildren().add(
                                waiting);
                    }

                    content.getChildren().add(
                            visit);
                }
            }

        } catch (Exception e) {

            content.getChildren().add(
                    whitePanelWithMessage(
                            "Unable to load visit history."));
        }

        return createScroll(content);
    }

    private static ScrollPane createProfilePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30));

        Button edit =
                primaryButton(
                        "✏ EDIT PROFILE");

        edit.setOnAction(
                e -> showEditProfileDialog());

        content.getChildren().addAll(

                pageHeading(
                        "👤 Parent Profile",
                        "Your registered parent information."),

                infoPanel(
                        "Parent Information",

                        "Full Name : " +
                                safeObject(
                                        parent.getData()
                                                .get("fullName"),
                                        "Not Available"),

                        "Email : " +
                                safeObject(
                                        parent.getData()
                                                .get("email"),
                                        "Not Available"),

                        "Phone : " +
                                safeObject(
                                        parent.getData()
                                                .get("phone"),
                                        "Not Available"),

                        "Student Name : " +
                                safeObject(
                                        parent.getData()
                                                .get("studentName"),
                                        getStudentValue(
                                                "fullName",
                                                "Not Available")),

                        "Student ID : " +
                                safeObject(
                                        parent.getData()
                                                .get("studentId"),
                                        "Not Available")),

                edit);

        return createScroll(content);
    }

    private static ScrollPane createSettingsPage() {

        VBox content =
                new VBox(22);

        content.setPadding(
                new Insets(30));

        content.getChildren().add(
                pageHeading(
                        "⚙ Settings",
                        "Manage your parent dashboard preferences."));

        VBox appearance =
                whitePanel();

        appearance.getChildren().addAll(

                sectionTitle(
                        "🎨 Appearance"),

                settingDescription(
                        "Choose your preferred dashboard theme."));

        ComboBox<String> theme =
                new ComboBox<>();

        theme.getItems().addAll(
                "Light",
                "Dark");

        theme.setValue(
                darkTheme
                        ? "Dark"
                        : "Light");

        theme.setPrefWidth(230);

        theme.setStyle(
                inputStyle());

        theme.setOnAction(
                e -> {

                    darkTheme =
                            "Dark".equals(
                                    theme.getValue());

                    rebuild("settings");
                });

        appearance.getChildren().add(
                theme);

        VBox account =
                whitePanel();

        account.getChildren().addAll(

                sectionTitle(
                        "👤 Account"),

                settingDescription(
                        "Update your parent profile or password."));

        Button edit =
                primaryButton(
                        "EDIT PROFILE");

        edit.setOnAction(
                e -> showEditProfileDialog());

        Button password =
                primaryButton(
                        "🔐 CHANGE PASSWORD");

        password.setOnAction(
                e -> showChangePasswordDialog());

        account.getChildren().addAll(
                edit,
                password);

        VBox about =
                whitePanel();

        about.getChildren().addAll(

                sectionTitle(
                        "ℹ About Hostel Hub"),

                settingLabel(
                        "Application : Hostel Hub Parent Portal"),

                settingLabel(
                        "Version : 1.0"),

                settingLabel(
                        "Technology : JavaFX + Firebase Firestore"));

        content.getChildren().addAll(
                appearance,
                account,
                about);

        return createScroll(content);
    }

    private static ScrollPane createAboutUsPage() {

        VBox content =
                new VBox(24);

        content.setPadding(
                new Insets(
                        30,
                        40,
                        45,
                        40));

        content.getChildren().add(
                pageHeading(
                        "ℹ About Us",
                        ""));

      //  VBox projectCard =
                whitePanel();

        // projectCard.setPadding(
        //         new Insets(30));

        // StackPane projectHeader =
        //         new StackPane();

        // VBox projectInfo =
        //         new VBox(7);

        // projectInfo.setAlignment(
        //         Pos.CENTER_LEFT);

        // Label projectTitle =
        //         pageText(
        //                 "SMART HOSTEL HUB",
        //                 26,
        //                 true,
        //                 PRIMARY);

        // Label projectSubtitle =
        //         pageText(
        //                 "Smart AI-Powered Hostel Management System",
        //                 17,
        //                 true,
        //                 text());

        // Label projectDescription =
        //         pageText(
        //                 "Smart Hostel Hub is a modern hostel management system designed to simplify and digitize hostel operations. The system provides dedicated portals for Students, Parents and Wardens with features including student information, room management, attendance tracking, fee management, mess menu and parent visit management.",
        //                 14,
        //                 false,
        //                 secondary());

        // projectDescription.setWrapText(true);

        // projectDescription.setMaxWidth(950);

        // projectInfo.getChildren().addAll(
        //         projectTitle,
        //         projectSubtitle,
        //         projectDescription);

        // ImageView projectLogo =
        //         loadImage(
        //                 "/images/logo.jpeg",
        //                 85,
        //                 85);

        // projectLogo.setPreserveRatio(
        //         true);

        // StackPane.setAlignment(
        //         projectLogo,
        //         Pos.TOP_RIGHT);

        // projectHeader.getChildren().addAll(
        //         projectInfo,
        //         projectLogo);

        // projectCard.getChildren().add(
        //         projectHeader);

        // content.getChildren().add(
        //         projectCard);

       // content.getChildren().add(
              //  sectionTitle(
                //        "👨‍💻 Team Information"));

        HBox teamCards =
                new HBox(20);

        teamCards.setAlignment(
                Pos.CENTER);

        // VBox member1 =
        //         teamMemberCard(
        //                 "👩‍💻",
        //                 "Samruddhi Kanade",
        //                 "Project Developer");

        // VBox member2 =
        //         teamMemberCard(
        //                 "👩‍💻",
        //                 "Vaishnavi Kadam",
        //                 "Project Developer");

        // HBox.setHgrow(
        //         member1,
        //         Priority.ALWAYS);

        // HBox.setHgrow(
        //         member2,
        //         Priority.ALWAYS);

        // teamCards.getChildren().addAll(
        //         member1,
        //         member2);

        Label specialThanks = new Label("🙏 Special Thanks");

specialThanks.setStyle(
        "-fx-font-size: 28px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: black;"
);

content.getChildren().addAll(specialThanks);

        // content.getChildren().add(
        //         teamCards);

        // content.getChildren().add(
        //         sectionTitle(
        //                 "🙏 Special Thanks"));

        VBox specialThanksCard =
                whitePanel();

        specialThanksCard.setPadding(
                new Insets(35));

        specialThanksCard.setAlignment(
                Pos.CENTER);

        VBox shashiSection =
                new VBox(14);

        shashiSection.setAlignment(
                Pos.CENTER);

        ImageView shashiImage =
                loadImage(
                        "/images/image.jpeg",
                        210,
                        210);

        Circle clip =
                new Circle(
                        105,
                        105,
                        105);

        shashiImage.setClip(
                clip);

        Label shashiName =
                pageText(
                        "Shashi Sir",
                        24,
                        true,
                        text());

        shashiName.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER);

        Label core2web =
                pageText(
                        "Core2Web",
                        17,
                        true,
                        BLUE);

        core2web.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER);

        Label shashiDesc =
                pageText(
                        "We sincerely thank Shashi Sir and Core2Web for providing valuable technical guidance, knowledge, motivation and continuous support throughout the development of Smart Hostel Hub.",
                        14,
                        false,
                        secondary());

        shashiDesc.setWrapText(true);

        shashiDesc.setMaxWidth(700);

        shashiDesc.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER);

        shashiSection.getChildren().addAll(
                shashiImage,
                shashiName,
                core2web,
                shashiDesc);

        specialThanksCard.getChildren().add(
                shashiSection);

        content.getChildren().add(
                specialThanksCard);

        content.getChildren().add(
                createThanksCard(
                        "👨‍🏫 Thanks to Instructors",
                        "Sachin Sir",
                        "Pramod Sir",
                        "Akshay Sir"));

        content.getChildren().add(
                createThanksCard(
                        "🌟 Thanks to Super Mentors",
                        "Shiv Sir",
                        "Subodh Sir"));

        VBox mentorsCard =
                whitePanel();

        Label mentorsTitle =
                sectionTitle(
                        "🤝 Thanks to Mentors & Team Leads");

        Label mentorsDescription =
                pageText(
                        "We sincerely thank all our mentors and team leads for their valuable guidance, technical support, motivation and continuous encouragement throughout the project development.",
                        14,
                        false,
                        secondary());

        mentorsDescription.setWrapText(true);

        mentorsDescription.setMaxWidth(1000);

        mentorsCard.getChildren().addAll(
                mentorsTitle,
                mentorsDescription);

        content.getChildren().add(
                mentorsCard);

        return createScroll(content);
    }

    private static ImageView loadImage(
            String resourcePath,
            double width,
            double height) {

        ImageView imageView =
                new ImageView();

        try {

            if (ParentDashboard.class
                    .getResource(resourcePath) != null) {

                Image image =
                        new Image(
                                ParentDashboard.class
                                        .getResourceAsStream(
                                                resourcePath));

                imageView.setImage(image);
            }

        } catch (Exception ignored) {
        }

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }

    private static VBox teamMemberCard(
            String icon,
            String name,
            String role) {

        VBox box =
                whitePanel();

        box.setAlignment(
                Pos.CENTER);

        box.setPadding(
                new Insets(25));

        box.getChildren().addAll(

                pageText(
                        icon,
                        28,
                        false,
                        PRIMARY),

                pageText(
                        name,
                        17,
                        true,
                        text()),

                pageText(
                        role,
                        13,
                        false,
                        muted()));

        return box;
    }

    private static VBox createThanksCard(
            String title,
            String... names) {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(title));

        for (String name : names) {

            box.getChildren().add(
                    infoLabel(
                            "• " + name));
        }

        return box;
    }

    private static void showEditProfileDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Parent Profile");

        dialog.setHeaderText(
                "Update your profile name");

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20));

        TextField name =
                new TextField(
                        safeObject(
                                parent.getData()
                                        .get("fullName"),
                                ""));

        name.setStyle(
                inputStyle());

        box.getChildren().add(
                name);

        dialog.getDialogPane()
                .setContent(box);

        ButtonType save =
                new ButtonType(
                        "SAVE",
                        ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        ButtonType.CANCEL);

        dialog.setResultConverter(
                button -> {

                    if (button == save) {

                        String newName =
                                name.getText()
                                        .trim();

                        if (newName.isEmpty()) {

                            showMessage(
                                    "Name cannot be empty.");

                            return null;
                        }

                        try {

                            FirebaseConfig
                                    .getFireStore()
                                    .collection("parents")
                                    .document(parentUid)
                                    .update(
                                            "fullName",
                                            newName)
                                    .get();

                            displayName =
                                    newName;

                            showMessage(
                                    "Profile updated successfully.");

                        } catch (Exception e) {

                            showMessage(
                                    "Unable to update profile.");
                        }
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static void showChangePasswordDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Change Password");

        dialog.setHeaderText(
                "Update your Hostel Hub password");

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20));

        PasswordField current =
                new PasswordField();

        current.setPromptText(
                "Current Password");

        PasswordField next =
                new PasswordField();

        next.setPromptText(
                "New Password");

        PasswordField confirm =
                new PasswordField();

        confirm.setPromptText(
                "Confirm New Password");

        current.setStyle(
                inputStyle());

        next.setStyle(
                inputStyle());

        confirm.setStyle(
                inputStyle());

        box.getChildren().addAll(
                current,
                next,
                confirm);

        dialog.getDialogPane()
                .setContent(box);

        ButtonType update =
                new ButtonType(
                        "UPDATE",
                        ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        update,
                        ButtonType.CANCEL);

        dialog.setResultConverter(
                button -> {

                    if (button == update) {

                        String oldPassword =
                                current.getText();

                        String newPassword =
                                next.getText();

                        String confirmPassword =
                                confirm.getText();

                        if (oldPassword.isEmpty() ||
                                newPassword.isEmpty() ||
                                confirmPassword.isEmpty()) {

                            showMessage(
                                    "Please fill all fields.");

                            return null;
                        }

                        if (!newPassword.equals(
                                confirmPassword)) {

                            showMessage(
                                    "New passwords do not match.");

                            return null;
                        }

                        if (newPassword.length() < 6) {

                            showMessage(
                                    "Password must contain at least 6 characters.");

                            return null;
                        }

                        boolean result =
                                FirebaseAuthController
                                        .changePassword(
                                                safeObject(
                                                        parent.getData()
                                                                .get("email"),
                                                        ""),
                                                oldPassword,
                                                newPassword);

                        showMessage(
                                result
                                        ? "Password updated successfully."
                                        : "Password update failed.");
                    }

                    return button;
                });

        dialog.showAndWait();
    }

    private static Map<String, Object> getFeeData() {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "totalFees",
                0.0);

        data.put(
                "paidFees",
                0.0);

        data.put(
                "pendingFees",
                0.0);

        data.put(
                "feeStatus",
                "PENDING");

        if (student == null) {
            return data;
        }

        Map<String, Object> studentData =
                student.getData();

        double total =
                getDouble(
                        studentData,
                        "totalFee");

        double paid =
                getDouble(
                        studentData,
                        "paidFee");

        double pending =
                getDouble(
                        studentData,
                        "remainingFee");

        String status =
                safeObject(
                        studentData.get("feeStatus"),
                        "");

        if (pending <= 0 &&
                total > 0) {

            pending =
                    Math.max(
                            0,
                            total - paid);
        }

        if (status.isEmpty()) {

            if (pending <= 0) {

                status = "PAID";

            } else if (paid > 0) {

                status = "PARTIAL";

            } else {

                status = "PENDING";
            }
        }

        data.put(
                "totalFees",
                total);

        data.put(
                "paidFees",
                paid);

        data.put(
                "pendingFees",
                pending);

        data.put(
                "feeStatus",
                status);

        return data;
    }

    private static List<Map<String, Object>>
    getAttendanceData() {

        List<Map<String, Object>> result =
                new ArrayList<>();

        if (student == null) {
            return result;
        }

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("students")
                            .document(
                                    student.getId())
                            .collection("attendance")
                            .get()
                            .get();

            for (QueryDocumentSnapshot doc :
                    snapshot.getDocuments()) {

                Map<String, Object> data =
                        new HashMap<>(
                                doc.getData());

                data.put(
                        "attendanceId",
                        doc.getId());

                result.add(data);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    private static double
    calculateAttendancePercentage(
            List<Map<String, Object>> records) {

        if (records.isEmpty()) {
            return 0;
        }

        int present = 0;

        for (Map<String, Object> record :
                records) {

            String status =
                    safeObject(
                            record.get("status"),
                            "");

            if (status.equalsIgnoreCase(
                    "Present") ||
                    status.equalsIgnoreCase(
                            "Completed")) {

                present++;
            }
        }

        return (present * 100.0) /
                records.size();
    }

    private static String getStudentValue(
            String key,
            String fallback) {

        if (student == null) {
            return fallback;
        }

        if ("studentId".equals(key) &&
                student.getData()
                        .get("studentId") == null) {

            return safeObject(
                    student.getData()
                            .get("uid"),
                    fallback);
        }

        return safeObject(
                student.getData()
                        .get(key),
                fallback);
    }

    private static double getDouble(
            Map<String, Object> data,
            String key) {

        Object value =
                data.get(key);

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {

            return ((Number) value)
                    .doubleValue();
        }

        try {

            return Double.parseDouble(
                    value.toString());

        } catch (Exception e) {

            return 0;
        }
    }

    private static String formatCurrency(
            double amount) {

        return String.format(
                "₹%,.2f",
                amount);
    }

    private static Label statusBadge(
            String status) {

        String actualStatus =
                safeObject(
                        status,
                        "Unknown");

        String foreground;
        String background;

        if (actualStatus.equalsIgnoreCase(
                "Approved") ||
                actualStatus.equalsIgnoreCase(
                        "Resolved") ||
                actualStatus.equalsIgnoreCase(
                        "Completed") ||
                actualStatus.equalsIgnoreCase(
                        "Paid") ||
                actualStatus.equalsIgnoreCase(
                        "Read")) {

            foreground = darkTheme
                    ? "#86EFAC"
                    : GREEN;

            background = darkTheme
                    ? "#14532D"
                    : "#DCFCE7";

        } else if (actualStatus.equalsIgnoreCase(
                "Rejected")) {

            foreground = darkTheme
                    ? "#FCA5A5"
                    : RED;

            background = darkTheme
                    ? "#7F1D1D"
                    : "#FEE2E2";

        } else if (actualStatus.equalsIgnoreCase(
                "Pending") ||
                actualStatus.equalsIgnoreCase(
                        "Waiting") ||
                actualStatus.equalsIgnoreCase(
                        "Acknowledged")) {

            foreground = darkTheme
                    ? "#FDBA74"
                    : ORANGE;

            background = darkTheme
                    ? "#7C2D12"
                    : "#FFEDD5";

        } else {

            foreground = darkTheme
                    ? "#A5B4FC"
                    : PRIMARY;

            background = darkTheme
                    ? "#312E81"
                    : "#EEF2FF";
        }

        Label badge =
                new Label(
                        "Status : " +
                                safe(actualStatus));

        badge.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        12));

        badge.setTextFill(
                Color.web(foreground));

        badge.setPadding(
                new Insets(
                        7,
                        12,
                        7,
                        12));

        badge.setStyle(
                "-fx-background-color:" +
                        background +
                        ";" +
                        "-fx-background-radius:20;");

        return badge;
    }

    private static Label statusMessage(
            String message,
            String foreground,
            String background) {

        Label label =
                pageText(
                        message,
                        13,
                        true,
                        foreground);

        label.setWrapText(true);

        label.setStyle(
                "-fx-background-color:" +
                        background +
                        ";" +
                        "-fx-background-radius:10;" +
                        "-fx-padding:10;");

        return label;
    }

    private static VBox dashboardCard(
            String icon,
            String title,
            String value,
            String subtitle,
            String accent) {

        VBox box =
                new VBox(7);

        box.setPadding(
                new Insets(20));

        box.setPrefWidth(240);

        box.setMinHeight(145);

        box.setStyle(
                "-fx-background-color:" +
                        card() +
                        ";" +
                        "-fx-background-radius:16;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:16;" +
                        "-fx-cursor:hand;");

        Label iconLabel =
                pageText(
                        icon,
                        25,
                        false,
                        accent);

        Label titleLabel =
                pageText(
                        title,
                        11,
                        true,
                        muted());

        Label valueLabel =
                pageText(
                        value,
                        21,
                        true,
                        text());

        Label subtitleLabel =
                pageText(
                        subtitle,
                        11,
                        false,
                        muted());

        box.getChildren().addAll(
                iconLabel,
                titleLabel,
                valueLabel,
                subtitleLabel);

        return box;
    }

    private static VBox whitePanel() {

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20));

        box.setStyle(
                "-fx-background-color:" +
                        panel() +
                        ";" +
                        "-fx-background-radius:16;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:16;");

        return box;
    }

    private static VBox whitePanelWithMessage(
            String message) {

        VBox box =
                whitePanel();

        box.getChildren().add(
                infoLabel(message));

        return box;
    }

    private static Label sectionTitle(
            String value) {

        return pageText(
                value,
                18,
                true,
                text());
    }

    private static VBox pageHeading(
            String title,
            String subtitle) {

        VBox box =
                new VBox(5);

        box.getChildren().addAll(

                pageText(
                        title,
                        26,
                        true,
                        text()),

                pageText(
                        subtitle,
                        13,
                        false,
                        muted()));

        return box;
    }

    private static VBox infoPanel(
            String title,
            String... values) {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(title));

        for (String value : values) {

            box.getChildren().add(
                    infoLabel(value));
        }

        return box;
    }

    private static Label settingDescription(
            String value) {

        Label label =
                pageText(
                        value,
                        13,
                        false,
                        muted());

        label.setWrapText(true);

        return label;
    }

    private static Label settingLabel(
            String value) {

        return pageText(
                value,
                13,
                false,
                secondary());
    }

    private static Label activityRow(
            String icon,
            String title,
            String status) {

        Label label =
                pageText(
                        icon +
                                "   " +
                                title +
                                "\n       Status : " +
                                status,
                        13,
                        false,
                        secondary());

        label.setWrapText(true);

        return label;
    }

    private static Label fieldLabel(
            String value) {

        return pageText(
                value,
                13,
                true,
                secondary());
    }

    private static Label infoLabel(
            String value) {

        Label label =
                pageText(
                        value,
                        14,
                        false,
                        secondary());

        label.setWrapText(true);

        return label;
    }

    private static Label pageText(
            String value,
            double size,
            boolean bold,
            String color) {

        Label label =
                new Label(
                        value == null
                                ? ""
                                : value);

        label.setFont(
                Font.font(
                        "Segoe UI",
                        bold
                                ? FontWeight.BOLD
                                : FontWeight.NORMAL,
                        size));

        label.setTextFill(
                Color.web(color));

        return label;
    }

    private static Button menuButton(
            String icon,
            String textValue) {

        Button button =
                new Button(
                        icon +
                                "   " +
                                textValue);

        button.setMaxWidth(
                Double.MAX_VALUE);

        button.setPrefHeight(43);

        button.setAlignment(
                Pos.CENTER_LEFT);

        button.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12));

        button.setStyle(
                menuNormalStyle());

        button.setOnMouseEntered(
                e -> {

                    if (!button.getStyle()
                            .contains(
                                    "-fx-background-color:#4F46E5")) {

                        button.setStyle(
                                menuHoverStyle());
                    }
                });

        button.setOnMouseExited(
                e -> {

                    if (!button.getStyle()
                            .contains(
                                    "-fx-background-color:#4F46E5")) {

                        button.setStyle(
                                menuNormalStyle());
                    }
                });

        return button;
    }

    private static String menuNormalStyle() {

        return "-fx-background-color:transparent;" +
                "-fx-text-fill:#D1D5DB;" +
                "-fx-font-family:'Segoe UI';" +
                "-fx-font-size:13px;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";
    }

    private static String menuHoverStyle() {

        return "-fx-background-color:#1F2937;" +
                "-fx-text-fill:#FFFFFF;" +
                "-fx-font-family:'Segoe UI';" +
                "-fx-font-size:13px;" +
                "-fx-background-radius:9;" +
                "-fx-cursor:hand;";
    }

    private static void setActiveMenuStyle(
            Button button) {

        button.setStyle(
                "-fx-background-color:" +
                        PRIMARY +
                        ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:9;" +
                        "-fx-cursor:hand;");
    }

    private static Button quickButton(
            String icon,
            String textValue) {

        Button button =
                new Button(
                        icon +
                                "\n" +
                                textValue);

        button.setPrefSize(
                170,
                75);

        button.setStyle(
                "-fx-background-color:" +
                        card() +
                        ";" +
                        "-fx-text-fill:" +
                        secondary() +
                        ";" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:14;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:14;" +
                        "-fx-cursor:hand;");

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                iconBg() +
                                ";" +
                                "-fx-text-fill:" +
                                PRIMARY +
                                ";" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:14;" +
                                "-fx-border-color:" +
                                PRIMARY +
                                ";" +
                                "-fx-border-radius:14;" +
                                "-fx-cursor:hand;"));

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                card() +
                                ";" +
                                "-fx-text-fill:" +
                                secondary() +
                                ";" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:14;" +
                                "-fx-border-color:" +
                                border() +
                                ";" +
                                "-fx-border-radius:14;" +
                                "-fx-cursor:hand;"));

        return button;
    }

    private static Button primaryButton(
            String value) {

        Button button =
                new Button(value);

        button.setPrefWidth(230);

        button.setPrefHeight(45);

        button.setStyle(
                "-fx-background-color:" +
                        PRIMARY +
                        ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;" +
                        "-fx-cursor:hand;");

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                PRIMARY_DARK +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-cursor:hand;"));

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                PRIMARY +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-cursor:hand;"));

        return button;
    }

    private static Button secondaryButton(
            String value) {

        Button button =
                new Button(value);

        button.setPrefWidth(230);

        button.setPrefHeight(45);

        button.setStyle(
                "-fx-background-color:" +
                        card() +
                        ";" +
                        "-fx-text-fill:" +
                        secondary() +
                        ";" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:10;" +
                        "-fx-cursor:hand;");

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                iconBg() +
                                ";" +
                                "-fx-text-fill:" +
                                PRIMARY +
                                ";" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-border-color:" +
                                PRIMARY +
                                ";" +
                                "-fx-border-radius:10;" +
                                "-fx-cursor:hand;"));

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                card() +
                                ";" +
                                "-fx-text-fill:" +
                                secondary() +
                                ";" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-border-color:" +
                                border() +
                                ";" +
                                "-fx-border-radius:10;" +
                                "-fx-cursor:hand;"));

        return button;
    }

    private static String inputStyle() {

        return "-fx-background-color:" +
                inputBg() +
                ";" +
                "-fx-text-fill:" +
                text() +
                ";" +
                "-fx-prompt-text-fill:" +
                muted() +
                ";" +
                "-fx-border-color:" +
                border() +
                ";" +
                "-fx-border-radius:8;" +
                "-fx-background-radius:8;";
    }

    private static ScrollPane createScroll(
            VBox content) {

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setStyle(
                "-fx-background-color:" +
                        bg() +
                        ";" +
                        "-fx-background:" +
                        bg() +
                        ";");

        return scroll;
    }

    private static String safe(
            String value) {

        return value == null ||
                value.trim().isEmpty()
                        ? "Not Available"
                        : value;
    }

    private static String safeObject(
            Object value,
            String fallback) {

        if (value == null) {
            return fallback;
        }

        String result =
                value.toString();

        return result.trim().isEmpty()
                ? fallback
                : result;
    }

    private static void showMessage(
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION);

        alert.setTitle(
                "Hostel Hub");

        alert.setHeaderText(null);

        alert.setContentText(
                message);

        alert.showAndWait();
    }
}