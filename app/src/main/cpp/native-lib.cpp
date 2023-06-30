#include <jni.h>
#include <string>

//Java_com_app_keyboard_utility_KBConstant_getMain(JNIEnv *env, jobject thiz) {

extern "C" JNIEXPORT jstring JNICALL
Java_com_voicetranslator_translate_languagetranslator_utils_Constant_getMain(JNIEnv *env, jobject thiz) {
    std::string hello = "https://cbdevgroup.com/pd/Language446/V1/";
    return env->NewStringUTF(hello.c_str());
}

//Java_com_app_keyboard_utility_KBConstant_getKey1(JNIEnv *env, jobject thiz) {
extern "C" JNIEXPORT jstring JNICALL
Java_com_voicetranslator_translate_languagetranslator_utils_Constant_getKey1(JNIEnv *env, jobject thiz) {
    std::string hello = "RDjyLXlvF0TkTWlZ";
    return env->NewStringUTF(hello.c_str());
}

//Java_com_app_keyboard_utility_KBConstant_getKey2(JNIEnv *env, jobject thiz) {
extern "C" JNIEXPORT jstring JNICALL
    Java_com_voicetranslator_translate_languagetranslator_utils_Constant_getKey2(JNIEnv *env, jobject thiz) {
    std::string hello = "wRFw25irLqteP2a6";
    return env->NewStringUTF(hello.c_str());
}
