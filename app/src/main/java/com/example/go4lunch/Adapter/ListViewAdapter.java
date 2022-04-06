package com.example.go4lunch.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.R;

import java.util.ArrayList;

public class ListViewAdapter extends ListAdapter<GooglePlaces.Results, ListViewAdapter.ViewHolder> {

    private final Context context;
    ArrayList<GooglePlaces.Results> googlePlacesList;

    public static final DiffUtil.ItemCallback<GooglePlaces.Results> DIFF_CALLBACK = new DiffUtil.ItemCallback<GooglePlaces.Results>() {
        @Override
        public boolean areItemsTheSame(@NonNull GooglePlaces.Results oldItem, @NonNull GooglePlaces.Results newItem) {
            return oldItem.getPlace_id().equals(newItem.getPlace_id());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull GooglePlaces.Results oldItem, @NonNull GooglePlaces.Results newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getPlace_id().equals(newItem.getPlace_id());
        }
    };

    public ListViewAdapter(Context context, ArrayList<GooglePlaces.Results> googlePlacesArrayList) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.googlePlacesList = googlePlacesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GooglePlaces.Results googlePlaces = googlePlacesList.get(position);
        holder.textview_name.setText(googlePlaces.getName());
        holder.textview_closing_hour.setText(googlePlaces.getTypes().get(0));

        holder.textview_type_and_address.setText(googlePlaces.getPlace_id());
        if (googlePlaces.getRating() != null){
            holder.textview_rating.setText(googlePlaces.getRating().toString());

        } else {holder.textview_rating.setText(R.string.unknown_rating);}
        Glide.with(context)
                .load(R.drawable.bowl_icon)
                .apply(RequestOptions.circleCropTransform())
                .into((holder.imageView));

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + googlePlacesList.size());
        return googlePlacesList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textview_name;
        private final TextView textview_distance;
        private final TextView textview_type_and_address;
        private final TextView textview_number_of_workmates;
        private final TextView textview_closing_hour;
        private final TextView textview_rating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            textview_name = itemView.findViewById(R.id.textview_name);
            textview_distance = itemView.findViewById(R.id.textview_distance);
            textview_type_and_address = itemView.findViewById(R.id.textview_type_and_adress);
            textview_number_of_workmates = itemView.findViewById(R.id.textview_number_of_workmates);
            textview_closing_hour = itemView.findViewById(R.id.textview_closing_hour);
            textview_rating = itemView.findViewById(R.id.textview_rating);

        }
    }
}
