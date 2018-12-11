import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class MisraGries {

    public static void main(String[] args) throws IOException {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        FileProcessing fileProcessing = new FileProcessing();
        List<String> words = fileProcessing.readFile(filePath + "/test_article.txt");

        MisraGries run = new MisraGries();
        Scanner input = new Scanner(System.in);
        int k = input.nextInt();
        Map<String, Integer> map = run.misraGries(words, k);

        fileProcessing.saveResult(map, "test_output");
//        System.out.println("Hello World!");
    }

    public Map<String, Integer> misraGries(List<String> words, int k) {
        Map<String, Integer> map = new HashMap<>();
        int len = words.size();
        for (int i = 0; i < len; i++) {
            String key = words.get(i);
            if (map.containsKey(key)) {
                int v = map.get(key);
                map.put(key, v + 1);
            } else if (map.size() < k) {
                map.put(key, 1);
            } else {
                for (Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext(); ) {
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
        }
        return map;
    }

}
