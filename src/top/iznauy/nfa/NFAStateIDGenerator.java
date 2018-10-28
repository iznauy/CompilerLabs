package top.iznauy.nfa;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class NFAStateIDGenerator {

    private static int nextID = 0;

    public static int getId() {
        nextID += 1;
        return nextID;
    }

    public static void init() {
        nextID = 0;
    }

}
