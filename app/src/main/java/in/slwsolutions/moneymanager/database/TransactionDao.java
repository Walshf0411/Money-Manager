package in.slwsolutions.moneymanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Insert
    void insertAll(Transaction... transactions);

    @Query("Select id, contactLookupKey, contactName, contactNumber, " +
            "contactImageURI, amount, lent, timestamp, sum(amount)" +
            "from `transaction` group by contactLookupKey")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("DELETE FROM `transaction`")
    void deleteAll();
}
