package ChessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.ArrayList;

public class Bot {

    Board board;
    int value;

    public Bot(Board board) {
        this.board = board;
    }

    public void bot_move(Board board) throws Exception {

        // Threading
        MainThreadClass mainThreadClass = new MainThreadClass(board);
        Move move = mainThreadClass.startThreads();

        System.out.println("Move: " + move);
        this.board.doMove(move);


    }

    double maxEval;
    double minEval;
    double eval;

    public double minimax(Board board, int depth, double alpha, double beta) throws Exception {


        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (depth == 0) {
            return (evaluation(board));

        } else if (board.isDraw()) {
            return 0;

        } else if (board.getSideToMove() == Side.WHITE) {
            maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Maxi: " + move);
                board.doMove(move);

                if (board.isMated()) {
                    maxEval = 900 * depth;
                    eval = 0;
                } else {
                    eval = minimax(board, depth - 1, alpha, beta);
                }

                board.undoMove();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else if (board.getSideToMove() == Side.BLACK) {
            minEval = Double.POSITIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Mini: " + move);
                board.doMove(move);

                if (board.isMated()) {
                    minEval = -900 * depth;
                    eval = 0;
                } else {
                    eval = minimax(board, depth - 1, alpha, beta);
                }

                board.undoMove();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;

        }
        System.out.println("error");
        return 0;
    }

    public int evaluation(Board board) {
        this.value = 0;
        for (Square square : board.getPieceLocation(Piece.BLACK_PAWN)) {
            this.value -= 10;
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_ROOK)) {
            this.value -= 50;
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_BISHOP)) {
            this.value -= 30;
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KNIGHT)) {
            this.value -= 30;
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_QUEEN)) {
            this.value -= 90;
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
            this.value -= 900;
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_PAWN)) {
            this.value += 10;
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_ROOK)) {
            this.value += 50;
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_BISHOP)) {
            this.value += 30;
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_KNIGHT)) {
            this.value += 30;
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_QUEEN)) {
            this.value += 90;
        }
        //for (Square square : board.getPieceLocation(Piece.WHITE_KING)) {
        //    this.value += 900;
        //}
        return value;
    }

    /*public int evaluation(char[] positions) {
        //System.out.println(positions);
        int[] evalBlack = new int[]{-10, -30, -30, -50, -90, -900}; //pnbrqk
        int[] evalWhite = new int[]{10, 30, 30, 50, 90, 900};
        int eval = 0;
            for (Character position : positions) {
                if (position == 'P') eval += evalWhite[0];
                else if (position == 'N') eval += evalWhite[1];
                else if (position == 'B') eval += evalWhite[2];
                else if (position == 'R') eval += evalWhite[3];
                else if (position == 'Q') eval += evalWhite[4];
                else if (position == 'K') eval += evalWhite[5];
                if (position == 'p') eval += evalBlack[0];
                else if (position == 'n') eval += evalBlack[1];
                else if (position == 'b') eval += evalBlack[2];
                else if (position == 'r') eval += evalBlack[3];
                else if (position == 'q') eval += evalBlack[4];
                else if (position == 'k') eval += evalBlack[5];
            }
        return eval;
    }

    public char[] positions(Board board) {
        String context = board.getPositionId();
        ArrayList<Character> output = new ArrayList<>();
        context = context.substring(0, context.length() - 9);
        context = context.replaceAll("[^a-zA-Z]", "");
        //for (int k = 0; k < context.length(); k++) {
        //        if (Character.isLetter(context.charAt(k))) output.add(context.charAt(k));
        //}
        return context.toCharArray();
    }*/

}
