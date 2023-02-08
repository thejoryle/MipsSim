import java.util.HashMap;
import java.util.Map;

/**
 * Supports commands:  and (r), or (r), add (r), addi (i), sll (r), sub (r), slt (r),
 *              beq (r), bne (i), lw (i), sw (i), j (j), jr (r), and jal (j).
 *
 *  Ignore registers: $at, $k0, $k1, $gp, $fp
 *
 *  All R-format opcodes = 000000; stored values represent funct bits
 */

public class asm2binConversion {
    public Map<String, String> asm2binConversion(){
        Map<String, String> asm2bin = new HashMap<>();
        //opcodes
        asm2bin.put("and", "100100");
        asm2bin.put("or", "100101");
        asm2bin.put("add", "100000");
        asm2bin.put("addi", "001000");
        asm2bin.put("sll", "000000");
        asm2bin.put("sub", "100010");
        asm2bin.put("slt", "101010");
        asm2bin.put("beq", "000100");
        asm2bin.put("bne", "000101");
        asm2bin.put("lw", "100011");
        asm2bin.put("sw", "101011");
        asm2bin.put("j", "000010");
        asm2bin.put("jr", "001000");
        asm2bin.put("jal", "000011");

        //registers
        asm2bin.put("zero", "00000");
        asm2bin.put("0", "00000");
        asm2bin.put("v0", "00010");
        asm2bin.put("v1", "00011");
        asm2bin.put("a0", "00100");
        asm2bin.put("a1", "00101");
        asm2bin.put("a2", "00110");
        asm2bin.put("a3", "00111");
        asm2bin.put("t0", "01000");
        asm2bin.put("t1", "01001");
        asm2bin.put("t2", "01010");
        asm2bin.put("t3", "01011");
        asm2bin.put("t4", "01100");
        asm2bin.put("t5", "01101");
        asm2bin.put("t6", "01110");
        asm2bin.put("t7", "01111");
        asm2bin.put("s0", "10000");
        asm2bin.put("s1", "10001");
        asm2bin.put("s2", "10010");
        asm2bin.put("s3", "10011");
        asm2bin.put("s4", "10100");
        asm2bin.put("s5", "10101");
        asm2bin.put("s6", "10110");
        asm2bin.put("s7", "10111");
        asm2bin.put("t8", "11000");
        asm2bin.put("t9", "11001");
        asm2bin.put("sp", "11101");
        asm2bin.put("ra", "11111");

        return asm2bin;
    }

    /**
     * Takes in an opcode(String) and returns a char representing the
     * opcode's format (R, I, and J). For example, if given add, this
     * function will return R as add is an R-format instruction.
     *
     *
     * @param opcode a string representing an opcode.
     * @return 'r' for R-format, 'i' for I-Format and 'j' for J-Format
     *          otherwise it returns the null character.
     */
    public char op2format(String opcode){
        return switch(opcode){
            case("add"), ("or"), ("and"), ("sll"), ("sub"), ("slt"), ("jr") -> 'r';
            case("addi"), ("bne"), ("beq"), ("lw"), ("sw") -> 'i';
            case("j"), ("jal") -> 'j';
            default -> '\n';
        };
    }
}
