package com.hostelhub.dao;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.hostelhub.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.hostelhub.model.Student;

import java.util.HashMap;
import java.util.Map;

public class FirestoreStudentDao {

    private final Firestore db;

    public FirestoreStudentDao() {
        System.out.println("Creating Firestore DAO...");
        db = FirebaseConfig.getFireStore();
        System.out.println("Firestore instance created: " + db);
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
            String roomNumber) {

        try {

            System.out.println("Saving student...");
            System.out.println("UID: " + uid);

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
            student.put("createdAt", FieldValue.serverTimestamp());

            db.collection("students")
                    .document(uid)
                    .set(student)
                    .get();

            System.out.println("STUDENT SAVED SUCCESSFULLY!");

            return true;

        } catch (Exception e) {

            System.out.println("FIRESTORE ERROR:");
            e.printStackTrace();

            return false;
        }
    }


    public Student getStudentByUid(String uid) {

    try {

        DocumentSnapshot document =
                db.collection("students")
                        .document(uid)
                        .get()
                        .get();

        if (!document.exists()) {
            System.out.println(
                    "Student not found for UID: " + uid
            );
            return null;
        }

        Student student =
                document.toObject(Student.class);

        return student;

    } catch (Exception e) {

        e.printStackTrace();
        return null;
    }
}
}