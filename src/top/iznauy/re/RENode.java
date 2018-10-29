package top.iznauy.re;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public final class RENode {

    private final Type type;

    private final char ch;

    public RENode(Type type, char ch) {
        this.type = type;
        this.ch = ch;
    }

    public Type getType() {
        return type;
    }


    public char getCh() {
        return ch;
    }

    public enum Type {
        Character, Operator
    }

    @Override
    public String toString() {
        return ch + "";
    }

}
