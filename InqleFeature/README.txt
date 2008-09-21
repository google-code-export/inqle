This Eclipse Feature project assembles the proper bundles and deploys them to a directory,
where the INQLE Server can be run.

Do not export this project as a Feature lightly!
Doing this will wipe out all files in the folder containing the INQLE Server.
E.g. the inqle configuration file (AppInfo.ttl) would be erased.
E.g. if running in embedded H2 mode all data would be lost.


To deploy the inqle server and wipe out any file data: 
In Eclipse, right click the InqleFeature project
Select Export... then Plug-in development ->
Deployable Features.