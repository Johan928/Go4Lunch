package com.example.go4lunch.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.go4lunch.Adapter.ListViewAdapter;
import com.example.go4lunch.Models.GooglePlaces;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentListViewBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.viewmodels.MapsViewViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    private static final String TAG = MapsViewFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private ArrayList<GooglePlaces.Results> googlePLacesList = new ArrayList<>();
    MapsViewViewModel mapsViewViewModel;
    private ListViewAdapter adapter;
    private FragmentListViewBinding binding;

    public ListViewFragment() {
        // Required empty public constructor
    }

    public static  ListViewFragment newInstance(){return  new ListViewFragment();}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void init() {
progressBar = getView().findViewById(R.id.progress_bar);
        recyclerView =  getView().findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ListViewAdapter(getContext(),googlePLacesList);
        recyclerView.setAdapter(adapter);
        mapsViewViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getContext())).get(MapsViewViewModel.class);


    }

    private void getPlaces(){
        mapsViewViewModel.getNearBySearchLiveData().observe(getActivity(), new Observer<List<GooglePlaces.Results>>() {
            @Override
            public void onChanged(List<GooglePlaces.Results> results) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onChanged: " + results.size());
                googlePLacesList.addAll(results);

adapter.submitList(googlePLacesList);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getPlaces();
    }
}