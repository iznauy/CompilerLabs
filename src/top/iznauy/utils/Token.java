package top.iznauy.utils;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public class Token {

    private String type;

    private String code;

    public Token(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
