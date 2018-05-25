# MBHS Robotics Segway 2018

This project aims to revive the old segway made in 2010 by switching out the VEX Cortex that used to control the segway with a Raspberry Pi. The benefits of this are that the RasPi is significantly smaller, cheaper, easier to work with, and more compatible with accessories. We started the project with a steel frame for the segway, two old wheelchair motors and wheels, and two Victor 883 Motor Controllers for each motor. 

### Prerequisites

What things you need to install the software and how to install them

```
motorSignals.ino is arduino code. 
Server.py is meant to be run on a Raspberry Pi
```

```
To run it, simply compile the arduino code onto some arduino and run
sudo python Server.py
```


## How it Works

The phone is mounted to the handlebar of the segway. With the phone connected to the Raspberry Pi's access point and Server.py running, use the app to send angle data to the RasPi.    

## Future Improvement

The segway can be improved by redoing the circuitry and replacing the current electrical components with newer parts. The constants for the PID have to be further tested and tuned.

## Authors

* **Reid Brown** - *Programmer* - [reidhbr](https://github.com/reidhbr)
* **Prem Chandrasekhar** - *Mechanic* - [prchandr](https://github.com/prchandr)
* **Ben Namovicz** - *Mechanic* - [Feed.](https:officialfeedband.com)
* **Stuart Nevans-Locke** - *Programmer* - [stnevans](https://github.com/stnevans)

