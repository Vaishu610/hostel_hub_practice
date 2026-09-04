package com.hostelhub.controller;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseAuthController {

    private static final String API_KEY =
            "AIzaSyAYnWwA6ha61NF-f64UGlNP6b5DJMnpR_4";

    private static final HttpClient client =
            HttpClient.newHttpClient();

    public static AuthResult signUpAndGetResult(
            String email,
            String password) {

        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);

        try {

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(payload.toString())
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Signup Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Signup Response: "
                            + response.body()
            );

            if (response.statusCode() == 200) {

                JSONObject json =
                        new JSONObject(response.body());

                String uid =
                        json.getString("localId");

                String idToken =
                        json.getString("idToken");

                return new AuthResult(
                        true,
                        uid,
                        idToken,
                        ""
                );
            }

            JSONObject error =
                    new JSONObject(response.body());

            String message =
                    "Signup failed";

            if (error.has("error")) {

                JSONObject errorObject =
                        error.getJSONObject("error");

                if (errorObject.has("message")) {

                    message =
                            errorObject.getString("message");
                }
            }

            return new AuthResult(
                    false,
                    null,
                    null,
                    message
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new AuthResult(
                    false,
                    null,
                    null,
                    e.getMessage()
            );
        }
    }

    public static boolean signUp(
            String email,
            String password) {

        AuthResult result =
                signUpAndGetResult(
                        email,
                        password
                );

        return result.isSuccess();
    }

    public static AuthResult signIn(
            String email,
            String password) {

        JSONObject payload =
                new JSONObject()
                        .put("email", email)
                        .put("password", password)
                        .put("returnSecureToken", true);

        try {

            URI uri = URI.create(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                            + API_KEY
            );

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(payload.toString())
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Login Status: "
                            + response.statusCode()
            );

            System.out.println(
                    "Login Response: "
                            + response.body()
            );

            if (response.statusCode() == 200) {

                JSONObject json =
                        new JSONObject(response.body());

                String uid =
                        json.getString("localId");

                String idToken =
                        json.getString("idToken");

                System.out.println(
                        "LOGIN SUCCESS"
                );

                System.out.println(
                        "Logged in UID: "
                                + uid
                );

                return new AuthResult(
                        true,
                        uid,
                        idToken,
                        ""
                );
            }

            JSONObject error =
                    new JSONObject(response.body());

            String message =
                    "Login failed";

            if (error.has("error")) {

                JSONObject errorObject =
                        error.getJSONObject("error");

                if (errorObject.has("message")) {

                    message =
                            errorObject.getString(
                                    "message"
                            );
                }
            }

            return new AuthResult(
                    false,
                    null,
                    null,
                    message
            );

        } catch (Exception e) {

            e.printStackTrace();

            return new AuthResult(
                    false,
                    null,
                    null,
                    e.getMessage()
            );
        }
    }

    public static class AuthResult {

        private final boolean success;
        private final String uid;
        private final String idToken;
        private final String errorMessage;

        public AuthResult(
                boolean success,
                String uid,
                String idToken,
                String errorMessage) {

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