package com.example.go4lunch.workmatesview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.Adapter.WorkmatesAdapter;
import com.example.go4lunch.databinding.FragmentWorkmatesBinding;
import com.example.go4lunch.factory.ViewModelFactory;
import com.example.go4lunch.model.GooglePlaces;
import com.example.go4lunch.model.Place;
import com.example.go4lunch.user.User;
import com.example.go4lunch.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;
    private List<GooglePlaces.Results> googlePlaces;
    private final List<User> userList = new ArrayList<>();
    private List<Place> placesList;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }


    private void initRecyclerview() {
        RecyclerView recyclerView = binding.recyclerViewWorkmates;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        WorkmatesAdapter adapter = new WorkmatesAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WorkmatesViewModel workmatesViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(WorkmatesViewModel.class);
        LiveData<WorkmatesViewState> liveData = workmatesViewModel.getWorkmatesViewState();
        liveData.observe(getViewLifecycleOwner(), workmatesViewState -> {
            if (workmatesViewState.getUserList() != null) {
                userList.clear();
                for (User user : workmatesViewState.getUserList()) {
                    if (!user.getUid().equals(UserManager.getInstance().getCurrentUser().getUid())) {

                        userList.add(user);
                    }
                }

                initRecyclerview();
            }
        });
    }
}