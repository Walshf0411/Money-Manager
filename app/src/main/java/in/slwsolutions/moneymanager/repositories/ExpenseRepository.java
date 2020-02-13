package in.slwsolutions.moneymanager.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import in.slwsolutions.moneymanager.dao.ExpenseDao;
import in.slwsolutions.moneymanager.database.Database;
import in.slwsolutions.moneymanager.database.Expense;

public class ExpenseRepository {
    private Context context;
    private LiveData<Expense> expenses;
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

    LiveData<Expense> getAllExpenses() {
        return this.expenses;
    }

}
