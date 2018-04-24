import javax.swing.UIManager.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.ServerSocket;
import javax.swing.filechooser.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;
import gnu.io.*;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class BroadcastMain {
	
	public static BroadcastGraphic BroadcastGraphic;
	public static BroadcastInput BroadcastInput; 
	public static Thread BroadcastInputThread;
	public static PrintWriter BroadcastOutput;
	public static String port;
	public static int baudRate;

	public static void main(String[] args) {
		BroadcastGraphic = new BroadcastGraphic();

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

		BroadcastInputThread = new Thread(BroadcastInput);
		BroadcastInputThread.start();
		
		

	}


}