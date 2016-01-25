set expFolfer=E:\EngineProjects\EngineSpace\C2DEngine_Java\graphics\AirForce\export
set proFolder=E:\EngineProjects\EngineSpace\C2DEngine_Java
copy %expFolfer%\*.java %proFolder%\src\game\core
copy %expFolfer%\*.bin %proFolder%\assets\res\c2d
copy %expFolfer%\imgs\*.* %proFolder%\assets\res\imgs_example
rem pause