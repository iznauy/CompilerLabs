package top.iznauy;

import top.iznauy.dfa.DFAStateIDGenerator;
import top.iznauy.nfa.NFAStateIDGenerator;

public class Main {

    public static void main(String[] args) {
	// write your code here
        NFAStateIDGenerator.init();
        DFAStateIDGenerator.init();
    }
}
