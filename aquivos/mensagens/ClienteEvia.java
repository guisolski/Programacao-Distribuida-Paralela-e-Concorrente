import java.io.BufferedReader;
import java.io.PrintStream;

public class ClienteEvia extends Thread  {
    private  BufferedReader teclado;
    private PrintStream saida;
    public ClienteEvia(BufferedReader teclado, PrintStream saida) {
        this.saida = saida;
        this.teclado = teclado;
    }
    public void run()
    {
        try {
            String msg;
            while (true)
            {
                    // cria linha para digitação da mensagem e a armazena na variavel msg
                    System.out.print("Ação: ");
                    msg = teclado.readLine();
                    // envia a mensagem para o servidor
                    saida.println(msg);


            }
        } catch (Exception e) {
            // caso ocorra alguma exceção de E/S, mostra qual foi.
            System.out.println("Ocorreu uma Falha... .. ." +
                    " IOException: " + e);
        }
    }
}
