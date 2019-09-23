import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class barbearia {
    private static int barbeiro ,cadeira, aberta;
    private static Condition barbeiro_disponivel ,cadeira_ocupada,porta_aberta,cliente_saiu;
    private  static Lock mutex;
    public barbearia()
    {
        this.mutex = new ReentrantLock();
        this.barbeiro_disponivel = mutex.newCondition();
        this.cadeira_ocupada = mutex.newCondition();
        this.porta_aberta = mutex.newCondition();
        this.cliente_saiu = mutex.newCondition();
        this.barbeiro = 0;
        this.aberta = 0;
        this.cadeira = 0;
    }
    public static void  corta_cabelo(int id) throws InterruptedException {
            System.out.println("O cliente " + id + " está esperando para cortar o cabelo");
            mutex.lock();
            try {
                    while (barbeiro == 0)
                    barbeiro_disponivel.await();
                barbeiro = barbeiro -1;
                cadeira = cadeira +1;
                cadeira_ocupada.signal();
                System.out.println("O cliente " + id + " está cortando cabelo");
                while (aberta == 0) porta_aberta.await();
                aberta  = aberta -1;
                cliente_saiu.signal();
                cliente_saiu.signal();
                System.out.println("O cliente " + id + " saiu");
            } finally {
                mutex.unlock();
            }
    }

    public static void pegue_proximo_cliente() throws InterruptedException {
        mutex.lock();
        barbeiro = barbeiro + 1;
        barbeiro_disponivel.signal();
        System.out.println("Barbeiro Disponivel");
        while ( cadeira == 0 ) {
            try {
                cadeira_ocupada.await();
            } finally {
                mutex.unlock();
            }
        }
        cadeira = cadeira-1;
    }

    public static void termine_corte() throws InterruptedException {
        mutex.lock();
        aberta = aberta + 1;
        porta_aberta.signal();
        while ( aberta > 0 ) {
            try {
                cliente_saiu.await();
            } finally {
                mutex.unlock();
            }
        }

    }
}
