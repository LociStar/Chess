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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bot {

    Board board;
    PositionTabels tables;
    double value;
    ArrayList<Double> values = new ArrayList<>();
    public Map<Long, tableNode> transposition = new HashMap<>();
    private final boolean transpose = true;
    private Move bestMove = new Move(Square.NONE, Square.NONE);
    public Move GlobalbestMove = new Move(Square.NONE, Square.NONE);
    public final int INITIAL_DEPTH = 0;
    public int CURRENT_DEPTH = 0;
    private boolean timeout;
    private long timeStart;
    public int TIMEOUT_MS = 10000;

    public int wtime;
    public int btime;
    public int winc = 0;
    public int binc = 0;

    public Bot(Board board, PositionTabels tables) {
        this.board = board;
        this.tables = tables;
    }

    public Move bot_move_old() throws Exception {
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
        if (board.getSideToMove().equals(Side.WHITE)) {
            if (board.getMoveCounter() == 40) {
                TIMEOUT_MS = winc - 100;
            }
            TIMEOUT_MS = (wtime / (40 - board.getMoveCounter()) + winc - 1000);
        } else {
            if (board.getMoveCounter() == 40) {
                TIMEOUT_MS = binc - 100;
            }
            TIMEOUT_MS = (btime / (40 - board.getMoveCounter()) + binc - 1000);
        }
        if (TIMEOUT_MS <= 0) {
            TIMEOUT_MS = 500;
        }

        timeout = false;
        timeStart = System.currentTimeMillis();
        for (int d = 0; ; d++) { // Iterative Deepening
            if (d > 0) {
                GlobalbestMove = bestMove;
            }

            CURRENT_DEPTH = INITIAL_DEPTH + d;
            minimax_new(this.board, CURRENT_DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);

            if (timeout) {
                board.doMove(GlobalbestMove);
                return GlobalbestMove;
            }
        }



        /*if (board.getSideToMove().equals(Side.WHITE)) {
            best = Collections.max(this.values);
        } else {
            best = Collections.min(this.values);
        }
        Move move = this.bestMoves.get(this.values.indexOf(best));
        this.values = new ArrayList<>();
        this.bestMoves = new MoveList();*/
        //depth_final = depth;
        //System.out.println(depth);
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
            if (board.isMated()) {
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
            if (board.isMated()) {
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
        if (System.currentTimeMillis() - timeStart > TIMEOUT_MS) {
            timeout = true;
            return alpha;
        }
        //System.out.println(board.getSideToMove());
        long key = board.getZobristKey();
        if (board.isDraw()) {
            addTransposition(key, CURRENT_DEPTH, alpha, beta, 0.0);
            return 0.0;
        }
        if (depth == 0) {
            double eval = evaluation(board);
            addTransposition(key, CURRENT_DEPTH, alpha, beta, eval);
            return eval;
        }

        MoveList legalMoves = MoveGenerator.generateLegalMoves(board);

        if (board.getSideToMove() == Side.WHITE) {
            if (board.isMated()) {
                addTransposition(key, CURRENT_DEPTH, alpha, beta, -900 * depth);
                return -(900 * depth);
            }
            double maxEval = Double.NEGATIVE_INFINITY;
            if (transpose && transposition.containsKey(key) && transposition.get(key).depth > depth) {
                double score = transposition.get(key).score;
                if (transposition.get(key).type == 0) {
                    return score;
                } else if (transposition.get(key).type < 0 && score >= beta) {
                    return score;
                } else if (transposition.get(key).type > 0 && score <= alpha) {
                    return score;
                } else if (beta <= alpha) {
                    return score;
                }
            }
            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Maxi: " + move);

                board.doMove(move);
                double eval = minimax_new(board, depth - 1, alpha, beta, false);
                board.undoMove();

                //maxEval = Math.max(maxEval, eval);
                if (eval > maxEval) {
                    maxEval = eval;
                    if (first) {
                        //System.out.println(board.getSideToMove());
                        this.bestMove = move;
                        //this.values.add(eval);
                    }
                }

                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else { // Side Black
            if (board.isMated()) {
                addTransposition(key, CURRENT_DEPTH, alpha, beta, 900 * depth);
                return 900 * depth;
            }

            double minEval = Double.POSITIVE_INFINITY;
            if (transpose && transposition.containsKey(key) && transposition.get(key).depth > depth) {
                double score = transposition.get(key).score;
                if (transposition.get(key).type == 0) {
                    return score;
                } else if (transposition.get(key).type < 0 && score >= beta) {
                    return score;
                } else if (transposition.get(key).type > 0 && score <= alpha) {
                    return score;
                } else if (beta <= alpha) {
                    return score;
                }
            }

            for (Move move : legalMoves) {
                //System.out.println(board.getSideToMove() + "Mini: " + move);

                board.doMove(move);
                double eval = minimax_new(board, depth - 1, alpha, beta, false);
                board.undoMove();

                //minEval = Math.min(minEval, eval);
                if (eval < minEval) {
                    minEval = eval;
                    if (first) {
                        this.bestMove = move;
                        //this.values.add(eval);
                    }
                }

                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }

            }
            return minEval;

        }
    }


    public double evaluation(Board board) {
        this.value = 0;
        for (Square square : board.getPieceLocation(Piece.BLACK_PAWN)) {
            this.value -= 10 + tables.black_pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_ROOK)) {
            this.value -= 50 + tables.black_rook[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_BISHOP)) {
            this.value -= 30 + tables.black_bishop[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_KNIGHT)) {
            this.value -= 30 + tables.black_night[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.BLACK_QUEEN)) {
            this.value -= 90 + tables.black_queen[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        if (board.getPieceLocation(Piece.BLACK_QUEEN).isEmpty() && board.getPieceLocation(Piece.WHITE_QUEEN).isEmpty() ||
                board.getMoveCounter() >= 29) {
            for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
                this.value -= 900 + tables.black_king_endgame[square.getRank().ordinal()][square.getFile().ordinal()];
            }
        } else {
            for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
                this.value -= 900 + tables.black_king_middle[square.getRank().ordinal()][square.getFile().ordinal()];
            }
        }
        // White Side
        for (Square square : board.getPieceLocation(Piece.WHITE_PAWN)) {
            this.value += 10 + tables.white_Pawn[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_ROOK)) {
            this.value += 50 + tables.white_rook[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_BISHOP)) {
            this.value += 30 + tables.white_bishop[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_KNIGHT)) {
            this.value += 30 + tables.white_night[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        for (Square square : board.getPieceLocation(Piece.WHITE_QUEEN)) {
            this.value += 90 + tables.white_queen[square.getRank().ordinal()][square.getFile().ordinal()];
        }
        if (board.getPieceLocation(Piece.BLACK_QUEEN).isEmpty() && board.getPieceLocation(Piece.WHITE_QUEEN).isEmpty() ||
                board.getMoveCounter() >= 29) {
            for (Square square : board.getPieceLocation(Piece.WHITE_KING)) {
                this.value += 900 + tables.white_king_endgame[square.getRank().ordinal()][square.getFile().ordinal()];
            }
        } else {
            for (Square square : board.getPieceLocation(Piece.BLACK_KING)) {
                this.value += 900 + tables.white_king_middle[square.getRank().ordinal()][square.getFile().ordinal()];
            }
        }
        return value;
    }

    public void addTransposition(long key, int depth, double alpha, double beta, double score) {
        if (transpose && score <= alpha) {
            transposition.put(key, new tableNode(score, depth, 1, CURRENT_DEPTH));
        } else if (transpose && score >= beta) {
            transposition.put(key, new tableNode(score, depth, -1, CURRENT_DEPTH));
        } else if (transpose) {
            transposition.put(key, new tableNode(score, depth, 0, CURRENT_DEPTH));
        }
    }

}
