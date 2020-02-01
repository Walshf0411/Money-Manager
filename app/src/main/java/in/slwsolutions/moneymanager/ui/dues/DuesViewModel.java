package in.slwsolutions.moneymanager.ui.dues;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DuesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DuesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}