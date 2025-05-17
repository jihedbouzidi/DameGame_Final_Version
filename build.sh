#!/bin/bash

# Créer les répertoires nécessaires
mkdir -p bin lib

# Télécharger les dépendances si elles n'existent pas
if [ ! -f lib/h2-2.2.224.jar ]; then
    echo "Téléchargement de H2 Database..."
    curl -L https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar -o lib/h2-2.2.224.jar
fi

if [ ! -f lib/jackson-databind-2.15.2.jar ]; then
    echo "Téléchargement de Jackson Databind..."
    curl -L https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.15.2/jackson-databind-2.15.2.jar -o lib/jackson-databind-2.15.2.jar
fi

if [ ! -f lib/jackson-core-2.15.2.jar ]; then
    echo "Téléchargement de Jackson Core..."
    curl -L https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.15.2/jackson-core-2.15.2.jar -o lib/jackson-core-2.15.2.jar
fi

if [ ! -f lib/jackson-annotations-2.15.2.jar ]; then
    echo "Téléchargement de Jackson Annotations..."
    curl -L https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.15.2/jackson-annotations-2.15.2.jar -o lib/jackson-annotations-2.15.2.jar
fi

# Compiler le projet
echo "Compilation du projet..."
javac -d bin -cp "lib/*" src/com/dames/Main.java src/com/dames/model/*.java src/com/dames/controller/*.java src/com/dames/view/*.java

# Exécuter le projet
echo "Exécution du projet..."
java -cp "bin:lib/*" com.dames.Main 