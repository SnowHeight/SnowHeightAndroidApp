package at.jamo.pp_snowheightapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReadSdDataViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReadSdDataViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}