package queue;

import queue.Client;
import queue.ConcreteStrategyTime;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<Server> servers = new ArrayList<Server>();
    private int maxNoServers;
    private int maxClientsPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxClientsPerServer) {
        //se creeaza coada noua cu un vanzator, iar apoi pornim un thread pentru acesta
        this.maxNoServers = maxNoServers;
        this.maxClientsPerServer = maxClientsPerServer;
        int i = 1;
        while (i <= this.maxNoServers) {
            Server server = new Server(this.maxClientsPerServer, i);
            servers.add(server);
            Thread t = new Thread(server);
            t.start();
            i++;
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchClient(Client c) {
        strategy.addClient(servers, c);
    }

    public List<Server> getServers() {
        return servers;
    }
}
