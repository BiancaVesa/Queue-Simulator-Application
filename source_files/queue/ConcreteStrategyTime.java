package queue;

import queue.Client;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addClient(List<Server> servers, Client c) {
        Server minServ = servers.get(0);
        AtomicInteger minTime = minServ.getWaitingPeriod();
        for (Server s : servers){
                if (s.getWaitingPeriod().get() <= minTime.get()){
                    minServ = s;
                    minTime = minServ.getWaitingPeriod();
                }
        }
        minServ.addClient(c);
    }
}
