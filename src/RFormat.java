import java.util.Map;
import java.util.Objects;

public class RFormat implements Instruction{
    String opcode;
    String rs = "zero";
    String rt = "";
    String rd = "";
    int shamt = 0;
    asm2binConversion converter = new asm2binConversion();

    //Create the instruction given its entire instruction parsed into its sub-parts
    //1 arg: jr;  2 args: sll; 3 args: and, add, or, sub, slt, beq
    public RFormat(String[] instruction){
        this.opcode = instruction[0];

        //jr special case
        if(!Objects.equals(this.opcode, "jr")) {
            this.rd = instruction[1];
            //set shamt for sll
            if(Objects.equals(this.opcode, "sll")){
                this.rt = instruction[2];
                this.shamt = Integer.parseInt(instruction[3]);
            } else{
                if(!Objects.equals(this.opcode, "jr")) {
                    this.rs = instruction[2];
                    this.rt = instruction[3];
                }
            }
        } else {
            this.rs = instruction[1];
        }
    }
    public void executeInstruction(Mips mips) {
        RegisterFile rf = mips.rf;
        switch(this.opcode){
            case("add") -> rf.setDataByName(this.rd, this.add(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
            case("and") -> rf.setDataByName(this.rd, this.and(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
            case("or") -> rf.setDataByName(this.rd, this.or(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
            case("jr") -> mips.setPc(rf.getDataByName(this.rs));
            case("slt") -> rf.setDataByName(this.rd, this.slt(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
            case("sll") -> rf.setDataByName(this.rd, this.sll(rf.getDataByName(this.rs)));
            case("sub") -> rf.setDataByName(this.rd, this.sub(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
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
    @Override
    public String toString(){
        return opcode + " $" + this.rd + ", $" + this.rs + ", $" + this.rd;
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
