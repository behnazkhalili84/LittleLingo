package com.example.littlelingo.ui.resultVocabulary;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ResultVocabularyModelView extends ViewModel {
    private final MutableLiveData<Integer> _correctAnswers = new MutableLiveData<>();
    private final MutableLiveData<Integer> _totalQuestions = new MutableLiveData<>();

    public LiveData<Integer> getCorrectAnswers() {
        return _correctAnswers;
    }

    public LiveData<Integer> getTotalQuestions() {
        return _totalQuestions;
    }

    public void setResults(int correct, int total) {
        _correctAnswers.setValue(correct);
        _totalQuestions.setValue(total);
    }

    public void resetResults() {
        _correctAnswers.setValue(0);
        _totalQuestions.setValue(0);
    }
}
