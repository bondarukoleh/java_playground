package network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemote extends Remote {
    public String getRemoteServiceData() throws RemoteException;
}
