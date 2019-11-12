import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.Socket;

public class Cliente {

    public static void main(String args[]) {
        try {
            //Instancia do atributo conexao do tipo Socket,
            // conecta a IP do Servidor, Porta
            Socket socket = new Socket("127.0.0.1", 5555);
            //Instancia do atributo saida, obtem os objetos que permitem
            // controlar o fluxo de comunicação
            PrintStream saida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome: ");
            String meuNome = teclado.readLine();
            //envia o nome digitado para o servidor
            saida.println(meuNome.toLowerCase());
            //envia o ip do usuario
            saida.println(Inet4Address.getLocalHost().getHostAddress());
            //instancia a thread para ip e porta conectados e depois inicia ela
            Thread thread = new ClientPega(socket);
            thread.start();
            thread = new ClienteEvia(teclado, saida);
            thread.start();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }

}
