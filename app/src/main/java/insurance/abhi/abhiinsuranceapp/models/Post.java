package insurance.abhi.abhiinsuranceapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rick on 07-07-2017.
 */

public class Post {
    public String id;
    public String partyName;
    public long totalAmount;
    public float interest;
    public int time;
    public Date created_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getSimplifiedDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(created_date);
    }
}