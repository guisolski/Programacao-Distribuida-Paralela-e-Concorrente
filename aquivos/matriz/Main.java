import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static int valorInicial = 0,p;

    private static boolean oneTime = true;


    private static int m ,k,n;
    private static Semaphore cal;
    private static double[][] a ,b , c , d ;
    private static int elementos;
    private  static threads[] th = new threads[p];


    public static void printaMatriz(double[][] s,String name) throws IOException {
        FileWriter arq = new FileWriter(name);
        PrintWriter gravarArq = new PrintWriter(arq);
        DecimalFormat decimal = new DecimalFormat( "0.00" );
        for(double[] i : s) {
            gravarArq.printf("[");
            for (int a= 0; a<i.length;a++){
                if(a != i.length-1)
                    gravarArq.printf(String.valueOf(decimal.format(i[a])).replaceAll(",",".") +", ");
                else
                    gravarArq.printf(String.valueOf(decimal.format(i[a])).replaceAll(",",".") +"");
            }
            gravarArq.println("]");
        }
        arq.close();
    }
    public static long sequencial(){
        long start = System.currentTimeMillis();
        for(int i = 0; i < a.length;i++){
            for (int j=0; j<b[0].length ; j++) {
                int sm = 0;
                for (int k1=0; k1<b.length; k1++) {
                    sm += a[i][k1]*b[k1][j];
                }
                d[i][j] = sm;
            }
        }
        long t = (System.currentTimeMillis()- start);
        System.out.println("tempo Sequencia= " + t);
        return  t;
    }
    public  static  long paralelo(int num){
        elementos =  m / p;
        int salva = elementos;
        long tempo = 0;
        if(m%p != 0 && m > p ) elementos = (int) Math.ceil((double) m / (double) p);

        int t = m%p;

        if(t!=0) System.out.println("Linhas alocadas por processador:   " + elementos + " em " + t + " processadores") ;
        System.out.println("Linhas alocadas por processador: " + salva + " em " + (p-t) + " processadores");
        if(t == 0) oneTime = false;
        for(int i= 0; i< p;i++){
            t--;
            if (t < 0){
                elementos = salva;
                if(oneTime){
                    valorInicial += elementos;
                    oneTime = false;
                }
            }
            th[i] = new threads(c,a,b,elementos,valorInicial,cal);
            if(t > 0 ){
                valorInicial += elementos;
            }
            else{
                valorInicial += oneTime ? 1 : elementos;
            }

        }
        long start = System.currentTimeMillis();
        for(int i = 0; i <th.length;i++){
            th[i].start();
        }
        try {
            cal.acquire();
            tempo = (System.currentTimeMillis()- start);
            System.out.println("tempo Paralelo = " + tempo);
            Main.printaMatriz(c,"C"+num);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return  tempo;
    }
    public  static void criaArquivosIniciais(int v1,int v2, String name) throws IOException {
        FileWriter arq = new FileWriter(name);
        PrintWriter gravarArq = new PrintWriter(arq);

        Random generator = new Random();
        DecimalFormat decimal = new DecimalFormat( "0.00" );
        for (int i=0; i<v1; i++) {
            for(int j=0; j<v2; j++){
                gravarArq.printf(String.valueOf( decimal.format(generator.nextDouble()*100)).replaceAll(",",".")+ " ");
            }
            gravarArq.println("");
        }
        arq.close();
    }
    public static  double[][]  learArquivo(int v1,int v2, String nome) throws IOException {
        FileReader arq = new FileReader(nome);
        BufferedReader lerArq = new BufferedReader(arq);
        double[][] cac = new double[v1][v2];
        String linha = lerArq.readLine();
        int i = 0;
        while (linha != null ) {
            String[] k = linha.split(" ");
            for(int j =0; j < k.length; j++){
                cac[i][j] = Double.parseDouble(k[j]);
            }
            i++;
            linha = lerArq.readLine();
        }
        arq.close();
        return cac;
    }
    public static void main(String[] args) throws IOException {
        Runtime runTime = Runtime.getRuntime();
        p = runTime.availableProcessors();
        int[][] valores = {{1000,1000,1000},{2000,2000,2000},{2000,1000,2000},{2000,4000,2000}};
        int num = 1;

        for(int[] i : valores) {

            m = i[0];
            k = i[1];
            n = i[2];

            cal = new Semaphore(1 - p);

            criaArquivosIniciais(m, k, "A"+ num);
            criaArquivosIniciais(k, n, "B"+ num);
            a = learArquivo(m, k, "A" + num);
            b = learArquivo(k, n, "B" + num);
            c = new double[m][n];
            d = new double[m][n];

            th = new threads[p];

            p = runTime.availableProcessors();
            System.out.println("Total de processadores: " + p);
            long tS = sequencial();
            valorInicial = 0;
            oneTime = true;
            long tP = paralelo(num);


            System.out.println("razão = " + (double) tS / tP);
            num++;
        }
    }
}
