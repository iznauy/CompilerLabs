package top.iznauy.table;


import top.iznauy.cfg.Token;

import java.util.*;

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
}
