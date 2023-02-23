import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Mips {
    RegisterFile registerFile = new RegisterFile();
    int[] memory = new int[8192];
    int pc = 0;
    ArrayList<Instruction> instructions;
    PipelineSim psim;
    boolean isPsim = false;
    int instrExecuted = 0;

    public Mips(ArrayList<Instruction> instructions, PipelineSim psim){
        this.instructions = instructions;
        this.psim = psim;
        this.isPsim = true;
    }
    public Mips(ArrayList<Instruction> instructions){
        this.instructions = instructions;
    }
    public void mipsInteractive(){
        Scanner scan = new Scanner(System.in);
        String cmd;

        boolean running = true;

        while(running){
            System.out.print("mips> ");
            cmd = scan.nextLine();

            switch(cmd){
                case("q") -> running = false;
                case("d") -> dump();
                case("p") ->{
                    if(isPsim){
                        psim.printStep();
                    }
                }
                case("c") -> {
                    clear();
                    if(isPsim){
                        psim.clear();
                    }
                }
                case("s") -> {
                    step();
                    if(!isPsim) {System.out.println("1 instruction(s) executed");}
                    else{this.psim.step(true);}
                }
                case("r") -> run();
                case("h") -> help();
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
                    if(!isPsim){System.out.println(arg1 + " instruction(s) executed");}
                }
            }
        }
    }
    public void mipsScript(File infile){
        try {
            Scanner scan = new Scanner(infile);
            String cmd;

            boolean running = true;
            while (running) {
                System.out.print("mips> ");
                cmd = scan.nextLine();
                System.out.println(cmd);

                switch (cmd) {
                    case ("q") -> running = false;
                    case ("d") -> dump();
                    case("p") ->{
                        if(isPsim){
                            psim.printStep();
                        }
                    }
                    case ("c") -> {
                        clear();
                        if(isPsim){
                            psim.clear();
                        }
                    }
                    case ("s") -> {
                        step();
                        if(!isPsim){System.out.println("1 instruction(s) executed");}
                        else{this.psim.step(true);}
                    }
                    case ("r") -> run();
                    case ("h") -> help();
                }
                if (cmd.length() > 1) {
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
                        if(!isPsim){System.out.println("1 instruction(s) executed");}
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + infile.getPath());
        }
    }
    public void dump(){
        System.out.println();
        System.out.println("pc = " + this.pc);
        this.registerFile.printAll();
        System.out.println();
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
        for(Register reg : this.registerFile.registerFile){
            reg.setData(0);
        }
        Arrays.fill(this.memory, 0);
        instrExecuted = 0;
        System.out.println("\tSimulator reset");
    }
    public void step(){
        if(pc < instructions.size() &&
        (!isPsim || (!psim.isSquashed() && !psim.isJsquashed() && !psim.isStalled()))) {
            this.instructions.get(pc).executeInstruction(this);
            this.instrExecuted++;
        }
    }
    public void step(int steps){
        for(int i = 0; i < steps; i++){
            step();
            this.psim.step(true);
        }
    }
    public void run(){
        if(!isPsim) {
            while(this.pc < this.instructions.size()) {
                step();
            }
        }
        else{
            while (this.psim.getPc() < this.instructions.size()) {
                step();
                this.psim.step(false);
            }
            this.psim.setCycles(this.psim.getCycles() + 4);
            System.out.printf("CPI = %.4f  Cycles = %d  Instructions = %d\n",
                    ((float)this.psim.getCycles())/ this.instrExecuted, this.psim.getCycles(), this.instrExecuted);
        }
    }
    public void help(){
        System.out.println();
        System.out.println("h = show help");
        System.out.println("d = dump register state");
        if(isPsim) {
            System.out.println("p = show pipeline registers");
        }
        System.out.println("s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)");
        System.out.println("s num = step through num clock cycles");
        System.out.println("r = run until the program ends");
        System.out.println("m num1 num2 = display data memory from location num1 to num2");
        System.out.println("c = clear all registers, memory, and the program counter to 0");
        System.out.println("q = exit the program");
        System.out.println();
    }
    public void setPc(int newPc){
        this.pc = newPc;
    }
    public int getPc(){
        return this.pc;
    }
    public void nextPc() { this.pc ++;}
}
