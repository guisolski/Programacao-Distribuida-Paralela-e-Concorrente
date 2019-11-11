import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Servidor extends Thread {
    private static HashMap<String, PrintStream> MAP_CLIENTES;
    private Socket conexao;
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
                Socket conexao = server.accept();
                Thread t = new ServeJob(conexao,LISTA_DE_NOMES,LISTA_DE_IP, MAP_CLIENTES,CLIENTE_MENSAGEM);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }


}