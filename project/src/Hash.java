/**
 * Created by xyz on 2018/12/12.
 */
public class Hash {

    public int hashCode(String str) {
        int h = str.hashCode();
        return (h & 0x7FFFFFFF);
    }

}
