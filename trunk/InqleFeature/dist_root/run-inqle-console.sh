echo starting INQLE...
echo ----------------------------
java -Dorg.osgi.framework.bootdelegation=* -server -Xmx512m -jar org.eclipse.osgi_3.3.2.R33x_v20080105.jar -configuration config-linux -consoleLog -debug -clean -console -dev
echo -----------------------------
echo INQLE stopped
#org.eclipse.equinox.launcher_1.0.1.R33x_v20070828.jar
#org.eclipse.osgi_3.3.2.R33x_v20080105.jar
