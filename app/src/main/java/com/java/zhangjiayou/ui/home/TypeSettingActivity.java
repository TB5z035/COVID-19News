package com.java.zhangjiayou.ui.home;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.java.zhangjiayou.R;

import java.util.HashSet;
import java.util.List;

public class TypeSettingActivity extends AppCompatActivity {

    private LinearLayout selectedLinearLayout;
    private LinearLayout unselectedLinearLayout;
    private List<CardView> items;
    private static final String[] availableTypes = {"News", "Paper"};
    private HashSet<String> hashSet;

    class MyItemView extends RelativeLayout {
        private String type;

        public MyItemView(@NonNull Context context, String type) {
            super(context);
            this.type = type;
            setTag(type);
            inflate(context, R.layout.drag_item, this);

            CardView cardView = findViewById(R.id.select_card_view);
            cardView.setClickable(true);
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.startDragAndDrop(
                            ClipData.newPlainText("Type", type),
                            new View.DragShadowBuilder(MyItemView.this),
                            MyItemView.this,
                            0
                    );
                    return false;
                }
            });

            TextView textView = findViewById(R.id.select_text_view);
            textView.setText(type);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_setting);

        getSupportActionBar().setTitle("Passage Types");

        hashSet = new HashSet<>();
        for (String item :
                availableTypes) {
            if (getIntent().getStringExtra(item) != null) hashSet.add(item);
        }


        selectedLinearLayout = findViewById(R.id.selected_layout);
        unselectedLinearLayout = findViewById(R.id.unselected_layout);
        for (String item :
                availableTypes) {
            if (hashSet.contains(item)) selectedLinearLayout.addView(new MyItemView(this, item));
            else unselectedLinearLayout.addView(new MyItemView(this, item));
        }

        View.OnDragListener onDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                View child = (View) event.getLocalState();
                ViewGroup parent = (ViewGroup) child.getParent();
                ViewGroup target = (ViewGroup) v;
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                        parent.removeView(child);
                        target.addView(child);
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                        target.removeView(child);
                        parent.addView(child);
                        return true;
                    case DragEvent.ACTION_DROP: // 放开被拖拽View
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
                        return true;
                }
                return false;
            }
        };

        selectedLinearLayout.setOnDragListener(onDragListener);
        unselectedLinearLayout.setOnDragListener(onDragListener);

    }
}