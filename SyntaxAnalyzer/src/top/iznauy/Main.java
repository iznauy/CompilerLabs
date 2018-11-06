package top.iznauy;

import top.iznauy.cfg.CFG;
import top.iznauy.cfg.Production;
import top.iznauy.table.Table;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created on 05/11/2018.
 * Description:
 *
 * @author iznauy
 */
public class Main {

    public static void main(String[] args) {

//        args = new String[] {"/Users/iznauy/CompilerLabs/SyntaxAnalyzer/test.cfg", "/Users/izn" +
//                "auy/CompilerLabs/SyntaxAnalyzer/test_in.txt" ,
//                "/Users/iznauy/CompilerLabs/SyntaxAnalyzer/test_out.txt"};

        if (args.length < 3) {
            System.out.println("Usage: java Main [CFG.cfg] [input.txt] [output.txt]");
            return;
        }

        String startSymbol;
        Set<String> nonTerminalSymbols = new HashSet<>();
        List<String> productionStrings = new ArrayList<>();
        String content;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]))) {
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
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[1]))) {
            String line;

            while ((line = bufferedReader.readLine()) != null)
                buffer.append(line.trim());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("输入文件读取出错");
            return;
        }
        content = buffer.toString();
        String[] contents = content.split(" ");
        List<String> inputSeq = Arrays.asList(contents);
        inputSeq = new ArrayList<>(inputSeq);

        CFG cfg = CFG.parse(startSymbol, nonTerminalSymbols, productionStrings);
        Table table = cfg.exportTable();
        List<Production> productions = table.parse(inputSeq);

        try (PrintWriter writer = new PrintWriter(new FileOutputStream(args[2]))) {
            for (Production production: productions) {
                writer.write(production.toString());
                writer.write('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("写入结果出错");
        }

    }

}
