package chessEngine;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Arrays;

public class test {
    public static void main(String[] args) throws Exception {

        //TranspositionTable transpositionTable = new TranspositionTable(100);
        Zobrist.zobristFillArray();
        Board board = new Board();
        Bot bot = new Bot(board, new PositionTabels());
        Move move = bot.bot_move();
        board.doMove(move);
        System.out.println(move);
        //System.out.println(bot.CURRENT_DEPTH);
        //long z = Zobrist.getZobristHash(board);
        //System.out.println(Zobrist.getZobristHash(board));
        //TranspositionTable transpositionTable = new TranspositionTable(200);
        //transpositionTable.record(z, 3, 3, (int) bot.evaluation(board)*10, 1);
        //System.out.println(transpositionTable.getDepth(z));
        //System.out.println(move.getFrom().ordinal());
        System.out.println(bot.transposition.size());
        //bot.addTransposition(Zobrist.getZobristHash(board), 0, 10, -10, 1.0);
        //System.out.println(bot.transposition.get(Zobrist.getZobristHash(board)).score);
        System.out.println(board.hashCode());
        System.out.println("ok");


    }
}
