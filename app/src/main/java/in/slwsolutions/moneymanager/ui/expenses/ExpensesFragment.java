package in.slwsolutions.moneymanager.ui.expenses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Expense;

public class ExpensesFragment extends Fragment {

    private ExpensesViewModel expensesViewModel;
    private RecyclerView recyclerView;
    private TextView dateHeader;
    private FloatingActionButton addExpenseButton;
    private TextInputLayout notes, amount;
    private View root;
    private Date todaysDate;
    private ExpensesRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        expensesViewModel =
                ViewModelProviders.of(this).get(ExpensesViewModel.class);
        root = inflater.inflate(R.layout.fragment_expenses, container, false);
        todaysDate = new Date(); // this will come handy to set the date on the header and
        // for adding new expense to the database
        initializeComponents();
        initializeRecyclerView();
        return root;
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        adapter = new ExpensesRecyclerViewAdapter(getContext(), null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setTodaysExpenses(expensesViewModel.getTodaysExpenses().getValue());
        recyclerView.setAdapter(adapter);

        expensesViewModel.getTodaysExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                Log.d("MoneyManager", "All Expenses: " + expenses.toString());
                List<Expense> todaysExpenses = getTodaysExpenses(expenses);
                Log.d("MoneyManager", "Today's Expenses" + todaysExpenses.toString());
                adapter.setTodaysExpenses(todaysExpenses);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private List<Expense> getTodaysExpenses(List<Expense> expenses) {
        List<Expense> todaysExpenses = new ArrayList<>();
        for (Expense expense : expenses) {
//            Log.i("MoneyManager", expense.timestamp.toString());
            if (isToday(expense.timestamp)) {
                todaysExpenses.add(expense);
            }
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

    private void initializeComponents() {
        dateHeader = (TextView) root.findViewById(R.id.expenses_today_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
        dateHeader.setText(dateFormat.format(todaysDate));

        addExpenseButton = (FloatingActionButton) root.findViewById(R.id.add_expense);
        addExpenseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createAlertDialogForm();
                    }
                }
        );
    }

    private void createAlertDialogForm() {
        View alertDialogForm = getLayoutInflater().inflate(R.layout.layout_add_new_expense, null, false);

        notes = (TextInputLayout) alertDialogForm.findViewById(R.id.notes);
        amount = (TextInputLayout) alertDialogForm.findViewById(R.id.amount);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Add new Expense")
                .setView(alertDialogForm)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (formValid()) {
                            addToDatabase();
                            // as we have set null as listeners when creating the alert dialog
                            // and have manually taken access to the button and set onclick
                            // listeners the dialog wont automatically dismiss, hence we call the
                            // dismiss method on the alertDialog object
                            alertDialog.dismiss();
                        }
                    }
                }
        );

    }

    private void addToDatabase() {
        Expense expense = new Expense(
                Double.valueOf(amount.getEditText().getText().toString()),
                notes.getEditText().getText().toString(),
                todaysDate
        );
        expensesViewModel.addNewExpense(expense);
    }

    private boolean formValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(notes.getEditText().getText())) {
            // check if the notes is empty
            valid = false;
            notes.setError("Notes cannot be empty");
        } else {
            notes.setError("");
        }

        if (TextUtils.isEmpty(amount.getEditText().getText())) {
            valid = false;
            amount.setError("Amount cannot be empty");
        } else {
            amount.setError("");
        }

        return valid;
    }
}