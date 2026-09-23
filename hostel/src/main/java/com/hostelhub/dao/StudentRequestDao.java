package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRequestDao {

        private final Firestore db;

        public StudentRequestDao() {
                db = FirebaseConfig.getFireStore();
        }

        public boolean saveComplaint(
                        String uid,
                        String category,
                        String description) {
                try {
                        Map<String, Object> data = new HashMap<>();

                        data.put("studentId", uid);
                        data.put("category", category);
                        data.put("description", description);
                        data.put("status", "Pending");
                        data.put(
                                        "createdAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .collection("complaints")
                                        .add(data)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean saveLeaveRequest(
                        String uid,
                        String leaveType,
                        String fromDate,
                        String toDate,
                        String reason) {
                try {
                        Map<String, Object> data = new HashMap<>();

                        data.put("studentId", uid);
                        data.put("leaveType", leaveType);
                        data.put("fromDate", fromDate);
                        data.put("toDate", toDate);
                        data.put("reason", reason);
                        data.put("status", "Pending");
                        data.put(
                                        "createdAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .collection("leaveRequests")
                                        .add(data)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean saveEmergency(
                        String uid,
                        String emergencyType,
                        String description) {
                try {
                        Map<String, Object> data = new HashMap<>();

                        data.put("studentId", uid);
                        data.put("emergencyType", emergencyType);
                        data.put("description", description);
                        data.put("status", "Urgent");
                        data.put(
                                        "createdAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .collection("emergencies")
                                        .add(data)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean saveAttendance(
                        String uid,
                        String date,
                        String checkInTime,
                        double latitude,
                        double longitude,
                        double distance) {
                try {
                        Map<String, Object> data = new HashMap<>();

                        data.put("studentId", uid);
                        data.put("date", date);
                        data.put("checkInTime", checkInTime);
                        data.put("checkOutTime", "");
                        data.put("checkInLatitude", latitude);
                        data.put("checkInLongitude", longitude);
                        data.put("checkInDistance", distance);
                        data.put("checkOutLatitude", 0.0);
                        data.put("checkOutLongitude", 0.0);
                        data.put("checkOutDistance", 0.0);
                        data.put("locationVerified", true);
                        data.put("status", "Present");
                        data.put(
                                        "createdAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .collection("attendance")
                                        .document(date)
                                        .set(data)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateCheckOut(
                        String uid,
                        String date,
                        String checkOutTime,
                        double latitude,
                        double longitude,
                        double distance) {
                try {
                        Map<String, Object> update = new HashMap<>();

                        update.put(
                                        "checkOutTime",
                                        checkOutTime);
                        update.put(
                                        "checkOutLatitude",
                                        latitude);
                        update.put(
                                        "checkOutLongitude",
                                        longitude);
                        update.put(
                                        "checkOutDistance",
                                        distance);
                        update.put(
                                        "locationVerified",
                                        true);
                        update.put(
                                        "status",
                                        "Completed");
                        update.put(
                                        "updatedAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .collection("attendance")
                                        .document(date)
                                        .update(update)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public Map<String, Object> getTodayAttendance(
                        String uid,
                        String date) {
                try {
                        DocumentSnapshot doc = db.collection("students")
                                        .document(uid)
                                        .collection("attendance")
                                        .document(date)
                                        .get()
                                        .get();

                        if (doc.exists()) {
                                return doc.getData();
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return null;
        }

        public List<Map<String, Object>> getStudentComplaints(
                        String uid) {
                return getNestedRequests(
                                uid,
                                "complaints",
                                "complaintId");
        }

        public List<Map<String, Object>> getStudentLeaveRequests(
                        String uid) {
                return getNestedRequests(
                                uid,
                                "leaveRequests",
                                "leaveId");
        }

        public List<Map<String, Object>> getStudentEmergencies(
                        String uid) {
                return getNestedRequests(
                                uid,
                                "emergencies",
                                "emergencyId");
        }

        private List<Map<String, Object>> getNestedRequests(
                        String uid,
                        String collection,
                        String idKey) {
                List<Map<String, Object>> result = new ArrayList<>();

                try {
                        QuerySnapshot snapshot = db.collection("students")
                                        .document(uid)
                                        .collection(collection)
                                        .get()
                                        .get();

                        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                                Map<String, Object> data = new HashMap<>(
                                                doc.getData());

                                data.put(idKey, doc.getId());
                                data.put("studentId", uid);

                                result.add(data);
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return result;
        }

        public List<Map<String, Object>> getAllComplaints() {
                return getAllNested(
                                "complaints",
                                "complaintId");
        }

        public List<Map<String, Object>> getAllLeaveRequests() {
                return getAllNested(
                                "leaveRequests",
                                "leaveId");
        }

        public List<Map<String, Object>> getAllEmergencies() {
                return getAllNested(
                                "emergencies",
                                "emergencyId");
        }

        private List<Map<String, Object>> getAllNested(
                        String collection,
                        String idKey) {
                List<Map<String, Object>> result = new ArrayList<>();

                try {
                        QuerySnapshot students = db.collection("students")
                                        .get()
                                        .get();

                        for (QueryDocumentSnapshot student : students.getDocuments()) {

                                String uid = student.getId();

                                String studentName = student.getString("fullName");

                                QuerySnapshot requests = student.getReference()
                                                .collection(collection)
                                                .get()
                                                .get();

                                for (QueryDocumentSnapshot request : requests.getDocuments()) {

                                        Map<String, Object> data = new HashMap<>(
                                                        request.getData());

                                        data.put(
                                                        idKey,
                                                        request.getId());

                                        data.put(
                                                        "studentId",
                                                        uid);

                                        data.put(
                                                        "studentName",
                                                        studentName == null
                                                                        ? "Unknown Student"
                                                                        : studentName);

                                        result.add(data);
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return result;
        }

        public List<Map<String, Object>> getAllAttendance() {
                List<Map<String, Object>> result = new ArrayList<>();

                try {
                        QuerySnapshot students = db.collection("students")
                                        .get()
                                        .get();

                        for (QueryDocumentSnapshot student : students.getDocuments()) {

                                String uid = student.getId();

                                String studentName = student.getString("fullName");

                                QuerySnapshot attendance = student.getReference()
                                                .collection("attendance")
                                                .get()
                                                .get();

                                for (QueryDocumentSnapshot record : attendance.getDocuments()) {

                                        Map<String, Object> data = new HashMap<>(
                                                        record.getData());

                                        data.put(
                                                        "attendanceId",
                                                        record.getId());

                                        data.put(
                                                        "studentId",
                                                        uid);

                                        data.put(
                                                        "studentName",
                                                        studentName == null
                                                                        ? "Unknown Student"
                                                                        : studentName);

                                        result.add(data);
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return result;
        }

        public boolean updateComplaintStatus(
                        String studentId,
                        String complaintId,
                        String status) {
                try {
                        db.collection("students")
                                        .document(studentId)
                                        .collection("complaints")
                                        .document(complaintId)
                                        .update(
                                                        "status",
                                                        status,
                                                        "updatedAt",
                                                        FieldValue.serverTimestamp())
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateLeaveStatus(
                        String studentId,
                        String leaveId,
                        String status) {
                try {
                        db.collection("students")
                                        .document(studentId)
                                        .collection("leaveRequests")
                                        .document(leaveId)
                                        .update(
                                                        "status",
                                                        status,
                                                        "updatedAt",
                                                        FieldValue.serverTimestamp())
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateEmergencyStatus(
                        String studentId,
                        String emergencyId,
                        String status) {
                try {
                        db.collection("students")
                                        .document(studentId)
                                        .collection("emergencies")
                                        .document(emergencyId)
                                        .update(
                                                        "status",
                                                        status,
                                                        "updatedAt",
                                                        FieldValue.serverTimestamp())
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public boolean updateProfile(
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
                try {
                        Map<String, Object> data = new HashMap<>();

                        data.put("fullName", fullName);
                        data.put("parentName", parentName);
                        data.put("dateOfBirth", dateOfBirth);
                        data.put("gender", gender);
                        data.put("course", course);
                        data.put("year", year);
                        data.put("phone", phone);
                        data.put("email", email);
                        data.put("college", college);
                        data.put("city", city);
                        data.put(
                                        "roomPreference",
                                        roomPreference);
                        data.put("address", address);
                        data.put("roomNumber", roomNumber);
                        data.put(
                                        "updatedAt",
                                        FieldValue.serverTimestamp());

                        db.collection("students")
                                        .document(uid)
                                        .update(data)
                                        .get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }
}