package com.example.nhadat_app.ui.message;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhadat_app.R;
import com.example.nhadat_app.databinding.FragmentHomeBinding;
import com.example.nhadat_app.databinding.FragmentMessageBinding;

public class Message extends Fragment {
    private FragmentMessageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentMessageBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        return root;
    }
}