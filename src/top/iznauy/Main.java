package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.dfa.DFARecognizer;
import top.iznauy.nfa.NFA;
import top.iznauy.utils.Token;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<NFA> nfaList = new ArrayList<>();
        NFA nfa = NFA.merge(nfaList);
        DFA originalDFA = DFA.DFAConverter.fromNFA(nfa);
        DFA dfa = DFA.DFAOptimizer.optimize(originalDFA);
        String content = "";
        DFARecognizer recognizer = new DFARecognizer(dfa, content);
        List<Token> tokenList = recognizer.recognize();
        for (Token token: tokenList) {
            System.out.println(token);
        }

    }
}
