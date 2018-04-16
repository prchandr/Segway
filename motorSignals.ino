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
    // send data only when you receive data:
    while(true){
      if (Serial.available() > 0) {
       
        // read the incoming byte:
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
        // say what you got:
      
      }
      if(leftRead){
       leftRead=false;
                 Serial.write(leftMotorSpeed);Serial.write(comma);Serial.write(rightMotorSpeed);
        if(comma==44){
          rightMotor.writeMicroseconds(map(rightMotorSpeed, 0, 255, 1000, 2000));
          leftMotor.writeMicroseconds(map(leftMotorSpeed, 0, 255, 1000, 2000));
        }else{
        }
      }
    }
}
