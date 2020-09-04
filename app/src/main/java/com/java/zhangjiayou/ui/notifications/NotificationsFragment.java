package com.java.zhangjiayou.ui.notifications;

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
import com.java.zhangjiayou.util.Passage;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final TextView test = root.findViewById(R.id.test_text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String text = PassageDatabase.getInstance(getContext()).getPassageDao().getPassageFromId("123").getTitle();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test.setText(text);
                    }
                });
            }
        }).start();

        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<Passage> passageList = PassageDatabase.getInstance(null).getPassageDao().getAllPassages();
                        for (Passage i :
                                passageList) {
                            System.out.println(i.getTitle());
                        }
                    }
                }).start();
            }
        });

        return root;
    }


}