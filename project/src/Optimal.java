/**
 * Created by Jiarong on 2018/12/10.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Optimal {
    private static final double m = 321320640.0;
    private static final double epsilon = 0.001; // e
    private static final double phi = 0.01;   // ϕ
    private static final double l = Math.pow(10, 5) * Math.pow(epsilon, 2);
    private static final int hashRange = (int) (100 / epsilon);
    private static final int t1Length = (int) (2 * Math.pow(phi, -1));
    private static final int t2Row = (int) (2 * Math.pow(epsilon, -1));
    private static final int t2Column = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private static final int t3D1 = (int) (100 * Math.pow(epsilon, -1));
    private static final int t3D2 = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private static final int t3D3 = (int) (4 * Math.log(Math.pow(epsilon, -1)));
    private static final int prob = (int) (l / m);
    private int s = 0;
    private static final int hashFunctionNum = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private HashMap<Integer, Integer> t1;
    private int[][] t2 = new int[t2Row][t2Column];
    private int[][][] t3 = new int[t3D1][t3D2][t3D3];
    //private double[] f = new double[t2Column];
    private ArrayList<HashFunction> hashFunctions;


    public Optimal() {
        HashMap<Integer, Integer> t1 = new HashMap<>();  // key , value

        // initialize hash functions
        for (int i = 0; i < hashFunctionNum; i++) {
            HashFunction hs = new HashFunction(hashRange);
            hashFunctions.add(hs);
        }

    }

    public void train(String fileName) throws Exception {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        File file = new File(fileName);
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(file));
        String tempString;
        Hash hash = new Hash();

        while ((tempString = reader.readLine()) != null) {

            int x = hash.hashCode(tempString);
            Insert(x);
        }
        report();

    }


    private void Insert(int x) {
        double d = new Random().nextDouble();
        while (d <= prob) {
            s++;
            // perform Misra-Gries
            misraGries(x);
            for (int j = 0; j < hashFunctionNum; j++) {
                int i = hashFunctions.get(j).getHashResult(x);

                // With probability ε, increment T2[i, j]
                d = new Random().nextDouble();
                if (d <= epsilon) {
                    t2[i][j]++;
                    int temp = t2[i][j];
                    int t = (int) Math.log(Math.pow(10, -6) * Math.pow(temp, 2));
                    double p = Math.min(epsilon * Math.pow(2, t), 1);
                    if (t >= 0) {
                        d = new Random().nextDouble();
                        if (d <= p) {
                            t3[i][j][t]++;
                        }
                    }


                }

            }


        }

    }

    private HashSet<Integer> report() {
        HashSet<Integer> setX = new HashSet<>();

        for (Map.Entry<Integer, Integer> entry : t1.entrySet()) {
            int x = entry.getKey();
            if (entry.getValue() >= 0) {
                double[] fj = new double[t2Column];
                for (int j = 0; j < hashFunctionNum; j++) {
                    double fx = findMidian(fj, 0, fj.length - 1);
                    if (fx >= (phi - (epsilon / 2)) * s) {
                        setX.add(x);
                    }


                }
            }
        }
        return setX;


    }


    private void misraGries(int x) {
        // perform Misra-Gries

        if (t1.containsKey(x)) {
            int v = t1.get(x);
            t1.put(x, v + 1);
        } else if (t1.size() < t1Length) {
            t1.put(x, 1);
        } else {
            for (Iterator<Map.Entry<Integer, Integer>> it = t1.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Integer> item = it.next();
//                    System.out.println("item: " + item.getKey() + ", " + item.getValue());
                int v = item.getValue();
                v--;
                if (v == 0) {
                    it.remove();
                } else {
                    item.setValue(v);
                }
            }
        }

    }

    private double calculateFj(int x, int j) {
        double res = 0;
        for (int t = 0; t < (int) (4 * Math.log(Math.pow(epsilon, -1))); t++) {

            int i = hashFunctions.get(j).getHashResult(x);
            res += (double) t3[i][j][t] / Math.min(epsilon * Math.pow(2, t), 1);
        }
        return res;
    }

    private double findMidian(double[] fj, int left, int right) {
        int i = left;
        int j = right;
        double partition = fj[left];

        while (i < j) {
            while (i < j && fj[j] >= partition) j--;
            if (i < j) {
                fj[i] = fj[j];
                i++;
            }


            while (i < j && fj[i] <= partition) i++;
            if (i < j) {
                fj[j] = fj[i];
                j--;
            }

        }
        fj[i] = partition;
        if (i == fj.length / 2) return i;
        else if (i < fj.length / 2) return findMidian(fj, i + 1, right);
        else return findMidian(fj, left, j - 1);


    }


}
