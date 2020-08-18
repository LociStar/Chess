package ChessEngine;

import com.github.bhlangonijr.chesslib.Board;

public class ThreadBot implements Runnable {

    Board board;
    Bot bot;
    double return_value;
    int death = 5;

    public ThreadBot(Board board) {
        this.board = board.clone();
        this.bot = new Bot(board);
    }

    @Override
    public void run() {
        try {
            return_value = bot.minimax(this.board, death, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

