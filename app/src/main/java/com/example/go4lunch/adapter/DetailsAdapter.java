package com.example.go4lunch.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.user.User;

import java.util.List;

public class DetailsAdapter extends ListAdapter<User, DetailsAdapter.DetailsViewHolder> {

    private final Context context;
    private final List<User> workmatesList;

    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {


        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUsername().equals(newItem.getUsername()) && oldItem.getUid().equals(newItem.getUid());
        }
    };

    public DetailsAdapter(Context context, List<User> workmatesList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.workmatesList = workmatesList;
    }

    @NonNull
    @Override
    public DetailsAdapter.DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new DetailsAdapter.DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        User user = workmatesList.get(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(user.getUsername())
                     .append(holder.itemView.getContext().getString(R.string.is_joining));
        holder.textView.setText(stringBuilder);

        if (user.getUrlPicture() != null) {
            Glide.with(context)
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return workmatesList.size();
    }

    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_avatar);
            textView = itemView.findViewById(R.id.textview_isjoining);
        }
    }

}


