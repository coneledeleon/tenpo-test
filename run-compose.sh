#!/bin/bash

### Se valida que docker esté instalado
if ! command -v docker &>/dev/null; then
  echo "Docker no está instalado o no se encuentra en el PATH."
  echo "Puedes ver como instalarlo en: https://docs.docker.com/compose/install/"
  exit 1
fi

echo "Se encuentra docker: $(docker --version)"

### Se valida la versión de Docker, pasa saber como ejecutar Docker Compose
DOCKER_COMPOSE_CMD=""
if docker compose version &>/dev/null 2>&1; then
  DOCKER_COMPOSE_CMD="docker compose"
  echo "Docker Compose v2 detectado: $(docker compose version --short 2>/dev/null || docker compose version)"
elif command -v docker-compose &>/dev/null; then
  DOCKER_COMPOSE_CMD="docker-compose"
  echo "[warning] Docker Compose v1 detectado (deprecado): $(docker-compose --version)"
  echo "[warning] Se recomienda actualizar a Docker Compose v2."
else
  echo "[error] Docker Compose no está disponible (ni 'docker compose' ni 'docker-compose')."
  echo "[error] Instálalo desde: https://docs.docker.com/compose/install/"
  exit 1
fi

echo "Usando comando: '${DOCKER_COMPOSE_CMD}'"


cd /tmp
rm -fr compose.zip docker-compose ## en caso de que existiera con anterioridad

for cmd in wget unzip; do
  if ! command -v "$cmd" &>/dev/null; then
    echo "[error] Dependencia requerida no encontrada: '$cmd'. Instálala e intenta de nuevo."
    exit 1
  fi
done

wget -O compose.zip https://github.com/coneledeleon/tenpo-test/raw/refs/heads/develop/compose.zip
unzip -x compose.zip

echo "Levantando Docker compose..."
${DOCKER_COMPOSE_CMD} -f ./docker-compose/tenpo-test-compose.yml up