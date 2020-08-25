package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Arrays;

public class test {
    public static void main(String[] args) throws Exception{
        PositionTabels t = new PositionTabels();
        System.out.println(Arrays.deepToString(t.white_Pawn));
        System.out.println(Arrays.deepToString(t.black_pawn));
        Square s = Square.A7;
        System.out.println(s.getFile().ordinal() + "  " + s.getRank().ordinal());
        System.out.println(10 + t.black_pawn[s.getRank().ordinal()][s.getFile().ordinal()]);
        Move move = new Move(Square.E1, Square.A1);
        String input = "d2d4";
        System.out.println(Square.fromValue("D2"));
    }
}
