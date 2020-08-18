package ChessEngine;

import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class SM {
    public int score;
    public Move bestMove;

    public SM(int score, Move move){
        this.score = score;
        this. bestMove = move;
    }
}
