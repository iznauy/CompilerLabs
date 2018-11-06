package top.iznauy.table;


import top.iznauy.cfg.Production;
import top.iznauy.cfg.Token;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
public class Table {

    private Map<Token, List<Action>> tableColumns;

    private int length = 0;

    public Table(Collection<? extends Token> tokens) {
        tableColumns = new HashMap<>();
        for (Token token: tokens) {
            tableColumns.put(token, new ArrayList<>());
        }
    }

    public void addRow() {
        length++;
        for (List<Action> actions: tableColumns.values()) {
            actions.add(null);
        }
    }

    public Map<Token, List<Action>> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(Map<Token, List<Action>> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public Action getByRowAndCol(int row, Token column) {
        return tableColumns.get(column).get(row);
    }

    public void setByRowAndCol(int row, Token column, Action action) {
        List<Action> actionList = tableColumns.get(column);
        while (row >= actionList.size())
            actionList.add(null);
        actionList.set(row, action);
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableColumns=" + tableColumns +
                ", length=" + length +
                '}';
    }

    public void addAccState() {
        Token token = new Token("$", Token.Type.TERMINAL);
        List<Action> actionList = tableColumns.get(token);
        for (Action action: actionList) {
            if (action != null && action.getNext() == 0 && action.getActionType() == Action.ActionType.R) {
                action.setActionType(Action.ActionType.ACC);
                break;
            }
        }
    }

    public List<Production> parse(List<String> inputSequence) {

        inputSequence.add("$"); // init buffer
        List<Token> inputTokenSequence = inputSequence.stream()
                .map(e -> new Token(e, Token.Type.TERMINAL)).collect(Collectors.toList());
        Stack<Token> inputTokenStack = new Stack<>(); // 装到一个stack里面，会更舒服一点
        for (int i = inputTokenSequence.size() - 1; i >= 0; i--)
                inputTokenStack.push(inputTokenSequence.get(i));

        Stack<Token> tokenStack = new Stack<>(); // init two stack
        tokenStack.push(new Token("$", Token.Type.TERMINAL));
        Stack<Integer> indexStack = new Stack<>();
        indexStack.push(0);

        List<Production> productionSequence = new LinkedList<>();

        boolean success = false;

        while (!inputTokenStack.empty()) {
            int state = indexStack.peek();
            Token token = inputTokenStack.peek();
            Action action = getByRowAndCol(state, token);
            if (action == null)
                throw new RuntimeException("Error");
            int next = action.getNext();
            if (action.getActionType() == Action.ActionType.S || action.getActionType() == Action.ActionType.J) {
                tokenStack.push(inputTokenStack.pop());
                indexStack.push(next);
            } else if (action.getActionType() == Action.ActionType.R) {
                Production production = Production.productions.get(next);
                int length = production.getLength();
                List<Token> popUpTokens = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    popUpTokens.add(0, tokenStack.pop());
                    indexStack.pop();
                }
                if (!production.follow(popUpTokens))
                    throw new RuntimeException("Error");
                productionSequence.add(production);
                inputTokenStack.push(new Token(production.getName(), Token.Type.NON_TERMINAL));
            } else if (action.getActionType() == Action.ActionType.ACC) {
                success = true;
                Production production = Production.productions.get(next);
                productionSequence.add(production);
                break;
            } else {
                throw new RuntimeException("Error");
            }
        }
        if (!success)
            throw new RuntimeException("Error");

        return productionSequence;
    }

}
