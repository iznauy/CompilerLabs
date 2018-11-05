package top.iznauy.dfa;

/**
 * Created on 2018/10/30.
 * Description:
 *
 * @author iznauy
 */
public class Template {

    public static String getTemplate() {
        return "import java.io.BufferedReader;\n" +
                "import java.io.FileReader;\n" +
                "import java.util.*;\n" +
                "\n" +
                "/**\n" +
                " * Created on 2018/10/30.\n" +
                " * Description:\n" +
                " *\n" +
                " * @author iznauy\n" +
                " */\n" +
                "public class Template {\n" +
                "\n" +
                "    public static class Token {\n" +
                "\n" +
                "        public String type;\n" +
                "\n" +
                "        public String code;\n" +
                "\n" +
                "        public Token(String type, String code) {\n" +
                "            this.type = type;\n" +
                "            this.code = code;\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public String toString() {\n" +
                "            return type + \":\\t\\t<'\" + code.trim() + \"'>\";\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private static Map<Integer, String> stateIdToTarget = new HashMap<>();\n" +
                "\n" +
                "    private static Map<Integer, Map<Character, Integer>> dfa = new HashMap<>();\n" +
                "\n" +
                "    private static int beginState = <BeginState>; // 0 will be replaced\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        if (args.length == 0) {\n" +
                "            System.out.println(\"Usage: java Template InputFile\");\n" +
                "            return;\n" +
                "        }\n" +
                "        <init>\n" +
                "        // read source code\n" +
                "        StringBuilder sourceCodeBuilder = new StringBuilder();\n" +
                "        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {\n" +
                "            String line;\n" +
                "            while((line = reader.readLine()) != null)\n" +
                "                sourceCodeBuilder.append(line);\n" +
                "        } catch (Exception e) {\n" +
                "            e.printStackTrace();\n" +
                "            return;\n" +
                "        }\n" +
                "        char[] sourceCode = sourceCodeBuilder.toString().toCharArray();\n" +
                "\n" +
                "        List<Token> tokens = new ArrayList<>();\n" +
                "        int pointer = 0;\n" +
                "        Set<Integer> endStates = stateIdToTarget.keySet();\n" +
                "\n" +
                "        StringBuffer beforeEndState = new StringBuffer();\n" +
                "        StringBuffer afterEndState = new StringBuffer();\n" +
                "\n" +
                "        int currentState = beginState;\n" +
                "        boolean toEnd = false;\n" +
                "        String endType = null;\n" +
                "\n" +
                "        if (endStates.contains(currentState)) {\n" +
                "            toEnd = true;\n" +
                "            endType = stateIdToTarget.get(currentState);\n" +
                "        }\n" +
                "\n" +
                "        while (true) {\n" +
                "            if (pointer >= sourceCode.length) {\n" +
                "                if (!toEnd && beforeEndState.length() == 0)\n" +
                "                    break;\n" +
                "                else if (!toEnd && beforeEndState.length() > 0) {\n" +
                "                    throw new RuntimeException(\"UnFinished Program\");\n" +
                "                } else {\n" +
                "                    Token token = new Token(endType, beforeEndState.toString());\n" +
                "                    tokens.add(token);\n" +
                "                    beforeEndState.delete(0, beforeEndState.length());\n" +
                "                    if (afterEndState.length() == 0)\n" +
                "                        break;\n" +
                "                    else {\n" +
                "                        toEnd = false;\n" +
                "                        pointer -= afterEndState.length();\n" +
                "                        afterEndState.delete(0, afterEndState.length());\n" +
                "                        currentState = beginState;\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            char currentChar = sourceCode[pointer];\n" +
                "            pointer++;\n" +
                "\n" +
                "            Integer nextState = dfa.get(currentState).get(currentChar);\n" +
                "            if (nextState == null) {\n" +
                "                if (!toEnd)\n" +
                "                    throw new RuntimeException(\"Error\");\n" +
                "                else {\n" +
                "                    toEnd = false;\n" +
                "                    Token token = new Token(endType, beforeEndState.toString());\n" +
                "                    tokens.add(token);\n" +
                "                    pointer -= afterEndState.length();\n" +
                "                    pointer--;\n" +
                "                    beforeEndState.delete(0, beforeEndState.length());\n" +
                "                    afterEndState.delete(0, afterEndState.length());\n" +
                "                    currentState = beginState;\n" +
                "                }\n" +
                "            } else {\n" +
                "                currentState = nextState;\n" +
                "                if (toEnd)\n" +
                "                    afterEndState.append(currentChar);\n" +
                "                else\n" +
                "                    beforeEndState.append(currentChar);\n" +
                "\n" +
                "                if (endStates.contains(currentState)) {\n" +
                "                    toEnd = true;\n" +
                "                    endType = stateIdToTarget.get(currentState);\n" +
                "                    beforeEndState.append(afterEndState.toString());\n" +
                "                    afterEndState.delete(0, afterEndState.length());\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        for (Token token: tokens) {\n" +
                "            System.out.println(token);\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    private static void addDFA(int state, int ... params) {\n" +
                "        Map<Character, Integer> nextStates = new HashMap<>();\n" +
                "        for (int i = 0; i < params.length / 2; i++) {\n" +
                "            char ch = (char) params[i * 2];\n" +
                "            int next = params[2 * i + 1];\n" +
                "            nextStates.put(ch, next);\n" +
                "        }\n" +
                "        dfa.put(state, nextStates);\n" +
                "    }\n" +
                "\n" +
                "    private static void addEndState(String name, int ... states) {\n" +
                "        for (int state: states) {\n" +
                "            stateIdToTarget.put(state, name);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    <initEndState>\n" +
                "\n" +
                "    <initGraph>\n" +
                "}";
    }

}
