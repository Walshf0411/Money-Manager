package in.slwsolutions.moneymanager.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.List;

import in.slwsolutions.moneymanager.database.Database;
import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.database.TransactionDao;
import in.slwsolutions.moneymanager.workers.FetchTransactionsWorker;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> transactions;
    private Context context;

    public TransactionRepository(Context context) {
        this.context = context;
        Database db = Database.getDatabase(context);
        transactionDao = db.transactionDao();
        transactions = transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void insert(final Transaction transaction) {
        Database.databaseWriteExecutor.execute(new Runnable(){
            public void run() {
                transactionDao.insert(transaction);
            }
        });
    }

    public void deleteDuesByContactKey(final String contactKey) {
        Database.databaseWriteExecutor.execute(new Runnable(){
            public void run() {
                transactionDao.deleteDuesByContactKey(contactKey);
            }
        });
    }

    public void deleteAll() {
        Database.databaseWriteExecutor.execute(new Runnable(){
            public void run() {
                transactionDao.deleteAll();
            }
        });
    }

    public void insertAll(final Transaction... transactions) {
        Database.databaseWriteExecutor.execute(new Runnable(){
            public void run() {
                transactionDao.insertAll(transactions);
            }
        });
    }

    public LiveData<List<Transaction>> getTransactionsByKey(String key) {
        return transactionDao.getTransactionsByLookupKey(key);
    }
}
