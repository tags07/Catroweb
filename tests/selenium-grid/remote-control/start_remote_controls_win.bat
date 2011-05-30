@echo off
cls

if "%ANT_HOME%"=="" (
  echo ***
  echo get and intall ant:
  echo https://code.google.com/p/winant/downloads/list
  echo ***
  pause
  exit /B 
)
  
ipconfig | find "IPv4" > %TEMP%.\TEMP.DAT
for /F "tokens=2 delims=:" %%a in (%TEMP%.\TEMP.DAT) do set IP=%%a
del %TEMP%.\TEMP.DAT
set host=%IP:~1%

set profile=
set /P profile=Please enter an environment to test [*firefox]: %=%
if "%profile%"=="" set profile=*firefox
  
set number=
set /P number=How many remote controls to you want to start: %=%
if "%number%"=="" set number=1

set /a i=0
:loop
if %i%==%number% goto END
  set /a port=%i%+5555
  set /a open=0
  netstat -an | findstr ":%port%" && set /a open=1
  if %open%==0 start /B ant -Dport=%port% -Dhost=%host% -DhubURL=http://catroidwebtest.ist.tugraz.at:4444 launch-remote-control 
  if %open%==1 set /a number=%number%+1
  set /a i=%i%+1
goto LOOP
:end
