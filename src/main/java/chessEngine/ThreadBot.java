package chessEngine;

import com.github.bhlangonijr.chesslib.Board;

public class ThreadBot implements Runnable {

    Board board;
    Bot bot;
    double return_value;
    int count_figures;
    int depth;


    public ThreadBot(Board board, PositionTabels tables) {
        this.board = board;
        this.bot = new Bot(board, tables);
        int x = board.getMoveCounter();
        //System.out.println("Move Counter: " + x);
        long bb = this.board.getBitboard();
        for (int i = 1; bb != 0; i++) {
            bb &= bb - 1;
            count_figures = i;
        }
        //System.out.println(count_figures);
        //this.death = (int) Math.round(3033200 + (3.956176 - 3033200) / (1 + Math.pow((x / 2893.109), 3.231842))); // n + 1 half-moves
        //this.death = (int) Math.round(2.888889 + (20 - 2.888889) / (1 + Math.pow((count_figures / 6.529144), 1.678072)));
        //depth = (int) (2.888889 + (20 - 2.888889) / (1 + Math.pow((count_figures / 6.529144), 1.678072)));
        this.depth = 4;
        //System.out.println("Death: " + depth);

    }

    @Override
    public void run() {
        try {
            return_value = bot.minimax(this.board, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

