% Below are parameters to use in Prelab 3.
% Katie Byl, 2012. ECE/ME 179D. UCSB.

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
