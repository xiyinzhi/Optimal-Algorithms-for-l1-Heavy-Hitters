import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xyz on 2018/12/10.
 */
public class Main {
    private static final int size = 321320640;

    public static void main(String[] args) {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        Main main = new Main();

        int k = 1000;
        FileProcessing fileProcessing = new FileProcessing();
//        LinkedHashMap<String, Integer> trueWords = fileProcessing.readCountFile(path + "/wiki_sorted_word_count.txt", k);
//        LinkedHashMap<String, Integer> MGWords = fileProcessing.readCountFile(path + "/wiki_MG_output.txt", k);
//        main.evaluateMG(trueWords, MGWords, k);
//        int size = fileProcessing.readFileSize(filePath + "/wiki_streaming.txt");
//        System.out.println(size);

        LinkedHashMap<String, Integer> trueSmallWords = fileProcessing.readCountFile(path + "/test_input_sorted_word_count.txt", k);
        LinkedHashMap<String, Integer> testSmallWords = fileProcessing.readCountFile(path + "/test_near_optimal_output.txt", k);
        main.evaluateNO(trueSmallWords, testSmallWords);
    }

    public void evaluateMG(LinkedHashMap<String, Integer> trueWords, LinkedHashMap<String, Integer> MGWords, int k) {
        int count = 0;
        int t = 0;
        for (String key : trueWords.keySet()) {
            if (trueWords.get(key) >= (size / k)) {
                t++;
                if (MGWords.containsKey(key)) {
                    count++;
                }
            }
        }
        System.out.println("Result:" + (double) count / (double) t);
        return;
    }

    public void evaluateNO(LinkedHashMap<String, Integer> trueSmallWords, LinkedHashMap<String, Integer> testSmallWords) {
        int count = 0;
        int t = 0;
        double epsilon = 0.001;
        int m = 1000000;
        double phi = 0.01;
        for (String key : trueSmallWords.keySet()) {
            int trueValue = trueSmallWords.get(key);
            if (trueValue >= phi * m) {
                t++;
                if (testSmallWords.containsKey(key)) {
                    int testValue = testSmallWords.get(key);
                    if (testValue - trueValue <= epsilon * m && testValue - trueValue >= -epsilon * m) {
                        count++;
                    }
                }
            } else if (trueValue <= (phi - epsilon) * m) {
                if (testSmallWords.containsKey(key)) {
                    System.out.println("Wrong!");
                }
            }
        }
        System.out.println("Result:" + (double) count / (double) t);
        return;
    }
}
