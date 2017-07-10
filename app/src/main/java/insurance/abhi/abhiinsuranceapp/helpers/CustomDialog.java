package insurance.abhi.abhiinsuranceapp.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import insurance.abhi.abhiinsuranceapp.R;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.models.Post;
import insurance.abhi.abhiinsuranceapp.models.RcdAmount;

/**
 * Created by rick on 08-07-2017.
 */

public class CustomDialog extends Dialog implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.amountEditText)
    EditText amountText;

    @BindView(R.id.add_button)
    Button addButton;

    @BindView(R.id.cancel_button)
    Button cancelButton;

    @BindView(R.id.dateEditText)
    EditText dateEditText;

    Date currentSelectedDate;
    DBHelper databaseHelper;
    public Activity activity;
    FragmentManager fgMananger;
    Post mPost;
    long totalAmountReceived = 0;

    public CustomDialog(Activity activity, DBHelper dbHelper, Post post, FragmentManager fg,long totalAmountReceived) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        databaseHelper = dbHelper;
        mPost = post;
        fgMananger = fg;
        this.totalAmountReceived = totalAmountReceived;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);

        ButterKnife.bind(this);

        currentSelectedDate = new Date();
        dateEditText.setText(Constants.getDateFormatString(currentSelectedDate));

    }

    @OnClick(R.id.changeDateButton) void changeDate()
    {
        showDatePicker();
    }

    @OnClick(R.id.add_button) void addClicked()
    {
            if (!amountText.getText().toString().isEmpty())
            {
                RcdAmount rcdAmount = new RcdAmount();
                rcdAmount.postId = mPost.id;
                rcdAmount.receivedAmount =  Long.valueOf(amountText.getText().toString());
                rcdAmount.startDate = currentSelectedDate;
                rcdAmount.endDate = new Date(currentSelectedDate.getMonth() < 12 ? currentSelectedDate.getYear() : currentSelectedDate.getYear() + 1,currentSelectedDate.getMonth() + 1,currentSelectedDate.getDate());
                rcdAmount.balanceAmount = mPost.getAmountTopay() - totalAmountReceived - Long.valueOf(amountText.getText().toString());
                databaseHelper.addRecdAmount(rcdAmount);
                dismiss();
            }
    }
    @OnClick(R.id.cancel_button) void cancelClicked()
    {
        dismiss();
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        currentSelectedDate = calendar.getTime();
        String dateStr = Constants.getDateFormatString(currentSelectedDate);
        dateEditText.setText(dateStr);
    }
    void showDatePicker()
    {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                CustomDialog.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(fgMananger,"DateDialogNew");
    }

}
