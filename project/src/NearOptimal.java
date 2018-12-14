import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class NearOptimal {
    //    private static final int m = 321320640;
    private static final int m = 1000;

    private static final double epsilon = 0.001;
    private static final double delta = 0.1;
    private static final double phi = 0.01;
    private static final int l = Math.min((int) (6 * Math.log(6 / delta) / Math.pow(epsilon, 2)), m);
    private static final int hashRange = l > 46340 ? Integer.MAX_VALUE : roundUp(4 * Math.pow(l, 2) / delta);
    private static final double p = (double) (6 * l) / (double) m;
    private static final double k = 1 / epsilon;
    private static final double threshold = 1 / phi;

    private HashFunction hashFunction;

    public static void main(String[] args) throws IOException {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        Map<Integer, Integer> t1 = new TreeMap<>();
        List<String> t2 = new ArrayList<>();


        NearOptimal main = new NearOptimal();
        main.insert(t1, t2, filePath + "/test_article.txt");
        main.report(t1, t2, "near_optimal_output");
    }


    public NearOptimal() {
        hashFunction = new HashFunction(hashRange);
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
                    int key = hashFunction.getHashResult(hash.hashCode(x));
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
//                            System.out.println(entry);
                            if (t1.containsKey(key) && entry.getValue() <= t1.get(key)) {
                                if (!t2.contains(x)) {
                                    if (t2.size() == threshold) {
                                        String y = t2.get(t2.size() - 1);
                                        if (t1.get(hashFunction.getHashResult(hash.hashCode(y))) < t1.get(key)) {
                                            t2.remove(y);
                                            t2.add(x);
                                        }
                                    } else {
                                        t2.add(x);
                                    }
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
        File file = new File("../" + fileName + ".txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        Hash hash = new Hash();
        int n = Math.min(t2.size(), t1.size());
        for (int i = 0; i < n; i++) {
            String key = t2.get(i);
            out.write(key + " " + t1.get(hashFunction.getHashResult(hash.hashCode(key))) + "\r\n");
        }
        out.close();

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
