import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static int valorInicial = 0,barreira,p;

    public static int[][] gerarMatriz(int x, int y){
        Random generator = new Random();
        int[][] n = new int[x][y];
        for(int i = 0; i<x;i++){
            for(int j = 0; j<y;j++){
                n[i][j] = generator.nextInt( 5);
            }
        }
        return n;
    }
    public static void printaMatriz(int[][] s){
        for(int[] i : s) {
            System.out.print("[");
            for (int a= 0; a<i.length;a++){
                if(a != i.length-1)
                    System.out.print(i[a] +", ");
                else
                    System.out.print(i[a] +"");
            }
            System.out.println("]");
        }
    }


    public static void main(String[] args) {
        Runtime runTime = Runtime.getRuntime();
        boolean oneTime = true;
        p = runTime.availableProcessors();

        int m = 100,k = 100,n = 100;
        Semaphore sA = new Semaphore(1);
        Semaphore sB = new Semaphore(1);
        Semaphore sC =new Semaphore(1);
        Semaphore sBarreira =new Semaphore(0);
        int[][] a = gerarMatriz(m,k);
        int[][] b = gerarMatriz(k,n);
        int[][] c = new int[m][n],d = new int[m][n];
        int elementos = (int) Math.round((double)m/(double) p);
        int salva = elementos;
        if(m%p != 0 && m > p) elementos = (m%p);
        int t = m%p;


        threads[] th = new threads[p];

        for(int i= 0; i< p;i++){
            t--;
            if (t < 0){
                elementos = salva;
                if(oneTime){
                valorInicial += 1;
                oneTime = false;
                }
            }
            th[i] = new threads(c,a,b,elementos,valorInicial,sA,sB,sC,sBarreira);
            if(t > 0 ){
                valorInicial += elementos;
            }
            else{
                valorInicial += 1;
            }

        }
        long start = System.currentTimeMillis();
            for(int i = 0; i < a.length;i++){
                for (int j=0; j<b[0].length ; j++) {
                    int sm = 0;
                    for (int k1=0; k1<b.length; k1++) {
                        int y = a[i][k1];
                        int e = b[k1][j];
                        sm += y*e;
                    }
                    d[i][j] = sm;
                }
            }

        System.out.println("tempo = " + (System.currentTimeMillis()- start));
        //Main.printaMatriz(d);



        start = System.currentTimeMillis();
        for(int i = 0; i <th.length;i++){
            th[i].start();
        }
        try {
            sBarreira.acquire();
                System.out.println("tempo = " + (System.currentTimeMillis()- start));
                //Main.printaMatriz(c);
            sBarreira.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
