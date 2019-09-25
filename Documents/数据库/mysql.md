# docker
1. docker run --name docker_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:latest
2. docker exec -it docker_mysql /bin/bash
3. mysql -u root -p
