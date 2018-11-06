package top.iznauy.cfg;

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
                    if (token.getType() == Token.Type.TERMINAL) {
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

    public static Set<LRItem> constructClosure(Set<LRItem> lrItemList) {
        Set<LRItem> resultSet = new HashSet<>(lrItemList);

        return resultSet;
    }

}
