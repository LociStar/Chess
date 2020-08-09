package ChessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class test {
     public static void main (String []args) throws Exception{
         System.out.println("Against Robot?");
         Boolean bot = true;

         Board board = new Board();

         Boolean no_win = true;

         while (!board.isDraw() && !board.isStaleMate() && board.isMated()) {
             break;
         }

//         //Make a move from E2 to E4 squares
//         board.doMove(new Move(Square.E2, Square.E4));

         //Move-generator:
         MoveList moves = MoveGenerator.generateLegalMoves(board);

         board.doMove(moves.get((int) (Math.random() * 10)));

         //print the chessboard in a human-readable form
         System.out.println(board.toString());
         System.out.println(moves);
     }

}
