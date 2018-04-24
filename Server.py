import socket, math, serial, time

HOST = ''
PORT = 101
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind((HOST, PORT))
s.listen(1)
conn, addr = s.accept()
print ('Connected to ', addr)
data = ''
azimuth = ''
pitch = ''
roll = ''
right = 127
left = 127
rPWM = 127

ser = serial.Serial('/dev/ttyACM0', 9600)

try:
    while 1:
        if ser.in_waiting > 0:
            print(str(ord(ser.read()))+ "," +  str(ord(ser.read()))+","+ str(ord(ser.read())))
        data = conn.recv(1024)
        data = data.decode('utf-8')
        dataArray = data.split(',')
        #Check if messed up
        if len(dataArray)<5:
            roll = float(dataArray[len(dataArray) - 1])
            pitch = dataArray[len(dataArray) - 2]
            rPWM = abs(float(pitch))/180*255
            azimuth = dataArray[len(dataArray) - 3]
            print('rPWM ' + str(rPWM) + '\n')
            #print('roll ' + roll)
            count = rPWM
            #print(chr (count))
            #right,left
            if roll>0:
                right = int(max(0, min(count, 255)))
                left = int(max(0, min(count - roll, 255)))
            else:
                right = int(max(0, min(count + roll, 255)))
                left = int(max(0, min(count, 255)))
            ser.write(chr(right) + chr(44)+ chr(left))
            #print('right ' + str(right) + 'left ' + str(left))
        else:
                print('Error occurred with data:   ' + str(dataArray) + '\n')
except(KeyboardInterrupt, SystemExit):
    ser.write(chr(127) + chr(44)+ chr(127))
    s.shutdown(socket.SHUT_RDWR)
    conn.close()
    s.close()
