package chessEngine;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.io.Console;
import java.util.*;

public class UCI {
    static String ENGINENAME = "LociStarBot";
    private static Board board = new Board();
    private static PositionTabels positionTabels = new PositionTabels();
    private static Bot bot = new Bot(board, positionTabels);

    public static void uciCommunication() throws Exception {
        Scanner input = new Scanner(System.in);
        while (true) {
            String inputString = input.nextLine();
            //System.out.println("inputString: " + inputString);
            if ("uci".equals(inputString)) {
                inputUCI();
            } else if (inputString.startsWith("setoption")) {
                inputSetOption(inputString);
            } else if ("isready".equals(inputString)) {
                inputIsReady();
            } else if ("ucinewgame".equals(inputString)) {
                inputUCINewGame();
            } else if (inputString.startsWith("position")) {
                inputPosition(inputString);
            } else if (inputString.startsWith("go")) {
                inputGo(inputString);
            } else if ("print".equals(inputString)) {
                inputPrint();
            } else if ("quit".equals(inputString)) {
                inputQuit();
            } else if ("stop".equals(inputString)) {
                //MoveList legalMoves = MoveGenerator.generateLegalMoves(board);
                System.out.println("bestmove " + bot.GlobalbestMove);
            }
        }
    }

    public static void inputUCI() {
        System.out.println("id name " + ENGINENAME);
        System.out.println("id author LociStar");
        //options go here
        //Zobrist.zobristFillArray();
        System.out.println("uciok");
    }

    public static void inputSetOption(String inputString) {
        //set options
    }

    public static void inputIsReady() {
        System.out.println("readyok");
    }

    public static void inputUCINewGame() {
        board = new Board();
        bot.transposition.clear();
        //Zobrist.zobristFillArray();
        bot = new Bot(board, positionTabels);
    }

    public static void inputPosition(String input) {
        input = input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input = input.substring(9);
            //System.out.println(input);
            board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        } else if (input.contains("fen")) {
            input = input.substring(4);
            board.loadFromFen(input);
        }
        if (input.contains("moves")) {
            input = input.substring(input.indexOf("moves") + 6).toUpperCase();
            String[] array = input.split(" ");
            //System.out.println(Arrays.toString(array));
            Move move = null;
            for (String s : array) {
                if (s.length() < 5) { // convert move-string to Move-Object
                    move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)));
                } else {
                    String temp = s.substring(4, 5);
                    if (temp.equalsIgnoreCase("q")) {
                        move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)), Piece.make(board.getSideToMove(), PieceType.QUEEN));
                    } else if (temp.equalsIgnoreCase("r")) {
                        move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)), Piece.make(board.getSideToMove(), PieceType.ROOK));
                    } else if (temp.equalsIgnoreCase("n")) {
                        move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)), Piece.make(board.getSideToMove(), PieceType.KNIGHT));
                    } else if (temp.equalsIgnoreCase("b")) {
                        move = new Move(Square.fromValue(s.substring(0, 2)), Square.fromValue(s.substring(2, 4)), Piece.make(board.getSideToMove(), PieceType.BISHOP));
                    }


                }
                //System.out.println(move);
                board.doMove(move);
            }
            //System.out.println("Bot last move: " + move);
        }
    }

    public static void inputGo(String input) throws Exception {
        if (input.equals("go")) {
            bot.wtime = 400000;
            bot.btime = 400000;
        } else {
            input = input.substring(3).concat(" ");
            ArrayList<String> tempArray = new ArrayList<>(Arrays.asList(input.split(" ")));
            //System.out.println(tempArray);
            if (!input.equals("infinite")) {
                if (input.contains("wtime")) {
                    bot.wtime = Integer.parseInt(tempArray.get(1));
                }
                if (input.contains("btime")) {
                    bot.btime = Integer.parseInt(tempArray.get(3));
                }
                if (input.contains("winc")) {
                    bot.winc = Integer.parseInt(tempArray.get(5));
                }
                if (input.contains("binc")) {
                    bot.binc = Integer.parseInt(tempArray.get(7));
                }
            } else {
                bot.wtime = 400000;
                bot.btime = 400000;
            }
        }

        //search for best move //TODO: Threading/ stop command
        //System.out.println("bestmove " + bot.bot_move().toString());
        //board.doMove(move);
        //System.out.println("Time millis: " +
        System.out.println("bestmove " + bot.bot_move().toString());
        System.out.println(bot.transposition.size());
        //System.out.println(board.getMoveCounter());
        bot.transposition.values().removeIf(e -> e.age <= bot.board.getMoveCounter());
        System.out.println(bot.transposition.size());

    }

    public static void inputPrint() {
        // pint field
        System.out.println(board.toString());
    }

    public static void inputStop() {
        System.out.println("quit");
    }

    public static void inputQuit() {
        System.exit(0);
    }
}