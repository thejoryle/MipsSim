public class RegisterFile {
    Register[] registerFile;
    public RegisterFile(){
        this.registerFile = new Register[] {
                new Register("0"), new Register("v0"), new Register("v1"), new Register("a0"),
                new Register("a1"), new Register("a2"), new Register("a3"), new Register("t0"),
                new Register("t1"), new Register("t2"), new Register("t3"), new Register("t4"),
                new Register("t5"), new Register("t6"), new Register("t7"), new Register("s0"),
                new Register("s1"), new Register("s2"), new Register("s3"), new Register("s4"),
                new Register("s5"), new Register("s6"), new Register("s7"), new Register("t8"),
                new Register("t9"), new Register("sp"), new Register("ra")};
    }
    public void printAll(){
        int outWidth = 4;
        for(int j = 0; j < outWidth; j++) {
            for (int i = 0; i < this.registerFile.length; i++) {
                System.out.print("$" + this.registerFile[i].getName() + " = " + this.registerFile[i].getData());
                if(j < 3){
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }
    public int getDataByName(String registerName){
        for(Register register : this.registerFile){
            if(register.getName() == registerName){
                return register.getData();
            }
        }
        System.out.println("Failed to find register in register file.");
        return -1;
    }
    public void setDataByName(String registerName, int data){
        for(Register register : this.registerFile){
            if(register.getName() == registerName){
                register.setData(data);
            }
        }
    }
}
