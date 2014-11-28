data = load('data1.txt'); %load raw data

%data(x,1) angular velocity
%data(x,2) accelerometer x-value
%data(x,3) accelerometer z-value


interval = 0.05; %50ms
datapoints = 200;

%Angle by integrating the gyro, blue in plot
integratedAngle = zeros(datapoints,1);
integratedAngle(1,1) = data(1,1)*interval;
for x = 2:datapoints
    %if(abs(data(x,1)) < 1)
     %   data(x,1) = 0;
    %end
    integratedAngle(x,1) = integratedAngle(x-1,1) + data(x,1) * interval;
end

%Angle from accelerometer, red in plot
accAngle = zeros(datapoints,1);
for x = 1:datapoints
    accAngle(x,1) = -atan2(data(x,3),data(x,2))*180/pi;
end

%Final angle using both gyro and acc, green in plot
angle = zeros(datapoints,1);
angle(1,1) = 0;
for x = 2:datapoints
    if(abs(data(x,1)) < 1)
       data(x,1) = 0;
    end
    gyroangle = data(x,1) * interval;
    accangle = -atan2(data(x,3),data(x,2))*180/pi;
    angle(x,1) = (angle(x-1,1) + gyroangle) * 0.92 + accangle*0.08;
end

plot(integratedAngle)
grid
hold on
plot(accAngle,'r')
plot(angle,'g')
hold off