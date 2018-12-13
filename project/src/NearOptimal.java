import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class NearOptimal {
    private static final int m = 321320640;

    private static final double epsilon = 0.001;
    private static final double delta = 0.1;
    private static final double phi = 0.01;
    private static final int l = (int) (6 * Math.log(6 / delta) / Math.pow(epsilon, 2));
    private static final int hashRange = roundUp(4 * Math.pow(l, 2) / delta);
    private static final double p = (double) (6 * l) / (double) m;
    private static final double k = 1 / epsilon;
    private static final double threshold = 1 / phi;

    public static void main(String[] args) throws IOException {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

//        System.out.println("Hello World!");
//        int a = 837983602;
//        int b = -98299;
//        System.out.println(a + " " + (a & 0x7FFFFFFF));
//        System.out.println(b + " " + (b & 0x7FFFFFFF));

        Map<Integer, Integer> t1 = new TreeMap<>();
        List<String> t2 = new ArrayList<>();

        NearOptimal main = new NearOptimal();
        main.insert(t1, t2, filePath + "/wiki_streaming.txt");
        main.report(t1, t2, "near_optimal_output.txt");
    }


    public void insert(Map<Integer, Integer> t1, List<String> t2, String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                Random r = new Random();
                double d = r.nextDouble();
                if (d <= p) {
                    // perform Misra-Gries
                    String x = tempString.toLowerCase();
                    Hash hash = new Hash();
                    int key = hash.hashFunction(hash.hashCode(x));
                    if (t1.containsKey(key)) {
                        int v = t1.get(key);
                        t1.put(key, v + 1);
                    } else if (t1.size() < k) {
                        t1.put(key, 1);
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

                    // sort T1 by value, desc
                    t1 = sortByValueDescending(t1);

                    // maintain T2
                    int i = 1;
                    for (Map.Entry<Integer, Integer> entry : t1.entrySet()) {
                        if (i <= threshold) {
                            if (entry.getValue() <= t1.get(key)) {
                                if (!t2.contains(x)) {
                                    if (t2.size() == threshold) {
                                        String y = t2.get(t2.size() - 1);
                                        if (t1.get(hash.hashFunction(hash.hashCode(y))) < t1.get(key)) {
                                            t2.remove(y);
                                            t2.add(x);
                                        }
                                    }
                                } else {
                                    t2.add(x);
                                }
                            }
                        } else {
                            break;
                        }
                        i++;
                    }
//                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                } else {
                    continue;
                }
            }
            reader.close();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        } finally

        {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }


    public void report(Map<Integer, Integer> t1, List<String> t2, String fileName) throws IOException {
        FileProcessing fileProcessing = new FileProcessing();
        fileProcessing.saveNearOptimalResult(t1, t2, fileName);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static int roundUp(double x) {
        int base = (int) x;
        if (x - base > 0) {
            return base + 1;
        } else {
            return base;
        }
    }

}
