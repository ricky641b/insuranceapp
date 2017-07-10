package insurance.abhi.abhiinsuranceapp.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rick on 08-07-2017.
 */

public class RcdAmount {
    public String id;
    public String postId;
    public long receivedAmount;
    public Date createdDate;
    public Date startDate;
    public Date endDate;
    public long balanceAmount;

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


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(long balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getSimplifiedDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(createdDate);
    }
    public String getSimplifiedStartDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy", Locale.getDefault());
        return dateFormat.format(startDate);
    }
    public String getSimplifiedEndDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMM, yyyy", Locale.getDefault());
        return dateFormat.format(endDate);
    }


}
