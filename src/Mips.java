import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Mips {
    RegisterFile rf = new RegisterFile();
    int[] memory = new int[8192];
    int pc = 0;
    ArrayList<Instruction> instructions;

    public Mips(ArrayList<Instruction> instructions){
        this.instructions = instructions;
    }

    public void runMips(){
        Scanner scan = new Scanner(System.in);
        String cmd;

        boolean running = true;
        System.out.print("mips> ");
        while(running){
            cmd = scan.nextLine();

            switch(cmd){
                case("q") -> running = false;
                case("d") -> dump();
                case("c") -> clear();
                case("s") -> {
                    step();
                    System.out.println("1 instruction(s) executed");
                }
                case("r") -> run();
            }
            if(cmd.length() > 1) {
                if (cmd.charAt(0) == 'm') {
                    String[] temp = cmd.split(" ");
                    int arg1 = Integer.parseInt(temp[1]);
                    int arg2 = Integer.parseInt(temp[2]);
                    printMemory(arg1, arg2);
                }
                if (cmd.charAt(0) == 's') {
                    String[] temp = cmd.split(" ");
                    int arg1 = Integer.parseInt(temp[1]);
                    step(arg1);
                    System.out.println(arg1 + " instruction(s) executed");
                }
            }
            System.out.print("mips> ");
        }
    }
    public void runMips(File infile){

    }
    public void dump(){
        System.out.println();
        System.out.println("pc = " + this.pc);
        rf.printAll();
    }
    public void printMemory(int first, int last){
        System.out.println();
        for(int i = first; i <= last; i++){
            System.out.println("[" + i + "]" + " = " + this.memory[i]);
        }
        System.out.println();
    }
    public void clear(){
        this.pc = 0;
        for(Register reg : this.rf.registerFile){
            reg.setData(0);
        }
        Arrays.fill(this.memory, 0);
        System.out.println("\tSimulator reset");
    }
    public void step(){
        instructions.get(pc).executeInstruction(this);
    }
    public void step(int steps){
        for(int i = 0; i < steps; i++){
            step();
        }
    }
    public void run(){
        for(Instruction inst : instructions){
            step();
        }
    }
    public void help(){
        System.out.println("h = show help");
        System.out.println("d = dump register state");
        System.out.println("s = single step through the program (i.e. execute 1 instruction and stop)");
        System.out.println("s num = step through num instructions of the program");
        System.out.println("r = run until the program ends");
        System.out.println("m num1 num2 = display data memory from location num1 to num2");
        System.out.println("c = clear all registers, memory, and the program counter to 0");
        System.out.println("q = exit the program");
    }
    public void setPc(int newPc){
        this.pc = newPc;
    }
    public int getPc(){
        return this.pc;
    }
}
