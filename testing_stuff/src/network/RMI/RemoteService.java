package network.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteService extends UnicastRemoteObject implements IRemote {
    public RemoteService() throws RemoteException {}

    @Override
    public String getRemoteServiceData() throws RemoteException {
        return "Hello form remote service!";
    }
}
