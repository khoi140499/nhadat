package com.example.nhadat_app.ui.noti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhadat_app.R;
import com.example.nhadat_app.databinding.FragmentNotifiBinding;

public class Notifi extends Fragment {
    private FragmentNotifiBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentNotifiBinding.inflate(inflater, container, false);
        View root=binding.getRoot();

        return root;
    }
}