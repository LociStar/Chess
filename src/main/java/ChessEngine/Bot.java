package ChessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class Bot {

    Board board;

    public Bot (Board board){
        this.board = board;
    }

    public void bot_move() throws Exception{
        //Move-generator:
        MoveList moves = MoveGenerator.generateLegalMoves(board);
        // random move
        board.doMove(moves.get((int) (Math.random() * 10)));
    }
}
