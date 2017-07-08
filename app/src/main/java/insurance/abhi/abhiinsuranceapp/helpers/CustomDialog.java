package insurance.abhi.abhiinsuranceapp.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import insurance.abhi.abhiinsuranceapp.R;
import insurance.abhi.abhiinsuranceapp.helperDB.DBHelper;
import insurance.abhi.abhiinsuranceapp.models.Post;

/**
 * Created by rick on 08-07-2017.
 */

public class CustomDialog extends Dialog {

    @BindView(R.id.amountEditText)
    EditText amountText;

    @BindView(R.id.add_button)
    Button addButton;

    @BindView(R.id.cancel_button)
    Button cancelButton;

    DBHelper databaseHelper;
    public Activity activity;
    Post mPost;

    public CustomDialog(Activity activity, DBHelper dbHelper, Post post) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        databaseHelper = dbHelper;
        mPost = post;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);

        ButterKnife.bind(this);

    }

    @OnClick(R.id.add_button) void addClicked()
    {
            if (!amountText.getText().toString().isEmpty())
            {
                long amount = Long.valueOf(amountText.getText().toString());
                databaseHelper.addRecdAmount(amount,mPost.id);
                dismiss();
            }
    }
    @OnClick(R.id.cancel_button) void cancelClicked()
    {
        dismiss();
    }

}
