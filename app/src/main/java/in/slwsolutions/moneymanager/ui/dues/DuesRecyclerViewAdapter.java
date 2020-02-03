package in.slwsolutions.moneymanager.ui.dues;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;

public class DuesRecyclerViewAdapter extends RecyclerView.Adapter<DuesRecyclerViewAdapter.DuesViewHolder>{

    private List<Transaction> transactions;
    private Context context;
    private DuesRecyclerViewItemClickListener activityCommander = null;

    public DuesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void setITemClickListener(DuesRecyclerViewItemClickListener activityCommander) {
        this.activityCommander = activityCommander;
    }

    @NonNull
    @Override
    public DuesRecyclerViewAdapter.DuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.dues_item, parent,false);
        return new DuesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DuesRecyclerViewAdapter.DuesViewHolder holder, final int position) {
        if (transactions != null) {
            final Transaction transaction = transactions.get(position);
            holder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activityCommander.onClick(position, transaction, holder.profileImage);
                        }
                    }
            );
            holder.contactName.setText(transaction.contactName);
            holder.phoneNumber.setText(transaction.contactNumber);
            if (transaction.contactImageURI != null){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(transaction.contactImageURI));
                    holder.profileImage.setImageBitmap(bitmap);
                    ViewCompat.setTransitionName(holder.profileImage, transaction.contactName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (transaction.amountAggregate < 0) {
                // The User is in due by the contact
                holder.amount.setText("-" + context.getString(R.string.Rs) + String.valueOf(-transaction.amountAggregate));
                holder.amount.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            } else {
                // The User has money owed to the contact
                holder.amount.setText("+"+ context.getString(R.string.Rs) + String.valueOf(transaction.amountAggregate));
                holder.amount.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (transactions != null)
            return transactions.size();

        return 0;
    }

    public class DuesViewHolder extends RecyclerView.ViewHolder{
        private ImageView profileImage;
        private TextView contactName, phoneNumber, amount;

        public DuesViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = (TextView)itemView.findViewById(R.id.contact_name);
            phoneNumber =  (TextView)itemView.findViewById(R.id.phone_number);
            amount = (TextView) itemView.findViewById(R.id.amount);
            profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
        }
    }

    public interface DuesRecyclerViewItemClickListener{
        void onClick(int position, Transaction transaction, ImageView profileImage);
    }
}