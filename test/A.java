public class A {
    A(){
        System.out.println("this class A, constructo");
        this.aPrivate();
    }
    public int aPublic(){
        System.out.println("this class A, public method a");
        return  0;
    }
    private int aPrivate(){
        System.out.println("this class A, private method a");
        return 0;
    }
}
