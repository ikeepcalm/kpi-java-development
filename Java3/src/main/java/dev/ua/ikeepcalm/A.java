package dev.ua.ikeepcalm;

interface F {
    void find();

    void change(String string);
}

class G {

    protected String data;

    public G() {
        this.data = "Default data";
    }

    public G(String data) {
        this.data = data;
    }

    public void displayInfo() {
        System.out.println("G class data: " + data);
    }
}

public class A extends G implements F {

    public A() {
        super();
    }

    public A(String data, int id) {
        super(data);
    }

    @Override
    public void find() {
        System.out.println("We do not care about implementation here");
    }

    @Override
    public void change(String string) {
        System.out.println("Changing data");
        this.data = string;
    }

}