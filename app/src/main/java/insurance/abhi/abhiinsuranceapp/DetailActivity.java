package insurance.abhi.abhiinsuranceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import insurance.abhi.abhiinsuranceapp.adapters.AmountAdapter;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.helpers.CustomDialog;
import insurance.abhi.abhiinsuranceapp.helpers.RecyclerItemClickListener;
import insurance.abhi.abhiinsuranceapp.models.Post;
import insurance.abhi.abhiinsuranceapp.models.RcdAmount;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.dateLabel)
    TextView createdDate;

    @BindView(R.id.partyNamelabel)
    TextView partyName;

    @BindView(R.id.loanDetailsLabel)
    TextView loanDetails;

    @BindView(R.id.loanDetailsRecyclerView)
    RecyclerView loanDetailsRecyclerView;

    @BindView(R.id.recyclerViewColumnLabel)
    LinearLayout linearLayout;

    @BindView(R.id.rcdAmountLabel)
    TextView rcdAmountLabel;

    String idFromIntent;
    DBHelper databaseHelper;
    Post post;
    List<RcdAmount> amountsList = new ArrayList<RcdAmount>();
    AmountAdapter amountsAdapter;

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
                showDialog();
            }
        });
        if (getSupportActionBar()!=null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        idFromIntent = getIntent().getStringExtra("id");
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
            databaseHelper = DBHelper.getInstance(DetailActivity.this);
            post = databaseHelper.getPost(idFromIntent);
            displayDetails(post);
        }
    });
    }

    void displayDetails(Post post)
    {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(post.partyName);
        }
        partyName.setText("Party Name: " + post.getPartyName());
        loanDetails.setText("Loan Amount: ₹" + post.getTotalAmount()
                + "\nInterest: " + post.getInterest()
                + "%" + "\nTime: " + post.getTime() + " months"
                + "\nAmount to be paid: ₹" + post.getAmountTopay()
        );
        createdDate.setText("Date: " + post.getSimplifiedDate());
        initRecyclerView();
    }

    void showList()
    {
        long totalRecdAmount = 0;
        amountsList = databaseHelper.getAllRecdAmount(post.id);
        if (amountsList.size()==0)
        {
            linearLayout.setVisibility(View.GONE);
        }
        else
        {
            for (int i=0;i<amountsList.size();i++)
            {
                RcdAmount rcdAmount = amountsList.get(i);
                totalRecdAmount += rcdAmount.getReceivedAmount();
            }
            linearLayout.setVisibility(View.VISIBLE);
        }
        rcdAmountLabel.setText("Total Received Amount: ₹" + totalRecdAmount);
        amountsAdapter.setList(amountsList);

    }
    void initRecyclerView() {
        amountsAdapter = new AmountAdapter(amountsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        loanDetailsRecyclerView.setLayoutManager(mLayoutManager);
        loanDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loanDetailsRecyclerView.setAdapter(amountsAdapter);
        loanDetailsRecyclerView.addOnItemTouchListener((new RecyclerItemClickListener(DetailActivity.this, loanDetailsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                RcdAmount rcdAmount = amountsList.get(position);
                deleteEntryConfirmation(rcdAmount.getId());
            }
        })));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(loanDetailsRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        loanDetailsRecyclerView.addItemDecoration(dividerItemDecoration);
        showList();
    }
    void showDialog()
    {
        CustomDialog customDialog = new CustomDialog(this,databaseHelper,post);
        customDialog.show();
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showList();
            }
        });
    }
    void deleteEntryConfirmation(final String amountId)
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
                        databaseHelper.deleteMainEntry(amountId);
                        showList();
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
