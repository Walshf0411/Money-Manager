package in.slwsolutions.moneymanager.ui.expenses;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.slwsolutions.moneymanager.database.Expense;
import in.slwsolutions.moneymanager.repositories.ExpenseRepository;

public class ExpensesViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;
    private LiveData<List<Expense>> expensesList;

    public ExpensesViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        expensesList = expenseRepository.getAllExpenses();
    }

    public void addNewExpense(Expense expense) {
        expenseRepository.addNewExpense(expense);
    }

    public LiveData<List<Expense>>getExpensesList() {
        return expensesList;
    }
}