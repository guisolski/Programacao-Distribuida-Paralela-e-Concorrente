public class cliente extends  Thread {
    private barbearia buffer;
    private  int id;
    public cliente(barbearia buffer, int id)
    {
        this.buffer = buffer;
        this.id = id;
    }

    public void run(){

            try {
                buffer.corta_cabelo(id);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }
}
