package top.iznauy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created on 2018/10/30.
 * Description:
 *
 * @author iznauy
 */
public class SourceLoader {

    public static String loadSourceCode(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.canRead())
            throw new RuntimeException();

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return builder.toString();
    }

}
