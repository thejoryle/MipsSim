import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AsmParser {

    /**
     * Parses an input file (hopefully of MIPS assembly code) and returns a map
     * of labels (K) with their line location (V) (starting with 0)
     *
     * @param infile an input file
     * @return a map of MIPS labels and their corresponding line numbers
     */
    public Map<String, Integer> firstPass(File infile){
        Map<String, Integer> result = new HashMap<>();
        try (Scanner scan = new Scanner(infile)) {
            int count = 0;
            while (scan.hasNext()) {
                String line = scan.nextLine();
                line = removeComments(line);
                line = line.replaceAll(" ", "");

                // only read lines with a colon, indicating a label, while ignoring comments
                if (!line.equals("") && line.charAt(0) != '#' && line.contains(":")) {
                    line = removeComments(line);
                    String[] temp = line.split(":");
                    result.put(temp[0], count);
                }
                if(!line.isEmpty()){
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + infile.getPath());
        }
        return result;
    }

    /**
     * Parses an input file (hopefully of MIPS assembly code), constructs instruction
     * objects representing each instruction, puts them sequentially into an arrayList,
     * then returns the arrayList. Only supports certain operations
     * and registers. See asm2binConversion.java for details.
     *
     * @param infile an input file, intended to be a .asm file
     * @return an arraylist of instruction objects representing the instructions
     * of infile
     */
    public ArrayList<Instruction> parseAssembly(File infile){
        ArrayList<Instruction> instructions = new ArrayList<>();
        //initialize converter
        asm2binConversion a = new asm2binConversion();
        Map<String, String> conversions = a.asm2binConversion();
        //generate label addresses
        Map<String, Integer> labels = firstPass(infile);

        int address = 0;
        try (Scanner scan = new Scanner(infile)) {
            while (scan.hasNext()) {
                String line = scan.nextLine();
                line = line.strip();
                line = removeComments(line);
                if(line.contains(":")) {
                    line = removeLabels(line);
                }
                String[] instr;

                //split the instruction into an array of strings
                if(!line.equals("")) {
                    //check for possible J-format instructions which don't have $ in them
                    if (!line.contains("$")) {
                        instr = line.split(" ");
                        instr[1] = instr[1].strip();
                    } else {
                        line = line.replaceAll(" ", "");
                        line = line.replaceAll("\\t", "");
                        //temp1 holds the opcode in index 0 and all else (registers/immediates) in index 1
                        String[] temp1 = line.split("\\$", 2);
                        //remove '$' from registers then split them into their own strings
                        temp1[1] = temp1[1].replaceAll("\\$", "");
                        String[] temp2 = temp1[1].split(",");
                        //combine the results into instr
                        instr = new String[1+ temp2.length];
                        instr[0] = temp1[0];
                        System.arraycopy(temp2, 0, instr, 1, temp2.length);
                    }

                    //create an instruction object corresponding to its format type
                    if (conversions.containsKey(instr[0])) {
                        //R-format instruction
                        if (a.op2format(instr[0]) == 'r') {
                            Instruction inst = new RFormat(instr);
                            instructions.add(inst);
//                            System.out.println("Added to inst mem: " + inst);
                            address++;
                        }
                        //I-format instruction
                        else if (a.op2format(instr[0]) == 'i') {
                            //replace labels with addresses for branch instructions
                            if(Objects.equals(instr[0], "beq") || Objects.equals(instr[0], "bne")){
                                int imm = labels.get(instr[3]) - address;
                                instr[3] = Integer.toString(imm);
                            }
                            Instruction inst = new IFormat(instr);
                            instructions.add(inst);
//                            System.out.println("Added to inst mem: " + inst);
                            address++;
                        }
                        //J-format instruction
                        else if (a.op2format(instr[0]) == 'j') {
                            int location;
                            //spaces weren't removed earlier so do it now
                            for(String s: instr){
                                s = s.replaceAll(" ", "");
                            }
                            //get address of label from first pass
                            if (labels.containsKey(instr[1])) {
                                location = labels.get(instr[1]);
                            } else {
                                System.out.println("Invalid J-Format instruction detected. Closing program.");
                                break;
                            }
                            Instruction inst = new JFormat(instr[0], location);
                            instructions.add(inst);
//                            System.out.println("Added to inst mem: " + inst);
                            address++;
                        } else {
                            System.out.println("invalid instruction: " + instr[0]);
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + infile.getPath());
        }
        return instructions;
    }

    public String removeComments(String in){
        if(in.equals("#")){
            return "";
        }
        String[] temp = in.split("#");
        return temp[0];
    }

    public String removeLabels(String in){
        String[] temp = in.split(":");
        if(temp.length > 1) {
            temp[1] = temp[1].strip();
            return temp[1];
        }
        else { return ""; }
    }
}
