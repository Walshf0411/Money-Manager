package in.slwsolutions.moneymanager.ui.dues;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.repositories.TransactionRepository;

public class DuesViewModel extends ViewModel {

    private TransactionRepository transactionRepository;

    private LiveData<List<Transaction>> transactions;

    public DuesViewModel(Context context) {
        super();
        transactionRepository = new TransactionRepository(context);
        transactions = transactionRepository.getTransactions();
    }

    LiveData<List<Transaction>> getAllWords() {
        return transactions;
    }

    public void insert(Transaction transaction) {
        transactionRepository.insert(transaction);
    }
}