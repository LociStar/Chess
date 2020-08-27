package chessEngine;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Arrays;

public class test {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        Bot bot = new Bot(board, new PositionTabels());
        bot.bot_move_old();
        board = new Board();
        bot = new Bot(board, new PositionTabels());
        bot.bot_move();

    }
}
