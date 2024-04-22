import java.util.ArrayList;

class InstructionArray {
    private ArrayList<Integer> array;
    static ArrayList<PairLable> dynamicArray;
    private int inst_num;

    public InstructionArray() {
        array= new ArrayList<>();
        dynamicArray = new ArrayList<>();
    }
    public void add(int binary) {
        array.add(binary) ;
    }
    public void addLable(String lable, int address) {
        PairLable pair = new PairLable(lable, address);
        dynamicArray.add(pair);
    }
    public static int findAdresslable(String name) {
        for(PairLable a : dynamicArray) {
            if(a.getLable().equals(name)) return a.getaddress(); 
        }
        return -1;
    }
    public void printLabel() {
        for(int i = 0; i < dynamicArray.size() ; i++ ){
            String lable = dynamicArray.get(i).getLable();
            int address = dynamicArray.get(i).getaddress();
            System.out.printf("%s address: %d\n", lable, address);
        }
        
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
    public void print(){
        for(int i =0; i < array.size(); i++) {
            printBinary(array.get(i));
        }
    }
}