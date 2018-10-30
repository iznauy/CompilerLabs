package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.dfa.DFARecognizer;
import top.iznauy.nfa.NFA;
import top.iznauy.re.RE;
import top.iznauy.re.RENode;
import top.iznauy.utils.SourceLoader;
import top.iznauy.utils.TemplateLoader;
import top.iznauy.utils.Token;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // args[0]: .czj file
        // args[1]: source code
        if (args.length < 2) {
            System.out.println("Usage: java Main regular.czj sourceCode [destination]");
            System.out.println("regular.czj: 自定义的文法规则");
            System.out.println("sourceCode: 想要进行词法分析的源程序");
            System.out.println("[destination]: 输出文件");
            return;
        }
        boolean outToFile = false;
        if (args.length == 3)
            outToFile = true;

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
        if (!outToFile) {
            for (Token token: tokens) {
                System.out.println(token);
            }
        } else {
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(args[2]))) {
                for (Token token: tokens) {
                    writer.write(token.toString());
                    writer.write('\n');
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

}
