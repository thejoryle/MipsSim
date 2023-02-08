import java.util.Map;
import java.util.Objects;

public class IFormat implements Instruction{
    String opcode;
    String rs;
    String rt;
    int immediate;
    asm2binConversion converter = new asm2binConversion();

    public IFormat(String[] instruction){
        this.opcode = instruction[0];
        if (Objects.equals(this.opcode, "lw") || Objects.equals(this.opcode, "sw")){
            this.rt = instruction[1];
            String[] temp = instruction[2].split("\\(");
            this.immediate = Integer.parseInt(temp[0]);
            this.rs = temp[1].replaceAll("\\)", "");
        } else if (Objects.equals(this.opcode, "beq") || Objects.equals(this.opcode, "bne")){
            this.rt = instruction[2];
            this.rs = instruction[1];
            this.immediate = Integer.parseInt(instruction[3]);
        }
        else {
            this.rt = instruction[1];
            this.rs = instruction[2];
            this.immediate = Integer.parseInt(instruction[3]);
        }
    }
    public void executeInstruction(Mips mips){
        RegisterFile rf = mips.rf;
        switch(this.opcode){
            case("add") -> rf.setDataByName(this.rt, this.addi(rf.getDataByName(this.rs), rf.getDataByName(this.rt)));
            case ("bne") -> { if(rf.getDataByName(this.rs) != rf.getDataByName(this.rt)) { mips.setPc(immediate);} }
            case ("beq") -> { if(rf.getDataByName(this.rs) == rf.getDataByName(this.rt)) { mips.setPc(immediate);} }
            case("sw") -> mips.memory[rf.getDataByName(this.rs)] = rf.getDataByName(this.rt);
            case("lw") -> rf.setDataByName(this.rt ,mips.memory[rf.getDataByName(this.rs) + this.immediate]);
        }
    }
    public int addi(int rs, int immediate){ return rs + immediate;}

    @Override
    public String toString(){
        return this.opcode + " " + this.rs + ", " + this.rt + ", " + immediate;
    }
    public void printBinary(){
        Map<String, String> conv = converter.asm2binConversion();
        //opcode
        String result = conv.get(this.opcode) + " ";
        //opcode rs
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
        //opcode rs rt imm
        result += int2bin(this.immediate);

        System.out.println(result);
    }

    public String int2bin(int imm){
        String result;
        if (imm >=0) {
            result = String.format("%16s", Integer.toBinaryString(imm)).replace(' ', '0');
        } else {
            result = Integer.toBinaryString(imm);
            int index2make16bits = result.length() - 16;
            result = result.substring(index2make16bits);
        }

        return result;
    }

}
