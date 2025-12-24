Project URL: https://roadmap.sh/projects/blogging-platform-api
## Running the Application

You can run the application using the included Maven wrapper, which ensures a consistent build environment.

From the project root directory, run:

```bash
./mvnw spring-boot:run
```

The application will start, connect to the database, and the API will be available at `http://localhost:8080`. You should see a log message similar to this in your terminal:

`... Started CmsApplication in X.XXX seconds (process running for Y.YYY)`

## Running the Tests

This project has a comprehensive test suite covering the controller, service, and repository layers. To run all tests, execute:

```bash
./mvnw clean test
```

A successful run will end with the message: `[INFO] BUILD SUCCESS`.

## API Endpoints

The following endpoints are available. You can use a tool like Postman, Insomnia, or cURL to interact with them.

| Method   | Endpoint            | Description                                   | Success Response          |
| :------- | :------------------ | :-------------------------------------------- | :------------------------ |
| `POST`   | `/api/posts`        | Creates a new blog post.                      | `201 Created` & Post JSON |
| `GET`    | `/api/posts/{slug}` | Retrieves a single post by its slug.          | `200 OK` & Post JSON      |
| `PUT`    | `/api/posts/{slug}` | Updates an existing post.                     | `200 OK` & Post JSON      |
| `DELETE` | `/api/posts/{slug}` | Deletes a post by its slug.                   | `204 No Content`          |
| `GET`    | `/health`           | Health check endpoint to verify server is up. | `200 OK` & String         |
