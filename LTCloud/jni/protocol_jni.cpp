#include <jni.h>

#include <string.h>
#include <unistd.h>

#include <android/log.h>

#define TAG "myDemo-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

static JavaVM *gJavaVM;

extern "C"
JNIEXPORT jbyteArray JNICALL Java_com_ltnw_common_ProtocolJniClient_HeartBeatCall
  (JNIEnv *env, jobject obj, jstring imei)
{

	jbyteArray jarray = env->NewByteArray(30);

	jclass strClass;
	jmethodID mid;

	strClass = (*env)->FindClass("java/lang/String");
	if (stringClass == NULL) {
	      return NULL; /* exception thrown */
	}

	mid = (*env)->GetMethodID(
			env,
			strClass,
			"getBytes",
			"(Ljava/lang/String;)[B");
	if (mid == NULL) {
		return NULL; /* exception thrown */
	}

	jbyteArray headArray= (jbyteArray)(*env)->CallObjectMethod(env,obj, mid, (*env)->NewStringUTF(env, "LTNW"));
	jbyte *headBytes = (*env)->GetByteArrayElements(headArray, 0);

	(*env)->SetByteArrayRegion(jarray, 0, 4, headBytes);



	return jarray;
}

