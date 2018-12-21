/**
 * Created by Jiarong on 2018/12/10.
 */

import java.io.*;
import java.util.*;

public class Optimal {
    private static final double m = 9000000; // 321320640.0;
    private static final double epsilon = 0.01; // e 0.03
    private static final double phi = 0.01;   // ϕ 0.05
    private static final double l = 5 * Math.pow(10, 2) * Math.pow(epsilon, -2);
    private static final int hashRange = (int) (100 / epsilon);
    private static final int t1Length = (int) (2 * Math.pow(phi, -1));
    private static final int t2Row = (int) (100 / epsilon);
    private static final int t2Column = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private static final int t3D1 = (int) (100 * Math.pow(epsilon, -1));
    private static final int t3D2 = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private static final int t3D3 = (int) (4.0 * Math.log(Math.pow(epsilon, -1)));
    private static final double prob = 1;
    private int s = 0;
    private static final int hashFunctionNum = (int) (200 * Math.log(12 * Math.pow(phi, -1)));
    private HashMap<String, Integer> t1;
    private int[][] t2 = new int[t2Row][t2Column];
    private int[][][] t3 = new int[t3D1][t3D2][t3D3];
    //private double[] f = new double[t2Column];
    private ArrayList<HashFunction> hashFunctions;

    // debg
    int over = 0;
    int c = 0;


    public Optimal() {
        t1 = new HashMap<>();  // key , value
        hashFunctions = new ArrayList<>();
        System.out.println(t3D3);

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
            Insert(x, tempString);
            c++;
            if (c % 100000 == 0) System.out.println(c);
        }
//        System.out.println(s);
        report();
//        System.out.println(c);
        System.out.println(s);

    }


    private void Insert(int x, String word) {
        double d = new Random().nextDouble();
        //double a = prob;
        s++; // since small dataset
        // perform Misra-Gries
        misraGries(word);
        for (int j = 0; j < hashFunctionNum; j++) {
            int i = hashFunctions.get(j).getHashResult(x);
            int t = 0;
            // a = i;
            // With probability ε, increment T2[i, j]
            d = new Random().nextDouble();
            if (d <= epsilon * 10) t2[i][j] = t2[i][j] + 1;
            //t2[i][j] = t2[i][j] + 1;
            int temp = t2[i][j];
            if (temp == 0) t = 1;
            else t = (int) Math.log(Math.pow(10, -6) * Math.pow(temp, 2));
            double p = Math.min(epsilon * Math.pow(2, t), 1);
            if (t >= 0) {
                d = new Random().nextDouble();
                if (d <= p) {
                    t3[i][j][t] = t3[i][j][t] + 1;

                }
            }


        }


    }

    private void report() throws Exception {
        String fileName = "xaa_out9";//out_full_1
        File file = new File(fileName + ".txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Comparator<Double> comparator = new Comparator<Double>() {

            @Override
            public int compare(Double d1, Double d2) {
                return (int) (d1 - d2);
            }
        };
        //HashSet<String> setX = new HashSet<>();

        for (Map.Entry<String, Integer> entry : t1.entrySet()) {
            Hash hash = new Hash();

            int x = hash.hashCode(entry.getKey());
            if (entry.getValue() >= 0) {
                int fjLength = t2Column;

                Queue<Double> fj = new PriorityQueue<>(fjLength, comparator);

                //double[] fj = new double[t2Column];
                for (int j = 0; j < hashFunctionNum; j++) {
                    double temp = calculateFj(x, j);
                    // fj contain top 5%?
                    if (fj.size() > fjLength) {
                        if (temp > fj.peek()) {
                            fj.poll();
                            fj.add(temp);
                        }
                    } else fj.add(temp);

                }

                double fx = 0;
                if (fjLength % 2 == 0) {
                    fx = (fj.toArray(new Double[fjLength])[fjLength / 2] + fj.toArray(new Double[fjLength])[(fjLength / 2) - 1]) / 2;
                } else {
                    fx = fj.toArray(new Double[fjLength])[(fjLength / 2) - 1];
                }
                //double fx = findMidian(fj, 0, fj.length - 1);
                if (fx >= (phi - (epsilon / 2)) * s) { //(phi - (epsilon / 2)) * (s)
                    out.write(entry.getKey() + " " + fx + "\n");
                    //setX.add(entry.getKey());


                    //System.out.println(x);
                }


            }
        }
        out.close();
        //return setX;


    }


    private void misraGries(String x) {
        // perform Misra-Gries

        if (t1.containsKey(x)) {
            int v = t1.get(x);
            t1.put(x, v + 1);
        } else if (t1.size() < t1Length) {
            t1.put(x, 1);
        } else {
            for (Iterator<Map.Entry<String, Integer>> it = t1.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> item = it.next();
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
        int i = 0;

    }

    private double calculateFj(int x, int j) {
        double res = 0;
        for (int t = 0; t < (int) (4 * Math.log(Math.pow(epsilon, -1))); t++) {

            int i = hashFunctions.get(j).getHashResult(x);
            double a = t3[i][j][t];
            double b = Math.min(epsilon * Math.pow(2, t), 1);
            res += a / b;
        }
        if (res != 0) over++;
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

    public static void main(String[] args) throws Exception {
        Optimal opt = new Optimal();
        opt.train("xaa_1");//wiki_streaming.txt
    }


}
