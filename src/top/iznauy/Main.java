package top.iznauy;

import top.iznauy.dfa.DFA;
import top.iznauy.dfa.DFARecognizer;
import top.iznauy.dfa.DFAStateIDGenerator;
import top.iznauy.nfa.NFA;
import top.iznauy.nfa.NFAStateIDGenerator;
import top.iznauy.re.RE;
import top.iznauy.re.RENode;
import top.iznauy.utils.TemplateLoader;
import top.iznauy.utils.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String filePath = "/Users/iznauy/CompilerLabs/java.czj";
        Map<String, String> res = TemplateLoader.loadLexicalItems(filePath);
        List<NFA> nfaList = new ArrayList<>();
        for (Map.Entry<String, String> re: res.entrySet()) {
            List<RENode> reNodeList = RE.fromString(re.getValue());
            nfaList.add(NFA.fromRE(reNodeList, re.getKey()));
        }
        NFA nfa = NFA.merge(nfaList);
        DFA dfa = DFA.DFAConverter.fromNFA(nfa);
        DFARecognizer recognizer = new DFARecognizer(dfa, getString2());
        List<Token> tokens = recognizer.recognize();
        for (Token token: tokens) {
            System.out.println(token);
        }
        System.out.println(NFAStateIDGenerator.getId());
        System.out.println(DFAStateIDGenerator.getId());
    }

    public static String getString() {
        return "while (true) { // i bought a watch last year\n" +
                "    if (x == 5)\n" +
                "        print(x);\n" +
                "    else\n" +
                "        print(x + 1);\n" +
                "}";
    }

    static String getString2() {
        return "\n" +
                "package retonfa;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.Stack;\n" +
                "import java.util.Scanner;\n" +
                "\n" +
                "public class ReToNfa {\n" +
                "\n" +
                "    public static void main(String[] args) { // test comment \n" +
                "        System.out.println(\"2333\\\\\"\");\n" +
                "        Scanner s = new Scanner(System.in);\n" +
                "        String str = s.nextLine();\n" +
                "        Nfa c = new Nfa(str);\n" +
                "        c.display();\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "class Edge {\n" +
                "\n" +
                "    private final int fromState;\n" +
                "    private final int toState;\n" +
                "    private final char transVal;\n" +
                "\n" +
                "    public Edge(int fs, char t, int ts) {\n" +
                "        fromState = fs;\n" +
                "        transVal = t;\n" +
                "        toState = ts;\n" +
                "    }\n" +
                "\n" +
                "    public Edge(int fs, int ts) {\n" +
                "        fromState = fs;\n" +
                "        transVal = 'e';\n" +
                "        toState = ts;\n" +
                "    }\n" +
                "\n" +
                "    public int getFromState() {\n" +
                "        return fromState;\n" +
                "    }\n" +
                "\n" +
                "    public int getToState() {\n" +
                "        return toState;\n" +
                "    }\n" +
                "\n" +
                "    public char getTransVal() {\n" +
                "        return transVal;\n" +
                "    }\n" +
                "\n" +
                "    public void display() {\n" +
                "       \n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class Nfa {\n" +
                "\n" +
                "    private final ArrayList<Edge> edges;\n" +
                "    private final int stateCount;\n" +
                "\n" +
                "    public Nfa() {\n" +
                "        stateCount = 1;\n" +
                "        edges = new ArrayList();\n" +
                "        Edge e = new Edge(0, 1);\n" +
                "        edges.add(e);\n" +
                "    }\n" +
                "\n" +
                "    public Nfa(char ch) {\n" +
                "        stateCount = 1;\n" +
                "        edges = new ArrayList();\n" +
                "        Edge e = new Edge(0, ch, 1);\n" +
                "        edges.add(e);\n" +
                "    }\n" +
                "\n" +
                "    public Nfa(ArrayList<Edge> es, int c) {\n" +
                "        edges = es;\n" +
                "        stateCount = c;\n" +
                "    }\n" +
                "\n" +
                "    public ArrayList<Edge> getEdges() {\n" +
                "        return edges;\n" +
                "    }\n" +
                "\n" +
                "    public int getStateCount() {\n" +
                "        return stateCount;\n" +
                "    }\n" +
                "\n" +
                "    public static Nfa union(Nfa a, Nfa b) {\n" +
                "        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());\n" +
                "        ArrayList<Edge> edgesb = new ArrayList<>(b.getEdges());\n" +
                "        ArrayList<Edge> edgesc = new ArrayList<>();\n" +
                "        int acount = a.getStateCount();\n" +
                "        int bcount = b.getStateCount();\n" +
                "\n" +
                "        for (Edge e : edgesa) {\n" +
                "            Edge p = new Edge(e.getFromState() + 1, e.getTransVal(), e.getToState() + 1);\n" +
                "            edgesc.add(p);\n" +
                "        }\n" +
                "\n" +
                "        Edge starttoa = new Edge(0, 1);\n" +
                "        edgesc.add(starttoa);\n" +
                "\n" +
                "        for (Edge e : edgesb) {\n" +
                "            Edge p = new Edge(e.getFromState() + acount + 2, e.getTransVal(), e.getToState() + acount + 2);\n" +
                "            edgesc.add(p);\n" +
                "        }\n" +
                "\n" +
                "        Edge starttob = new Edge(0, acount + 2);\n" +
                "        edgesc.add(starttob);\n" +
                "\n" +
                "        Edge atoend = new Edge(acount + 1, acount + bcount + 3);\n" +
                "        edgesc.add(atoend);\n" +
                "\n" +
                "        Edge btoend = new Edge(acount + bcount + 2, acount + bcount + 3);\n" +
                "        edgesc.add(btoend);\n" +
                "\n" +
                "        Nfa c = new Nfa(edgesc, acount + bcount + 3);\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "    public static Nfa concat(Nfa a, Nfa b) {\n" +
                "        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());\n" +
                "        ArrayList<Edge> edgesb = new ArrayList<>(b.getEdges());\n" +
                "        ArrayList<Edge> edgesc = new ArrayList<>();\n" +
                "\n" +
                "        int acount = a.getStateCount();\n" +
                "        int bcount = b.getStateCount();\n" +
                "\n" +
                "        for (Edge e : edgesa) {\n" +
                "            Edge p = new Edge(e.getFromState(), e.getTransVal(), e.getToState());\n" +
                "            edgesc.add(p);\n" +
                "        }\n" +
                "        for (Edge e : edgesb) {\n" +
                "            Edge p = new Edge(e.getFromState() + acount, e.getTransVal(), e.getToState() + acount);\n" +
                "            edgesc.add(p);\n" +
                "        }\n" +
                "\n" +
                "        Nfa c = new Nfa(edgesc, acount + bcount);\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "    public static Nfa closure(Nfa a) {\n" +
                "        ArrayList<Edge> edgesa = new ArrayList<>(a.getEdges());\n" +
                "        ArrayList<Edge> edgesc = new ArrayList<>();\n" +
                "        int acount = a.getStateCount();\n" +
                "\n" +
                "        for (Edge e : edgesa) {\n" +
                "            Edge p = new Edge(e.getFromState() + 1, e.getTransVal(), e.getToState() + 1);\n" +
                "            edgesc.add(p);\n" +
                "        }\n" +
                "\n" +
                "        Edge endtostart = new Edge(acount + 1, 1);\n" +
                "        edgesc.add(endtostart);\n" +
                "\n" +
                "        Edge nstarttostart = new Edge(0, 1);\n" +
                "        edgesc.add(nstarttostart);\n" +
                "\n" +
                "        Edge endtonend = new Edge(acount + 1, acount + 2);\n" +
                "        edgesc.add(endtonend);\n" +
                "\n" +
                "        Edge nstarttonend = new Edge(0, acount + 2);\n" +
                "        edgesc.add(nstarttonend);\n" +
                "\n" +
                "        Nfa c = new Nfa(edgesc, acount + 2);\n" +
                "\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "    public void display() {\n" +
                "    }\n" +
                "\n" +
                "    public Nfa(String str) {\n" +
                "        Nfa c = Nfa.reToNfa(str);\n" +
                "        edges = c.getEdges();\n" +
                "        stateCount = c.getStateCount();\n" +
                "    }\n" +
                "\n" +
                "    private static Nfa reToNfa(String re) {\n" +
                "        Stack<Object> stack = new Stack<>();\n" +
                "        for (int i = 0; i < re.length(); i++) {\n" +
                "            switch (re.charAt(i)) {\n" +
                "                case '(':\n" +
                "                    stack.push('(');\n" +
                "                    break;\n" +
                "                case '|':\n" +
                "                    stack.push('|');\n" +
                "                    break;\n" +
                "                case '*':\n" +
                "                    Nfa x = Nfa.closure((Nfa) stack.pop());\n" +
                "                    stack.push(x);\n" +
                "                    break;\n" +
                "                case ')':\n" +
                "                    Stack<Object> hold = new Stack<>();\n" +
                "                    Object r = stack.pop();\n" +
                "                    while (r instanceof Nfa || (char) r != '(') {\n" +
                "                        hold.push(r);\n" +
                "                        r = stack.pop();\n" +
                "                    }\n" +
                "                    Object p = hold.pop();\n" +
                "                    while (!hold.empty()) {\n" +
                "                        Object q = hold.pop();\n" +
                "                        if (!(q instanceof Nfa)) {\n" +
                "                            p = Nfa.union((Nfa) p, (Nfa) hold.pop());\n" +
                "                        } else {\n" +
                "                            p = Nfa.concat((Nfa) p, (Nfa) q);\n" +
                "                        }\n" +
                "                    }\n" +
                "                    stack.push((Nfa) p);\n" +
                "                    break;\n" +
                "                default:\n" +
                "                    Nfa one = new Nfa(re.charAt(i));\n" +
                "                    stack.push(one);\n" +
                "                    break;\n" +
                "            }\n" +
                "        }\n" +
                "        return (Nfa) stack.pop();\n" +
                "    }\n" +
                "}";
    }
}
