package com.example.littlelingo.ui.quiz;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateQuestions {

    private DatabaseReference mDatabase;

    public CreateQuestions() {
        mDatabase = FirebaseDatabase.getInstance().getReference("questions");
    }

    public void addQuestions() {
        // Question 1
        Questions vocabularyQuestion1 = new Questions(1, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/apple.jpeg?alt=media&token=6e2a3e1b-ccd6-4997-b2bb-2fa849e48d4a",
                "drink", "water", "apple", "nose", "apple", "vocabulary");
        mDatabase.child("1").setValue(vocabularyQuestion1);

        // Question 2
        Questions vocabularyQuestion2 = new Questions(2, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/water.jpeg?alt=media&token=2b543b26-4443-44fa-adf8-1dc2697d1128",
                "juice", "water", "apple", "nose", "water", "vocabulary");
        mDatabase.child("2").setValue(vocabularyQuestion2);

        // Question 3
        Questions vocabularyQuestion3 = new Questions(3, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/juice.jpeg?alt=media&token=8bd7359a-1318-4b40-b1e6-3385e1e2efa3",
                "juice", "water", "apple", "nose", "juice", "vocabulary");
        mDatabase.child("3").setValue(vocabularyQuestion3);

        // Question 4
        Questions vocabularyQuestion4 = new Questions(4, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/drink.jpeg?alt=media&token=9c739bc7-d501-4cbf-894c-c9bf77fc5ca8",
                "juice", "water", "drink", "nose", "drink", "vocabulary");
        mDatabase.child("4").setValue(vocabularyQuestion4);

        // Question 5
        Questions vocabularyQuestion5 = new Questions(5, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/toes.jpeg?alt=media&token=336ab8d1-3f5a-4007-8135-2563fabbb834",
                "Toes", "water", "drink", "nose", "Toes", "vocabulary");
        mDatabase.child("5").setValue(vocabularyQuestion5);

        // Question 6
        Questions vocabularyQuestion6 = new Questions(6, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/nose.ogg?alt=media&token=1ea4d054-b872-4f7a-8749-afdf552ee021",
                "Noes", "Toes", "water", "drink", "Noes", "vocabulary");
        mDatabase.child("6").setValue(vocabularyQuestion6);

        // Question 7
        Questions vocabularyQuestion7 = new Questions(7, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/biscuit.jpeg?alt=media&token=76f5c488-665b-4045-8c12-7b2eaaee2a6c",
                "biscuit", "milk", "water", "apple", "biscuit", "vocabulary");
        mDatabase.child("7").setValue(vocabularyQuestion7);

        // Question 8
        Questions vocabularyQuestion8 = new Questions(8, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/chair.jpeg?alt=media&token=78c4ba05-6b13-4cfe-83df-bb7c6ef2ea6e",
                "cup", "drink", "chair", "plate", "chair", "vocabulary");
        mDatabase.child("8").setValue(vocabularyQuestion8);

        // Question 9
        Questions vocabularyQuestion9 = new Questions(9, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/cup.jpeg?alt=media&token=c4142c8b-14e0-4619-b597-03a25f72e23d",
                "cup", "plate", "chair", "nose", "cup", "vocabulary");
        mDatabase.child("9").setValue(vocabularyQuestion9);

        // Question 10
        Questions vocabularyQuestion10 = new Questions(10, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/dady.jpeg?alt=media&token=06f91ba9-d230-4aa7-9cf6-8348b086d1b4",
                "daddy", "mommy", "cup", "plate", "daddy", "vocabulary");
        mDatabase.child("10").setValue(vocabularyQuestion10);

        // Question 11
        Questions vocabularyQuestion11 = new Questions(11, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/milk.jpeg?alt=media&token=2eb78a24-4efb-4505-8d92-59ccfa9fb4f8",
                "cup", "nose", "water", "milk", "milk", "vocabulary");
        mDatabase.child("11").setValue(vocabularyQuestion11);

        // Question 12
        Questions vocabularyQuestion12 = new Questions(12, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/mommy.jpeg?alt=media&token=77ebd833-56db-4c37-8db3-2b8243aa8890",
                "daddy", "mommy", "cup", "plate", "mommy", "vocabulary");
        mDatabase.child("12").setValue(vocabularyQuestion12);

        // Question 13
        Questions vocabularyQuestion13 = new Questions(13, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/mouth.jpeg?alt=media&token=8501e6bb-1943-4d47-9d1d-337d4823668f",
                "Noes", "Toes", "mouth", "sweet", "mouth", "vocabulary");
        mDatabase.child("13").setValue(vocabularyQuestion13);

        // Question 14
        Questions vocabularyQuestion14 = new Questions(14, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/plate.png?alt=media&token=503c43cb-95d8-4413-9e19-7c85e2285be5",
                "noes", "toes", "plate", "cup", "plate", "vocabulary");
        mDatabase.child("14").setValue(vocabularyQuestion14);

        // Question 15
        Questions vocabularyQuestion15 = new Questions(15, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/sweets.jpeg?alt=media&token=f44bc33e-4108-488d-abf8-6a52125e29c8",
                "Sweets", "Toes", "water", "drink", "Sweets", "vocabulary");
        mDatabase.child("15").setValue(vocabularyQuestion15);

        // Question 16
        Questions vocabularyQuestion16 = new Questions(16, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/table.jpeg?alt=media&token=c8169476-cdf1-447c-a51b-82200f69b85b",
                "Noes", "Table", "water", "drink", "Table", "vocabulary");
        mDatabase.child("16").setValue(vocabularyQuestion16);

        // Question 1
        Questions grammarQuestion1 = new Questions(17, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/aimage.jpeg?alt=media&token=9c571fd0-1903-43e9-8eb0-c8e8c61f0a54",
                "Alligator", "Balloon", "Chair", "drink", "Alligator", "grammar");
        mDatabase.child("17").setValue(grammarQuestion1);

        // Question 2
        Questions grammarQuestion2 = new Questions(18, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/bimage.jpeg?alt=media&token=9d1b439b-1bd7-4ed6-9258-27b56dfc993a",
                "juice", "Balloon", "apple", "nose", "Balloon", "grammar");
        mDatabase.child("18").setValue(grammarQuestion2);

        // Question 3
        Questions grammarQuestion3 = new Questions(19, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/cimage.jpeg?alt=media&token=d6390ba3-0cd4-4a1b-bb8b-9b6e85674910",
                "Carrot", "Water", "Apple", "Biscuit", "Carrot", "grammar");
        mDatabase.child("19").setValue(grammarQuestion3);

        // Question 4
        Questions grammarQuestion4 = new Questions(20, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/dimage.jpeg?alt=media&token=f2b6b105-1182-4143-a056-54c04d50295a",
                "juice", "water", "deer", "nose", "deer", "grammar");
        mDatabase.child("20").setValue(grammarQuestion4);

        // Question 5
        Questions grammarQuestion5 = new Questions(21, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/eimage.jpeg?alt=media&token=2df91982-c97c-4359-9654-34fb7bf3a68b",
                "Eye", "deer", "bees", "apple", "Eye", "grammar");
        mDatabase.child("21").setValue(grammarQuestion5);

        // Question 6
        Questions grammarQuestion6 = new Questions( 22, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/fimage.jpeg?alt=media&token=f8e9b5a6-7c86-4005-aca3-1663c8562102",
                "Noes", "Fish", "water", "drink", "Fish", "grammar");
        mDatabase.child("22").setValue(grammarQuestion6);

        // Question 7
        Questions grammarQuestion7 = new Questions(23, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/gimage1.jpeg?alt=media&token=688ad1f1-525e-445b-a6f4-a1581de8de32",
                "biscuit", "milk", "water", "Table", "grapes", "grammar");
        mDatabase.child("23").setValue(grammarQuestion7);

        // Question 8
        Questions grammarQuestion8 = new Questions( 24, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/himage.jpeg?alt=media&token=c82dfbce-a1fa-427b-921c-514e231b643e",
                "cup", "drink", "chair", "plate", "chair", "grammar");
        mDatabase.child("8").setValue(grammarQuestion8);


    }
}
