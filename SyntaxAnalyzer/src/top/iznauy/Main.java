package top.iznauy;

import top.iznauy.cfg.CFG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
public class Main {

    public static void main(String[] args) {
        String CFG_File_Path = "/Users/iznauy/CompilerLabs/SyntaxAnalyzer/test.cfg";
        String startSymbol = null;
        Set<String> nonTerminalSymbols = new HashSet<>();
        List<String> productionStrings = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CFG_File_Path))) {
            String line;
            startSymbol = bufferedReader.readLine();
            int state = 0;
            bufferedReader.readLine(); // read %%
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (line.equals("%%")) {
                        state = 1;
                        continue;
                    }
                    if (state == 0)
                        nonTerminalSymbols.add(line);
                    else
                        productionStrings.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CFG文件读取出错。");
            return;
        }
        CFG cfg = CFG.parse(startSymbol, nonTerminalSymbols, productionStrings);
        System.out.println(cfg);
    }

}
