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
    private static Map<String, PrintStream> MAP_CLIENTES;
    private Socket conexao;
    private static List<String> LISTA_DE_NOMES = new ArrayList<>();
    private static List<String> LISTA_DE_IP = new ArrayList<>();


    public static void main(String args[]) {
        try {
            MAP_CLIENTES = new HashMap<String, PrintStream>();
            ServerSocket server = new ServerSocket(5555);
            System.out.println("ServidorSocket rodando na porta 5555");
            while (true) {
                Socket conexao = server.accept();
                Thread t = new ServeJob(conexao,LISTA_DE_NOMES,LISTA_DE_IP, MAP_CLIENTES);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }


}