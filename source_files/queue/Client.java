package queue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Client {
    private int ID;
    private int tArrival;
    private int tService;
    private int waitingTime;

    public Client(int ID, int tArrival, int tService){
        this.ID = ID;
        this.tArrival = tArrival;
        this.tService = tService;
        waitingTime = 0;
    }

    public int getTArrival(){
        return tArrival;
    }

    public int getTService(){
        return tService;
    }

    public int getWaitingTime(){
        return waitingTime;
    }

    public void incWaitingTime(){
        waitingTime++;
    }

    public void decTService(){
        tService--;
    }

    public static void sorting(List<Client> list){// metoda folosita pentru a sorta clientii din coada in ordine creascatoare a tArrival
        Collections.sort(list, new Comparator<Client>(){
            public int compare(Client c1, Client c2){
                if(c1.tArrival == c2.tArrival)
                    return 0;
                return c1.tArrival < c2.tArrival ? -1 : 1;
            }
        });
    }

    public String toString() {
        return "("+ID+", "+tArrival+", "+tService+")";
    }
}
