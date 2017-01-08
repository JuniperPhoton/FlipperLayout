package com.juniperphoton.flipperviewproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.juniperphoton.flipperviewlib.FlipperView;

public class MainActivity extends AppCompatActivity {
    private FlipperView mFlipperView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlipperView = (FlipperView) findViewById(R.id.flipper_view);
        View prevView = findViewById(R.id.prev_btn);
        View nextView = findViewById(R.id.next_btn);

        prevView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipperView.previous();
            }
        });
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipperView.next();
            }
        });
    }
}
