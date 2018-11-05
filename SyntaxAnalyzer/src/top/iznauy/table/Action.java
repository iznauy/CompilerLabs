package top.iznauy.table;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
class Action {

    private Integer next;

    private ActionType actionType;

    public Action(ActionType actionType) {
        next = null;
        this.actionType = actionType;
    }

    public Action(Integer next, ActionType actionType) {
        this.next = next;
        this.actionType = actionType;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public static enum ActionType {
        R, // 规约
        J, // 用在goto部分表示跳转
        S, // 入栈
        ACC, // 接受
        ERROR; // 出错
    }

}
