package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangjiayou.R;

import java.util.HashSet;
import java.util.Set;

public class FrameFragment extends Fragment {
    private static final int NUM_PAGES = 2;
    private Set<String> historyId = new HashSet<>();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private FragmentStateAdapter pagerAdapter;

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0)
                return new HomeFragment("news", historyId);
            else if (position == 1)
                return new HomeFragment("paper", historyId);
            else
                throw new UnsupportedPassageType();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(this.getActivity(), "boss destroy!!", Toast.LENGTH_SHORT).show();
        getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .edit()
                .putStringSet(String.valueOf(R.string.history_fileid_set_key), historyId)
                .apply();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(this.getActivity(), "boss create!", Toast.LENGTH_SHORT).show();

        Set<String> list = getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        if (list != null) historyId = new HashSet<>(list);

        View root = inflater.inflate(R.layout.fragment_frame, container, false);
        viewPager2 = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tabLayout);
        //TODO:really?
        pagerAdapter = new ScreenSlidePagerAdapter(this.getActivity());

        viewPager2.setAdapter(pagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
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
        final String[] names = new String[]{"News", "Events"};
        int[] colors = new int[]{activeColor, normalColor};
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            TextView tabView = new TextView(getActivity());

            tabView.setText(names[position]);
            tabView.setTextSize(14);

            tab.setCustomView(tabView);
        }).attach();


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
