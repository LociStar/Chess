package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.ArrayList;

public class ThreadBot implements Runnable {

    Bot bot;
    double return_value;
    int depth;
    MoveList bestMoves = new MoveList();
    ArrayList<Double> values = new ArrayList<>();
    boolean finished = false;


    public ThreadBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        try {
            minimax_new(bot.board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
            finished = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double minimax_new(Board board, int depth, double alpha, double beta, boolean first) throws Exception {
        //System.out.println(board.getSideToMove());

        if (board.isDraw()) {
            return 0.0;
        }
        if (depth == 0) {
            return (bot.evaluation(board));
        }

        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (board.getSideToMove() == Side.WHITE) {
            if (board.isMated()) {
                return -(900 * depth);
            }
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Maxi: " + move);

                board.doMove(move);
                double eval = minimax_new(board, depth - 1, alpha, beta, false);
                board.undoMove();

                //maxEval = Math.max(maxEval, eval);
                if (eval > maxEval) {
                    maxEval = eval;

                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
                if (first) {
                    //System.out.println(board.getSideToMove());
                    this.bestMoves.add(move);
                    this.values.add(eval);
                }
            }
            return maxEval;
        } else { // Side Black
            if (board.isMated()) {
                return 900 * depth;
            }

            double minEval = Double.POSITIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Mini: " + move);

                board.doMove(move);
                double eval = minimax_new(board, depth - 1, alpha, beta, false);
                board.undoMove();

                //minEval = Math.min(minEval, eval);
                if (eval < minEval) {
                    minEval = eval;
                }

                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
                if (first) {
                    this.bestMoves.add(move);
                    this.values.add(eval);
                }
            }
            return minEval;

        }
    }

}

