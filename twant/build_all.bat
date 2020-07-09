@rem ##########################################################################
@rem
@rem  想要城正式版打包腳本(Windows版)
@rem               zwm
@rem               2020-07-08
@rem
@rem ##########################################################################


@echo 開始打包...

set build_time=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%

@rem 確定輸出目錄的名稱，並新建之
set output_root_dir=..\..\..\takewant_build\%build_time%
md %output_root_dir%


@rem 先clean一下
for /F %%i in ('gradlew clean') do @set foo=1; 

@echo 打Google Play渠道aab包
for /F %%i in ('gradlew bundleGoogleRelease') do @set foo=1; 
copy app\build\outputs\bundle\googleRelease\app-google-release.aab %output_root_dir%

@echo 打Google Play渠道apk包
for /F %%i in ('gradlew assembleGoogleRelease') do @set foo=1;
copy app\build\outputs\apk\google\release\twant_*.apk  %output_root_dir%

@echo 打官網渠道apk包
for /F %%i in ('gradlew assembleOfficialRelease') do @set foo=1;
copy app\build\outputs\apk\official\release\twant_*.apk  %output_root_dir%


@echo 打應用寶渠道apk包
for /F %%i in ('gradlew assembleTencentRelease') do @set foo=1;
copy app\build\outputs\apk\tencent\release\twant_*.apk  %output_root_dir%


@echo 打華為渠道apk包
for /F %%i in ('gradlew assembleHuaweiRelease') do @set foo=1;
copy app\build\outputs\apk\huawei\release\twant_*.apk  %output_root_dir%

@echo 打小米渠道apk包
for /F %%i in ('gradlew assembleXiaomiRelease') do @set foo=1;
copy app\build\outputs\apk\xiaomi\release\twant_*.apk  %output_root_dir%


@echo 打包完成，輸出目錄：[%output_root_dir%]
