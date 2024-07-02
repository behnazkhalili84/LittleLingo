package com.example.littlelingo.ui.shopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.littlelingo.R;
import com.stripe.android.PaymentConfiguration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShoppingPage extends Fragment {

    private static final String TAG = "ShoppingActivity";
    private static final String BASE_URL = "http://10.0.2.2:3000/"; // Use the emulator's localhost address

    private ApiService apiService;


@NonNull
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_shopping_page, container, false);
    
        Button btnCreatePaymentIntent = view.findViewById(R.id.checkoutButton);
        btnCreatePaymentIntent.setOnClickListener(v -> createPaymentIntent());

        return view;
    }

    private void createPaymentIntent() {
        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        ApiService apiService = retrofit.create(ApiService.class);

        // Create PaymentIntentRequest
        PaymentIntentRequest request = new PaymentIntentRequest(5000, "usd"); // Example: 5000 cents = $50.00

        // Make network request
        Call<PaymentIntentResponse> call = apiService.createPaymentIntent(request);
        call.enqueue(new Callback<PaymentIntentResponse>() {
            @Override
            public void onResponse(Call<PaymentIntentResponse> call, Response<PaymentIntentResponse> response) {
                if (response.isSuccessful()) {
                    PaymentIntentResponse paymentIntentResponse = response.body();
                    if (paymentIntentResponse != null) {
                        String clientSecret = paymentIntentResponse.getClientSecret();
                        // Handle successful payment intent creation
                        Log.d(TAG, "Client Secret: " + clientSecret);
                        // Use the clientSecret to complete the payment on the client side
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(getContext(), "Failed to create payment intent", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PaymentIntentResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(getContext(), "Failed to create payment intent", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Network request failed", t);

            }
        });
            }
        }