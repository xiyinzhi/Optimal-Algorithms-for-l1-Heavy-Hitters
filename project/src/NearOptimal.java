import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class NearOptimal {
//        private static final int m = 321320640;
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
    private static Map<Integer, Integer> t1;
    private static List<String> t2;

    public static void main(String[] args) throws IOException {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);


        NearOptimal main = new NearOptimal();
        t1 = main.insert(t1, t2, filePath + "/wiki_streaming.txt");
        main.report(t1, t2, "near_optimal_output");
    }


    public NearOptimal() {
        t1 = new HashMap<>();
        t2 = new ArrayList<>();
        hashFunction = new HashFunction(hashRange);
    }

    private long startTime = System.currentTimeMillis();


    public Map<Integer, Integer> insert(Map<Integer, Integer> t1, List<String> t2, String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int index = 0;
            while ((tempString = reader.readLine()) != null) {

                if (index % 1000000 == 0) {
                    long time = System.currentTimeMillis();
                    System.out.println(index + " " + (time - startTime));
                }
                index++;

                Random r = new Random();
                double d = r.nextDouble();
                if (d <= p) {
                    // perform Misra-Gries
                    String x = tempString.toLowerCase();
                    Hash hash = new Hash();
                    int key = hashFunction.getHashResult(hash.hashCode(x));
//                    System.out.println(x + ":" + key);
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
                    if (t1.containsKey(key)) {
                        for (Map.Entry<Integer, Integer> entry : t1.entrySet()) {
                            if (i <= threshold) {
//                            System.out.println(entry);
                                if (entry.getValue() <= t1.get(key)) {
                                    if (!t2.contains(x)) {
                                        boolean flag = false;
                                        // in case x is not in t2 but h(x) is in the 1/φ of t1
                                        for (String str : t2) {
                                            if (hashFunction.getHashResult(hash.hashCode(str)) == key) {
                                                flag = true;
                                                break;
                                            }
                                        }
                                        if (flag == true) {
                                            break;
                                        }
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
                                    // Ensure that elements in T2 are ordered according to corresponding values in T1.
                                    i = 0;
                                    for (int tempKey : t1.keySet()) {
                                        if (i < t2.size()) {
                                            int hashedKey = hashFunction.getHashResult(hash.hashCode(t2.get(i)));
                                            if (hashedKey != tempKey) {
                                                for (int j = i; j < t2.size(); j++) {
                                                    String a = t2.get(j);
                                                    if (hashFunction.getHashResult(hash.hashCode(a)) == tempKey) {
                                                        String tempStri = t2.get(i);
                                                        String tempStrj = t2.get(j);
                                                        t2.remove(i);
                                                        t2.add(i, tempStrj);
                                                        t2.remove(j);
                                                        t2.add(j, tempStri);
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            break;
                                        }
                                        i++;
                                    }
                                    break;
                                }
                            } else {
                                break;
                            }
                            i++;
                        }

                    }
//                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                } else {
                    continue;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return t1;
    }


    public void report(Map<Integer, Integer> t1, List<String> t2, String fileName) throws IOException {
        File file = new File("../" + fileName + ".txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        int n = Math.min(t2.size(), t1.size());
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : t1.entrySet()) {
            if (i < n) {
                String key = t2.get(i);
                out.write(key + " " + entry.getValue() + "\r\n");
                i++;
            } else {
                break;
            }
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
