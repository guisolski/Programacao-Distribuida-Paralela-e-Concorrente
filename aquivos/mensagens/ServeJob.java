import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerJob extends Thread {
    private static HashMap<String, PrintStream> MAP_CLIENTES;
    private static HashMap<String, String> CLIENTE_MENSAGEM;
    private Socket conexao;
    private String nomeCliente;
    private static ArrayList<String> LISTA_DE_NOMES;
    private static ArrayList<String> LISTA_DE_IP;
    private String ipCliente;

    public ServerJob(Socket socket, ArrayList<String> LISTA_DE_NOMES, ArrayList<String> LISTA_DE_IP, HashMap MAP_CLIENTES, HashMap CLIENTE_MENSAGEM) {
        this.MAP_CLIENTES = MAP_CLIENTES;
        this.conexao = socket;
        this.LISTA_DE_NOMES = LISTA_DE_NOMES;
        this.LISTA_DE_IP = LISTA_DE_IP;
        this.CLIENTE_MENSAGEM = CLIENTE_MENSAGEM;
    }

    public void run() {
        try {
            BufferedReader entrada =
                    new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.nomeCliente = entrada.readLine();
            this.ipCliente = entrada.readLine();
            if (armazena(this.nomeCliente, ipCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
                this.conexao.close();
                return;
            }
            if (this.nomeCliente == null) {
                this.conexao.close();
                return;
            }
            //adiciona os dados de saida do cliente no objeto MAP_CLIENTES
            //A chave será o nome e valor o printstream
            MAP_CLIENTES.put(this.nomeCliente, saida);
            String[] msg = entrada.readLine().split(":");

            while (msg != null && !(msg[0].trim().equals(""))) {
                if (msg[0].trim().equalsIgnoreCase("conectados")) {
                    saida.println("Conectados: " + LISTA_DE_NOMES.toString());
                } else if (msg[0].trim().equalsIgnoreCase("mensagens")) {
                   saida.println(this.CLIENTE_MENSAGEM.get(this.nomeCliente));
                } else {
                    if (msg.length == 2)
                        this.send(saida, " escreveu: ", msg[0], msg[1]);
                    else
                        saida.println("Mensagem invalida");
                }

                msg = entrada.readLine().split(":");
            }
            System.out.println(this.nomeCliente + " saiu do bate-papo!");
            send(saida, " saiu", " do bate-papo!", this.nomeCliente);
            remove(this.nomeCliente);
            MAP_CLIENTES.remove(this.nomeCliente);
            this.conexao.close();
        } catch (IOException e) {
            System.out.println("Falha na Conexao... .. ." + " IOException: " + e);
        }
    }

    public void send(PrintStream saida, String acao, String msg, String pessoa) {
        boolean achou = false;
        for (Map.Entry<String, PrintStream> cliente : MAP_CLIENTES.entrySet()) {
            PrintStream chat = cliente.getValue();
            if (chat != saida) {
               // System.out.println("entrou " + msg + cliente.getKey());
                if (pessoa.trim().equalsIgnoreCase(cliente.getKey())) {
                    if (this.CLIENTE_MENSAGEM.get(pessoa.trim()) != null) {
                        this.CLIENTE_MENSAGEM.put(pessoa.trim(), this.CLIENTE_MENSAGEM.get(pessoa.trim()) + "\n" + this.nomeCliente + acao + msg);
                    } else {

                        this.CLIENTE_MENSAGEM.put(pessoa.trim(), this.nomeCliente + acao + msg);
                    }
                    chat.println(this.nomeCliente + acao + msg);
                    achou = true;
                    break;
                }
            }

        }
        if(!achou && !acao.trim().equalsIgnoreCase("sistema")) {

        }
        if (!achou && !acao.trim().equalsIgnoreCase("saiu")) {
            if (this.CLIENTE_MENSAGEM.get(pessoa.trim()) != null) {
                this.CLIENTE_MENSAGEM.put(pessoa.trim(), this.CLIENTE_MENSAGEM.get(pessoa.trim()) + "\n" + this.nomeCliente + acao + msg);
            } else {
                System.out.println("aqui");
                this.CLIENTE_MENSAGEM.put(pessoa.trim(), this.nomeCliente + acao + msg);
            }
        }
    }

    public boolean armazena(String newName, String newIp) {
        if (LISTA_DE_NOMES.contains(newName)) return true;
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
