# README.md

## Initialisation et lancement des applications

Il faut récupérer au préalable le dépôt Git de la partie front-end pour utiliser la web-app :
-   https://github.com/C-Pierre/OCR-Exercice-2

### Base de données

Lancer les commandes suivantes pour créer la DB et ses tables via le script fournit :
-     mysql -u root -p
      CREATE DATABASE IF NOT EXISTS chatop_db;
      CREATE USER IF NOT EXISTS 'myUser'@'localhost' IDENTIFIED BY 'MotDePasse';
      GRANT ALL PRIVILEGES ON chatop_db.* TO 'myUser'@'localhost';
      FLUSH PRIVILEGES;
      mysql -u myUser -p chatop_db < ./script.sql

Le script `script.sql` a été copié à la racine de l'API. Il est toujours présent dans le front.

### API / Back-end

Créer le fichier `.env` à la racine du projet avec les éléments suivants :
-     DB_URL=jdbc:mysql://localhost:3306/chatop_db
      DB_USERNAME=myUser
      DB_PASSWORD=MotDePasse
      SERVER_PORT=3001
      APP_DOCUMENTATION_URI=/v3/api-docs
      APP_SWAGGER_URI=/swagger-ui
      APP_BASE_PATH=/api
      APP_SECURITY_PUBLIC_PATHS=/api/auth/login,/api/auth/register,/swagger-ui/**,/v3/api-docs/**
      APP_JWT_DURATION=86400000
      APP_JWT_SECRET=mySecretTokenToGenerateAndProvide
      APP_UPLOADS_PATH=uploads/
      APP_FRONT_URL=http://localhost:4200

Lancer ensuite l'application avec la commande :
-     nvm spring-boot:run

### Web-app / Front-end

Installer l'application avec la commande :
-     npm install

Lancer ensuite l'application avec la commande :
-     npm run start

## Utilisation

Une fois le front et le back initialisés et installés, l'application est disponible aux endpoints suivants :
- http://localhost:4200 (application : web-app + API) 
- http://localhost:3001 (API)

## Documentation

Une documentation a été mise en place avec OpenApi.   
Celle-ci est accessible sur les endpoints suivants :
- http://localhost:3001/swagger-ui/index.html
- http://localhost:3001/v3/api-docs

## Fonctionnement

### Authentification
La quasi-totalité des routes nécessite d'être authentifié.

Depuis Postman ou équivalent, après le login et la récupération du token, il faut que celui-ci soit joint au `Header`, au niveau de l'`Authorization`, et ce pour chaque requête :
-     Authorization : bearer monSuperTokenBienLongJusteIci

Le token est valide durant 24 heures.

### Body
Pour les requêtes POST et PUT des Rentals, le body doit être sous la forme d'un `Content-Type : multi-part/form-data`.

Les autres requêtes POST et PUT ont un elles un body sous forme de JSON : `Content-type : application/json`.

### Règles conditionnelles
Les Messages d'un Rental ne peuvent être récupérés que par le Owner ou le User.

Un Rental ne peut être modifié ou supprimé que par son Owner.

Un User ne peut être modifié ou supprimé que par le User lui-même.

## Développement

L'API a été réalisée en Java 17, Spring 4 et Maven 3.

La documentation OpenAPI 3 est générée via les annotations placées dans les controllers.

Un système de cache a été mis en place dans les différents services principaux (User, Rental, Message).
Il est possible de le vérifier depuis les logs du fait de la présence de `logging.level.org.springframework.cache=TRACE` dans `application.properties`.

## Architecture de l'API

- back-api/
  - src/
    - main/java/com/chatop/back/
      - auth/
        - controller/
          - `AuthController` : controller pour l'authentification
        - jwt/
          - `JwtAuthFilter` : filtre sur les requêtes HTTP pour vérifier le token
          - `JwtService` : service responsable de la lecture et création du token
        - request/
          - `LoginRequest` et `RegisterRequest.java` : DTO pour les requêtes
        - dto/
          - `AuthDto` : DTO pour les réponses
        - security/
          - `SecurityBeans` : configuration pour l'encodage du mot de passe
        - service/
          - `AuthLoginService` : service pour l'authentification
          - `AuthRegisterService` : service pour la création d'un compte / User
      - common/
        - authorization/
          - validator/
            - `AuthorizationValidator`: validateur commun pour les permissions d'un User
        - error/
          - builder/
            - `ErrorBuilder` : builder pour la réponse des erreurs
          - response/
            - `ErrorResponse` : DTO pour le format de réponse des erreurs
          - exception/
            - handler/
              - `GlobalExceptionHandler` : gestion globale des exceptions de l'application
      - config/
        - `OpenApiConfig` : configuration de la documentation
        - `SecurityConfig` : configuration de la sécurité de l'application et du routing
        - `WebConfig` : configuration web / CORS
      - message/
        - controller/
          - `MessageController` : controller des messages
        - entity/
          - `MessageEntity` : l'entité des messages
        - mapper/
          - `MessageMapper` : mapper de l'entité vers le DTO
        - repository/
          - `MessageRepository` : le repository des messages
        - request/
          - `CreateMessageRequest` : DTO pour la création d'un message
        - dto/
          - `MessageDto` : DTO pour la réponse d'un message
          - `MessagesDto` : DTO pour la réponse des messages
        - service/
          - `CreateMessageService` : service pour la création d'un message
          - `DeleteMessageService` : service pour la suppression d'un message
          - `UpdateMessageService` : service pour la modification d'un message
          - `GetMessageService` : service pour la récupération d'un message
          - `GetMessagesService` : service pour la récupération des messages
        - validator/
          - `MessageAuthorizationValidator`: validateur pour les permissions d'un User
      - rental/
        - controller/
          - `RentalController` : controller des locations
        - dto/
          - `RentalDto` : DTO pour la réponse d'une location
          - `RentalsDto` : DTO pour la réponse des locations
        - entity/
          - `RentalEntity` : l'entité des locations
        - mapper/
          - `RentalMapper` : mapper de l'entité vers le DTO
        - repository/
          - `RentalRepository` : le repository des locations
        - request/
          - `CreateRentalRequest` : DTO pour la création d'une location
          - `UpdateRentalRequest` : DTO pour l'édition d'une location
        - service/
          - picture/
            - `EncodeRentalPictureService` : service pour l'encodage base64 d'une image de location
            - `SaveRentalPictureService` : service pour la sauvegarde d'une image de location
          - `CreateRentalService` : service pour la création d'une location
          - `DeleteRentalService` : service pour la suppression d'une location
          - `UpdateRentalService` : service pour la modification d'une location
          - `GetRentalService` : service pour la récupération d'une location
          - `GetRentalsService` : service pour la récupération des locations
        - validator/
          - `RentalAuthorizationValidator`: validateur pour les permissions d'un User
      - user/
        - controller/
          - `UserController` : controller des utilisateurs
        - entity/
          - `UserEntity` : l'entité des utilisateurs
        - mapper/
          - `UserMapper` : mapper de l'entité vers le DTO
        - repository/
          - `UserRepository` : le repository des utilisateurs
        - request/
          - `CreateUserRequest` : DTO pour la création d'un user
          - `UpdateUserRequest` : DTO pour l'édition d'un user
        - dto/
          - `UserDto` : DTO pour la réponse d'un utilisateur
          - `UsersDto` : DTO pour la réponse des utilisateurs
        - service/
          - `CeateUserService` : service pour la création d'un utilisateur
          - `DeleteteUserService` : service pour la suppression d'un utilisateur
          - `UpdateService` : service pour la modification d'un utilisateur
          - `GetCurrentUserService` : service pour la récupération de l'utilisateur courant
          - `GetUserService` : service pour la récupération d'un utilisateur
          - `GetUsersService` : service pour la récupération des utilisateurs
        - validator/
          - `UserAuthorizationValidator`: validateur pour les permissions d'un User
      - `BackApplication` : le fichier principal, en charge de l'exécution de l'application
    - main/resources/
      - `application.properties` : les variables de l'application
  - uploads/
    - emplacement des fichiers / images importés sur le serveur (non versionné)
  - `pom.xml` : fichier responsable des imports de dépendances 
  - `script.sql` : le script pour créer la base de données du projet 
  - `.env` : les variables d'environnement (non versionné)

## Dépendances

Présentation des dépendances / librairies utilisées dans l'API.

### Spring Boot Starters
- `spring-boot-starter-data-jpa` :	Fournit JPA/Hibernate pour la gestion des entités, ORM et accès à la base de données.
- `spring-boot-starter-security` : Ajoute Spring Security pour l’authentification et l’autorisation.
- `spring-boot-starter-validation` : Permet l’utilisation des annotations de validation (@NotNull, @Size, etc.) via Jakarta Bean Validation.
- `spring-boot-starter-web` : Fournit les fonctionnalités Spring pour créer des API REST et servir des pages web.
- `spring-boot-starter-cache` : Permet la mise en cache avec Spring (@Cacheable, @CacheEvict).

### Spring Boot Dev Tools
- `spring-boot-devtools` : Redémarrage automatique de l’application pendant le développement, support du LiveReload.

### Base de données
- `mysql-connector-j` : Driver JDBC pour MySQL, nécessaire pour se connecter à la base de données MySQL.

### Lombok
- `lombok` : Génère automatiquement getters, setters, builders, constructeurs, etc., pour réduire le boilerplate.

### Spring Boot Testing
- `spring-boot-starter-data-jpa-test` : Fournit des outils pour tester les repositories JPA.
- `spring-boot-starter-security-test` : Fournit des utilitaires pour tester la sécurité (mock authentication).
- `spring-boot-starter-validation-test` : Utilitaire pour tester la validation des objets.
- `spring-boot-starter-webmvc-test` : Utilitaire pour tester les contrôleurs REST et Spring MVC.

### JWT
- `jjwt-api` : Fournit les classes principales pour créer et parser des JWT.
- `jjwt-impl` : Implémentation runtime des fonctionnalités de jjwt-api.
- `jjwt-jackson` : Permet de sérialiser/désérialiser les claims JWT en JSON via Jackson.

### Swagger/OpenAPI
- `springdoc-openapi-starter-webmvc-ui` : Génère automatiquement la documentation OpenAPI (Swagger UI) pour tes endpoints REST.

### Plugins Maven
- `maven-compiler-plugin` : Configure le compilateur Java, et permet d’utiliser Lombok comme annotation processor.
- `spring-boot-maven-plugin` : Permet de packager l’application Spring Boot en jar/war exécutable

### Dotenv
- `dotenv-java` : Pour l'utilisation d'un fichier .env pour les variables d'environnement dans l'app.