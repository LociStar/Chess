package ChessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Scanner;

public class test {
    public static void main(String[] args) throws Exception {

        boolean bot_active = false;
        Bot bot = null;

        Scanner in = new Scanner(System.in);
        Board board = new Board();

        System.out.println("Against Robot?");
        if (in.nextLine().equalsIgnoreCase("yes")) {
            bot_active = true;
            bot = new Bot(board);// load engine/ object
        }

        System.out.println("Name: ");
        Person person = new Person(in.nextLine());

        while (!board.isDraw() && !board.isMated()) {
            //print the chessboard in a human-readable form
            System.out.println(board.toString());

            if (bot_active) {
                if (board.getSideToMove() == person.side) {
                    String move_in = in.nextLine().toUpperCase();
                    try {
                        board.doMove(new Move(Square.fromValue(move_in.substring(0, 2)), Square.fromValue(move_in.substring(2, 4))), true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    bot.bot_move();
                }
            }

//         //Make a move from E2 to E4 squares
//         board.doMove(new Move(Square.E2, Square.E4));
        }

    }
}
