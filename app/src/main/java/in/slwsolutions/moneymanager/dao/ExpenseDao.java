package in.slwsolutions.moneymanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import in.slwsolutions.moneymanager.database.Expense;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Query("DELETE FROM expense WHERE id=:id")
    void delete(int id);

    @Query("DELETE FROM Expense")
    void deleteAll();

    @Query("SELECT * FROM expense WHERE timestamp BETWEEN :start AND :end")
    LiveData<List<Expense>> getExpenses(Date start, Date end);

    @Query("SELECT * FROM expense")
    LiveData<List<Expense>> getAllExpense();
}
