package top.iznauy.cfg;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
final class Token {

    public static enum Type {
        TERMINAL,
        NON_TERMINAL,
        START
    }

    private final String content;

    private final Type type;

    public Token(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (content != null ? !content.equals(token.content) : token.content != null) return false;
        return type == token.type;
    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
