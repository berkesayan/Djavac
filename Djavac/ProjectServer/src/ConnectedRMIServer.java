import java.net.Socket;
import java.util.ArrayList;

public class ConnectedRMIServer {
	public boolean isWorking = false; // flag
	public String IPAddress = "";
	public int connectedClientCount = 0;

	public ConnectedRMIServer(String IPAddress, boolean isWorking, int connectedClientCount) {
		this.IPAddress = IPAddress;
		this.isWorking = isWorking;
		this.connectedClientCount = connectedClientCount;
	}

	public static ConnectedRMIServer GetAvailableServer(ArrayList<ConnectedRMIServer> servers) throws Exception {
		ConnectedRMIServer ret = servers.get(0);

		for (ConnectedRMIServer sv : servers) {
			try {
				if (sv.connectedClientCount < ret.connectedClientCount) {
					if (CheckAvailableClientSocket.execute(sv.IPAddress)) {
						ret = sv;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}

		ret.connectedClientCount++;
		return ret;
	}
}
