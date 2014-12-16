clear all
load angle.txt

figure % create new figure
subplot(2,1,1) % first subplot
plot(angle(:,1),'')
hold on
grid
plot(angle(:,2), 'r')
plot(angle(:,5), 'k')
title('Gyroscope')
legend('Raw gyroscope value','Filtered gyroscope value','Final angle', 'location', 'southeast')
ylabel('deg')
xlabel('periods')
hold off

subplot(2,1,2) % second subplot
plot(angle(:,3))
hold on
grid
plot(angle(:,4), 'r')
plot(angle(:,5), 'k')
title('Accelerometer')
legend('Raw accelerometer value','Filtered accelerometer value', 'Final Angle', 'location', 'southeast')
ylabel('deg')
xlabel('periods')
hold off
