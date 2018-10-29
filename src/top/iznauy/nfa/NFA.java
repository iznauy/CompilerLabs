package top.iznauy.nfa;

import top.iznauy.utils.REs;

import java.util.*;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class NFA {

    private NFAState startState;

    private Map<NFAState, String> endStates;

    public NFA(NFAState startState) {
        this.startState = startState;
        this.endStates = new HashMap<>();
    }

    public NFA(NFAState startState, Map<NFAState, String> endStates) {
        this.startState = startState;
        this.endStates = endStates;
    }

    public NFAState getStartState() {
        return startState;
    }

    public void setStartState(NFAState startState) {
        this.startState = startState;
    }

    public Map<NFAState, String> getEndStates() {
        return endStates;
    }

    public void addEndState(NFAState endState, String type) {
        this.endStates.put(endState, type);
    }

    public static NFA merge(List<NFA> nfaList) {
        assert nfaList != null;
        assert nfaList.size() > 0;

        if (nfaList.size() == 1)
            return nfaList.get(0);

        NFAState startState = new NFAState();
        Map<NFAState, String> endStates = new HashMap<>();

        for (NFA nfa: nfaList) {
            NFAState toState = nfa.getStartState();
            NFAEdge edge = new NFAEdge(null, startState, toState);
            startState.addOutEdge(edge);
            endStates.putAll(nfa.getEndStates());
        }
        return new NFA(startState, endStates);
    }

    public static NFA fromRE() {
        return null;
    }


    @Override
    public String toString() {
        return "NFA{" +
                "startState=" + startState +
                ", endStates=" + endStates +
                '}';
    }
}
