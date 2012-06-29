package org.systemsbiology.xtandem.fragmentation;

/**
 * Match class defintion
 */
public class SimpleChainingMatch {
    int fromA;
    int fromB;
    int toA;
    int toB;
    double score;


    public SimpleChainingMatch(int fA, int tA, int fB, int tB, double s) {
        fromA = fA;
        fromB = fB;
        toA = tA;
        toB = tB;
        score = s;
    }

    /**
     * Returns the value of fromA.
     */
    public int getFromA() {
        return fromA;
    }

    /**
     * Returns the value of fromB.
     */
    public int getFromB() {
        return fromB;
    }

    /**
     * Returns the value of toA.
     */
    public int getToA() {
        return toA;
    }

    /**
     * Returns the value of toB.
     */
    public int getToB() {
        return toB;
    }

    /**
     * Returns the value of score.
     */
    public double getScore() {
        return score;
    }

    //check whether this Match onecjt overlap with input Match m;
//return true if two objects do not overlap
    public boolean notOverlap(SimpleChainingMatch m) {
        return (m.getFromA() > toA || fromA > m.getToA()) && (m.getFromB() > toB || fromB > m.getToB());
    }

    public boolean isChainable(SimpleChainingMatch m) {
        return (m.getFromA() > toA && m.getFromB() > toB);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + fromA + ", " + toA + ", " + fromB + ",  " + toB + ", " + score + ",]");
        return buffer.toString();
    }

    public String showInTargets(String original,String sought)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(original.substring(getFromA(),getToA()));
        sb.append("\n");
        sb.append(sought.substring(getFromB(),getToB()));
        sb.append("\n");

        return sb.toString();
    }
}
