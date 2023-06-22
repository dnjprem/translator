#include <jni.h>
#include <string>
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_keyboard_utility_KBConstant_getMain(JNIEnv *env, jobject thiz) {
    std::string hello = "https://cbdevgroup.com/kk/CreditCard442/V1/";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_keyboard_utility_KBConstant_getKey1(JNIEnv *env, jobject thiz) {
    std::string hello = "fx7DgOab1N5UF4ak";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_app_keyboard_utility_KBConstant_getKey2(JNIEnv *env, jobject thiz) {
    std::string hello = "iLBHT5KzwjceEzsf";
    return env->NewStringUTF(hello.c_str());
}