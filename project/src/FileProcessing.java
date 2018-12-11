import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class FileProcessing {

    public Set<String> readFile(String fileName, int k) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Set<String> set = new HashSet<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null && k > 0) {
                set.add(tempString.split(" ")[0]);
                k--;
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
        return set;
    }

    public void saveResult(Map<String, Integer> map, String fileName) throws IOException {
        File file = new File("../" + fileName + ".txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
//        out.write("食屎啦你");
        for (String key : map.keySet()) {
            out.write(key + " " + map.get(key) + "\r\n");
        }
        out.close();
    }
}
