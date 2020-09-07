package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangjiayou.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FrameFragment extends Fragment {
    private int numPages = 2;
    private Set<String> historyIds = new HashSet<>();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private static final Integer TYPE_SETTING_ACTIVITY = 0;
    private static ArrayList<String> availableList;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_SETTING_ACTIVITY) {
            availableList = data.getStringArrayListExtra("available");
            Toast.makeText(getActivity(), availableList.toString(), Toast.LENGTH_SHORT);
            System.out.println(availableList);


            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                TextView tabView = new TextView(getActivity());

                tabView.setText(availableList.get(position));
                if (position == tabLayout.getSelectedTabPosition())
                    tabView.setTextSize(20);
                else
                    tabView.setTextSize(14);
                tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);

                tab.setCustomView(tabView);
            }).attach();
            viewPager2.setAdapter(new FragmentStateAdapter(this.getActivity()) {
                @NonNull
                @Override
                public Fragment createFragment(int position) {
                    return new HomeFragment(availableList.get(position), historyIds);
                }

                @Override
                public long getItemId(int position) {
                    return super.getItemId(position);
                }

                @Override
                public boolean containsItem(long itemId) {
                    return super.containsItem(itemId);
                }

                @Override
                public int getItemCount() {
                    return availableList.size();
                }
            });
            viewPager2.getAdapter().notifyDataSetChanged();

        }
    }

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
        availableList = new ArrayList<>();
        availableList.add("News");
        availableList.add("Paper");

        View root = inflater.inflate(R.layout.fragment_frame, container, false);
        viewPager2 = root.findViewById(R.id.home_view_pager);
        tabLayout = root.findViewById(R.id.tabLayout);
        floatingActionButton = root.findViewById(R.id.edit_button);

        viewPager2.setAdapter(new FragmentStateAdapter(this.getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return new HomeFragment(availableList.get(position), historyIds);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public boolean containsItem(long itemId) {
                return super.containsItem(itemId);
            }

            @Override
            public int getItemCount() {
                return availableList.size();
            }
        });

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                int tabCount = tabLayout.getTabCount();
//                for (int i = 0; i < tabCount; i++) {
//                    TabLayout.Tab tab = tabLayout.getTabAt(i);
//                    TextView tabView = (TextView) tab.getCustomView();
//
//                    if (tab.getPosition() == position) {
//                        tabView.setTextSize(20);
//                        tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
//                        tabView.setTypeface(Typeface.DEFAULT_BOLD);
//                    } else {
//                        tabView.setTextSize(14);
//                        tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
//                        tabView.setTypeface(Typeface.DEFAULT);
//                    }
//                }
//            }
//        });

        int activeColor = Color.parseColor("#ff678f");
        int normalColor = Color.parseColor("#666666");
        int[] colors = new int[]{activeColor, normalColor};

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            TextView tabView = new TextView(getActivity());

            tabView.setText(availableList.get(position));
            if (position == tabLayout.getSelectedTabPosition())
                tabView.setTextSize(20);
            else
                tabView.setTextSize(14);
            tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);

            tab.setCustomView(tabView);
        }).attach();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), TypeSettingActivity.class);
                intent.putExtra("available", availableList);
                startActivityForResult(intent, TYPE_SETTING_ACTIVITY);
            }
        });

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
