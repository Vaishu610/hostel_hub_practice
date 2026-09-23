package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreStudentDao {

        private final Firestore db;

        public FirestoreStudentDao() {
                db = FirebaseConfig.getFireStore();
        }

        public boolean saveStudent(
                        String uid,
                        String name,
                        String parentName,
                        String dob,
                        String gender,
                        String course,
                        String year,
                        String phone,
                        String email,
                        String college,
                        String city,
                        String roomPreference,
                        String address,
                        String roomNumber,
                        String messPlan) {

                try {

                        if (uid == null || uid.trim().isEmpty()) {
                                return false;
                        }

                        double totalFee = "With Mess".equalsIgnoreCase(messPlan)
                                        ? 75000.0
                                        : 41000.0;

                        Map<String, Object> student = new HashMap<>();

                        student.put("uid", uid);
                        student.put("fullName", name);
                        student.put("parentName", parentName);
                        student.put("dateOfBirth", dob);
                        student.put("gender", gender);
                        student.put("course", course);
                        student.put("year", year);
                        student.put("phone", phone);
                        student.put("email", email);
                        student.put("college", college);
                        student.put("city", city);
                        student.put("roomPreference", roomPreference);
                        student.put("address", address);
                        student.put("roomNumber", roomNumber);
                        student.put("messPlan", messPlan);

                        student.put("totalFee", totalFee);
                        student.put("paidFee", 0.0);
                        student.put("remainingFee", totalFee);
                        student.put("feeStatus", "PENDING");

                        student.put(
                                        "createdAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .set(student)
                                        .get();

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public Student getStudentByUid(String uid) {

                try {

                        if (uid == null || uid.trim().isEmpty()) {
                                return null;
                        }

                        DocumentSnapshot document = db.collection("students")
                                        .document(uid)
                                        .get()
                                        .get();

                        if (!document.exists()) {
                                return null;
                        }

                        return document.toObject(Student.class);

                } catch (Exception e) {

                        e.printStackTrace();
                        return null;
                }
        }

        public List<Student> getAllStudents() {

                List<Student> students = new ArrayList<>();

                try {

                        List<QueryDocumentSnapshot> documents = db.collection("students")
                                        .get()
                                        .get()
                                        .getDocuments();

                        for (DocumentSnapshot document : documents) {

                                if (!document.exists()) {
                                        continue;
                                }

                                Student student = document.toObject(
                                                Student.class);

                                if (student != null) {
                                        students.add(student);
                                }
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return students;
        }

        public int getStudentCount() {
                return getAllStudents().size();
        }

        public boolean ensureFeeData(String uid) {

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return false;
                        }

                        DocumentSnapshot document = db.collection("students")
                                        .document(uid)
                                        .get()
                                        .get();

                        if (!document.exists()) {
                                return false;
                        }

                        Map<String, Object> data = document.getData();

                        if (data == null) {
                                data = new HashMap<>();
                        }

                        Map<String, Object> updates = new HashMap<>();

                        Object totalObject = data.get("totalFee");

                        Object paidObject = data.get("paidFee");

                        Object remainingObject = data.get("remainingFee");

                        Object statusObject = data.get("feeStatus");

                        double totalFee = getNumberValue(totalObject);

                        double paidFee = getNumberValue(paidObject);

                        String messPlan = data.get("messPlan") == null
                                        ? ""
                                        : data.get(
                                                        "messPlan").toString();

                        if (totalFee <= 0) {

                                totalFee = "With Mess".equalsIgnoreCase(
                                                messPlan)
                                                                ? 75000.0
                                                                : 41000.0;

                                updates.put(
                                                "totalFee",
                                                totalFee);
                        }

                        if (paidObject == null) {

                                paidFee = 0.0;

                                updates.put(
                                                "paidFee",
                                                paidFee);
                        }

                        if (paidFee < 0) {
                                paidFee = 0.0;
                        }

                        if (paidFee > totalFee) {
                                paidFee = totalFee;
                        }

                        double calculatedRemaining = Math.max(
                                        0.0,
                                        totalFee - paidFee);

                        double existingRemaining = getNumberValue(
                                        remainingObject);

                        if (remainingObject == null ||
                                        Math.abs(
                                                        existingRemaining -
                                                                        calculatedRemaining) > 0.001) {

                                updates.put(
                                                "remainingFee",
                                                calculatedRemaining);
                        }

                        String feeStatus = statusObject == null
                                        ? ""
                                        : statusObject.toString();

                        if (calculatedRemaining <= 0) {

                                feeStatus = "PAID";

                        } else if (paidFee > 0) {

                                feeStatus = "PARTIALLY_PAID";

                        } else {

                                feeStatus = "PENDING";
                        }

                        if (statusObject == null ||
                                        !feeStatus.equals(
                                                        statusObject.toString())) {

                                updates.put(
                                                "feeStatus",
                                                feeStatus);
                        }

                        if (!updates.isEmpty()) {

                                db.collection("students")
                                                .document(uid)
                                                .update(updates)
                                                .get();
                        }

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateFeePayment(
                        String uid,
                        double paymentAmount) {

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty() ||
                                        paymentAmount <= 0) {

                                return false;
                        }

                        ensureFeeData(uid);

                        DocumentSnapshot document = db.collection("students")
                                        .document(uid)
                                        .get()
                                        .get();

                        if (!document.exists()) {
                                return false;
                        }

                        double totalFee = getNumberValue(
                                        document.get(
                                                        "totalFee"));

                        double paidFee = getNumberValue(
                                        document.get(
                                                        "paidFee"));

                        if (totalFee <= 0) {
                                return false;
                        }

                        double remainingFee = Math.max(
                                        0.0,
                                        totalFee - paidFee);

                        if (paymentAmount > remainingFee) {
                                return false;
                        }

                        double newPaidFee = paidFee + paymentAmount;

                        double newRemainingFee = Math.max(
                                        0.0,
                                        totalFee - newPaidFee);

                        String feeStatus;

                        if (newRemainingFee <= 0) {

                                feeStatus = "PAID";

                        } else if (newPaidFee > 0) {

                                feeStatus = "PARTIALLY_PAID";

                        } else {

                                feeStatus = "PENDING";
                        }

                        Map<String, Object> updates = new HashMap<>();

                        updates.put(
                                        "totalFee",
                                        totalFee);

                        updates.put(
                                        "paidFee",
                                        newPaidFee);

                        updates.put(
                                        "remainingFee",
                                        newRemainingFee);

                        updates.put(
                                        "feeStatus",
                                        feeStatus);

                        updates.put(
                                        "lastPaymentAmount",
                                        paymentAmount);

                        updates.put(
                                        "lastPaymentAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .update(updates)
                                        .get();

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateStudent(
                        String uid,
                        String name,
                        String parentName,
                        String dob,
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

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return false;
                        }

                        Map<String, Object> updates = new HashMap<>();

                        updates.put(
                                        "fullName",
                                        name);

                        updates.put(
                                        "parentName",
                                        parentName);

                        updates.put(
                                        "dateOfBirth",
                                        dob);

                        updates.put(
                                        "gender",
                                        gender);

                        updates.put(
                                        "course",
                                        course);

                        updates.put(
                                        "year",
                                        year);

                        updates.put(
                                        "phone",
                                        phone);

                        updates.put(
                                        "email",
                                        email);

                        updates.put(
                                        "college",
                                        college);

                        updates.put(
                                        "city",
                                        city);

                        updates.put(
                                        "roomPreference",
                                        roomPreference);

                        updates.put(
                                        "address",
                                        address);

                        updates.put(
                                        "roomNumber",
                                        roomNumber);

                        updates.put(
                                        "updatedAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .update(updates)
                                        .get();

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateStudentDetails(
                        String uid,
                        String name,
                        String parentName,
                        String dob,
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

                return updateStudent(
                                uid,
                                name,
                                parentName,
                                dob,
                                gender,
                                course,
                                year,
                                phone,
                                email,
                                college,
                                city,
                                roomPreference,
                                address,
                                roomNumber);
        }

        public boolean updateRoomNumber(
                        String uid,
                        String roomNumber) {

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return false;
                        }

                        Map<String, Object> updates = new HashMap<>();

                        updates.put(
                                        "roomNumber",
                                        roomNumber == null
                                                        ? ""
                                                        : roomNumber.trim());

                        updates.put(
                                        "updatedAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .update(updates)
                                        .get();

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public boolean deleteStudent(
                        String uid) {

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return false;
                        }

                        db.collection("students")
                                        .document(uid)
                                        .delete()
                                        .get();

                        return true;

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public boolean studentExists(
                        String uid) {

                try {

                        if (uid == null ||
                                        uid.trim().isEmpty()) {

                                return false;
                        }

                        return db.collection("students")
                                        .document(uid)
                                        .get()
                                        .get()
                                        .exists();

                } catch (Exception e) {

                        e.printStackTrace();
                        return false;
                }
        }

        public List<Student> getStudentsByRoom(
                        String roomNumber) {

                List<Student> students = new ArrayList<>();

                try {

                        if (roomNumber == null ||
                                        roomNumber.trim().isEmpty()) {

                                return students;
                        }

                        List<QueryDocumentSnapshot> documents = db.collection("students")
                                        .whereEqualTo(
                                                        "roomNumber",
                                                        roomNumber.trim())
                                        .get()
                                        .get()
                                        .getDocuments();

                        for (DocumentSnapshot document : documents) {

                                if (!document.exists()) {
                                        continue;
                                }

                                Student student = document.toObject(
                                                Student.class);

                                if (student != null) {
                                        students.add(student);
                                }
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return students;
        }

        private double getNumberValue(
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
}