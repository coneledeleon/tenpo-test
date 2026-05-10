cd /tmp
rm -fr compose.zip docker-compose ## en caso de que existiera con anterioridad
wget -O compose.zip https://github.com/coneledeleon/tenpo-test/raw/refs/heads/develop/compose.zip
unzip -x compose.zip
docker compose -f ./docker-compose/tenpo-test-compose.yml up