import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;

public class PipelineSim{
    int pc = 0;
    int cycles = 0;
    Mips mips;
    final String[] stages = {"pc", "if/id", "id/exe", "exe/mem", "mem/wb"};
    String[] pipeline = new String[4];

    /* True to print pipeline, false otherwise */
    public void step(boolean print){
        Instruction instr = this.mips.instructions.get(this.pc);
        String op = instr.getOp();
        if(op.equals("lw") && isStall(instr)){
            System.out.println("Stall detected.");
            advance(op);
        }
        else if(!op.equals("beq") && !op.equals("bne")){ // change this to a fall through
            advance(op);
        }
        if(print){
            printStep();
        }
    }
    public void advance(String op){
        for(int i = pipeline.length - 1; i > 0; i--){
            pipeline[i] = pipeline[i-1];
        }
        pipeline[0] = op;
        this.pc++; // change this for j instructions
        this.cycles++;
    }
    public boolean isStall(Instruction instr){
        if(instr.getOp().equals("lw")){
            IFormat lw = (IFormat) instr;
            Instruction next = this.mips.instructions.get(this.pc + 1);
            if(next instanceof RFormat){
                RFormat nextR = (RFormat) next;
                if(lw.getRt().equals(nextR.getRs()) ||
                    lw.getRt().equals(nextR.getRt())){
                    return true;
                }
            } else if(next instanceof IFormat){
                IFormat nextI = (IFormat) next;
                if(lw.getRt().equals(nextI.getRs())){
                    return true;
                }
            } // might need a case here for j instructions
        }
        return false;
    }
    public void printStep() {
        Formatter formatter;
        for(String s: stages){
            formatter = new Formatter();
            formatter.format("%9s", s);
            System.out.print(formatter);
        }
        System.out.println();

        formatter = new Formatter();
        formatter.format("%9d", this.pc);
        System.out.print(formatter);
        for(String s: pipeline){
            formatter = new Formatter();
            formatter.format("%9s", s);
            System.out.print(formatter);
        }
        System.out.println();
    }
    public void setMips(Mips mips){this.mips = mips;}
    public int getPc(){return this.pc;}
    public void setPc(int pc){this.pc = pc;}
}
