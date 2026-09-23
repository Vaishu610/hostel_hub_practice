package com.hostelhub.controller;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseAuthController {

    private static final String API_KEY = "AIzaSyAYnWwA6ha61NF-f64UGlNP6b5DJMnpR_4";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static AuthResult signUpAndGetResult(String email, String password) {
        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);
        return request("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY, payload,
                "Signup failed");
    }

    public static boolean signUp(String email, String password) {
        return signUpAndGetResult(email, password).isSuccess();
    }

    public static AuthResult signIn(String email, String password) {
        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);
        return request("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY, payload,
                "Login failed");
    }

    public static boolean changePassword(String email, String currentPassword, String newPassword) {
        AuthResult login = signIn(email, currentPassword);
        if (!login.isSuccess())
            return false;

        JSONObject payload = new JSONObject()
                .put("idToken", login.getIdToken())
                .put("password", newPassword)
                .put("returnSecureToken", true);

        AuthResult result = request(
                "https://identitytoolkit.googleapis.com/v1/accounts:update?key=" + API_KEY,
                payload,
                "Password update failed");
        return result.isSuccess();
    }

    private static AuthResult request(String url, JSONObject payload, String defaultError) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                return new AuthResult(
                        true,
                        json.optString("localId", null),
                        json.optString("idToken", null),
                        "");
            }

            JSONObject error = new JSONObject(response.body());
            String message = defaultError;
            if (error.has("error")) {
                JSONObject errorObject = error.getJSONObject("error");
                message = errorObject.optString("message", defaultError);
            }
            return new AuthResult(false, null, null, message);

        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResult(false, null, null, e.getMessage() == null ? defaultError : e.getMessage());
        }
    }

    public static class AuthResult {
        private final boolean success;
        private final String uid;
        private final String idToken;
        private final String errorMessage;

        public AuthResult(boolean success, String uid, String idToken, String errorMessage) {
            this.success = success;
            this.uid = uid;
            this.idToken = idToken;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getUid() {
            return uid;
        }

        public String getIdToken() {
            return idToken;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
