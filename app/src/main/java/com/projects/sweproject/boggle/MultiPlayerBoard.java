package com.projects.sweproject.boggle;

import java.util.ArrayList;

/**
 * Created by hrishikesh on 2/26/17.
 */

public class MultiPlayerBoard {

    ArrayList<String> BoardList;
    ArrayList<String> Player1WordList;
    ArrayList<String> Player2WordList;
   String PassCode;
    boolean GameStarted;
    boolean Player2Joined;
    String AllWords;



    public MultiPlayerBoard(){}

    public MultiPlayerBoard (String[] Board, String allWords){
        BoardList = new ArrayList();
        Player1WordList = new ArrayList();
        Player2WordList = new ArrayList();
        AllWords = allWords;

        for (int i =0; i<Board.length; i++){
            this.BoardList.add(Board[i]);
        }
    }
}



