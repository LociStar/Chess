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
    PositionTabels tables;
    MoveList moves;
    ArrayList<Double> evaluations = new ArrayList<>();

    public MainThreadClass(Board board, PositionTabels tables) throws Exception {
        this.board = board;
        this.tables = tables;
        moves = MoveGenerator.generateLegalMoves(this.board);
    }

    public Move startThreads() throws Exception {
        //double before = System.currentTimeMillis();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        ArrayList<Thread> threadBots = new ArrayList<>();

        for (Move move : moves) {
            board.doMove(move);
            Thread threadBot = new Thread(new Runnable() {
                @Override
                public void run() {
                    return;
                }
            });
            board.undoMove();
            threadBots.add(threadBot);
            executor.execute(threadBot);
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        //int return_index = maxMinIndex(threadBots, board.getSideToMove());
        if (executor.isTerminated()) {
            for (int i = 0; i < moves.size(); i++) {
                //evaluations.add(threadBots.get(i).return_value);
            }
        }

        double ret;
        if (board.getSideToMove() == Side.BLACK) {
            ret = Collections.min(evaluations); // min/max -> bot is black/white
        } else {
            ret = Collections.max(evaluations);
        }
        //System.out.println(ret);

        //System.out.println(threadBots.get(return_index).return_value);
        //double after = System.currentTimeMillis();
        //double durationMS = (after - before);
        //System.out.println("Time: " + durationMS + " ms");
        //System.out.println("Bots possible moves: " + moves);
        //return moves.get(return_index);

        //System.out.println(evaluations);
        //System.out.println(moves.get(evaluations.indexOf(ret)));
        return (moves.get(evaluations.indexOf(ret)));
    }

    public int maxMinIndex(ArrayList<ThreadBot> threadBots, Side side) {
        double temp = threadBots.get(0).return_value;
        int index = 0;
        for (int i = 1; i < threadBots.size(); i++) {
            if (side == Side.WHITE) {
                if (threadBots.get(i).return_value > temp) {
                    temp = threadBots.get(i).return_value;
                    index = i;
                }
            } else {
                if (threadBots.get(i).return_value < temp) {
                    temp = threadBots.get(i).return_value;
                    index = i;
                }
            }
        }
        return index;
    }
}