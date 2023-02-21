import java.util.Map;
import java.util.Objects;

public class RFormat implements Instruction{
    String opcode;
    String rs = "zero";
    String rt = "";
    String rd = "";
    int shamt = 0;
    boolean squash = false;
    asm2binConversion converter = new asm2binConversion();

    //Create the instruction given its entire instruction parsed into its sub-parts
    //1 arg: jr;  2 args: sll; 3 args: and, add, or, sub, slt, beq
    public RFormat(String[] instruction){
        this.opcode = instruction[0];

        //jr special case
        if(Objects.equals(this.opcode, "jr")) {
            this.rs = instruction[1];
        } else{
            this.rd = instruction[1];
            //set shamt for sll
            if(Objects.equals(this.opcode, "sll")){
                this.rt = instruction[2];
                this.shamt = Integer.parseInt(instruction[3]);
            } else {
                if(!Objects.equals(this.opcode, "jr")) {
                    this.rs = instruction[2];
                    this.rt = instruction[3];
                }
            }
        }
    }
    public void executeInstruction(Mips mips) {
//        System.out.println("Executing: " + this);
        RegisterFile rf = mips.registerFile;
        switch(this.opcode){
            // rd = rs + rt
            case("add") -> {
                rf.setDataByName(this.rd, this.add(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
                mips.nextPc();
            }
            // rd = rs & rt
            case("and") -> {
                rf.setDataByName(this.rd, this.and(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
                mips.nextPc();
            }
            // rd = rs | rt
            case("or") -> {
                rf.setDataByName(this.rd, this.or(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
                mips.nextPc();
            }
            // pc = rs
            case("jr") -> {
                mips.setPc(rf.getDataByName(this.rs));
                squash = true;
            }
            // rd = rs < rt ? 1 : 0
            case("slt") -> {
                rf.setDataByName(this.rd, this.slt(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
                mips.nextPc();
            }
            // rd = rs << shamt
            case("sll") -> {
                rf.setDataByName(this.rd, this.sll(rf.getDataByName(this.rs)));
                mips.nextPc();
            }
            // rd = rs - rt
            case("sub") -> {
                rf.setDataByName(this.rd, this.sub(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
                mips.nextPc();
            }
            default -> System.out.println("RFormat inst failed to execute.");
        };
    }
    public int add(int rs, int rt){
        return rs + rt;
    }
    public int and(int rs, int rt){
        return rs & rt;
    }
    public int or(int rs, int rt){
        return rs | rt;
    }
    public int slt(int rs, int rt){
        return rs < rt ? 1 : 0;
    }
    //rd = rs << shamt -> did not change rd here, check for bugs
    public int sll(int rs){
        return rs << this.shamt;
    }
    public int sub(int rs, int rt){
        return rs - rt;
    }
    public String getOp(){return this.opcode;}
    public String getRs(){return this.rs;}
    public String getRt(){return this.rt;}
    public boolean squash(){return this.squash;}
    @Override
    public String toString(){
        return opcode + " " + this.rd + " " + this.rs + " " + this.rt;
    }
    public void printBinary(){
        Map<String, String> conv = this.converter.asm2binConversion();
        //opcode
        String result = "000000 ";
        // opcode rs
        if (this.rs == null){
            result += "00000 ";
        } else {
            result += conv.get(this.rs) + " ";
        }
        //opcode rs rt
        if (this.rt == null){
            result += "00000 ";
        } else {
            result += conv.get(this.rt) + " ";
        }
        //opcode rs rt rd
        if (this.rd == null){
            result += "00000 ";
        } else {
            result += conv.get(this.rd) + " ";
        }
        //opcode rs rt rd shamt
        String shift = String.format("%5s", Integer.toBinaryString(this.shamt)).replace(' ', '0') + " ";
        result += shift;
        //opcode rs rt rd shamt funct
        result += conv.get(this.opcode);

        System.out.println(result);
    }
}
