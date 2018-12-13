import java.util.Random;

public class HashFunction {
    // Attributes
    private int a;
    private int b;
    private final static int Prime = 99991;
    private int hashRange; // mapping result's range alg1 is different from alg2

    // Constructors
    public HashFunction(){}
    public HashFunction(int hashRange){
        this.a = new Random().nextInt(99991);
        this.b = new Random().nextInt(99991);
        this.hashRange = hashRange;

    }

    // Functions
    public int getHashResult(int x) {
        return ((a * x + b) % Prime) % hashRange;
    }
}
