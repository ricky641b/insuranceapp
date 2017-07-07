package insurance.abhi.abhiinsuranceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.models.Post;

public class NewEntryActivity extends AppCompatActivity {

    @BindView(R.id.partyNameEditText)
    AppCompatEditText partyNameText;
    @BindView(R.id.amountEditText)
    AppCompatEditText amountText;
    @BindView(R.id.interestEditText)
    AppCompatEditText interestText;
    @BindView(R.id.monthEditText)
    AppCompatEditText monthText;
    @BindView(R.id.totalAmountLabel)
    AppCompatTextView totalAmountLabel;
    @BindView(R.id.interestAmountLabel)
    AppCompatTextView interestLabel;
    DBHelper databaseHelper;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                databaseHelper = DBHelper.getInstance(NewEntryActivity.this);
            }
        });

    }

    @OnClick(R.id.calculateButton) void calculateButton()
    {
        float amount =  Float.valueOf(amountText.getText().toString());
        float interest = Float.valueOf(interestText.getText().toString());
        float month = Float.valueOf(monthText.getText().toString());

        double interestAmount = amount * (interest/100.0) * (month/12.0) ;

        double totalAmount = interestAmount + amount;

        totalAmountLabel.setText("Total Amount " + String.valueOf(totalAmount));
        interestLabel.setText("Interest Amount " + String.valueOf(interestAmount));

    }
    @OnClick(R.id.addButton) void addNewClicked()
    {
        Post post = new Post();
        post.partyName = partyNameText.getText().toString();
        post.totalAmount = Long.valueOf(amountText.getText().toString());
        post.interest = Float.valueOf(interestText.getText().toString());
        post.time = Integer.valueOf(monthText.getText().toString());
        addNewEntry(post);
    }
    void addNewEntry(Post post)
    {
        databaseHelper.addOrUpdatePost(post);
        setResult(REQUEST_CODE);
        finish();
    }
}
