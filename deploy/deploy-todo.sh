#!/bin/bash
set -euo pipefail

if [ "$#" -ne 1 ]; then
  printf 'Использование: bash deploy-todo.sh IMAGE_TAG\n' >&2
  exit 1
fi

IMAGE_TAG="$1"
IMAGE="ghcr.io/ponomareenko/todo-api:${IMAGE_TAG}"

printf 'Образ для деплоя: %s\n' "$IMAGE"

ACTIVE_PORT=$(sed -nE 's#^[[:space:]]*proxy_pass http://127\.0\.0\.1:(8080|8081);[[:space:]]*$#\1#p' /etc/nginx/sites-available/todo-api)

printf 'Активный порт: %s\n' "$ACTIVE_PORT"

case "$ACTIVE_PORT" in
  8080)
    TARGET_PORT=8081
    TARGET_CONTAINER=todo-api-candidate
    ;;
  8081)
    TARGET_PORT=8080
    TARGET_CONTAINER=todo-api-test
    ;;
  *)
    printf 'Ошибка: неожиданный активный порт: %s\n' "$ACTIVE_PORT" >&2
    exit 1
    ;;
esac

printf 'Целевой порт: %s\nКонтейнер: %s\n' \
  "$TARGET_PORT" "$TARGET_CONTAINER"

printf 'Скачиваем образ: %s\n' "$IMAGE"
docker pull "$IMAGE"

if docker container inspect "$TARGET_CONTAINER" > /dev/null 2>&1; then
  docker stop "$TARGET_CONTAINER"
  docker rm "$TARGET_CONTAINER"
fi

docker run -d \
  --name "$TARGET_CONTAINER" \
  --restart unless-stopped \
  -p "127.0.0.1:${TARGET_PORT}:8080" \
  "$IMAGE"

READY=false

for attempt in {1..24}; do
  CONTAINER_STATE=$(docker inspect --format '{{.State.Status}}' "$TARGET_CONTAINER")
  HEALTH_STATUS=$(docker inspect --format '{{.State.Health.Status}}' "$TARGET_CONTAINER")

  printf 'Попытка %s: состояние=%s, здоровье=%s\n' \
    "$attempt" "$CONTAINER_STATE" "$HEALTH_STATUS"

  if [ "$CONTAINER_STATE" = "running" ] && [ "$HEALTH_STATUS" = "healthy" ]; then
    READY=true
    break
  fi

  if [ "$HEALTH_STATUS" = "unhealthy" ] ||
     [ "$CONTAINER_STATE" = "exited" ] ||
     [ "$CONTAINER_STATE" = "dead" ]; then
    break
  fi

  sleep 5
done

if [ "$READY" != "true" ]; then
  printf 'Ошибка: новый контейнер не готов. Трафик не переключён.\n' >&2
  docker logs --tail 50 "$TARGET_CONTAINER" >&2
  exit 1
fi

printf 'Новый контейнер готов к переключению.\n'

sudo -n /usr/local/sbin/switch-todo "$TARGET_PORT"
