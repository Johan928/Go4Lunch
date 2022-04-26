package com.example.go4lunch.listview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.Adapter.ListViewAdapter;
import com.example.go4lunch.databinding.FragmentListViewBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

    private static final String TAG = ListViewFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private final ArrayList<GooglePlaces.Results> googlePLacesList = new ArrayList<>();
    private ListViewViewModel listViewViewModel;
    private ListViewAdapter adapter;
    private FragmentListViewBinding binding;
    private LiveData<ListViewViewState> livedata;
    private final UserManager userManager = UserManager.getInstance();
    private List<User> selectedRestaurantList = new ArrayList<>();


    public ListViewFragment() {
        // Required empty public constructor
    }

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {

        progressBar = binding.progressBar;
        recyclerView = binding.recyclerView;
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        ViewModelFactory vm = ViewModelFactory.getInstance();
        listViewViewModel = new ViewModelProvider(this, vm).get(ListViewViewModel.class);

        listViewViewModel.getListViewLiveData().observe(getViewLifecycleOwner(),new Observer<ListViewViewState>() {
            @Override
            public void onChanged(ListViewViewState listViewViewState) {

                LatLng myPosition;
                if (listViewViewState.getPlaces() != null && listViewViewState.getLocation() != null && listViewViewState.getSelectedRestaurantsList() != null) {
                    selectedRestaurantList = listViewViewState.getSelectedRestaurantsList();
                    myPosition = new LatLng(listViewViewState.getLocation().getLatitude(), listViewViewState.getLocation().getLongitude());
                    progressBar.setVisibility(View.GONE);
                    googlePLacesList.clear();
                    googlePLacesList.addAll(listViewViewState.getPlaces());

                        adapter = new ListViewAdapter(ListViewFragment.this.getContext(), googlePLacesList, myPosition, selectedRestaurantList);
                        recyclerView.setAdapter(adapter);

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }
}