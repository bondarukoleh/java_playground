package network.RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class RunRemote {
    public static void main(String[] args) {
        try {
            RemoteService remoteService = new RemoteService();
            // Create and export a remote object, as far as I get - we don't need this
//            IRemote remoteStub = (IRemote) UnicastRemoteObject.exportObject(remoteService, 3000);

            // When the remote object got binded to the rmiregistry it was attached with the loopback
            // IP address which will obviously fail if you try to invoke a method from a remote address.
            // In order to fix this we need to set the java.rmi.server.hostname property to the IP address
            // where other devices can reach your rmiregistry over the network.
            // In this case the IP address on the local network of the PC binding the remote object on the RMI Registry
            // is 192.168.56.1.
            System.setProperty("java.rmi.server.hostname","192.168.56.1");
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Say_Hello_Remote", remoteService);

            System.out.println("Remote service ready.");
        } catch (AlreadyBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
