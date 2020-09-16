package com.java.zhangjiayou.ui.explore;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangjiayou.MainActivity;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.util.NetworkChecker;

public class ExploreFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private static Integer NUM_PAGES = 6;
    final private String[] tabNames = {"中国疫情走势", "世界疫情走势", "世界热力图", "知识图谱", "新闻聚类", "知疫学者"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        if (!NetworkChecker.isNetworkConnected(requireActivity())) {
            root = inflater.inflate(R.layout.fragment_frame_no_network, container, false);
            ((MainActivity) requireActivity()).getSupportActionBar().setTitle("Explore");
            return root;
        }

        root = inflater.inflate(R.layout.fragment_explore, container, false);
        viewPager2 = root.findViewById(R.id.explore_view_pager);
        tabLayout = root.findViewById(R.id.explore_tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager2.setAdapter(new FragmentStateAdapter(this.requireActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return WebViewerFragment.newInstance(position);
            }
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
        });
        viewPager2.setUserInputEnabled(false);
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int tabCount = tabLayout.getTabCount();
                for (int i = 0; i < tabCount; i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    TextView tabView = (TextView) tab.getCustomView();
                    if (tab.getPosition() == position) {
                        tabView.setTextSize(18);
                        tabView.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        tabView.setTextSize(14);
                        tabView.setTypeface(Typeface.DEFAULT);
                    }
                }
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> {
            TextView textView = new TextView(requireActivity());
            textView.setText(tabNames[position]);
            textView.setTextSize(14);
            textView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);

            tab.setCustomView(textView);
        })).attach();

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