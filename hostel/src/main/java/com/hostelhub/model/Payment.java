package com.hostelhub.model;

public class Payment {

    private String paymentId;
    private String orderId;
    private String studentId;
    private String studentName;
    private double amount;
    private String paymentDate;
    private String receiptNumber;
    private String status;

    public Payment() {
    }

    public Payment(
            String paymentId,
            String orderId,
            String studentId,
            String studentName,
            double amount,
            String paymentDate,
            String receiptNumber,
            String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.receiptNumber = receiptNumber;
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}