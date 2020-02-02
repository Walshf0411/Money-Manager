package in.slwsolutions.moneymanager.ui.dues;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import in.slwsolutions.moneymanager.R;

public class DueDetailActivity extends AppCompatActivity {

    private TextView name, number;
    private ImageView profileImage;
    private RecyclerView recyclerView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_detail);
        intent = getIntent();

        initializeComponents();
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

//        recyclerView.setAdapter();
    }
}
