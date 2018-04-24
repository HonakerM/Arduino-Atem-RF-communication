import java.util.*;
import java.net.*;
import java.io.*;
import jssc.*;
import static jssc.SerialPort.BAUDRATE_57600;

public  class BroadcastInput implements Runnable {
	public static String port;
	public static int baudRate;
	public static Socket mainsocketin;
	public static String mainLineIn;
	public static BufferedReader mainInputStream;
	public static PrintWriter sensorsocketout;
	public static SerialPort serialPort;
	public static boolean serialConnected = false;
	public static String message="";
	public static boolean Running = false;
    static BroadcastGraphic BroadcastGraphic;
    public static ArrayList<Camera> cameraDevices = new ArrayList<Camera>();
    public static ArrayList<String> serialBuffer = new ArrayList<String>();

	public BroadcastInput() {
	    Running =true;
		String[] portNames = SerialPortList.getPortNames();

		if (portNames.length == 0) {
			BroadcastGraphic.log("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
			BroadcastGraphic.log("Press Enter to exit...");
			try {
				System.in.read();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return;
		}

		port = portNames[0];
		baudRate = 9600;
	}

	public BroadcastInput(String portIn, int baudRateIn) {
        port = portIn;
        baudRate = baudRateIn;
	}
	public void run() {
		serialPort = new SerialPort(port);
		try {
		    BroadcastGraphic.log("Opening Serial Port:"+port);
			serialPort.openPort();//Open serial port
			serialPort.setParams(BAUDRATE_57600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);



            BroadcastGraphic.log("Connecting to device");
			if (!connectDevice()){
				BroadcastGraphic.log("timeout error");

				BroadcastMain.BroadcastGraphic.errorReporter("Serial Port timeout error");

			}



			while(Running){
                if(!serialBuffer.isEmpty()){
                    if((message = serialBuffer.get(0)) != null){
                        try {

                            System.out.println(message);
                            String messageIdentifier = message.substring(0, 1);
                            message = message.substring(2);
                            if (messageIdentifier.equals("N")) {
                                cameraDevices.add(new Camera(Integer.parseInt(message), Integer.parseInt(message)));
                                BroadcastGraphic.addDevice(cameraDevices.get(cameraDevices.size() - 1));
                                BroadcastGraphic.log("Camera " + Integer.parseInt(message) + " connected");
                            }
                            if (messageIdentifier.equals("V")) {
                                int deviceId = Integer.parseInt(message.substring(0, 1));
                                for (int i = 0; i < cameraDevices.size(); i++) {
                                    if (deviceId == cameraDevices.get(i).getCameraID()) {
                                        cameraDevices.get(i).setStatus(Integer.parseInt(message.substring(message.length() - 1)));
                                        BroadcastGraphic.updateDevices(cameraDevices);
                                        BroadcastGraphic.log("Updating camera " + deviceId + " status to " + Integer.parseInt(message.substring(message.length() - 1)));
                                    }
                                }
                            } else {
                                BroadcastGraphic.log(messageIdentifier + message);
                            }
                        } catch (NumberFormatException a) {
                            BroadcastGraphic.log("number format exception");
                        } catch (IndexOutOfBoundsException b) {
                            BroadcastGraphic.log("Camera out of bounds");
                        }
                        serialBuffer.remove(0);
                    }
                }

            }


		}
		catch (SerialPortException ex) {
			BroadcastGraphic.log(ex.toString());
            BroadcastMain.BroadcastGraphic.errorReporter("Serial Port exception error");
            System.out.println("Serial Port exception error");
		}
	}

	public static void send(String message){
		try {
			serialPort.writeString(">"+message +"<");
            BroadcastGraphic.log("Sending:"+message);
		}catch( SerialPortException error){
			BroadcastGraphic.log("unable to send");

			BroadcastMain.BroadcastGraphic.errorReporter("unable to send data");

		}
	}

	public boolean connectDevice(){
        long startTime = System.currentTimeMillis();
		while(!serialConnected){
			send("1");//Write data to port
            if(serialBuffer.contains("connected")){
                serialConnected = true;
                serialBuffer.clear();
                BroadcastGraphic.log("Connected to Serial Device");
                return true;

            }
            if(System.currentTimeMillis()>=(startTime+15000)){
                return false;
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex){
                BroadcastGraphic.log("Interrupted while connecting devices");
                BroadcastMain.BroadcastGraphic.errorReporter("unable to delay thread");
            }
		}
		return false;
	}
	public static void setCameraNumber(int DeviceID, int cameraNumber){
	    for(int i=0;i<cameraDevices.size();i++){
	        if(cameraDevices.get(i).getCameraID()==DeviceID){
	            int oldNumber=cameraDevices.get(i).getCamera();
	            send("c"+cameraNumber+DeviceID+oldNumber);
                cameraDevices.get(i).setCamera(cameraNumber);
                BroadcastGraphic.log("Camera "+oldNumber+" changed to Camera "+cameraNumber);
            }
        }
    }



}
class PortReader implements SerialPortEventListener {
    BroadcastInput BroadcastInput;

    StringBuilder message = new StringBuilder();
    Boolean receivingMessage = false;
    public void serialEvent(SerialPortEvent event) {
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                byte buffer[] = BroadcastInput.serialPort.readBytes();
                for (byte b: buffer) {
                    if (b == '>') {
                        receivingMessage = true;
                        message.setLength(0);
                    }
                    else if (receivingMessage) {
                        if (b == '\r') {
                            receivingMessage = false;
                            BroadcastInput.serialBuffer.add(message.toString());

                        }
                        else {
                            message.append((char)b);
                        }
                    }
                }
            }
            catch (SerialPortException ex) {
                BroadcastGraphic.log(ex.toString());
                BroadcastGraphic.log("serialEvent");
            }
        }
    }

}