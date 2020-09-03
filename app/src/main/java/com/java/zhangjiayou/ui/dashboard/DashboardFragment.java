package com.java.zhangjiayou.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.SearchableActivity;

public class DashboardFragment extends Fragment {
    private SearchView searchView;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        searchView = root.findViewById(R.id.search_box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(),"Searching...",Toast.LENGTH_SHORT).show();
                Intent params = new Intent(getActivity(), SearchableActivity.class);
                params.putExtra("query",searchView.getQuery());
                startActivity(params);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getContext(),"changed",Toast.LENGTH_SHORT);
                return false;
            }
        });


        return root;
    }
}