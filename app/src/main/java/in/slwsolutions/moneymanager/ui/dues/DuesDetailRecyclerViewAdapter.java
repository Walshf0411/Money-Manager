package in.slwsolutions.moneymanager.ui.dues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;

public class DuesDetailRecyclerViewAdapter extends RecyclerView.Adapter<DuesDetailRecyclerViewAdapter.DuesDetailRecyclerViewHolder> {

    private List<Transaction> transactionList;
    private Context ctx;

    public DuesDetailRecyclerViewAdapter(Context ctx, List<Transaction> transactionList) {
        this.ctx = ctx;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public DuesDetailRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.detail_due_item, parent, false);
        return new DuesDetailRecyclerViewHolder(view);
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DuesDetailRecyclerViewHolder holder, int position) {
        if (transactionList != null) {
            Transaction transaction = transactionList.get(position);
            if (transaction.amount < 0) {
                // The User is in due by the contact
                holder.amount.setText(String.valueOf(transaction.amount));
                holder.amount.setTextColor(ctx.getResources().getColor(android.R.color.holo_red_light));
            } else {
                // The User has money owed to the contact
                holder.amount.setText("+" + String.valueOf(transaction.amount));
                holder.amount.setTextColor(ctx.getResources().getColor(android.R.color.holo_green_light));
            }
            holder.date.setText(String.valueOf(transaction.timestamp));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public class DuesDetailRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView amount, date;

        public DuesDetailRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.amount);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
