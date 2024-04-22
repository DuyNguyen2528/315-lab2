import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException; 

class MIPS{ 
    static String[] instructionSet = {"and", 
                                "or", 
                                "add",
                                "addi",
                                "sll", 
                                "sub", 
                                "slt",
                                "beq", 
                                "bne", 
                                "lw", 
                                "sw", 
                                "j", 
                                "jr", 
                                "jal"}; 
    public static int countLine(File file) {
        int numLine = 0;
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                numLine++;
                //System.out.println(line);
            }
            sc.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
        return numLine;
    }
    public static void firstPass(File file, InstructionArray instAray) {
        String colon = ":";
        int address = 0;
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String line = sc.nextLine();


                if(line.contains(colon)) {
                    //System.out.println(line);
                    String[] parts = line.split(":");
                    String label = parts[0].trim();
                    instAray.addLable(label, address);
                    //System.out.println( Integer.toHexString(starting_address));
                }
                address  = address + 4;
            }

            sc.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }
    public static int reg_extract(String reg) {
        if(reg.charAt(0) != '$') {
            return Integer.parseInt(reg);
        }
        char reg_type = reg.charAt(1);
        if(reg_type == '0' || reg_type == 'z') return 0;
        int reg_num = reg.charAt(2) - '0';

        switch(reg_type) {
            case 'a':
                return reg_num + 4;

            case 'v':
                return reg_num + 2;

            case 't':
                if(reg_num <= 7) {
                    return reg_num + 8;
                }
                else {
                    return reg_num + 16;
                }

            case 's':
                return reg_num + 16;


            default:
            System.out.println("invalid instruction");
            System.exit(0);
            
        }
        
        return 0;

    }
    public static boolean checkvalid(String line) {
        String a[] = line.split(" ");
        String opcode = a[0];
        
        for(String valid:instructionSet) {
            if(valid.equals(opcode)) {
                return true;
            }
        }
        return false;
    }
    public static int opcode_extract(String opcode) {

        int intOpcode = OpcodeConverter.convertToInt(opcode);
        //System.out.println("Integer opcode for " + opcode + ": " + intOpcode);
        return intOpcode;
        
    }
    public static int Merge(int opcode, int first, int second, int third){
        if (second < 0 || second > 31 || first < 0 || first > 31) {
            throw new IllegalArgumentException("Invalid register value.");
        }

        // Combine the opcode and register values to form the MIPS instruction
        int instruction = 0;
        // set up for beq and bne
        if(opcode > 3 && opcode < 5) {
            instruction |= (opcode << 26); // Left-shift opcode by 26 bits
            instruction |= (first << 21); // Shift rs to its position
            instruction |= (second << 16); // Shift rt to its position
            instruction |= (third); // Shift rt to its position


        }
        // immediate instruction
        else if(opcode <= 15) {
            instruction |= (opcode << 26); // Left-shift opcode by 26 bits
            instruction |= (second << 21); // Shift rs to its position
            instruction |= (first << 16); // Shift rt to its position
            instruction |= (third); // Shift rt to its position
            return instruction;
        }
        
        else {
            instruction |= (opcode); // Left-shift opcode by 26 bits
            instruction |= (second << 21); // Shift rs to its position
            instruction |= (third << 16); // Shift rt to its position
            instruction |= (first << 11); // Shift rt to its position
            
            return instruction;
        }
        
        
        
        

        return instruction;

    }
    public static int offset(String lable) {
        System.out.print("offset is: ");
        System.out.println(InstructionArray.findAdresslable(lable));
        return InstructionArray.findAdresslable(lable);
    }
    public static int decode(String[] instruction, int address) {
        int InstructionSize = instruction.length;
        int opcode;
        int rs;
        int rt;
        int rd;
        int immediate_value;
        int final_value= 0;
        int lable;
        opcode = opcode_extract(instruction[0]);
        if(opcode > 3 && opcode < 5) {
            rs = reg_extract(instruction[1]);
            rt = reg_extract(instruction[2]);
            lable = offset(instruction[3]);
            int offset = lable - address;
            System.out.println(address);
            final_value = Merge(opcode, rs, rt, offset);
            
        }
        else if(opcode < 15 ) {
                rd = reg_extract(instruction[1]);
                rs = reg_extract(instruction[2]);
                immediate_value = reg_extract(instruction[3]);
                final_value = Merge(opcode, rd, rs, immediate_value);
        }
        else {
            rd = reg_extract(instruction[1]);
            rs = reg_extract(instruction[2]);
            rt = reg_extract(instruction[3]);
            final_value = Merge(opcode, rd, rs, rt);
        }
        return final_value;
        
    }
    public static int binaryReturn(String instruction, int instructionAdd) {
        if(checkvalid(instruction)){
            //extract info of instruction
            String info[] = instruction.split("\\s*,\\s*|\\s+");
            return decode(info,instructionAdd);
        }
        else {
            System.out.println("invalid instruction");
            System.exit(0);
        }
        return -1;
    }
    public static String extractLable(String line) {
        String a[] = line.split(":");
        String lable = a[0].trim();
        return lable;
    }
    public static void printBinary(int number) {
        // Convert the integer to a 32-bit binary string
        String binaryString = String.format("%32s", Integer.toBinaryString(number)).replace(' ', '0');

        // Print the binary string in MIPS format
        for (int i = 0; i < binaryString.length(); i++) {
            System.out.print(binaryString.charAt(i));
            // Add space every 4 bits
            if (i == 5 || i == 10 || i == 15 || i == 20 || i == 25 ) {
                System.out.print(" ");
            }
            
        }
        System.out.println();
    }
    public static void secondpass(File file, InstructionArray InstArray) {
        int address = 0;
        String instruction;
        int binaryInstruction;
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                line = line.trim();
                String opcode;
                if(!line.isEmpty() && !line.startsWith("#")) {

                    
                    if(line.contains(":")) {
                        int index = line.indexOf(":");
                        instruction= (line.substring(index + 1, line.length())).trim();
                        String set[] = instruction.split(" ");
                        String opcode_name = set[0];

                        for(String a : instructionSet) {
                            if(opcode_name.equals(a)) {

                                binaryInstruction = binaryReturn(instruction,address);
                                //printBinary(binaryInstruction);
                                InstArray.add(binaryInstruction);
                            }
                            else {
                                //address= address + 4;
                                continue;
                            }
                        }
                        
                        
                    
                    }
                    else {
                        binaryInstruction = binaryReturn(line,address);
                        InstArray.add(binaryInstruction);
                    }
                    
                    address= address + 4;
                }
            }
            InstArray.print();
            sc.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("An error has occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String args[]){  
        // COUNT HOW MANY LINE OF FILE
        int numLine = 0;
        File file = new File("demo.asm");
        numLine = countLine(file);
        InstructionArray instAray = new InstructionArray();
        firstPass(file, instAray);
        
        int binary = 0;
        
        
        //instAray.printLabel();
        //instAray.print();
        secondpass(file, instAray);

        
    }  
}
