package in.slwsolutions.moneymanager.ui.expenses;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Expense;

public class ExpensesRecyclerViewAdapter extends RecyclerView.Adapter<ExpensesRecyclerViewAdapter.ExpensesRecyclerViewHolder> {

    Context context;
    List<Expense> expenseList;

    public ExpensesRecyclerViewAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpensesRecyclerViewAdapter.ExpensesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpensesRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpensesRecyclerViewAdapter.ExpensesRecyclerViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.amount.setText(context.getString(R.string.Rs) + String.valueOf(expense.amount));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        holder.date.setText(dateFormat.format(expense.timestamp));
        holder.notes.setText(expense.notes);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.notes.getVisibility() == View.VISIBLE) {
                            holder.notes.setVisibility(View.GONE);
                        } else {
                            holder.notes.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return expenseList == null? 0: expenseList.size();
    }

    public class ExpensesRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView amount, notes, date;

        public ExpensesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            notes = itemView.findViewById(R.id.expense_notes);
            date = itemView.findViewById(R.id.date);
        }
    }

}