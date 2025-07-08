public class calculator{
    private int a;
    private int b;
    
    public int getno(){
        return a;
    }
    public void setno(int a){
        this.a=a;
    }
    public int getno2(){
        return b;
    }
    public void setno2(int b){
        this.b=b;
    }
}
public class Main{
    public static void main(String[] args){
        calculator c=new calculator();
        c.setno(5);
        c.setno2(10);
        System.out.println("a:"+c.getno());
        System.out.println("b:"+c.getno2());
    }
}