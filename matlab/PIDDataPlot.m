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
title('Controller variables')
legend('p','i','d','u')
hold off

subplot(2,1,2) % second subplot
plot(data(:,4), 'y')
hold on
grid
plot(data(:,5), 'k')
plot(data(:,6), 'm')
title('Oth typ vinklar n shit')
legend('e','u', 'y')
hold off
