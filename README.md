# User & Playlist Service

A microservice for managing users and playlists.

## Configuration

The application is configured through environment variables. You should define them in your system or in an `application.properties` or `application.yml` file.

### Important Configuration Properties

- **Server Settings:**
  - `server.port`: The port on which the server will listen (default: 8080)
  - `spring.profiles.active`: Environment mode (`development` or `production`)

- **Database Settings:**
  - `spring.datasource.url`: PostgreSQL connection URL
  - `spring.datasource.username`: Database user
  - `spring.datasource.password`: Database password

- **JWT Settings:**
  - `jwt.secret`: Secret key for JWT authentication
  - `jwt.expiration`: Token expiration time (default: 3600s)

### Development Mode

During development, you can disable certain features by setting the following properties in `application.properties`:

```
# Skip database connection during development
spring.datasource.url=
```

This is useful when you want to work on specific parts of the application without having all services running.

## Running the Application

```bash
java -jar user-playlist-service.jar
```

The server will start on the configured port (default: 8080).

## API Endpoints

- `POST /api/auth/register`: Register a new user
- `POST /api/auth/login`: Authenticate and get a JWT token
- `GET /api/user/profile`: Get user profile
- `PUT /api/user/profile`: Update user profile
- `POST /api/playlist`: Create a playlist
- `PUT /api/playlist/:id`: Update a playlist
- `DELETE /api/playlist/:id`: Delete a playlist
- `POST /api/playlist/:id/tracks`: Add a track to a playlist
- `DELETE /api/playlist/:id/tracks/:trackId`: Remove a track from a playlist
- `GET /api/playlist`: Get user playlists



Architecture


![image](https://github.com/user-attachments/assets/1db7990e-71ee-4014-8c94-0c3c9cecb248)

![image](https://github.com/user-attachments/assets/cf6ec9b5-0445-454a-9cbf-c3eb8cdd90f0)



# Music Conveyor

A microservice for streaming and processing audio files.

## Configuration

The application is configured through environment variables. You can create a `.env` file in the root directory based on the `.env.example` template:

```bash
# Copy the example environment file
cp .env.example .env

# Edit the .env file with your configuration
nano .env
```

### Important Environment Variables

- **Server Settings:**
  - `PORT`: The port on which the server will listen (default: 8080)
  - `ENV`: Environment mode (`development` or `production`)

- **Database Settings:**
  - `DB_HOST`: PostgreSQL host (default: localhost)
  - `DB_PORT`: PostgreSQL port (default: 5432)
  - `DB_NAME`: Database name
  - `DB_USER`: Database user
  - `DB_PASSWORD`: Database password

- **Redis Settings:**
  - `REDIS_HOST`: Redis host (default: localhost)
  - `REDIS_PORT`: Redis port (default: 6379)
  - `REDIS_PASSWORD`: Redis password (if any)
  - `REDIS_DB`: Redis database index (default: 0)

- **MinIO Settings:**
  - `MINIO_ENDPOINT`: MinIO server endpoint (default: localhost:9000)
  - `MINIO_ACCESS_KEY`: MinIO access key
  - `MINIO_SECRET_KEY`: MinIO secret key
  - `MINIO_BUCKET`: Default bucket for audio files (default: music)
  - `MINIO_USE_SSL`: Whether to use SSL for MinIO connection (default: false)

- **Kafka Settings:**
  - `KAFKA_BROKERS`: Comma-separated list of Kafka brokers (default: localhost:9092)
  - `KAFKA_GROUP_ID`: Kafka consumer group ID (default: music-conveyor)

### Development Mode

During development, you can skip connecting to external services by setting the following environment variables in your `.env` file:

```
# Skip services during development
DB_SKIP=true
REDIS_SKIP=true
MINIO_SKIP=true
KAFKA_SKIP=true
```

This is useful when you want to work on specific parts of the application without having all services running.

## Running the Application

```bash
go run cmd/app/main.go
```

The server will start on the configured port (default: 8080).

## API Endpoints

- `GET /health`: Health check endpoint
- `GET /api/stream/:id`: Stream a track
- `GET /api/stream/:id/download`: Download a track
- `GET /api/stream/status`: Check streaming status 
