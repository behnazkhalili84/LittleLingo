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
import androidx.navigation.fragment.NavHostFragment;

import com.example.littlelingo.R;
import com.example.littlelingo.SignInActivity;
import com.example.littlelingo.databinding.FragmentHomeBinding;
import com.example.littlelingo.ui.SharedViewModel;
import com.example.littlelingo.ui.user.AuthViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AuthViewModel authViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Access SharedViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Observe data from SharedViewModel
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
           // Toast.makeText(getContext(), "Name: " + name, Toast.LENGTH_SHORT).show();
            homeViewModel.setText("Welcome, " + user.getName());
        });

//        sharedViewModel.getUserID().observe(getViewLifecycleOwner(), userID -> {
//            // add code here
//           // Toast.makeText(getContext(), "UserID: " + userID, Toast.LENGTH_SHORT).show();
//        });

        // Navigation
        binding.grammarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_grammarLearning);
            }
        });

        binding.vocabularyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_vocabularylearning);
            }
        });

        binding.vocabularyQuizLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.nav_vocabulayquiz);
            }
        });

//        binding.grammarQuizLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_dashboardFragment_to_grammarQuizFragment);
//            }
//        });

//        binding.awardLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_dashboardFragment_to_videoFragment);
//            }
//        });

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