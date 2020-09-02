package chessEngine;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public class TranspositionTable {
    public int[] hashtable;
    public int HASHSIZE;
    public static final int SLOTS = 4;

    public TranspositionTable(int sizeInMb) {
        this.HASHSIZE = sizeInMb * 1024 * 1024 * 8 / 32 / 4;
        this.hashtable = new int[this.HASHSIZE * 4];
    }

    public void clear() {
        this.hashtable = new int[this.HASHSIZE * 4];
    }

    public void record(long zobrist, int depth, int flag, int eval, int move) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        this.hashtable[hashkey] = 0 | eval + 131071 | 262144 | flag << 20 | depth << 22;
        this.hashtable[hashkey + 1] = move;
        this.hashtable[hashkey + 2] = (int)(zobrist >> 32);
        this.hashtable[hashkey + 3] = (int)(zobrist & -1L);
    }

    public boolean entryExists(long zobrist) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        return this.hashtable[hashkey + 2] == (int)(zobrist >> 32) && this.hashtable[hashkey + 3] == (int)(zobrist & -1L) && this.hashtable[hashkey] != 0;
    }

    public int getEval(long zobrist) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        return this.hashtable[hashkey + 2] == (int)(zobrist >> 32) && this.hashtable[hashkey + 3] == (int)(zobrist & -1L) ? (this.hashtable[hashkey] & 262143) - 131071 : 0;
    }

    public int getFlag(long zobrist) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        return this.hashtable[hashkey + 2] == (int)(zobrist >> 32) && this.hashtable[hashkey + 3] == (int)(zobrist & -1L) ? this.hashtable[hashkey] >> 20 & 3 : 0;
    }

    public int getMove(long zobrist) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        return this.hashtable[hashkey + 2] == (int)(zobrist >> 32) && this.hashtable[hashkey + 3] == (int)(zobrist & -1L) ? this.hashtable[hashkey + 1] : 0;
    }

    public int getDepth(long zobrist) {
        int hashkey = (int)(zobrist % (long)this.HASHSIZE) * 4;
        return this.hashtable[hashkey + 2] == (int)(zobrist >> 32) && this.hashtable[hashkey + 3] == (int)(zobrist & -1L) ? this.hashtable[hashkey] >> 22 : 0;
    }

}
