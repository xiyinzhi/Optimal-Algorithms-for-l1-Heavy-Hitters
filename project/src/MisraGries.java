import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class MisraGries {

    public static void main(String[] args) throws IOException {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        FileProcessing fileProcessing = new FileProcessing();
//        List<String> words = fileProcessing.readFile(filePath + "/wiki_streaming.txt");

        MisraGries run = new MisraGries();
        Scanner input = new Scanner(System.in);
        int k = input.nextInt();
        Map<String, Integer> map = run.misraGries(k, filePath + "/wiki_streaming.txt");

        fileProcessing.saveResult(map, "wiki_MG_output");
//        System.out.println("Hello World!");
    }

    public Map<String, Integer> misraGries(int k, String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Map<String, Integer> map = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
//                list.add(tempString.toLowerCase());
                String key = tempString.toLowerCase();
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
        return map;
    }

}
