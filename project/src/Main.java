import java.util.Map;

/**
 * Created by xyz on 2018/12/10.
 */
public class Main {

    public static void main(String[] args) {
        int index = System.getProperty("user.dir").lastIndexOf("/");
        String path = System.getProperty("user.dir").substring(0, index);

        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        int k = 1000;
        FileProcessing fileProcessing = new FileProcessing();
        Map<String, Integer> true_words = fileProcessing.readCountFile(path + "/wiki_sorted_word_count.txt", k);
        Map<String, Integer> test_words = fileProcessing.readCountFile(path + "/wiki_MG_output.txt", k);
        int size = fileProcessing.readFileSize(filePath + "/wiki_streaming.txt");
        Main main = new Main();

        System.out.println("Result:" + main.evaluate(true_words, test_words, k, size));
    }

    public double evaluate(Map<String, Integer> true_words, Map<String, Integer> test_words, int k, int size) {
        int count = 0;
        int t = 0;
        for (String key : true_words.keySet()) {
            if (true_words.get(key) >= (size / k)) {
                t++;
                if (test_words.containsKey(key)) {
                    count++;
                }
            }
        }
        return (double) count / (double) t;
    }
}
