package at.jamo.pp_snowheightapp.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReadEpromSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReadEpromSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}