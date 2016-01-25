#include "Java_c2d_util_math_Matrix4.h"
#include <memory.h>
#include <stdio.h>
#include <string.h>

#define M00 0
#define M01 4
#define M02 8
#define M03 12
#define M10 1
#define M11 5
#define M12 9
#define M13 13
#define M20 2
#define M21 6
#define M22 10
#define M23 14
#define M30 3
#define M31 7
#define M32 11
#define M33 15
static inline void matrix4_mapvector(float* matrix, float* va, float* vb) {
	float tmp[4];
	tmp[0] = matrix[M00] * va[0] + matrix[M01] * va[1] + matrix[M02] * va[2] + matrix[M03] * va[3];
	tmp[1] = matrix[M10] * va[0] + matrix[M11] * va[1] + matrix[M12] * va[2] + matrix[M13] * va[3];
	tmp[2] = matrix[M20] * va[0] + matrix[M21] * va[1] + matrix[M22] * va[2] + matrix[M23] * va[3];
	tmp[3] = matrix[M30] * va[0] + matrix[M31] * va[1] + matrix[M32] * va[2] + matrix[M33] * va[3];
	memcpy(vb, tmp, sizeof(float) *  4);
}
static inline void matrix4_mul(float* mata, float* matb, float* matc) {
	float tmp[16];
	tmp[M00] = mata[M00] * matb[M00] + mata[M01] * matb[M10] + mata[M02] * matb[M20] + mata[M03] * matb[M30];
	tmp[M01] = mata[M00] * matb[M01] + mata[M01] * matb[M11] + mata[M02] * matb[M21] + mata[M03] * matb[M31];
	tmp[M02] = mata[M00] * matb[M02] + mata[M01] * matb[M12] + mata[M02] * matb[M22] + mata[M03] * matb[M32];
	tmp[M03] = mata[M00] * matb[M03] + mata[M01] * matb[M13] + mata[M02] * matb[M23] + mata[M03] * matb[M33];
	tmp[M10] = mata[M10] * matb[M00] + mata[M11] * matb[M10] + mata[M12] * matb[M20] + mata[M13] * matb[M30];
	tmp[M11] = mata[M10] * matb[M01] + mata[M11] * matb[M11] + mata[M12] * matb[M21] + mata[M13] * matb[M31];
	tmp[M12] = mata[M10] * matb[M02] + mata[M11] * matb[M12] + mata[M12] * matb[M22] + mata[M13] * matb[M32];
	tmp[M13] = mata[M10] * matb[M03] + mata[M11] * matb[M13] + mata[M12] * matb[M23] + mata[M13] * matb[M33];
	tmp[M20] = mata[M20] * matb[M00] + mata[M21] * matb[M10] + mata[M22] * matb[M20] + mata[M23] * matb[M30];
	tmp[M21] = mata[M20] * matb[M01] + mata[M21] * matb[M11] + mata[M22] * matb[M21] + mata[M23] * matb[M31];
	tmp[M22] = mata[M20] * matb[M02] + mata[M21] * matb[M12] + mata[M22] * matb[M22] + mata[M23] * matb[M32];
	tmp[M23] = mata[M20] * matb[M03] + mata[M21] * matb[M13] + mata[M22] * matb[M23] + mata[M23] * matb[M33];
	tmp[M30] = mata[M30] * matb[M00] + mata[M31] * matb[M10] + mata[M32] * matb[M20] + mata[M33] * matb[M30];
	tmp[M31] = mata[M30] * matb[M01] + mata[M31] * matb[M11] + mata[M32] * matb[M21] + mata[M33] * matb[M31];
	tmp[M32] = mata[M30] * matb[M02] + mata[M31] * matb[M12] + mata[M32] * matb[M22] + mata[M33] * matb[M32];
	tmp[M33] = mata[M30] * matb[M03] + mata[M31] * matb[M13] + mata[M32] * matb[M23] + mata[M33] * matb[M33];
	memcpy(matc, tmp, sizeof(float) *  16);
}
static inline float matrix4_det(float* val) {
	return val[M30] * val[M21] * val[M12] * val[M03] - val[M20] * val[M31] * val[M12] * val[M03] - val[M30] * val[M11]
			* val[M22] * val[M03] + val[M10] * val[M31] * val[M22] * val[M03] + val[M20] * val[M11] * val[M32] * val[M03] - val[M10]
			* val[M21] * val[M32] * val[M03] - val[M30] * val[M21] * val[M02] * val[M13] + val[M20] * val[M31] * val[M02] * val[M13]
			+ val[M30] * val[M01] * val[M22] * val[M13] - val[M00] * val[M31] * val[M22] * val[M13] - val[M20] * val[M01] * val[M32]
			* val[M13] + val[M00] * val[M21] * val[M32] * val[M13] + val[M30] * val[M11] * val[M02] * val[M23] - val[M10] * val[M31]
			* val[M02] * val[M23] - val[M30] * val[M01] * val[M12] * val[M23] + val[M00] * val[M31] * val[M12] * val[M23] + val[M10]
			* val[M01] * val[M32] * val[M23] - val[M00] * val[M11] * val[M32] * val[M23] - val[M20] * val[M11] * val[M02] * val[M33]
			+ val[M10] * val[M21] * val[M02] * val[M33] + val[M20] * val[M01] * val[M12] * val[M33] - val[M00] * val[M21] * val[M12]
			* val[M33] - val[M10] * val[M01] * val[M22] * val[M33] + val[M00] * val[M11] * val[M22] * val[M33];
}

static inline bool matrix4_inv(float* val,float* res) {
	float tmp[16];
	float l_det = matrix4_det(val);
	if (l_det == 0) return false;
	tmp[M00] = val[M12] * val[M23] * val[M31] - val[M13] * val[M22] * val[M31] + val[M13] * val[M21] * val[M32] - val[M11]
		* val[M23] * val[M32] - val[M12] * val[M21] * val[M33] + val[M11] * val[M22] * val[M33];
	tmp[M01] = val[M03] * val[M22] * val[M31] - val[M02] * val[M23] * val[M31] - val[M03] * val[M21] * val[M32] + val[M01]
		* val[M23] * val[M32] + val[M02] * val[M21] * val[M33] - val[M01] * val[M22] * val[M33];
	tmp[M02] = val[M02] * val[M13] * val[M31] - val[M03] * val[M12] * val[M31] + val[M03] * val[M11] * val[M32] - val[M01]
		* val[M13] * val[M32] - val[M02] * val[M11] * val[M33] + val[M01] * val[M12] * val[M33];
	tmp[M03] = val[M03] * val[M12] * val[M21] - val[M02] * val[M13] * val[M21] - val[M03] * val[M11] * val[M22] + val[M01]
		* val[M13] * val[M22] + val[M02] * val[M11] * val[M23] - val[M01] * val[M12] * val[M23];
	tmp[M10] = val[M13] * val[M22] * val[M30] - val[M12] * val[M23] * val[M30] - val[M13] * val[M20] * val[M32] + val[M10]
		* val[M23] * val[M32] + val[M12] * val[M20] * val[M33] - val[M10] * val[M22] * val[M33];
	tmp[M11] = val[M02] * val[M23] * val[M30] - val[M03] * val[M22] * val[M30] + val[M03] * val[M20] * val[M32] - val[M00]
		* val[M23] * val[M32] - val[M02] * val[M20] * val[M33] + val[M00] * val[M22] * val[M33];
	tmp[M12] = val[M03] * val[M12] * val[M30] - val[M02] * val[M13] * val[M30] - val[M03] * val[M10] * val[M32] + val[M00]
		* val[M13] * val[M32] + val[M02] * val[M10] * val[M33] - val[M00] * val[M12] * val[M33];
	tmp[M13] = val[M02] * val[M13] * val[M20] - val[M03] * val[M12] * val[M20] + val[M03] * val[M10] * val[M22] - val[M00]
		* val[M13] * val[M22] - val[M02] * val[M10] * val[M23] + val[M00] * val[M12] * val[M23];
	tmp[M20] = val[M11] * val[M23] * val[M30] - val[M13] * val[M21] * val[M30] + val[M13] * val[M20] * val[M31] - val[M10]
		* val[M23] * val[M31] - val[M11] * val[M20] * val[M33] + val[M10] * val[M21] * val[M33];
	tmp[M21] = val[M03] * val[M21] * val[M30] - val[M01] * val[M23] * val[M30] - val[M03] * val[M20] * val[M31] + val[M00]
		* val[M23] * val[M31] + val[M01] * val[M20] * val[M33] - val[M00] * val[M21] * val[M33];
	tmp[M22] = val[M01] * val[M13] * val[M30] - val[M03] * val[M11] * val[M30] + val[M03] * val[M10] * val[M31] - val[M00]
		* val[M13] * val[M31] - val[M01] * val[M10] * val[M33] + val[M00] * val[M11] * val[M33];
	tmp[M23] = val[M03] * val[M11] * val[M20] - val[M01] * val[M13] * val[M20] - val[M03] * val[M10] * val[M21] + val[M00]
		* val[M13] * val[M21] + val[M01] * val[M10] * val[M23] - val[M00] * val[M11] * val[M23];
	tmp[M30] = val[M12] * val[M21] * val[M30] - val[M11] * val[M22] * val[M30] - val[M12] * val[M20] * val[M31] + val[M10]
		* val[M22] * val[M31] + val[M11] * val[M20] * val[M32] - val[M10] * val[M21] * val[M32];
	tmp[M31] = val[M01] * val[M22] * val[M30] - val[M02] * val[M21] * val[M30] + val[M02] * val[M20] * val[M31] - val[M00]
		* val[M22] * val[M31] - val[M01] * val[M20] * val[M32] + val[M00] * val[M21] * val[M32];
	tmp[M32] = val[M02] * val[M11] * val[M30] - val[M01] * val[M12] * val[M30] - val[M02] * val[M10] * val[M31] + val[M00]
		* val[M12] * val[M31] + val[M01] * val[M10] * val[M32] - val[M00] * val[M11] * val[M32];
	tmp[M33] = val[M01] * val[M12] * val[M20] - val[M02] * val[M11] * val[M20] + val[M02] * val[M10] * val[M21] - val[M00]
		* val[M12] * val[M21] - val[M01] * val[M10] * val[M22] + val[M00] * val[M11] * val[M22];

	float inv_det = 1.0f / l_det;
	res[M00] = tmp[M00] * inv_det;
	res[M01] = tmp[M01] * inv_det;
	res[M02] = tmp[M02] * inv_det;
	res[M03] = tmp[M03] * inv_det;
	res[M10] = tmp[M10] * inv_det;
	res[M11] = tmp[M11] * inv_det;
	res[M12] = tmp[M12] * inv_det;
	res[M13] = tmp[M13] * inv_det;
	res[M20] = tmp[M20] * inv_det;
	res[M21] = tmp[M21] * inv_det;
	res[M22] = tmp[M22] * inv_det;
	res[M23] = tmp[M23] * inv_det;
	res[M30] = tmp[M30] * inv_det;
	res[M31] = tmp[M31] * inv_det;
	res[M32] = tmp[M32] * inv_det;
	res[M33] = tmp[M33] * inv_det;
	return true;
}
JNIEXPORT void JNICALL Java_c2d_util_math_Matrix4_mapVector
(JNIEnv *env, jclass, jfloatArray matrix, jfloatArray va, jfloatArray vb){
	float* matf = (float*)env->GetPrimitiveArrayCritical(matrix, 0);
	float* vaf = (float*)env->GetPrimitiveArrayCritical(va, 0);
	float* vbf = (float*)env->GetPrimitiveArrayCritical(vb, 0);
	matrix4_mapvector(matf,vaf,vbf);
	env->ReleasePrimitiveArrayCritical(matrix, matf, 0);
	env->ReleasePrimitiveArrayCritical(va, vaf, 0);
	env->ReleasePrimitiveArrayCritical(vb, vbf, 0);
}
JNIEXPORT void JNICALL Java_c2d_util_math_Matrix4_multiply
(JNIEnv *env, jclass, jfloatArray matrixA, jfloatArray matrixB, jfloatArray matrixC){
	float* mata = (float*)env->GetPrimitiveArrayCritical(matrixA, 0);
	float* matb = (float*)env->GetPrimitiveArrayCritical(matrixB, 0);
	float* matc = (float*)env->GetPrimitiveArrayCritical(matrixC, 0);
	matrix4_mul(mata, matb,matc);
	env->ReleasePrimitiveArrayCritical(matrixA, mata, 0);
	env->ReleasePrimitiveArrayCritical(matrixB, matb, 0);
	env->ReleasePrimitiveArrayCritical(matrixC, matc, 0);
}

JNIEXPORT jboolean JNICALL Java_c2d_util_math_Matrix4_inverse
(JNIEnv *env, jclass, jfloatArray matrixA, jfloatArray matrixB) {
	float* matA = (float*)env->GetPrimitiveArrayCritical(matrixA, 0);	
	float* matB = (float*)env->GetPrimitiveArrayCritical(matrixB, 0);	
	bool result = matrix4_inv(matA,matB);
	env->ReleasePrimitiveArrayCritical(matrixA, matA, 0);
	env->ReleasePrimitiveArrayCritical(matrixB, matB, 0);	
	return result;
}

JNIEXPORT jfloat JNICALL Java_c2d_util_math_Matrix4_det
  (JNIEnv *env, jclass, jfloatArray matrix) {
	float* mat = (float*)env->GetPrimitiveArrayCritical(matrix, 0);	
	float result = matrix4_det(mat);
	env->ReleasePrimitiveArrayCritical(matrix, mat, 0);	
	return result;
}
