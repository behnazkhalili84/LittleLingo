package com.example.littlelingo.ui.vocabularyquiz;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.littlelingo.ui.user.AuthRepository;
import com.example.littlelingo.ui.user.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VocabularyQuizViewModel extends ViewModel {

    private AuthRepository authRepository;
    private MutableLiveData<Users> userLiveData;

    private void loadQuestionsFromFirebase() {
        DatabaseReference questionsRef = FirebaseDatabase.getInstance().getReference("questions");
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<VocabularyQuestion> questions = new ArrayList<>();
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    VocabularyQuestion question = questionSnapshot.getValue(VocabularyQuestion.class);
                    questions.add(question);
                }
                questionsList.postValue(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            public LiveData<Users> getUserLiveData() {
                return authRepository.getUserLiveData();
            }

            private MutableLiveData<List<VocabularyQuestion>> questionsList;
            private MutableLiveData<Integer> currentPosition;
            private MutableLiveData<Integer> selectedOptionPosition;
            private MutableLiveData<Integer> correctAnswer;

            public void VocabularyQuizViewModel() {
                userLiveData = new MutableLiveData<>();
                questionsList = new MutableLiveData<>();
                currentPosition = new MutableLiveData<>(1);
                selectedOptionPosition = new MutableLiveData<>(0);
                correctAnswer = new MutableLiveData<>(0);
                authRepository = new AuthRepository();
                loadQuestionsFromFirebase();
            }



            public LiveData<List<VocabularyQuestion>> getQuestionsList() {
                return questionsList;
            }

            public LiveData<Integer> getCurrentPosition() {
                return currentPosition;
            }

            public LiveData<Integer> getSelectedOptionPosition() {
                return selectedOptionPosition;
            }

            public LiveData<Integer> getCorrectAnswer() {
                return correctAnswer;
            }

            public void setQuestion(int position) {
                currentPosition.setValue(position);
            }

            public void selectOption(int position) {
                selectedOptionPosition.setValue(position);
            }

            public void incrementCorrectAnswer() {
                correctAnswer.setValue((correctAnswer.getValue() != null ? correctAnswer.getValue() : 0) + 1);
            }



            private void loadQuestionsFromFirebase() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("questions");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<VocabularyQuestion> questions = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            VocabularyQuestion question = snapshot.getValue(VocabularyQuestion.class);
                            questions.add(question);
                        }
                        questionsList.setValue(questions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });
            }
        });
    }

}

