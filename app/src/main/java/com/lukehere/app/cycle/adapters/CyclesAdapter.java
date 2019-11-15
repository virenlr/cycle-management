package com.lukehere.app.cycle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.card.MaterialCardView;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.pojo.Cycle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CyclesAdapter extends FirestorePagingAdapter<Cycle, CyclesAdapter.CyclesViewHolder> {
    private LayoutInflater mInflater;
    private ListItemClickListener mOnClickListener;
    private SwipeRefreshLayout mPullToRefresh;
    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(Cycle clickedCycle);
    }

    public CyclesAdapter(Context context, SwipeRefreshLayout pullToRefresh, @NonNull FirestorePagingOptions<Cycle> options, ListItemClickListener listener) {
        super(options);
        mPullToRefresh = pullToRefresh;
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CyclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cycle_list_item, parent, false);
        return new CyclesViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CyclesViewHolder holder, int position, @NonNull Cycle cycle) {
        holder.cycleName.setText(String.valueOf(cycle.getCycleNumber()));

        if (cycle.getDecommissionedStatus() == 1) {
            holder.cycleCard.setStrokeColor(mContext.getResources().getColor(R.color.cycleStatusBlack));
            holder.cycleStatusIndicator.setCardBackgroundColor(mContext.getResources().getColor(R.color.cycleStatusBlack));
        } else if (cycle.getTyreCondition() == 1 || cycle.getChainCondition() == 1 || cycle.getCableCondition() == 1 || cycle.getBrakeCondition() == 1 || cycle.getLubricationCondition() == 1 || cycle.getMiscellaneousCondition() == 1) {
            holder.cycleCard.setStrokeColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.cycleStatusIndicator.setCardBackgroundColor(mContext.getResources().getColor(R.color.cycleStatusRed));
        } else if (cycle.getStatus() == 1) {
            holder.cycleCard.setStrokeColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.cycleStatusIndicator.setCardBackgroundColor(mContext.getResources().getColor(R.color.cycleStatusAmber));
        } else {
            holder.cycleCard.setStrokeColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.cycleStatusIndicator.setCardBackgroundColor(mContext.getResources().getColor(R.color.cycleStatusGreen));
        }

        if (cycle.getStatus() == 0) {
            holder.location.setVisibility(View.VISIBLE);
            holder.location.setText(cycle.getLocation());
        } else {
            holder.location.setVisibility(View.GONE);
            holder.location.setText("");
        }

        if (cycle.getDecommissionedStatus() == 1) {
            holder.location.setVisibility(View.GONE);
            holder.location.setText("");
        }

        holder.itemView.setTag(cycle);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state) {
            case LOADING_INITIAL:
                mPullToRefresh.setRefreshing(true);
                break;
            case LOADING_MORE:
                mPullToRefresh.setRefreshing(true);
                break;
            case LOADED:
                mPullToRefresh.setRefreshing(false);
                break;
            case FINISHED:
                mPullToRefresh.setRefreshing(false);
                break;
            case ERROR:
                Toast.makeText(mContext, "Error getting cycle information", Toast.LENGTH_SHORT).show();
                retry();
                break;
        }
    }

    class CyclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialCardView cycleStatusIndicator;
        MaterialCardView cycleCard;
        TextView cycleName;
        TextView location;

        CyclesViewHolder(@NonNull View itemView) {
            super(itemView);

            cycleStatusIndicator = itemView.findViewById(R.id.cycle_status_indicator);
            cycleCard = itemView.findViewById(R.id.cycle_card);
            cycleName = itemView.findViewById(R.id.cycle_name_text_view);
            location = itemView.findViewById(R.id.location);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View v) {
            Cycle cycle = (Cycle) v.getTag();
            mOnClickListener.onListItemClick(cycle);
        }
    }
}