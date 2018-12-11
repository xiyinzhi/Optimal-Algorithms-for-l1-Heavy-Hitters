import java.io.*;
import java.util.*;

/**
 * Created by xyz on 2018/12/10.
 */
public class FileProcessing {

    public Map<String, Integer> readCountFile(String fileName, int k) {
        File file = new File(fileName);
        BufferedReader reader = null;
        Map<String, Integer> map = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null && k > 0) {
                String[] tempS = tempString.split(" ");
                map.put(tempS[0], Integer.parseInt(tempS[1]));
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
        return map;
    }

    public int readFileSize(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int size = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                size++;
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
        return size;
    }

    public void saveResult(Map<String, Integer> map, String fileName) throws IOException {
        File file = new File("../" + fileName + ".txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        for (String key : map.keySet()) {
            out.write(key + " " + map.get(key) + "\r\n");
        }
        out.close();
    }
}
