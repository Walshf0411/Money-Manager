package in.slwsolutions.moneymanager.ui.dues;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import in.slwsolutions.moneymanager.MainActivity;
import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;
import in.slwsolutions.moneymanager.repositories.TransactionRepository;
import in.slwsolutions.moneymanager.workers.FetchTransactionsWorker;

public class DueDetailActivity extends AppCompatActivity {

    private TextView name, number;
    private ImageView profileImage;
    private RecyclerView recyclerView;
    private Intent intent;
    private TransactionRepository repo;
    private DuesDetailRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_detail);
        intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        adapter = new DuesDetailRecyclerViewAdapter(this, null);

        initializeComponents();
        initializeRecyclerView();
        populateRecyclerView();
    }

    private void initializeComponents() {
        name = (TextView) findViewById(R.id.contact_name);
        number = (TextView) findViewById(R.id.contact_number);
        profileImage = (ImageView)findViewById(R.id.profile_image);

        name.setText(intent.getStringExtra("contact_name"));
        number.setText(intent.getStringExtra("contact_number"));
        if (intent.getStringExtra("contact_image_uri") != null){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(intent.getStringExtra("contact_image_uri")));
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void populateRecyclerView() {
        repo = new TransactionRepository(this);

        class GetAllTransactions extends AsyncTask<String, Void, List<Transaction>> {
            protected List<Transaction> doInBackground(String... urls) {
                return repo.getTransactionsByKey(urls[0]);
            }

            protected void onProgressUpdate(Void... progress) {

            }

            protected void onPostExecute(List<Transaction> transactions) {
                adapter.setTransactionList(transactions);
            }
        }
        new GetAllTransactions().execute(intent.getStringExtra("contact_lookup_key"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                finishActivity(0);
                return true;

            case R.id.delete_due:
                deleteDue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDue() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("This due is be deleted");
        progressDialog.show();

        class DeleteDuesTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                repo.deleteDuesByContactKey(strings[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
                Intent revIntent = new Intent(DueDetailActivity.this, MainActivity.class);
                revIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(revIntent);
            }
        }

        new DeleteDuesTask().execute(intent.getStringExtra("contact_lookup_key"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.due_detail_menu, menu);
        return true;
    }
}