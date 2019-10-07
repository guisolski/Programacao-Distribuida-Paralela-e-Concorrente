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

            int count = 0;
            for (int i=this.valorInicial; i<this.a.length && count != this.elementos; i++) {
                for (int j=this.valorInicial; j<this.b[valorInicial].length && count != this.elementos; j++) {
                    int sm = 0;
                    for (int k=this.valorInicial; k<this.b.length && count != this.elementos; k++) {
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
                count++;
            }


            Main.barreira++;
            if(Main.barreira == Main.p)  sBarreira.release();
            sBarreira.acquire();
            if(Main.printa == 1){
                System.out.println("tempo = " + (System.currentTimeMillis()- Main.start));
                //Main.printaMatriz(c);
                Main.printa = 0;
            }
            sBarreira.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
