rem @ECHO OFF
rem echo Copying bootstrap
rem rmdir ".\public\bootstrap\css" /s /q
rem rmdir ".\public\bootstrap\js" /s /q
rem xcopy ".\src\bootstrap-4.0.0\dist\css\*" ".\public\bootstrap\css\*"
rem xcopy ".\src\bootstrap-4.0.0\dist\js\*" ".\public\bootstrap\js\*"