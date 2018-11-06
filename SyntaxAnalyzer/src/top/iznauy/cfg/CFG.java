package top.iznauy.cfg;

import sun.tools.jconsole.Tab;
import top.iznauy.table.Action;
import top.iznauy.table.Table;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
public final class CFG {

    private String startSymbol;

    private Map<String, List<Production>> productionMap;

    private Set<String> terminals;

    private Map<String, Set<String>> first;

    private CFG(String startSymbol, Map<String, List<Production>> productionMap, Set<String> terminals) {
        this.startSymbol = startSymbol;
        this.productionMap = productionMap;
        this.terminals = terminals;
        this.first = new HashMap<>();
        this.calculateFirstSet(); // 计算
    }

    private void calculateFirstSet() {
        for (String name: productionMap.keySet())
            first.put(name, new HashSet<>());

        boolean changed = true;
        while (changed) {
            changed = false;
            for (String name: first.keySet()) {
                List<Production> productions = productionMap.get(name);
                for (Production production: productions) {
                    Token token = production.getItem(0); // 获取第一个token
                    Set<String> firstSet = first.get(name);
                    if (token.getType() == Token.Type.TERMINAL && !firstSet.contains(token.getContent())) {
                        firstSet.add(token.getContent()); // 如果是一个终结符，就加进去
                        changed = true;
                    } else if (token.getType() == Token.Type.NON_TERMINAL) { // 对于非终结符
                        int preSize = firstSet.size();
                        firstSet.addAll(first.get(token.getContent()));

                        if (preSize != firstSet.size())
                            changed = true;
                    }
                }
            }
        }
    }


    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public Map<String, List<Production>> getProductionMap() {
        return productionMap;
    }

    public void setProductionMap(Map<String, List<Production>> productionMap) {
        this.productionMap = productionMap;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CFG cfg = (CFG) o;

        if (startSymbol != null ? !startSymbol.equals(cfg.startSymbol) : cfg.startSymbol != null) return false;
        if (productionMap != null ? !productionMap.equals(cfg.productionMap) : cfg.productionMap != null) return false;
        return terminals != null ? terminals.equals(cfg.terminals) : cfg.terminals == null;
    }

    @Override
    public int hashCode() {
        int result = startSymbol != null ? startSymbol.hashCode() : 0;
        result = 31 * result + (productionMap != null ? productionMap.hashCode() : 0);
        result = 31 * result + (terminals != null ? terminals.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CFG{" +
                "startSymbol='" + startSymbol + '\'' +
                ", productionMap=" + productionMap +
                ", terminals=" + terminals +
                ", first=" + first +
                '}';
    }

    public static CFG parse(String startSymbol, Set<String> nonTerminalSymbols, List<String> productionStrings) {
        List<Production> productions = new ArrayList<>(productionStrings.size());
        Set<String> terminals = new HashSet<>();
        for (String productionString: productionStrings) {
            String[] splits = productionString.split("->");

            if (splits.length != 2)
                throw new RuntimeException("不正确的产生式格式");

            String name = splits[0].trim();
            if (!nonTerminalSymbols.contains(name) && !name.equals(startSymbol))
                throw new RuntimeException("非终结符未全部给出");

            String[] rights = splits[1].trim().split(" ");
            List<Token> tokenList = new ArrayList<>();

            for (String right: rights) {
                if (nonTerminalSymbols.contains(right))
                    tokenList.add(new Token(right, Token.Type.NON_TERMINAL));
                else {
                    tokenList.add(new Token(right, Token.Type.TERMINAL));
                    terminals.add(right);
                }
            }

            Production production = new Production(name, tokenList);
            productions.add(production);
        }
        Map<String, List<Production>> productionMap = productions.stream()
                .collect(Collectors.groupingBy(Production::getName));

        return new CFG(startSymbol, productionMap, terminals);
    }

    public Set<LRItem> move(Set<LRItem> lrItemList, Token token) {
        Set<LRItem> resultSet = new HashSet<>();
        for (LRItem item: lrItemList) {
            Token nextToken = item.nextAcceptToken();
            if (nextToken.equals(token)) // 正好是
                resultSet.add(item.move());
        }
        return resultSet.size() > 0 ? resultSet : null; // 假如没有东西，那就返回null
    }

    public Set<LRItem> constructClosure(Set<LRItem> lrItemList) {
        Set<LRItem> resultSet = new HashSet<>(lrItemList);
        boolean changed = true;
        while (changed) {
            changed = false;
            List<LRItem> tempResultList = new ArrayList<>();
            for (LRItem item: resultSet) {
                if (item.toEnd())
                    continue; // 表示是一个规约项，那么就不考虑
                Token nextToken = item.nextAcceptToken();
                if (nextToken.getType() == Token.Type.TERMINAL) // 这一项是一个终结符
                    continue;

                // 是一个非终结符
                Set<String> probeSet = new HashSet<>();
                String content = nextToken.getContent();
                int pointer = item.getPointer();
                int length = item.getProductionSize();
                if (pointer == length - 1) { // 也就是当前读取的是最后一个字符，就是A --> ·B, xx
                                                // 那么probe集合就是当前项的probe集合
                    probeSet.addAll(item.getProbe());
                } else {
                    Token token = item.getByPointer(pointer + 1); // A --> ·BC，这儿得到的是C
                    if (token.getType() == Token.Type.TERMINAL)
                        probeSet.add(token.getContent());
                    else
                        probeSet.addAll(first.get(token.getContent()));
                } // probe集合已经生成完毕了

                List<Production> productions = productionMap.get(content); // 拿到非终结符的所有产生式

                for (Production production: productions) {
                    boolean has = false;
                    for (LRItem innerItem: resultSet) { // 对于每一个产生式，查看历史结果集是不是已经有了这样的式子
                        if (innerItem.getProduction().equals(production) && innerItem.getPointer() == 0) { // 已经有了
                            has = true;
                            if (!innerItem.getProbe().containsAll(probeSet)) { // 但是没有全部的项目
                                changed = true;

                                innerItem.getProbe().addAll(probeSet);
                                break;
                            }
                        }
                    }
                    if (!has) {
                         // 或者干脆没有
                        LRItem tempNewItem = new LRItem(probeSet, 0, production);
                        changed = true;
                        boolean news = true;
                        for (LRItem otherNewItem: tempResultList) {
                            if (otherNewItem.equals(tempNewItem)) {
                                otherNewItem.getProbe().addAll(probeSet);
                                news = false;
                                break;
                            }
                        }
                        if (news)
                            tempResultList.add(tempNewItem);

                    }
                }
            }
            resultSet.addAll(tempResultList);
        }
        return resultSet;
    }

    public Set<LRItem> initStartSet() {
        Set<String> temp = new HashSet<>();
        temp.add("$");
        LRItem initLRItem = new LRItem(temp, 0, this.productionMap.get(this.startSymbol).get(0));
        Set<LRItem> initLRItems = new HashSet<>();
        initLRItems.add(initLRItem);
        return initLRItems;
    }

    public Table exportTable() {
        // 初始化开始状态
        Set<LRItem> startLRItem = initStartSet();
        startLRItem = constructClosure(startLRItem);
        List<Set<LRItem>> itemSetList = new ArrayList<>();
        itemSetList.add(startLRItem);
        List<Token> allTokens = terminals.stream().map(e -> new Token(e, Token.Type.TERMINAL))
                .collect(Collectors.toList());
        allTokens.add(new Token("$", Token.Type.TERMINAL));
        allTokens.addAll(productionMap.keySet().stream().map(e -> new Token(e, Token.Type.NON_TERMINAL))
                .collect(Collectors.toList()));
        Table table = new Table(allTokens);
        table.addRow();
        for (int i = 0; i < itemSetList.size(); i++) {
            Set<LRItem> curLRItems = itemSetList.get(i);
            // 首先计算出出边
            for (Token inputToken: allTokens) {
                Set<LRItem> afterMoveSet = move(curLRItems, inputToken);
                if (afterMoveSet == null)
                    continue;
                // 假如移动后不为空，就说明是一个合法的input
                afterMoveSet = constructClosure(afterMoveSet);
                boolean has = false;
                for (int j = 0; j < itemSetList.size(); j++) {
                    if (afterMoveSet.equals(itemSetList.get(j))) {
                        // 如果是已经有的状态
                        if (inputToken.getType() == Token.Type.TERMINAL) // 终结符的话，是移入栈中
                            table.setByRowAndCol(i, inputToken, new Action(j, Action.ActionType.S));
                        else
                            table.setByRowAndCol(i, inputToken, new Action(j, Action.ActionType.J));
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    // 假如是没有的状态
                    table.addRow();
                    int index = itemSetList.size();
                    itemSetList.add(afterMoveSet);
                    if (inputToken.getType() == Token.Type.TERMINAL) // 终结符的话，是移入栈中
                        table.setByRowAndCol(i, inputToken, new Action(index, Action.ActionType.S));
                    else
                        table.setByRowAndCol(i, inputToken, new Action(index, Action.ActionType.J));
                }
            }
            // 上面已经计算完毕了所有的出边
            // 下面计算规约
            for (LRItem item: curLRItems) {
                if (item.toEnd()) { // 可以规约啦！
                    for (String content: item.getProbe()) {
                        Token token = new Token(content, Token.Type.TERMINAL);
                        table.setByRowAndCol(i, token, new Action(item.getProduction().getId(),
                                Action.ActionType.R));
                    }
                }
            }
        }
        return table;
    }
}
