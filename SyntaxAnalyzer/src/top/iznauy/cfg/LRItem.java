package top.iznauy.cfg;

import java.util.List;
import java.util.Set;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
final class LRItem extends Production{

    private Set<Token> probe;

    private int pointer; // 指针为k表示在第k个的前面，所以指针后面的元素为items[pointer]

    public LRItem(String name, List<Token> items, Set<Token> probe, int pointer) {
        super(name, items);
        this.probe = probe;
        this.pointer = pointer;
    }

    public LRItem(Set<Token> probe, int pointer, Production production) {
        super(production.getName(), production.getItems());
        this.probe = probe;
        this.pointer = pointer;
    }

    public Set<Token> getProbe() {
        return probe;
    }

    public void setProbe(Set<Token> probe) {
        this.probe = probe;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LRItem lrItem = (LRItem) o;

        if (pointer != lrItem.pointer) return false;
        return probe != null ? probe.equals(lrItem.probe) : lrItem.probe == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (probe != null ? probe.hashCode() : 0);
        result = 31 * result + pointer;
        return result;
    }

    public boolean sameKernel(Object o) {
        return super.equals(o);
    }


}
