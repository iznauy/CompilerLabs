package top.iznauy.nfa;

import top.iznauy.re.RENode;

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

    private static class Pair {
        NFAState startState;
        NFAState endState;
    }

    private static Pair concat(Pair pair1, Pair pair2) {
        Pair pair = new Pair();
        pair.startState = pair1.startState;
        pair.endState = pair2.endState;
        List<NFAEdge> outEdges = pair2.endState.getOutEdges();
        for (NFAEdge edge: outEdges) {
            edge.setFromState(pair1.endState);
            pair1.endState.addOutEdge(edge);
        }
        return pair;
    }

    private static Pair charPair(Character c) {
        Pair pair = new Pair();
        pair.startState = new NFAState();
        pair.endState = new NFAState();
        pair.startState.addOutEdge(new NFAEdge(c, pair.startState, pair.endState));
        return pair;
    }

    private static Pair union(Pair pair1, Pair pair2) {
        Pair pair = new Pair();
        NFAState startState = new NFAState();
        NFAState endState = new NFAState();
        pair.startState = startState;
        pair.endState = endState;
        startState.addOutEdge(new NFAEdge(null, startState, pair1.startState));
        startState.addOutEdge(new NFAEdge(null, startState, pair2.startState));
        pair1.endState.addOutEdge(new NFAEdge(null, pair1.endState, endState));
        pair2.endState.addOutEdge(new NFAEdge(null, pair2.endState, endState));
        return pair;
    }

    private static Pair closure(Pair rawPair) {
        Pair pair = new Pair();
        NFAState startState = new NFAState();
        NFAState endState = new NFAState();
        startState.addOutEdge(new NFAEdge(null, startState, rawPair.startState));
        startState.addOutEdge(new NFAEdge(null, startState, endState));
        rawPair.endState.addOutEdge(new NFAEdge(null, rawPair.endState, endState));
        rawPair.endState.addOutEdge(new NFAEdge(null, rawPair.endState, rawPair.startState));
        pair.startState = startState;
        pair.endState = endState;
        return pair;
    }

    public static Pair question(Pair rawPair) {
        Pair pair = charPair(null);
        return concat(rawPair, pair);

    }

    public static NFA fromRE(List<RENode> reNodeList, String target) {
//        List<RENode> extendRENodeList = new ArrayList<>();
//        RENode lastRENode = null;
//        for (RENode reNode: reNodeList) {
//            if (lastRENode == null) {
//                lastRENode = reNode;
//                extendRENodeList.add(reNode);
//                continue;
//            }
//
//            boolean addConcat = false;
//            if (reNode.getType() == RENode.Type.Character && lastRENode.getType() == RENode.Type.Character) // 假如两边都是普通字符
//                 // 这种标志就表示是连接运算
//                addConcat = true;
//            else if (reNode.getType() == RENode.Type.Character && lastRENode.getType() == RENode.Type.Operator &&
//                    lastRENode.getCh() == ')') //"asda)a"，这样的串也需要连接
//                addConcat = true;
//            else if (reNode.getType() == RENode.Type.Operator)
//
//            if (addConcat)
//                extendRENodeList.add(new RENode(RENode.Type.Operator, '.'));
//            extendRENodeList.add(reNode);
//            lastRENode = reNode;
//        }

        Stack<Object> stack = new Stack<>();
        for (RENode reNode: reNodeList) {
            if (reNode.getType() == RENode.Type.Operator) {
                char ch = reNode.getCh();
                switch (ch) {
                    case '(':
                    case '|':
                        stack.push(reNode);
                        break;
                    case '*':
                        Pair pair0 = (Pair) stack.pop();
                        pair0 = closure(pair0);
                        stack.push(pair0);
                        break;
                    case '?':
                        Pair pair1 = (Pair) stack.pop();
                        pair1 = question(pair1);
                        stack.push(pair1);
                        break;
                    case ')':
                        Stack<Object> hold = new Stack<>();
                        Object r = stack.pop();
                        while (r instanceof Pair || (((RENode)r).getCh() != '(' && ((RENode)r).getType() == RENode.Type.Operator)) {
                           hold.push(r); // 理论上这里面没有char类型的RENode，后半句可以不加
                           r = stack.pop();
                        }
                        Pair p = (Pair)hold.pop();
                        while (!hold.empty()) {
                            Object q = hold.pop();
                            if (q instanceof Pair)
                                p = concat(p, (Pair) q);
                            else
                                p = union(p, (Pair) hold.pop());
                        }
                        stack.push(p);
                        break;
                    default:
                        throw new RuntimeException();
                }
            } else {
                stack.push(charPair(reNode.getCh()));
            }
        }
        assert stack.size() == 0;
        Pair pair = (Pair) stack.pop();
        NFA nfa = new NFA(pair.startState);
        nfa.addEndState(pair.endState, target);
        return nfa;
    }


    @Override
    public String toString() {
        return "NFA{" +
                "startState=" + startState +
                ", endStates=" + endStates +
                '}';
    }
}
