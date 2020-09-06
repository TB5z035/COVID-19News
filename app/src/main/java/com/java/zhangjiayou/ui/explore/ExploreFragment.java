package com.java.zhangjiayou.ui.explore;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.java.zhangjiayou.R;
import com.java.zhangjiayou.ui.home.DetailActivity;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private static Integer NUM_PAGES = 4;
    final private String[] tabNames = {"Visual", "Domain", "Cluster", "Person"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        viewPager2 = root.findViewById(R.id.explore_view_pager);
        tabLayout = root.findViewById(R.id.explore_tab_layout);

        viewPager2.setAdapter(new FragmentStateAdapter(this.getActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                //TODO:customize the fragment
                return new ViewerFragment();
            }
            @Override
            public int getItemCount() {
                return NUM_PAGES;
            }
        });
        viewPager2.setUserInputEnabled(false);
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
            TextView textView = new TextView(getActivity());
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

//        final TextView textView = root.findViewById(R.id.text_notifications);
//        exploreViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//
//        final TextView test = root.findViewById(R.id.test_text);
//
//        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<Passage> passageList = PassageDatabase.getInstance(null).getPassageDao().getAllPassages();
//                        for (Passage i :
//                                passageList) {
//                            System.out.println(i.getTitle());
//                        }
//                    }
//                }).start();
//            }
//        });
//
//        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharePortWeibo.initSDK(getActivity());
//                new SharePortWeibo().setText("本地测试").share();
//            }
//        });
//
//        root.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharePortWeixin.initSDK(getActivity());
//                new SharePortWeixin().setText("本地测试").share();
//            }
//        });

        return root;
    }





}