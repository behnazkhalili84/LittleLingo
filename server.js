const express = require('express');
const stripe = require('stripe')('sk_test_51PXtLyDaLkoIps9pRiStHFkHpyiYYC2KsPEMyLrQhtLkRj8x6uWnznBEowGrzE6ON7eeODAhzUMF4s1zq0MPzhHH00e1jqDzSL'); // replace with your Stripe secret key
const app = express();

// Middleware to parse JSON bodies
app.use(express.json());

// Endpoint to create a payment intent
app.post('/create-payment-intent', async (req, res) => {
    const { amount } = req.body; // Expecting the amount to be passed in the request body

    try {
        // Create a payment intent with the specified amount
        const paymentIntent = await stripe.paymentIntents.create({
            amount,
            currency: 'usd',
        });

        // Respond with the client secret of the payment intent
        res.send({
            clientSecret: paymentIntent.client_secret,
        });
    } catch (error) {
        // Handle any errors that occur during the creation of the payment intent
        res.status(500).send({ error: error.message });
    }
});

// Start the server and listen on port 3000
app.listen(3000, () => console.log('Node server listening on port 3000!'));
