package in.slwsolutions.moneymanager.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import in.slwsolutions.moneymanager.database.Database;


public class FetchTransactionsWorker extends Worker {
    Context context;
    WorkerParameters workerParams;

    public FetchTransactionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
    }

    @NonNull
    @Override
    public Result doWork() {
        Database.getDatabase(context).transactionDao()
                .getTransactionsByLookupKey(workerParams.getInputData().getString("contact_lookup_key"));
        return Result.success();
    }
}
