package in.slwsolutions.moneymanager.ui.dues;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.repositories.TransactionRepository;

public class DuesViewModel extends AndroidViewModel {

    private TransactionRepository transactionRepository;

    private LiveData<List<Transaction>> transactions;

    public DuesViewModel(Application application) {
        super(application);
        transactionRepository = new TransactionRepository(application);
        transactions = transactionRepository.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }

    public void deleteAll() {
        transactionRepository.deleteAll();
    }

    public LiveData<List<Transaction>> getTransactionsByKey(String key) {
        return transactionRepository.getTransactionsByKey(key);
    }
}