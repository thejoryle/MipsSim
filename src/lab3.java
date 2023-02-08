import java.io.File;

public class lab3 {
    public static void main(String[] args) {
        String asmPath = args[0];
        String scriptPath;
        File asmFile = new File(asmPath);
        File scriptFile = null;
        if(args.length > 1){
            scriptPath = args[1];
            scriptFile = new File(scriptPath);
        }

        AsmParser parser = new AsmParser();
        Mips mips = new Mips(parser.parseAssembly(asmFile));

        if(args.length == 1) {
            mips.runMips();
        }
        else{
            mips.runMips(scriptFile);
        }
    }
}
