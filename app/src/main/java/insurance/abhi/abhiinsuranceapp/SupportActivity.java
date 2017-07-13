package insurance.abhi.abhiinsuranceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.contactUsButton) void contactUs()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getString(R.string.mail), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT,Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL.toUpperCase() + " " + Build.VERSION.SDK_INT + "\nPlease write your feedback below this line\n");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));

    }
}
