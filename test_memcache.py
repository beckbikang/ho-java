import memcache
mc = memcache.Client(['127.0.0.1:11222'], debug=True)
#mc = memcache.Client(['127.0.0.1:9090'], debug=True)
print(mc)
ret=mc.set("test_mc", "{\"a\":123}")
print(ret)
