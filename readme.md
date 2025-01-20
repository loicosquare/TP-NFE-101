# Pharmacies-Kafka-Project
Ce projet implémente une architecture basée sur Apache Kafka pour le traitement et l'analyse de données relatives aux pharmacies à Paris. Les données passent par un pipeline Kafka où elles sont produites, transformées et consommées, avant d'être stockées dans une base de données MySQL et exposées via une API REST.

# Prérequis
## Kafka et Zookeeper
## Téléchargez et installez Apache Kafka. Vous pouvez suivre la documentation officielle d'Apache Kafka.
## Java Development Kit (JDK)
Assurez-vous d'avoir installé Java 17 ou une version supérieure.
## MySQL
Installez MySQL et configurez une base de données pour le projet.
## Postman (ou tout autre client API)
Utilisé pour tester les endpoints exposés par l'application.

# 1. Configuration de Zookeeper et Kafka
   Avant de commencer, assurez-vous que Zookeeper et Kafka sont configurés correctement. Voici les étapes:
### Démarrer Zookeeper

### ./bin/windows/zookeeper-server-start.bat ./config/zookeeper.properties
### Démarrer Kafka

### ./bin/windows/kafka-server-start.bat ./config/server.properties

# 2. Gestion des Topics Kafka
Un topic est un flux de messages géré par Kafka. Les étapes ci-dessous montrent comment créer et interagir avec les topics nécessaires :

### Créer le topic pharmacies.raw avec 3 partitions

### ./bin/windows/kafka-topics.bat --create --topic pharmacies.raw --bootstrap-server localhost:9092 --partitions 3
### Voir les détails du topic

### ./bin/windows/kafka-topics.bat --describe --topic pharmacies.raw --bootstrap-server localhost:9092
### Lister tous les topics existants

### ./bin/windows/kafka-topics.bat --bootstrap-server localhost:9092 --list
### Lire les messages du topic pharmacies.raw
### ./bin/windows/kafka-console-consumer.bat --topic pharmacies.raw --from-beginning --bootstrap-server localhost:9092
