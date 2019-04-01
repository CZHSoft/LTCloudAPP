#include <jni.h>

#include <string.h>
#include <unistd.h>

#include <android/log.h>

#define TAG "myDemo-jni" // ������Զ����LOG�ı�ʶ
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // ����LOGD����
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // ����LOGI����
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // ����LOGW����
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // ����LOGE����
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // ����LOGF����

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

