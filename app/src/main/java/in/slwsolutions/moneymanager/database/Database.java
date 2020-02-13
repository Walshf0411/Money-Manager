package in.slwsolutions.moneymanager.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.slwsolutions.moneymanager.dao.ExpenseDao;
import in.slwsolutions.moneymanager.dao.TransactionDao;

@androidx.room.Database(entities = {Transaction.class, Expense.class}, version = 14)
@TypeConverters({DateConverter.class})
public abstract class Database extends RoomDatabase {
    public abstract TransactionDao transactionDao();
    public abstract ExpenseDao expenseDao();

    private static volatile Database INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "transaction_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}