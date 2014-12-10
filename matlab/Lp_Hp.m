s = tf('s');
Ghp = s/(s+10);
Glp = 1/(s+1);
hold on
bode(Ghp)
bode(Glp)
hold off
h = 0.3;
Hhp = c2d(Ghp,h ,'tustin')
Hlp = c2d(Glp,h ,'tustin')