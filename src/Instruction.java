import java.util.HashMap;
import java.util.Map;

public interface Instruction{
    void printBinary();
    void executeInstruction(Mips mips);
    String getOp();
}
