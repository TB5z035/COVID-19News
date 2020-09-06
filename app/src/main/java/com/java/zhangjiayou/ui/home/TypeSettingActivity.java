package com.java.zhangjiayou.ui.home;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
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

    private class myItemView extends RelativeLayout {
        private String type;

        public myItemView(@NonNull Context context, String type) {
            super(context);
            this.type = type;
            inflate(context, R.layout.drag_item, this);
            CardView cardView = findViewById(R.id.select_card_view);
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Create a new ClipData.
                    // This is done in two steps to provide clarity. The convenience method
                    // ClipData.newPlainText() can create a plain text ClipData in one step.

                    // Create a new ClipData.Item from the ImageView object's tag
                    ClipData.Item item = new ClipData.Item("helllo!");

                    // Create a new ClipData using the tag as a label, the plain text MIME type, and
                    // the already-created item. This will create a new ClipDescription object within the
                    // ClipData, and set its MIME type entry to "text/plain"
                    ClipData dragData = new ClipData(
                            "helllo!",
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                            item);

                    // Instantiates the drag shadow builder.
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

                    // Starts the drag

                    v.startDragAndDrop(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0          // flags (not currently used, set to 0)
                    );
                    return false;
                }
            });
            TextView textView = findViewById(R.id.select_text_view);
            textView.setText(type);

        }

    }

    private void modifyItems() {
//        LinearLayout.LayoutParams
        for (String item :
                availableTypes) {
            if (hashSet.contains(item)) selectedLinearLayout.addView(new myItemView(this, item));
            else unselectedLinearLayout.addView(new myItemView(this, item));
        }
//        selectedLinearLayout.addView(new myItemView(this, "Hsy!"));
        TextView textView;
        textView = new TextView(this);
        textView.setText("Test!!!!!!!!!!!!!!!");
        selectedLinearLayout.addView(textView);
        selectedLinearLayout.addView(new myItemView(this, "Hey!"));
        textView = new TextView(this);
        textView.setText("Test!!fjhgfhj!!!!!!!!!!");
        selectedLinearLayout.addView(textView);
        selectedLinearLayout.addView(new myItemView(this, "HA!"));

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

        modifyItems();


        selectedLinearLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
                        return true;
                    case DragEvent.ACTION_DROP: // 放开被拖拽View
                        String content = event.getClipData().getItemAt(0).getText().toString(); //接收数据
                        Toast.makeText(getApplication(), "hi", Toast.LENGTH_SHORT).show();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
                        return true;
                }
                return false;
            }
        });

    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

        // Defines the constructor for myDragShadowBuilder
        public MyDragShadowBuilder(View v) {

            // Stores the View parameter passed to myDragShadowBuilder.
            super(v);

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            // Defines local variables
            int width, height;

            // Sets the width of the shadow to half the width of the original View
            width = getView().getWidth();

            // Sets the height of the shadow to half the height of the original View
            height = getView().getHeight();

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            shadow.setBounds(0, 0, width, height);

            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height);

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2);
        }


        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        @Override
        public void onDrawShadow(Canvas canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            shadow.draw(canvas);
        }
    }

}