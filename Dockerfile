 FROM tomcat:8.5.50-jdk8-openjdk

# linhas substituída pela configuração das variáveis que permitirão executar
# o Dockerfile tanto para tasks.war como para tasks-bakend.war
# COPY frontend/target/tasks.war /usr/local/tomcat/webapps/tasks.war

ARG WAR_FILE
ARG CONTEXT

# as variáveis WAR_FILE e CONTEXT serão informadas na construção da imagem
 COPY ${WAR_FILE} /usr/local/tomcat/webapps/${CONTEXT}.war
