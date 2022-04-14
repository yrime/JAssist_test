package test.jassist;

public class JAssis {
    public static void main(String[] args){
        System.out.println("this test string");
        String jarname = args[0];
        new JAssistInstrumentation().instr(jarname);
        System.out.println(jarname + " was instrumented");
    }
}
