
package com.hostelhub.dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.hostelhub.config.FirebaseConfig;
import com.hostelhub.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

        private final Firestore db;

        public PaymentDao() {
                db = FirebaseConfig.getFireStore();
        }

        public boolean savePayment(Payment payment) {
                try {
                        DocumentReference ref = db.collection("payments")
                                        .document(payment.getPaymentId());

                        ApiFuture<?> future = ref.set(payment);

                        future.get();

                        return true;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        public List<Payment> getPaymentsByStudent(
                        String studentId) {

                List<Payment> payments = new ArrayList<>();

                try {

                        QuerySnapshot snapshot = db.collection("payments")
                                        .whereEqualTo(
                                                        "studentId",
                                                        studentId)
                                        .get()
                                        .get();

                        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                                payments.add(
                                                doc.toObject(
                                                                Payment.class));
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return payments;
        }

        public List<Payment> getAllPayments() {

                List<Payment> payments = new ArrayList<>();

                try {

                        QuerySnapshot snapshot = db.collection("payments")
                                        .get()
                                        .get();

                        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {

                                payments.add(
                                                doc.toObject(
                                                                Payment.class));
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return payments;
        }
}
