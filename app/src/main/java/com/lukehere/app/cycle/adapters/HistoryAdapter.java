package com.lukehere.app.cycle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.lukehere.app.cycle.R;
import com.lukehere.app.cycle.pojo.History;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HistoryAdapter extends FirestorePagingAdapter<History, HistoryAdapter.HistoryViewHolder> {
    private LayoutInflater mInflater;
    private ListItemClickListener mOnClickListener;
    private SwipeRefreshLayout mPullToRefresh;
    private Context mContext;

    private DateFormat dateFormat;

    public interface ListItemClickListener {
        void onListItemClick(LinearLayout clickedHistoryItemLayout);
    }

    public HistoryAdapter(Context context, SwipeRefreshLayout pullToRefresh, @NonNull FirestorePagingOptions<History> options, ListItemClickListener listener) {
        super(options);
        mPullToRefresh = pullToRefresh;
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mOnClickListener = listener;

        dateFormat = DateFormat.getDateTimeInstance();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_list_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryViewHolder holder, int position, @NonNull History history) {
        holder.historyDetailsLayout.setVisibility(View.GONE);

        holder.cycleNumber.setText(String.valueOf(history.getCycleNumber()));

        try {
            String borrowTime = dateFormat.format(Long.parseLong(history.getBorrowDate()));
            holder.borrowedAt.setText(borrowTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            String returnTime = dateFormat.format(Long.parseLong(history.getReturnDate()));
            holder.returnedAt.setText(returnTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        holder.security.setText(history.getSecurityEmailAddress());
        holder.registrationNumber.setText(history.getRegistrationNumber());
        holder.fulName.setText(history.getFullName());
        holder.phoneNumber.setText(history.getPhoneNumber());
        holder.borrowedFrom.setText(history.getBorrowedFrom());
        holder.returnedTo.setText(history.getReturnedTo());
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
                Toast.makeText(mContext, "Error getting history information", Toast.LENGTH_SHORT).show();
                retry();
                break;
        }
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout historyDetailsLayout;
        TextView cycleNumber;
        TextView borrowedAt;
        TextView returnedAt;
        TextView security;
        TextView registrationNumber;
        TextView fulName;
        TextView phoneNumber;
        TextView borrowedFrom;
        TextView returnedTo;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            historyDetailsLayout = itemView.findViewById(R.id.history_details_layout);

            cycleNumber = itemView.findViewById(R.id.history_cycle_number);
            borrowedAt = itemView.findViewById(R.id.history_borrowed_at);
            returnedAt = itemView.findViewById(R.id.history_returned_at);
            security = itemView.findViewById(R.id.history_security_email);
            registrationNumber = itemView.findViewById(R.id.history_registration_number);
            fulName = itemView.findViewById(R.id.history_full_name);
            phoneNumber = itemView.findViewById(R.id.history_phone_number);
            borrowedFrom = itemView.findViewById(R.id.history_borrowed_from);
            returnedTo = itemView.findViewById(R.id.history_returned_to);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View v) {
            mOnClickListener.onListItemClick(historyDetailsLayout);
        }
    }
}