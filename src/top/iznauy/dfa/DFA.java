package top.iznauy.dfa;

import top.iznauy.nfa.NFA;
import top.iznauy.nfa.NFAEdge;
import top.iznauy.nfa.NFAState;
import top.iznauy.utils.REs;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class DFA {

    static Map<Integer, DFAState> states = new HashMap<>();

    private DFAState startState;

    private Map<DFAState, String> endStates;

    public DFA(DFAState startState, Map<DFAState, String> endStates) {
        this.startState = startState;
        this.endStates = endStates;
    }

    public DFAState getStartState() {
        return startState;
    }

    public Map<DFAState, String> getEndStates() {
        return endStates;
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
                        if (result == null || REs.reToPrior.get(tempResult) > REs.reToPrior.get(result)) {// 优先级更高，优先识别
                            result = tempResult;
                        }

                    }
                }
                if (result != null) // 也是DFA的终态
                    dfaEndStates.put(dfaState, result);
            }

            return new DFA(startState, dfaEndStates);
        }
    }

    @Override
    public String toString() {
        return "DFA{\n" +
                "startState=" + startState +
                ", \nendStates=" + endStates +
                '}';
    }

    public static void export(DFA dfa, String targetLocation) {
        targetLocation += "Template.java";
        
        String sourceCode = Template.getTemplate();
        sourceCode = sourceCode.replace("<BeginState>", dfa.startState.getId() + "");

        StringBuilder methodNameListBuilder = new StringBuilder();

        // 向代码中添加结束状态
        Map<DFAState, String> endStates = dfa.getEndStates();
        StringBuilder endStateBuilder = new StringBuilder();
        endStateBuilder.append("public static void initEndState() {\n");
        Map<String, List<Integer>> nameToIndex = new HashMap<>();

        for (String s: endStates.values())
            nameToIndex.computeIfAbsent(s, k -> new ArrayList<>());
        for (Map.Entry<DFAState, String> endState: endStates.entrySet())
            nameToIndex.get(endState.getValue()).add(endState.getKey().getId());
        for (Map.Entry<String, List<Integer>> item: nameToIndex.entrySet()) {
            String name = item.getKey();
            List<Integer> states = item.getValue();
            endStateBuilder.append("addEndState(\"").append(name).append("\"");
            for (Integer integer: states)
                endStateBuilder.append(", ").append(integer);
            endStateBuilder.append(");\n");
        }
        endStateBuilder.append("};\n");
        methodNameListBuilder.append("initEndState();\n");
        sourceCode = sourceCode.replace("<initEndState>", endStateBuilder.toString());

        int nodeCount = 0;
        int methodCount = 0;
        // 向代码中添加转换关系
        StringBuilder builder = new StringBuilder();
        StringBuilder graphBuilder = new StringBuilder();
        for (Map.Entry<Integer, DFAState> stateEntry: states.entrySet()) {
            nodeCount++;
            int id = stateEntry.getKey();
            DFAState state = stateEntry.getValue();
            graphBuilder.append("addDFA(").append(id);
            for (DFAEdge edge: state.getOutEdges().values()) {
                graphBuilder.append(", ").append((int)edge.getTag());
                graphBuilder.append(", ").append(edge.getEndState().getId());
            }
            graphBuilder.append(");\n");
            if (nodeCount % 10 == 0) { // 一个新的方法将要诞生
                methodCount++;
                builder.append("public static void init").append(methodCount).append(" () {\n")
                        .append(graphBuilder.toString()).append("};\n");
                methodNameListBuilder.append("init").append(methodCount).append("();\n");
                graphBuilder.delete(0, graphBuilder.length());
            }
        }
        sourceCode = sourceCode.replace("<initGraph>", builder.toString());

        sourceCode = sourceCode.replace("<init>", methodNameListBuilder.toString());

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(targetLocation))) {
            writer.write(sourceCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
