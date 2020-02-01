package in.slwsolutions.moneymanager.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import in.slwsolutions.moneymanager.database.Database;
import in.slwsolutions.moneymanager.database.Transaction;

public class FetchTransactionsWorker extends Worker {
    Context context;

    public FetchTransactionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Database db = Room.databaseBuilder(context,
                Database.class, "database-name").build();

//        Transaction transaction = new Transaction("asdad", 45.0, true);
//
//        db.transactionDao().insert(transaction);

        List<Transaction> transactions =  db.transactionDao().getAllTransactions();


        return Result.success();
    }
}
