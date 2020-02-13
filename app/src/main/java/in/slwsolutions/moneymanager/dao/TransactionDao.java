package in.slwsolutions.moneymanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import in.slwsolutions.moneymanager.database.Transaction;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Insert
    void insertAll(Transaction... transactions);

    @Query("SELECT id, contactLookupKey, contactName, contactNumber, " +
            "contactImageURI, amount, lent, timestamp, notes, sum(amount)" +
            "FROM `transaction` GROUP BY contactLookupKey")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("DELETE FROM `transaction`")
    void deleteAll();

    @Query("DELETE FROM `transaction` WHERE contactLookupKey=:contactKey")
    void deleteDuesByContactKey(String contactKey);

    @Query("SELECT * FROM `transaction` WHERE contactLookupKey=:key ORDER BY timestamp DESC")
    LiveData<List<Transaction>> getTransactionsByLookupKey(String key);
}
