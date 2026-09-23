
package com.hostelhub.view;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.controller.FirebaseAuthController;
import com.hostelhub.dao.FirestoreWardenDao;
import com.hostelhub.dao.NoticeDao;
import com.hostelhub.dao.StudentRequestDao;
import com.hostelhub.model.Student;
import com.hostelhub.model.Warden;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDashboard {

    private static BorderPane root;
    private static Student student;
    private static boolean darkTheme = false;
    private static String displayName;

    private static final NoticeDao noticeDao =
            new NoticeDao();

    private static final double HOSTEL_LATITUDE = 18.5204;
    private static final double HOSTEL_LONGITUDE = 73.8567;
    private static final double HOSTEL_RADIUS = 100;

    private static final String PRIMARY = "#4F46E5";
    private static final String PRIMARY_DARK = "#3730A3";
    private static final String GREEN = "#16A34A";
    private static final String RED = "#DC2626";
    private static final String ORANGE = "#EA580C";
    private static final String BLUE = "#2563EB";

    public static void show(Student loggedStudent) {

        student = loggedStudent;

        displayName =
                safe(
                        student.getFullName()
                );

        root =
                new BorderPane();

        rebuild("dashboard");

        Scene scene =
                new Scene(
                        root,
                        1550,
                        800
                );

        Stage stage =
                Welcome.stage;

        stage.setTitle(
                "Hostel Hub - Student Dashboard"
        );

        stage.setScene(scene);

        stage.setMaximized(true);

        stage.show();
    }

    private static void rebuild(
            String page
    ) {

        root.setStyle(
                "-fx-background-color:" +
                        bg() +
                        ";"
        );

        root.setTop(
                createTopBar()
        );

        root.setLeft(
                createSidebar()
        );

        switch (page) {

            case "settings":
                root.setCenter(
                        createSettingsPage()
                );
                break;

            case "profile":
                root.setCenter(
                        createProfilePage()
                );
                break;

            case "room":
                root.setCenter(
                        createRoomPage()
                );
                break;

            case "payments":
                root.setCenter(
                        createPaymentsPage()
                );
                break;

            case "complaints":
                root.setCenter(
                        createComplaintsPage()
                );
                break;

            case "mess":
                root.setCenter(
                        createMessPage()
                );
                break;

            case "leave":
                root.setCenter(
                        createLeavePage()
                );
                break;

            case "attendance":
                root.setCenter(
                        createAttendancePage()
                );
                break;

            case "notices":
                root.setCenter(
                        createNoticesPage()
                );
                break;

            case "services":
                root.setCenter(
                        createServicesPage()
                );
                break;

            case "scholarship":
                root.setCenter(
                        createScholarshipPage()
                );
                break;

            case "emergency":
                root.setCenter(
                        createEmergencyPage()
                );
                break;

            default:
                root.setCenter(
                        createDashboardPage()
                );
                break;
        }
    }

    private static String bg() {

        return darkTheme
                ? "#0F172A"
                : "#F4F6FB";
    }

    private static String panel() {

        return darkTheme
                ? "#1E293B"
                : "#FFFFFF";
    }

    private static String card() {

        return darkTheme
                ? "#1E293B"
                : "#FFFFFF";
    }

    private static String border() {

        return darkTheme
                ? "#334155"
                : "#E5E7EB";
    }

    private static String text() {

        return darkTheme
                ? "#F8FAFC"
                : "#111827";
    }

    private static String secondary() {

        return darkTheme
                ? "#CBD5E1"
                : "#374151";
    }

    private static String muted() {

        return darkTheme
                ? "#94A3B8"
                : "#6B7280";
    }

    private static String inputBg() {

        return darkTheme
                ? "#0F172A"
                : "#F9FAFB";
    }

    private static HBox createTopBar() {

        HBox bar =
                new HBox(20);

        bar.setAlignment(
                Pos.CENTER_LEFT
        );

        bar.setPadding(
                new Insets(
                        15,
                        30,
                        15,
                        30
                )
        );

        bar.setStyle(
                "-fx-background-color:" +
                        panel() +
                        ";" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-width:0 0 1 0;"
        );

        Label logo =
                new Label(
                        "HOSTEL HUB"
                );

        logo.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        23
                )
        );

        logo.setTextFill(
                Color.web(PRIMARY)
        );

        Label small =
                new Label(
                        "  |  Student Portal"
                );

        small.setFont(
                Font.font(
                        "Segoe UI",
                        13
                )
        );

        small.setTextFill(
                Color.web(muted())
        );

        HBox logoBox =
                new HBox(
                        logo,
                        small
                );

        logoBox.setAlignment(
                Pos.CENTER_LEFT
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button notification =
                new Button(
                        "🔔"
                );

        notification.setPrefSize(
                42,
                42
        );

        notification.setStyle(
                "-fx-background-color:" +
                        (darkTheme
                                ? "#312E81"
                                : "#EEF2FF") +
                        ";" +
                        "-fx-background-radius:22;" +
                        "-fx-font-size:17px;" +
                        "-fx-cursor:hand;"
        );

        notification.setOnAction(
                e -> showNotificationSummary()
        );

        Label profileIcon =
                new Label(
                        "👤"
                );

        profileIcon.setFont(
                Font.font(25)
        );

        VBox info =
                new VBox(2);

        info.setAlignment(
                Pos.CENTER_RIGHT
        );

        Label name =
                new Label(
                        displayName
                );

        name.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        14
                )
        );

        name.setTextFill(
                Color.web(text())
        );

        Label role =
                new Label(
                        "Student"
                );

        role.setFont(
                Font.font(
                        "Segoe UI",
                        11
                )
        );

        role.setTextFill(
                Color.web(muted())
        );

        info.getChildren().addAll(
                name,
                role
        );

        bar.getChildren().addAll(
                logoBox,
                spacer,
                notification,
                profileIcon,
                info
        );

        return bar;
    }

    private static void showNotificationSummary() {

        try {

            StudentRequestDao dao =
                    new StudentRequestDao();

            List<Map<String, Object>> complaints =
                    dao.getStudentComplaints(
                            student.getUid()
                    );

            List<Map<String, Object>> leaves =
                    dao.getStudentLeaveRequests(
                            student.getUid()
                    );

            List<Map<String, Object>> emergencies =
                    dao.getStudentEmergencies(
                            student.getUid()
                    );

            int pendingComplaints = 0;
            int pendingLeaves = 0;
            int activeEmergencies = 0;

            for (
                    Map<String, Object> item :
                    complaints
            ) {

                String status =
                        safeObject(
                                item.get("status"),
                                "Pending"
                        );

                if (
                        !isClosedComplaint(
                                status
                        )
                ) {

                    pendingComplaints++;
                }
            }

            for (
                    Map<String, Object> item :
                    leaves
            ) {

                String status =
                        safeObject(
                                item.get("status"),
                                "Pending"
                        );

                if (
                        !isClosedLeave(
                                status
                        )
                ) {

                    pendingLeaves++;
                }
            }

            for (
                    Map<String, Object> item :
                    emergencies
            ) {

                String status =
                        safeObject(
                                item.get("status"),
                                "Urgent"
                        );

                if (
                        !isClosedEmergency(
                                status
                        )
                ) {

                    activeEmergencies++;
                }
            }

            showMessage(
                    "🔔 Notifications\n\n" +
                            "📝 Complaints : " +
                            pendingComplaints +
                            "\n📅 Leave Requests : " +
                            pendingLeaves +
                            "\n🆘 Emergency Alerts : " +
                            activeEmergencies
            );

        } catch (Exception e) {

            showMessage(
                    "Unable to load notifications."
            );
        }
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
                        14
                )
        );

        side.setStyle(
                "-fx-background-color:#111827;" +
                        "-fx-border-color:" +
                        (
                                darkTheme
                                        ? "#020617"
                                        : "#111827"
                        ) +
                        ";" +
                        "-fx-border-width:0 1 0 0;"
        );

        Label title =
                new Label(
                        "MAIN MENU"
                );

        title.setPadding(
                new Insets(
                        5,
                        12,
                        10,
                        12
                )
        );

        title.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        11
                )
        );

        title.setTextFill(
                Color.web("#9CA3AF")
        );

        side.getChildren().add(
                title
        );

        Button dashboard =
                menuButton(
                        "🏠",
                        "Dashboard"
                );

        Button room =
                menuButton(
                        "🛏",
                        "My Room"
                );

        Button payments =
                menuButton(
                        "💳",
                        "Payments"
                );

        Button complaints =
                menuButton(
                        "📝",
                        "Complaints"
                );

        Button mess =
                menuButton(
                        "🍽",
                        "Mess"
                );

        Button leave =
                menuButton(
                        "📅",
                        "Leave"
                );

        Button attendance =
                menuButton(
                        "📍",
                        "Attendance"
                );

        Button notices =
                menuButton(
                        "📢",
                        "Notices"
                );

        Button services =
                menuButton(
                        "🔧",
                        "Services"
                );

        Button scholarship =
                menuButton(
                        "🎓",
                        "Scholarship"
                );

        Button profile =
                menuButton(
                        "👤",
                        "Profile"
                );

        Button emergency =
                menuButton(
                        "🆘",
                        "Emergency"
                );

        side.getChildren().addAll(
                dashboard,
                room,
                payments,
                complaints,
                mess,
                leave,
                attendance,
                notices,
                services,
                scholarship,
                profile,
                emergency
        );

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        Button settings =
                menuButton(
                        "⚙",
                        "Settings"
                );

        Button logout =
                menuButton(
                        "🚪",
                        "Logout"
                );

        side.getChildren().addAll(
                spacer,
                settings,
                logout
        );

        dashboard.setOnAction(
                e -> rebuild("dashboard")
        );

        room.setOnAction(
                e -> rebuild("room")
        );

        payments.setOnAction(
                e -> rebuild("payments")
        );

        complaints.setOnAction(
                e -> rebuild("complaints")
        );

        mess.setOnAction(
                e -> rebuild("mess")
        );

        leave.setOnAction(
                e -> rebuild("leave")
        );

        attendance.setOnAction(
                e -> rebuild("attendance")
        );

        notices.setOnAction(
                e -> rebuild("notices")
        );

        services.setOnAction(
                e -> rebuild("services")
        );

        scholarship.setOnAction(
                e -> rebuild("scholarship")
        );

        profile.setOnAction(
                e -> rebuild("profile")
        );

        emergency.setOnAction(
                e -> rebuild("emergency")
        );

        settings.setOnAction(
                e -> rebuild("settings")
        );

        logout.setOnAction(
                e -> StudentLogin.show()
        );

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
                        35
                )
        );

        content.getChildren().addAll(

                pageText(
                        "Welcome, " +
                                displayName +
                                " 👋",
                        30,
                        true,
                        text()
                ),

                pageText(
                        "Here's your hostel overview.",
                        14,
                        false,
                        muted()
                )
        );

        Map<String, Object> feeData =
                getFeeData();

        double totalFees =
                getDouble(
                        feeData,
                        "totalFees"
                );

        double paidFees =
                getDouble(
                        feeData,
                        "paidFees"
                );

        double pendingFees =
                getDouble(
                        feeData,
                        "pendingFees"
                );

        String feeStatus =
                safeObject(
                        feeData.get("feeStatus"),
                        "PENDING"
                );

        List<Map<String, Object>> attendanceData =
                getAttendanceData();

        double attendancePercentage =
                calculateAttendancePercentage(
                        attendanceData
                );

        int pendingLeaves =
                getPendingLeaveCount();

        HBox cards =
                new HBox(18);

        VBox room =
                dashboardCard(
                        "🛏",
                        "MY ROOM",
                        safe(
                                student.getRoomNumber()
                        ),
                        safe(
                                student.getRoomPreference()
                        ),
                        BLUE
                );

        VBox fee =
                dashboardCard(
                        "💳",
                        "PENDING FEES",
                        formatCurrency(
                                pendingFees
                        ),
                        pendingFees > 0
                                ? feeStatus
                                : "All fees paid",
                        GREEN
                );

        VBox leave =
                dashboardCard(
                        "📅",
                        "LEAVE STATUS",
                        pendingLeaves > 0
                                ? pendingLeaves +
                                " Pending"
                                : "Clear",
                        pendingLeaves > 0
                                ? "Awaiting warden approval"
                                : "No pending request",
                        PRIMARY
                );

        VBox attendance =
                dashboardCard(
                        "📊",
                        "ATTENDANCE",
                        String.format(
                                "%.0f%%",
                                attendancePercentage
                        ),
                        attendanceData.isEmpty()
                                ? "No records available"
                                : "Based on attendance",
                        GREEN
                );

        fee.setOnMouseClicked(
                e -> openPaymentPage()
        );

        leave.setOnMouseClicked(
                e -> rebuild("leave")
        );

        attendance.setOnMouseClicked(
                e -> rebuild("attendance")
        );

        cards.getChildren().addAll(
                room,
                fee,
                leave,
                attendance
        );

        content.getChildren().add(
                cards
        );

        HBox analytics =
                new HBox(
                        20,
                        createAttendanceChart(
                                attendanceData
                        ),
                        createFeeStatus(
                                totalFees,
                                paidFees,
                                pendingFees,
                                feeStatus
                        )
                );

        HBox.setHgrow(
                analytics.getChildren().get(0),
                Priority.ALWAYS
        );

        HBox.setHgrow(
                analytics.getChildren().get(1),
                Priority.ALWAYS
        );

        content.getChildren().add(
                analytics
        );

        HBox lower =
                new HBox(
                        20,
                        createAnnouncements(),
                        createTodayMenu()
                );

        HBox.setHgrow(
                lower.getChildren().get(0),
                Priority.ALWAYS
        );

        HBox.setHgrow(
                lower.getChildren().get(1),
                Priority.ALWAYS
        );

        content.getChildren().add(
                lower
        );

        content.getChildren().add(
                sectionTitle(
                        "Quick Actions"
                )
        );

        HBox quick =
                new HBox(15);

        Button complaint =
                quickButton(
                        "📝",
                        "New Complaint",
                        ORANGE
                );

        Button leaveBtn =
                quickButton(
                        "📅",
                        "Apply Leave",
                        PRIMARY
                );

        Button att =
                quickButton(
                        "📍",
                        "Attendance",
                        GREEN
                );

        Button emerg =
                quickButton(
                        "🆘",
                        "Emergency",
                        RED
                );

        Button prof =
                quickButton(
                        "👤",
                        "My Profile",
                        BLUE
                );

        complaint.setOnAction(
                e -> rebuild("complaints")
        );

        leaveBtn.setOnAction(
                e -> rebuild("leave")
        );

        att.setOnAction(
                e -> rebuild("attendance")
        );

        emerg.setOnAction(
                e -> rebuild("emergency")
        );

        prof.setOnAction(
                e -> rebuild("profile")
        );

        quick.getChildren().addAll(
                complaint,
                leaveBtn,
                att,
                emerg,
                prof
        );

        content.getChildren().add(
                quick
        );

        content.getChildren().add(
                sectionTitle(
                        "Recent Activity"
                )
        );

        content.getChildren().add(
                createRecentActivity()
        );

        return createScroll(content);
    }

    private static void openPaymentPage() {

        try {

            PaymentPage paymentPage =
                    new PaymentPage(student);

            paymentPage.show(student);

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                    "Unable to open payment page."
            );
        }
    }

    private static VBox createAttendanceChart(
            List<Map<String, Object>> attendanceData
    ) {

        VBox box =
                whitePanel();

        box.setPrefWidth(600);

        box.getChildren().addAll(

                sectionTitle(
                        "📊 Attendance"
                ),

                settingDescription(
                        "Your latest attendance records"
                )
        );

        if (
                attendanceData.isEmpty()
        ) {

            box.getChildren().add(
                    infoLabel(
                            "No attendance records available."
                    )
            );

            return box;
        }

        Map<String, Integer> dayMap =
                new HashMap<>();

        for (
                Map<String, Object> record :
                attendanceData
        ) {

            String date =
                    safeObject(
                            record.get("date"),
                            ""
                    );

            try {

                LocalDate localDate =
                        LocalDate.parse(date);

                String day =
                        localDate
                                .getDayOfWeek()
                                .toString()
                                .substring(0, 3);

                dayMap.put(
                        day,
                        dayMap.getOrDefault(
                                day,
                                0
                        ) + 1
                );

            } catch (Exception ignored) {
            }
        }

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis(
                        0,
                        1,
                        1
                );

        xAxis.setLabel("Day");
        yAxis.setLabel("Present");

        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );

        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(240);

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        String[] days = {
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI",
                "SAT",
                "SUN"
        };

        for (
                String day :
                days
        ) {

            series.getData().add(
                    new XYChart.Data<>(
                            day,
                            dayMap.getOrDefault(
                                    day,
                                    0
                            ) > 0
                                    ? 1
                                    : 0
                    )
            );
        }

        chart.getData().add(series);

        box.getChildren().add(
                chart
        );

        return box;
    }

    private static VBox createFeeStatus(
            double totalFees,
            double paidFees,
            double pendingFees,
            String feeStatus
    ) {

        VBox box =
                whitePanel();

        double progressValue =
                totalFees > 0
                        ? Math.min(
                                1,
                                paidFees / totalFees
                        )
                        : 0;

        ProgressBar progress =
                new ProgressBar(
                        progressValue
                );

        progress.setMaxWidth(
                Double.MAX_VALUE
        );

        progress.setPrefHeight(15);

        box.getChildren().addAll(

                sectionTitle(
                        "💳 Fee Status"
                ),

                pageText(
                        formatCurrency(
                                totalFees
                        ),
                        30,
                        true,
                        text()
                ),

                pageText(
                        "Total Hostel Fees",
                        13,
                        false,
                        muted()
                ),

                progress,

                pageText(
                        formatCurrency(
                                paidFees
                        ) +
                                " Paid",
                        15,
                        true,
                        GREEN
                ),

                pageText(
                        formatCurrency(
                                pendingFees
                        ) +
                                " Pending",
                        15,
                        true,
                        pendingFees > 0
                                ? RED
                                : GREEN
                ),

                pageText(
                        "Status : " +
                                safe(feeStatus),
                        13,
                        true,
                        pendingFees > 0
                                ? RED
                                : GREEN
                )
        );

        Region spacer =
                new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        Button pay =
                primaryButton(
                        pendingFees > 0
                                ? "PAY FEES"
                                : "VIEW PAYMENT"
                );

        pay.setOnAction(
                e -> openPaymentPage()
        );

        box.getChildren().addAll(
                spacer,
                pay
        );

        return box;
    }

    private static VBox createAnnouncements() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "📢 Latest Notices"
                )
        );

        try {

            List<Map<String, Object>> notices =
                    noticeDao.getNotices();

            notices.sort(
                    (a, b) -> {

                        Object first =
                                a.get("createdAt");

                        Object second =
                                b.get("createdAt");

                        if (
                                first == null &&
                                second == null
                        ) {
                            return 0;
                        }

                        if (first == null) {
                            return 1;
                        }

                        if (second == null) {
                            return -1;
                        }

                        return String.valueOf(second)
                                .compareTo(
                                        String.valueOf(first)
                                );
                    }
            );

            int count = 0;

            for (
                    Map<String, Object> notice :
                    notices
            ) {

                String title =
                        safeValue(
                                safeObject(
                                        notice.get("title"),
                                        "Notice"
                                ),
                                "Notice"
                        );

                String message =
                        safeValue(
                                safeObject(
                                        notice.get("message"),
                                        ""
                                ),
                                ""
                        );

                box.getChildren().add(
                        announcement(
                                title,
                                message
                        )
                );

                count++;

                if (count >= 5) {
                    break;
                }
            }

            if (count == 0) {

                box.getChildren().add(
                        infoLabel(
                                "No notices available."
                        )
                );
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load notices."
                    )
            );
        }

        Button view =
                secondaryButton(
                        "VIEW ALL NOTICES"
                );

        view.setOnAction(
                e -> rebuild("notices")
        );

        box.getChildren().add(
                view
        );

        return box;
    }

    private static VBox createTodayMenu() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "🍽 Today's Menu"
                )
        );

        try {

            DocumentSnapshot doc =
                    FirebaseConfig
                            .getFireStore()
                            .collection("messMenu")
                            .document(
                                    LocalDate.now()
                                            .toString()
                            )
                            .get()
                            .get();

            if (!doc.exists()) {

                box.getChildren().add(
                        infoLabel(
                                "Today's menu has not been updated."
                        )
                );

            } else {

                box.getChildren().addAll(

                        menuRow(
                                "🌅",
                                "Breakfast",
                                safeValue(
                                        doc.getString(
                                                "breakfast"
                                        ),
                                        "Not updated"
                                )
                        ),

                        menuRow(
                                "☀",
                                "Lunch",
                                safeValue(
                                        doc.getString(
                                                "lunch"
                                        ),
                                        "Not updated"
                                )
                        ),

                        menuRow(
                                "☕",
                                "Snacks",
                                safeValue(
                                        doc.getString(
                                                "snacks"
                                        ),
                                        "Not updated"
                                )
                        ),

                        menuRow(
                                "🌙",
                                "Dinner",
                                safeValue(
                                        doc.getString(
                                                "dinner"
                                        ),
                                        "Not updated"
                                )
                        )
                );
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load today's menu."
                    )
            );
        }

        return box;
    }

    private static VBox createRecentActivity() {

        VBox box =
                whitePanel();

        try {

            StudentRequestDao dao =
                    new StudentRequestDao();

            List<Map<String, Object>> complaints =
                    dao.getStudentComplaints(
                            student.getUid()
                    );

            List<Map<String, Object>> leaves =
                    dao.getStudentLeaveRequests(
                            student.getUid()
                    );

            List<Map<String, Object>> emergencies =
                    dao.getStudentEmergencies(
                            student.getUid()
                    );

            int count = 0;

            for (
                    Map<String, Object> item :
                    complaints
            ) {

                if (count >= 5) {
                    break;
                }

                box.getChildren().add(
                        activityRow(
                                "📝",
                                "Complaint • " +
                                        safeObject(
                                                item.get("category"),
                                                "General"
                                        ),
                                safeObject(
                                        item.get("status"),
                                        "Pending"
                                )
                        )
                );

                count++;
            }

            for (
                    Map<String, Object> item :
                    leaves
            ) {

                if (count >= 5) {
                    break;
                }

                box.getChildren().add(
                        activityRow(
                                "📅",
                                "Leave Request • " +
                                        safeObject(
                                                item.get(
                                                        "leaveType"
                                                ),
                                                "Leave"
                                        ),
                                safeObject(
                                        item.get("status"),
                                        "Pending"
                                )
                        )
                );

                count++;
            }

            for (
                    Map<String, Object> item :
                    emergencies
            ) {

                if (count >= 5) {
                    break;
                }

                box.getChildren().add(
                        activityRow(
                                "🆘",
                                "Emergency • " +
                                        safeObject(
                                                item.get(
                                                        "emergencyType"
                                                ),
                                                "Emergency"
                                        ),
                                safeObject(
                                        item.get("status"),
                                        "Urgent"
                                )
                        )
                );

                count++;
            }

            if (count == 0) {

                box.getChildren().add(
                        infoLabel(
                                "No recent activity."
                        )
                );
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load recent activity."
                    )
            );
        }

        return box;
    }

    private static ScrollPane createPaymentsPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        Map<String, Object> feeData =
                getFeeData();

        double total =
                getDouble(
                        feeData,
                        "totalFees"
                );

        double paid =
                getDouble(
                        feeData,
                        "paidFees"
                );

        double pending =
                getDouble(
                        feeData,
                        "pendingFees"
                );

        String status =
                safeObject(
                        feeData.get("feeStatus"),
                        "PENDING"
                );

        Button pay =
                primaryButton(
                        pending > 0
                                ? "PAY FEES"
                                : "VIEW PAYMENT"
                );

        pay.setOnAction(
                e -> openPaymentPage()
        );

        content.getChildren().addAll(

                pageHeading(
                        "💳 Payments",
                        "Manage your hostel fee payments."
                ),

                infoPanel(
                        "Fee Summary",
                        "Total Fees : " +
                                formatCurrency(total),
                        "Paid Fees : " +
                                formatCurrency(paid),
                        "Remaining Fees : " +
                                formatCurrency(pending),
                        "Payment Status : " +
                                status
                ),

                pay,

                createPaymentHistory()
        );

        return createScroll(content);
    }

    private static VBox createPaymentHistory() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "Payment History"
                )
        );

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("payments")
                            .whereEqualTo(
                                    "studentId",
                                    student.getUid()
                            )
                            .get()
                            .get();

            List<QueryDocumentSnapshot> payments =
                    snapshot.getDocuments();

            payments.sort(
                    Comparator.comparing(
                            doc ->
                                    safeObject(
                                            doc.getData()
                                                    .get(
                                                            "paymentDate"
                                                    ),
                                            ""
                                    ),
                            Comparator.reverseOrder()
                    )
            );

            if (payments.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No payment records found."
                        )
                );

            } else {

                for (
                        QueryDocumentSnapshot payment :
                        payments
                ) {

                    double amount =
                            getDouble(
                                    payment.getData(),
                                    "amount"
                            );

                    String date =
                            safeObject(
                                    payment.getData()
                                            .get(
                                                    "paymentDate"
                                            ),
                                    "Date unavailable"
                            );

                    String receipt =
                            safeObject(
                                    payment.getData()
                                            .get(
                                                    "receiptNumber"
                                            ),
                                    payment.getId()
                            );

                    String status =
                            safeObject(
                                    payment.getData()
                                            .get(
                                                    "status"
                                            ),
                                    "Unknown"
                            );

                    box.getChildren().add(
                            activityRow(
                                    "💳",
                                    formatCurrency(amount) +
                                            " • " +
                                            date +
                                            " • Receipt " +
                                            receipt,
                                    status
                            )
                    );
                }
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load payment history."
                    )
            );
        }

        return box;
    }

    private static ScrollPane createComplaintsPage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30)
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
                "Security",
                "Other"
        );

        category.setPromptText(
                "Select complaint category"
        );

        category.setPrefWidth(400);

        category.setStyle(
                inputStyle()
        );

        TextArea description =
                new TextArea();

        description.setPromptText(
                "Describe your complaint..."
        );

        description.setPrefHeight(150);

        description.setStyle(
                inputStyle()
        );

        Button submit =
                primaryButton(
                        "SUBMIT COMPLAINT"
                );

        submit.setOnAction(
                e -> {

                    if (
                            category.getValue() == null ||
                            description.getText()
                                    .trim()
                                    .isEmpty()
                    ) {

                        showMessage(
                                "Please fill all details."
                        );

                        return;
                    }

                    boolean result =
                            new StudentRequestDao()
                                    .saveComplaint(
                                            student.getUid(),
                                            category.getValue(),
                                            description.getText()
                                                    .trim()
                                    );

                    if (result) {

                        category.setValue(null);

                        description.clear();

                        showMessage(
                                "Complaint submitted successfully."
                        );

                        rebuild(
                                "complaints"
                        );

                    } else {

                        showMessage(
                                "Failed to submit complaint."
                        );
                    }
                }
        );

        content.getChildren().addAll(

                pageHeading(
                        "📝 Complaints",
                        "Submit and track your hostel complaints."
                ),

                fieldLabel(
                        "Complaint Category"
                ),

                category,

                fieldLabel(
                        "Description"
                ),

                description,

                submit,

                createStudentComplaints()
        );

        return createScroll(content);
    }

    private static VBox createStudentComplaints() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "My Complaints"
                )
        );

        try {

            List<Map<String, Object>> list =
                    new StudentRequestDao()
                            .getStudentComplaints(
                                    student.getUid()
                            );

            if (list.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No complaints submitted."
                        )
                );

            } else {

                for (
                        Map<String, Object> item :
                        list
                ) {

                    String category =
                            safeObject(
                                    item.get("category"),
                                    "General"
                            );

                    String description =
                            safeObject(
                                    item.get("description"),
                                    ""
                            );

                    String status =
                            safeObject(
                                    item.get("status"),
                                    "Pending"
                            );

                    box.getChildren().add(
                            activityRow(
                                    "📝",
                                    category +
                                            " • " +
                                            description,
                                    status
                            )
                    );
                }
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load complaints."
                    )
            );
        }

        return box;
    }

    private static ScrollPane createLeavePage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30)
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
                "Select leave type"
        );

        type.setStyle(
                inputStyle()
        );

        DatePicker from =
                new DatePicker();

        DatePicker to =
                new DatePicker();

        from.setStyle(
                inputStyle()
        );

        to.setStyle(
                inputStyle()
        );

        TextArea reason =
                new TextArea();

        reason.setPromptText(
                "Reason for leave..."
        );

        reason.setPrefHeight(120);

        reason.setStyle(
                inputStyle()
        );

        Button submit =
                primaryButton(
                        "SUBMIT LEAVE"
                );

        submit.setOnAction(
                e -> {

                    if (
                            type.getValue() == null ||
                            from.getValue() == null ||
                            to.getValue() == null ||
                            reason.getText()
                                    .trim()
                                    .isEmpty()
                    ) {

                        showMessage(
                                "Please fill all details."
                        );

                        return;
                    }

                    if (
                            to.getValue()
                                    .isBefore(
                                            from.getValue()
                                    )
                    ) {

                        showMessage(
                                "To Date cannot be before From Date."
                        );

                        return;
                    }

                    boolean result =
                            new StudentRequestDao()
                                    .saveLeaveRequest(
                                            student.getUid(),
                                            type.getValue(),
                                            from.getValue().toString(),
                                            to.getValue().toString(),
                                            reason.getText()
                                                    .trim()
                                    );

                    if (result) {

                        type.setValue(null);

                        from.setValue(null);

                        to.setValue(null);

                        reason.clear();

                        showMessage(
                                "Leave application submitted successfully."
                        );

                        rebuild(
                                "leave"
                        );

                    } else {

                        showMessage(
                                "Failed to submit leave."
                        );
                    }
                }
        );

        content.getChildren().addAll(

                pageHeading(
                        "📅 Leave Application",
                        "Apply for hostel leave and track approval."
                ),

                fieldLabel(
                        "Leave Type"
                ),

                type,

                fieldLabel(
                        "From Date"
                ),

                from,

                fieldLabel(
                        "To Date"
                ),

                to,

                fieldLabel(
                        "Reason"
                ),

                reason,

                submit,

                createStudentLeaves()
        );

        return createScroll(content);
    }

    private static VBox createStudentLeaves() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "My Leave Requests"
                )
        );

        try {

            List<Map<String, Object>> list =
                    new StudentRequestDao()
                            .getStudentLeaveRequests(
                                    student.getUid()
                            );

            if (list.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No leave requests found."
                        )
                );

            } else {

                for (
                        Map<String, Object> item :
                        list
                ) {

                    String type =
                            safeObject(
                                    item.get("leaveType"),
                                    "Leave"
                            );

                    String from =
                            safeObject(
                                    item.get("fromDate"),
                                    ""
                            );

                    String to =
                            safeObject(
                                    item.get("toDate"),
                                    ""
                            );

                    String reason =
                            safeObject(
                                    item.get("reason"),
                                    ""
                            );

                    String status =
                            safeObject(
                                    item.get("status"),
                                    "Pending"
                            );

                    VBox request =
                            whitePanel();

                    request.getChildren().addAll(

                            pageText(
                                    "📅  " + type,
                                    16,
                                    true,
                                    text()
                            ),

                            infoLabel(
                                    "From : " +
                                            from +
                                            "     To : " +
                                            to
                            ),

                            infoLabel(
                                    "Reason : " +
                                            reason
                            ),

                            statusBadge(
                                    status
                            )
                    );

                    box.getChildren().add(
                            request
                    );
                }
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load leave requests."
                    )
            );
        }

        return box;
    }

    private static ScrollPane createAttendancePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        VBox heading =
                pageHeading(
                        "📍 Night Hostel Attendance",
                        "Mark your night presence using hostel location verification."
                );

        VBox status =
                whitePanel();

        Label date =
                infoLabel(
                        "Date : " +
                                LocalDate.now()
                );

        Label time =
                infoLabel(
                        "Current Time : " +
                                LocalTime.now()
                                        .format(
                                                DateTimeFormatter.ofPattern(
                                                        "hh:mm a"
                                                )
                                        )
                );

        Label location =
                infoLabel(
                        "📍 Location : Not Checked"
                );

        Label permission =
                infoLabel(
                        "🟠 Location Service : Required"
                );

        Button enable =
                primaryButton(
                        "📍 ENABLE LOCATION"
                );

        Button checkIn =
                primaryButton(
                        "🌙 MARK NIGHT PRESENCE"
                );

        Button checkOut =
                dangerButton(
                        "🔴 CHECK OUT"
                );

        checkIn.setDisable(true);

        enable.setOnAction(
                e -> {

                    permission.setText(
                            "🟢 Location Service : ENABLED"
                    );

                    permission.setTextFill(
                            Color.web(GREEN)
                    );

                    location.setText(
                            "📍 Location : Ready for verification"
                    );

                    location.setTextFill(
                            Color.web(GREEN)
                    );

                    checkIn.setDisable(false);
                }
        );

        checkIn.setOnAction(
                e -> saveCheckIn(
                        location,
                        checkIn
                )
        );

        checkOut.setOnAction(
                e -> saveCheckOut(
                        checkOut
                )
        );

        HBox buttons =
                new HBox(
                        15,
                        enable,
                        checkIn,
                        checkOut
                );

        status.getChildren().addAll(
                sectionTitle(
                        "Today's Night Presence"
                ),
                date,
                time,
                permission,
                location,
                buttons
        );

        VBox verification =
                whitePanel();

        verification.getChildren().addAll(

                sectionTitle(
                        "📍 Hostel Location Verification"
                ),

                infoLabel(
                        "Hostel Latitude : " +
                                HOSTEL_LATITUDE
                ),

                infoLabel(
                        "Hostel Longitude : " +
                                HOSTEL_LONGITUDE
                ),

                infoLabel(
                        "Allowed Radius : " +
                                HOSTEL_RADIUS +
                                " meters"
                ),

                infoLabel(
                        "Only students within the allowed hostel radius can mark presence."
                )
        );

        content.getChildren().addAll(
                heading,
                status,
                verification,
                createAttendanceHistory()
        );

        loadTodayAttendance(
                location,
                checkIn,
                checkOut
        );

        return createScroll(content);
    }

    private static VBox createAttendanceHistory() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "Attendance History"
                )
        );

        List<Map<String, Object>> records =
                getAttendanceData();

        if (records.isEmpty()) {

            box.getChildren().add(
                    infoLabel(
                            "No attendance records found."
                    )
            );

            return box;
        }

        records.sort(
                Comparator.comparing(
                        item ->
                                safeObject(
                                        item.get("date"),
                                        ""
                                ),
                        Comparator.reverseOrder()
                )
        );

        for (
                Map<String, Object> record :
                records
        ) {

            String date =
                    safeObject(
                            record.get("date"),
                            ""
                    );

            String in =
                    safeObject(
                            record.get(
                                    "checkInTime"
                            ),
                            "-"
                    );

            String out =
                    safeObject(
                            record.get(
                                    "checkOutTime"
                            ),
                            "-"
                    );

            String status =
                    safeObject(
                            record.get(
                                    "status"
                            ),
                            "Present"
                    );

            box.getChildren().add(
                    activityRow(
                            "📍",
                            date +
                                    " • In: " +
                                    in +
                                    " • Out: " +
                                    out,
                            status
                    )
            );
        }

        return box;
    }

    private static void saveCheckIn(
            Label location,
            Button checkIn
    ) {

        double distance =
                calculateDistance(
                        HOSTEL_LATITUDE,
                        HOSTEL_LONGITUDE,
                        HOSTEL_LATITUDE,
                        HOSTEL_LONGITUDE
                );

        if (
                distance > HOSTEL_RADIUS
        ) {

            showMessage(
                    "You are outside the hostel location."
            );

            return;
        }

        String date =
                LocalDate.now().toString();

        String time =
                LocalTime.now().format(
                        DateTimeFormatter.ofPattern(
                                "hh:mm a"
                        )
                );

        boolean result =
                new StudentRequestDao()
                        .saveAttendance(
                                student.getUid(),
                                date,
                                time,
                                HOSTEL_LATITUDE,
                                HOSTEL_LONGITUDE,
                                distance
                        );

        if (result) {

            location.setText(
                    "🟢 Location : VERIFIED • " +
                            String.format(
                                    "%.2f meters",
                                    distance
                            )
            );

            location.setTextFill(
                    Color.web(GREEN)
            );

            checkIn.setDisable(true);

            showMessage(
                    "Night presence marked successfully."
            );

        } else {

            showMessage(
                    "Unable to mark night presence."
            );
        }
    }

    private static void saveCheckOut(
            Button checkOut
    ) {

        String date =
                LocalDate.now().toString();

        String time =
                LocalTime.now().format(
                        DateTimeFormatter.ofPattern(
                                "hh:mm a"
                        )
                );

        double distance =
                calculateDistance(
                        HOSTEL_LATITUDE,
                        HOSTEL_LONGITUDE,
                        HOSTEL_LATITUDE,
                        HOSTEL_LONGITUDE
                );

        boolean result =
                new StudentRequestDao()
                        .updateCheckOut(
                                student.getUid(),
                                date,
                                time,
                                HOSTEL_LATITUDE,
                                HOSTEL_LONGITUDE,
                                distance
                        );

        if (result) {

            checkOut.setDisable(true);

            showMessage(
                    "Check-out recorded successfully."
            );

        } else {

            showMessage(
                    "Unable to record check-out."
            );
        }
    }

    private static void loadTodayAttendance(
            Label location,
            Button checkIn,
            Button checkOut
    ) {

        try {

            DocumentSnapshot doc =
                    FirebaseConfig
                            .getFireStore()
                            .collection("students")
                            .document(
                                    student.getUid()
                            )
                            .collection("attendance")
                            .document(
                                    LocalDate.now()
                                            .toString()
                            )
                            .get()
                            .get();

            if (!doc.exists()) {
                return;
            }

            String in =
                    doc.getString(
                            "checkInTime"
                    );

            String out =
                    doc.getString(
                            "checkOutTime"
                    );

            if (
                    in != null &&
                    !in.isEmpty()
            ) {

                checkIn.setDisable(true);
            }

            if (
                    out != null &&
                    !out.isEmpty()
            ) {

                checkOut.setDisable(true);
            }

            if (
                    Boolean.TRUE.equals(
                            doc.getBoolean(
                                    "locationVerified"
                            )
                    )
            ) {

                Double distance =
                        doc.getDouble(
                                "checkInDistance"
                        );

                location.setText(
                        "🟢 Location : Verified" +
                                (
                                        distance == null
                                                ? ""
                                                : " • " +
                                                String.format(
                                                        "%.2f meters",
                                                        distance
                                                )
                                )
                );

                location.setTextFill(
                        Color.web(GREEN)
                );
            }

        } catch (Exception e) {

            location.setText(
                    "📍 Location : Unable to check"
            );
        }
    }

    private static ScrollPane createMessPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        content.getChildren().addAll(

                pageHeading(
                        "🍽 Mess Menu",
                        "Today's menu updated by hostel administration."
                ),

                createTodayMenu()
        );

        return createScroll(content);
    }

    private static ScrollPane createNoticesPage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30)
        );

        content.getChildren().add(
                pageHeading(
                        "📢 Notices",
                        "Latest hostel notices and announcements."
                )
        );

        try {

            Firestore db =
                    FirebaseConfig.getFireStore();

            QuerySnapshot snapshot =
                    db.collection("notices")
                            .get()
                            .get();

            if (snapshot.isEmpty()) {

                content.getChildren().add(
                        whitePanelWithMessage(
                                "No notices available."
                        )
                );

            } else {

                FirestoreWardenDao wardenDao =
                        new FirestoreWardenDao();

                for (
                        QueryDocumentSnapshot doc :
                        snapshot.getDocuments()
                ) {

                    Map<String, Object> data =
                            doc.getData();

                    String title =
                            data.get("title") != null
                                    ? String.valueOf(
                                    data.get("title")
                            )
                                    : "Notice";

                    String message =
                            data.get("message") != null
                                    ? String.valueOf(
                                    data.get("message")
                            )
                                    : "";

                    String createdByUid =
                            data.get("createdBy") != null
                                    ? String.valueOf(
                                    data.get("createdBy")
                            )
                                    : "";

                    String createdBy =
                            "Hostel Administration";

                    if (
                            !createdByUid.isEmpty()
                    ) {

                        Warden warden =
                                wardenDao.getWardenByUid(
                                        createdByUid
                                );

                        if (
                                warden != null &&
                                warden.getFullName() != null &&
                                !warden.getFullName().isEmpty()
                        ) {

                            createdBy =
                                    warden.getFullName();
                        }
                    }

                    VBox notice =
                            whitePanel();

                    notice.getChildren().addAll(

                            pageText(
                                    "📢 " + title,
                                    18,
                                    true,
                                    text()
                            ),

                            pageText(
                                    message,
                                    14,
                                    false,
                                    secondary()
                            ),

                            pageText(
                                    "Published by : " +
                                            createdBy,
                                    11,
                                    false,
                                    muted()
                            )
                    );

                    content.getChildren().add(
                            notice
                    );
                }
            }

        } catch (Exception e) {

            content.getChildren().add(
                    whitePanelWithMessage(
                            "Unable to load notices."
                    )
            );
        }

        return createScroll(content);
    }

    private static ScrollPane createRoomPage() {

        return simpleInfoPage(

                "🛏 My Room",

                "Your hostel room information.",

                "Student Name : " +
                        displayName,

                "Room Number : " +
                        safe(
                                student.getRoomNumber()
                        ),

                "Room Preference : " +
                        safe(
                                student.getRoomPreference()
                        ),

                "Building : Not Assigned",

                "Floor : Not Assigned",

                "Bed Number : Not Assigned"
        );
    }

    private static ScrollPane createScholarshipPage() {

        VBox content =
                new VBox(22);

        content.setPadding(
                new Insets(
                        30,
                        35,
                        40,
                        35
                )
        );

        VBox heading =
                pageHeading(
                        "🎓 Scholarship",
                        "Explore Maharashtra Government scholarship schemes and apply through MahaDBT."
                );

        VBox mahaDBT =
                whitePanel();

        mahaDBT.getChildren().addAll(

                sectionTitle(
                        "🏛 MahaDBT Scholarship Portal"
                ),

                infoLabel(
                        "MahaDBT is the official Maharashtra Government scholarship portal. "
                                + "Eligible students can apply for available scholarship "
                                + "schemes according to their category, course and eligibility."
                )
        );

        Button apply =
                primaryButton(
                        "🎓 APPLY NOW ON MAHADBT"
                );

        apply.setOnAction(
                e -> openMahaDBT()
        );

        mahaDBT.getChildren().add(
                apply
        );

        VBox schemes =
                whitePanel();

        schemes.getChildren().add(
                sectionTitle(
                        "📚 Scholarship Categories"
                )
        );

        schemes.getChildren().add(
                scholarshipCard(
                        "🎓 Post Matric Scholarship",
                        "Financial support for eligible students pursuing "
                                + "higher education after matriculation.",
                        "Category, family income, course and government rules."
                )
        );

        schemes.getChildren().add(
                scholarshipCard(
                        "💰 Tuition Fee & Examination Fee",
                        "Financial assistance towards eligible tuition and "
                                + "examination fees.",
                        "Eligibility depends on the selected scheme."
                )
        );

        schemes.getChildren().add(
                scholarshipCard(
                        "🏫 Government Scholarship Schemes",
                        "Different Maharashtra Government scholarship and "
                                + "freeship schemes are available for eligible students.",
                        "Students must select the scheme applicable to them."
                )
        );

        VBox documents =
                whitePanel();

        documents.getChildren().add(
                sectionTitle(
                        "📄 Documents Usually Required"
                )
        );

        documents.getChildren().addAll(

                infoLabel("• Aadhaar Card"),

                infoLabel("• Domicile Certificate"),

                infoLabel("• Income Certificate"),

                infoLabel("• Caste Certificate, if applicable"),

                infoLabel("• Previous Year Marksheet"),

                infoLabel("• College Admission / Bonafide Certificate"),

                infoLabel("• Bank Account Details"),

                infoLabel(
                        "• Other documents required by the selected scheme"
                )
        );

        VBox process =
                whitePanel();

        process.getChildren().add(
                sectionTitle(
                        "📝 Application Process"
                )
        );

        process.getChildren().addAll(

                infoLabel(
                        "1. Click on APPLY NOW ON MAHADBT."
                ),

                infoLabel(
                        "2. Register or login to the MahaDBT portal."
                ),

                infoLabel(
                        "3. Complete your student profile."
                ),

                infoLabel(
                        "4. Select the scholarship scheme applicable to you."
                ),

                infoLabel(
                        "5. Upload the required documents."
                ),

                infoLabel(
                        "6. Submit the scholarship application."
                ),

                infoLabel(
                        "7. Track your scholarship application status."
                )
        );

        VBox note =
                whitePanel();

        note.getChildren().addAll(

                sectionTitle(
                        "⚠ Important"
                ),

                infoLabel(
                        "Scholarship eligibility, documents, income limits and "
                                + "scheme availability may change according to government rules. "
                                + "Always verify the latest information on the official MahaDBT portal."
                )
        );

        content.getChildren().addAll(
                heading,
                mahaDBT,
                schemes,
                documents,
                process,
                note
        );

        return createScroll(content);
    }

    private static VBox scholarshipCard(
            String title,
            String description,
            String eligibility
    ) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(18)
        );

        card.setStyle(
                "-fx-background-color:" +
                        inputBg() +
                        ";" +
                        "-fx-background-radius:12;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:12;"
        );

        Label titleLabel =
                pageText(
                        title,
                        16,
                        true,
                        text()
                );

        Label descriptionLabel =
                infoLabel(
                        description
                );

        Label eligibilityLabel =
                infoLabel(
                        "Eligibility : " +
                                eligibility
                );

        card.getChildren().addAll(
                titleLabel,
                descriptionLabel,
                eligibilityLabel
        );

        return card;
    }

    private static void openMahaDBT() {

        try {

            java.awt.Desktop.getDesktop().browse(
                    new java.net.URI(
                            "https://mahadbt.maharashtra.gov.in/"
                    )
            );

        } catch (Exception e) {

            showMessage(
                    "Unable to open MahaDBT portal."
            );
        }
    }

    private static ScrollPane createServicesPage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        HBox services =
                new HBox(15);

        Button complaint =
                quickButton(
                        "📝",
                        "Complaint",
                        ORANGE
                );

        Button leave =
                quickButton(
                        "📅",
                        "Leave",
                        PRIMARY
                );

        Button emergency =
                quickButton(
                        "🆘",
                        "Emergency",
                        RED
                );

        complaint.setOnAction(
                e -> rebuild("complaints")
        );

        leave.setOnAction(
                e -> rebuild("leave")
        );

        emergency.setOnAction(
                e -> rebuild("emergency")
        );

        services.getChildren().addAll(
                complaint,
                leave,
                emergency
        );

        content.getChildren().addAll(

                pageHeading(
                        "🔧 Services",
                        "Hostel services and support."
                ),

                services
        );

        return createScroll(content);
    }

    private static ScrollPane createProfilePage() {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        Button edit =
                primaryButton(
                        "✏ EDIT PROFILE"
                );

        edit.setOnAction(
                e -> showEditProfileDialog()
        );

        content.getChildren().addAll(

                pageHeading(
                        "👤 My Profile",
                        "Your registered student information."
                ),

                infoPanel(

                        "Student Information",

                        "Full Name : " +
                                displayName,

                        "Email : " +
                                safe(
                                        student.getEmail()
                                ),

                        "Phone : " +
                                safe(
                                        student.getPhone()
                                ),

                        "Parent Name : " +
                                safe(
                                        student.getParentName()
                                ),

                        "Date of Birth : " +
                                safe(
                                        student.getDateOfBirth()
                                ),

                        "Gender : " +
                                safe(
                                        student.getGender()
                                ),

                        "Course : " +
                                safe(
                                        student.getCourse()
                                ),

                        "Year : " +
                                safe(
                                        student.getYear()
                                ),

                        "College : " +
                                safe(
                                        student.getCollege()
                                ),

                        "City : " +
                                safe(
                                        student.getCity()
                                ),

                        "Room Preference : " +
                                safe(
                                        student.getRoomPreference()
                                ),

                        "Room Number : " +
                                safe(
                                        student.getRoomNumber()
                                ),

                        "Address : " +
                                safe(
                                        student.getAddress()
                                )
                ),

                edit
        );

        return createScroll(content);
    }

    private static ScrollPane createEmergencyPage() {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(30)
        );

        ComboBox<String> type =
                new ComboBox<>();

        type.getItems().addAll(

                "Medical Emergency",
                "Accident",
                "Security Issue",
                "Family Emergency",
                "Harassment",
                "Other"
        );

        type.setPromptText(
                "Select emergency type"
        );

        type.setStyle(
                inputStyle()
        );

        TextArea description =
                new TextArea();

        description.setPromptText(
                "Describe your emergency..."
        );

        description.setPrefHeight(150);

        description.setStyle(
                inputStyle()
        );

        Button send =
                dangerButton(
                        "SEND EMERGENCY ALERT"
                );

        send.setOnAction(
                e -> {

                    if (
                            type.getValue() == null ||
                            description.getText()
                                    .trim()
                                    .isEmpty()
                    ) {

                        showMessage(
                                "Please enter emergency details."
                        );

                        return;
                    }

                    Alert confirm =
                            new Alert(
                                    Alert.AlertType.CONFIRMATION,
                                    "Hostel administration will be notified.",
                                    ButtonType.OK,
                                    ButtonType.CANCEL
                            );

                    confirm.setTitle(
                            "Emergency Alert"
                    );

                    confirm.setHeaderText(
                            "Send Emergency Alert?"
                    );

                    if (
                            confirm.showAndWait()
                                    .orElse(
                                            ButtonType.CANCEL
                                    )
                                    != ButtonType.OK
                    ) {

                        return;
                    }

                    boolean result =
                            new StudentRequestDao()
                                    .saveEmergency(
                                            student.getUid(),
                                            type.getValue(),
                                            description.getText()
                                                    .trim()
                                    );

                    if (result) {

                        type.setValue(null);

                        description.clear();

                        showMessage(
                                "Emergency alert sent successfully."
                        );

                        rebuild(
                                "emergency"
                        );

                    } else {

                        showMessage(
                                "Failed to send emergency alert."
                        );
                    }
                }
        );

        content.getChildren().addAll(

                pageHeading(
                        "🆘 Emergency Help",
                        "Send an urgent alert to hostel administration."
                ),

                fieldLabel(
                        "Emergency Type"
                ),

                type,

                fieldLabel(
                        "Description"
                ),

                description,

                send,

                createEmergencyHistory()
        );

        return createScroll(content);
    }

    private static VBox createEmergencyHistory() {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(
                        "My Emergency Alerts"
                )
        );

        try {

            List<Map<String, Object>> list =
                    new StudentRequestDao()
                            .getStudentEmergencies(
                                    student.getUid()
                            );

            if (list.isEmpty()) {

                box.getChildren().add(
                        infoLabel(
                                "No emergency alerts found."
                        )
                );

            } else {

                for (
                        Map<String, Object> item :
                        list
                ) {

                    String type =
                            safeObject(
                                    item.get(
                                            "emergencyType"
                                    ),
                                    "Emergency"
                            );

                    String description =
                            safeObject(
                                    item.get(
                                            "description"
                                    ),
                                    ""
                            );

                    String status =
                            safeObject(
                                    item.get("status"),
                                    "Urgent"
                            );

                    VBox alert =
                            whitePanel();

                    alert.getChildren().addAll(

                            pageText(
                                    "🆘  " + type,
                                    16,
                                    true,
                                    text()
                            ),

                            infoLabel(
                                    "Details : " +
                                            description
                            ),

                            statusBadge(
                                    status
                            )
                    );

                    box.getChildren().add(
                            alert
                    );
                }
            }

        } catch (Exception e) {

            box.getChildren().add(
                    infoLabel(
                            "Unable to load emergency history."
                    )
            );
        }

        return box;
    }

    private static ScrollPane createSettingsPage() {

        VBox content =
                new VBox(22);

        content.setPadding(
                new Insets(30)
        );

        content.getChildren().add(
                pageHeading(
                        "⚙ Settings",
                        "Manage your dashboard preferences."
                )
        );

        VBox appearance =
                whitePanel();

        appearance.getChildren().addAll(

                sectionTitle(
                        "🎨 Appearance"
                ),

                settingDescription(
                        "Choose your preferred dashboard theme."
                )
        );

        ComboBox<String> theme =
                new ComboBox<>();

        theme.getItems().addAll(
                "Light",
                "Dark"
        );

        theme.setValue(
                darkTheme
                        ? "Dark"
                        : "Light"
        );

        theme.setPrefWidth(230);

        theme.setStyle(
                inputStyle()
        );

        theme.setOnAction(
                e -> {

                    darkTheme =
                            "Dark".equals(
                                    theme.getValue()
                            );

                    rebuild(
                            "settings"
                    );
                }
        );

        appearance.getChildren().add(
                theme
        );

        VBox account =
                whitePanel();

        account.getChildren().addAll(

                sectionTitle(
                        "👤 Account"
                ),

                settingDescription(
                        "Update your profile or password."
                )
        );

        Button edit =
                primaryButton(
                        "EDIT PROFILE"
                );

        edit.setOnAction(
                e -> showEditProfileDialog()
        );

        Button password =
                primaryButton(
                        "🔐 CHANGE PASSWORD"
                );

        password.setOnAction(
                e -> showChangePasswordDialog()
        );

        account.getChildren().addAll(
                edit,
                password
        );

        VBox about =
                whitePanel();

        about.getChildren().addAll(

                sectionTitle(
                        "ℹ About Hostel Hub"
                ),

                settingLabel(
                        "Application : Hostel Hub Student Portal"
                ),

                settingLabel(
                        "Version : 1.0"
                ),

                settingLabel(
                        "Technology : JavaFX + Firebase Firestore"
                )
        );

        content.getChildren().addAll(
                appearance,
                account,
                about
        );

        return createScroll(content);
    }

    private static void showEditProfileDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Edit Profile"
        );

        dialog.setHeaderText(
                "Update your profile name"
        );

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20)
        );

        TextField name =
                new TextField(
                        displayName
                );

        name.setPromptText(
                "Full Name"
        );

        name.setStyle(
                inputStyle()
        );

        box.getChildren().add(
                name
        );

        dialog.getDialogPane()
                .setContent(box);

        ButtonType save =
                new ButtonType(
                        "SAVE",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        ButtonType.CANCEL
                );

        dialog.setResultConverter(
                button -> {

                    if (button == save) {

                        String newName =
                                name.getText()
                                        .trim();

                        if (newName.isEmpty()) {

                            showMessage(
                                    "Name cannot be empty."
                            );

                            return null;
                        }

                        try {

                            FirebaseConfig
                                    .getFireStore()
                                    .collection("students")
                                    .document(
                                            student.getUid()
                                    )
                                    .update(
                                            "fullName",
                                            newName
                                    )
                                    .get();

                            displayName =
                                    newName;

                            student.setFullName(
                                    newName
                            );

                            rebuild(
                                    "profile"
                            );

                            showMessage(
                                    "Profile updated successfully."
                            );

                        } catch (Exception ex) {

                            showMessage(
                                    "Unable to update profile."
                            );
                        }
                    }

                    return button;
                }
        );

        dialog.showAndWait();
    }

    private static void showChangePasswordDialog() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Change Password"
        );

        dialog.setHeaderText(
                "Update your Hostel Hub password"
        );

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20)
        );

        PasswordField current =
                new PasswordField();

        current.setPromptText(
                "Current Password"
        );

        PasswordField next =
                new PasswordField();

        next.setPromptText(
                "New Password"
        );

        PasswordField confirm =
                new PasswordField();

        confirm.setPromptText(
                "Confirm New Password"
        );

        current.setStyle(
                inputStyle()
        );

        next.setStyle(
                inputStyle()
        );

        confirm.setStyle(
                inputStyle()
        );

        box.getChildren().addAll(
                current,
                next,
                confirm
        );

        dialog.getDialogPane()
                .setContent(box);

        ButtonType update =
                new ButtonType(
                        "UPDATE",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        update,
                        ButtonType.CANCEL
                );

        dialog.setResultConverter(
                button -> {

                    if (button == update) {

                        String oldPassword =
                                current.getText();

                        String newPassword =
                                next.getText();

                        String confirmPassword =
                                confirm.getText();

                        if (
                                oldPassword.isEmpty() ||
                                newPassword.isEmpty() ||
                                confirmPassword.isEmpty()
                        ) {

                            showMessage(
                                    "Please fill all fields."
                            );

                            return null;
                        }

                        if (
                                !newPassword.equals(
                                        confirmPassword
                                )
                        ) {

                            showMessage(
                                    "New passwords do not match."
                            );

                            return null;
                        }

                        if (
                                newPassword.length() < 6
                        ) {

                            showMessage(
                                    "Password must contain at least 6 characters."
                            );

                            return null;
                        }

                        boolean result =
                                FirebaseAuthController
                                        .changePassword(
                                                student.getEmail(),
                                                oldPassword,
                                                newPassword
                                        );

                        showMessage(
                                result
                                        ? "Password updated successfully."
                                        : "Password update failed."
                        );
                    }

                    return button;
                }
        );

        dialog.showAndWait();
    }

    private static Map<String, Object> getFeeData() {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "totalFees",
                0.0
        );

        data.put(
                "paidFees",
                0.0
        );

        data.put(
                "pendingFees",
                0.0
        );

        data.put(
                "feeStatus",
                "PENDING"
        );

        try {

            DocumentSnapshot doc =
                    FirebaseConfig
                            .getFireStore()
                            .collection("students")
                            .document(
                                    student.getUid()
                            )
                            .get()
                            .get();

            if (doc.exists()) {

                Map<String, Object> studentData =
                        doc.getData();

                double totalFee =
                        getDouble(
                                studentData,
                                "totalFee"
                        );

                double paidFee =
                        getDouble(
                                studentData,
                                "paidFee"
                        );

                double remainingFee =
                        getDouble(
                                studentData,
                                "remainingFee"
                        );

                String feeStatus =
                        safeObject(
                                studentData.get(
                                        "feeStatus"
                                ),
                                ""
                        );

                if (totalFee <= 0) {

                    totalFee =
                            getDouble(
                                    studentData,
                                    "totalFees"
                            );
                }

                if (paidFee <= 0) {

                    paidFee =
                            getDouble(
                                    studentData,
                                    "paidFees"
                            );
                }

                if (remainingFee <= 0) {

                    remainingFee =
                            getDouble(
                                    studentData,
                                    "pendingFees"
                            );
                }

                if (
                        remainingFee <= 0 &&
                        totalFee > 0
                ) {

                    remainingFee =
                            Math.max(
                                    0,
                                    totalFee - paidFee
                            );
                }

                if (feeStatus.isEmpty()) {

                    if (remainingFee <= 0) {

                        feeStatus = "PAID";

                    } else if (paidFee > 0) {

                        feeStatus = "PARTIAL";

                    } else {

                        feeStatus = "PENDING";
                    }
                }

                data.put(
                        "totalFees",
                        totalFee
                );

                data.put(
                        "paidFees",
                        paidFee
                );

                data.put(
                        "pendingFees",
                        remainingFee
                );

                data.put(
                        "feeStatus",
                        feeStatus
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return data;
    }

    private static List<Map<String, Object>>
    getAttendanceData() {

        List<Map<String, Object>> result =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    FirebaseConfig
                            .getFireStore()
                            .collection("students")
                            .document(
                                    student.getUid()
                            )
                            .collection("attendance")
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot doc :
                    snapshot.getDocuments()
            ) {

                Map<String, Object> data =
                        new HashMap<>(
                                doc.getData()
                        );

                data.put(
                        "attendanceId",
                        doc.getId()
                );

                result.add(data);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    private static double
    calculateAttendancePercentage(
            List<Map<String, Object>> records
    ) {

        if (records.isEmpty()) {
            return 0;
        }

        int present = 0;

        for (
                Map<String, Object> record :
                records
        ) {

            String status =
                    safeObject(
                            record.get("status"),
                            ""
                    );

            if (
                    status.equalsIgnoreCase("Present") ||
                    status.equalsIgnoreCase("Completed")
            ) {

                present++;
            }
        }

        return (
                present * 100.0
        ) / records.size();
    }

    private static int getPendingLeaveCount() {

        try {

            List<Map<String, Object>> list =
                    new StudentRequestDao()
                            .getStudentLeaveRequests(
                                    student.getUid()
                            );

            int count = 0;

            for (
                    Map<String, Object> item :
                    list
            ) {

                String status =
                        safeObject(
                                item.get("status"),
                                "Pending"
                        );

                if (
                        !isClosedLeave(
                                status
                        )
                ) {

                    count++;
                }
            }

            return count;

        } catch (Exception e) {

            return 0;
        }
    }

    private static boolean isClosedComplaint(
            String status
    ) {

        return status.equalsIgnoreCase(
                "RESOLVED"
        ) ||
                status.equalsIgnoreCase(
                        "REJECTED"
                ) ||
                status.equalsIgnoreCase(
                        "CLOSED"
                );
    }

    private static boolean isClosedLeave(
            String status
    ) {

        return status.equalsIgnoreCase(
                "APPROVED"
        ) ||
                status.equalsIgnoreCase(
                        "REJECTED"
                ) ||
                status.equalsIgnoreCase(
                        "COMPLETED"
                );
    }

    private static boolean isClosedEmergency(
            String status
    ) {

        return status.equalsIgnoreCase(
                "RESOLVED"
        ) ||
                status.equalsIgnoreCase(
                        "CLOSED"
                );
    }

    private static Label statusBadge(
            String status
    ) {

        Label badge =
                new Label(
                        "Status : " +
                                safe(status)
                );

        String color;

        if (
                status.equalsIgnoreCase(
                        "Approved"
                ) ||
                status.equalsIgnoreCase(
                        "Resolved"
                ) ||
                status.equalsIgnoreCase(
                        "Completed"
                ) ||
                status.equalsIgnoreCase(
                        "Paid"
                )
        ) {

            color = GREEN;

        } else if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            color = RED;

        } else if (
                status.equalsIgnoreCase(
                        "Acknowledged"
                )
        ) {

            color = ORANGE;

        } else {

            color = PRIMARY;
        }

        badge.setFont(
                Font.font(
                        "Segoe UI",
                        FontWeight.BOLD,
                        13
                )
        );

        badge.setTextFill(
                Color.web(color)
        );

        return badge;
    }

    private static double getDouble(
            Map<String, Object> data,
            String key
    ) {

        Object value =
                data.get(key);

        if (value == null) {
            return 0;
        }

        if (value instanceof Number) {

            return (
                    (Number) value
            ).doubleValue();
        }

        try {

            return Double.parseDouble(
                    value.toString()
            );

        } catch (Exception e) {

            return 0;
        }
    }

    private static String formatCurrency(
            double amount
    ) {

        return String.format(
                "₹%,.2f",
                amount
        );
    }

    private static double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2
    ) {

        final int EARTH_RADIUS =
                6371000;

        double latDistance =
                Math.toRadians(
                        lat2 - lat1
                );

        double lonDistance =
                Math.toRadians(
                        lon2 - lon1
                );

        double a =
                Math.sin(
                        latDistance / 2
                )
                        * Math.sin(
                        latDistance / 2
                )
                        + Math.cos(
                        Math.toRadians(lat1)
                )
                        * Math.cos(
                        Math.toRadians(lat2)
                )
                        * Math.sin(
                        lonDistance / 2
                )
                        * Math.sin(
                        lonDistance / 2
                );

        return EARTH_RADIUS
                * 2
                * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );
    }

    private static ScrollPane simpleInfoPage(
            String title,
            String subtitle,
            String... values
    ) {

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        content.getChildren().add(
                pageHeading(
                        title,
                        subtitle
                )
        );

        content.getChildren().add(
                infoPanel(
                        title,
                        values
                )
        );

        return createScroll(content);
    }

    private static VBox dashboardCard(
            String icon,
            String title,
            String value,
            String subtitle,
            String accent
    ) {

        VBox box =
                new VBox(7);

        box.setPadding(
                new Insets(20)
        );

        box.setPrefWidth(240);

        box.setMinHeight(145);

        box.setStyle(
                "-fx-background-color:" +
                        card() +
                        ";" +
                        "-fx-background-radius:14;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:14;" +
                        "-fx-cursor:hand;"
        );

        Label iconLabel =
                pageText(
                        icon,
                        25,
                        false,
                        accent
                );

        Label titleLabel =
                pageText(
                        title,
                        11,
                        true,
                        muted()
                );

        Label valueLabel =
                pageText(
                        value,
                        23,
                        true,
                        text()
                );

        Label subtitleLabel =
                pageText(
                        subtitle,
                        11,
                        false,
                        muted()
                );

        box.getChildren().addAll(
                iconLabel,
                titleLabel,
                valueLabel,
                subtitleLabel
        );

        return box;
    }

    private static VBox whitePanel() {

        VBox box =
                new VBox(12);

        box.setPadding(
                new Insets(20)
        );

        box.setStyle(
                "-fx-background-color:" +
                        panel() +
                        ";" +
                        "-fx-background-radius:14;" +
                        "-fx-border-color:" +
                        border() +
                        ";" +
                        "-fx-border-radius:14;"
        );

        return box;
    }

    private static VBox whitePanelWithMessage(
            String message
    ) {

        VBox box =
                whitePanel();

        box.getChildren().add(
                infoLabel(message)
        );

        return box;
    }

    private static Label sectionTitle(
            String value
    ) {

        return pageText(
                value,
                18,
                true,
                text()
        );
    }

    private static VBox pageHeading(
            String title,
            String subtitle
    ) {

        VBox box =
                new VBox(5);

        box.getChildren().addAll(

                pageText(
                        title,
                        26,
                        true,
                        text()
                ),

                pageText(
                        subtitle,
                        13,
                        false,
                        muted()
                )
        );

        return box;
    }

    private static VBox infoPanel(
            String title,
            String... values
    ) {

        VBox box =
                whitePanel();

        box.getChildren().add(
                sectionTitle(title)
        );

        for (
                String value :
                values
        ) {

            box.getChildren().add(
                    infoLabel(value)
            );
        }

        return box;
    }

    private static Label settingDescription(
            String value
    ) {

        Label label =
                pageText(
                        value,
                        13,
                        false,
                        muted()
                );

        label.setWrapText(true);

        return label;
    }

    private static Label settingLabel(
            String value
    ) {

        return pageText(
                value,
                13,
                false,
                secondary()
        );
    }

    private static Label announcement(
            String title,
            String description
    ) {

        Label label =
                pageText(
                        "📌  " +
                                title +
                                "\n      " +
                                description,
                        13,
                        false,
                        secondary()
                );

        label.setWrapText(true);

        return label;
    }

    private static Label menuRow(
            String icon,
            String title,
            String food
    ) {

        Label label =
                pageText(
                        icon +
                                "  " +
                                title +
                                "  →  " +
                                food,
                        12,
                        false,
                        secondary()
                );

        label.setWrapText(true);

        return label;
    }

    private static Label activityRow(
            String icon,
            String title,
            String status
    ) {

        Label label =
                pageText(
                        icon +
                                "   " +
                                title +
                                "\n       Status : " +
                                status,
                        13,
                        false,
                        secondary()
                );

        label.setWrapText(true);

        return label;
    }

    private static Label fieldLabel(
            String value
    ) {

        return pageText(
                value,
                13,
                true,
                secondary()
        );
    }

    private static Label infoLabel(
            String value
    ) {

        Label label =
                pageText(
                        value,
                        14,
                        false,
                        secondary()
                );

        label.setWrapText(true);

        return label;
    }

    private static Label pageText(
            String value,
            double size,
            boolean bold,
            String color
    ) {

        Label label =
                new Label(value);

        label.setFont(
                Font.font(
                        "Segoe UI",
                        bold
                                ? FontWeight.BOLD
                                : FontWeight.NORMAL,
                        size
                )
        );

        label.setTextFill(
                Color.web(color)
        );

        return label;
    }

    private static Button menuButton(
            String icon,
            String textValue
    ) {

        Button button =
                new Button(
                        icon +
                                "   " +
                                textValue
                );

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setPrefHeight(43);

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );

        button.setStyle(
                "-fx-background-color:transparent;" +
                        "-fx-text-fill:#D1D5DB;" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-background-radius:9;" +
                        "-fx-cursor:hand;"
        );

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:#1F2937;" +
                                "-fx-text-fill:#FFFFFF;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-background-radius:9;" +
                                "-fx-cursor:hand;"
                )
        );

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:transparent;" +
                                "-fx-text-fill:#D1D5DB;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-background-radius:9;" +
                                "-fx-cursor:hand;"
                )
        );

        return button;
    }

    private static Button quickButton(
            String icon,
            String textValue,
            String accent
    ) {

        Button button =
                new Button(
                        icon +
                                "\n" +
                                textValue
                );

        button.setPrefSize(
                170,
                75
        );

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
                        "-fx-cursor:hand;"
        );

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                accent +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:14;" +
                                "-fx-border-color:" +
                                accent +
                                ";" +
                                "-fx-border-radius:14;" +
                                "-fx-cursor:hand;"
                )
        );

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
                                "-fx-cursor:hand;"
                )
        );

        return button;
    }

    private static Button primaryButton(
            String value
    ) {

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
                        "-fx-cursor:hand;"
        );

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
                                "-fx-cursor:hand;"
                )
        );

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
                                "-fx-cursor:hand;"
                )
        );

        return button;
    }

    private static Button secondaryButton(
            String value
    ) {

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
                        "-fx-cursor:hand;"
        );

        return button;
    }

    private static Button dangerButton(
            String value
    ) {

        Button button =
                new Button(value);

        button.setPrefWidth(230);

        button.setPrefHeight(45);

        button.setStyle(
                "-fx-background-color:" +
                        RED +
                        ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-family:'Segoe UI';" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:10;" +
                        "-fx-cursor:hand;"
        );

        button.setOnMouseEntered(
                e -> button.setStyle(
                        "-fx-background-color:#B91C1C;" +
                                "-fx-text-fill:white;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-cursor:hand;"
                )
        );

        button.setOnMouseExited(
                e -> button.setStyle(
                        "-fx-background-color:" +
                                RED +
                                ";" +
                                "-fx-text-fill:white;" +
                                "-fx-font-family:'Segoe UI';" +
                                "-fx-font-size:13px;" +
                                "-fx-font-weight:bold;" +
                                "-fx-background-radius:10;" +
                                "-fx-cursor:hand;"
                )
        );

        return button;
    }

    private static String inputStyle() {

        return
                "-fx-background-color:" +
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
            VBox content
    ) {

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setStyle(
                "-fx-background-color:" +
                        bg() +
                        ";" +
                        "-fx-background:" +
                        bg() +
                        ";"
        );

        return scroll;
    }

    private static String safe(
            String value
    ) {

        return value == null ||
                value.trim().isEmpty()
                ? "Not Available"
                : value;
    }

    private static String safeValue(
            String value,
            String fallback
    ) {

        return value == null ||
                value.trim().isEmpty()
                ? fallback
                : value;
    }

    private static String safeObject(
            Object value,
            String fallback
    ) {

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
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Hostel Hub"
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}

