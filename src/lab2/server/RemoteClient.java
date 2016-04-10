//package lab3.server;

//import lab3.server.Player;
//import lab3.server.RemoteService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemoteClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        errorHandler(args.length != 2, "Wrong call. Arguments: <nick> <single|multi>");
        errorHandler(!args[1].equals("single") && !args[1].equals("multi"), "Wrong game type. Choose single or multi");

        final String nick = args[0];
        final String gameType = args[1];
        final RemoteService service = (RemoteService) Naming.lookup("rmi://localhost:1099/tictactoe");
        final Player player = new Player(nick);
        service.registerPlayer(player);
        service.chooseGame(player, gameType);

    }

    public static void errorHandler(Boolean statement, String error) {
        if (statement) {
            System.out.println(error);
            System.exit(-1);
        }
    }
}
