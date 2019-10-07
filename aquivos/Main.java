import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        barbearia barb = new barbearia();
        barbeiro c1 = new barbeiro(barb);
        ArrayList<cliente> clientes = new ArrayList<>();
        c1.start();
        for(int i = 1; i <= 10 ; i++)
            clientes.add(new cliente(barb,i));
        for(cliente i : clientes)
            i.start();

    }
}
