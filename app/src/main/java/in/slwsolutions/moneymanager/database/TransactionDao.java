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

    @Query("Select * from `transaction`")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("DELETE FROM `transaction`")
    void deleteAll();
}
