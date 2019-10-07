import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static int valorInicial = 0,barreira,p,printa;
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
        printa = 1;
        p = runTime.availableProcessors();

        int m = 10,k = 10,n = 10;
        Semaphore sA = new Semaphore(1);
        Semaphore sB = new Semaphore(1);
        Semaphore sC =new Semaphore(1);
        Semaphore sBarreira =new Semaphore(0);
        int[][] a = gerarMatriz(m,k);
        int[][] b = gerarMatriz(k,n);
        int[][] c = new int[m][n];
        int elementos = (m*n)/p;
        threads[] th = new threads[p];
        for(int i= 0; i< p;i++){
            th[i] = new threads(c,a,b,elementos,valorInicial,sA,sB,sC,sBarreira);
            valorInicial += elementos;
        }
        for(int i = 0; i <th.length;i++){
            th[i].start();
        }




    }
}
