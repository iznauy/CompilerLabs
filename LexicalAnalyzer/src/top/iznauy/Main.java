package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.nfa.NFA;
import top.iznauy.re.RE;
import top.iznauy.re.RENode;
import top.iznauy.utils.TemplateLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //args = new String[]{"/Users/iznauy/CompilerLabs/example/java.czj", "/Users/iznauy/CompilerLabs/example/"};
        if (args.length < 2) {
            System.out.println("Usage: java Main regular.czj destination_path");
            System.out.println("regular.czj: 自定义的文法规则");
            System.out.println("destinationPath: 生成词法分析器的位置");
            return;
        }
        Map<String, String> reMap = TemplateLoader.loadLexicalItems(args[0]);
        List<NFA> subNFAs = new ArrayList<>();
        for (Map.Entry<String, String> res : reMap.entrySet()) {
            List<RENode> reNodes = RE.fromString(res.getValue());
            NFA nfa = NFA.fromRE(reNodes, res.getKey());
            subNFAs.add(nfa);
        }
        NFA nfa = NFA.merge(subNFAs);

        DFA dfa = DFA.DFAConverter.fromNFA(nfa);
        DFA.export(dfa, args[1]);
    }

}
