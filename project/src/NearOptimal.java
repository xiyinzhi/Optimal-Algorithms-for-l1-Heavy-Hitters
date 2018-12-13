import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class NearOptimal {
    private static final int size = 321320640;

    private static final double epsilon = 0.0001;
    private static final double delta = 0.1;
    private static final double phi = 0.01;
    private static final int l = (int) (6 * Math.log(6 / delta) / Math.pow(epsilon, 2));
    private static final int hashRange = 1;

    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        int a = 837983602;
//        int b = -98299;
//        System.out.println(a + " " + (a & 0x7FFFFFFF));
//        System.out.println(b + " " + (b & 0x7FFFFFFF));
        Map<Integer, Integer> t1 = new TreeMap<>();
        List<String> t2 = new ArrayList<>();

        NearOptimal main = new NearOptimal();
        main.insert(t1, t2);
        main.report(t1, t2);
    }


    public void insert(Map<Integer, Integer> t1, List<String> t2) {

    }

    public void report(Map<Integer, Integer> t1, List<String> t2) {

    }
}
