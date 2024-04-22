import java.util.HashMap;
import java.util.Map;

public class OpcodeConverter {
    private static final Map<String, Integer> opcodeMap = createOpcodeMap();

    private static Map<String, Integer> createOpcodeMap() {
        Map<String, Integer> map = new HashMap<>();
        // Define opcode mappings
        map.put("add", 32);
        map.put("sub", 34);
        map.put("and", 36);
        map.put("or", 37);
        map.put("addi", 8);
        map.put("sll", 0);
        map.put("slt", 42);
        map.put("beq", 4);
        map.put("bne", 5);
        map.put("lw", 35);
        map.put("sw", 43);
        map.put("j", 2);
        map.put("jr", 8);
        map.put("jal", 3);
        return map;
    }

    public static int convertToInt(String opcode) {
        Integer intOpcode = opcodeMap.get(opcode);
        if (intOpcode == null) {
            // Handle invalid opcode
            throw new IllegalArgumentException("Invalid opcode: " + opcode);
        }
        return intOpcode;
    }
}