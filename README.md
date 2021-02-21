# TP 10

### Informations
L'outil de Linting utilisé est **SonarLint**.
Le projet est séparé en deux parties, côté **serveur** et côté **client**.
Il y a un fichier *properties* contenant les informations du collège.
La **javadoc** est disponible dans le dossier du même nom.

### Exécutables
Le dossier *executable* contient 3 JAR exécutables :

* generator.jar
 ```shell
java -jar generator.jar
```
* server.jar
```shell
java -jar server.jar
```
* client.jar
```shell
java --module-path ./javafx --add-modules javafx.controls,javafx.fxml -jar client.jar
```

### Utilisation
Pour générer un fichier json contenant l'ensemble des informations du collège, il faut lancer l'exécutable **generator.jar**.
Pour utiliser la partie client (**client.jar**), il faut lancer l'exécutable **server.jar** avant.
