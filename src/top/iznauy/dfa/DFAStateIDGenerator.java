package top.iznauy.dfa;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
class DFAStateIDGenerator {

    private static int nextID = 0;

    public static int getId() {
        nextID += 1;
        return nextID;
    }

    public static void init() {
        nextID = 0;
    }

}
