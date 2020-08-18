package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainThreadClass {
    Board board;
    MoveList moves;
    ArrayList<Double> evaluations = new ArrayList<>();

    public MainThreadClass(Board board) throws Exception {
        this.board = board;
        moves = MoveGenerator.generateLegalMoves(this.board);
    }

    public Move startThreads() throws Exception {
        double before = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        ArrayList<ThreadBot> threadBots = new ArrayList<>();

        for (Move move : moves) {
            board.doMove(move);
            ThreadBot threadBot = new ThreadBot(board);
            board.undoMove();
            threadBots.add(threadBot);
            executor.execute(threadBot);
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        if (executor.isTerminated()) {
            for (int i = 0; i < moves.size(); i++) {
                evaluations.add(threadBots.get(i).return_value);
            }
        }

        double ret;
        if (board.getSideToMove() == Side.BLACK) {
            ret = Collections.min(evaluations); // min/max -> bot is black/white
        } else {
            ret = Collections.max(evaluations);
        }
        System.out.println(ret);
        double after = System.currentTimeMillis();
        double durationMS = (after - before);
        System.out.println("Time: " + durationMS + " ms");
        System.out.println("Bots possible moves: " + moves);
        System.out.println(evaluations);
        return (moves.get(evaluations.indexOf(ret)));
    }
}
