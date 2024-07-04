package com.example.littlelingo.ui.shopping;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("create-payment-intent")
    Call<PaymentIntentResponse> createPaymentIntent(String bearerpk_test_51PXtLyDaLkoIps9pdltuFN4mhpc41kuZOAh2Y9wasAdvDepYacQxZ0y6uxB5JsvH3drStpoFWDVACETz4niPPfwu00S76Byt0o,
                                                    @Body PaymentIntentRequest request);

}

//public interface ApiService {
//    @POST("create-payment-intent")
//    Call<PaymentIntentResponse> createPaymentIntent(
//            String bearerToken,
//            @Body PaymentIntentRequest request
//    );
//}