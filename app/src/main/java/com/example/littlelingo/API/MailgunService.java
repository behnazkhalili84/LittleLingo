package com.example.littlelingo.API;

import android.util.Log;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class MailgunService {

    private static final String TAG = "MailgunService";
    private static final String API_KEY = "secure-api-key";
    private static final String MAILGUN_DOMAIN = "sandbox.mailgun.org";
    private static final String BASE_URL = "https://api.mailgun.net/v3/" + MAILGUN_DOMAIN + "/messages";
    private String username;

    private final OkHttpClient client = new OkHttpClient();

    public MailgunService(String username) {
        this.username = username;
    }

    public void sendSimpleMessage(String to, String subject, String text) throws IOException {
        String credential = Credentials.basic("api", API_KEY);

        RequestBody formBody = new FormBody.Builder()
                .add("from", username + " <postmaster@" + MAILGUN_DOMAIN + ">")
                .add("to", to)
                .add("subject", subject)
                .add("text", text)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(formBody)
                .header("Authorization", credential)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e(TAG, "Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            } else {
                Log.d(TAG, "Response: " + response.body().string());
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            throw e;
        }
    }
}
