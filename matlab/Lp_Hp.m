s = tf('s');
Ghp = s/(s+10);
Glp = 1/(s+1);
hold on
bode(Ghp)
bode(Glp)
title('High and low pass filter')
legend('High pass','Low pass')
grid
hold off
h = 0.010;
Hhp = c2d(Ghp,h ,'tustin')
Hlp = c2d(Glp,h ,'tustin')