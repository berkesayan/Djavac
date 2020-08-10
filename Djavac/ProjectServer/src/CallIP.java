package src;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CallIP {

	public String IP = "";

	public String callIP() {
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();
			System.out.println(String.format("networkInterface: %s", networkInterface.toString()));
			System.out.println("******************");
			System.out.println(networkInterface);
			try {
				if (!networkInterface.isUp()) {
					continue;
				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				int npf = interfaceAddress.getNetworkPrefixLength();
				InetAddress address = interfaceAddress.getAddress();
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast == null && npf != 8 && networkInterface.getName() == "wlan0") {
					System.out.println(String.format("IPv6: %s; Network Prefix Length: %s", address, npf));
				} else {
					System.out.println(
							String.format("IPv4: %s; Subnet Mask: %s; Broadcast: %s", address, npf, broadcast));
					System.out.println("?????????????" + address.toString());
					if (address.toString().indexOf('/') > -1) {
						if (address.toString().indexOf("192.168.1.") > -1) {
							IP = address.toString().replace("/", "");
						}
					}

				}

			}
		}
		return IP;
	}

}
