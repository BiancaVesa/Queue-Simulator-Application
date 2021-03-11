package queue_simulation;
import queue.*;
import java.util.*;
import java.io.*;

public class ClientGenerator implements Runnable {

    public int timeLimit;
    public int tArrivalMax;
    public int tArrivalMin;
    public int tServiceMax;
    public int tServiceMin;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    public List<Client> generatedClients = new ArrayList<Client>();
    public Scheduler scheduler;
    private ArrayList<Client> allClients = new ArrayList<Client>();
    private float avgWTime = 0;
    PrintWriter writer;

    public ClientGenerator(String fileIn, String fileOut) {
        try {
            readWriteData(fileIn, fileOut);
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e1) {
        }
        scheduler = new Scheduler(numberOfServers, (numberOfClients / numberOfServers) + 1);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomClients();
    }

    private void generateNRandomClients() {
        int i = 1;

        while (i <= numberOfClients) {
            Random r = new Random();
            int tArrival = r.nextInt(tArrivalMax - tArrivalMin) + tArrivalMin;
            int tService = r.nextInt(tServiceMax - tServiceMin) + tServiceMin;
            generatedClients.add(new Client(i, tArrival, tService));
            i++;
        }
        Client.sorting(generatedClients);
    }

    public void readWriteData(String fileIn, String fileOut) throws FileNotFoundException, UnsupportedEncodingException {
        File f = new File(fileIn);
        Scanner scan = new Scanner(f);
        numberOfClients = Integer.parseInt(scan.nextLine());
        numberOfServers = Integer.parseInt(scan.nextLine());
        timeLimit = Integer.parseInt(scan.nextLine());
        String s = scan.nextLine();
        int i = s.indexOf(',');
        tArrivalMin = Integer.parseInt(s.substring(0, i));
        tArrivalMax = Integer.parseInt(s.substring(i + 1, s.length()));
        s = scan.nextLine();
        i = s.indexOf(',');
        tServiceMin = Integer.parseInt(s.substring(0, i));
        tServiceMax = Integer.parseInt(s.substring(i + 1, s.length()));
        writer = new PrintWriter(fileOut, "UTF-8");
    }

    public void run() {
        int currentTime = 0;
        ArrayList<Client> toRemove = new ArrayList<>();

        while (currentTime <= timeLimit) {
            if (!Thread.interrupted()) {
                for (Client c : generatedClients) {
                    if (c.getTArrival() == currentTime) {
                        scheduler.dispatchClient(c);
                        toRemove.add(c);
                    }
                }
                generatedClients.removeAll(toRemove);

                writer.println("Time: " + currentTime);
                writer.print("Waiting clients: ");
                if (!generatedClients.isEmpty()) {
                    for (Client c : generatedClients) {
                        writer.print(c);
                    }
                }
                writer.println();
                for (Server serv : scheduler.getServers()) {
                    writer.println(serv);
                }
                writer.println();
                currentTime++;
                try {
                    Thread.currentThread().sleep(1000);
                    for (Server s : scheduler.getServers()) {
                        if (!s.isEmptyQueue()) {
                            Client[] c = s.getClients();
                            if (c[0].getTService() == 1) {
                                allClients.add(c[0]);
                                s.removeClient(c[0]);
                            } else c[0].decTService();
                            if (c.length > 1) {
                                for (int i = 1; i < c.length; i++) {
                                    c[i].incWaitingTime();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        }
        if (currentTime == timeLimit + 1) {
            for (Client c : allClients) {
                avgWTime += c.getWaitingTime();
            }
            avgWTime /= numberOfClients;
            writer.println("Average waiting time:  " + avgWTime);
            writer.close();
        }
    }

    public static void main(String args[]) {
        ClientGenerator gen = new ClientGenerator(args[0], args[1]);
        Thread t = new Thread(gen);
        t.start();
    }
}
