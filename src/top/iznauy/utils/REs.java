package top.iznauy.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public class REs {

    public static final String OK_1 = "OK1";

    public static final String OK_2 = "OK2";

    public static Map<String, Integer> reToPrior = new HashMap<>();

    static {
        reToPrior.put(OK_1, 1);
        reToPrior.put(OK_2, 2);
    }

}
