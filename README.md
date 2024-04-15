# Docker compose demo

## How to run ?

### 1. Build registry service
```bash
cd registry
./mvnw clean package
```

### 2. Build gateway service
```bash
cd gateway
./mvnw clean package
```

### 3. Build auth service
```bash
cd AuthService
./gradlew clean build
```

### 4. Build blog service
```bash
cd BlogService
./gradlew clean build
```

### 5. Run all services using docker compose
```bash
docker compose up --build
```

### 6. Start and build all start using utility script
```bash
./start.sh
```

### 7. Test services
```bash to see all posts
curl http://localhost:8888/blog/index
```

```bash to add blog
1. localhost:8888/auth/login
tuong@gmail.com
123
if login successful, you will add post and logout


