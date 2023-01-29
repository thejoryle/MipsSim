public class RFormat implements Instruction{
    String opcode;
    Register rs;
    Register rt;
    Register rd;
    int shamt;
    String funct;

    public RFormat(String opcode, Register rs, Register rt, Register rd, int shamt, String funct){
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.shamt = shamt;
        this.funct = funct;
    }

    public void printBinary(){

    }
}
