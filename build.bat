@echo off
IF exist build.log (
	del /F build.log
)

rmdir jars /S /Q
mkdir jars

echo Starting OLMS services build process...

cd fwk-annotations
echo 1) Building fwk annotations...
call mvn clean install >> ../build.log
echo Build completed.

cd..
cd olms-registry-server
echo 4) Building olms-registry-services...
call mvn clean install >> ../build.log
copy target\*.jar ..\jars\ >> ../build.log
echo Build completed.

cd..
cd olms-gateway-server
echo 4) Building olms-gateway-services...
call mvn clean install >> ../build.log
copy target\*.jar ..\jars\ >> ../build.log
echo Build completed.

cd..
cd olms-data-access
echo 2) Building master-data-access module...
call mvn clean install >> ../build.log
echo Build completed.

cd..
cd olms-mdms-data-access
echo 3) Building tdms-data-access module...
call mvn clean install >> ../build.log
echo Build completed.

cd..
cd olms-master-services
echo 4) Building olms-master-services...
call mvn clean install -DskipTests >> ../build.log
copy target\*.jar ..\jars\ >> ../build.log
echo Build completed.

cd..
cd olms-tran-services
echo 5) Building olms-tran-services...
call mvn clean install -DskipTests >> ../build.log
copy target\*.jar ..\jars\ >> ../build.log
echo Build completed.

cd..
cd olms-rpt-services
echo 6) Building olms-rpt-services...
call mvn clean install -DskipTests >> ../build.log
copy target\*.jar ..\jars\ >> ../build.log
echo Build completed.

cd..
echo OLMS services build process completed.