package com.hostelhub.dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeeDao {

    private final Firestore db;

    public FeeDao() {
        db = FirebaseConfig.getFireStore();
    }

    public boolean createFeeRecord(String studentUid, double totalFees) {
        try {
            Map<String, Object> data = new HashMap<>();

            data.put("studentId", studentUid);
            data.put("totalFees", totalFees);
            data.put("paidFees", 0.0);
            data.put("pendingFees", totalFees);
            data.put("updatedAt",
                    com.google.cloud.firestore.FieldValue.serverTimestamp());

            db.collection("fees")
                    .document(studentUid)
                    .set(data)
                    .get();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Object> getStudentFee(String studentUid) {
        try {
            DocumentSnapshot document = db.collection("fees")
                    .document(studentUid)
                    .get()
                    .get();

            if (document.exists()) {
                return document.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePayment(
            String studentUid,
            double paymentAmount) {
        try {
            Map<String, Object> existing = getStudentFee(studentUid);

            if (existing == null) {
                return false;
            }

            double total = number(existing.get("totalFees"));

            double paid = number(existing.get("paidFees"));

            double pending = number(existing.get("pendingFees"));

            if (paymentAmount <= 0 || paymentAmount > pending) {
                return false;
            }

            double newPaid = paid + paymentAmount;
            double newPending = Math.max(0, total - newPaid);

            Map<String, Object> update = new HashMap<>();

            update.put("paidFees", newPaid);
            update.put("pendingFees", newPending);
            update.put("updatedAt",
                    com.google.cloud.firestore.FieldValue.serverTimestamp());

            db.collection("fees")
                    .document(studentUid)
                    .update(update)
                    .get();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getAllFeeRecords() {
        List<Map<String, Object>> records = new ArrayList<>();

        try {
            QuerySnapshot snapshot = db.collection("fees")
                    .get()
                    .get();

            for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                Map<String, Object> data = doc.getData();

                data.put("studentId", doc.getId());

                records.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    private double number(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        try {
            return Double.parseDouble(
                    String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }
}