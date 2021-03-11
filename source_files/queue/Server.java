package queue;

import queue.Client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod = new AtomicInteger(0);
    private int nrCrt;

    public Server(int maxNoClients, int nrCrt) {
        //initializam prima coada si resetam perioada de asteptare
        clients = new ArrayBlockingQueue<Client>(maxNoClients);
        this.nrCrt = nrCrt;
    }

    public void addClient(Client newClient) {
        //adaugam un nou client la coada si incrementam timpul de asteptare
        clients.add(newClient);
        int t = waitingPeriod.intValue() + newClient.getTService();
        waitingPeriod.set(t);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void run() {
        //se proceseaza pe rand fiecare client, blocam coada pentru un timp de ArrivalTime milisecunde
        //timpul de asteptare scade
        while (!clients.isEmpty()) {
            Client c = clients.peek();
            try {
                Thread.currentThread().sleep(c.getTService() * 1000);
            } catch (InterruptedException ex) {
            }
            waitingPeriod.getAndDecrement();
        }
    }

    public Client[] getClients() {
        Client[] c = new Client[]{};
        return clients.toArray(c);
    }

    public boolean isEmptyQueue() {
        if (clients.isEmpty())
            return true;
        else return false;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public String toString() {
        String s = "Queue " + nrCrt + ": ";
        if (clients.isEmpty())
            return "Queue " + nrCrt + ": closed";
        else {
            Client[] cl = getClients();
            for (Client c : cl)
                s += c + " ";
        }
        return s;
    }
}

