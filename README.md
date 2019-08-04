# Springboot Reactive Postgres

#### Books CRUD Example Project.
Project has standard **REST API**:
- GET `/` get all books.
- GET `/{id}` get book with id.
- POST `/` create new book.
- PUT `/{id}` update book.
- DELETE `/{id}` delete book.
- DELETE `/` delete all books. 

#### Project has 2 versions of API:
 - `/v1/books` Standard Spring Annotated Controller.
 - `/v2/books` Functional Endpoints with **RouterFunction** and Handler.