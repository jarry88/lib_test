@rem ##########################################################################
@rem
@rem  ��Ҫ����ʽ�����_��(Windows��)
@rem               zwm
@rem               2020-07-08
@rem
@rem ##########################################################################


@echo �_ʼ���...

set build_time=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%

@rem �_��ݔ��Ŀ䛵����Q���K�½�֮
set output_root_dir=..\..\..\takewant_build\%build_time%
md %output_root_dir%


@rem ��cleanһ��
for /F %%i in ('gradlew clean') do @set foo=1; 

@echo ��Google Play����aab��
for /F %%i in ('gradlew bundleGoogleRelease') do @set foo=1; 
copy app\build\outputs\bundle\googleRelease\app-google-release.aab %output_root_dir%

@echo ��Google Play����apk��
for /F %%i in ('gradlew assembleGoogleRelease') do @set foo=1;
copy app\build\outputs\apk\google\release\twant_*.apk  %output_root_dir%

@echo ��پW����apk��
for /F %%i in ('gradlew assembleOfficialRelease') do @set foo=1;
copy app\build\outputs\apk\official\release\twant_*.apk  %output_root_dir%


@echo ���Ì�����apk��
for /F %%i in ('gradlew assembleTencentRelease') do @set foo=1;
copy app\build\outputs\apk\tencent\release\twant_*.apk  %output_root_dir%


@echo ���A������apk��
for /F %%i in ('gradlew assembleHuaweiRelease') do @set foo=1;
copy app\build\outputs\apk\huawei\release\twant_*.apk  %output_root_dir%

@echo ��С������apk��
for /F %%i in ('gradlew assembleXiaomiRelease') do @set foo=1;
copy app\build\outputs\apk\xiaomi\release\twant_*.apk  %output_root_dir%


@echo �����ɣ�ݔ��Ŀ䛣�[%output_root_dir%]
