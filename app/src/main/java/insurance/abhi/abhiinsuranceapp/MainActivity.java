package insurance.abhi.abhiinsuranceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import insurance.abhi.abhiinsuranceapp.adapters.PostsAdapter;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.helpers.RecyclerItemClickListener;
import insurance.abhi.abhiinsuranceapp.models.Post;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.postsRecyclerView)
    RecyclerView mPostsRecyclerView;
    List<Post> mPosts = new ArrayList<>();
    PostsAdapter mPostsAdapter;
    DBHelper databaseHelper;
    private static int REQUEST_CODE = 1;
    private static int DETAIL_ACTIVITY_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNewEntryActivity();
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initRecyclerView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_backup) {
            launchBackupActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    void launchNewEntryActivity()
    {
        Intent newIntent = new Intent(MainActivity.this,NewEntryActivity.class);
        startActivityForResult(newIntent,REQUEST_CODE);
    }
    void launchBackupActivity()
    {
        Intent newIntent = new Intent(MainActivity.this,BackupActivity.class);
        startActivityForResult(newIntent,REQUEST_CODE);
    }
    void initDB()
    {
        // Get singleton instance of database
        databaseHelper = DBHelper.getInstance(this);
        // Get all posts from database
        refreshList();
    }
    void refreshList()
    {
        mPosts = databaseHelper.getAllPosts();
        mPostsAdapter.setList(mPosts);
    }
    void initRecyclerView()
    {
        mPostsAdapter = new PostsAdapter(mPosts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPostsRecyclerView.setLayoutManager(mLayoutManager);
        mPostsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPostsRecyclerView.setAdapter(mPostsAdapter);
        initDB();
        mPostsRecyclerView.addOnItemTouchListener((new RecyclerItemClickListener(MainActivity.this, mPostsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Post post = mPosts.get(position);
                openDetailActivity(post.getId());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Post post = mPosts.get(position);
                deleteEntryConfirmation(post.id);
            }
        })));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CODE","CODE " + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == 1)
        {
            initDB();
        }
    }
    void openDetailActivity(String id)
    {
        Intent detailIntent = new Intent(MainActivity.this,DetailActivity.class);
        detailIntent.putExtra("id",id);
        startActivityForResult(detailIntent,DETAIL_ACTIVITY_REQUEST_CODE);
    }
    void deleteEntryConfirmation(final String postId)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteMainEntry(postId);
                        refreshList();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
