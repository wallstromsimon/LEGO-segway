clear all
load feedbackData.txt

figure % create new figure
subplot(2,1,1) % first subplot
plot(feedbackData(:,1),'')
hold on
grid
plot(feedbackData(:,3), 'g')
plot(feedbackData(:,5), 'k')
title('Segway vinkel och vinkelhastighet')
legend('Phi','PhiDot','u', 'location', 'northwest')
hold off

subplot(2,1,2) % second subplot
plot(feedbackData(:,2))
hold on
grid
plot(feedbackData(:,4), 'g')
plot(feedbackData(:,5), 'k')
title('Segway vinkel och vinkelhastighet, begränsad')
legend('L1 * Phi','L3 * PhiDot','u', 'location', 'northwest')
hold off
