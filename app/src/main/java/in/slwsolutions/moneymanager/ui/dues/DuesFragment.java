package in.slwsolutions.moneymanager.ui.dues;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import in.slwsolutions.moneymanager.R;
import in.slwsolutions.moneymanager.database.Transaction;

import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DuesFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CONTACT = 7979;
    private DuesViewModel duesViewModel;
    private RecyclerView recyclerView;
    private DuesRecyclerViewAdapter adapter;
    private FloatingActionButton addDues;
    private View root;
    private TextInputLayout contactName, amount, date, notes;
    private static final int SELECT_CONTACT = 9999;
    private String name, number = null, lookupKey, photoUri, hasPhoneNumber;
    private Switch lent;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        duesViewModel =
                ViewModelProviders.of(this).get(DuesViewModel.class);
        root = inflater.inflate(R.layout.fragment_dues, container, false);

        requestContactsPermission();

        addDues = root.findViewById(R.id.add_dues);
        addDues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogForm();
            }
        });

        setupRecyclerview();

        duesViewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
            }
        });

        return root;
    }

    private void setupRecyclerview() {
        recyclerView = root.findViewById(R.id.transactions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DuesRecyclerViewAdapter(getContext());
        adapter.setTransactions(duesViewModel.getTransactions().getValue());
        recyclerView.setAdapter(adapter);

        adapter.setITemClickListener(
                new DuesRecyclerViewAdapter.DuesRecyclerViewItemClickListener() {
                    @Override
                    public void onClick(int position, Transaction transaction, ImageView profileImage) {
                        Intent intent = new Intent(getContext(), DueDetailActivity.class);
                        intent.putExtra("contact_lookup_key", transaction.contactLookupKey);
                        intent.putExtra("contact_name", transaction.contactName);
                        intent.putExtra("contact_number", transaction.contactNumber);
                        intent.putExtra("contact_image_uri", transaction.contactImageURI);

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                profileImage,
                                ViewCompat.getTransitionName(profileImage));

                        startActivity(intent, options.toBundle());
                    }
                }
        );
    }

    private void createAlertDialogForm() {
        View alertDialogForm = getLayoutInflater().inflate(R.layout.layout_add_new_due, null);

        contactName = (TextInputLayout) alertDialogForm.findViewById(R.id.contact_name);
        contactName.getEditText().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_PICK);
                        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(i, SELECT_CONTACT);
                    }
                }
        );

        lent = (Switch) alertDialogForm.findViewById(R.id.lent);
        amount = (TextInputLayout) alertDialogForm.findViewById(R.id.amount);
        date = (TextInputLayout) alertDialogForm.findViewById(R.id.date);
        notes = (TextInputLayout) alertDialogForm.findViewById(R.id.notes);

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getContext().getString(R.string.add_new_due))
                .setView(alertDialogForm)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, null)
                .show();

        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          if (isFormValid()) {
                                              // form valid add it to the database
                                              addToDatabase();
                                              dialog.dismiss();
                                          }
                                      }
                                  }
        );
    }

    private void addToDatabase() {
        boolean lentChecked = lent.isChecked();
        Transaction t = new Transaction(
                lookupKey,
                name,
                number,
                photoUri,
                Double.valueOf(amount.getEditText().getText().toString()),
                lentChecked);

        duesViewModel.insert(t);
    }

    private boolean isFormValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(contactName.getEditText().getText())) {
            // Contact name is empty
            valid = false;
            Log.e(getString(R.string.TAG), "Contact Name is empty");
            contactName.setError(getString(R.string.contact_name_error));
        } else {
            contactName.setError("");
        }

        if (TextUtils.isEmpty(amount.getEditText().getText())) {
            // Amount is empty
            valid = false;
            Log.e(getString(R.string.TAG), "Amount is empty");
            amount.setError(getString(R.string.amount_error));
        } else {
            double value = Double.valueOf(amount.getEditText().getText().toString());
            if (value <= 0) {
                valid = false;
                Log.e(getString(R.string.TAG), "Amount should be greater than 0");
                amount.setError(getString(R.string.amount_less_than_zero_error));
            } else {
                amount.setError("");
            }
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // The user has chosen a contact
        if (requestCode == SELECT_CONTACT && resultCode == RESULT_OK) {

            // get the data from the selection
            Uri uri = data.getData();
            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

            Cursor cursor = getContext().getContentResolver().query(
                    uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // User has a phone number
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)) > 0) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    number = cursor.getString(numberIndex);
                }
            }

            lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
            if (Integer.valueOf(hasPhoneNumber) == 1) {
                // The contact has a phone number
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

            Log.i(getString(R.string.TAG),
                    "SELECTED CONTACT : " +
                            lookupKey + "\n" +
                            name + "\n" +
                            photoUri + "\n" +
                            number

            );
            contactName.getEditText().setText(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            );
            contactName.setError("");
        }
    }

    private void requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CONTACTS) != PERMISSION_GRANTED) {
            // Permission not yet granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Contacts access needed");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage("please confirm Contacts access");//TODO put real question
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(
                                new String[]
                                        {Manifest.permission.READ_CONTACTS}
                                , PERMISSION_REQUEST_CONTACT);
                    }
                });
                builder.show();
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSION_REQUEST_CONTACT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}