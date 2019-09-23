public class barbeiro extends Thread{
    private barbearia buffer;

    public barbeiro(barbearia buffer)
    {
        this.buffer = buffer;
    }
    public  void run(){
        while (true){
            try {
                buffer.pegue_proximo_cliente();
                buffer.termine_corte();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
