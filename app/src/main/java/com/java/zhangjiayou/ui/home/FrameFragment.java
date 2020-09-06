package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangjiayou.R;

import java.util.HashSet;
import java.util.Set;

public class FrameFragment extends Fragment {
    private static final int NUM_PAGES = 2;
    private Set<String> historyIds = new HashSet<>();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .edit()
                .putStringSet(String.valueOf(R.string.history_fileid_set_key), historyIds)
                .apply();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Set<String> list = getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        if (list != null) historyIds = new HashSet<>(list);

        View root = inflater.inflate(R.layout.fragment_frame, container, false);
        viewPager2 = root.findViewById(R.id.home_view_pager);
        tabLayout = root.findViewById(R.id.tabLayout);
        floatingActionButton = root.findViewById(R.id.edit_button);

        //TODO:really?

        viewPager2.setAdapter(new FragmentStateAdapter(this.getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return new HomeFragment("news", historyIds);
                else if (position == 1)
                    return new HomeFragment("paper", historyIds);
                else
                    throw new UnsupportedPassageType();
            }

            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int tabCount = tabLayout.getTabCount();
                for (int i = 0; i < tabCount; i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    TextView tabView = (TextView) tab.getCustomView();

                    if (tab.getPosition() == position) {
                        tabView.setTextSize(20);
                        tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
                        tabView.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        tabView.setTextSize(14);
                        tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
                        tabView.setTypeface(Typeface.DEFAULT);
                    }
                }
            }
        });
//to be modified
        int activeColor = Color.parseColor("#ff678f");
        int normalColor = Color.parseColor("#666666");
        final String[] names = new String[]{"News", "Paper"};
        int[] colors = new int[]{activeColor, normalColor};
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            TextView tabView = new TextView(getActivity());

            tabView.setText(names[position]);
            tabView.setTextSize(14);


            tab.setCustomView(tabView);
        }).attach();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),TypeSettingActivity.class);
                startActivity(intent);
            }
        });
        floatingActionButton.setExpanded(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

}
