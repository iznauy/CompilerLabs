package top.iznauy.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
public final class Production {

    public static List<Production> productions = new ArrayList<>();

    private static int count = 0;

    private final int id;

    private final String name;

    private final List<Token> items;

    Production(String name, List<Token> items) {
        this.id = count;
        count++;
        this.name = name;
        this.items = items;
        productions.add(this);
    }

    public String getName() {
        return name;
    }

    List<Token> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    Token getItem(int index) {
        return items.get(index);
    }

    public int getLength() {
        return items.size();
    }

    public boolean follow(List<Token> tokens) {
        return items.equals(tokens);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (Token token: items)
            buffer.append(token);
        return name + " -> " + buffer.toString();
    }
}
