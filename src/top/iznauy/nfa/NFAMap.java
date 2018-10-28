package top.iznauy.nfa;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class NFAMap {

    // 主要是加速查找
    private static Map<Integer, NFAState> stateMap = new HashMap<>();

    public static NFAState getState(Integer id) {
        return stateMap.get(id);
    }

    public static void addState(NFAState state) {
        stateMap.put(state.getId(), state);
    }
}
