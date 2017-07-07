package insurance.abhi.abhiinsuranceapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.models.Post;

public class DetailActivity extends AppCompatActivity {


    String idFromIntent;
    DBHelper databaseHelper;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idFromIntent = getIntent().getStringExtra("id");
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            databaseHelper = DBHelper.getInstance(DetailActivity.this);
            post = databaseHelper.getPost(idFromIntent);

        }
    });


    }

}
