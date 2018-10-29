package top.iznauy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/10/29.
 * Description:
 *
 * @author iznauy
 */
public class TemplateLoader {

    public static Map<String, String> loadLexicalItems(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead())
            throw new RuntimeException();

        Map<String, String> constantMap = new HashMap<>();
        Map<String, String> nameToRE = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line, name, re;
            boolean inConstantSpace = true;
            int prior = 0;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                if (line.equals("%%")) {
                    inConstantSpace = false;
                    continue;
                }
                String[] nameAndRE = line.split(" ", 2);
                if (nameAndRE.length < 2)
                    throw new RuntimeException();
                name = nameAndRE[0];
                re = nameAndRE[1].trim();
                for (Map.Entry<String, String> entry: constantMap.entrySet()) { // 不断进行全文替换
                    String marco = "{" + entry.getKey() + "}";
                    while (re.contains(marco))
                        re = re.replace(marco, "(" + entry.getValue() + ")");
                }
                if (inConstantSpace) {
                    constantMap.put(name, re);
                } else { // 已经到了下面的词素定义区域
                    REs.reToPrior.put(re, prior);
                    prior++; // 排在后边的表达式优先级会更高一些
                    nameToRE.put(name, re);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return nameToRE;

    }

    public static void main(String[] args) {
        String filePath = "/Users/iznauy/CompilerLabs/java.iz";
        Map<String, String> results = loadLexicalItems(filePath);
        for (Map.Entry<String, String> result: results.entrySet()) {
            System.out.println(result);
        }
    }

}
