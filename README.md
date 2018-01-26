Test (application deployed in application context `voting`).
========================================
> For windows use `Git Bash`

##### Resources:
+ `restaurant`
+ `dish`
## Examples of curl commands and JSON responses
#### get all restaurants (sorted by name)
`curl -s http://localhost:8080/voting/restaurants --user user@gmail.com:userPassword`

`JSON response example: [{"id":100004,"name":"Restaurant1"},{"id":100005,"name":"Restaurant2"},{"id":100006,"name":"Restaurant3"}]`

#### get all restaurants with menu (sorted by name)
`curl -s http://localhost:8080/voting/restaurants/with_menu --user user@gmail.com:userPassword`

`JSON response example: [{"id":100004,"name":"Restaurant1","menu":[{"id":100009,"name":"vine1","price":550},{"id":100007,"name":"salad1","price":600},{"id":100008,"name":"steak1","price":800}]},{"id":100005,"name":"Restaurant2","menu":[{"id":100018,"name":"vine2","price":500},{"id":100016,"name":"salad2","price":540},{"id":100017,"name":"steak2","price":750}]},{"id":100006,"name":"Restaurant3","menu":[{"id":100021,"name":"vine3","price":530},{"id":100019,"name":"salad3","price":570},{"id":100020,"name":"steak3","price":770}]}]`

#### get all restaurants with votes (sorted by count of votes in reverse order)
`curl -s http://localhost:8080/voting/restaurants/with_votes --user user@gmail.com:userPassword`

`JSON response example: [{"id":100004,"name":"Restaurant1","countOfVotes":2},{"id":100006,"name":"Restaurant3","countOfVotes":1},{"id":100005,"name":"Restaurant2","countOfVotes":0}]`

#### vote for restaurant with id = 100004
`curl -s -X POST http://localhost:8080/voting/restaurants/100004/vote --user user@gmail.com:userPassword`
###### 2 variants of JSON response depending on execution time of request: 1) before 11:00; 2) after 11:00
`first case example: {"date":"2018-01-25","restaurant":{"id":100004,"name":"Restaurant1"}}`

`second case example: {"url":"http://localhost:8080/voting/restaurants/100004/vote","message":"You can not vote after 11:00"}`

#### get restaurant with id = 100004
`curl -s http://localhost:8080/voting/restaurants/100004 --user user@gmail.com:userPassword`

`JSON response example: {"id":100004,"name":"Restaurant1","menu":[{"id":100009,"name":"vine1","price":550},{"id":100007,"name":"salad1","price":600},{"id":100008,"name":"steak1","price":800}]}`

#### get restaurant with id = 100004 with votes
`curl -s http://localhost:8080/voting/restaurants/100004/with_votes --user user@gmail.com:userPassword`

`JSON response example: {"id":100004,"name":"Restaurant1","countOfVotes":2}`

#### get menu of restaurant with id = 100004 (sorted by price)
`curl -s http://localhost:8080/voting/restaurants/100004/dishes --user user@gmail.com:userPassword`

`JSON response example: [{"id":100009,"name":"vine1","price":550},{"id":100007,"name":"salad1","price":600},{"id":100008,"name":"steak1","price":800}]`

### For Admin
#### create restaurant
`curl -s -X POST -d '{"name":"newRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants --user admin@gmail.com:adminPassword`

`JSON response example: {"id":100022,"name":"newRestaurant"}`

#### update restaurant with id = 100004
`curl -s -X PUT -d '{"name":"UPDATED Restaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants/100004 --user admin@gmail.com:adminPassword`

#### delete restaurant with id = 100004
`curl -s -X DELETE http://localhost:8080/voting/restaurants/100004 --user admin@gmail.com:adminPassword`

#### get dish with id = 100013 in restaurant with id = 100004
`curl -s http://localhost:8080/voting/restaurants/100004/dishes/100013 --user admin@gmail.com:adminPassword`

`JSON response example: {"id":100013,"name":"salad1","price":700}`

#### create dish in restaurant with id = 100004
`curl -s -X POST -d '{"name":"newDish", "price":500, "date":"2118-12-31"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants/100004/dishes --user admin@gmail.com:adminPassword`

`JSON response example: {"id":100024,"name":"newDish","price":500}`

#### update dish with id = 100013 in restaurant with id = 100004
`curl -s -X PUT -d '{"name":"UPDATED Dish", "price":500, "date":"2118-12-31"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants/100004/dishes/100013 --user admin@gmail.com:adminPassword`

#### delete dish with id = 100007 in restaurant with id = 100004
`curl -s -X DELETE http://localhost:8080/voting/restaurants/100004/dishes/100007 --user admin@gmail.com:adminPassword`

### Basic errors and exceptional situations

#### not found resource
`curl -s http://localhost:8080/voting/restaurants/1 --user user@gmail.com:userPassword`

`JSON response example: {"url":"http://localhost:8080/voting/restaurants/1","message":"Not found entity with id=1"}`

#### forbidden operation
`curl -s -X POST -d '{"name":"newRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants --user user@gmail.com:userPassword`

`JSON response example: {"url":"http://localhost:8080/voting/restaurants","message":"Access is denied"}`

#### validation fail
###### restaurant:
`curl -s -X POST -d '{"name":" "}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants --user admin@gmail.com:adminPassword`

`JSON response example: {"url":"http://localhost:8080/voting/restaurants","message":"name must not be blank; name size must be between 2 and 100"}`

###### dish:
`curl -s -X POST -d '{"name":" ", "price":5, "date":"2118-12-31"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants/100004/dishes --user admin@gmail.com:adminPassword`

`JSON response example: {"url":"http://localhost:8080/voting/restaurants/100004/dishes","message":"name must not be blank; name size must be between 2 and 100; price must be between 50 and 50000"}`
#### violation of uniqueness
###### restaurant:
`curl -s -X POST -d '{"name":"Restaurant1"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants --user admin@gmail.com:adminPassword`

`JSON response example:{"url":"http://localhost:8080/voting/restaurants","message":"Restaurant with this name already exists"}`

###### dish:
`curl -s -X POST -d '{"name":"salad1", "price":500, "date":"2118-12-31"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/restaurants/100004/dishes --user admin@gmail.com:adminPassword`

`JSON response example: {"url":"http://localhost:8080/voting/restaurants/100004/dishes","message":"Dish with this date and name already exists in this restaurant"}`









