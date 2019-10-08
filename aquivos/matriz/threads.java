import java.util.concurrent.Semaphore;

public class threads extends Thread {
    private int[][] c, a, b;
    private int elementos,valorInicial;
    private Semaphore sA,sB,sC,sBarreira;
    public threads(int[][] c1, int[][] a1, int[][] b1, int elementos, int valorInicial, Semaphore sA, Semaphore sB, Semaphore sC, Semaphore sBarreira){
        this.c = c1;
        this.a = a1;
        this.b = b1;
        this.sBarreira = sBarreira;
        this.elementos = valorInicial+elementos;
        this.valorInicial = valorInicial;
        this.sA = sA;
        this.sB = sB;
        this.sC = sC;


    }

    public void run() {
        try {
            for(int i = this.valorInicial; i < elementos;i++){
                for (int j=0; j<this.b[0].length ; j++) {
                    int sm = 0;
                    for (int k=0; k<this.b.length ; k++) {
                        this.sA.acquire();
                        int y = this.a[i][k];
                        this.sA.release();
                        this.sB.acquire();
                        int e = this.b[k][j];
                        this.sB.release();
                        sm += y*e;
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
