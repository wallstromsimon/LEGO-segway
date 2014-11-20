/*
 * inner.c
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

/* Block states (auto storage) */
DW_inner_T inner_DW;

/* External inputs (root inport signals with auto storage) */
ExtU_inner_T inner_U;

/* External outputs (root outports fed by signals with auto storage) */
ExtY_inner_T inner_Y;

/* Real-time model */
RT_MODEL_inner_T inner_M_;
RT_MODEL_inner_T *const inner_M = &inner_M_;

/* Model step function */
void inner_step(void)
{
  real_T rtb_FilterCoefficient;

  /* Gain: '<S1>/Filter Coefficient' incorporates:
   *  DiscreteIntegrator: '<S1>/Filter'
   *  Gain: '<S1>/Derivative Gain'
   *  Gain: '<S1>/Setpoint Weighting (Derivative)'
   *  Inport: '<Root>/r'
   *  Inport: '<Root>/y'
   *  Sum: '<S1>/Sum3'
   *  Sum: '<S1>/SumD'
   */
  rtb_FilterCoefficient = ((inner_P.inner_c * inner_U.r - inner_U.y) *
    inner_P.inner_D - inner_DW.Filter_DSTATE) * inner_P.inner_N;

  /* Outport: '<Root>/u' incorporates:
   *  DiscreteIntegrator: '<S1>/Integrator'
   *  Gain: '<S1>/Proportional Gain'
   *  Gain: '<S1>/Setpoint Weighting (Proportional)'
   *  Inport: '<Root>/r'
   *  Inport: '<Root>/y'
   *  Sum: '<S1>/Sum'
   *  Sum: '<S1>/Sum1'
   */
  inner_Y.u = ((inner_P.inner_b * inner_U.r - inner_U.y) * inner_P.inner_P +
               inner_DW.Integrator_DSTATE) + rtb_FilterCoefficient;

  /* Update for DiscreteIntegrator: '<S1>/Integrator' incorporates:
   *  Gain: '<S1>/Integral Gain'
   *  Inport: '<Root>/r'
   *  Inport: '<Root>/y'
   *  Sum: '<S1>/Sum2'
   */
  inner_DW.Integrator_DSTATE += (inner_U.r - inner_U.y) * inner_P.inner_I *
    inner_P.Integrator_gainval;

  /* Update for DiscreteIntegrator: '<S1>/Filter' */
  inner_DW.Filter_DSTATE += inner_P.Filter_gainval * rtb_FilterCoefficient;

  /* Matfile logging */
  rt_UpdateTXYLogVars(inner_M->rtwLogInfo, (&inner_M->Timing.taskTime0));

  /* signal main to stop simulation */
  {                                    /* Sample time: [0.01s, 0.0s] */
    if ((rtmGetTFinal(inner_M)!=-1) &&
        !((rtmGetTFinal(inner_M)-inner_M->Timing.taskTime0) >
          inner_M->Timing.taskTime0 * (DBL_EPSILON))) {
      rtmSetErrorStatus(inner_M, "Simulation finished");
    }
  }

  /* Update absolute time for base rate */
  /* The "clockTick0" counts the number of times the code of this task has
   * been executed. The absolute time is the multiplication of "clockTick0"
   * and "Timing.stepSize0". Size of "clockTick0" ensures timer will not
   * overflow during the application lifespan selected.
   * Timer of this task consists of two 32 bit unsigned integers.
   * The two integers represent the low bits Timing.clockTick0 and the high bits
   * Timing.clockTickH0. When the low bit overflows to 0, the high bits increment.
   */
  if (!(++inner_M->Timing.clockTick0)) {
    ++inner_M->Timing.clockTickH0;
  }

  inner_M->Timing.taskTime0 = inner_M->Timing.clockTick0 *
    inner_M->Timing.stepSize0 + inner_M->Timing.clockTickH0 *
    inner_M->Timing.stepSize0 * 4294967296.0;
}

/* Model initialize function */
void inner_initialize(void)
{
  /* Registration code */

  /* initialize non-finites */
  rt_InitInfAndNaN(sizeof(real_T));

  /* initialize real-time model */
  (void) memset((void *)inner_M, 0,
                sizeof(RT_MODEL_inner_T));
  rtmSetTFinal(inner_M, 50.0);
  inner_M->Timing.stepSize0 = 0.01;

  /* Setup for data logging */
  {
    static RTWLogInfo rt_DataLoggingInfo;
    inner_M->rtwLogInfo = &rt_DataLoggingInfo;
  }

  /* Setup for data logging */
  {
    rtliSetLogXSignalInfo(inner_M->rtwLogInfo, (NULL));
    rtliSetLogXSignalPtrs(inner_M->rtwLogInfo, (NULL));
    rtliSetLogT(inner_M->rtwLogInfo, "tout");
    rtliSetLogX(inner_M->rtwLogInfo, "");
    rtliSetLogXFinal(inner_M->rtwLogInfo, "");
    rtliSetLogVarNameModifier(inner_M->rtwLogInfo, "rt_");
    rtliSetLogFormat(inner_M->rtwLogInfo, 0);
    rtliSetLogMaxRows(inner_M->rtwLogInfo, 1000);
    rtliSetLogDecimation(inner_M->rtwLogInfo, 1);

    /*
     * Set pointers to the data and signal info for each output
     */
    {
      static void * rt_LoggedOutputSignalPtrs[] = {
        &inner_Y.u
      };

      rtliSetLogYSignalPtrs(inner_M->rtwLogInfo, ((LogSignalPtrsType)
        rt_LoggedOutputSignalPtrs));
    }

    {
      static int_T rt_LoggedOutputWidths[] = {
        1
      };

      static int_T rt_LoggedOutputNumDimensions[] = {
        1
      };

      static int_T rt_LoggedOutputDimensions[] = {
        1
      };

      static boolean_T rt_LoggedOutputIsVarDims[] = {
        0
      };

      static void* rt_LoggedCurrentSignalDimensions[] = {
        (NULL)
      };

      static int_T rt_LoggedCurrentSignalDimensionsSize[] = {
        4
      };

      static BuiltInDTypeId rt_LoggedOutputDataTypeIds[] = {
        SS_DOUBLE
      };

      static int_T rt_LoggedOutputComplexSignals[] = {
        0
      };

      static const char_T *rt_LoggedOutputLabels[] = {
        "" };

      static const char_T *rt_LoggedOutputBlockNames[] = {
        "inner/u" };

      static RTWLogDataTypeConvert rt_RTWLogDataTypeConvert[] = {
        { 0, SS_DOUBLE, SS_DOUBLE, 0, 0, 0, 1.0, 0, 0.0 }
      };

      static RTWLogSignalInfo rt_LoggedOutputSignalInfo[] = {
        {
          1,
          rt_LoggedOutputWidths,
          rt_LoggedOutputNumDimensions,
          rt_LoggedOutputDimensions,
          rt_LoggedOutputIsVarDims,
          rt_LoggedCurrentSignalDimensions,
          rt_LoggedCurrentSignalDimensionsSize,
          rt_LoggedOutputDataTypeIds,
          rt_LoggedOutputComplexSignals,
          (NULL),

          { rt_LoggedOutputLabels },
          (NULL),
          (NULL),
          (NULL),

          { rt_LoggedOutputBlockNames },

          { (NULL) },
          (NULL),
          rt_RTWLogDataTypeConvert
        }
      };

      rtliSetLogYSignalInfo(inner_M->rtwLogInfo, rt_LoggedOutputSignalInfo);

      /* set currSigDims field */
      rt_LoggedCurrentSignalDimensions[0] = &rt_LoggedOutputWidths[0];
    }

    rtliSetLogY(inner_M->rtwLogInfo, "yout");
  }

  /* states (dwork) */
  (void) memset((void *)&inner_DW, 0,
                sizeof(DW_inner_T));

  /* external inputs */
  (void) memset((void *)&inner_U, 0,
                sizeof(ExtU_inner_T));

  /* external outputs */
  inner_Y.u = 0.0;

  /* Matfile logging */
  rt_StartDataLoggingWithStartTime(inner_M->rtwLogInfo, 0.0, rtmGetTFinal
    (inner_M), inner_M->Timing.stepSize0, (&rtmGetErrorStatus(inner_M)));

  /* InitializeConditions for DiscreteIntegrator: '<S1>/Integrator' */
  inner_DW.Integrator_DSTATE = inner_P.Integrator_IC;

  /* InitializeConditions for DiscreteIntegrator: '<S1>/Filter' */
  inner_DW.Filter_DSTATE = inner_P.Filter_IC;
}

/* Model terminate function */
void inner_terminate(void)
{
  /* (no terminate code required) */
}
