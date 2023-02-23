import java.util.Arrays;

public class PipelineSim{
    private int pc = 0;
    private int cycles = 0;
    private Mips mips;
    private final String[] stages = {"pc", "if/id", "id/exe", "exe/mem", "mem/wb"};
    private final String[] pipeline = {"empty", "empty", "empty", "empty"};
    private boolean stalled = false;
    private boolean squashed = false;
    private boolean jsquashed = false;
    private int cyclesToStall;
    private int cyclesToSquash;
    private String stalledOp;
    private int jaddress;

    /* True to print pipeline, false otherwise */
    public void step(boolean print){
        if(this.pc < mips.instructions.size()) {
            Instruction instr = this.mips.instructions.get(this.pc);
            String op = instr.getOp();

            //handle stalls and squashes first
            if (cyclesToStall == 1) {
                stalled = true;
            }
            if (stalled && cyclesToStall == 0) {
                stall(this.stalledOp);
            } else if (squashed && cyclesToSquash == 0) {
                squash(op);
                pc = jaddress;
            } else if (jsquashed) {
                cycle("squash");
                this.jsquashed = false;
                this.pc = jaddress;
            }
            else { //set stalls and squashes after
                if (isStall(instr)) {
                    this.stalledOp = this.mips.instructions.get(this.pc + 1).getOp();
                    cyclesToStall = 2;
                } else if (instr.squash()) {
                    if (instr instanceof IFormat branch) {
                        this.squashed = true;
                        cyclesToSquash = 3;
                        this.jaddress = pc + branch.immediate;
                    } else if (instr instanceof JFormat jump) {
                        this.jsquashed = true;
                        this.jaddress = jump.address;
                    } else {
                        this.jsquashed = true;
                        RFormat jr = (RFormat) instr;
                        this.jaddress = mips.registerFile.getDataByName(jr.getRs());
                    }
                }
                cycle(op);
                this.pc++;
                if (cyclesToSquash > 0) {
                    cyclesToSquash--;
                }
                if (cyclesToStall > 0) {
                    cyclesToStall--;
                }
            }
            if (print) {
                printStep();
            }
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
        this.pipeline[1] = "stall";
        this.stalled = false;
    }
    public void squash(String op){
        cycle(op);
        for(int i = 0; i < 3; i++){
            pipeline[i] = "squash";
        }
        this.squashed = false;
    }
    public boolean isStall(Instruction instr){
        if(instr.getOp().equals("lw") && (this.pc < mips.instructions.size() - 1)){
            IFormat lw = (IFormat) instr;
            Instruction next = this.mips.instructions.get(this.pc + 1);
            if(next instanceof RFormat nextR){
                return lw.getRt().equals(nextR.getRs()) ||
                        lw.getRt().equals(nextR.getRt());
            } else if(next instanceof IFormat nextI) { //check for rs on all instr and rt for sw/beq/bne
                if (nextI.getOp().equals("lw") || nextI.getOp().equals("addi")) {
                    return lw.getRt().equals(nextI.getRs());
                } else {
                    return lw.getRt().equals(nextI.getRt()) || lw.getRt().equals(nextI.getRs());
                }
            }
        }
        return false;
    }
    public void printStep() {
        //print top row
        for(String s: stages){
            System.out.printf("%9s", s);
        }
        System.out.println();

        //print bottom row
        System.out.printf("%9d", this.pc);
        for(String s: pipeline){
            System.out.printf("%9s", s);
        }
        System.out.println();
        System.out.println("PC: " + pc + ", Cycles: " + cycles);
    }
    public void clear(){
        this.pc = 0;
        this.cycles = 0;
        Arrays.fill(pipeline, "empty");
        this.stalled = false;
        this.squashed = false;
        this.jsquashed = false;
        this.cyclesToStall = 0;
        this.cyclesToSquash = 0;
        this.stalledOp = null;
        this.jaddress = 0;
    }
    public boolean isSquashed() {return this.squashed;}
    public boolean isJsquashed(){return this.jsquashed;}
    public boolean isStalled(){return this.stalled;}
    public void setMips(Mips mips){this.mips = mips;}
    public int getPc(){return this.pc;}
    public void setPc(int pc){this.pc = pc;}
    public int getCycles(){return this.cycles;}
    public void setCycles(int cycles){this.cycles = cycles;}
}
