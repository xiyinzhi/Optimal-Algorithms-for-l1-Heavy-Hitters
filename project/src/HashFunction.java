/**
 * Created by Jiarong on 2018/12/10.
 */

import java.util.Random;

public class HashFunction {
    // Attributes
    private int a;
    private int b;
    private final static int Prime = 99991;
    private int hashRange; // mapping result's range alg1 is different from alg2

    // Constructors
//    public HashFunction(){}
    public HashFunction(int hashRange) {
        this.a = new Random().nextInt(99991);
        this.b = new Random().nextInt(99991);

        this.hashRange = hashRange;
    }

    // Functions
    public int getHashResult(int x) {
        long tempSum = (long) a * (long) x + (long) b;
        int tempD = (int) (tempSum % Prime);
        return tempD % hashRange;
    }
}
