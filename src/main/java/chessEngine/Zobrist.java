package chessEngine;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

import java.security.SecureRandom;

public class Zobrist {
    static long zArray[][][] = new long[2][6][64];
    static long zEnPassant[] = new long[8];
    static long zCastle[] = new long[4];
    static long zBlackMove;
    static long FileMasks8[] =/*from fileA to FileH*/
            {
                    0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
                    0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
            };
    public static long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }
    public static long random64Bad() {
        return (long)(Math.random()*1000000000000000000L);
    }
    public static void zobristFillArray() {
        for (int color = 0; color < 2; color++)
        {
            for (int pieceType = 0; pieceType < 6; pieceType++)
            {
                for (int square = 0; square < 64; square++)
                {
                    zArray[color][pieceType][square] = random64();
                }
            }
        }
        for (int column = 0; column < 8; column++)
        {
            zEnPassant[column] = random64();
        }
        for (int i = 0; i < 4; i++)
        {
            zCastle[i] = random64();
        }
        zBlackMove = random64();
    }
    public static long getZobristHash(Board board) {
        long returnZKey = 0;
        for (int square = 0; square < 64; square++)
        {
            if (((board.getBitboard(Piece.WHITE_PAWN) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][0][square];
            }
            else if (((board.getBitboard(Piece.BLACK_PAWN) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][0][square];
            }
            else if (((board.getBitboard(Piece.WHITE_KNIGHT) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][1][square];
            }
            else if (((board.getBitboard(Piece.BLACK_KNIGHT) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][1][square];
            }
            else if (((board.getBitboard(Piece.WHITE_BISHOP) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][2][square];
            }

            else if (((board.getBitboard(Piece.BLACK_BISHOP) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][2][square];
            }
            else if (((board.getBitboard(Piece.WHITE_ROOK) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][3][square];
            }
            else if (((board.getBitboard(Piece.BLACK_ROOK) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][3][square];
            }
            else if (((board.getBitboard(Piece.WHITE_QUEEN) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][4][square];
            }
            else if (((board.getBitboard(Piece.BLACK_QUEEN) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][4][square];
            }
            else if (((board.getBitboard(Piece.WHITE_KING) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[0][5][square];
            }
            else if (((board.getBitboard(Piece.BLACK_KING) >> square) & 1) == 1)
            {
                returnZKey ^= zArray[1][5][square];
            }
        }
        for (int column = 0; column < 8; column++)
        {
            if (board.getEnPassantTarget().getBitboard() == FileMasks8[column])
            {
                returnZKey ^= zEnPassant[column];
            }
        }
        if (board.getCastleRight(Side.WHITE).equals(CastleRight.KING_SIDE))
            returnZKey ^= zCastle[0];
        if (board.getCastleRight(Side.WHITE).equals(CastleRight.QUEEN_SIDE))
            returnZKey ^= zCastle[1];
        if (board.getCastleRight(Side.BLACK).equals(CastleRight.KING_SIDE))
            returnZKey ^= zCastle[2];
        if (board.getCastleRight(Side.BLACK).equals(CastleRight.QUEEN_SIDE))
            returnZKey ^= zCastle[3];
        if (!board.getSideToMove().equals(Side.WHITE))
            returnZKey ^= zBlackMove;
        return returnZKey;
    }
    public static void testDistribution() {
        int sampleSize = 2000;
        int sampleSeconds = 10;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (sampleSeconds * 1000);
        int[] distArray;
        distArray = new int[sampleSize];
        while (System.currentTimeMillis() < endTime)
        {
            for (int i = 0; i < 10000; i++)
            {
                distArray[(int)(random64()% (sampleSize / 2)) + (sampleSize / 2)]++;
            }
        }
        for (int i = 0; i < sampleSize; i++)
        {
            System.out.println(distArray[i]);
        }
    }
}