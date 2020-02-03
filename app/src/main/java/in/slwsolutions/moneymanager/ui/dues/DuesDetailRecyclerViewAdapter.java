package in.slwsolutions.moneymanager.ui.dues;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(@NonNull final DuesDetailRecyclerViewHolder holder, int position) {
        if (transactionList != null) {
            final Transaction transaction = transactionList.get(position);
            if (transaction.amount < 0) {
                // The User is in due by the contact
                holder.amount.setText("- " + ctx.getString(R.string.Rs) + String.valueOf(-transaction.amount));
                holder.amount.setTextColor(ctx.getResources().getColor(android.R.color.holo_red_light));
            } else {
                // The User has money owed to the contact
                holder.amount.setText("+" + ctx.getString(R.string.Rs) + String.valueOf(transaction.amount));
                holder.amount.setTextColor(ctx.getResources().getColor(android.R.color.holo_green_light));
            }
            holder.date.setText(String.valueOf(transaction.timestamp));

            if (transaction.notes != null) {
                holder.notes.setText(transaction.notes);
            }
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (transaction.notes != null) {
                                if (holder.notes.getVisibility() == View.GONE) {
                                    holder.notes.animate()
                                            .translationY(holder.notes.getHeight())
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animator) {
                                                    super.onAnimationEnd(animator);
                                                    holder.notes.setVisibility(View.VISIBLE);
                                                }
                                            });
                                } else if (holder.notes.getVisibility() == View.VISIBLE) {
                                    holder.notes.animate()
                                            .translationY(0)
                                            .setDuration(300)
                                            .setListener(new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animator) {
                                                    super.onAnimationEnd(animator);
                                                    holder.notes.setVisibility(View.GONE);
                                                }
                                            });
                                }
                                return;
                            }
                            Toast.makeText(ctx, "No notes associated with due", Toast.LENGTH_SHORT).show();

                        }
                    }
            );

        }
    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }

    public class DuesDetailRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView amount, date, notes;

        public DuesDetailRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.amount);
            date = (TextView) itemView.findViewById(R.id.date);
            notes = (TextView) itemView.findViewById(R.id.due_notes);
        }
    }
}
