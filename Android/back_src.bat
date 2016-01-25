SET sourcePath=src
SET WINRARPATH="D:\SoftWare\ComoontTools\winrar\WinRAR.exe"
IF NOT EXIST %WINRARPATH% (
SET WINRARPATH="C:\Program Files\WinRAR\WinRAR.exe"
)
IF NOT EXIST %WINRARPATH% (
SET WINRARPATH="E:\CommonTools\WinRAR\WinRAR.exe"
)
SET destPath=BAK_%sourcePath%

IF NOT EXIST %destPath% (
MKDIR %destPath%
)
rem DEL %destPath%*.rar
cd %destPath%
%WINRARPATH% a -m5 -k -t -agYYYY-MM-DD-HH-MM %sourcePath% ..\src_c2d ..\src_c3d ..\src_c3d ..\pay ..\adv