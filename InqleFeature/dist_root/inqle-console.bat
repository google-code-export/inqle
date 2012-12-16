eclipse -console -consolelog -debug -dev -vmargs -Xmx256m -server
REM -Dhttp.proxyHost=myproxy.mydomain.com -Dhttp.proxyPort=80
REM -Dorg.eclipse.rwt.clientLibraryVariant=DEBUG -Dorg.eclipse.rwt.clientLogLevel=ALL

REM run in dev mode: eclipse -console -consolelog -debug -dev -vmargs -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -server -Xshare:off
