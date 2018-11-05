package top.iznauy.cfg;

import java.util.List;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
final class Production {

    private String name;

    private List<Token> items;

    Production(String name, List<Token> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Token> getItems() {
        return items;
    }

    public void setItems(List<Token> items) {
        this.items = items;
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
