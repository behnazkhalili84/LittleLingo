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
    private static final String API_KEY = "33d655e7ac6b29ceaa50a1b4d0c78579-623e10c8-1f41a326";
    private static final String MAILGUN_DOMAIN = "sandboxad7e3b4966de4c938a90fb863061260a.mailgun.org";
    private static final String BASE_URL = "https://api.mailgun.net/v3/" + MAILGUN_DOMAIN + "/messages";

    private final OkHttpClient client = new OkHttpClient();

    public void sendSimpleMessage(String to, String subject, String text) throws IOException {
        String credential = Credentials.basic("api", API_KEY);

        RequestBody formBody = new FormBody.Builder()
                .add("from", "Excited User <postmaster@" + MAILGUN_DOMAIN + ">")
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
//old api
//sandboxad7e3b4966de4c938a90fb863061260a.mailgun.org
//33d655e7ac6b29ceaa50a1b4d0c78579-623e10c8-1f41a326
//new key
//98dda2cf55d65e1c3b2563e7691db22f-623e10c8-720a8c63