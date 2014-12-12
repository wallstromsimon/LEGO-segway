clear all
load PIDData.txt %PIDData nästa gång.

figure % create new figure
subplot(2,1,1) % first subplot
plot(PIDData(:,1),'')
hold on
grid
plot(PIDData(:,2), 'r')
plot(PIDData(:,3), 'g')
plot(PIDData(:,5), 'k')
title('P, I, D variables and output power')
legend('p','i','d','u')
ylabel('%-power')
xlabel('periods')
hold off

subplot(2,1,2) % second subplot
plot(PIDData(:,4), 'y')
hold on
grid
plot(PIDData(:,5), 'k')
plot((-1).*PIDData(:,4), 'm')%VAD HÄNDE MED DEN SISTA???
title('Angle, error and output power')
legend('e','u', 'y')
ylabel('%-power, deg')
xlabel('periods')
hold off
