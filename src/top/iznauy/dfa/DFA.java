package top.iznauy.dfa;

import top.iznauy.nfa.NFA;
import top.iznauy.nfa.NFAEdge;
import top.iznauy.nfa.NFAState;
import top.iznauy.utils.REs;

import java.util.*;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class DFA {

    private DFAState startState;

    private Map<DFAState, String> endStates;

    public DFA(DFAState startState) {
        this.startState = startState;
        this.endStates = new HashMap<>();
    }

    public DFA(DFAState startState, Map<DFAState, String> endStates) {
        this.startState = startState;
        this.endStates = endStates;
    }

    public DFAState getStartState() {
        return startState;
    }

    public void setStartState(DFAState startState) {
        this.startState = startState;
    }

    public Map<DFAState, String> getEndStates() {
        return endStates;
    }

    public void setEndStates(Map<DFAState, String> endStates) {
        this.endStates = endStates;
    }


    public static class DFAConverter {

        private static Set<NFAState> findEpsilonClosure(NFAState state) {
            Set<NFAState> states = new HashSet<>();
            states.add(state);
            return findEpsilonClosure(states);
        }

        private static Set<NFAState> findEpsilonClosure(Collection<? extends NFAState> states) {
            Set<NFAState> resultSet = new HashSet<>();
            // init stack
            Stack<NFAState> stateStack = new Stack<>();
            for (NFAState state: states)
                stateStack.push(state);

            // find Closure
            while (stateStack.size() != 0) {
                NFAState currentState = stateStack.pop();
                if (resultSet.contains(currentState)) // 如果不在集合中，说明已经充分探索，就把它丢掉好了，不过性能上存在着优化的空间
                    continue; // 可以保证每个节点只被拓展一次，但无法保证只被入栈一次
                resultSet.add(currentState);
                for (NFAEdge edge: currentState.getOutEdges()) {
                    if (edge.getTag() == null)
                        stateStack.push(edge.getToState());
                }
            }
            return resultSet;
        }

        private static Set<Character> getPossibleNextTag(Collection<? extends NFAState> states) {
            Set<Character> possibleChars = new HashSet<>();
            for (NFAState state: states) {
                for (NFAEdge edge: state.getOutEdges()) {
                    if (edge.getTag() == null)
                        continue;
                    possibleChars.add(edge.getTag());
                }
            }
            return possibleChars;
        }

        private static Set<NFAState> findNextStateSet(Collection<? extends NFAState> states, char tag) {
            Set<NFAState> resultSet = new HashSet<>();
            for (NFAState state: states) {
                for (NFAEdge edge: state.getOutEdges()) {
                    if (edge.getTag()!= null && edge.getTag() == tag) {
                        resultSet.add(edge.getToState());
                    }
                }
            }
            return resultSet;
        }

        public static DFA fromNFA(NFA nfa) {

            Set<NFAState> startStates = findEpsilonClosure(nfa.getStartState());
            Map<Set<NFAState>, DFAState> nFAToDFAMap = new HashMap<>();
            DFAState startState = new DFAState(startStates);
            nFAToDFAMap.put(startStates, startState);

            Queue<DFAState> dFAQueue = new LinkedList<>();
            dFAQueue.add(startState);

            while (dFAQueue.size() > 0) {
                DFAState state = dFAQueue.poll();
                Set<Character> possibleNextTags = getPossibleNextTag(state.getNfaStates());
                for (char tag: possibleNextTags) {
                    Set<NFAState> epsilonClosure = findEpsilonClosure(findNextStateSet(state.getNfaStates(), tag));
                    DFAState nextState = nFAToDFAMap.get(epsilonClosure);
                    if (nextState == null) { // 这是一个没有遇到的状态
                        nextState = new DFAState(epsilonClosure); // 新建一个状态
                        nFAToDFAMap.put(epsilonClosure, nextState); // 加入映射表
                        dFAQueue.add(nextState); // 加入queue
                    }
                    // 现在这个状态不是空的
                    DFAEdge edge = new DFAEdge(tag, state, nextState); // 加入一条新的边
                    state.addOutEdge(edge);
                }
            }

            Collection<DFAState> dfaStates = nFAToDFAMap.values();

            Map<DFAState, String> dfaEndStates = new HashMap<>();
            Map<NFAState, String> nfaEndStates = nfa.getEndStates();

            for (DFAState dfaState: dfaStates) {
                Set<NFAState> nfaStates = dfaState.getNfaStates();
                String result = null;
                for (NFAState nfaState: nfaStates) {
                    if (nfaEndStates.keySet().contains(nfaState)) {
                        // 假如确实是一个原本NFA的终态
                        String tempResult = nfaEndStates.get(nfaState);
                        if (result == null || REs.reToPrior.get(tempResult) > REs.reToPrior.get(result)) // 优先级更高，优先识别
                            result = tempResult;
                    }
                }
                if (result != null) // 也是DFA的终态
                    dfaEndStates.put(dfaState, result);
            }

            return new DFA(startState, dfaEndStates);
        }
    }

    // debug
    public static void main(String[] args) {
        NFAState state1 = new NFAState();
        NFAState state2 = new NFAState();
        NFAEdge edge1 = new NFAEdge('a', state1, state2);
        state1.addOutEdge(edge1);
        NFA nfa1 = new NFA(state1);
        nfa1.addEndState(state2, REs.OK_1);

        NFAState state3 = new NFAState();
        NFAState state4 = new NFAState();
        NFAEdge edge2 = new NFAEdge('b', state3, state4);
        state3.addOutEdge(edge2);
        NFA nfa2 = new NFA(state3);
        nfa2.addEndState(state4, REs.OK_2);

        List<NFA> nfas = new ArrayList<>();
        nfas.add(nfa1);
        nfas.add(nfa2);

        NFA mergedNFA = NFA.merge(nfas);

        DFA dfa = DFAConverter.fromNFA(mergedNFA);

        System.out.println(dfa);

    }

    @Override
    public String toString() {
        return "DFA{\n" +
                "startState=" + startState +
                ", \nendStates=" + endStates +
                '}';
    }
}
