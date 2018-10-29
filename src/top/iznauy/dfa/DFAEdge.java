package top.iznauy.dfa;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class DFAEdge {

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

    public void setTag(char tag) {
        this.tag = tag;
    }

    public DFAState getFromState() {
        return fromState;
    }

    public void setFromState(DFAState fromState) {
        this.fromState = fromState;
    }

    public DFAState getEndState() {
        return endState;
    }

    public void setEndState(DFAState endState) {
        this.endState = endState;
    }
}
