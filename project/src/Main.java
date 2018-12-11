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

        int k = 1000;
        FileProcessing fileProcessing = new FileProcessing();
        Map<String, Integer> true_words = fileProcessing.readCountFile(path + "/wiki_sorted_word_count.txt", k);
        Map<String, Integer> test_words = fileProcessing.readCountFile(path + "/wiki_MG_output.txt", k);
//        int size = fileProcessing.readFileSize(filePath + "/wiki_streaming.txt");
//        System.out.println(size);

        Main main = new Main();
        main.evaluateMG(true_words, test_words, k);
    }

    public void evaluateMG(Map<String, Integer> true_words, Map<String, Integer> test_words, int k) {
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
        System.out.println("Result:" + (double) count / (double) t);
        return;
    }
}
