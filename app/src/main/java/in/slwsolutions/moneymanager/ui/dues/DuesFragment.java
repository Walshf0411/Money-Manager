package in.slwsolutions.moneymanager.ui.dues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Database;
import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.workers.FetchTransactionsWorker;

public class DuesFragment extends Fragment {

    private DuesViewModel duesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        duesViewModel =
                ViewModelProviders.of(this).get(DuesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dues, container, false);

        inflateDB();

        return root;
    }

    private void inflateDB() {
        OneTimeWorkRequest dbInsertTask = new OneTimeWorkRequest.Builder(FetchTransactionsWorker.class)
                .build();

        WorkManager.getInstance(getContext()).enqueue(dbInsertTask);
    }
}