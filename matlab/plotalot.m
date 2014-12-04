clear all
load data\p.txt
load data\i.txt
load data\d.txt
load data\e.txt
load data\y.txt
load data\u.txt

plot(p)
hold on
grid
plot(i, 'r')
plot(d, 'g')
plot(e, 'y')
plot(u, 'p')
plot(y, 'm')
legend('p','i','d','e','u', 'y', 'Location','northwest')
hold off