%% Prelab 5

% Segway paramaters
Jw = 1.6e-05;  % (kg*m^2) Wheel inertia
Rw = 0.0310; % (meters)  Radius of wheel
L = 0.0950; % (meters)  Length from wheel to body mass.
mb = 0.5910; % (kg) body mass
mw = 0.034; % (kg) combined mass of BOTH wheels
Jb = 0.0019; % (kg*m^2) Body inertia
b = 0.062; % damping factor (approximate!)
g = 9.81;  % (m/s^2) gravity
Nmotors=2;  % number of motors used
Klego=2;   % A scaling parameter for motor output (approx)
Km = Nmotors*b/Klego ; % Motor effort constant; Km = 0.062 (approx)


% State Space model (Linearized)
M33=Jw+Rw^2*mb+Rw^2*mw;
M34=L*Rw*mb;
M43=L*Rw*mb;
M44=Jb+L^2*mb;
A42=L*g*mb;

M=[ 1 0 0   0
    0 1 0   0
    0 0 M33 M34
    0 0 M43 M44];
    
Atilde=[0 0   1  0
        0 0   0  1
        0 0   -b b
        0 A42 b  -b];
Btilde=[0 0 1 -1]';

A=inv(M)*Atilde;
B=inv(M)*Btilde;

C=diag([1 1 1 1]);
D=0;

% State Space for the open-loop system
ss_open=ss(A,B,C,D)


%-------% Let's use Q=diag[(1 0 0 0]) and R=[1],R=[0.1] & R=[10]%--------%
Q=diag([1 1 1 1]);
R1=[1];
R2=[0.1];
R3=[10];

%K-matrices
K1=lqr(A, B, Q, R1);
K2=lqr(A, B, Q, R2);
K3=lqr(A, B, Q, R3);

% closed loop state space vectors
ss1=ss(A-B*K1,B,C,D)
ss2=ss(A-B*K2,B,C,D)
ss3=ss(A-B*K3,B,C,D)

% PLOTS
%time vector
dt=0.001;
t=0:dt:5;

%Initial condition
X0=[0 5*pi/180 0 0]';

% Plots for R1
%open loop response
figure(11), clf
y_open=initial(ss_open,X0,t); %simulated response with initial condition
open_loop_poles=eig(A)


for n=1:4
    subplot(5,1,n)
    plot(t,y_open(:,n),'b-');
end

u_open = 0*t;  % Free response u
subplot(515)
plot(t,u_open,'r-');
subplot(511)
title('Open loop response')
ylabel('State X1')
subplot(512)
ylabel('State X2')
subplot(513)
ylabel('State X3')
subplot(514)
ylabel('State X4')
subplot(515)
xlabel('Time [s]')
ylabel('Control effort (u)')

%closed loop system 1 (R=1)
figure(12); clf
y1 = initial(ss1,X0,t);

closed_loop_poles_R_1 = eig(A-B*K1)

for n=1:4
    subplot(5,1,n)
    plot(t,y1(:,n),'b-');
    hold on
end
subplot(511)
title('Closed-loop response with R=1')
ylabel('State X1')
subplot(512)
ylabel('State X2')
subplot(513)
ylabel('State X3')
subplot(514)
ylabel('State X4')
subplot(515)
xlabel('Time [s]')
ylabel('Control effort (u)')

% Calculate u:
u1 = -y1*K1'; 

hold on
plot(t,u1,'r-'); % u is the actuation, assuming "u=-K*x" is the law

%closed loop system 2 (R=0.1)
figure(13); clf
y2 = initial(ss2,X0,t);

closed_loop_poles_R_01 = eig(A-B*K2)

for n=1:4
    subplot(5,1,n)
    plot(t,y2(:,n),'b-');
    hold on
end
subplot(511)
title('Closed-loop response with R=0.1')
ylabel('State X1')
subplot(512)
ylabel('State X2')
subplot(513)
ylabel('State X3')
subplot(514)
ylabel('State X4')
subplot(515)
xlabel('Time [s]')
ylabel('Control effort (u)')

% Calculate u:
u2 = -y2*K2'; 

hold on
plot(t,u2,'r-'); % u is the actuation, assuming "u=-K*x" is the law

%closed loop system 3 (R=10)
figure(14); clf
y3 = initial(ss3,X0,t);

closed_loop_poles_R_10 = eig(A-B*K3)

for n=1:4
    subplot(5,1,n)
    plot(t,y3(:,n),'b-');
    hold on
end
subplot(511)
title('Closed-loop response with R=10')
ylabel('State X1')
subplot(512)
ylabel('State X2')
subplot(513)
ylabel('State X3')
subplot(514)
ylabel('State X4')
subplot(515)
xlabel('Time [s]')
ylabel('Control effort (u)')
% Calculate u:
u3 = -y3*K3'; 

hold on
plot(t,u3,'r-'); % u is the actuation, assuming "u=-K*x" is the law

%% Comments

% Changing R has no effect on the states over time. This is because the 
% "cost" of the different states is always the same.
% U becomes greater when R becomes smaller. The smaller the ratio R/Q is 
% the greater u will be. This is because the "cost" of u is greater with a 
% greater R.













