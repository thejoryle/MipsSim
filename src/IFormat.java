import java.util.Map;
import java.util.Objects;

public class IFormat implements Instruction{
    String opcode;
    String rs;
    String rt;
    int immediate;
    boolean squash = false;
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
//        System.out.println("Executing: " + this);
        switch(this.opcode){
            case("addi") -> {
                //rt = rs + imm
                mips.registerFile.setDataByName(this.rt, this.addi(mips.registerFile.getDataByName(this.rs), this.immediate));
                mips.nextPc();
            }
            case ("bne") -> {
                // if rs != rt, pc = imm; else, pc = pc+1
                if(mips.registerFile.getDataByName(this.rs) != mips.registerFile.getDataByName(this.rt)) {
                    mips.setPc(mips.getPc() + this.immediate);
                    this.squash = true;
                } else {
                mips.nextPc();
                }
            }
            case ("beq") -> {
                // if rs != rt, pc = imm; else, pc = pc+1
                if(mips.registerFile.getDataByName(this.rs) == mips.registerFile.getDataByName(this.rt)) {
                    mips.setPc(mips.getPc() + immediate);
                    this.squash = true;
                } else {
                mips.nextPc();
                }
            }
            case("sw") -> {
                // M[rs+imm] = rt
                mips.memory[mips.registerFile.getDataByName(this.rs) + immediate] = mips.registerFile.getDataByName(this.rt);
                mips.nextPc();
            }
            case("lw") -> {
                // rt = M[rs+imm]
                mips.registerFile.setDataByName(this.rt ,mips.memory[mips.registerFile.getDataByName(this.rs) + this.immediate]);
                mips.nextPc();
            }
            default -> System.out.println("IFormat instr failed to execute.");
        }
    }
    public int addi(int rs, int immediate){ return rs + immediate;}
    public String getOp(){return this.opcode;}
    public String getRs(){return this.rs;}
    public String getRt(){return this.rt;}

    @Override
    public String toString(){
        return this.opcode + " " + this.rs + " " + this.rt + " " + immediate;
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
