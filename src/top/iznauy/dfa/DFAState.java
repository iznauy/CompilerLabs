package top.iznauy.dfa;

import top.iznauy.nfa.NFAState;

import java.util.*;

/**
 * Created on 2018/10/28.
 * Description:
 *
 * @author iznauy
 */
public class DFAState {

    private int id;

    private Map<Character, DFAEdge> outEdges;

    private Set<NFAState> nfaStates;

    public DFAState(Set<NFAState> nfaStates) {
        id = DFAStateIDGenerator.getId();
        outEdges = new HashMap<>();
        this.nfaStates = nfaStates;
    }

    public DFAState(int id, Map<Character, DFAEdge> outEdges) {
        this.id = id;
        this.outEdges = outEdges;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOutEdges(Map<Character, DFAEdge> outEdges) {
        this.outEdges = outEdges;
    }
    public void addOutEdge(DFAEdge edge) {
        this.outEdges.put(edge.getTag(), edge);
    }

    public Map<Character, DFAEdge> getOutEdges() {
        return outEdges;
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
}
