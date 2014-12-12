clear all
load data.txt %PIDData nästa gång.

figure % create new figure
subplot(2,1,1) % first subplot
plot(data(:,1),'')
hold on
grid
plot(data(:,2), 'r')
plot(data(:,3), 'g')
plot(data(:,5), 'k')
title('P, I, D variables and output power')
legend('p','i','d','u')
ylabel('%-power')
xlabel('periods')
hold off

subplot(2,1,2) % second subplot
plot(data(:,4), 'y')
hold on
grid
plot(data(:,5), 'k')
plot(data(:,6), 'm')
title('Angle, error and output power')
legend('e','u', 'y')
ylabel('%-power, deg')
xlabel('periods')
hold off
