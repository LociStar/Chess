package chessEngine;

public class tableNode{
    protected double score;
    protected int depth;
    protected int type; // 0 is exact, 1 is upper bound and -1 is lower bound
    protected int age;
    public tableNode(double score, int depth, int type, int age){
        this.score = score;
        this.depth = depth;
        this.type = type;
        this.age = age;
    }
}
