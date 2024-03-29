package top.iznauy.dfa;

import top.iznauy.nfa.NFAState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
class DFAState {

    private int id;

    private Map<Character, DFAEdge> outEdges;

    private Set<NFAState> nfaStates;

    public DFAState(Set<NFAState> nfaStates) {
        id = DFAStateIDGenerator.getId();
        outEdges = new HashMap<>();
        this.nfaStates = nfaStates;
        DFA.states.put(id, this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addOutEdge(DFAEdge edge) {
        this.outEdges.put(edge.getTag(), edge);
    }

    public Map<Character, DFAEdge> getOutEdges() {
        return outEdges;
    }

    public void setOutEdges(Map<Character, DFAEdge> outEdges) {
        this.outEdges = outEdges;
    }

    public DFAEdge getOutEdge(Character tag) {
        return this.outEdges.get(tag);
    }

    public Set<NFAState> getNfaStates() {
        return nfaStates;
    }

    public void setNfaStates(Set<NFAState> nfaStates) {
        this.nfaStates = nfaStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFAState dfaState = (DFAState) o;

        return id == dfaState.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "DFAState{" +
                "id=" + id +
                ", outEdges=" + outEdges +
                "}";
    }
}
