# Articles App
This is a second task for Profitsoft Academy. CLone this code from github and run it using instructions provided bellow.
## Running
For running on your local machine select **dev** profile. Before running make sure you have available working database. You need to set-up following environment properties:
- **`DB_USER`** database username.
- **`DB_PASSWORD`** password for provided user.
- **`DB_URL`** link to your database.

For testing upload method use articles.json in **`src/main/resources/static`**
## Technologies Used:
- **`Spring Boot:`** Facilitates rapid development of production-ready applications.
- **`Postgre Database:`** A database for storing user data during application runtime.
- **`Spring Data JPA:`** Simplifies the implementation of data access layers in Spring applications.
- **`MapStruct`** Streamlines the process of mapping between DTOs and entities.
- **`Spring Validation`** Ensures that incoming data meets specified criteria, enhancing the robustness of the application.
- **`JUnit and Mockito`** Utilized for writing unit and integration tests to validate the functionality of the application.
- **`Docker Testcontainers`** To perform integration tests.
- **`Swagger`** Facilitates app preview. You can access it at */api/v1/swagger-ui/index.html*
## Endpoints
1. **POST** *api/v1/articles* : Creates a new article and saves to database if all fields are valid and author available.
2. **PUT** *api/v1/articles/{id}* : Updates article entity if one exists. Field validation enabled.
3. **DELETE** *api/v1/articles/{id}* : Deletes  article entity if one exists.
4. **GET** *api/v1/articles/{id}* : Finds article by id.
5. **POST** *api/v1/articles/_list* Finds articles by search parameters and returns page as result.
6. **POST** *api/v1/articles/_report* Finds articles by search parameters and returns excel file as result.
7. **POST** *api/v1/articles/upload* Consumes a JSON file with list of articles and saves all valid ones. Returns count of saved and invalid articles.
8. **POST** *api/v1/authors* : Creates a new author and saves to database if all fields are valid.
9. **PUT** *api/v1/authors/{id}* : Updates author entity if one exists. Field validation enabled.
10. **DELETE** *api/v1/authors/{id}* : Deletes author entity if one exists.
11. **GET** *api/v1/authors* : Returns all available authors.

## Entities
### Article
- **`id`** (UUID)
- **`title`** (String)
- **`field`** (Field)
- **`journal`** (String)
- **`year`** (Integer)
- **`author`** (Author)
### Author
- **`id`** (UUID)
- **`firstName`** (String)
- **`lastName`** (String)
- **`email`** (String)

### Field(Enum)
- PHYSICS
- CHEMISTRY
- ENGINEERING
- ASTRONOMY
- HISTORY
- ECONOMICS
- LINGUISTICS
- MATHEMATICS
- BIOLOGY
- LITERATURE
- COMPUTER_SCIENCE
- PSYCHOLOGY
- MEDICINE