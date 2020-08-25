package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class Bot {

    Board board;
    PositionTabels tables;
    double value;

    public Bot(Board board, PositionTabels tables) {
        this.board = board;
        this.tables = tables;
    }

    public Move bot_move() throws Exception {
        // Threading
        MainThreadClass mainThreadClass = new MainThreadClass(this.board, this.tables);
        return mainThreadClass.startThreads();
    }

    double maxEval;
    double minEval;
    double eval;

    public double minimax(Board board, int depth, double alpha, double beta) throws Exception {


        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (depth == 0) {
            return (evaluation(board));

        } else if (board.isDraw()) {
            if (board.getSideToMove() == Side.WHITE) {
                return Double.NEGATIVE_INFINITY;
            } else return Double.POSITIVE_INFINITY;

        } else if (board.getSideToMove() == Side.WHITE) {
            maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Maxi: " + move);
                board.doMove(move);
                if (board.isMated()) {
                    maxEval = 900 * depth;
                    eval = 0;
                } else eval = minimax(board, depth - 1, alpha, beta);

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
                } else eval = minimax(board, depth - 1, alpha, beta);

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

    public double evaluation(Board board) {
        this.value = 0;
        for (Square square : board.getPieceLocation(Piece.BLACK_PAWN)) {
            this.value -= 10 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_ROOK)) {
            this.value -= 50 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_BISHOP)) {
            this.value -= 30 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KNIGHT)) {
            this.value -= 30 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_QUEEN)) {
            this.value -= 90 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
            this.value -= 900 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_PAWN)) {
            this.value += 10 + tables.white_Pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_ROOK)) {
            this.value += 50 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_BISHOP)) {
            this.value += 30 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_KNIGHT)) {
            this.value += 30 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_QUEEN)) {
            this.value += 90 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_KING)) {
            this.value += 900 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        return value;
    }

}
