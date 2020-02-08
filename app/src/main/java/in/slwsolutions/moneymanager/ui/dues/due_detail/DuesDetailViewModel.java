package in.slwsolutions.moneymanager.ui.dues.due_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import in.slwsolutions.moneymanager.repositories.TransactionRepository;

public class DuesDetailViewModel extends AndroidViewModel {

    TransactionRepository transactionRepo;

    public DuesDetailViewModel(@NonNull Application application) {
        super(application);
        transactionRepo = new TransactionRepository(application);
    }
}
