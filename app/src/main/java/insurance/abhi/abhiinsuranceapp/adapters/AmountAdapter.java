package insurance.abhi.abhiinsuranceapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import insurance.abhi.abhiinsuranceapp.R;
import insurance.abhi.abhiinsuranceapp.models.RcdAmount;

/**
 * Created by rick on 08-07-2017.
 */

public class AmountAdapter extends RecyclerView.Adapter<AmountAdapter.ViewHolder> {

    private List<RcdAmount> mAmountsList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rcdAmount;
        public TextView startDate;
        public TextView endDate;
        public TextView balanceText;

        public ViewHolder(View view) {
            super(view);
            rcdAmount = (TextView) view.findViewById(R.id.amountLabel);
            startDate = (TextView) view.findViewById(R.id.startDateLabel);
            endDate = (TextView) view.findViewById(R.id.endDateLabel);
            balanceText =  (TextView) view.findViewById(R.id.balanceLabel);
        }
    }


    public AmountAdapter(List<RcdAmount> amountsList) {
        mAmountsList = amountsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_amount_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RcdAmount rcdAmount = mAmountsList.get(position);
        holder.startDate.setText(rcdAmount.getSimplifiedStartDate());
        holder.endDate.setText(rcdAmount.getSimplifiedEndDate());
        holder.rcdAmount.setText("₹" + rcdAmount.getReceivedAmount());
        holder.balanceText.setText("₹" + rcdAmount.getBalanceAmount() );
    }

    @Override
    public int getItemCount() {
        return mAmountsList.size();
    }

    public void setList(List<RcdAmount> amounts)
    {
        mAmountsList = amounts;
        notifyDataSetChanged();
    }
}
