package in.slwsolutions.moneymanager.database;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {Transaction.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class Database extends RoomDatabase {
    public abstract TransactionDao transactionDao();
}
