package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.dfa.DFARecognizer;
import top.iznauy.dfa.DFAStateIDGenerator;
import top.iznauy.nfa.NFA;
import top.iznauy.nfa.NFAStateIDGenerator;
import top.iznauy.re.RE;
import top.iznauy.re.RENode;
import top.iznauy.utils.REs;
import top.iznauy.utils.Token;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String re = "(a|b)*a(a|b)c";
        String re2 = "e.*f";
        List<RENode> reNodes = RE.fromString(re);
        List<RENode> reNodes1 = RE.fromString(re2);
        String content = "aabaaace1231414faaaac";
        System.out.println();
        NFA nfa = NFA.fromRE(reNodes, REs.OK_1);
        NFA nfa1 = NFA.fromRE(reNodes1, REs.OK_2);
        List<NFA> nfas = new ArrayList<>();
        nfas.add(nfa1);
        nfas.add(nfa);
        nfa = NFA.merge(nfas);
        DFA dfa = DFA.DFAConverter.fromNFA(nfa);
        DFARecognizer recognizer = new DFARecognizer(dfa, content);
        List<Token> tokens = recognizer.recognize();
        for (Token token: tokens) {
            System.out.println(token);
        }
        System.out.println(NFAStateIDGenerator.getId());
        System.out.println(DFAStateIDGenerator.getId());
    }
}
