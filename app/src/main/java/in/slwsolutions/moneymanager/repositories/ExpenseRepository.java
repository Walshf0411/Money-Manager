package in.slwsolutions.moneymanager.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import in.slwsolutions.moneymanager.dao.ExpenseDao;
import in.slwsolutions.moneymanager.database.Database;
import in.slwsolutions.moneymanager.database.Expense;

public class ExpenseRepository {
    private Context context;
    private LiveData<List<Expense>> expenses;
    private ExpenseDao expenseDao;

    public ExpenseRepository(Context context) {
        this.context = context;
        Database database = Database.getDatabase(context);
        this.expenseDao = database.expenseDao();
        this.expenses = this.expenseDao.getAllExpense();
    }

    public void addNewExpense(final Expense expense) {
        Database.databaseWriteExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        expenseDao.insert(expense);
                    }
                }
        );
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return this.expenses;
    }

    public LiveData<List<Expense>> getExpensesBetweenDates(Date start, Date end){
        return expenseDao.getExpenses(start, end);
    }
}
