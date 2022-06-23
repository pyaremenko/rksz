

public class Main
{
    public static void main(String[] args) throws InterruptedException {

        Data d = new Data();
        TikTakToy w1 = new TikTakToy(1, d);
        TikTakToy w2 = new TikTakToy(2, d);
        TikTakToy w3 = new TikTakToy(3, d);

        w3.join();
    }
}
