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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private Set<String> historyIds = new HashSet<>();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private FloatingActionButton floatingActionButton;
    private static final Integer TYPE_SETTING_ACTIVITY = 0;
    private static ArrayList<String> availableList;

    /**
     * Get result from TypeSettingActivity & rearrange tabs
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_SETTING_ACTIVITY) {
            availableList = data.getStringArrayListExtra("available");

            // Reset content and styles of tabs according to intent returned
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

            // Set up fragments inside ViewPager
            viewPager2.setAdapter(new FragmentStateAdapter(this.getActivity()) {
                @NonNull
                @Override
                public Fragment createFragment(int position) {
                    return HomeFragment.newInstance(availableList.get(position), historyIds);
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
                return HomeFragment.newInstance(availableList.get(position), historyIds);
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

        int activeColor = Color.parseColor("#ff678f");
        int normalColor = Color.parseColor("#666666");
        int[] colors = new int[]{activeColor, normalColor};

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
                        tabView.setTextColor(colors[0]);
                    } else {
                        tabView.setTextSize(14);
                        tabView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
                        tabView.setTypeface(Typeface.DEFAULT);
                        tabView.setTextColor(colors[1]);
                    }
                }
            }
        });
        viewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            private static final float MIN_SCALE = 0.95f;
            private static final float MIN_ALPHA = 0.85f;

            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();

                if (position < -1) {
                    view.setAlpha(0f);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else {
                    view.setAlpha(0f);
                }
            }
        });

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
