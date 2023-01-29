import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "Expected exactly 1 argument: a file name.");
        }
        String filePath = args[0];
        File infile = new File(filePath);

        AsmParser parser = new AsmParser();
        Map<String, Integer> labels = parser.firstPass(infile);
        System.out.println("Infile received: " + infile);

        //test
        for( Map.Entry<String, Integer> label : labels.entrySet()){
            System.out.println(label.getKey() + " - Line #" + label.getValue());
        }
    }
}
