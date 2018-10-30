package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.dfa.DFARecognizer;
import top.iznauy.nfa.NFA;
import top.iznauy.re.RE;
import top.iznauy.re.RENode;
import top.iznauy.utils.SourceLoader;
import top.iznauy.utils.TemplateLoader;
import top.iznauy.utils.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // args[0]: .czj file
        // args[1]: source code

        Map<String, String> reMap = TemplateLoader.loadLexicalItems(args[0]);
        String content = SourceLoader.loadSourceCode(args[1]);

        List<NFA> subNFAs = new ArrayList<>();
        for (Map.Entry<String, String> res: reMap.entrySet()) {
            List<RENode> reNodes = RE.fromString(res.getValue());
            NFA nfa = NFA.fromRE(reNodes, res.getKey());
            subNFAs.add(nfa);
        }
        NFA nfa = NFA.merge(subNFAs);

        DFA dfa = DFA.DFAConverter.fromNFA(nfa);
        DFARecognizer recognizer = new DFARecognizer(dfa, content);

        List<Token> tokens = recognizer.recognize();
        for (Token token: tokens) {
            System.out.println(token);
        }
    }

}
