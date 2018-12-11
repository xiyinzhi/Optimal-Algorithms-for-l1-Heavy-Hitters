import java.util.Set;

/**
 * Created by xyz on 2018/12/10.
 */
public class Main {

    public static void main(String[] args) {
        int index = System.getProperty("user.dir").lastIndexOf("/");
//        String path = System.getProperty("user.dir").substring(0, index);
//
//        index = path.lastIndexOf("/");
        String filePath = System.getProperty("user.dir").substring(0, index);

        int k = 1000;
        FileProcessing fileProcessing = new FileProcessing();
        Set<String> true_words = fileProcessing.readFile(filePath + "/wiki_word_count.txt", k);
        Set<String> test_words = fileProcessing.readFile(filePath + "/wiki_output.txt", k);

        Main main = new Main();

        System.out.println("Result:" + main.evaluate(true_words, test_words, k));
    }

    public double evaluate(Set<String> true_words, Set<String> test_words, int k) {
        int count = 0;
        for (String key : true_words) {
            if (test_words.contains(key)) {
                count++;
            }
        }
        return (double) count / (double) k;
    }
}
