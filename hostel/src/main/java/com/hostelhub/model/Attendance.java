package com.hostelhub.model;

public class Attendance {

    private String attendanceId;
    private String studentId;
    private String studentName;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private double checkInLatitude;
    private double checkInLongitude;
    private double checkOutLatitude;
    private double checkOutLongitude;
    private double checkInDistance;
    private double checkOutDistance;
    private boolean locationVerified;
    private String status;

    public Attendance() {
    }

    public Attendance(
            String attendanceId,
            String studentId,
            String studentName,
            String date,
            String checkInTime,
            String checkOutTime,
            double checkInLatitude,
            double checkInLongitude,
            double checkOutLatitude,
            double checkOutLongitude,
            double checkInDistance,
            double checkOutDistance,
            boolean locationVerified,
            String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.checkInLatitude = checkInLatitude;
        this.checkInLongitude = checkInLongitude;
        this.checkOutLatitude = checkOutLatitude;
        this.checkOutLongitude = checkOutLongitude;
        this.checkInDistance = checkInDistance;
        this.checkOutDistance = checkOutDistance;
        this.locationVerified = locationVerified;
        this.status = status;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public double getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(double checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }

    public double getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(double checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public double getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(double checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }

    public double getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(double checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public double getCheckInDistance() {
        return checkInDistance;
    }

    public void setCheckInDistance(double checkInDistance) {
        this.checkInDistance = checkInDistance;
    }

    public double getCheckOutDistance() {
        return checkOutDistance;
    }

    public void setCheckOutDistance(double checkOutDistance) {
        this.checkOutDistance = checkOutDistance;
    }

    public boolean isLocationVerified() {
        return locationVerified;
    }

    public void setLocationVerified(boolean locationVerified) {
        this.locationVerified = locationVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
