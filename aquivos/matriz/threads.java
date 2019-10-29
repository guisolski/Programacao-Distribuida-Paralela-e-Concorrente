import java.util.concurrent.Semaphore;

public class threads extends Thread {
    private double[][] c, a, b;
    private int elementos,valorInicial;
    private Semaphore cal;
    public threads(double[][] c1, double[][] a1, double[][] b1, int elementos, int valorInicial, Semaphore cal){
        this.c = c1;
        this.a = a1;
        this.b = b1;
        this.elementos = valorInicial+elementos;
        this.valorInicial = valorInicial;
        this.cal = cal ;
    }

    public void run() {
            for(int i = this.valorInicial; i < elementos;i++){
                for (int j=0; j<this.b[0].length ; j++) {
                    for (int k=0; k<this.b.length ; k++) {
                        this.c[i][j] += this.a[i][k] * this.b[k][j];
                    }

                }
            }
            cal.release();
    }
}
