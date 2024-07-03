package com.example.littlelingo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.littlelingo.SignInActivity;
import com.example.littlelingo.databinding.FragmentHomeBinding;
import com.example.littlelingo.ui.SharedViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Access SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe data from SharedViewModel
        sharedViewModel.getName().observe(getViewLifecycleOwner(), name -> {
           // Toast.makeText(getContext(), "Name: " + name, Toast.LENGTH_SHORT).show();
            homeViewModel.setText("Welcome, " + name);
        });

        sharedViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
            // add code here
           // Toast.makeText(getContext(), "UserID: " + userID, Toast.LENGTH_SHORT).show();
        });

        // Get arguments safely
//        if (getArguments() != null) {
//            String name = getArguments().getString("name");
//            String userID = getArguments().getString("userID");
//            if (name != null) {
//              //  Toast.makeText(getContext(), "Home get args: " + message+" "+message2, Toast.LENGTH_SHORT).show();
//                homeViewModel.setText(name+" "+userID);
//            }
//        }
//        else{
//            Toast.makeText(getContext(),"Home Intent: failllll ", Toast.LENGTH_SHORT).show();
//
//        }

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}