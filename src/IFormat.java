public class IFormat implements Instruction{
    String opcode;
    Register rs;
    Register rt;
    int immediate;

    public IFormat(String opcode, Register rs, Register rt, int immediate){
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        this.immediate = immediate;
    }

    public void printBinary(){

    }
}
