package in.slwsolutions.moneymanager.ui.expenses;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.slwsolutions.moneymanager.database.Expense;
import in.slwsolutions.moneymanager.repositories.ExpenseRepository;

public class ExpensesViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;
    private LiveData<List<Expense>> todaysExpenses;

    public ExpensesViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        todaysExpenses = getTodaysExpenses();
    }

    public LiveData<List<Expense>> getTodaysExpenses() {
        if (todaysExpenses == null) {
            return expenseRepository.getAllExpenses();
        }

        return todaysExpenses;
    }

    private boolean isToday(Date date) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        // SET THE TIME TO 0hh 0mm 0ss 00ms
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date start = cal.getTime(); // this will return the time at the start of the day

        cal.add(Calendar.DATE, 1); // add one day to the calendar

        Date end = cal.getTime(); // get the date

        Log.d("MoneyManager", start.toString() + ".compareTo(" + date.toString() + "): " + start.compareTo(date));
        return start.compareTo(date) < 0 && // date should be greater than today's date
                end.compareTo(date) > 0; // ans should be less than tomorrow's date
    }

    public void setTodaysExpenses(LiveData<List<Expense>> todaysExpenses) {
        this.todaysExpenses = todaysExpenses;
    }

    public void addNewExpense(Expense expense) {
        expenseRepository.addNewExpense(expense);
    }
}