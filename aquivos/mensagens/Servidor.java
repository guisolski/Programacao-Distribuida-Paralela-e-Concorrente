import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;



public class Servidor extends Thread {
    private static HashMap<String, PrintStream> MAP_CLIENTES;
    private static  Socket conexao;
    private static ArrayList<String> LISTA_DE_NOMES = new ArrayList<>();
    private static ArrayList<String> LISTA_DE_IP = new ArrayList<>();
    private static HashMap<String, String> CLIENTE_MENSAGEM;

    public static void main(String args[]) {
        try {
            MAP_CLIENTES = new HashMap<String, PrintStream>();
            CLIENTE_MENSAGEM = new HashMap<String, String>();
            ServerSocket server = new ServerSocket(5555);
            System.out.println("ServidorSocket rodando na porta 5555");
            while (true) {
                conexao = server.accept();
                Thread t = new ServerJob(conexao,LISTA_DE_NOMES,LISTA_DE_IP, MAP_CLIENTES,CLIENTE_MENSAGEM);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }


}
