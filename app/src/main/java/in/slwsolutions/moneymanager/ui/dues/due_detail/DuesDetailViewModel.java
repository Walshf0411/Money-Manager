package in.slwsolutions.moneymanager.ui.dues.due_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.repositories.TransactionRepository;

public class DuesDetailViewModel extends AndroidViewModel {

    TransactionRepository transactionRepo;
    LiveData<List<Transaction>> contactTransactions;
    String contactLookupKey;

    public DuesDetailViewModel(@NonNull Application application) {
        super(application);
        transactionRepo = new TransactionRepository(application);
        if (contactLookupKey != null) {
            contactTransactions = getContactTransactions();
        }
    }

    public void setContactLookupKey(String contactLookupKey) {
        this.contactLookupKey = contactLookupKey;
        contactTransactions = getContactTransactions();
    }

    private LiveData<List<Transaction>> getContactTransactions() {
        return transactionRepo.getTransactionsByKey(contactLookupKey);
    }
}
