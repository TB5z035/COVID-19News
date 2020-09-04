package com.java.zhangjiayou.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.zhangjiayou.R;
import com.java.zhangjiayou.network.NoResponseError;
import com.java.zhangjiayou.network.PassagePortal;
import com.java.zhangjiayou.util.Passage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {
    private Integer index = 1;


    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreAdapter loadMoreAdapter;

    private List<Passage> dataList = new ArrayList<>();

    private void getData(final boolean mode) {
        if (mode) {
            index = 1;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    onUpdateList(new PassagePortal().getNewsFromType("news", index, 20), mode);
                } catch (NoResponseError noResponseError) {
                    noResponseError.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void onUpdateList(List<Passage> list, boolean mode) {
        try {
            if (mode) dataList.clear();
            dataList.addAll(list);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
                }
            });
            index++;
        } catch (NullPointerException e) {
//            Toast.makeText(null, "You clicked too fast!", Toast.LENGTH_SHORT).show();
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .edit()
                .putStringSet(String.valueOf(R.string.history_fileid_set_key), loadMoreAdapter.getViewedMap())
                .apply();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(new SimpleDateFormat("mm:ss").format(new Date()));
//            }
//        });

        swipeRefreshLayout = root.findViewById(R.id.SwipeRefresh);
        swipeRefreshLayout.setRefreshing(true);

        //Pull-to-refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), R.string.toast_test, Toast.LENGTH_SHORT).show();
                getData(true);
            }
        });
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);


        Set<String> stringSet = getActivity()
                .getSharedPreferences(String.valueOf(R.string.history_fileid_set_key), Context.MODE_PRIVATE)
                .getStringSet(String.valueOf(R.string.history_fileid_set_key), new HashSet<String>());
        loadMoreAdapter = new LoadMoreAdapter(dataList, new HashSet<>(stringSet));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreAdapter);
        getData(true);

//        recyclerView.addOnItemTouchListener(new MyOnItemTouchListener() {
////            @Override
////            void doOnItemClickListener(View view, int position) {
////                Toast.makeText(getActivity(), "单击！" + position, Toast.LENGTH_SHORT).show();
////            }
//
//            @Override
//            void doOnItemLongClickListener(final View view, int position) {
//                Toast.makeText(getActivity(), "点击进入" + position, Toast.LENGTH_SHORT).show();
//                final Passage[] passage = {loadMoreAdapter.getItem(position)};
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Passage passageInDB = PassageDatabase.getInstance(null).getPassageDao().getPassageFromId(passage[0].getId());
//                        if (passageInDB == null)
//                            PassageDatabase.getInstance(null).getPassageDao().insert(passage[0]);
//                        else
//                            passage[0] = passageInDB;
////                        getActivity().runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                view.setBackgroundColor(R.color.colorInfoCardViewed);
////                            }
////                        });
//                    }
//                }).start();
//
//            }
//        });

        //Pull-to-load-more-listener
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);

                if (dataList.size() < 100) {
                    getData(false);
                } else {
                    loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                }
            }
        });

        return root;
    }


}