package top.iznauy.cfg;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
final class LRItem {

    private final Set<String> probe; // 展望符集合

    private final int pointer; // 指针为k表示在第k个的前面，所以指针后面的元素为items[pointer]

    private final Production production; // 对应的产生式

    public LRItem(Set<String> probe, int pointer, Production production) {
        this.production = production;
        this.probe = probe;
        this.pointer = pointer;
    }

    public void addProbe(String token) {
        this.probe.add(token);
    }

    public Set<String> getProbe() {
        return probe;
    }

    public int getPointer() {
        return pointer;
    }

    public Production getProduction() {
        return production;
    }

    public Token getByPointer(int pointer) {
        return production.getItem(pointer);
    }

    public Token nextAcceptToken() {
        return production.getItem(pointer);
    }

    public LRItem move() {
        Set<String> probeCopy = new HashSet<>(probe);
        return new LRItem(probeCopy, pointer + 1, production);
    }

    public boolean toEnd() { // 判断是否进入可规约状态
        return production.getItems().size() == pointer;
    }

    public int getProductionSize() {
        return production.getItems().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LRItem item = (LRItem) o;

        if (pointer != item.pointer) return false;
        return production != null ? production.equals(item.production) : item.production == null;
    }

    @Override
    public int hashCode() {
        int result = pointer;
        result = 31 * result + (production != null ? production.hashCode() : 0);
        return result;
    }
}
