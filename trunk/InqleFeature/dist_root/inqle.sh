echo starting INQLE...
echo ----------------------------
java -Dorg.osgi.framework.bootdelegation=* -jar org.eclipse.osgi_3.3.2.R33x_v20080105.jar -configuration config-linux -debug -clean -dev -vmargs -server -Xmx256m -XX:+HeapDumpOnOutOfMemoryError
echo -----------------------------
echo INQLE stopped

