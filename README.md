# stock-price-updater
Stock price updater

#### How to execute
1. mvn clean install
2. java -jar target/price-updater-1.0.0-SNAPSHOT.jar

#### Goal
Build an application that sends messages to a legacy consumer. There is a load handler consolidating most recent stock prices

#### Consumer keeps printing
Yes, just to get our tests easier, this first release keeps consumer printing the values

#### Tests
curl -XPUT http://localhost:8080/stock/generateUpdates

curl -X PUT http://localhost:8080/stock -d '[{"companyName":"Google","price":15.89},{"companyName":"Facebook","price":56.56},{"companyName":"Google","price":18.89},{"companyName":"Facebook","price":78.89},{"companyName":"Apple","price":21.9},{"companyName":"Apple","price":25.67}]' -H "Content-type: application/json"

curl -XPUT http://localhost:8080/stock/add/Google/price/162.78/


