package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    private TextView[][] cell_tvs;
    private int clock = 0;
    private boolean[][] dugCells;
    private boolean[][] bombs;
    private boolean flagging = false;
    private int flagsLeft = 4;
    private boolean running;
    private boolean gameOver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }

        cell_tvs = new TextView[10][8];
        dugCells = new boolean[10][8];
        bombs = new boolean[10][8];

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
        placeMines();
        runTimer();
    }
    public void placeMines(){
        Random rand = new Random();
        int toPlace = flagsLeft;
        while(toPlace > 0){
            int i = rand.nextInt(10);
            int j = rand.nextInt(8);
            if(!bombs[i][j]) {
                bombs[i][j] = true;
                cell_tvs[i][j].setText("\uD83D\uDCA3");
                cell_tvs[i][j].setTextColor(Color.argb(0,0,0,0));
                toPlace--;
            }
        }
    }


    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                timeView.setText(String.valueOf(seconds));
                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
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
        int touching = 0;
        dugCells[i][j] = true;

        if(i > 0) {
            if (j > 0 && !dugCells[i-1][j-1] && bombs[i - 1][j - 1]) touching++;
            if (!dugCells[i-1][j]&& bombs[i - 1][j]) touching++;
            if (j < cell_tvs[i].length-1 && !dugCells[i-1][j+1] && bombs[i - 1][j + 1]) touching++;
        }

        if(j > 0 && !dugCells[i][j-1] && bombs[i][j-1]) touching++;
        if(j < cell_tvs[i].length-1 && !dugCells[i][j+1] &&bombs[i][j+1]) touching++;

        if(i < cell_tvs.length-1) {
            if (j > 0 && !dugCells[i+1][j-1] &&bombs[i + 1][j - 1]) touching++;
            if (!dugCells[i+1][j]&&bombs[i + 1][j]) touching++;
            if (j < cell_tvs[i].length-1 && !dugCells[i+1][j+1] && bombs[i + 1][j + 1]) touching++;
        }


        if(touching == 0){
            if(i > 0) {
                if (j > 0 && !dugCells[i-1][j-1] && cell_tvs[i - 1][j - 1].getText().equals("-1")) dig(i-1, j-1);
                if (!dugCells[i-1][j] && cell_tvs[i - 1][j].getText().equals("-1")) dig(i-1, j);
                if (j < cell_tvs[i].length-1 && !dugCells[i-1][j+1] && cell_tvs[i - 1][j + 1].getText().equals("-1")) dig(i-1,j+1);
            }
            if(j > 0 && !dugCells[i][j-1] && cell_tvs[i][j-1].getText().equals("-1")) dig(i, j-1);
            if(j < cell_tvs[i].length-1 && !dugCells[i][j+1] && cell_tvs[i][j+1].getText().equals("-1")) dig(i,j+1);
            if(i < cell_tvs.length-1) {
                if (j > 0 && !dugCells[i+1][j-1] && cell_tvs[i + 1][j - 1].getText().equals("-1")) dig(i+1, j-1);
                if (!dugCells[i+1][j] && cell_tvs[i + 1][j].getText().equals("-1")) dig(i+1, j);
                if (j < cell_tvs[i].length-1 && !dugCells[i+1][j+1] && cell_tvs[i + 1][j + 1].getText().equals("-1")) dig(i+1, j+1);
            }
            tv.setTextColor(Color.LTGRAY);
        }
        else{
            tv.setText(String.valueOf(touching));
            tv.setTextColor(Color.DKGRAY);
        }
        tv.setBackgroundColor(Color.LTGRAY);
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

    public void checkForWin(){
        for(int i = 0; i < cell_tvs.length; i++){
            for(int j = 0; j < cell_tvs[i].length; j++){
                if(!bombs[i][j] && !dugCells[i][j]) return;
            }
        }
        System.out.println("through");
        running = false;
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("com.example.minesweeper.gameWon", (boolean)true);
        TextView clockTV = (TextView) findViewById(R.id.timer);
        intent.putExtra("com.example.minesweeper.time", clockTV.getText());
        startActivity(intent);
    }
    public void onCellClick(View view){
        TextView tv = (TextView) view;
        if(!running && !gameOver) running = true;
        if(!running && gameOver){
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra("com.example.minesweeper.gameWon", (boolean)false);
            TextView clockTV = (TextView) findViewById(R.id.timer);
            intent.putExtra("com.example.minesweeper.time", clockTV.getText());
            startActivity(intent);
        }
        int i = findI(tv);
        int j = findJ(tv);
        if(!flagging){
            if(bombs[i][j]){
                running = false;
                gameOver = true;
                cell_tvs[i][j].setTextColor(Color.BLACK);
            }
            else dig(i, j);
        }
        else if(!dugCells[i][j]){
            if(tv.getText().toString().equals("\uD83D\uDEA9")) {
                if(bombs[i][j]){
                    tv.setText("\uD83D\uDCA3");
                    tv.setTextColor(Color.argb(0,0,0,0));
                }
                else {
                    tv.setText("-1");
                    tv.setTextColor(Color.rgb(23, 212, 64));
                }
                flagsLeft++;
            }
            else if(flagsLeft > 0){
                tv.setText("\uD83D\uDEA9");
                tv.setTextColor(Color.BLACK);
                flagsLeft--;
            }
            updateFlags();
        }
        if(!gameOver) checkForWin();
    }
}