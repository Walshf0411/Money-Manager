package in.slwsolutions.moneymanager.ui.dues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;

public class DuesDetailRecyclerViewAdapter extends RecyclerView.Adapter<DuesDetailRecyclerViewAdapter.DuesDetailRecyclerViewHolder> {

    private List<Transaction> transactionList;
    private Context ctx;

    @NonNull
    @Override
    public DuesDetailRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.activity_due_detail, parent);
        return new DuesDetailRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DuesDetailRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return transactionList != null?transactionList.size():0;
    }

    public class DuesDetailRecyclerViewHolder extends RecyclerView.ViewHolder{

        public DuesDetailRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
