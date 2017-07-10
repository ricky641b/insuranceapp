package insurance.abhi.abhiinsuranceapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import insurance.abhi.abhiinsuranceapp.R;
import insurance.abhi.abhiinsuranceapp.models.Post;

/**
 * Created by rick on 07-07-2017.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Post> postsList;
    Context mContext;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView partyName;
        public TextView loanDetails;
        public TextView createdDate;
        public RelativeLayout statusView;
        public ViewHolder(View view) {
            super(view);
            partyName = (TextView) view.findViewById(R.id.partyName);
            loanDetails = (TextView) view.findViewById(R.id.amount);
            createdDate = (TextView) view.findViewById(R.id.date);
            statusView = (RelativeLayout)view.findViewById(R.id.status);
        }
    }


    public PostsAdapter(List<Post> postsList,Context ctx) {
        this.postsList = postsList;
        mContext = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.partyName.setText("Party Name: " + post.getPartyName());
        holder.loanDetails.setText("Loan Amount: ₹" + post.getTotalAmount()
                + "\nInterest: " + post.getInterest() + "%"
                + "\nInterest Amount: ₹" + (post.getAmountTopay() - post.getTotalAmount())
                + "\nTime: " + post.getTime() + " months"
                + "\nTotal amount to pay: ₹" + post.getAmountTopay()
        );
        holder.createdDate.setText("Start Date: " + post.getSimplifiedOnDate(post.getDateOn()) +
                "\nEnd Date: " + post.getSimplifiedOnDate(post.endDate)
        );
        Log.d("STATUS",post.loanStatus + "");
        if (post.loanStatus == 1)
        {
            holder.statusView.setBackgroundColor(mContext.getResources().getColor(R.color.paidColor));
        }
        else
        {
            holder.statusView.setBackgroundColor(mContext.getResources().getColor(R.color.pendingColor));
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public void setList(List<Post> posts)
    {
        postsList = posts;
        notifyDataSetChanged();
    }
}
