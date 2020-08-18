package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        boolean bot_active = false;
        Bot bot;

        Scanner in = new Scanner(System.in);
        Board board = new Board();
        //board.loadFromFen("rr1k4/8/8/8/8/8/8/7K b - - 0 0");

        System.out.println("Player vs. Robot?");
        if (in.nextLine().equalsIgnoreCase("yes")) {
            bot_active = true;
        }
        bot = new Bot(board);// load engine/ bot-object

        System.out.println("Name: ");
        Person person = new Person(in.nextLine());

        while (!board.isDraw() && !board.isMated()) {
            //print the chessboard in a human-readable form
            System.out.println(board.toString());

            if (bot_active) {

                if (board.getSideToMove() == person.side) {
                    //Player move-input
                    System.out.println("Enter your move: ");
                    String move_in = in.nextLine().toUpperCase();

                    if (move_in.equalsIgnoreCase("value")) {
                        System.out.println(bot.evaluation(board));
                    } else {
                        // MoveValidation compare to LegalMoves
                        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);
                        Move player_move = new Move(Square.fromValue(move_in.substring(0, 2)), Square.fromValue(move_in.substring(2, 4)));
                        for (Move move : legalMoves) {
                            if (move.equals(player_move)) {
                                board.doMove(player_move, true);
                                break;
                            }
                        }
                    }

                } else {
                    bot.bot_move(board);
                }
            } else {
                bot.bot_move(board);
            }

//         //Make a move from E2 to E4 squares
//         board.doMove(new Move(Square.E2, Square.E4));
        }
        System.out.println(board.toString());
        System.out.println("Game Over");
        if(board.isMated()) System.out.println(board.getSideToMove() + " won!");

    }
}
