/*
* Message is code '0' but in actually will be straight message
* Program is code 1
* Preview is code 2
* Off is code 3
*/
#include <Dhcp.h>
#include <Dns.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>
#include <EthernetUdp.h>

#include <ATEMstd.h>

#include <VirtualWire.h>


ATEMstd AtemSwitcher;

uint16_t delayBetweenValues = 30;
long programVal, previewVal;
long programValorg = -1;
long previewValorg = -1;

const int transmit_pin = 12;
uint8_t buf[VW_MAX_MESSAGE_LEN];
uint8_t buflen = VW_MAX_MESSAGE_LEN;

IPAddress switcherIp(192, 168, 0, 240);
EthernetServer server(23);
String str;
byte mac[] = {
	0x90, 0xA2, 0xDA, 0x00, 0xE8, 0xE9 };
IPAddress clientip(192, 168, 0, 175);
boolean serialConnected = false;

void setup() {
	//ethernet initialization
	Ethernet.begin(mac, clientip);
	server.begin();

	//initialize connection to switcher
	AtemSwitcher.begin(switcherIp);
	AtemSwitcher.serialOutput(2);
	AtemSwitcher.connect();

	//wait for connection
	while (!AtemSwitcher.hasInitialized()) {
		AtemSwitcher.runLoop();
	}

	//rf setup//
	vw_set_tx_pin(transmit_pin);
	vw_setup(2000);

	//Serial set up
	Serial.begin(57600);
	while (!Serial) {
		; // wait for serial port to connect. Needed for native USB port only
	}
	Serial.println("connect");
	while (!serialConnected) {
		if (Serial.available()>0) {
			str = readSerial();
			if (str.toInt() == 1) {
				Serial.println(">connected");
				serialConnected = true;
			}
		}
	}
	while (!Serial.available()>0) {}

	Serial.println(">Ready to transmit");



}

void loop() {

	//camera update for rf
	programVal = AtemSwitcher.getProgramInput();
	AtemSwitcher.runLoop(delayBetweenValues); // Short delay


	// Preview Input - Video Source
	previewVal = AtemSwitcher.getPreviewInput();
	AtemSwitcher.runLoop(delayBetweenValues); // Short delay

	 //update camera RF
	if (previewVal != previewValorg) {

		str = previewVal + ":2";
		char msg[3];
		str.toCharArray(msg, 3);
		vw_send((uint8_t *)msg, 3);
		vw_wait_tx();
		if (Serial) {
			Serial.flush();
			Serial.println(">V:" + String(previewVal) + ":2");
		}
	}
	if (programVal != programValorg) {
		str = programVal + ":1";
		char msg[3];
		str.toCharArray(msg, 3);
		vw_send((uint8_t *)msg, 3);
		vw_wait_tx();
		if (Serial) {
			Serial.flush();
			Serial.println(">V:" + String(programVal) + ":2");
		}
	}



	//listen for lcd change from ethernet
	str = "";
	if (Serial.available()>0) {
		str = readSerial();

		Serial.flush();
		char msg[str.length()];
		str.toCharArray(msg, str.length());
		vw_send((uint8_t *)msg, str.length());
		vw_wait_tx();

	}


	//update values
	programValorg = programVal;
	previewValorg = previewVal;

}


boolean array_cmp(int *a, int *b) {
	int n;

	// test each element to be the same. if not, return false
	for (n = 0; n<8; n++) if (a[n] != b[n]) return false;

	//ok, if we have not returned yet, they are equal :)
	return true;
}

//method to read serial from computer
String readSerial() {
	String str = "";
	int c;
	if (Serial.available()>0) {
		if ((char)(c = Serial.read()) == '>') {
			while ((char)(c = Serial.read()) != '<') {
				if (c != -1) {
					str = str + (char)c;
				}

			}
		}
	}
	serialFlush();
	Serial.println(str);
	return str;
}

//method to clear Serial buffer
void serialFlush() {
	while (Serial.available() > 0) {
		char t = Serial.read();
	}
}
