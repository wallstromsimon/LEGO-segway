%param
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

%A=inv(M)*Atilde;
A = M\Atilde;
%B=inv(M)*Btilde;
B = M\Btilde;

C=diag([1 1 1 1]);
D=0;

% State Space for the open-loop system
ss_open=ss(A,B,C,D); %cont

h = 0.1;
H = c2d(ss_open, h); %disc

tf(ss_open);

[u,t] = gensig('square',4,10,h);
lsim(H,u,t)