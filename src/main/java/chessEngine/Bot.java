package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.ArrayList;
import java.util.Collections;

public class Bot {

    Board board;
    PositionTabels tables;
    double value;
    MoveList bestMoves = new MoveList();
    ArrayList<Double> values = new ArrayList<>();

    public Bot(Board board, PositionTabels tables) {
        this.board = board;
        this.tables = tables;
    }
    public Move bot_move_old() throws Exception{
        // Threading
        long before = System.nanoTime();
        MainThreadClass mainThreadClass = new MainThreadClass(this.board, this.tables);
        Move move = mainThreadClass.startThreads();
        long after = System.nanoTime();
        long timeElapsed = (after - before) / 1000000;
        System.out.println(timeElapsed);
        return move;
    }

    public Move bot_move() throws Exception {

        long before = System.nanoTime();
        //System.out.println(board);
        minimax_new(this.board.clone(), 6, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
        long after = System.nanoTime();
        long timeElapsed = (after - before) / 1000000;
        double best = 0;
        System.out.println(timeElapsed);
        if (board.getSideToMove().equals(Side.WHITE)) {
            best = Collections.max(this.values);
        } else {
            best = Collections.min(this.values);
        }
        Move move = this.bestMoves.get(this.values.indexOf(best));
        this.values = new ArrayList<>();
        this.bestMoves = new MoveList();
        return move;
    }

    public double minimax(Board board, int depth, double alpha, double beta) throws Exception {
        //System.out.println(board.getSideToMove());

        if (board.isDraw()) {
            return 0.0;
        }
        if (depth == 0) {
            return (evaluation(board));
        }

        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (board.getSideToMove() == Side.WHITE) {
            if (board.isMated()){
                return -(900 * depth);
            }
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Maxi: " + move);

                board.doMove(move);
                double eval = minimax(board, depth - 1, alpha, beta);
                board.undoMove();

                //maxEval = Math.max(maxEval, eval);
                if (eval > maxEval) {
                    maxEval = eval;
                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else { // Side Black
            if (board.isMated()){
                return 900 * depth;
            }

            double minEval = Double.POSITIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Mini: " + move);

                board.doMove(move);
                double eval = minimax(board, depth - 1, alpha, beta);
                board.undoMove();

                //minEval = Math.min(minEval, eval);
                if (eval < minEval) {
                    minEval = eval;
                    //this.bestMove = move;
                }

                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;

        }
    }


    public double minimax_new(Board board, int depth, double alpha, double beta, boolean first) throws Exception {
        //System.out.println(board.getSideToMove());

        if (board.isDraw()) {
            return 0.0;
        }
        if (depth == 0) {
            return (evaluation(board));
        }

        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (board.getSideToMove() == Side.WHITE) {
            if (board.isMated()){
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
            if (board.isMated()){
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
                    //this.bestMove = move;
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


    public double evaluation(Board board) {
        this.value = 0;
        for (Square square : board.getPieceLocation(Piece.BLACK_PAWN)) {
            this.value -= 10;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_ROOK)) {
            this.value -= 50;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_BISHOP)) {
            this.value -= 30;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KNIGHT)) {
            this.value -= 30;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_QUEEN)) {
            this.value -= 90;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        /*for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
            this.value -= 900;// + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }*/
        for (Square square : board.getPieceLocation(Piece.WHITE_PAWN)) {
            this.value += 10;//+ tables.white_Pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_ROOK)) {
            this.value += 50;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_BISHOP)) {
            this.value += 30;// + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_KNIGHT)) {
            this.value += 30;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_QUEEN)) {
            this.value += 90;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        /*for (Square square : board.getPieceLocation(Piece.WHITE_KING)) {
            this.value += 900;//+ tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }*/
        return value;
    }

}
