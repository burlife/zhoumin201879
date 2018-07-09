package com.bawi.zhoumin201879;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
private ScratchTextView scratchTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          scratchTextView=findViewById(R.id.t_guagua);
        String[] str_reward={"one","two","three"};
       scratchTextView.setText(str_reward[getRandom()]);
       scratchTextView.initScratchCard(0xFFFFFFFF, 20, 1f);
    }
    private int getRandom() {
        Random random = new Random();
        int number = random.nextInt(4);
        return number;
    }
}
