package chessEngine;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Arrays;

public class test {
    public static void main(String[] args) throws Exception{
        PositionTabels t = new PositionTabels();
        System.out.println(Arrays.deepToString(t.white_Pawn));
        System.out.println(Arrays.deepToString(t.black_pawn));
        Move move;
        String s = "D7D8q";
        System.out.println(s.charAt(4));
        move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)), Piece.make(Side.BLACK, PieceType.fromValue(s.substring(4, 5))));
        System.out.println(move);
    }
}
