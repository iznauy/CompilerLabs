package top.iznauy.nfa;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class NFAEdge {
    // may be null in nfa
    private Character tag;

    private NFAState fromState;

    private NFAState toState;

    public NFAEdge(Character tag, NFAState fromState, NFAState toState) {
        this.tag = tag;
        this.fromState = fromState;
        this.toState = toState;
    }

    public Character getTag() {
        return tag;
    }

    public void setTag(Character tag) {
        this.tag = tag;
    }

    public NFAState getFromState() {
        return fromState;
    }

    public void setFromState(NFAState fromState) {
        this.fromState = fromState;
    }

    public NFAState getToState() {
        return toState;
    }

    public void setToState(NFAState toState) {
        this.toState = toState;
    }

    @Override
    public String toString() {
        return "NFAEdge{" +
                "tag=" + tag +
                ", fromState=" + fromState.getId() +
                ", toState=" + toState.getId() +
                '}';
    }
}
