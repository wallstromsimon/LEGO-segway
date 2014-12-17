clear all
load feedbackData.txt

figure % create new figure
subplot(2,1,1) % first subplot
plot(feedbackData(:,1),'')
hold on
grid
plot(feedbackData(:,3), 'g')
plot(feedbackData(:,5), 'k')
title('Wheel angle and angular velocity')
legend('Theta','ThetaDot','u', 'location', 'northeast')
ylabel('%-power, deg, deg/sec')
xlabel('periods')
hold off

subplot(2,1,2) % second subplot
plot(feedbackData(:,2))
hold on
grid
plot(feedbackData(:,4), 'g')
plot(feedbackData(:,5), 'k')
title('Wheel angle and angular velocity, limited')
legend('L2 * Theta','L4 * ThetaDot','u', 'location', 'northeast')
ylabel('%-power, deg, deg/sec')
xlabel('periods')
hold off
