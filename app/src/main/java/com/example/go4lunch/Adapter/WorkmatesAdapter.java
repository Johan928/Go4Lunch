package com.example.go4lunch.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.go4lunch.details.DetailsActivity;
import com.example.go4lunch.user.User;

import java.util.List;

public class WorkmatesAdapter extends ListAdapter<User, WorkmatesAdapter.ViewHolder> {

    private static final String TAG = "987";
    private final Context context;
    private final List<User> userList;



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

    public WorkmatesAdapter(Context context, List<User> userList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);


        if (user.getSelectedRestaurantPlaceId() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(user.getUsername()).append(" is eating at ") .append(user.getSelectedRestaurantName());
            holder.textview_name.setText(stringBuilder);
            holder.textview_name.setTextColor(context.getResources().getColor(R.color.black));
            holder.textview_name.setTag(user.getSelectedRestaurantPlaceId());


        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(user.getUsername()).append(" hasn't decided yet.");
            holder.textview_name.setText(stringBuilder);
            holder.textview_name.setTextColor(context.getResources().getColor(R.color.grey));
        }


        if (user.getUrlPicture() != null) {
            String url = user.getUrlPicture();
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
        }

        holder.textview_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    String placeId = v.getTag().toString();
                    intent.putExtra("placeId", placeId);
                    context.startActivity(intent);
                }

            }
        });


    }

        @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textview_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_avatar);
            textview_name = itemView.findViewById(R.id.textview_isjoining);

        }

    }
}
