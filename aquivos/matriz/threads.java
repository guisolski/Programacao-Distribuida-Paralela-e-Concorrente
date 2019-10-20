import java.util.concurrent.Semaphore;

public class threads extends Thread {
    private double[][] c, a, b;
    private int elementos,valorInicial;
    private Semaphore cal,sC,sBarreira;
    public threads(double[][] c1, double[][] a1, double[][] b1, int elementos, int valorInicial, Semaphore cal, Semaphore sC, Semaphore sBarreira){
        this.c = c1;
        this.a = a1;
        this.b = b1;
        this.sBarreira = sBarreira;
        this.elementos = valorInicial+elementos;
        this.valorInicial = valorInicial;
        this.cal =cal ;
        this.sC = sC;


    }

    public void run() {
        try {
            for(int i = this.valorInicial; i < elementos;i++){
                for (int j=0; j<this.b[0].length ; j++) {
                    double sm = 0;
                    for (int k=0; k<this.b.length ; k++) {
                        this.cal.acquire();
                            sm += this.a[i][k] * this.b[k][j];
                        this.cal.release();
                    }
                    this.sC.acquire();
                        this.c[i][j] = sm;
                    this.sC.release();
                }
            }
            Main.barreira++;
            if(Main.barreira ==  Main.p)  sBarreira.release();
            sBarreira.acquire();
            sBarreira.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
