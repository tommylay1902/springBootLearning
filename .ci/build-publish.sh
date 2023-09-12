 "${USERNAME:?USERNAME not set or empty}"
: "${REPO:?REPO not set or empty}"
: "${TAG:?TAG not set or empty}"

# Set the path to your custom Dockerfile
DOCKERFILE="Dockerfile-Frontend"

docker buildx create --use

docker buildx build \
    --platform=linux/amd64,linux/arm64 \
    -t "${USERNAME}/${REPO}:${TAG}" \
    -t "${USERNAME}/${REPO}:latest" \
    -f "${DOCKERFILE}" \
    "${@:2}" \
    --push \
    "$1"