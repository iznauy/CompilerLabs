package top.iznauy.dfa;

import top.iznauy.utils.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public class DFARecognizer {

    private DFA dfa;

    private StringBuffer beforeEndState;

    private StringBuffer afterEndState;

    private char[] content;

    public DFARecognizer(DFA dfa, String content) {
        this.dfa = dfa;
        beforeEndState = new StringBuffer();
        afterEndState = new StringBuffer();
        this.content = content.toCharArray();
    }

    public List<Token> recognize() {
        List<Token> tokens = new ArrayList<>();
        int pointer = 0;
        DFAState currentState = dfa.getStartState();
        boolean toEnd = false;
        String endType = null;
        Map<DFAState, String> stateToName = dfa.getEndStates();
        Set<DFAState> endStates = stateToName.keySet();
        if (endStates.contains(currentState)) {
            toEnd = true;
            endType = stateToName.get(currentState);
        }

        while (true) {
            if (pointer > content.length)
                break;
            char currentChar = content[pointer];
            pointer++;
            DFAEdge edge = currentState.getOutEdge(currentChar);
            if (edge == null) {
                if (!toEnd)
                    throw new RuntimeException("error");
                else {
                    Token token = new Token(endType, beforeEndState.toString());
                    tokens.add(token);
                    pointer -= afterEndState.length();
                    beforeEndState.delete(0, beforeEndState.length());  // 识别出了一个新token，很开心
                    afterEndState.delete(0, afterEndState.length());
                    currentState = dfa.getStartState(); // 重置dfa
                }
            } else {
                currentState = edge.getEndState();
                if (toEnd) { // 判断字符应该追加在哪里
                    afterEndState.append(currentChar);
                } else
                    beforeEndState.append(currentChar);

                if (endStates.contains(currentState)) { // 是不是到了一个新的终止状态
                    toEnd = true;
                    endType = stateToName.get(currentState);
                    beforeEndState.append(afterEndState.toString()); // 把后续部分追加上去
                    afterEndState.delete(0, afterEndState.length());
                }
            }
        }
        if (!toEnd)
            throw new RuntimeException("unfinished");
        if (afterEndState.length() > 0)
            throw new RuntimeException("extra chars");
        Token token = new Token(endType, beforeEndState.toString());
        tokens.add(token);
        return tokens;
    }

    public void setDfa(DFA dfa) {
        this.dfa = dfa;
    }

}
