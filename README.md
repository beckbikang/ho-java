# ho-java overview
accept memcache protocol data save it to kafka use netty

just use set command,write to kafka easily

# architecture

<img width="611" alt="image" src="https://user-images.githubusercontent.com/7270440/196463451-7d158eeb-c49c-4a88-8406-efe43b533c51.png">


# the feature
1. ✅write data to kafka by topic 
2. ✅registe server to consul
3. add prometheus monitor application





# how to use server

## 1 start Java server

<img width="1197" alt="image" src="https://user-images.githubusercontent.com/7270440/196458902-826417c1-e919-4bdd-a8c9-d93e371995a8.png">


## 2 use test script

```python
import memcache
mc = memcache.Client(['127.0.0.1:11222'], debug=True)
#mc = memcache.Client(['127.0.0.1:9090'], debug=True)
print(mc)
ret=mc.set("test_mc", "{\"a\":123}")
print(ret)
```
## 3 listen kafka show result

java server show this

<img width="1194" alt="image" src="https://user-images.githubusercontent.com/7270440/196459764-830932cd-cf73-47e6-968b-8e3ad54f5a26.png">

other kafka client show this

<img width="556" alt="image" src="https://user-images.githubusercontent.com/7270440/196459887-df3a6fad-ef77-45b2-8c8f-6c78a6427ef1.png">

# resgiste on consul

## 1 run consul

consul agent -dev -ui=true -client 0.0.0.0 &

http://127.0.0.1:8500/ui/dc1/services

## 2 add  config

1. add spring-cloud library
2. add config
```
spring.cloud.consul.host=localhost
spring.cloud.consul.port=8500
spring.cloud.consul.discovery.service-name=ho-java-producer
```

<img width="951" alt="image" src="https://user-images.githubusercontent.com/7270440/196719387-f2a69582-4a0c-4985-8a18-6f6c4be7795a.png">


