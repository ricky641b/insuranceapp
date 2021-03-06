package insurance.abhi.abhiinsuranceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.helpers.Constants;
import insurance.abhi.abhiinsuranceapp.models.Post;

public class NewEntryActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{

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
    @BindView(R.id.dateEditText)
    EditText dateEditText;


    Date currentSelectedDate;
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
        currentSelectedDate = new Date();
        dateEditText.setText(Constants.getDateFormatString(currentSelectedDate));
    }

    @OnClick(R.id.changeDateButton)void changeDate()
    {
        showDatePicker();
    }

    @OnClick(R.id.calculateButton) void calculateButton()
    {
        if (validate()) {
            float amount = Long.valueOf(amountText.getText().toString());
            float interest = Float.valueOf(interestText.getText().toString());
            int month = Integer.valueOf(monthText.getText().toString());

            double interestAmount = amount * (interest / 100.0) * (month);

            double totalAmount = interestAmount + amount;

            totalAmountLabel.setText("Total Amount " + String.valueOf(totalAmount));
            interestLabel.setText("Interest Amount " + String.valueOf(interestAmount));
        }

    }
    @OnClick(R.id.addButton) void addNewClicked()
    {
        if (validate())
        {

            Calendar cal = Calendar.getInstance();
            cal.setTime(currentSelectedDate);
            cal.add(Calendar.MONTH,Integer.valueOf(monthText.getText().toString()));
            float amount = Long.valueOf(amountText.getText().toString());
            float interest = Float.valueOf(interestText.getText().toString());
            int month = Integer.valueOf(monthText.getText().toString());

            double interestAmount = amount * (interest / 100.0) * (month);

            double totalAmount = interestAmount + amount;
            Post post = new Post();
            post.partyName = partyNameText.getText().toString();
            post.totalAmount = (long)amount;
            post.interest = interest;
            post.amountTopay = (long)totalAmount;
            post.dateOn = currentSelectedDate;
            post.endDate = cal.getTime();
            post.time = Integer.valueOf(monthText.getText().toString());
            addNewEntry(post);
        }
    }
    void showDatePicker()
    {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                NewEntryActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(getFragmentManager(),"DateDialogNew");
    }


    void addNewEntry(Post post)
    {
        databaseHelper.addOrUpdatePost(post);
        setResult(REQUEST_CODE);
        finish();
    }
    boolean validate()
    {
        if (partyNameText.getText().toString().isEmpty() || amountText.toString().isEmpty() || interestText.getText().toString().isEmpty()
                || monthText.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Some fields are empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        currentSelectedDate = calendar.getTime();
        String dateStr = Constants.getDateFormatString(currentSelectedDate);
        dateEditText.setText(dateStr);
    }
}
