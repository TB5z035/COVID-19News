package com.java.zhangjiayou.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.database.PassageDatabase;
import com.java.zhangjiayou.sharing.SharePortWeibo;
import com.java.zhangjiayou.sharing.SharePortWeixin;
import com.java.zhangjiayou.util.Passage;

import java.util.List;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
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