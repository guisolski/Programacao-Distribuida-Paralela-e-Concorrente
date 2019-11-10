import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServeJob extends Thread {
    private static Map<String, PrintStream> MAP_CLIENTES;
    private Socket conexao;
    private String nomeCliente;
    private static List<String> LISTA_DE_NOMES;
    private static List<String> LISTA_DE_IP;
    private String ipCliente;

    public ServeJob(Socket socket,List<String>  LISTA_DE_NOMES, List<String> LISTA_DE_IP,Map MAP_CLIENTES) {
        this.MAP_CLIENTES = MAP_CLIENTES;
        this.conexao = socket;
        this.LISTA_DE_NOMES = LISTA_DE_NOMES;
        this.LISTA_DE_IP = LISTA_DE_IP;
    }
    public void run() {
        try {
            BufferedReader entrada =
                    new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.nomeCliente = entrada.readLine();
            this.ipCliente = entrada.readLine();
            if (armazena(this.nomeCliente,ipCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
                this.conexao.close();
                return;
            } else {
                //mostra o nome do cliente conectado ao servidor
                System.out.println(this.nomeCliente + " : Conectado ao Servidor!");
                System.out.println(this.ipCliente + " : Ip cliente!");
                //Quando o cliente se conectar recebe todos que estão conectados
                saida.println("Conectados: " + LISTA_DE_NOMES.toString());
            }
            if (this.nomeCliente == null) {
                return;
            }
            //adiciona os dados de saida do cliente no objeto MAP_CLIENTES
            //A chave será o nome e valor o printstream
            MAP_CLIENTES.put(this.nomeCliente, saida);
            String[] msg = entrada.readLine().split(":");

            while (msg != null && !(msg[0].trim().equals(""))) {

                if(msg[0].trim().equalsIgnoreCase("mostrar")){
                    saida.println("Conectados: " + LISTA_DE_NOMES.toString());
                } else{
                    send(saida, " escreveu: ", msg);
                }
                msg = entrada.readLine().split(":");
            }
            System.out.println(this.nomeCliente + " saiu do bate-papo!");
            String[] out = {" do bate-papo!"};
            send(saida, " saiu", out);
            remove(this.nomeCliente);
            MAP_CLIENTES.remove(this.nomeCliente);
            this.conexao.close();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }
    /**
     * Se o array da msg tiver tamanho igual a 1, então envia para todos
     * Se o tamanho for 2, envia apenas para o cliente escolhido
     */
    public void send(PrintStream saida, String acao, String[] msg) {
        out:
        for (Map.Entry<String, PrintStream> cliente : MAP_CLIENTES.entrySet()) {
            PrintStream chat = cliente.getValue();

            if (chat != saida) {
                if (msg.length == 1) {
                    chat.println(this.nomeCliente + acao + msg[0]);
                } else {
                    System.out.println("entrou "+ msg + cliente.getKey());
                    System.out.println(msg[1].trim().equalsIgnoreCase(cliente.getKey()));
                    if (msg[1].trim().equalsIgnoreCase(cliente.getKey())) {
                        chat.println(this.nomeCliente + acao + msg[0]);
                        break out;
                    }
                }
            }
        }
    }
    public boolean armazena(String newName,String newIp) {
        if(LISTA_DE_NOMES.contains(newName)) return true;
        LISTA_DE_IP.add(newIp);
        LISTA_DE_NOMES.add(newName);
        return false;
    }
    public void remove(String oldName) {
        for (int i = 0; i < LISTA_DE_NOMES.size(); i++) {
            if (LISTA_DE_NOMES.get(i).equals(oldName))
                LISTA_DE_NOMES.remove(oldName);
        }
    }
}
