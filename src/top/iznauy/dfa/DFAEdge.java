package top.iznauy.dfa;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
class DFAEdge {

    private char tag;

    private DFAState fromState;

    private DFAState endState;

    public DFAEdge(char tag, DFAState fromState, DFAState endState) {
        this.tag = tag;
        this.fromState = fromState;
        this.endState = endState;
    }

    public char getTag() {
        return tag;
    }

    public DFAState getFromState() {
        return fromState;
    }

    public DFAState getEndState() {
        return endState;
    }
}
