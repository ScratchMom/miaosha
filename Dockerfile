# 基础镜像
FROM openjdk:8-jdk-alpine
MAINTAINER LAOWANG
# 工作目录
WORKDIR /opt/apps/gateway/logs/
# 文件拷贝,把target目录下的jar报拷贝到镜像的/APP/目录下
ADD target/miaosha_idea.jar /App/
# 暴露的端口号,没有实际作用
EXPOSE 8089
# 指定JVM大小
ENTRYPOINT ["java","-Xmx2048m","-jar"]
# 运行Jar包
CMD ["/App/miaosha_idea.jar"]