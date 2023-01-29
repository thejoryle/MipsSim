public class Register {
    String name;
    int value;

    public Register(String name, int value){
        this.name = name;
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
    }
}
