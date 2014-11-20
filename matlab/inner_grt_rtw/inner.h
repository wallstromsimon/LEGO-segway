/*
 * inner.h
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
#ifndef RTW_HEADER_inner_h_
#define RTW_HEADER_inner_h_
#include <float.h>
#include <string.h>
#include <stddef.h>
#ifndef inner_COMMON_INCLUDES_
# define inner_COMMON_INCLUDES_
#include "rtwtypes.h"
#include "rtw_continuous.h"
#include "rtw_solver.h"
#include "rt_logging.h"
#endif                                 /* inner_COMMON_INCLUDES_ */

#include "inner_types.h"

/* Shared type includes */
#include "multiword_types.h"
#include "rt_nonfinite.h"

/* Macros for accessing real-time model data structure */
#ifndef rtmGetFinalTime
# define rtmGetFinalTime(rtm)          ((rtm)->Timing.tFinal)
#endif

#ifndef rtmGetRTWLogInfo
# define rtmGetRTWLogInfo(rtm)         ((rtm)->rtwLogInfo)
#endif

#ifndef rtmGetErrorStatus
# define rtmGetErrorStatus(rtm)        ((rtm)->errorStatus)
#endif

#ifndef rtmSetErrorStatus
# define rtmSetErrorStatus(rtm, val)   ((rtm)->errorStatus = (val))
#endif

#ifndef rtmGetStopRequested
# define rtmGetStopRequested(rtm)      ((rtm)->Timing.stopRequestedFlag)
#endif

#ifndef rtmSetStopRequested
# define rtmSetStopRequested(rtm, val) ((rtm)->Timing.stopRequestedFlag = (val))
#endif

#ifndef rtmGetStopRequestedPtr
# define rtmGetStopRequestedPtr(rtm)   (&((rtm)->Timing.stopRequestedFlag))
#endif

#ifndef rtmGetT
# define rtmGetT(rtm)                  ((rtm)->Timing.taskTime0)
#endif

#ifndef rtmGetTFinal
# define rtmGetTFinal(rtm)             ((rtm)->Timing.tFinal)
#endif

/* Block states (auto storage) for system '<Root>' */
typedef struct {
  real_T Integrator_DSTATE;            /* '<S1>/Integrator' */
  real_T Filter_DSTATE;                /* '<S1>/Filter' */
} DW_inner_T;

/* External inputs (root inport signals with auto storage) */
typedef struct {
  real_T r;                            /* '<Root>/r' */
  real_T y;                            /* '<Root>/y' */
} ExtU_inner_T;

/* External outputs (root outports fed by signals with auto storage) */
typedef struct {
  real_T u;                            /* '<Root>/u' */
} ExtY_inner_T;

/* Parameters (auto storage) */
struct P_inner_T_ {
  real_T inner_D;                      /* Mask Parameter: inner_D
                                        * Referenced by: '<S1>/Derivative Gain'
                                        */
  real_T inner_I;                      /* Mask Parameter: inner_I
                                        * Referenced by: '<S1>/Integral Gain'
                                        */
  real_T inner_N;                      /* Mask Parameter: inner_N
                                        * Referenced by: '<S1>/Filter Coefficient'
                                        */
  real_T inner_P;                      /* Mask Parameter: inner_P
                                        * Referenced by: '<S1>/Proportional Gain'
                                        */
  real_T inner_b;                      /* Mask Parameter: inner_b
                                        * Referenced by: '<S1>/Setpoint Weighting (Proportional)'
                                        */
  real_T inner_c;                      /* Mask Parameter: inner_c
                                        * Referenced by: '<S1>/Setpoint Weighting (Derivative)'
                                        */
  real_T Integrator_gainval;           /* Computed Parameter: Integrator_gainval
                                        * Referenced by: '<S1>/Integrator'
                                        */
  real_T Integrator_IC;                /* Expression: InitialConditionForIntegrator
                                        * Referenced by: '<S1>/Integrator'
                                        */
  real_T Filter_gainval;               /* Computed Parameter: Filter_gainval
                                        * Referenced by: '<S1>/Filter'
                                        */
  real_T Filter_IC;                    /* Expression: InitialConditionForFilter
                                        * Referenced by: '<S1>/Filter'
                                        */
};

/* Real-time Model Data Structure */
struct tag_RTM_inner_T {
  const char_T *errorStatus;
  RTWLogInfo *rtwLogInfo;

  /*
   * Timing:
   * The following substructure contains information regarding
   * the timing information for the model.
   */
  struct {
    time_T taskTime0;
    uint32_T clockTick0;
    uint32_T clockTickH0;
    time_T stepSize0;
    time_T tFinal;
    boolean_T stopRequestedFlag;
  } Timing;
};

/* Block parameters (auto storage) */
extern P_inner_T inner_P;

/* Block states (auto storage) */
extern DW_inner_T inner_DW;

/* External inputs (root inport signals with auto storage) */
extern ExtU_inner_T inner_U;

/* External outputs (root outports fed by signals with auto storage) */
extern ExtY_inner_T inner_Y;

/* Model entry point functions */
extern void inner_initialize(void);
extern void inner_step(void);
extern void inner_terminate(void);

/* Real-time Model object */
extern RT_MODEL_inner_T *const inner_M;

/*-
 * The generated code includes comments that allow you to trace directly
 * back to the appropriate location in the model.  The basic format
 * is <system>/block_name, where system is the system number (uniquely
 * assigned by Simulink) and block_name is the name of the block.
 *
 * Note that this particular code originates from a subsystem build,
 * and has its own system numbers different from the parent model.
 * Refer to the system hierarchy for this subsystem below, and use the
 * MATLAB hilite_system command to trace the generated code back
 * to the parent model.  For example,
 *
 * hilite_system('simdisc/inner')    - opens subsystem simdisc/inner
 * hilite_system('simdisc/inner/Kp') - opens and selects block Kp
 *
 * Here is the system hierarchy for this model
 *
 * '<Root>' : 'simdisc'
 * '<S1>'   : 'simdisc/inner'
 */
#endif                                 /* RTW_HEADER_inner_h_ */
