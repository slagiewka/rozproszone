package lab4.Server;

import lab4.Persistance.H2Init;

public class ServerRunner {
    public static void main(String[] args) {
        H2Init.resetDatabase();
        new Thread(new ServerThread(args)).start();
    }
}
