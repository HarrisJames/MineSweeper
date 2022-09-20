package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        boolean won = intent.getBooleanExtra("com.example.minesweeper.gameWon", false);
        String time =  intent.getStringExtra("com.example.minesweeper.time");
        TextView tv = (TextView) findViewById(R.id.textView);
        String text;
        if(won) text = "Good Job! You Won in " + time + " seconds!";
        else text = "Sorry! You Lost in " + time + " seconds!";
        tv.setText(text);
    }

    public void playAgain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}