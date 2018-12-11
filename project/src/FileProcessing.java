import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xyz on 2018/12/10.
 */
public class FileProcessing {

    public List<String> readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                list.add(tempString.toLowerCase());
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
        return list;
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
