sudo docker build -t sinonblack/sinonhub:dev .
sudo docker-compose up -d
sudo docker-compose ps
echo  "Start completed"

docker rmi $(docker images | grep "^<none>" | awk '{print $3}') --force
docker logs -f webslots