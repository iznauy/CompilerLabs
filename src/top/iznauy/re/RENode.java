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

    @Override
    public String toString() {
        if (type == Type.Character)
            return ch + "";
        else
            return "\\" + ch;
    }

    public enum Type {
        Character, Operator
    }

}
