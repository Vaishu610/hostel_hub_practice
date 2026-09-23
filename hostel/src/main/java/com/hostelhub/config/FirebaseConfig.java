package com.hostelhub.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;

public class FirebaseConfig {

    static {
        getFirebaseConfig();
    }

    private static void getFirebaseConfig() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                InputStream serviceAccount = FirebaseConfig.class
                        .getClassLoader()
                        .getResourceAsStream("firebase-service-account.json");

                if (serviceAccount == null) {
                    throw new RuntimeException(
                            "firebase-service-account.json not found in resources");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(
                                GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Firestore getFireStore() {
        return FirestoreClient.getFirestore();
    }
}