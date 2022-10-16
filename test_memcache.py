import memcache
mc = memcache.Client(['127.0.0.1:11222'], debug=True)
print(mc)
ret=mc.set("name", "python")
print(ret)
