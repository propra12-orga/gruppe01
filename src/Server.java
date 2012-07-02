import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Enumeration;
import java.util.Vector;


public class Server // implements Runnable
{
		private Thread thread; // Server thread
		private Selector selector; // Selector für Multiplexen
		private int keysAdded=0; // Anzahl der connections
	//	private ByteBuffer buffer =
		
		public Server (int port)
		{
			init(port);
			Vector allServerSocketChannels = new Vector();
		} 

		
		public void init (int port)
		{
			// läd alle listening serversockets auf ein interface ??
			// Vector AllServerSocketChannel = new vector();
			
			try
			{
				//versuche selecor zum Multiplexen zu erstellen
				selector = Selector.open();
			}
			
			catch (Exception Ex)
			{
				System.err.println("could not open selector");
				Ex.printStackTrace();
			}
			
			
		
		try 
		{
			// erreichbare network interfaces 
			Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
			
			while (interfaces.hasMoreElements())
			{
				// bearbeite nächstes network interface
				NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
				// hole alle adressen für ausgewähltes network interface
				Enumeration addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements())
			{
				// process next address of selected network interface 
				InetAddress address = (InetAddress) addresses.nextElement();
				System.out.println("binding to port " + port + " on InetAddress " + address);
			// erstelle socket adresse für ausgewählte adresse und port
			InetSocketAddress isa = new InetSocketAddress(address,port);
			System.out.println("opening a non-blocking ServerSocketChannel on port " + port + " on InetAddress " + address);
			}
			}
			// öffne horchenden ServerSocketChannel
			ServerSocketChannel server = ServerSocketChannel.open();
			server.configureBlocking(false); // nicht blockierender modus
			server.socket().bind(null); // binde an Socket
			
			}

		

		catch (IOException ex)
		{
				System.err.println("could not start server!");
					ex.printStackTrace();
					System.exit(0);
			}
		}
	}
