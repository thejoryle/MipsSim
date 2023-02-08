import java.util.Map;

public class JFormat implements Instruction{
    String opcode;
    int address;
    asm2binConversion converter = new asm2binConversion();

    public JFormat(String opcode, int address){
        this.opcode = opcode;
        this.address = address;
    }
    public void executeInstruction(Mips mips){
        switch(this.opcode){
            case("j") -> mips.setPc(this.address);
            case("jal") -> {
                mips.rf.setDataByName("ra", mips.getPc() + 1);
                mips.setPc(this.address);
            }
        }
    }
    @Override
    public String toString(){
        return this.opcode + " " + address;
    }
    public void printBinary(){
        Map<String, String> conv = converter.asm2binConversion();
        //opcode
        String result = conv.get(this.opcode) + " ";
        //opcode address
        result += int2bin(address);

        System.out.println(result);
    }

    public String int2bin(int imm){
        String result;
        if (imm >= 0) {
            result = String.format("%26s", Integer.toBinaryString(imm)).replace(' ', '0');
        } else {
            imm = Math.abs(imm);
            result = String.format("%26s", Integer.toBinaryString(imm)).replace(' ', '1');
        }
        return result;
    }
}
