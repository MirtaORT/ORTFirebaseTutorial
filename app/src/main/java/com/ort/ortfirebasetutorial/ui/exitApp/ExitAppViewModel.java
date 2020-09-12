package com.ort.ortfirebasetutorial.ui.exitApp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExitAppViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ExitAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("¿De verdad queres irte ya?");
    }

    public LiveData<String> getText() {
        return mText;
    }
}