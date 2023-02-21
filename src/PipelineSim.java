import java.util.Formatter;

public class PipelineSim{
    private int pc = 0;
    private int cycles = 0;
    private Mips mips;
    private final String[] stages = {"pc", "if/id", "id/exe", "exe/mem", "mem/wb"};
    private String[] pipeline = {"empty", "empty", "empty", "empty"};
    private boolean stalled = false;
    private boolean squashed = false;
    private String stalledOp;
    private String[] squashedOps = new String[3];

    /* True to print pipeline, false otherwise */
    public void step(boolean print){
        Instruction instr = this.mips.instructions.get(this.pc);
        String op = instr.getOp();

        if(stalled && pipeline[1] != null && pipeline[1].equals("lw")){
            stall(this.stalledOp);
        } else if(instr instanceof IFormat iform && iform.squash){
            this.squashed = true;
        }else {
            if (isStall(instr)) {
                this.stalled = true;
                this.stalledOp = this.mips.instructions.get(this.pc+1).getOp();
            }
            if (!op.equals("beq") && !op.equals("bne")) { // change this to a fall through
                cycle(op);
                this.pc++;
            }
        }

        if (print) {
            printStep();
        }
    }
    public void cycle(String op){
        for(int i = pipeline.length - 1; i > 0; i--){
            pipeline[i] = pipeline[i-1];
        }
        pipeline[0] = op;
        this.cycles++;
    }

    public void stall(String op){
        cycle(op);
        this.cycles++;
        this.pipeline[1] = "stall";
        this.stalled = false;
    }
    public boolean isStall(Instruction instr){
        if(instr.getOp().equals("lw")){
            IFormat lw = (IFormat) instr;
            Instruction next = this.mips.instructions.get(this.pc + 1);
            if(next instanceof RFormat nextR){
                return lw.getRt().equals(nextR.getRs()) ||
                        lw.getRt().equals(nextR.getRt());
            } else if(next instanceof IFormat nextI){
                return lw.getRt().equals(nextI.getRs());
            } // might need a case here for j instructions
        }
        return false;
    }
    public boolean squash(){
        return true;
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
    public int getCycles(){return this.cycles;}
    public void setCycles(int cycles){this.cycles = cycles;}
}
