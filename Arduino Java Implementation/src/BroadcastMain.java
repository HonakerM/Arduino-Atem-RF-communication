import java.io.*;


public class BroadcastMain {
	
	public static BroadcastGraphic BroadcastGraphic;
	public static BroadcastInput BroadcastInput; 
	public static Thread BroadcastInputThread;
	public static PrintWriter BroadcastOutput;
	public static String port;
	public static int baudRate;

	public static void main(String[] args) {

		//read through arguments
		if(args.length == 0){
			BroadcastInput = new BroadcastInput();
		} else if(args.length !=2){
			System.out.println("usage: java BroadcastMain [port] [baudRate]");
			System.exit(0);
		} else {
			try {
				BroadcastInput = new BroadcastInput(args[0], Integer.parseInt(args[1]));
			}catch(NumberFormatException error){
				System.out.println("error [baudrate] must be a number");
			}
		}

		//star broadcast input thread
		BroadcastInputThread = new Thread(BroadcastInput);
		BroadcastInputThread.start();


		//start graphic
		BroadcastGraphic = new BroadcastGraphic();
		

	}


}