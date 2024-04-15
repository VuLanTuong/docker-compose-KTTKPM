cd registry
./mvnw clean package
cd ..

cd gateway
./mvnw clean package
cd ..

cd AuthService
./gradlew clean build
cd ..

cd BlogService
./gradlew clean build
cd ..

docker compose down
docker compose up --build

