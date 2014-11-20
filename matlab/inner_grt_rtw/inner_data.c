/*
 * inner_data.c
 *
 * Code generation for model "inner".
 *
 * Model version              : 1.68
 * Simulink Coder version : 8.6 (R2014a) 27-Dec-2013
 * C source code generated on : Thu Nov 20 13:08:33 2014
 *
 * Target selection: grt.tlc
 * Note: GRT includes extra infrastructure and instrumentation for prototyping
 * Embedded hardware selection: 32-bit Generic
 * Code generation objectives: Unspecified
 * Validation result: Not run
 */
#include "inner.h"
#include "inner_private.h"

/* Block parameters (auto storage) */
P_inner_T inner_P = {
  -0.0179008201129659,                 /* Mask Parameter: inner_D
                                        * Referenced by: '<S1>/Derivative Gain'
                                        */
  -145.039919974658,                   /* Mask Parameter: inner_I
                                        * Referenced by: '<S1>/Integral Gain'
                                        */
  132.805666822822,                    /* Mask Parameter: inner_N
                                        * Referenced by: '<S1>/Filter Coefficient'
                                        */
  -21.842902173143,                    /* Mask Parameter: inner_P
                                        * Referenced by: '<S1>/Proportional Gain'
                                        */
  1.0,                                 /* Mask Parameter: inner_b
                                        * Referenced by: '<S1>/Setpoint Weighting (Proportional)'
                                        */
  1.0,                                 /* Mask Parameter: inner_c
                                        * Referenced by: '<S1>/Setpoint Weighting (Derivative)'
                                        */
  0.01,                                /* Computed Parameter: Integrator_gainval
                                        * Referenced by: '<S1>/Integrator'
                                        */
  0.0,                                 /* Expression: InitialConditionForIntegrator
                                        * Referenced by: '<S1>/Integrator'
                                        */
  0.01,                                /* Computed Parameter: Filter_gainval
                                        * Referenced by: '<S1>/Filter'
                                        */
  0.0                                  /* Expression: InitialConditionForFilter
                                        * Referenced by: '<S1>/Filter'
                                        */
};
