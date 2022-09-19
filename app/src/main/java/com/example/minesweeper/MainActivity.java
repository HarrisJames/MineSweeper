package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TextView[][] cell_tvs;
    private boolean flagging = false;
    private int flagsLeft = 4;
    private boolean running;
    private boolean gameWon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cell_tvs = new TextView[10][8];

        LayoutInflater li = LayoutInflater.from(this);
        GridLayout grid = findViewById(R.id.gridLayout);
        for (int i = 0; i<10; i++) {
            for (int j = 0; j < 8; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setBackgroundColor(Color.rgb(23, 212, 64));
                tv.setTextColor(Color.rgb(23, 212, 64));
                tv.setOnClickListener(this::onCellClick);
                tv.setText("-1");

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);
                grid.addView(tv, lp);
                cell_tvs[i][j] = tv;
            }
        }
        updateFlags();

        //populate flags
    }

    public void updateFlags(){
        TextView flags = findViewById(R.id.flagsLeft);
        flags.setText(String.valueOf(flagsLeft));
    }
    public void changeMode(View view) {
        TextView mode =  findViewById(R.id.mode);
        String text;
        if(flagging){
            text = "\u26cf\ufe0f";
            flagging = false;
        }
        else {
            text = "\uD83D\uDEA9";
            flagging = true;
        }
        mode.setText(text);
    }

    public void dig(int i, int j){
        TextView tv = cell_tvs[i][j];
        tv.setBackgroundColor(Color.LTGRAY);
        tv.setTextColor(Color.LTGRAY);
    }

    public int findI(TextView tv){
        for(int i = 0; i < cell_tvs.length; i++){
            for(int j = 0; j < cell_tvs[i].length; j++){
                if(cell_tvs[i][j] == tv) return i;
            }
        }
        return -1;
    }

    public int findJ(TextView tv){
        for (TextView[] cell_tv : cell_tvs) {
            for (int j = 0; j < cell_tv.length; j++) {
                if (cell_tv[j] == tv) return j;
            }
        }
        return -1;
    }

    public void onCellClick(View view){
        TextView tv = (TextView) view;
        int i = findI(tv);
        int j = findJ(tv);
        if(!flagging){
            dig(i, j);
        }
        else if(tv.getText().equals("-1")){
            if(tv.getText().toString().equals("\uD83D\uDEA9")) {
                tv.setText("-1");
                flagsLeft++;
            }
            else if(flagsLeft > 0){
                tv.setText("\uD83D\uDEA9");
                flagsLeft--;
            }
            updateFlags();
        }
    }
}