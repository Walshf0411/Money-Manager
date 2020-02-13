package in.slwsolutions.moneymanager.ui.expenses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
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
        adapter.setExpenseList(expensesViewModel.getExpensesList().getValue());
        recyclerView.setAdapter(adapter);

        expensesViewModel.getExpensesList().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                adapter.setExpenseList(expenses);
                adapter.notifyDataSetChanged();
            }
        });
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

        Button positiveButton  = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(formValid()) {
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