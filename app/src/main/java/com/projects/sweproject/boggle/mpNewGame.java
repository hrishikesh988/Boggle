package com.projects.sweproject.boggle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class mpNewGame extends AppCompatActivity {

    int[][] matrix = {{R.id.Point00, R.id.Point01, R.id.Point02, R.id.Point03},
            {R.id.Point10, R.id.Point11, R.id.Point12, R.id.Point13},
            {R.id.Point20, R.id.Point21, R.id.Point22, R.id.Point23},
            {R.id.Point30, R.id.Point31, R.id.Point32, R.id.Point33}};

    Point[][] lMatrix;
    int[][] touchPath;
    int gridX,gridY;
    int viewHeight;
    int viewWidth;
    int offset;
    AlertDialog.Builder alertDialog;
    static boolean active = false;

    private TextView scoreView;

    int score = 0;
    int word_count = 0;

    BoardCreator bc;
    String [][] board;
    String level;

    private LinearLayout main;
    private SquareTextView sq;
    private TextView wordIn;
    private TextView timer;
    private String letter, word,letter_path="";

    private ArrayList<String> selected_words;
    // The following are used for the shake detection

    private DatabaseReference mDatabaseReference;

    private WordDBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        //get selected level
        Bundle extras = getIntent().getExtras();
        String Level = extras.getString("LEVEL");
        this.level = Level;

        dbHelper = new WordDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Toast.makeText(getApplicationContext(), "Level: "+ Level, Toast.LENGTH_LONG).show();
        // ShakeDetector initialization

        scoreView = (TextView) findViewById(R.id.score_textView);
        scoreView.setText("Your Score: "+score);

        //init
        board = new String[4][4];
        lMatrix = new Point[4][4];
        touchPath = new int[4][4];
        viewHeight = 0;
        viewWidth = 0;
        wordIn = (TextView)findViewById(R.id.WordInput);
        timer = (TextView) findViewById(R.id.timer);

        //Touch grid
        main = (LinearLayout) findViewById(R.id.MainLayout);
        main.post(new Runnable() {
            @Override
            public void run() {
                SquareLayout box = (SquareLayout) findViewById(R.id.SquareLayout);
                gridX = (int)box.getX();

                sq = (SquareTextView) findViewById(matrix[0][0]);
                viewWidth = sq.getWidth();
                viewHeight = sq.getHeight();

                offset = (viewWidth * 2) / 6;

                int x;
                int y = 0;
                for(int i = 0; i < 4; ++i){
                    x = 0;
                    for(int j = 0; j < 4; ++j){
                        lMatrix[i][j] = new Point(x, y);
                        x = x + viewWidth;
                    }
                    y = y + viewHeight;
                }
            }
        });


        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            gridY =
                    TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        gridY += getStatusBarHeight();
        Log.i("*** TAG :: ","gridY = "+ gridY);
        //start
        startGame();
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void startGame(){
        //clear

        word = "";
        letter_path ="";
        wordIn.setText(word);
        selected_words = new ArrayList<String>();
        bc = new BoardCreator(dbHelper, level);


        //gen board
        for(int i=0;i<4;++i) {
            for (int j = 0; j < 4; ++j) {
                sq = (SquareTextView) findViewById(matrix[i][j]);
                touchPath[i][j] = 0;
                String[] str = bc.getBoardLayout();
                MultiPlayerBoard mpb = new MultiPlayerBoard(str);

                //stores the current board to fire base
                mDatabaseReference.child("Board").setValue(mpb);


                board[i][j] = str[i*4+j];

                sq.setText(board[i][j], TextView.BufferType.EDITABLE);

            }
        }
        //start timer
        // TODO: create motion lock
        new CountDownTimer(180000, 1000) {




            public void onTick(long millisUntilFinished) {

            timer.setText("Time left: " + ((millisUntilFinished/1000)/60)  + ":"+ ((String.format("%02d", (millisUntilFinished/1000)%60))));

            }

            public void onFinish() {


                timer.setText("Time's up!");


                alertDialog = new AlertDialog.Builder(mpNewGame.this);
                alertDialog.setTitle("GAME OVER! All words in the game are: " + bc.getAllWordsInString());


                alertDialog.setPositiveButton("GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        //quit go back to Mainacitivyt
                        Intent intent = new Intent(mpNewGame.this, SinglePlayer.class);
                        startActivity(intent);

                    }
                });

                alertDialog.create();
                if(active)
                    alertDialog.show();

            }
        }.start();


    }

    private void track(int x, int y){
        int pointX;
        int pointY;
        //letter_path = "";
        //wordIn.setText("");
        //wordIn.append("X:"+x+" Y:"+y+"\n");
        //wordIn.append("GX:"+gridX+"GY: "+gridY+" OS:"+offset+" VW:"+viewWidth+"\n");
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                //wordIn.append("|"+lMatrix[i][j].x+","+lMatrix[i][j].y+"|");
                pointX = lMatrix[i][j].x + gridX;
                pointY = lMatrix[i][j].y + gridY;

                if(x > pointX + offset && x < pointX + viewWidth - offset){
                    if(y > pointY + offset && y < pointY + viewHeight - offset){
                        if(touchPath[i][j] == 0){

                            sq = (SquareTextView) findViewById(matrix[i][j]);
                            letter = sq.getText().toString();

                            //highlight
                            sq.setBackgroundColor(Color.RED);

                            word = word + letter;
                            letter_path = letter_path + i+j;
                            wordIn.setText(word);
                            //wordIn.setGravity(Gravity.LEFT);


                            //un highlight

                            touchPath[i][j] = 1;
                        }
                    }
                }
            }
        }
    }

    private void resetHighlight(){
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                sq = (SquareTextView) findViewById(matrix[i][j]);

                sq.setBackgroundColor(Color.WHITE);
            }
        }
    }

    //TODO This function should be used properly
    public void submit(){
        // clear touchPath
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                touchPath[i][j] = 0;
            }
        }
        // clear word
        word = "";
        //wordIn.setText("");
        //wordIn.append(" ");
        //resetHighlight();
    }

    public boolean onTouchEvent(MotionEvent event) {

        int X = (int) event.getX();
        int Y = (int) event.getY();
        int EA = event.getAction();
        //Log.d("*** MotionEvent :: "," Event: "+EA);
        switch (EA) {
            case MotionEvent.ACTION_DOWN:
                track(X, Y);
                break;

            case MotionEvent.ACTION_MOVE:
                track(X, Y);
                break;

            case MotionEvent.ACTION_UP:
                submit();
                break;
        }

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        super.onBackPressed();
        active = false;
    }

    public int calculateScore(String word){

        if (word.length() == 3 || word.length() == 4){
            score++;
        }

        else if (word.length() == 5 ){
            score = score + 2;
        }

        else if (word.length() == 6 ){
            score = score + 3;
        }

        else if (word.length() == 7 ){
            score = score + 5;
        }

        else if (word.length() >= 8 ){
            score = score + 10;
        }

        return score;
    }


    public  void clickOnSubmitButton(View view) {

            String input = wordIn.getText().toString();

            if(input.length()<3){
                Toast.makeText(getApplicationContext(), "Word should be longer than 2 letters!", Toast.LENGTH_SHORT).show();
            }
            else {


                boolean isValidWord = dbHelper.getWord(input);

                if (isValidWord == true) {

                    if (selected_words.contains(letter_path) == false) {
                        Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
                        selected_words.add(letter_path);
                        word_count++;
                        scoreView.setText("Your Score: " + calculateScore(input));
                    } else {
                        Toast.makeText(getApplicationContext(), "you have already selected this word!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();

                }
            }
            wordIn.setText("");
            letter_path = "";
            resetHighlight();
        }

    public  void clickOnCancelButton(View view) {
        wordIn.setText("");
        letter_path ="";
        resetHighlight();
    }

    public static Intent newIntent(Context packageContext, String gameLevel) {
        Intent i = new Intent( packageContext, mpNewGame.class);
        i.putExtra("LEVEL",gameLevel);
        return i;
    }


}