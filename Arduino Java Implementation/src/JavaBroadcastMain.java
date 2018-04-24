import jssc.*;

import static jssc.SerialPort.BAUDRATE_9600;

public class JavaBroadcastMain {
    public static void main(String[] args)
    {
        String[] portNames = SerialPortList.getPortNames();
        System.out.println(BAUDRATE_9600);
        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return;
        }

        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }

        SerialPort serialPort = new SerialPort("COM1");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.writeBytes("This is a test string".getBytes());//Write data to port
            serialPort.closePort();//Close serial port
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}


