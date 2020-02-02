package in.slwsolutions.moneymanager.ui.dues;

import android.content.Context;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;

public class DuesRecyclerViewAdapter extends RecyclerView.Adapter<DuesRecyclerViewAdapter.DuesViewHolder>{

    private List<Transaction> transactions;
    private Context context;

    public DuesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DuesRecyclerViewAdapter.DuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.dues_item, parent,false);
        return new DuesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DuesRecyclerViewAdapter.DuesViewHolder holder, int position) {
        if (transactions != null) {
            holder.contactKey.setText(transactions.get(position).contactLookupKey);
        }
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();

        return 0;
    }

    public class DuesViewHolder extends RecyclerView.ViewHolder{
        private TextView contactKey;

        public DuesViewHolder(@NonNull View itemView) {
            super(itemView);
            contactKey = (TextView)itemView.findViewById(R.id.contact_name);
        }
    }
}