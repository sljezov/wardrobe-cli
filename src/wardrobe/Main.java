package wardrobe;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        new WardrobeCli(new Wardrobe(), new java.util.Scanner(System.in)).run();
    }
}
