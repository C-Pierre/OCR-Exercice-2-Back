# README.md

## Initialisation et lancement des applications

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

Créer le fichier `./src/main/resources/application.properties` avec les éléments suivants :
-     spring.application.name=back
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.datasource.url=jdbc:mysql://localhost:3306/chatop_db?useSSL=false&serverTimezone=UTC
      spring.datasource.username=DB_USERNAME
      spring.datasource.password=DB_PASSWORD
      spring.servlet.multipart.enabled=true
      spring.servlet.multipart.max-file-size=10MB
      spring.servlet.multipart.max-request-size=10MB
      app.jwt.secret=APP_SECRE_TKEY
      app.uploads.path = uploads/
      app.front.url = http://localhost:4200
      server.port=3001

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
          - `LoginRequest` et `RegisterRequest.java` : interfaces pour les requêtes
        - response/
          - `AuthResponse` : interface pour les réponses
        - security/
          - `SecurityBeans` : configuration pour l'encodage du mot de passe
        - service/
          - `AuthService` : service pour la gestion de l'authentification
      - config/
        - `OpenApiConfig` : configuration de la documentation
        - `SecurityConfig` : configuration de la sécurité de l'application et du routing
        - `WebConfig` : configuration web / CORS
      - exception/
        - `ErrorResponse` : interface pour le format de réponse des erreurs
        - `GlobalExceptionHandler` : gestion globale des exceptions de l'application
      - message/
        - controller/
          - `MessageController` : controller des messages
        - entity/
          - `MessageEntity` : l'entité des messages
        - repository/
          - `MessageRepository` : le repository des messages
        - request/
          - `CreateMessageRequest` : l'interface pour la création d'un message
        - response/
          - `MessageResponse` : l'interface pour la réponse des messages
        - service/
          - `MessageService` : service pour la gestion des messages
        - validator/
          - `MessageValidator`: validateur pour les informations d'un message
      - rental/
        - controller/
          - `RentalController` : controller des locations
        - entity/
          - `RentalEntity` : l'entité des locations
        - repository/
          - `RentalRepository` : le repository des locations
        - request/
          - `CreateRentalRequest` : l'interface pour la création d'une location
          - `UpdateRentalRequest` : l'interface pour l'édition d'une location
        - response/
          - `RentalResponse` : l'interface pour la réponse des locations
        - service/
          - `RentalService` : service pour la gestion des locations
          - `RentalPictureService` : service pour la gestion des images des locations
          - `RentalResponseService` : service pour la gestion des réponses liées aux locations
        - validator/
          - `RentalValidator`: validateur pour les informations d'une location
      - user/
        - controller/
          - `UserController` : controller des utilisateurs
        - entity/
          - `UserEntity` : l'entité des utilisateurs
        - repository/
          - `UserRepository` : le repository des utilisateurs
        - response/
          - `UserResponse` : l'interface pour la réponse des utilisateurs
        - service/
          - `UserService` : service pour la gestion des utilisateurs
        - validator/
          - `UserValidator`: validateur pour les informations d'un utilisateur
      - `BackApplication` : le fichier principal, en charge de l'exécution de l'application
    - main/resources/
      - `application.properties` : les variables de l'application (non versionné)
  - uploads/
    - emplacement des fichiers / images importés sur le serveur (non versionné)
  - `pom.xml` : fichier responsable des imports de dépendances 

## Développement

L'API a été réalisée en Java 21 et Spring 4, avec Maven via l'IDE IntelliJ.

La documentation OpenAPI 3 est générée via les annotations placées dans les controllers.

Un système de cache a été mis en place dans les différents services principaux (User, Rental, Message).

## Dépendances

Présentation des dépendances / librairies utilisées dans l'API.

### Spring Boot Starters
- `spring-boot-starter-data-jpa` :	Fournit JPA/Hibernate pour la gestion des entités, ORM et accès à la base de données.
- `spring-boot-starter-security` : Ajoute Spring Security pour l’authentification et l’autorisation.
- `spring-boot-starter-validation` : Permet l’utilisation des annotations de validation (@NotNull, @Size, etc.) via Jakarta Bean Validation.
- `spring-boot-starter-webmvc` : Fournit les fonctionnalités Spring MVC pour créer des API REST et servir des pages web.
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
- `spring-boot-maven-plugin` : Permet de packager l’application Spring Boot en jar/war exécutable.