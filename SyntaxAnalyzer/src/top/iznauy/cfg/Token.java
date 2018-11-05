package top.iznauy.cfg;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
class Token {

    public static enum Type {
        TERMINAL,
        NON_TERMINAL,
        START
    }

    public Token(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    private String name;

    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (name != null ? !name.equals(token.name) : token.name != null) return false;
        return type == token.type;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
