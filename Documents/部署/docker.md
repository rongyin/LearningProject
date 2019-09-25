# stop vs kill

docker stop
Stop a running container by sending SIGTERM and then SIGKILL after a grace period

docker kill
Kill a running container using SIGKILL or a specified signal

https://www.jianshu.com/p/a5ea2ff60594

```
# 使用官方提供的 Python 开发镜像作为基础镜像
FROM python:2.7-slim

# 将工作目录切换为 /app
WORKDIR /app

# 将当前目录下的所有内容复制到 /app 下
ADD . /app

# 使用 pip 命令安装这个应用所需要的依赖
RUN pip install --trusted-host pypi.python.org -r requirements.txt

# 允许外界访问容器的 80 端口
EXPOSE 80

# 设置环境变量
ENV NAME World

# 设置容器进程为：python app.py，即：这个 Python 应用的启动命令
CMD ["python", "app.py"]
entrypoint [""]

```

- An ENTRYPOINT allows you to configure a container that will run as an executable.
- The main purpose of a CMD is to provide defaults for an executing container. These defaults can include an executable, or they can omit the executable, in which case you must specify an ENTRYPOINT instruction as well.
- RUN 指令：用于指定 docker build 过程中要运行的命令。
