package insurance.abhi.abhiinsuranceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import insurance.abhi.abhiinsuranceapp.adapters.PostsAdapter;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.helpers.RecyclerItemClickListener;
import insurance.abhi.abhiinsuranceapp.models.Post;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{


    private Menu menu;
    List<Post> searchList = new ArrayList<>();

    private static final Comparator<Post> ALPHABETICAL_COMPARATOR = new Comparator<Post>() {
        @Override
        public int compare(Post a, Post b) {
            return a.getPartyName().compareTo(b.getPartyName());
        }
    };
    SearchView searchView;
    @BindView(R.id.postsRecyclerView)
    RecyclerView mPostsRecyclerView;

    @BindView(R.id.empty_view)
    TextView emptyView;


    List<Post> mPosts = new ArrayList<>();
    PostsAdapter mPostsAdapter;
    DBHelper databaseHelper;
    boolean showCompleted = false;
    private static int REQUEST_CODE = 1;
    private static int DETAIL_ACTIVITY_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic

        if (query.length() > 0) {

            searchList = filter(mPosts, query);;
            mPostsAdapter.setList(searchList);
        }
        else
        {
            searchView.setIconified(true);
            searchList.clear();
            mPostsAdapter.setList(mPosts);

        }
       // mPostsAdapter.replaceAll(filteredModelList);
       // mBinding.recyclerView.scrollToPosition(0);
        return true;

    }

    private static List<Post> filter(List<Post> posts, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Post> filteredModelList = new ArrayList<>();
        for (Post post: posts) {
            final String partyName = post.getPartyName();
            if (partyName.toLowerCase().contains(lowerCaseQuery)) {
                filteredModelList.add(post);
            }
        }
        return filteredModelList;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
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
        else if (id == R.id.action_status)
        {
            showCompleted = !showCompleted;
            refreshList();
            updateMenuTitle();
        }
        else if (id == R.id.action_support)
        {
            launchSupportActivity();
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
    void launchSupportActivity()
    {
        Intent newIntent = new Intent(MainActivity.this,SupportActivity.class);
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
        mPosts = databaseHelper.getAllPosts(showCompleted);
        if (mPosts.isEmpty())
        {
            emptyView.setVisibility(View.VISIBLE);
            mPostsRecyclerView.setVisibility(View.GONE);
        }
        else
        {
            emptyView.setVisibility(View.GONE);
            mPostsRecyclerView.setVisibility(View.VISIBLE);
        }
        mPostsAdapter.setList(mPosts);
    }
    void initRecyclerView()
    {
        mPostsAdapter = new PostsAdapter(mPosts,getApplicationContext());
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
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchList.clear();
            mPostsAdapter.setList(mPosts);
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();

    }
    void updateMenuTitle()
    {
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_status);
            if (showCompleted) {
                menuItem.setTitle("Show Pending");
            } else {
                menuItem.setTitle("Show History");
            }
        }
    }
}
