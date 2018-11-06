package top.iznauy.cfg;

import java.util.List;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
final class Production {

    private static int count = 0;

    private final int id;

    private final String name;

    private final List<Token> items;

    Production(String name, List<Token> items) {
        this.id = count;
        count++;
        this.name = name;
        this.items = items;
    }

    String getName() {
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
        return "Production{" +
                "name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
