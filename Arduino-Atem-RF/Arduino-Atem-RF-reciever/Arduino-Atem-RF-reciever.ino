#include <VirtualWire.h>

#include <Wire.h>
#include <LiquidCrystal_I2C.h>


//rf variables and decleration
const int receive_pin = 2;
int deviceId = 1;
String uniqueId;

uint8_t buf[VW_MAX_MESSAGE_LEN];
uint8_t buflen = VW_MAX_MESSAGE_LEN;


//LCD set up
LiquidCrystal_I2C lcd(0x27, 16, 2); // set the LCD address to 0x27 for a 16 chars and 2 line display

//LED and pin set up
int redPin = 11;
int greenPin = 12;
int bluePin = 13;
int upPin = 3;
int setPin = 4;

//start variable definition
boolean rfConnect = false;
boolean idSet = false;

//Atem variable definition
String currentId;

void setup() {
	//pin setup
	pinMode(upPin, INPUT);
	pinMode(setPin, INPUT);

	//led set up
	pinMode(redPin, OUTPUT);
	pinMode(greenPin, OUTPUT);
	pinMode(bluePin, OUTPUT);
	digitalWrite(redPin, LOW);
	digitalWrite(greenPin, LOW);
	digitalWrite(bluePin, HIGH);

	//rf setup//
	vw_set_rx_pin(receive_pin);
	vw_setup(2000);
	vw_rx_start();

	//lcd init
	lcd.init();  //initialize the lcd
	lcd.backlight();  //open the backlight
	lcd.setCursor(0, 0);
	lcd.print("welcome");
	


	//set device id
	lcd.print("");
	lcd.print("Id:" + deviceId);
	while (!idSet) {
		if (digitalRead(upPin) == HIGH) {
			deviceId++;
			if (deviceId>8) {
				deviceId = 1;
			}
			lcd.print("Id:" + deviceId);
		}
		else if (digitalRead(upPin) == HIGH) {
			lcd.print("");
			lcd.print("Device ID set");
			idSet = true;
		}
	}

	cycleLights();





}

void loop() {
	//wait for rf message
	vw_wait_rx_max(60000);
	if (vw_have_message()) {
		vw_get_message(buf, buflen);
		String device = (char*)buf;
		if (device.substring(0, 1).equals(String(deviceId)) || device.substring(0, 1) == 0) {
			if (device.substring(2).toInt()>0) {
				if (device.substring(2).toInt() == 1) {
					lcd.setCursor(0, 0); // set the cursor to column 3, line 0
					lcd.print("PROGRAM");
					currentId = "PROGRAM";

					digitalWrite(redPin, HIGH);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, LOW);
				}
				else if (device.substring(2).toInt() == 2) {
					lcd.setCursor(0, 0); // set the cursor to column 3, line 0
					lcd.print("PREVIEW");
					currentId = "PREVIEW";
					digitalWrite(redPin, LOW);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, HIGH);
				}
				else if (device.substring(2).toInt() == 3) {
					lcd.setCursor(0, 0); // set the cursor to column 3, line 0
					lcd.print("       ");
					currentId = "";
					digitalWrite(redPin, LOW);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, HIGH);
				}
			}
			else if (device.substring(2).toInt() == 0) {
				lcd.setCursor(0, 0);
				lcd.clear();
				lcd.print(currentId);
				lcd.setCursor(0, 1);
				lcd.print(device.substring(2));
			}
		}
	}
}

//method to simplify light cycle
void cycleLights() {
	digitalWrite(redPin, HIGH);
	digitalWrite(greenPin, LOW);
	digitalWrite(bluePin, LOW);
	delay(250);
	digitalWrite(redPin, LOW);
	digitalWrite(greenPin, HIGH);
	digitalWrite(bluePin, LOW);
	delay(250);
	digitalWrite(redPin, LOW);
	digitalWrite(greenPin, LOW);
	digitalWrite(bluePin, HIGH);
	delay(250);
}