package top.iznauy.re;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public class RE {

    public static List<RENode> fromString(String re) {
        List<RENode> resultList = new LinkedList<>();

        boolean escape = false;
        for (int i = 0; i < re.length(); i++) {
            char ch = re.charAt(i);
            if (escape) {
                escape = false;
                switch (ch) {
                    case 'n':
                        resultList.add(new RENode(RENode.Type.Character, '\n'));
                        break;
                    case 'r':
                        resultList.add(new RENode(RENode.Type.Character, '\r'));
                        break;
                    case 't':
                        resultList.add(new RENode(RENode.Type.Character, '\t'));
                        break;
                    default:
                        resultList.add(new RENode(RENode.Type.Character, ch));
                }
            } else {
                switch (ch) {
                    case '(':
                    case ')':
                    case '|':
                    case '*':
                    case '?':
                        resultList.add(new RENode(RENode.Type.Operator, ch));
                        break;
                    case '[':
                        resultList.add(new RENode(RENode.Type.Operator, '('));
                        i++;
                        while (re.charAt(i) != ']') {
                            char begin = re.charAt(i);
                            i++;
                            if (re.charAt(i) != '-') {
                                throw new RuntimeException();
                            }
                            i++;
                            char end = re.charAt(i);
                            for (char c = begin; c <= end; c++) {
                                resultList.add(new RENode(RENode.Type.Character, c));
                                resultList.add(new RENode(RENode.Type.Operator, '|'));
                            }
                            i++;
                        }
                        resultList.remove(resultList.size() - 1);
                        resultList.add(new RENode(RENode.Type.Operator, ')'));
                        break;
                    case '+':
                        RENode last = resultList.get(resultList.size() - 1);
                        if (last.getType() == RENode.Type.Character) {
                            resultList.add(last);
                            resultList.add(new RENode(RENode.Type.Operator, '*'));
                        } else if (last.getCh() == ')') {
                            int in = 1;
                            for (int j = resultList.size() - 2; j >= 0; j--) {
                                if (resultList.get(j).getType() == RENode.Type.Operator) {
                                    if (resultList.get(j).getCh() == ')')
                                        in++;
                                    else if (resultList.get(j).getCh() == '(')
                                        in--;
                                    if (in == 0) {
                                        resultList.addAll(resultList.subList(j, resultList.size()));
                                        resultList.add(new RENode(RENode.Type.Operator, '*'));
                                        break;
                                    }
                                }
                            }
                        } else {
                            throw new RuntimeException();
                        }
                        break;
                    case '\\':
                        escape = true;
                        break;
                    case '.':
                        // need to finish
                        resultList.add(new RENode(RENode.Type.Operator, '('));
                        for (char c = 32; c <= 126; c++) {
                            resultList.add(new RENode(RENode.Type.Character, c));
                            resultList.add(new RENode(RENode.Type.Operator, '|'));
                        }
                        resultList.add(new RENode(RENode.Type.Character, '\n'));
                        resultList.add(new RENode(RENode.Type.Operator, '|'));
                        resultList.add(new RENode(RENode.Type.Character, '\r'));
                        resultList.add(new RENode(RENode.Type.Operator, '|'));
                        resultList.add(new RENode(RENode.Type.Character, '\t'));

                        resultList.add(new RENode(RENode.Type.Operator, ')'));
                        break;
                    default:
                        resultList.add(new RENode(RENode.Type.Character, ch));
                }
            }

        }
        return resultList;
    }

    public static void main(String[] args) {
        String re = ".*";
        List<RENode> reNodes = fromString(re);
        for (RENode node: reNodes) {
            System.out.print(node);
        }
    }

}
