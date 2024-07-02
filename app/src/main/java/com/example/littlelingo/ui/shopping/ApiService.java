package com.example.littlelingo.ui.shopping;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("create-payment-intent")
    Call<PaymentIntentResponse> createPaymentIntent(@Body PaymentIntentRequest request);
}
