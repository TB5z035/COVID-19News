package com.java.zhangjiayou.ui.home;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.java.zhangjiayou.R;

import java.util.ArrayList;
import java.util.List;

//TODO:Add animation
public class TypeSettingActivity extends AppCompatActivity {
    private static final Integer TYPE_SETTING_ACTIVITY = 0;
    private LinearLayout selectedLinearLayout;
    private LinearLayout unselectedLinearLayout;
    private List<CardView> items;
    private static final String[] AllTypes = {"News", "Paper","经济与发展", "病毒研究进展", "感染形势"};
    private ArrayList<String> availableList;

    class MyItemView extends RelativeLayout {
        private String type;

        public MyItemView(@NonNull Context context, String type) {
            super(context);
            this.type = type;
            setTag(type);
            inflate(context, R.layout.drag_item, this);

            CardView cardView = findViewById(R.id.select_card_view);
            cardView.setLongClickable(true);
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

    private void startShakeByView(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        scaleAnim.setRepeatMode(Animation.REVERSE);
        scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setDuration(duration);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);
        view.startAnimation(smallAnimationSet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_setting);

        getSupportActionBar().setTitle("Passage Types");

        availableList = getIntent().getStringArrayListExtra("available");
        Intent intent = new Intent();
        intent.putExtra("available", new ArrayList<>(availableList));
        TypeSettingActivity.this.setResult(TYPE_SETTING_ACTIVITY, intent);

        selectedLinearLayout = findViewById(R.id.selected_layout);
        unselectedLinearLayout = findViewById(R.id.unselected_layout);
        for (String item :
                AllTypes) {
            if (!availableList.contains(item)) {
                unselectedLinearLayout.addView(new MyItemView(this, item));
            }
        }
        for (String item :
                availableList) {
            selectedLinearLayout.addView(new MyItemView(this, item));
        }

        selectedLinearLayout.setOnDragListener((v, event) -> {
            final int action = event.getAction();
            MyItemView child = (MyItemView) event.getLocalState();
            ViewGroup parent = (ViewGroup) child.getParent();
            ViewGroup target = (ViewGroup) v;
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                    startShakeByView(child, 1f, 1f, 5, 50);
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                    child.clearAnimation();
                    parent.removeView(child);
                    target.addView(child);
                    startShakeByView(child, 1f, 1f, 5, 50);
                    if (availableList.contains(child.type)) {
                        availableList.remove(child.type);
                    }
                    if (!availableList.contains(child.type)) {
                        availableList.add(child.type);
                    }
                    break;
                case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                    break;
                case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                    break;
                case DragEvent.ACTION_DROP: // 放开被拖拽View
                    child.clearAnimation();
                    break;
                case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
                    break;
                default:
                    return false;
            }
            Intent intent1 = new Intent();
            intent1.putExtra("available", new ArrayList<>(availableList));
            TypeSettingActivity.this.setResult(TYPE_SETTING_ACTIVITY, intent1);
            return true;
        });
        unselectedLinearLayout.setOnDragListener((v, event) -> {
            final int action = event.getAction();
            MyItemView child = (MyItemView) event.getLocalState();
            ViewGroup parent = (ViewGroup) child.getParent();
            ViewGroup target = (ViewGroup) v;
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                    startShakeByView(child, 1f, 1f, 5, 50);
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                    child.clearAnimation();
                    parent.removeView(child);
                    target.addView(child);
                    startShakeByView(child, 1f, 1f, 5, 50);
                    if (availableList.contains(child.type)) {
                        availableList.remove(child.type);
                    }
                    break;
                case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                    break;
                case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                    break;
                case DragEvent.ACTION_DROP: // 放开被拖拽View
                    child.clearAnimation();
                    break;
                case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
                    break;
                default:
                    return false;
            }
            Intent intent1 = new Intent();
            intent1.putExtra("available", new ArrayList<>(availableList));
            TypeSettingActivity.this.setResult(TYPE_SETTING_ACTIVITY, intent1);
            return true;
        });

    }
}