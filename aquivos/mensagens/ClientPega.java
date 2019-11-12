import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientPega extends Thread {
    private Socket conexao;


    // construtor que recebe o socket do cliente
    public ClientPega(Socket socket) {
        this.conexao = socket;
    }

    public void run()
    {
        try {
            //recebe mensagens de outro cliente através do servidor
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            //cria variavel de mensagem
            String msg;
            while (true)
            {
                    // pega o que o servidor enviou
                    msg = entrada.readLine();
                    //se a mensagem contiver dados, passa pelo if,
                    // caso contrario cai no break e encerra a conexao
                    if (msg == null) {
                        System.out.println("Conexão encerrada!");
                        System.exit(0);
                    }
                    System.out.println();
                    //imprime a mensagem recebida
                    System.out.println(msg);
                    //cria uma linha visual para resposta
                    System.out.print("Ação: ");
            }
        } catch (Exception e) {
            // caso ocorra alguma exceção de E/S, mostra qual foi.
            System.out.println("Ocorreu uma Falha... .. ." +
                    " IOException: " + e);
        }
}
}
