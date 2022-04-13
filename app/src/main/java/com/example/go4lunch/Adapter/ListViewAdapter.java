package com.example.go4lunch.Adapter;

import static com.example.go4lunch.BuildConfig.MAPS_API_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.GooglePlaces;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListViewAdapter extends ListAdapter<GooglePlaces.Results, ListViewAdapter.ViewHolder> {

    private final Context context;
    ArrayList<GooglePlaces.Results> googlePlacesList;
    private final LatLng myPosition;

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

    public ListViewAdapter(Context context, ArrayList<GooglePlaces.Results> googlePlacesArrayList, LatLng myPosition) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.googlePlacesList = googlePlacesArrayList;
        this.myPosition = myPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GooglePlaces.Results googlePlaces = googlePlacesList.get(position);

        holder.textview_name.setText(googlePlaces.getName()); // name

        if (googlePlaces.getOpening_hours() != null) { // opened or close
            if (googlePlaces.getOpening_hours().getOpen_now()) {
                holder.textview_closing_hour.setText(R.string.opened);
            } else {
                holder.textview_closing_hour.setText(R.string.closed);
            }
        } else {
            holder.textview_closing_hour.setText("N/C");
        }

        float[] results = new float[1];
        Location.distanceBetween(myPosition.latitude, myPosition.longitude, googlePlaces.getGeometry().getLocation().getLat(), googlePlaces.getGeometry().getLocation().getLng(), results);
        float distance = results[0];
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(new DecimalFormat("#").format(distance))
                     .append(context.getResources().getString(R.string.mesuremetre));
        holder.textview_distance.setText(stringBuilder); //distance

        holder.textview_type_and_address.setText(googlePlaces.getVicinity()); // address

        if (googlePlaces.getRating() != null) { //rating and conversion in stars

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.GONE);
            Double rating = googlePlaces.getRating();
            //holder.textview_rating.setText(String.valueOf(rating));
            holder.textview_rating.setVisibility(View.GONE); //hiding note textview
            if (rating >= 2) {
                holder.star1.setVisibility(View.VISIBLE);
            }
            if (rating >= 3) {
                holder.star2.setVisibility(View.VISIBLE);
            }
            if (rating >= 4) {
                holder.star3.setVisibility(View.VISIBLE);
            }

        } else {
            holder.textview_rating.setVisibility(View.VISIBLE);
            holder.textview_rating.setText(R.string.unknown_rating); // unknown note
            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.GONE);
        }
        if (googlePlaces.getPhotos() != null) { // first available photo
            if (googlePlaces.getPhotos().size() > 0) {
                Glide.with(context)
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + googlePlaces.getPhotos().get(0).getPhoto_reference() + "&key=" + MAPS_API_KEY)
                        .apply(RequestOptions.centerInsideTransform())
                        .into((holder.imageView));
            }
        }


    }

    @Override
    public int getItemCount() {
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
        private final ImageView star1;
        private final ImageView star2;
        private final ImageView star3;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            textview_name = itemView.findViewById(R.id.textview_name);
            textview_distance = itemView.findViewById(R.id.textview_distance);
            textview_type_and_address = itemView.findViewById(R.id.textview_type_and_adress);
            textview_number_of_workmates = itemView.findViewById(R.id.textview_number_of_workmates);
            textview_closing_hour = itemView.findViewById(R.id.textview_closing_hour);
            textview_rating = itemView.findViewById(R.id.textview_rating);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);

        }
    }
}
