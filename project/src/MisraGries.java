import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Integer> map = run.misraGries(words);

        fileProcessing.saveResult(map, "test_output");
//        System.out.println("Hello World!");
    }

    public Map<String, Integer> misraGries(List<String> words) {
        Map<String, Integer> map = new HashMap<>();
        return map;
    }

}
