# METAMATH_SERVER
Run server locally:
spring-boot:run

Deploy server to GCP:
./deploy.sh

## IntelliJ Hints

- New project > from existing resources
- Import project from existing model (maven)..next>next>etc>jdk11
- Maven projects need to be imported: Enable auto-import
- Enable google-java-format: Enable for this project

To debug ResourceWriter in Intellij:
- Edit Configurations --> "Application"
- Main class: org.sophize.metamath.resourcewriter.ResourceWriter
- Program Arguments: runParms/RunParmsResourceGenerator.txt
- Working directory: /home/abc/code/Sophize/sophize-machines/METAMATH

To debug Server in Intellij:
- Edit Configurations --> 'Maven'
- Command line: spring-boot:run -Dspring-boot.run.fork=false
