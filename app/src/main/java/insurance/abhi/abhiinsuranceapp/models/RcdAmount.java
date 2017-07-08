package insurance.abhi.abhiinsuranceapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rick on 08-07-2017.
 */

public class RcdAmount {
    public String id;
    public long receivedAmount;
    public Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(long receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public String getSimplifiedDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(createdDate);
    }
}
