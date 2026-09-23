package com.hostelhub.view;

import com.google.cloud.firestore.DocumentSnapshot;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.dao.FirestoreStudentDao;
import com.hostelhub.dao.PaymentDao;
import com.hostelhub.model.Payment;
import com.hostelhub.model.Student;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class PaymentPage {

        private Student student;

        private double totalFees = 0.0;
        private double paidFees = 0.0;
        private double pendingFees = 0.0;

        private TextField amountField;
        private TextField upiField;
        private TextField cardNumberField;
        private TextField cvvField;
        private ComboBox<String> bankBox;

        private RadioButton upiRadio;
        private RadioButton cardRadio;
        private RadioButton netBankingRadio;

        public PaymentPage(Student student) {
                this.student = student;
        }

        public void show(Student student2) {

                if (student2 != null) {
                        this.student = student2;
                }

                loadFeeData();

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color:#F5F7FB;");

                root.setTop(
                                createHeader());

                root.setCenter(
                                createPaymentContent());

                Scene scene = new Scene(
                                root,
                                1550,
                                800);

                Stage stage = Welcome.stage;

                stage.setTitle(
                                "Hostel Hub - Fee Payment");

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
        }

        private void loadFeeData() {

                totalFees = 0.0;
                paidFees = 0.0;
                pendingFees = 0.0;

                try {

                        if (student == null) {
                                return;
                        }

                        String uid = student.getUid();

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return;
                        }

                        FirestoreStudentDao dao = new FirestoreStudentDao();

                        dao.ensureFeeData(uid);

                        DocumentSnapshot doc = FirebaseConfig
                                        .getFireStore()
                                        .collection("students")
                                        .document(uid)
                                        .get()
                                        .get();

                        if (!doc.exists()) {
                                return;
                        }

                        Map<String, Object> data = doc.getData();

                        if (data == null) {
                                return;
                        }

                        totalFees = getNumber(
                                        data.get("totalFee"));

                        paidFees = getNumber(
                                        data.get("paidFee"));

                        pendingFees = getNumber(
                                        data.get("remainingFee"));

                        if (totalFees <= 0) {

                                String messPlan = getString(
                                                data.get(
                                                                "messPlan"));

                                if (messPlan.isEmpty()) {
                                        messPlan = student.getMessPlan();
                                }

                                totalFees = "With Mess".equalsIgnoreCase(
                                                messPlan)
                                                                ? 75000.0
                                                                : 41000.0;
                        }

                        paidFees = Math.max(
                                        0.0,
                                        Math.min(
                                                        paidFees,
                                                        totalFees));

                        pendingFees = Math.max(
                                        0.0,
                                        totalFees - paidFees);

                } catch (Exception e) {

                        e.printStackTrace();
                }
        }

        private double getNumber(
                        Object value) {

                if (value instanceof Number) {

                        return ((Number) value)
                                        .doubleValue();
                }

                if (value != null) {

                        try {

                                return Double.parseDouble(
                                                value.toString());

                        } catch (Exception ignored) {
                        }
                }

                return 0.0;
        }

        private String getString(
                        Object value) {

                return value == null
                                ? ""
                                : value.toString();
        }

        private HBox createHeader() {

                HBox header = new HBox();

                header.setPadding(
                                new Insets(
                                                18,
                                                30,
                                                18,
                                                30));

                header.setAlignment(
                                Pos.CENTER_LEFT);

                header.setSpacing(20);

                header.setStyle(
                                "-fx-background-color:#111827;");

                Label logo = new Label(
                                "HOSTEL HUB");

                logo.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                24));

                logo.setTextFill(
                                Color.WHITE);

                Label title = new Label(
                                "Fee Payment");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                20));

                title.setTextFill(
                                Color.web("#D1D5DB"));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Label studentName = new Label(
                                safe(
                                                student.getFullName()));

                studentName.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                15));

                studentName.setTextFill(
                                Color.WHITE);

                Button back = new Button(
                                "← Dashboard");

                back.setStyle(
                                "-fx-background-color:#374151;" +
                                                "-fx-text-fill:white;" +
                                                "-fx-font-size:14px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-padding:9 16;" +
                                                "-fx-background-radius:8;" +
                                                "-fx-cursor:hand;");

                back.setOnAction(
                                e -> StudentDashboard.show(
                                                student));

                header.getChildren().addAll(
                                logo,
                                title,
                                spacer,
                                studentName,
                                back);

                return header;
        }

        private VBox createPaymentContent() {

                VBox main = new VBox(25);

                main.setPadding(
                                new Insets(
                                                35,
                                                60,
                                                35,
                                                60));

                main.setAlignment(
                                Pos.TOP_CENTER);

                Label heading = new Label(
                                "Pay Hostel Fees");

                heading.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                30));

                heading.setTextFill(
                                Color.web("#111827"));

                Label subtitle = new Label(
                                "Enter the amount you want to pay");

                subtitle.setFont(
                                Font.font(
                                                "Segoe UI",
                                                14));

                subtitle.setTextFill(
                                Color.web("#6B7280"));

                HBox cards = new HBox(25);

                cards.setAlignment(
                                Pos.TOP_CENTER);

                VBox feeCard = createFeeSummaryCard();

                VBox paymentCard = createPaymentCard();

                HBox.setHgrow(
                                feeCard,
                                Priority.ALWAYS);

                HBox.setHgrow(
                                paymentCard,
                                Priority.ALWAYS);

                cards.getChildren().addAll(
                                feeCard,
                                paymentCard);

                main.getChildren().addAll(
                                heading,
                                subtitle,
                                cards);

                return main;
        }

        private VBox createFeeSummaryCard() {

                VBox card = new VBox(20);

                card.setPadding(
                                new Insets(28));

                card.setPrefWidth(450);

                card.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:18;" +
                                                "-fx-border-color:#E5E7EB;" +
                                                "-fx-border-radius:18;");

                Label title = new Label(
                                "Fee Summary");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                21));

                title.setTextFill(
                                Color.web("#111827"));

                VBox totalBox = feeRow(
                                "Total Hostel Fees",
                                formatCurrency(
                                                totalFees));

                VBox paidBox = feeRow(
                                "Already Paid",
                                formatCurrency(
                                                paidFees));

                VBox pendingBox = feeRow(
                                "Pending Amount",
                                formatCurrency(
                                                pendingFees));

                Separator separator = new Separator();

                Label pendingTitle = new Label(
                                "Amount Payable");

                pendingTitle.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                17));

                Label pendingAmount = new Label(
                                formatCurrency(
                                                pendingFees));

                pendingAmount.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                28));

                pendingAmount.setTextFill(
                                Color.web("#7C3AED"));

                HBox amountRow = new HBox();

                amountRow.setAlignment(
                                Pos.CENTER_LEFT);

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                amountRow.getChildren().addAll(
                                pendingTitle,
                                spacer,
                                pendingAmount);

                Label info = new Label(
                                "You can pay any amount up to the pending fee.");

                info.setWrapText(true);

                info.setFont(
                                Font.font(
                                                "Segoe UI",
                                                13));

                info.setTextFill(
                                Color.web("#6B7280"));

                card.getChildren().addAll(
                                title,
                                totalBox,
                                paidBox,
                                pendingBox,
                                separator,
                                amountRow,
                                info);

                return card;
        }

        private VBox feeRow(
                        String labelText,
                        String amountText) {

                VBox box = new VBox(5);

                HBox row = new HBox();

                row.setAlignment(
                                Pos.CENTER_LEFT);

                Label label = new Label(
                                labelText);

                label.setFont(
                                Font.font(
                                                "Segoe UI",
                                                14));

                label.setTextFill(
                                Color.web("#4B5563"));

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                Label amount = new Label(
                                amountText);

                amount.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                15));

                amount.setTextFill(
                                Color.web("#111827"));

                row.getChildren().addAll(
                                label,
                                spacer,
                                amount);

                box.getChildren().add(row);

                return box;
        }

        private VBox createPaymentCard() {

                VBox card = new VBox(18);

                card.setPadding(
                                new Insets(28));

                card.setPrefWidth(520);

                card.setStyle(
                                "-fx-background-color:white;" +
                                                "-fx-background-radius:18;" +
                                                "-fx-border-color:#E5E7EB;" +
                                                "-fx-border-radius:18;");

                Label title = new Label(
                                "Payment Details");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                21));

                title.setTextFill(
                                Color.web("#111827"));

                Label amountLabel = new Label(
                                "Payment Amount");

                amountLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                14));

                amountField = new TextField();

                amountField.setPrefHeight(45);

                amountField.setPromptText(
                                "Enter amount");

                amountField.setStyle(
                                inputStyle());

                if (pendingFees > 0) {

                        amountField.setText(
                                        formatInputAmount(
                                                        pendingFees));
                }

                Label methodLabel = new Label(
                                "Select Payment Method");

                methodLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                14));

                ToggleGroup group = new ToggleGroup();

                upiRadio = new RadioButton(
                                "UPI");

                cardRadio = new RadioButton(
                                "Card");

                netBankingRadio = new RadioButton(
                                "Net Banking");

                upiRadio.setToggleGroup(group);
                cardRadio.setToggleGroup(group);
                netBankingRadio.setToggleGroup(group);

                upiRadio.setSelected(true);

                HBox methods = new HBox(
                                25,
                                upiRadio,
                                cardRadio,
                                netBankingRadio);

                VBox detailsBox = new VBox(12);

                upiField = new TextField();

                upiField.setPromptText(
                                "Enter demo UPI ID");

                upiField.setPrefHeight(42);
                upiField.setStyle(
                                inputStyle());

                cardNumberField = new TextField();

                cardNumberField.setPromptText(
                                "Enter demo card number");

                cardNumberField.setPrefHeight(42);
                cardNumberField.setStyle(
                                inputStyle());

                cvvField = new TextField();

                cvvField.setPromptText(
                                "Enter CVV");

                cvvField.setPrefHeight(42);
                cvvField.setStyle(
                                inputStyle());

                bankBox = new ComboBox<>();

                bankBox.getItems().addAll(
                                "State Bank of India",
                                "HDFC Bank",
                                "ICICI Bank",
                                "Axis Bank",
                                "Bank of Maharashtra");

                bankBox.setPromptText(
                                "Select demo bank");

                bankBox.setPrefHeight(42);

                bankBox.setMaxWidth(
                                Double.MAX_VALUE);

                bankBox.setStyle(
                                inputStyle());

                detailsBox.getChildren().add(
                                upiField);

                upiRadio.setOnAction(
                                e -> {

                                        detailsBox.getChildren()
                                                        .clear();

                                        detailsBox.getChildren()
                                                        .add(
                                                                        upiField);
                                });

                cardRadio.setOnAction(
                                e -> {

                                        detailsBox.getChildren()
                                                        .clear();

                                        HBox cardRow = new HBox(10);

                                        HBox.setHgrow(
                                                        cardNumberField,
                                                        Priority.ALWAYS);

                                        HBox.setHgrow(
                                                        cvvField,
                                                        Priority.ALWAYS);

                                        cardRow.getChildren()
                                                        .addAll(
                                                                        cardNumberField,
                                                                        cvvField);

                                        detailsBox.getChildren()
                                                        .add(cardRow);
                                });

                netBankingRadio.setOnAction(
                                e -> {

                                        detailsBox.getChildren()
                                                        .clear();

                                        detailsBox.getChildren()
                                                        .add(
                                                                        bankBox);
                                });

                Label demoLabel = new Label(
                                "DEMO PAYMENT • No real money will be deducted");

                demoLabel.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                12));

                demoLabel.setTextFill(
                                Color.web("#B45309"));

                Button payNow = primaryButton(
                                pendingFees > 0
                                                ? "PAY " +
                                                                formatCurrency(
                                                                                pendingFees)
                                                : "FEES PAID");

                payNow.setMaxWidth(
                                Double.MAX_VALUE);

                payNow.setPrefHeight(48);

                payNow.setDisable(
                                pendingFees <= 0);

                amountField.textProperty().addListener(
                                (observable, oldValue, newValue) -> {

                                        updatePayButton(
                                                        payNow);
                                });

                payNow.setOnAction(
                                e -> processPayment(
                                                payNow));

                card.getChildren().addAll(
                                title,
                                amountLabel,
                                amountField,
                                methodLabel,
                                methods,
                                detailsBox,
                                demoLabel,
                                payNow);

                return card;
        }

        private void updatePayButton(
                        Button payNow) {

                try {

                        String value = amountField
                                        .getText()
                                        .trim();

                        if (value.isEmpty()) {

                                payNow.setText(
                                                "PAY " +
                                                                formatCurrency(
                                                                                pendingFees));

                                payNow.setDisable(
                                                pendingFees <= 0);

                                return;
                        }

                        double enteredAmount = Double.parseDouble(value);

                        if (enteredAmount <= 0) {

                                payNow.setText(
                                                "ENTER AMOUNT");

                                payNow.setDisable(true);

                                return;
                        }

                        if (enteredAmount > pendingFees) {

                                payNow.setText(
                                                "AMOUNT EXCEEDS PENDING FEE");

                                payNow.setDisable(true);

                                return;
                        }

                        payNow.setText(
                                        "PAY " +
                                                        formatCurrency(
                                                                        enteredAmount));

                        payNow.setDisable(false);

                } catch (Exception e) {

                        payNow.setText(
                                        "ENTER VALID AMOUNT");

                        payNow.setDisable(true);
                }
        }

        private void processPayment(
                        Button payNow) {

                loadFeeData();

                double amount;

                try {

                        amount = Double.parseDouble(
                                        amountField
                                                        .getText()
                                                        .trim());

                } catch (Exception e) {

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Invalid Amount",
                                        "Please enter a valid payment amount.");

                        return;
                }

                if (amount <= 0) {

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Invalid Amount",
                                        "Payment amount must be greater than zero.");

                        return;
                }

                if (pendingFees <= 0) {

                        showAlert(
                                        Alert.AlertType.INFORMATION,
                                        "Fees Already Paid",
                                        "There is no pending fee amount.");

                        return;
                }

                if (amount > pendingFees) {

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Invalid Amount",
                                        "Amount cannot be greater than pending fees of "
                                                        + formatCurrency(
                                                                        pendingFees));

                        return;
                }

                String method;

                if (upiRadio.isSelected()) {

                        method = "UPI";

                        if (upiField.getText()
                                        .trim()
                                        .isEmpty()) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "UPI Required",
                                                "Please enter a demo UPI ID.");

                                return;
                        }

                } else if (cardRadio.isSelected()) {

                        method = "Card";

                        if (cardNumberField.getText()
                                        .trim()
                                        .isEmpty()
                                        ||
                                        cvvField.getText()
                                                        .trim()
                                                        .isEmpty()) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Card Details Required",
                                                "Please enter demo card number and CVV.");

                                return;
                        }

                } else {

                        method = "Net Banking";

                        if (bankBox.getValue() == null) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Bank Required",
                                                "Please select a demo bank.");

                                return;
                        }
                }

                Alert confirm = new Alert(
                                Alert.AlertType.CONFIRMATION);

                confirm.setTitle(
                                "Confirm Demo Payment");

                confirm.setHeaderText(
                                "Confirm Fee Payment");

                confirm.setContentText(
                                "Amount: "
                                                + formatCurrency(amount)
                                                + "\nPayment Method: "
                                                + method
                                                + "\n\nThis is a demo transaction.");

                ButtonType result = confirm.showAndWait()
                                .orElse(
                                                ButtonType.CANCEL);

                if (result != ButtonType.OK) {
                        return;
                }

                showProcessing(
                                amount,
                                method);
        }

        private void showProcessing(
                        double amount,
                        String method) {

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color:#F5F7FB;");

                VBox box = new VBox(20);

                box.setAlignment(
                                Pos.CENTER);

                ProgressIndicator indicator = new ProgressIndicator();

                indicator.setPrefSize(
                                60,
                                60);

                Label title = new Label(
                                "Processing Payment...");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                24));

                Label message = new Label(
                                "Please wait while your demo payment is being processed.");

                message.setFont(
                                Font.font(
                                                "Segoe UI",
                                                14));

                message.setTextFill(
                                Color.web("#6B7280"));

                box.getChildren().addAll(
                                indicator,
                                title,
                                message);

                root.setCenter(box);

                Scene scene = new Scene(
                                root,
                                1100,
                                750);

                Stage stage = Welcome.stage;

                stage.setTitle(
                                "Hostel Hub - Processing Payment");

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();

                PauseTransition pause = new PauseTransition(
                                Duration.seconds(2));

                pause.setOnFinished(
                                e -> saveSuccessfulPayment(
                                                amount,
                                                method));

                pause.play();
        }

        private void saveSuccessfulPayment(
                        double amount,
                        String method) {

                try {

                        String paymentId = "PAY-" +
                                        UUID.randomUUID()
                                                        .toString()
                                                        .substring(
                                                                        0,
                                                                        8)
                                                        .toUpperCase();

                        String orderId = "ORD-" +
                                        UUID.randomUUID()
                                                        .toString()
                                                        .substring(
                                                                        0,
                                                                        8)
                                                        .toUpperCase();

                        String receiptNumber = "HH-" +
                                        System.currentTimeMillis();

                        String paymentDate = LocalDateTime.now()
                                        .format(
                                                        DateTimeFormatter.ofPattern(
                                                                        "dd-MM-yyyy HH:mm:ss"));

                        Payment payment = new Payment(
                                        paymentId,
                                        orderId,
                                        student.getUid(),
                                        student.getFullName(),
                                        amount,
                                        paymentDate,
                                        receiptNumber,
                                        "SUCCESS");

                        PaymentDao paymentDao = new PaymentDao();

                        boolean saved = paymentDao.savePayment(
                                        payment);

                        if (!saved) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Payment Error",
                                                "Payment receipt could not be saved.");

                                StudentDashboard.show(
                                                student);

                                return;
                        }

                        FirestoreStudentDao studentDao = new FirestoreStudentDao();

                        boolean feeUpdated = studentDao.updateFeePayment(
                                        student.getUid(),
                                        amount);

                        if (!feeUpdated) {

                                showAlert(
                                                Alert.AlertType.ERROR,
                                                "Fee Update Error",
                                                "Payment was saved but fee could not be updated.");

                                StudentDashboard.show(
                                                student);

                                return;
                        }

                        showSuccess(
                                        payment,
                                        method);

                } catch (Exception e) {

                        e.printStackTrace();

                        showAlert(
                                        Alert.AlertType.ERROR,
                                        "Payment Error",
                                        "Unable to complete payment.");

                        StudentDashboard.show(
                                        student);
                }
        }

        private void showSuccess(
                        Payment payment,
                        String method) {

                BorderPane root = new BorderPane();

                root.setStyle(
                                "-fx-background-color:#F5F7FB;");

                VBox box = new VBox(20);

                box.setAlignment(
                                Pos.CENTER);

                box.setPadding(
                                new Insets(40));

                Label icon = new Label(
                                "✓");

                icon.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                60));

                icon.setTextFill(
                                Color.web("#16A34A"));

                Label title = new Label(
                                "Payment Successful");

                title.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                28));

                Label amount = new Label(
                                formatCurrency(
                                                payment.getAmount()));

                amount.setFont(
                                Font.font(
                                                "Segoe UI",
                                                FontWeight.BOLD,
                                                32));

                amount.setTextFill(
                                Color.web("#7C3AED"));

                Label details = new Label(
                                "Payment Method : "
                                                + method
                                                + "\n\nReceipt No : "
                                                + payment.getReceiptNumber()
                                                + "\n\nPayment ID : "
                                                + payment.getPaymentId()
                                                + "\n\nOrder ID : "
                                                + payment.getOrderId()
                                                + "\n\nDate : "
                                                + payment.getPaymentDate());

                details.setFont(
                                Font.font(
                                                "Segoe UI",
                                                14));

                details.setTextFill(
                                Color.web("#374151"));

                details.setAlignment(
                                Pos.CENTER);

                Button continueButton = primaryButton(
                                "VIEW RECEIPT");

                continueButton.setOnAction(
                                e -> showReceipt(
                                                payment,
                                                method));

                box.getChildren().addAll(
                                icon,
                                title,
                                amount,
                                details,
                                continueButton);

                root.setCenter(box);

                Scene scene = new Scene(
                                root,
                                1100,
                                750);

                Stage stage = Welcome.stage;

                stage.setTitle(
                                "Hostel Hub - Payment Successful");

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
        }

        private void showReceipt(
                        Payment payment,
                        String method) {

                Alert receipt = new Alert(
                                Alert.AlertType.INFORMATION);

                receipt.setTitle(
                                "Payment Receipt");

                receipt.setHeaderText(
                                "HOSTEL HUB - PAYMENT RECEIPT");

                receipt.setContentText(
                                "Student Name : "
                                                + payment.getStudentName()

                                                + "\nStudent ID : "
                                                + payment.getStudentId()

                                                + "\n\nAmount Paid : "
                                                + formatCurrency(
                                                                payment.getAmount())

                                                + "\nPayment Method : "
                                                + method

                                                + "\nPayment ID : "
                                                + payment.getPaymentId()

                                                + "\nOrder ID : "
                                                + payment.getOrderId()

                                                + "\nReceipt No : "
                                                + payment.getReceiptNumber()

                                                + "\nDate : "
                                                + payment.getPaymentDate()

                                                + "\nStatus : SUCCESS"

                                                + "\n\nThank you for your payment.");

                receipt.showAndWait();

                StudentDashboard.show(
                                student);
        }

        private Button primaryButton(
                        String text) {

                Button button = new Button(text);

                button.setStyle(
                                "-fx-background-color:#7C3AED;" +
                                                "-fx-text-fill:white;" +
                                                "-fx-font-size:14px;" +
                                                "-fx-font-weight:bold;" +
                                                "-fx-background-radius:9;" +
                                                "-fx-padding:11 20;" +
                                                "-fx-cursor:hand;");

                return button;
        }

        private String inputStyle() {

                return "-fx-font-size:14px;" +
                                "-fx-background-radius:8;" +
                                "-fx-border-radius:8;" +
                                "-fx-border-color:#D1D5DB;";
        }

        private String formatCurrency(
                        double amount) {

                return String.format(
                                "₹%,.2f",
                                amount);
        }

        private String formatInputAmount(
                        double amount) {

                if (amount == Math.floor(amount)) {

                        return String.valueOf(
                                        (long) amount);
                }

                return String.valueOf(amount);
        }

        private String safe(
                        String value) {

                return value == null ||
                                value.trim().isEmpty()
                                                ? "Student"
                                                : value;
        }

        private void showAlert(
                        Alert.AlertType type,
                        String title,
                        String message) {

                Alert alert = new Alert(type);

                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
}