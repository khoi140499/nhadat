package com.example.nhadat_app.ui.message;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nhadat_app.Adapter.ListChatUser;
import com.example.nhadat_app.R;
import com.example.nhadat_app.SignIn;
import com.example.nhadat_app.databinding.FragmentHomeBinding;
import com.example.nhadat_app.databinding.FragmentMessageBinding;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Message extends Fragment implements View.OnClickListener {
    private FragmentMessageBinding binding;
    private LinearLayout ln1, ln2;
    private Button btn;
    private RecyclerView re;
    private ListChatUser adapter;
    private TextView txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentMessageBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        ln1=root.findViewById(R.id.linear_message);
        ln2=root.findViewById(R.id.linear_mess);
        btn=root.findViewById(R.id.btn_dangnhapmess);
        re=root.findViewById(R.id.recycle_chatuser);
        txt=root.findViewById(R.id.error);

        checkLogin();
        setListener();
        setViewRecycleView();
        return root;
    }

    private void checkLogin(){
        if(ParseUser.getCurrentUser()==null){
            ln1.setVisibility(View.VISIBLE);
            ln2.setVisibility(View.GONE);
        }
        else{
            ln1.setVisibility(View.GONE);
            ln2.setVisibility(View.VISIBLE);
        }
    }

    private void setViewRecycleView(){
        if(ParseUser.getCurrentUser()!=null){
            ParseQuery<ParseObject> query=ParseQuery.getQuery("Message");
            query.whereEqualTo("user_send", ParseUser.getCurrentUser().getObjectId());
            query.findInBackground((objects, e) -> {
                if(e==null){
                    setListData(objects);
                }
            });
        }
    }

    private void setListData(List<ParseObject> listData){
        HashSet<String> hs=new HashSet<>();
        if(listData.size()==0){
            txt.setVisibility(View.VISIBLE);
            re.setVisibility(View.GONE);
        }
        for(ParseObject as:listData){
            hs.add(as.getString("user_receiver"));
        }

        List<String> list=new ArrayList<>();
        for(String as:hs){
            list.add(as);
        }
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new ListChatUser(getContext(),list);
        re.setAdapter(adapter);
    }

    private void setListener(){
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_dangnhapmess){
            Intent a=new Intent(getContext(), SignIn.class);
            a.putExtra("activity", "message");
            startActivity(a);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}