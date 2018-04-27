#include <VirtualWire.h>

#include <Wire.h>
#include <LiquidCrystal_I2C.h>


//rf variables and decleration
const int receive_pin = 2; //!purple
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
uint8_t upPin = A0;
uint8_t setPin = A1;
int powerpinA = 6;
int powerpinB = 7;
int powerpinC = 5;
int powerpinD = 8; //! blue
int groundpinA = 9; //! grey
//start variable definition
boolean rfConnect = false;
boolean idSet = false;

//Atem variable definition
String currentId;

void setup() {
	//pin setup
	pinMode(upPin, INPUT);
	pinMode(setPin, INPUT);
	pinMode(powerpinA, OUTPUT);
	pinMode(powerpinB, OUTPUT);
	pinMode(powerpinC, OUTPUT);
	pinMode(powerpinD, OUTPUT);
	pinMode(groundpinA, OUTPUT);
	digitalWrite(powerpinA, HIGH);
	digitalWrite(powerpinB, HIGH);
	digitalWrite(powerpinC, HIGH);
	digitalWrite(powerpinD, HIGH);
	digitalWrite(groundpinA, LOW);


	Serial.begin(9600);
	//led set up
	//TODO: Implement on HardWare
	/*
	pinMode(redPin, OUTPUT);
	pinMode(greenPin, OUTPUT);
	pinMode(bluePin, OUTPUT);
	digitalWrite(redPin, LOW);
	digitalWrite(greenPin, LOW);
	digitalWrite(bluePin, HIGH);
	*/

	//lcd init
	lcd.init();  //initialize the lcd
	lcd.backlight();  //open the backlight
	lcd.setCursor(0, 0);
	lcd.print("welcome");


	//rf setup//
	vw_set_rx_pin(receive_pin);
	vw_setup(2000);
	vw_rx_start();

	
	


	//set device id
	lcd.clear();
	lcd.setCursor(0, 0);
	lcd.print("Id:"+ String(deviceId));
	long time = millis();
	while (!idSet) {

		if (analogRead(upPin) > 1000) {
			lcd.clear();
			lcd.setCursor(0, 0);
			lcd.print("Device ID set");
			idSet = true;
		}
		if (millis() > (time + 1000)) {
			time = millis();
			deviceId++;
			if (deviceId > 8) {
				deviceId = 1;
			}
			lcd.clear();
			lcd.setCursor(0, 0);
			lcd.print("Id:" + String(deviceId));
		}
		
	}
	lcd.clear();
	lcd.setCursor(0, 0);
	lcd.print("Id set:" + String(deviceId));
	cycleLights();





}

void loop() {
	
	if (vw_have_message()) {
		vw_get_message(buf, buflen);
		String device = (char*)buf;
		Serial.println(device);
		if (device.substring(0, 1).equals(String(deviceId)) || device.substring(0, 1) == 0) {
			lcd.setCursor(0, 0); // set the cursor to column 3, line 0
			if (device.substring(2).toInt()>0) {
				
				if (device.substring(2).toInt() == 1) {
					
					lcd.print("PROGRAM ");
					//currentId = "PROGRAM";
					/*
					digitalWrite(redPin, HIGH);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, LOW);
					*/
				}
				else if (device.substring(2).toInt() == 2) {
					
					lcd.print("PREVIEW ");
					
					//currentId = "PREVIEW";
					/*
					digitalWrite(redPin, LOW);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, HIGH);
					*/
				}
				else if (device.substring(2).toInt() == 3) {
					
					lcd.print("        ");
					
					//currentId = "";
					/*
					digitalWrite(redPin, LOW);
					digitalWrite(bluePin, LOW);
					digitalWrite(greenPin, HIGH);
					*/
				}
			} else if (device.substring(2).toInt() == 0) {
				
				
				lcd.setCursor(0, 1);
				lcd.print(device.substring(2));
				lcd.setCursor(0, 0);
			}
		} else {
			lcd.setCursor(0, 0);
			lcd.print("        ");
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