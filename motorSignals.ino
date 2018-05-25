/*
* This file is the code to be run on the arduino.
* It recieves how much to power the right and left motors over the serial communication
* In our current setup, the raspberry pi is plugged into it.
* This code simply powers the motor controller with the given numbers, it does no calculation.
*/

int rightMotorPin = 5;
int leftMotorPin = 3;
int leftMotorSpeed,comma,rightMotorSpeed;

#include "Servo.h"

Servo rightMotor;
Servo leftMotor;
void setup() {
  Serial.begin(9600);     // opens serial port, sets data rate to 9600 bps
  rightMotor.attach(rightMotorPin);
  leftMotor.attach(leftMotorPin);
}

boolean rightRead = false;
boolean commaRead=false;
boolean leftRead;


void loop() {
  /*
  * We expect data in the format "rightMotorSpeed,leftMotorSpeed"
  */
    while(true){
      if (Serial.available() > 0) {
        if(!rightRead){
          rightMotorSpeed = Serial.read();
          rightRead=true;
        }else if(!commaRead){
          comma = Serial.read();
          commaRead=true;
        }else{
          leftMotorSpeed = Serial.read();
          rightRead = false;
          commaRead = false;
          leftRead = true;
        }
      
      }
      if(leftRead){
       leftRead=false;
                 Serial.write(leftMotorSpeed);Serial.write(comma);Serial.write(rightMotorSpeed);//This part is just meant for debugging.
        if(comma==44){
          //Reference for communication with Victor 883 https://forum.arduino.cc/index.php?topic=58642.0
          //We recieve 0-255, 128 being no movement, 0 being full reverse.
          //That's then mapped from 1000-2000 for the Victor 883
          rightMotor.writeMicroseconds(map(rightMotorSpeed, 0, 255, 1000, 2000));
          leftMotor.writeMicroseconds(map(leftMotorSpeed, 0, 255, 1000, 2000));
        }else{
        }
      }
    }
}
