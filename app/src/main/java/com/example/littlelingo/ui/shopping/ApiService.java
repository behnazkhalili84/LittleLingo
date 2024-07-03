package com.example.littlelingo.ui.shopping;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("create-payment-intent")
    Call<PaymentIntentResponse> createPaymentIntent(String bearerSkTest51PXtLyDaLkoIps9pRiStHFkHpyiYYC2KsPEMyLrQhtLkRj8x6uWnznBEowGrzE6ON7eeODAhzUMF4s1zq0MPzhHH00e1jqDzSL, @Body PaymentIntentRequest request);

}
