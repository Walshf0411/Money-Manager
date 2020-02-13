package in.slwsolutions.moneymanager.ui.expenses;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import in.slwsolutions.moneymanager.database.Expense;
import in.slwsolutions.moneymanager.repositories.ExpenseRepository;

public class ExpensesViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;

    public ExpensesViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
    }

    public void addNewExpense(Expense expense) {
        expenseRepository.addNewExpense(expense);
    }
}