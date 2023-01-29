import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AsmParser {
    Instruction[] instructions;

    public Map<String, Integer> firstPass(File infile){
        Map<String, Integer> result = new HashMap<>();
        try (Scanner scan1 = new Scanner(infile)) {
            int count = 0;
            while (scan1.hasNext()) {
                String line = scan1.nextLine();
                line = line.strip();
                // only read lines with a colon, indicating a label, while ignoring comments
                if (!line.equals("") && line.charAt(0) != '#' && line.contains(":")) {
                    line = removeComments(line);
                    String[] temp = line.split(":");
                    result.put(temp[0], count);
                }
                count++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + infile.getPath());
        }
        return result;
    }

    public String removeComments(String in){
        String[] temp = in.split("#");
        return temp[0];
    }
}
