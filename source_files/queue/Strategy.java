package queue;

import queue.Client;
import queue.Server;

import java.util.List;

public interface Strategy {
    public void addClient(List<Server> servers, Client c);
}
