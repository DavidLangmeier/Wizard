package at.aau.ase.wizard;

public class NodepadeTestPoints {
    private int round;
    private int points;
    private int stiche;

    public NodepadeTestPoints(int round, int points, int stiche) {
        this.round = round;
        this.points = points;
        this.stiche = stiche;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getStiche() {
        return stiche;
    }

    public void setStiche(int stiche) {
        this.stiche = stiche;
    }
}
