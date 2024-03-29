package top.iznauy.nfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class NFAState {

    private int id;

    private List<NFAEdge> outEdges;

    public NFAState() {
        id = NFAStateIDGenerator.getId();
        outEdges = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public List<NFAEdge> getOutEdges() {
        return outEdges;
    }

    public void addOutEdge(NFAEdge edge) {
        this.outEdges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NFAState nfaState = (NFAState) o;

        return id == nfaState.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "NFAState{" +
                "id=" + id +
                ", outEdges=" + outEdges +
                '}';
    }
}
