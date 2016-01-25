set makespace="makespace"
if EXIST %makespace% ( rmdir /s /q %makespace% )
mkdir %makespace%
cd %makespace%

set packorg="packorg"
if EXIST %packorg% ( rmdir /s /q %packorg% )
mkdir %packorg%
mkdir %packorg%\c2d
xcopy /E ..\bin\classes\c2d %packorg%\c2d

jar cvf AFEngine.jar -C %packorg%\ .


copy ..\makelib.pro makelib.pro

java -jar E:\Tech\iptv\Tool\PackJarTool\tools\proguard4.5.1\lib\proguard.jar @makelib.pro 

copy AFEngineLib.jar ..\C2DEngine_android.jar
cd ..\
pause
rmdir /s /q %makespace%
pause