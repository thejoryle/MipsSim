import java.util.HashMap;
import java.util.Map;

/**
 * Supports commands:  and, or, add, addi, sll, sub, slt,
 *              beq, bne, lw, sw, j, jr, and jal.
 *
 *  Ignore registers: $at, $k0, $k1, $gp, $fp
 */

public class asm2binConversion {
    public asm2binConversion(){
        Map<String, String> asm2bin = new HashMap<>();
        asm2bin.put("and", "100100");
        asm2bin.put("or", "");
    }
}
