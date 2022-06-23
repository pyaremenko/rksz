public class TikTakToy extends Thread {

    private int id;
    private Data data;

    public TikTakToy(int id, Data d){
        this.id = id;
        data = d;
        this.start();
    }

    @Override
    public void run(){
        super.run();

        for(int i = 0; i < 5; i++){
            synchronized (data){
                try{
                    while (id != data.getState()){
                        data.wait();
                    }
                    if( id == 2){
                        data.Toy();
                    }
                    else if (id == 3){
                        data.Tic();
                    }
                    else {
                        data.Tac();
                    }
                    data.notify();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
