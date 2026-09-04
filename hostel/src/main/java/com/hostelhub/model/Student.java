package com.hostelhub.model;

public class Student {

    private String uid;
    private String fullName;
    private String parentName;
    private String dateOfBirth;
    private String gender;
    private String course;
    private String year;
    private String phone;
    private String email;
    private String college;
    private String city;
    private String roomPreference;
    private String address;
   private String roomNumber;

    public Student() {
    }

    public Student(
            String uid,
            String fullName,
            String parentName,
            String dateOfBirth,
            String gender,
            String course,
            String year,
            String phone,
            String email,
            String college,
            String city,
            String roomPreference,
            String address,
            String roomNumber) {

        this.uid = uid;
        this.fullName = fullName;
        this.parentName = parentName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.course = course;
        this.year = year;
        this.phone = phone;
        this.email = email;
        this.college = college;
        this.city = city;
        this.roomPreference = roomPreference;
        this.address = address;
        this.roomNumber=roomNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoomPreference() {
        return roomPreference;
    }

    public void setRoomPreference(String roomPreference) {
        this.roomPreference = roomPreference;
    }

    public String getAddress() {
        return address;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}