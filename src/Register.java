public class Register {
    private String name;
    private int data = 0;
    public Register(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public int getData(){
        return this.data;
    }
    public void setData(int newData){
        this.data = newData;
    }
}
